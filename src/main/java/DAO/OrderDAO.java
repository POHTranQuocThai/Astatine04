
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import db.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Cart;
import model.Order;
import model.Products;
import model.User;

/**
 *
 * @author Tran Quoc Thai - CE181618
 */
public class OrderDAO extends DBContext {

    private int getPendingOrderId(int customerId) throws SQLException {
        String sql = "SELECT TOP 1 Order_Id FROM Orders WHERE Customer_Id = ? AND Status = 'Pending' ORDER BY Order_Date DESC";
        try ( ResultSet rs = execSelectQuery(sql, new Object[]{customerId})) {
            return rs.next() ? rs.getInt("Order_Id") : -1; // Trả về -1 nếu không có đơn hàng
        }

    }

//    private int getNextOrderDetailId() throws SQLException {
//        String sql = "SELECT ISNULL(MAX(Order_Detail_Id), 0) + 1 FROM Order_Details";
//        try ( ResultSet rs = execSelectQuery(sql)) {
//            if (rs.next()) {
//                return rs.getInt(1);
//            }
//        }
//    }
    public int saveCartToDatabase(int customerId, CartDAO cDAO) throws SQLException {
        ProductDAO pDAO = new ProductDAO();
        int rowsAffected = 0;
        String sqlNextId = "SELECT ISNULL(MAX(Order_Id), 0) + 1 as nextId FROM Orders";
        String insertOrderSql = "INSERT INTO Orders (Order_Id, Customer_Id, Order_Date, Total_Price, Status) VALUES (?, ?, GETDATE(), 0, 'Pending')";
        String insertOrderDetail = "INSERT INTO Order_Details (Order_Detail_Id, Product_Id, Order_Id, Quantity, Price) VALUES (?, ?,?,?, ?)";
        String updateOrderDetail = "UPDATE od\n"
                + "SET od.Quantity = ?, od.Price = ?\n"
                + "FROM Order_Details od\n"
                + "JOIN Orders o ON od.Order_ID = o.Order_ID\n"
                + "WHERE od.Product_Id = ?\n"
                + "AND od.Order_Id = ?\n"
                + "AND o.Status = 'Pending';";
        int nextId = 0;
        nextId = getPendingOrderId(customerId);
        int nextOrderDetail = 0;
        String sqlNextDetailId = "SELECT ISNULL(MAX(Order_Detail_Id), 0) + 1 as nextDetailId FROM Order_Details";
        try ( ResultSet rs = execSelectQuery(sqlNextDetailId)) {
            if (rs.next()) {
                nextOrderDetail = rs.getInt("nextDetailId");
            }
        }

        if (nextId == -1) {
            try ( ResultSet rs = execSelectQuery(sqlNextId)) {
                if (rs.next()) {
                    nextId = rs.getInt("nextId");
                }
            }
            execQuery(insertOrderSql, new Object[]{
                nextId,
                customerId
            });

            for (Order order : cDAO.getProductsInCart(customerId)) {
                Products prod = pDAO.getProductById(order.getProductId());
                Object[] insertOrderDetailParam = {
                    nextOrderDetail++, // Order_Detail_Id, tăng nextId cho mỗi sản phẩm mới   
                    order.getProductId(), // Product_ID
                    nextId,
                    order.getAmount(), // Amount           
                    order.getAmount() * prod.getPrice()// TotalPrice
                };
                rowsAffected += execQuery(insertOrderDetail, insertOrderDetailParam);
            }
        } else {

            for (Order order : cDAO.getProductsInCart(customerId)) {
                // Cập nhật nếu sản phẩm đã tồn tại
                Products prod = pDAO.getProductById(order.getProductId());

                Object[] updateParams = {order.getAmount(), order.getAmount() * prod.getPrice(), order.getProductId(), nextId};
                int updatedRows = execQuery(updateOrderDetail, updateParams);

                if (updatedRows == 0) { // Nếu không có dòng nào được cập nhật, thêm mới sản phẩm
                    Object[] insertParams = {
                        nextOrderDetail++, // Order_Detail_Id, tăng nextId cho mỗi sản phẩm mới   
                        order.getProductId(), // Product_ID
                        nextId,
                        order.getAmount(), // Amount           
                        order.getAmount() * prod.getPrice()// TotalPrice
                    };
                    rowsAffected += execQuery(insertOrderDetail, insertParams);
                } else {
                    rowsAffected += updatedRows;
                }
            }
        }

        return rowsAffected;
    }

