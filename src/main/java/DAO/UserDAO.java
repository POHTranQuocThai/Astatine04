
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import db.DBContext;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.User;

/**
 *
 * @author Tran Quoc Thai - CE181618
 */
public class UserDAO extends DBContext {

    public static void main(String[] args) {
        UserDAO aO = new UserDAO();

    }

    //Ham kiem tra dang nhap
    public boolean login(String email, String password) throws SQLException {
        try {
            String sql = "select count(customer_id) from customers where email = ? and password = ?";
            Object[] params = {email, password};
            ResultSet rs = execSelectQuery(sql, params);
            if (rs.next()) {
                return rs.getInt(1) == 1;
            }
            return true;
        } catch (SQLException sQLException) {
        }
        return false;
    }

    public User signup(String fullname, String email, String password) throws SQLException {
        String getNextIdQuery = "SELECT COALESCE(MAX(Customer_ID), 0) + 1 AS nextId FROM Customers";
        User u = new User();
        // Câu lệnh SQL chèn dữ liệu vào bảng customers
        try ( ResultSet rs = execSelectQuery(getNextIdQuery)) {
            if (rs.next()) {
                int nextId = rs.getInt(1);
                u.setUserId(nextId);
                u.setFullname(fullname);
                u.setEmail(email);
                u.setPassword(password);
                String sql = "INSERT INTO customers VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                Object[] params = {nextId, fullname, email, password, "", "", "", "", "", "", 0};
                System.out.println("SQL: " + sql);
                System.out.println("Params: " + Arrays.toString(params));
                int rowsAffected = execQuery(sql, params);
                System.out.println("Rows affected: " + rowsAffected);

                if (rowsAffected > 0) {
                    return u;
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // Nên in lỗi cụ thể ra để debug
        }
        return null;
    }

    public boolean checkFullname(String fullname) {
        try {
            String sql = "select customer_name from customers where customer_name = ?";
            Object[] params = {fullname};
            ResultSet rs = execSelectQuery(sql, params);
            if (rs.next()) { // Kiểm tra nếu có dòng kết quả trả về
                return true; // Nếu có kết quả trả về, fullname đã tồn tại
            }
            return false; // Nếu không có kết quả, fullname chưa tồn tại
        } catch (SQLException sQLException) {
            sQLException.printStackTrace(); // In lỗi ra nếu có ngoại lệ
        }
        return false; // Mặc định trả về false nếu có lỗi hoặc không có kết quả
    }

    public boolean checkEmail(String email) {
        try {
            String sql = "select email from customers where email = ?";
            Object[] params = {email};
            ResultSet rs = execSelectQuery(sql, params);
            if (rs.next()) { // Kiểm tra nếu có dòng kết quả trả về
                return true; // Nếu có kết quả trả về, email đã tồn tại
            }
            return false; // Nếu không có kết quả, email chưa tồn tại
        } catch (SQLException sQLException) {
            sQLException.printStackTrace(); // In lỗi ra nếu có ngoại lệ
        }
        return false; // Mặc định trả về false nếu có lỗi hoặc không có kết quả
    }

    public String getHashPass(String password) throws
            NoSuchAlgorithmException {
        String plainText = password;
        MessageDigest mdAlgorithm = MessageDigest.getInstance("MD5");
        mdAlgorithm.update(plainText.getBytes());

        byte[] digest = mdAlgorithm.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            plainText = Integer.toHexString(0xFF & digest[i]);

            if (plainText.length() < 2) {
                plainText = "0" + plainText;
            }

            hexString.append(plainText);
        }
        return hexString.toString();
    }

    public int getUserId(String email) {
        try {
            String sql = "select customer_ID from customers where email = ?";
            Object[] params = {email};
            ResultSet rs = execSelectQuery(sql, params);
            if (rs.next()) { // Kiểm tra nếu có dòng kết quả trả về
                return rs.getInt(1); // Nếu có kết quả trả về, email đã tồn tại
            }
        } catch (SQLException sQLException) {
            sQLException.printStackTrace(); // In lỗi ra nếu có ngoại lệ
        }
        return 0; // Mặc định trả về false nếu có lỗi hoặc không có kết quả
    }

    public boolean checkIsAdmin(String email) {
        try {
            String sql = "select isAdmin from customers where email = ?";
            Object[] params = {email};
            ResultSet rs = execSelectQuery(sql, params);
            if (rs.next()) { // Kiểm tra nếu có dòng kết quả trả về
                return rs.getBoolean(1); // Nếu có kết quả trả về, email đã tồn tại
            }
        } catch (SQLException sQLException) {
            sQLException.printStackTrace(); // In lỗi ra nếu có ngoại lệ
        }
        return false; // Mặc định trả về false nếu có lỗi hoặc không có kết quả
    }

    public User getInfoUser(int userId) {
        String sql = "select * from Customers where customer_Id = ?";
        Object[] params = {userId};
        try ( ResultSet rs = execSelectQuery(sql, params)) {
            if (rs.next()) {
                return new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getBoolean(11));
            }
        } catch (Exception e) {
        }
        return null;
    }

