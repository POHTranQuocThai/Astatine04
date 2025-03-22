/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.OrderDAO;
import DAO.ProductDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Order;
import model.Products;

/**
 *
 * @author Ha Minh Huy - CE181719
 */
public class LoadBillServlet extends HttpServlet {

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
        String orderId = request.getParameter("orderId");
        String userId = request.getParameter("userId");
        OrderDAO oDAO = new OrderDAO();
        ProductDAO pDAO = new ProductDAO();
        Order order = null;
        if (userId != null && orderId != null) {
            try {
                order = oDAO.getBill(Integer.parseInt(orderId), Integer.parseInt(userId));
                List<Products> list = new ArrayList<>();
                list = pDAO.getInfoBill(Integer.parseInt(userId), Integer.parseInt(orderId));
                System.out.println("order" + order);
                System.out.println("lOr" + list);
                if (order != null && !list.isEmpty()) {
                    request.setAttribute("Bill", order);
                    request.setAttribute("listProd", list);
                    request.getRequestDispatcher("/WEB-INF/orderDetail.jsp").forward(request, response);
                }
            } catch (SQLException ex) {
                Logger.getLogger(LoadBillServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("user id" + userId);
        System.out.println("order id" + orderId);
        response.sendRedirect("Profile?action=order");
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
