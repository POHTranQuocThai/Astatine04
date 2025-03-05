
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import db.DBContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Cart;
import model.Order;
import model.Products;
import model.User;

/**
 *
 * @author Tran Quoc Thai - CE181618
 */
public class OrderDAO extends DBContext {

    public int saveCartToDatabase(int customerId, CartDAO cDAO) throws SQLException {
        ProductDAO pDAO = new ProductDAO();
        int rowsAffected = 0;

        // Tính nextId một lần trước khi vào vòng lặp
        String sqlNextId = "SELECT ISNULL(MAX(order_Id), 0) + 1 as nextId FROM Orders";
        int nextId = 0;
        try ( ResultSet rs = execSelectQuery(sqlNextId)) {
            if (rs.next()) {
                nextId = rs.getInt("nextId");
            }
        }

        String updateOrder = "UPDATE Order_Details SET Quantity = ? WHERE Product_Id = ? AND Order_Id = ?";

        String insertOrder = "NSERT INTO Orders (Customer_Id, Email, Phone, Street, Ward, District, City, Country, \n"
                + "Voucher_Id, Transport_Id, Order_Date, Total_Price, Status) \n"
                + "VALUES (?, ?, ?, ?, ?, ?,?, ?, null, null, ?, ?, ?);";

        String insertOrderDetail = "INSERT INTO Order_Details (Product_Id,Order_ID,Quantity,Price)"
                + "VALUES (?, ?, ?, ?)";
        for (Order order : cDAO.getProductsInCart(customerId)) {
            // Cập nhật nếu sản phẩm đã tồn tại
            Products prod = pDAO.getProductById(order.getProductId());

            Object[] updateParams = {order.getAmount(), order.getProductId(), nextId};
            int updatedRows = execQuery(updateOrder, updateParams);
            System.out.println("updatedRows " + updatedRows);
            if (updatedRows == 0) { // Nếu không có dòng nào được cập nhật, thêm mới sản phẩm
                Object[] insertParams = {
                    nextId, // Order_ID, tăng nextId cho mỗi sản phẩm mới
                    customerId,
                    "", // Email
                    "", // Phone
                    "", // Strees
                    "", // Ward
                    "", // District
                    "", // City
                    "", // Country
                    null, // Voucher_Id (có thể NULL)
                    null, // Transport_Id (có thể NULL)
                    new java.sql.Timestamp(System.currentTimeMillis()), // Order_Date
                    order.getTotalPrice(), // TotalPrice
                    "Pending", // Status
                };
                Object[] insertParamsDetail = {
                    nextId,
                    order.getProductId(),
                    order.getAmount(),
                    order.getAmount()*prod.getPrice()
                };
                rowsAffected += execQuery(insertOrder, insertParams);
                rowsAffected += execQuery(insertOrderDetail, insertParamsDetail);
            } else {
                rowsAffected += updatedRows;
            }
        }

        return rowsAffected;
    }

    public int orderedSuccess(int userId, Order order, CartDAO cDAO) throws SQLException {
        ProductDAO pDAO = new ProductDAO();
        int rowsAffected = 0;

        // Lấy Order_ID lớn nhất hiện có (giả sử mỗi đơn hàng chỉ có một mã duy nhất)
        String sqlOrderId = "SELECT Order_Id FROM Orders WHERE Customer_ID = ? AND status = 'Pending'";
        List<Integer> pendingOrderIds = new ArrayList<>();
        try ( ResultSet rs = execSelectQuery(sqlOrderId, new Object[]{userId})) {
            while (rs.next()) { // 🛠 SỬA LỖI: Lấy tất cả Order_ID, không chỉ lấy 1 cái
                pendingOrderIds.add(rs.getInt(1));
            }
        }

        String sql = "UPDATE Orders SET street = ?, ward = ?, district = ?, city = ?, country = ?, phone = ?, order_date = GETDATE(), status = ?, totalPrice = ?, email = ? WHERE Order_ID = ?";
        String updateStock = "UPDATE products SET countinstock = ?, selled = ? WHERE Product_ID = ?";

        for (int orderId : pendingOrderIds) {
            Object[] orderParams = {
                order.getStreet(),
                order.getWard(),
                order.getDistrict(),
                order.getCity(),
                order.getCountry(),
                order.getPhone(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getEmail(),
                orderId
            };
            rowsAffected += execQuery(sql, orderParams);
            System.out.println("row" + rowsAffected);
        }
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
        } catch (SQLException e) {
            e.printStackTrace(); // In ra lỗi nếu có
        }

        return rowsAffected;
    }

