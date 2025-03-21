/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.CartDAO;
import DAO.OrderDAO;
import DAO.ProductDAO;
import DAO.TransportDAO;
import DAO.UserDAO;
import DAO.VoucherDAO;
import com.google.gson.JsonObject;
import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import model.Cart;
import model.Products;
import model.User;

/**
 *
 * @author Tran Quoc Thai - CE181618
 */
public class CheckoutServlet extends HttpServlet {

    private static final Map<String, Double> DISTANCE_MAP = new HashMap<>();
    private static final double BASE_FEE = 5000; // Giá cơ bản (VND)
    private static final double FEE_PER_KM = 2000; // Phí mỗi km (VND)
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
        ProductDAO pDAO = new ProductDAO();
        VoucherDAO vDAO = new VoucherDAO();
        UserDAO uDAO = new UserDAO();
        TransportDAO tDAO = new TransportDAO();
        String action = request.getParameter("action");
        String id = request.getParameter("id");
        String voucherId = request.getParameter("voucherId");
        String dis = request.getParameter("dis");
        int userId = 0;
        User user = null; // Các quận trong TP. Cần Thơ
        DISTANCE_MAP.put("Ninh Kiều, Cần Thơ", 5.0);
        DISTANCE_MAP.put("Cái Răng, Cần Thơ", 10.0);
        DISTANCE_MAP.put("Ô Môn, Cần Thơ", 15.0);
        DISTANCE_MAP.put("Thốt Nốt, Cần Thơ", 30.0);
        DISTANCE_MAP.put("Phong Điền, Cần Thơ", 20.0);
        DISTANCE_MAP.put("Cờ Đỏ, Cần Thơ", 40.0);
        DISTANCE_MAP.put("Vĩnh Thạnh, Cần Thơ", 50.0);

        // Các tỉnh gần Cần Thơ
        DISTANCE_MAP.put("Vĩnh Long", 40.0);
        DISTANCE_MAP.put("Đồng Tháp", 80.0);
        DISTANCE_MAP.put("An Giang", 90.0);
        DISTANCE_MAP.put("Kiên Giang", 120.0);
        DISTANCE_MAP.put("Hậu Giang", 30.0);
        DISTANCE_MAP.put("Sóc Trăng", 60.0);
        DISTANCE_MAP.put("Bạc Liêu", 110.0);
        DISTANCE_MAP.put("Cà Mau", 180.0);

        // Các quận tại TP. Hồ Chí Minh
        DISTANCE_MAP.put("Quận 1, TP.HCM", 170.0);
        DISTANCE_MAP.put("Quận 3, TP.HCM", 172.0);
        DISTANCE_MAP.put("Quận 5, TP.HCM", 168.0);
        DISTANCE_MAP.put("Quận 7, TP.HCM", 175.0);
        DISTANCE_MAP.put("Quận 10, TP.HCM", 171.0);
        DISTANCE_MAP.put("Bình Thạnh, TP.HCM", 174.0);
        DISTANCE_MAP.put("Thủ Đức, TP.HCM", 180.0);
        DISTANCE_MAP.put("Gò Vấp, TP.HCM", 176.0);
        DISTANCE_MAP.put("Tân Bình, TP.HCM", 169.0);
        DISTANCE_MAP.put("Bình Tân, TP.HCM", 165.0);

        // Các tỉnh phía sau Cần Thơ
        DISTANCE_MAP.put("Bình Dương", 190.0);
        DISTANCE_MAP.put("Đồng Nai", 200.0);
        DISTANCE_MAP.put("Bình Phước", 230.0);
        DISTANCE_MAP.put("Bà Rịa - Vũng Tàu", 250.0);
        DISTANCE_MAP.put("Lâm Đồng", 320.0);
        DISTANCE_MAP.put("Khánh Hòa", 400.0);
        DISTANCE_MAP.put("Ninh Thuận", 450.0);
        DISTANCE_MAP.put("Bình Thuận", 500.0);
        DISTANCE_MAP.put("TP. Hồ Chí Minh", 170.0);
        DISTANCE_MAP.put("Hà Nội", 1900.0);

