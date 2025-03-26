/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.UserDAO;
import com.google.gson.Gson;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;

/**
 *
 * @author Ma Tan Loc - CE181795
 */
public class adUserServelt extends HttpServlet {

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

        UserDAO uDAO = new UserDAO();

        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        HttpSession session = request.getSession();
        Integer currentUserId = (Integer) session.getAttribute("userId");

        if (currentUserId == null) {
            String googleId = (String) session.getAttribute("googleId");
            User currentUser = uDAO.getUserByEmail((String) session.getAttribute("email"));

            if (googleId != null) {
                currentUserId = Integer.parseInt(googleId);
            } else if (currentUser != null) {
                currentUserId = currentUser.getUserId();
            }

            session.setAttribute("userId", currentUserId);
        }
        System.out.println("Current User ID: " + currentUserId);

        if (action == null) {
            action = "list";
        }
        if ("list".equals(action)) {
            List<User> user = uDAO.getAllExcept(currentUserId);
            request.setAttribute("listPr", user);
            request.getRequestDispatcher("/WEB-INF/adListUser.jsp").forward(request, response);
        }

        if (idParam == null || idParam.isEmpty()) {
            if ("edit".equals(action)) {
                request.getRequestDispatcher("/WEB-INF/adEditUser.jsp").forward(request, response);
            }
            if ("delete".equals(action)) {
                request.getRequestDispatcher("/WEB-INF/adListUser.jsp").forward(request, response);
            }
        } else {
            if ("edit".equals(action)) {
                int id = Integer.parseInt(idParam);
                User user = uDAO.getUserById(id);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(new Gson().toJson(user));
            }
            if ("delete".equals(action)) {
                int id = Integer.parseInt(idParam);
                User user = uDAO.getUserById(id);
                request.setAttribute("user", user);
                request.getRequestDispatcher("/WEB-INF/adDeleteUser.jsp").forward(request, response);
            }
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

        UserDAO uDAO = new UserDAO();

        String action = request.getParameter("action");
        String idParam = request.getParameter("id");
        int id = Integer.parseInt(idParam);

        if (action.equals("edit")) {
            try {
                int userId = id;
                String fullname = request.getParameter("fullname");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String phone = request.getParameter("phone");
                boolean isAdmin = request.getParameter("isAdmin") != null;
                String street = request.getParameter("street");
                String ward = request.getParameter("ward");
                String district = request.getParameter("district");
                String city = request.getParameter("city");
                String country = request.getParameter("country");

                System.out.println("🔹 Dữ liệu nhận được:");
                System.out.println("userId: " + userId);
                System.out.println("fullname: " + fullname);
                System.out.println("email: " + email);

                User user = new User(userId, fullname, email, password, phone, street, ward, district, city, country, isAdmin);

                try {
                    uDAO.updateUser(user);
                    System.out.println("Cập nhật user thành công!");
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(new Gson().toJson(user));
                } catch (SQLException ex) {
                    System.out.println("Lỗi SQL: " + ex.getMessage());
                    ex.printStackTrace();
                }

                request.setAttribute("user", user);
                response.sendRedirect("User?action=list");
            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().write("error");
            }
        }
        if (action.equals("delete")) {
            uDAO.deleteUser(id);
            response.sendRedirect("User?action=list");
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