    public boolean checkInfoUser(User user) {
        if ((user.getStreet().isEmpty() || user.getStreet() == null) || (user.getWard().isEmpty() || user.getWard() == null) || (user.getCity() == null || user.getCity().isEmpty()) || (user.getDistrict() == null || user.getDistrict().isEmpty())
                || (user.getCountry() == null || user.getCountry().isEmpty()) || (user.getPhone() == null || user.getPhone().isEmpty())) {
            return false;
        }
        return true;
    }

    public ArrayList<User> getAll() {
        ArrayList<User> user = new ArrayList<>();

        String query = "select * from Customers";

        try ( ResultSet rs = execSelectQuery(query)) {
            while (rs.next()) {
                user.add(new User(
                        rs.getInt(1), // ID
                        rs.getString(2), // Name
                        rs.getString(3), // Street
                        rs.getString(4), // Ward
                        rs.getString(5), // District
                        rs.getString(6), // City
                        rs.getString(7), // Country
                        rs.getString(8), // Password(encrypt)
                        rs.getString(9), // Email
                        rs.getString(10), // Contacts                                      
                        rs.getBoolean(11) // Check Admin(T/F)                                        
                ));
            }
        } catch (Exception e) {
            // Xử lý lỗi nếu có
            e.printStackTrace(); // Hiển thị thông báo lỗi nếu xảy ra
        }
        return user; // Trả về danh sách các sản phẩm
    }

