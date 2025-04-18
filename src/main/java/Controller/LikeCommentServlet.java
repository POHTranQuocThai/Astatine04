/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.CommentDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tran Quoc Thai - CE181618
 */
public class LikeCommentServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LikeCommentServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LikeCommentServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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
        processRequest(request, response);
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

        Gson gson = new Gson();
        JsonObject jsonResponse = new JsonObject();

        try ( PrintWriter out = response.getWriter()) {
            // Đọc dữ liệu từ request
            StringBuilder sb = new StringBuilder();
            try ( BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            System.out.println("Received JSON: " + sb.toString()); // Debug
            JsonObject requestData = gson.fromJson(sb.toString(), JsonObject.class);

            if (!requestData.has("user_id") || !requestData.has("comment_id")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu user_id hoặc comment_id");
                return;
            }

            int userId = requestData.get("user_id").getAsInt();
            int commentId = requestData.get("comment_id").getAsInt();
            System.out.println("com"+ commentId);
            if (userId <= 0 || commentId <= 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu không hợp lệ");
                return;
            }

            CommentDAO comDAO = new CommentDAO();

            // Kiểm tra xem user đã like chưa
            boolean wasLiked = comDAO.checkUserLiked(userId, commentId);
            System.out.println("User " + userId + " đã like comment " + commentId + "? " + wasLiked);

            if (wasLiked) {
                comDAO.removeLike(userId, commentId);
            } else {
                comDAO.addLike(userId, commentId);
            }

            // Lấy số lượng like mới
            int likeCount = comDAO.getLikeCount(commentId);
            System.out.println("Updated like count for comment " + commentId + ": " + likeCount);

            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("liked", !wasLiked);
            jsonResponse.addProperty("like_count", likeCount);
            out.print(gson.toJson(jsonResponse));
            out.flush();

        } catch (SQLException ex) {
            Logger.getLogger(LikeCommentServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi xử lý cơ sở dữ liệu");
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