        HttpSession session = request.getSession(); // false để không tạo mới nếu không có      
        OrderDAO oDAO = new OrderDAO();
        CartDAO cDAO = (CartDAO) session.getAttribute("SHOP");
        if (session != null) {
            user = (User) session.getAttribute("User");
            if (user != null) {
                userId = user.getUserId();
            }
        }
        double shipPrice = 0;
        String addressUser = "";
        User userInfo = uDAO.getInfoUser(userId);
        if (userInfo != null && userInfo.getDistrict() != null && userInfo.getCity() != null) {
            addressUser = userInfo.getDistrict() + ", " + userInfo.getCity();
            System.out.println("add" + addressUser);
            if (DISTANCE_MAP.containsKey(addressUser)) {
                double distance = DISTANCE_MAP.get(addressUser);
                shipPrice = BASE_FEE + (distance * FEE_PER_KM);
            }
        } else {
            addressUser = "Không xác định";
            shipPrice = BASE_FEE; // Mặc định phí vận chuyển cơ bản
        }

        System.out.println("ttt" + shipPrice);
        if (cDAO == null) {
            cDAO = new CartDAO();
            if (!oDAO.getProductByUserId(userId).isEmpty()) {
                for (Products p : oDAO.getProductByUserId(userId)) {
                    if (p.getStatus().equals("Pending")) {
                        System.out.println("p" + p.getStatus());
                        p.setCountInStock(p.getCountInStock() - p.getQuanOrder());
                        Cart c = new Cart(p, p.getQuanOrder());
                        cDAO.addToCart(c);
                    }
                }
                session.setAttribute("SHOP", cDAO);  // Lưu cDAO vào session

            }
        }

        // Kiểm tra và xử lý hành động "remove"
        if ("remove".equals(action)
                && id != null) {
            try {
                int productId = Integer.parseInt(id);
                int removeOrder = oDAO.removeOrder(userId, productId);
                cDAO.removeProductCart(productId);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID sản phẩm không hợp lệ");
            }
        }

        if (voucherId != null && voucherId != "" || dis != null && dis != "") {
            int discount = Integer.parseInt(dis);
            System.out.println("d" + discount);
            request.setAttribute("applied", "applied");
            request.setAttribute("discount", discount);
            request.setAttribute("voucherId", voucherId);
        }

        // Lấy danh sách order cũ và thêm vào giỏ hàng (chỉ khi chưa có)
        // Thiết lập thuộc tính cho checkout.jsp
        System.out.println("ship" + shipPrice);
        request.setAttribute("brand", pDAO.getAllBrand());
        request.setAttribute("type", pDAO.getAllType());
        request.setAttribute("transport", tDAO.getAllTransport());
        request.setAttribute("vouchers", vDAO.getAllVoucher());
        request.setAttribute("shipPrice", shipPrice);

        request.getRequestDispatcher("/WEB-INF/checkout.jsp").forward(request, response);
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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String id = request.getParameter("id");
        String num = request.getParameter("num");
        int userId = 0;
        HttpSession session = request.getSession(false);
        ProductDAO pDAO = new ProductDAO();

        JsonObject jsonResponse = new JsonObject(); // JSON trả về

        if (session != null) {
            User user = (User) session.getAttribute("User");
            if (user != null) {
                userId = user.getUserId();
            }
        }

        if (id != null && !id.isEmpty() && userId != 0) {
            CartDAO cDAO = (CartDAO) session.getAttribute("SHOP");
            if (cDAO == null) {
                cDAO = new CartDAO();
            }
            Products p = pDAO.getProductById(Integer.parseInt(id));
            if (p != null) {
                String[] image = p.getImage() != null ? p.getImage().split(",") : new String[0];
                p.setImage(image[0]);
                Cart c = new Cart(p, Integer.parseInt(num));
                cDAO.addToCart(c);
            }
            session.setAttribute("SHOP", cDAO);

            // Trả về JSON chứa tổng số sản phẩm trong giỏ
            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("numOrder", cDAO.size());
        } else {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Invalid request");
        }

        out.print(jsonResponse.toString());
        out.flush();
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
