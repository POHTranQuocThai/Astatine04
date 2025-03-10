package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Comment {

    private int id;
    private int customerId;
    private int productId;
    private int parentId;
    private String commentText;
    private LocalDateTime createdAt;
    private int likeCount;
    private boolean isLiked;
    private String userName;

    // ✅ Constructor cần có đầy đủ tham số
    public Comment(int id, int customerId, int productId, int parentId, String commentText, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
        this.parentId = parentId;
        this.commentText = commentText;
        this.createdAt = createdAt;
    }

    public Comment(int id, int customerId, int productId, int parentId, String commentText, LocalDateTime createdAt, int likeCount, boolean isLiked, String userName) {
        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
        this.parentId = parentId;
        this.commentText = commentText;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    // Định dạng ngày giờ khi cần hiển thị
    public String getFormattedCreatedAt() {
        return createdAt != null ? createdAt.format(FORMATTER) : "N/A";
    }
}