    // Retrieve a user by ID
    public User getUserById(int id) {
        User user = null;
        String query = "SELECT * FROM Customers WHERE Customer_ID = ?";
        Object[] params = {id};

        try ( ResultSet rs = execSelectQuery(query, params)) {
            if (rs.next()) {
                user = new User(
                        rs.getInt(1), // ID
                        rs.getString(2), // Fullname
                        rs.getString(3), // Street
                        rs.getString(4), // Ward
                        rs.getString(5), // District
                        rs.getString(6), // City
                        rs.getString(7), // Country
                        rs.getString(8), // Password (hashed)
                        rs.getString(9), // Email
                        rs.getString(10), // Contacts
                        rs.getBoolean(11) // Admin flag
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log lỗi
        }
        return user;
    }

    public int updateUser(User user) throws SQLException {
        String query = "UPDATE Customers SET customer_name = ?, street = ?, ward = ?, district = ?, city = ?, country = ?, password = ?, email = ?, phone = ?, isAdmin = ? WHERE customer_id = ?";

        Object[] params = {
            user.getFullname(), // customer_name
            user.getStreet(), // street
            user.getWard(), // ward
            user.getDistrict(), // district
            user.getCity(), // city
            user.getCountry(), // country
            user.getPassword(), // password
            user.getEmail(), // email
            user.getPhone(), // phone (updated from 'contacts')
            user.isIsAdmin(), // isAdmin flag
            user.getUserId() // customer_id
        };

        // Execute the update and return affected rows
        return execQuery(query, params);
    }

    public void updatePassword(String email, String password) {
        String sql = "UPDATE Customers\n"
                + "   SET Password = ?\n"
                + " WHERE Email = ?";
        Object[] params = {password, email};
        try {
            int rowsUpdated = execQuery(sql, params);
            if (rowsUpdated > 0) {
                System.out.println("Cập nhật mật khẩu thành công.");
            } else {
                System.out.println("Không tìm thấy email để cập nhật.");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật mật khẩu: " + e.getMessage());
        }
    }

    public int deleteUser(int id) {
        String deleteTokensQuery = "DELETE FROM tokenForgetPassword WHERE userId = ?";
        String deleteUserQuery = "DELETE FROM customers WHERE customer_id = ?";

        Object[] params = {id};

        try {
            execQuery(deleteTokensQuery, params);

            return execQuery(deleteUserQuery, params);
        } catch (SQLException ex) {
            return 0;
        }
    }

    // Method to get User by email
    public User getUserByEmail(String email) {
        User user = null;
        String query = "SELECT * FROM Customers WHERE email = ?";

        try ( ResultSet rs = execSelectQuery(query, new Object[]{email})) {
            if (rs.next()) {
                user = new User(
                        rs.getInt("Customer_ID"), // ID
                        rs.getString("customer_name"), // Fullname
                        rs.getString("email"), // Street
                        rs.getString("password"), // Ward
                        rs.getString("phone"), // District
                        rs.getString("street"), // City
                        rs.getString("ward"), // Country
                        rs.getString("district"), // Password (hashed)
                        rs.getString("city"), // Email
                        rs.getString("country"), // Avatar
                        rs.getBoolean("isAdmin") // Admin flag
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL errors
        }
        return user;
    }

    public int getNumberPage() {
        String query = "Select count(*) from Customers";
        try ( ResultSet rs = execSelectQuery(query)) {
            while (rs.next()) {
                int total = rs.getInt(1);
                int countPage = 0;
                countPage = total / 6;
                if (total % 6 != 0) {
                    countPage++;
                }
                return countPage;
            }
        } catch (Exception e) {
        }
        return 0;
    }

    public List<User> getPagingAd(int index) throws SQLException {
        String query = "SELECT C.* \n"
                + "FROM Customers C\n"
                + "ORDER BY C.Customer_ID\n"
                + "OFFSET ? ROWS \n"
                + "FETCH FIRST 6 ROWS ONLY;";
        List<User> list = new ArrayList<>();
        try ( PreparedStatement ps = getConnection().prepareStatement(query)) {

            ps.setInt(1, (index - 1) * 6);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new User(
                        rs.getInt(1), // ID
                        rs.getString(2), // Fullname
                        rs.getString(3), // Street
                        rs.getString(4), // Ward
                        rs.getString(5), // District
                        rs.getString(6), // City
                        rs.getString(7), // Country
                        rs.getString(8), // Password (hashed)
                        rs.getString(9), // Email
                        rs.getString(10), // Contacts
                        rs.getBoolean(11) // Admin flag
                ));
            }
            return list;
        } catch (Exception e) {
        }
        return null;
    }

    public int createGoogleUser(User user) throws SQLException {
        String createGoogleUser = "INSERT INTO Customers (Customer_ID, Customer_Name, Email, Password) "
                + "VALUES ((SELECT COALESCE(MAX(Customer_ID), 0) + 1 FROM Customers), ?, ?, ?)";

        Object[] params = {
            user.getFullname(),
            user.getEmail(),
            user.getPassword(),};

        try {
            return execQuery(createGoogleUser, params);
        } catch (SQLException ex) {
            System.out.println("Error while creatinguser: " + ex.getMessage());
        }
        return 0;
    }

}