    public int orderedSuccess(int userId, Order order, CartDAO cDAO, String voucher, String transport) throws SQLException {
        ProductDAO pDAO = new ProductDAO();
        int rowsAffected = 0;
        String sql = "UPDATE Orders SET Street = ?, Ward = ?, District = ?, City = ?, Country = ?,Voucher_Id = ?,Transport_Id = ?, Phone = ?, Order_Date = GETDATE(), Status = ?, Total_Price = ?, Email = ? WHERE Customer_Id = ? AND Status = 'Pending'";
        String updateStock = "UPDATE Products SET Count_In_Stock = ?, Sold = ? WHERE Product_Id = ?";
        Object[] params = {
            order.getStreet(),
            order.getWard(),
            order.getDistrict(),
            order.getCity(),
            order.getCountry(),
            voucher != "" ? Integer.parseInt(voucher) : null,
            transport != "" ? Integer.parseInt(transport) : null,
            order.getPhone(),
            "Ordered",
            order.getTotalPrice(),
            order.getEmail(),
            userId
        };
        try {
            for (Order cartOrder : cDAO.getProductsInCart(userId)) {
                Products prod = pDAO.getProductById(cartOrder.getProductId());
                if (prod != null) {
                    int updatedStock = prod.getCountInStock() - cartOrder.getAmount();
                    int updatedSelled = prod.getSelled() + cartOrder.getAmount();

                    if (updatedStock >= 0) {
                        Object[] updateStockParams = {updatedStock, updatedSelled, cartOrder.getProductId()};
                        int affectedRows = execQuery(updateStock, updateStockParams);
                        System.out.println("Rows affected for updating stock: " + affectedRows);
                        rowsAffected += affectedRows;
                    } else {
                        System.out.println("Insufficient stock for Product_ID: " + cartOrder.getProductId());
                    }

                } else {
                    System.out.println("Product not found for ID: " + cartOrder.getProductId());
                }
            }

            // Thực thi câu lệnh UPDATE cho Orders
            rowsAffected += execQuery(sql, params);
        } catch (SQLException e) {
            e.printStackTrace(); // In ra lỗi nếu có
        }
        return rowsAffected;
    }