    public ArrayList<Products> getProductByUserId(int userId) {
        ArrayList<Products> prod = new ArrayList<>();
        String sql = "SELECT p.*, b.Brand_Name, o.Quantity, os.status, cat.Category_Name\n"
                + "FROM Products p\n"
                + "JOIN Brands b ON p.Brand_Id = b.Brand_ID\n"
                + "JOIN Order_Details o ON o.Product_ID = p.Product_ID\n"
                + "JOIN Orders os ON os.Order_Id = o.Order_Id\n"
                + "JOIN Customers c ON c.Customer_ID = os.Customer_Id\n"
                + "JOIN Categories cat ON p.Category_Id = cat.Category_Id\n"
                + "WHERE c.Customer_ID = '?'";
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //get Order Table
    public ArrayList<Order> getAllOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        String query = "SELECT o.Order_ID, p.Product_Name, c.Customer_Name, o.City,o.Email, o.Phone, o.Amount, o.Order_Date, o.Status, o.TotalPrice FROM Orders o \n"
                + "JOIN Customers c ON o.Customer_ID = c.Customer_ID \n"
                + "JOIN Products p ON o.Product_ID = p.Product_ID \n"
                + "WHERE o.Status = 'Processing';";

        try ( ResultSet rs = execSelectQuery(query)) {
            while (rs != null && rs.next()) {
                orders.add(new Order(
                        rs.getInt("Order_ID"),
                        rs.getString("Product_Name"),
                        rs.getString("Customer_Name"),
                        rs.getString("City"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getInt("Amount"),
                        rs.getDate("Order_Date"),
                        rs.getString("Status"),
                        rs.getDouble("TotalPrice")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ghi lại lỗi nếu có
        }
        return orders;
    }

    //Admin
    public Order getOrderByOrderId(int orderId) {
        String sql = "SELECT o.Order_ID, c.Customer_Name, p.Product_Name, o.Street, o.Ward, o.District, o.City, o.Country, o.Email, o.Phone, o.Order_Date, o.Status\n"
                + "FROM Orders o\n"
                + "JOIN Customers c ON o.Customer_ID = c.Customer_ID\n"
                + "JOIN Products p ON o.Product_ID = p.Product_ID\n"
                + "WHERE o.Status = 'Processing' AND o.Order_ID = ?;";
        Object[] params = {orderId};

        try ( ResultSet rs = execSelectQuery(sql, params)) {
            if (rs.next()) {
                return new Order(
                        rs.getInt("Order_ID"), // Order ID
                        rs.getString("Customer_Name"), // Customer Name
                        rs.getString("Product_Name"), // Product Name
                        rs.getString("Street"), // Street
                        rs.getString("Ward"), // Ward
                        rs.getString("District"), // District
                        rs.getString("City"), // City
                        rs.getString("Country"), // Country
                        rs.getString("Email"), // Email
                        rs.getString("Phone"), // Phone
                        rs.getDate("Order_Date"), // Order Date
                        rs.getString("Status") // Status
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if no order found
    }

    // Returns a list of orders by email
    public List<Order> getOrderByEmail(String email) {
        String sql = "SELECT o.Customer_ID, p.Product_Name, o.City, o.Email, o.Phone, o.Amount, o.Order_Date, o.TotalPrice, o.Status "
                + "FROM Orders o "
                + "JOIN Products p ON o.Product_ID = p.Product_ID "
                + "WHERE o.Status = 'Processing' AND o.Email = ?;";
        Object[] params = {email};
        List<Order> orderList = new ArrayList<>();

        try ( ResultSet rs = execSelectQuery(sql, params)) {
            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("Customer_ID"), // Customer ID
                        rs.getString("Product_Name"), // Product Name
                        rs.getString("City"), // City
                        rs.getString("Email"), // Email
                        rs.getString("Phone"), // Phone
                        rs.getInt("Amount"), // Amount
                        rs.getDate("Order_Date"), // Order Date
                        rs.getString("Status"), // Status
                        rs.getDouble("TotalPrice") // Total Price
                );
                orderList.add(order); // Add order to the list
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception appropriately
        }
        return orderList; // Return the list of orders
    }

}

