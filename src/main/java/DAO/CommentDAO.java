/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Comment;

/**
 *
 * @author Tran Quoc Thai - CE181618
 */
public class CommentDAO extends db.DBContext {

    public int saveCommentDB(int userId, int productId, String content, int parentId) throws SQLException {
        String sqlNextId = "SELECT ISNULL(MAX(Comment_Id), 0) + 1 FROM Comments";
        int nextId = 0;
        try ( ResultSet rs = execSelectQuery(sqlNextId)) {
            if (rs.next()) {
                nextId = rs.getInt(1);
            }
        }
        String query = "INSERT INTO Comments (Comment_Id,Customer_Id, Product_Id, Parent_Comment_Id, Comment_Text, Created_At) "
                + "VALUES (?,?, ?, ?, ?, GETDATE())";

        Object[] params = {
            nextId,
            userId,
            productId,
            parentId == 0 ? null : parentId, // Nếu không có thì truyền null
            content
        };

        try {
            return execQuery(query, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Integer getCommentId(int userId, int productId) throws SQLException {
        String sql = "SELECT TOP 1 Comment_Id FROM Comments \n"
                + "WHERE Customer_Id = ? AND Product_Id = ? \n"
                + "ORDER BY Created_At DESC";
        Object[] params = {userId, productId};
        ResultSet rs = execSelectQuery(sql, params);
        if (rs.next()) { // Kiểm tra nếu có dòng kết quả trả về
            return rs.getInt("Comment_Id"); // ✅ Lấy comment_id mới nhất
        }
        return null;
    }

    public int getLikeCount(int commentId) throws SQLException {
        int likeCount = 0;
        String sql = "SELECT COUNT(DISTINCT Customer_Id) \n"
                + "FROM Comment_Reactions \n"
                + "WHERE Comment_Id = ?";
        Object[] params = {commentId};
        ResultSet rs = execSelectQuery(sql, params);
        if (rs.next()) { // Kiểm tra nếu có dòng kết quả trả về
            likeCount = rs.getInt(1);
        }
        return likeCount;
    }

    public boolean checkUserLiked(int userId, int commentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Comment_Reactions WHERE Customer_Id = ? AND Comment_Id = ?";

        try ( PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, commentId);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("User " + userId + " like count for comment " + commentId + ": " + count);
                    return count > 0; // Nếu có like, trả về true
                }
            }
        }
        return false;
    }

    public void addLike(int userId, int commentId) throws SQLException {
        String sql = "INSERT INTO Comment_Reactions (Customer_Id, Comment_Id) VALUES (?, ?)";
        Object[] params = {userId, commentId};
        execQuery(sql, params); // Sửa lại thành execQuery()
    }

    public void removeLike(int userId, int commentId) throws SQLException {
        String sql = "DELETE FROM Comment_Reactions \n"
                + "WHERE Customer_Id = ? AND Comment_Id = ? \n"
                + "AND EXISTS (SELECT 1 FROM Comment_Reactions WHERE Customer_Id = ? AND Comment_Id = ?)";

        Object[] params = {userId, commentId, userId, commentId}; // Truyền đủ 4 tham số
        execQuery(sql, params);
    }

    public ArrayList<Comment> getCommentByProductId(int productId, int userId) {
        ArrayList<Comment> comments = new ArrayList<>();
        DateTimeFormatter dbFormatter = DateTimeFormatter.ofPattern("HH:mm:ss yyyy-MM-dd");

        String query = "SELECT \n"
                + "    c.Comment_Id, \n"
                + "    c.Customer_Id, \n"
                + "      -- 🆕 Lấy tên từ bảng Customers\n"
                + "    c.Product_Id, \n"
                + "    CAST(c.Comment_Text AS VARCHAR(MAX)) AS Comment_Text,  \n"
                + "    c.Created_At, \n"
                + "    c.Parent_Comment_Id, \n"
                + "    COUNT(DISTINCT cr.Customer_Id) AS likeCount,\n"
                + "    CASE \n"
                + "        WHEN COUNT(cr2.Customer_Id) > 0 THEN 1 \n"
                + "        ELSE 0 \n"
                + "    END AS isLiked,\n"
                + "	cu.Customer_Name\n"
                + "FROM Comments c  \n"
                + "LEFT JOIN Customers cu ON c.Customer_Id = cu.Customer_Id  -- 🆕 JOIN để lấy Customer_Name\n"
                + "LEFT JOIN Comment_Reactions cr ON c.Comment_Id = cr.Comment_Id  \n"
                + "LEFT JOIN Comment_Reactions cr2 ON c.Comment_Id = cr2.Comment_Id AND cr2.Customer_Id = ?  \n"
                + "WHERE c.Product_Id = ?  \n"
                + "GROUP BY c.Comment_Id, c.Customer_Id, cu.Customer_Name, c.Product_Id, c.Created_At, c.Parent_Comment_Id, CAST(c.Comment_Text AS VARCHAR(MAX))  \n"
                + "ORDER BY c.Created_At ASC;";

        Object[] params = {userId, productId};

        try ( ResultSet rs = execSelectQuery(query, params)) {
            while (rs.next()) {
                Timestamp createdAtTimestamp = rs.getTimestamp("Created_At");
                LocalDateTime createdAt = (createdAtTimestamp != null) ? createdAtTimestamp.toLocalDateTime() : null;

                comments.add(new Comment(
                        rs.getInt("Comment_Id"),
                        rs.getInt("Customer_Id"),
                        rs.getInt("Product_Id"),
                        rs.getInt("Parent_Comment_Id"),
                        rs.getString("Comment_Text"),
                        createdAt,
                        rs.getInt("likeCount"), // Số lượt like
                        rs.getBoolean("isLiked"), // Trạng thái like của user
                        rs.getString("Customer_Name")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comments;
    }

    public boolean updateComment(int commentId, String newContent) {
        try {
            String query = "UPDATE Comments SET Comment_Text = ? WHERE Comment_Id = ?";
            Object[] params = {newContent,commentId, };
            return execQuery(query, params) > 0; // Sửa lại thành execQuery()
        } catch (SQLException ex) {
            Logger.getLogger(CommentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean deleteComment(int commentId) {
    String deleteCommentReactions = "DELETE FROM Comment_Reactions WHERE Comment_Id = ?";
    String deleteComment = "DELETE FROM Comments WHERE Comment_Id = ?";

    try (Connection conn = getConnection();
         PreparedStatement stmt1 = conn.prepareStatement(deleteCommentReactions);
         PreparedStatement stmt2 = conn.prepareStatement(deleteComment)) {

        conn.setAutoCommit(false); // Bắt đầu transaction

        stmt1.setInt(1, commentId);
        stmt1.executeUpdate(); // Xóa phản ứng trước

        stmt2.setInt(1, commentId);
        int rowsDeleted = stmt2.executeUpdate(); // Xóa bình luận

        conn.commit(); // Commit nếu không có lỗi
        return rowsDeleted > 0;

    } catch (SQLException ex) {
        Logger.getLogger(CommentDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
}


}
