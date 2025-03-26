/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.OrderDAO;
import DAO.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Order;
import model.User;

/**
 *
 * @author Ma Tan Loc - CE181795
 */
public class ProfileServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        String email = (String) session.getAttribute("email");

        if (email != null) {
            UserDAO uDao = new UserDAO();
            OrderDAO oDAO = new OrderDAO();
            User user = uDao.getUserByEmail(email);

            if (user != null) {
                if ("password".equals(action)) {
                    request.getSession().setAttribute("user", user);
                    request.getRequestDispatcher("/WEB-INF/profilePassword.jsp").forward(request, response);
                } else if ("edit".equals(action)) {
                    request.getSession().setAttribute("user", user);
                    request.getRequestDispatcher("/WEB-INF/profile.jsp").forward(request, response);
                } else if ("order".equals(action)) {
                    List<Order> orderList = oDAO.getOrderByEmail(email); // Retrieve list of orders
                    request.getSession().setAttribute("orderList", orderList);
                    request.getRequestDispatcher("/WEB-INF/profileOrder.jsp").forward(request, response);
                }
            } else {
                // Optionally, handle the case where the user is not found
            }
        } else {
            response.sendRedirect("Login");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserDAO uDao = new UserDAO();
        String action = request.getParameter("action");
        String email = request.getParameter("email");
        if ("password".equals(action)) {
            String oldPassword = request.getParameter("old_password");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirm_password");

            if (password == null || password.isEmpty() || confirmPassword == null || confirmPassword.isEmpty()) {
                request.getSession().setAttribute("error", "Please enter valid password.");
                request.getRequestDispatcher("/WEB-INF/profilePassword.jsp").forward(request, response);
                return;
            }
            if (password.length() < 6 || confirmPassword.length() < 6) {
                request.getSession().setAttribute("error", "Please enter at least 6 characters.");
                request.getRequestDispatcher("/WEB-INF/profilePassword.jsp").forward(request, response);
                return;
            }
            if (!password.equals(confirmPassword)) {
                request.getSession().setAttribute("error", "Confirm password must be the same as password.");
                request.getRequestDispatcher("/WEB-INF/profilePassword.jsp").forward(request, response);
                return;
            }

            User user = uDao.getUserByEmail(email);
            if (user == null) {
                request.getSession().setAttribute("error", "Can't fint user account.");
                request.getRequestDispatcher("/WEB-INF/profilePassword.jsp").forward(request, response);
                return;
            }

            try {
                String hashedOldPass = uDao.getHashPass(oldPassword);
                if (!user.getPassword().equals(hashedOldPass)) {
                    request.getSession().setAttribute("error", "Old password incorrect.");
                    request.getRequestDispatcher("/WEB-INF/profilePassword.jsp").forward(request, response);
                    return;
                }
            } catch (NoSuchAlgorithmException e) {
            }

            try {
                String hashedPassword = uDao.getHashPass(password); // Hash mật khẩu mới
                user.setPassword(hashedPassword);
                uDao.updatePassword(email, hashedPassword); // Cập nhật mật khẩu đã hash
                request.getSession().setAttribute("successMessage", "Change password successful!");
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(ProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
                request.getSession().setAttribute("error", "Failed to change password successful!");
            }

            request.getRequestDispatcher("/WEB-INF/profilePassword.jsp").forward(request, response);

        } else if ("edit".equals(action)) {
            String fullname = request.getParameter("fullname");
            String phone = request.getParameter("phone");
            String street = request.getParameter("street");
            String ward = request.getParameter("ward");
            String district = request.getParameter("district");
            String city = request.getParameter("city");
            String country = request.getParameter("country");

            if (email != null) {
                try {
                    User user = uDao.getUserByEmail(email);
                    if (user != null) {
                        user.setFullname(fullname);
                        user.setStreet(street);
                        user.setWard(ward);
                        user.setDistrict(district);
                        user.setCity(city);
                        user.setCountry(country);
                        user.setPhone(phone);

                        int result = uDao.updateUser(user);
                        if (result > 0) {
                            request.getSession().setAttribute("successMessage", "Update profile successful!");
                            response.sendRedirect("Profile?action=edit");
                        } else {
                            request.getSession().setAttribute("error", "Failed to update profile.");
                            request.getSession().setAttribute("user", user); // Pass the updated user back
                            request.getRequestDispatcher("/WEB-INF/profile.jsp").forward(request, response);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.sendRedirect("error.jsp");
                }
            } else {
                response.sendRedirect("Login");
            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
