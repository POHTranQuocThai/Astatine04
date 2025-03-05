
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tran Quoc Thai - CE181618
 */
public class SignupServlet extends HttpServlet {

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
        request.getRequestDispatcher("WEB-INF/signup.jsp").forward(request, response);
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
        try {
            String fullname = request.getParameter("fullname");
            String email = request.getParameter("email");
            String pass = request.getParameter("password");
            UserDAO uDAO = new UserDAO();
            System.out.println("f"+fullname);
            System.out.println("f"+email);
            System.out.println("f"+pass);

            boolean existsEmail = uDAO.checkEmail(email);

            if (existsEmail) {
                request.setAttribute("existsEmail", "The email already exists!!");
                request.setAttribute("fullname", fullname);
                request.setAttribute("email", email);
                request.getRequestDispatcher("WEB-INF/signup.jsp").forward(request, response);
                return;
            }

            String hashedPass = uDAO.getHashPass(pass);

            if (uDAO.signup(fullname, email, hashedPass) != null) {
                request.setAttribute("successMes", "You have successfully registered!");
                request.getRequestDispatcher("WEB-INF/login.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMes", "Sign Up failed!");
                request.getRequestDispatcher("WEB-INF/signup.jsp").forward(request, response);
            }
        } catch (SQLException | NoSuchAlgorithmException ex) {
            Logger.getLogger(SignupServlet.class.getName()).log(Level.SEVERE, null, ex);
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
