/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.OrderDAO;
import DAO.ProductDAO;
import com.google.gson.Gson;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Order;
import model.Products;

/**
 *
 * @author Ma Tan Loc - CE181795
 */
public class OrderDataServlet extends HttpServlet {

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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        OrderDAO oDAO = new OrderDAO();
        ProductDAO pDAO = new ProductDAO();
        List<Order> orderList = oDAO.getAllDataOrders();
        List<Products> productList = pDAO.getTop5Selled();

        // Chuyển đổi danh sách Order thành JSON với timestamp
        List<Map<String, Object>> jsonList = new ArrayList<>();
        for (Order order : orderList) {
            Map<String, Object> jsonObj = new HashMap<>();
            jsonObj.put("amount", order.getAmount());
            jsonObj.put("timestamp", order.getOrderDate().getTime()); // Chuyển đổi Date -> timestamp
            jsonObj.put("status", order.getStatus());
            jsonList.add(jsonObj);
        }

        // Chuyển đổi danh sách Product thành JSON
        List<Map<String, Object>> jsonProductList = new ArrayList<>();
        for (Products product : productList) {
            Map<String, Object> jsonObj = new HashMap<>();
            jsonObj.put("label", product.getProductName());
            jsonObj.put("y", product.getSelled());
            jsonProductList.add(jsonObj);
        }

        // Đặt vào một Map để có key phân biệt
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("orders", jsonList);
        resultMap.put("products", jsonProductList);

        // Chuyển đổi Map thành JSON và gửi về response
        String jsonResponse = new Gson().toJson(resultMap);
        response.getWriter().write(jsonResponse);
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