    private void deleteEmptyPendingOrders(int userId) throws SQLException {
        String deleteSql = "DELETE FROM Orders WHERE Order_Id IN "
                + "(SELECT o.Order_Id FROM Orders o LEFT JOIN Order_Details od "
                + "ON o.Order_Id = od.Order_Id WHERE o.Customer_Id = ? AND o.Status = 'Pending' "
                + "GROUP BY o.Order_Id HAVING COUNT(od.Order_Detail_Id) = 0)";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(deleteSql)) {
            ps.setInt(1, userId);
            int deletedRows = ps.executeUpdate();
            if (deletedRows > 0) {
                System.out.println("🗑 Xoá " + deletedRows + " đơn hàng Pending trống.");
            }
        }
    }

    public ArrayList<Products> getProductByUserId(int userId) {
        ArrayList<Products> prod = new ArrayList<>();
        String sql = "SELECT p.*, cat.Category_Name ,b.Brand_Name, o.Quantity, os.Status\n"
                + "FROM Products p\n"
                + "				JOIN Brands b ON p.Brand_Id = b.Brand_ID\n"
                + "				JOIN Order_Details o ON o.Product_ID = p.Product_ID\n"
                + "				JOIN Orders os ON os.Order_Id = o.Order_Id\n"
                + "				JOIN Customers c ON c.Customer_ID = os.Customer_Id\n"
                + "				JOIN Categories cat ON p.Category_Id = cat.Category_Id\n"
                + "				WHERE c.Customer_ID = ? and os.Status = 'Pending'";
        Object[] params = {userId};

        try ( ResultSet rs = execSelectQuery(sql, params)) {
            while (rs.next()) {
                // Tách chuỗi hình ảnh
                String[] image = rs.getString(9).split(",");
//                String[] image = (imgData != null) ? imgData.split(",") : new String[]{""};
                // Tạo đối tượng Products và thêm vào danh sách
                Products p = new Products(
                        rs.getInt(1), // Cột ID
                        rs.getString(2), // Cột tên sản phẩm
                        rs.getInt(5), // Cột mô tả
                        rs.getInt(6), // Cột số lượng
                        rs.getDouble(7), // Cột giá
                        image[0], // Mảng hình ảnh từ cột hình ảnh
                        rs.getString(10), // Cột tên thương hiệu
                        rs.getString(11),
                        rs.getString(12),
                        rs.getInt(13)
                );

                // Thiết lập trạng thái (nếu cần)
                p.setStatus(rs.getString(14)); // Đảm bảo tên cột chính xác

                // Thêm sản phẩm vào danh sách
                prod.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace(); // In ra lỗi nếu có
        }

        return prod; // Trả về danh sách sản phẩm
    }

    public int removeOrder(int userId, int prodId) {
        String sql = "DELETE o\n"
                + "FROM Order_Details o\n"
                + "JOIN Orders os ON os.Order_Id = o.Order_Id\n"
                + "WHERE os.Customer_ID = ? AND o.Product_ID = ?";
        Object[] params = {userId, prodId};
        try {
            return execQuery(sql, params);
        } catch (Exception e) {
        }
        return 0;
    }

    public int updateOrder(Order order) {
        String sql = "UPDATE Orders SET Street = ?, Ward = ?, District = ?, City = ?, Country = ?, Status = ? WHERE Order_ID = ?";
        Object[] params = {
            order.getStreet(),
            order.getWard(),
            order.getDistrict(),
            order.getCity(),
            order.getCountry(),
            order.getStatus(),
            order.getOrderId(),};

        try {
            return execQuery(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int deleteOrder(int orderId) {
        String sql = "DELETE FROM Orders WHERE Order_ID = ?";
        Object[] params = {orderId};
        try {
            return execQuery(sql, params);
        } catch (SQLException e) {
        }
        return 0;
    }

    //get Order Table
    public ArrayList<Order> getAllOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        String query = "SELECT o.Order_ID, c.Customer_Name, o.Email, o.Phone, o.Total_Price, o.Street, o.Ward, o.District, o.City, o.Order_Date, o.Status FROM Orders o \n"
                + "JOIN Customers c ON o.Customer_ID = c.Customer_ID \n";

        try ( ResultSet rs = execSelectQuery(query)) {
            while (rs != null && rs.next()) {
                orders.add(new Order(
                        rs.getInt("Order_ID"),
                        rs.getString("Customer_Name"),
                        rs.getDouble("Total_Price"),
                        rs.getString("Street"),
                        rs.getString("Ward"),
                        rs.getString("District"),
                        rs.getString("City"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getDate("Order_Date"),
                        rs.getString("Status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ghi lại lỗi nếu có
        }
        return orders;
    }

    //Admin
    public Order getOrderByOrderId(int orderId) {
        String sql = "SELECT o.Order_ID, c.Customer_Name, o.Email, o.Phone, o.Total_Price, o.Street, o.Ward, o.District, o.City, o.Order_Date, o.Status FROM Orders o \n"
                + "JOIN Customers c ON o.Customer_ID = c.Customer_ID \n"
                + "WHERE o.Order_ID = ?";

        Object[] params = {orderId};
        try ( ResultSet rs = execSelectQuery(sql, params)) {
            if (rs.next()) {
                return new Order(
                        rs.getInt("Order_ID"),
                        rs.getString("Customer_Name"),
                        rs.getDouble("Total_Price"),
                        rs.getString("Street"),
                        rs.getString("Ward"),
                        rs.getString("District"),
                        rs.getString("City"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getDate("Order_Date"),
                        rs.getString("Status")
                );
            }
        } catch (Exception e) {
        }
        return null; // Return null if no order found
    }

    // Returns a list of orders by email
    public List<Order> getOrderByEmail(String email) {
        String sql = "SELECT o.Order_ID, c.Customer_Name, o.Email, o.Phone, o.Total_Price, o.Street, o.Ward, o.District, o.City, o.Order_Date, o.Status FROM Orders o \n"
                + "JOIN Customers c ON o.Customer_ID = c.Customer_ID \n"
                + "WHERE o.Email = ?";
        Object[] params = {email};
        List<Order> orderList = new ArrayList<>();

        try ( ResultSet rs = execSelectQuery(sql, params)) {
            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("Order_ID"),
                        rs.getString("Customer_Name"),
                        rs.getDouble("Total_Price"),
                        rs.getString("Street"),
                        rs.getString("Ward"),
                        rs.getString("District"),
                        rs.getString("City"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getDate("Order_Date"),
                        rs.getString("Status")
                );
                orderList.add(order); // Add order to the list
            }
        } catch (SQLException e) {
            // Handle SQL exception appropriately

        }
        return orderList; // Return the list of orders
    }

    public void updateOrderStatus(int orderId, String status) throws SQLException {
        String query = "UPDATE Orders SET Status = ? WHERE Order_ID = ?";
        Object[] params = {status, orderId};
        execQuery(query, params);
    }

    public List<Order> getProductsInOrder(int orderId) throws SQLException {
        List<Order> orderItems = new ArrayList<>();
        String sql = "SELECT Product_Id, Quantity FROM Order_Details WHERE Order_Id = ?";

        Object[] params = {orderId};

        try ( ResultSet rs = execSelectQuery(sql, params)) {
            while (rs.next()) {
                int productId = rs.getInt("Product_Id");
                int amount = rs.getInt("Quantity"); // Sửa lại "Amount" thành "Quantity" cho đúng với cột trong DB
                orderItems.add(new Order(orderId, productId, amount));
            }
        }
        return orderItems;
    }

    public ArrayList<Order> getAllDataOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        String query = "SELECT od.Quantity,o.Order_Date, o.Status FROM Orders o\n"
                + "JOIN Order_Details od ON o.Order_Id = od.Order_Id\n"
                + "Where o.Status = 'Delivered'";

        try ( ResultSet rs = execSelectQuery(query)) {
            while (rs != null && rs.next()) {
                orders.add(new Order(
                        rs.getInt("Quantity"),
                        rs.getDate("Order_Date"),
                        rs.getString("Status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

//    public ArrayList<Order> checkBill(int orderId) {
//        ArrayList<Order> orders = new ArrayList<>();
//        String query = "SELECT \n"
//                + "    o.Order_Id,\n"
//                + "    o.Email,\n"
//                + "    o.Phone,\n"
//                + "    CONCAT(o.Street, ', ', o.Ward, ', ', o.District, ', ', o.City, ', ', o.Country) AS Address,\n"
//                + "    o.Order_Date,\n"
//                + "    o.Total_Price,\n"
//                + "    o.Status,\n"
//                + "    p.Product_Name,\n"
//                + "    od.Price,\n"
//                + "    od.Quantity,\n"
//                + "    v.Voucher_Name,\n"
//                + "    v.Discount\n"
//                + "FROM [Orders] o\n"
//                + "JOIN Order_Details od ON o.Order_Id = od.Order_Id\n"
//                + "JOIN Products p ON od.Product_Id = p.Product_Id\n"
//                + "LEFT JOIN Vouchers v ON o.Voucher_Id = v.Voucher_Id\n"
//                + "WHERE o.Order_Id = ? AND o.Status = 'Ordered'  ";
//
//        try ( ResultSet rs = execSelectQuery(query, new Object[]{orderId})) {
//            while (rs.next()) {
//                orders.add(new Order(
//                        rs.getInt("Order_Id"),
//                        rs.getString("Email"),
//                        rs.getString("Phone"),
//                        rs.getString("Address"),
//                        rs.getString("Order_Date"),
//                        rs.getDouble("Total_Price"),
//                        rs.getString("Status"),
//                        rs.getString("Product_Name"),
//                        rs.getDouble("Price"),
//                        rs.getInt("Quantity"),
//                        rs.getString("Voucher_Name"),
//                        rs.getDouble("Discount")
//                ));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return orders;
//    }
}
