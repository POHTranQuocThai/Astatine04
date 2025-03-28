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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Order;

/**
 *
 * @author Ma Tan Loc - CE181795
 */
public class adOrderServlet extends HttpServlet {

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
        OrderDAO oDAO = new OrderDAO();

        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        if (action == null) {
            action = "list";
        }

        if ("list".equals(action)) {
            List<Order> list = oDAO.getAllOrders();
            request.setAttribute("orderList", list);
            request.getRequestDispatcher("/WEB-INF/adOrder.jsp").forward(request, response);
        }

        if ("delete".equals(action) && idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                Order order = oDAO.getOrderByOrderId(id);

                if (order == null) {
                    // Nếu không tìm thấy đơn hàng với ID, chuyển hướng về trang danh sách
                    response.sendRedirect("Order?action=list");
                    return;
                }

                // Hiển thị thông tin đơn hàng lên form edit
                request.setAttribute("order", order);
                request.getRequestDispatcher("/WEB-INF/adOrderDelete.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                // Nếu ID không hợp lệ, chuyển hướng về trang danh sách
                response.sendRedirect("Order");
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
        OrderDAO oDAO = new OrderDAO();
        ProductDAO pDAO = new ProductDAO();
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        try {
            if ("delete".equals(action) && idParam != null && !idParam.isEmpty()) {
                int orderId = Integer.parseInt(idParam);
                oDAO.deleteOrder(orderId);
                response.sendRedirect("Order");
                return;
            }

            if ("updateStatus".equals(action) && idParam != null && !idParam.isEmpty()) {
                int orderId = Integer.parseInt(idParam);
                String status = request.getParameter("status");

                if (status != null && !status.isEmpty()) {
                    if ("Canceled".equals(status)) {
                        // Lấy danh sách sản phẩm từ đơn hàng
                        List<Order> orderItems = oDAO.getProductsInOrder(orderId);
                        for (Order item : orderItems) {
                            pDAO.returnProductStock(item.getProductId(), item.getAmount());
                        }
                    }

                    // Cập nhật trạng thái đơn hàng
                    oDAO.updateOrderStatus(orderId, status);
                    response.getWriter().write("Success: Order status updated.");
                } else {
                    response.getWriter().write("Error: Invalid status.");
                }
            }
        } catch (SQLException | NumberFormatException ex) {
            Logger.getLogger(adOrderServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write("Error processing request.");
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