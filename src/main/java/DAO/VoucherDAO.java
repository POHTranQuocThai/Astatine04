/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import db.DBContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Voucher;

/**
 *
 * @author Tran Quoc Thai - CE181618
 */
public class VoucherDAO extends DBContext {

    public ArrayList<Voucher> getAllVoucher() {
        ArrayList<Voucher> voucher = new ArrayList<>();

        // Câu truy vấn SQL để chọn tất cả các cột từ bảng 'products'
        String query = "select * from Vouchers";

        // Thực thi truy vấn và lấy kết quả trả về
        try ( ResultSet rs = execSelectQuery(query)) {
            // Lặp qua từng hàng trong tập kết quả
            while (rs.next()) {
                voucher.add(new Voucher(
                        rs.getInt(1), // Cột ID
                        rs.getString(2), // Cột tên sản phẩm
                        rs.getInt(3),
                        rs.getDate(4).toLocalDate()
                ));
            }
        } catch (Exception e) {
            // Xử lý lỗi nếu có
            e.printStackTrace(); // Hiển thị thông báo lỗi nếu xảy ra
        }
        return voucher; // Trả về danh sách các sản phẩm
    }

    public Voucher getVoucherById(int voucherId) {
        String query = "SELECT * FROM Vouchers WHERE Voucher_ID = ?";
        Object[] params = {voucherId};
        try ( ResultSet rs = execSelectQuery(query, params)) {
            if (rs.next()) {
                return new Voucher(
                        rs.getInt("Voucher_ID"),
                        rs.getString("Voucher_Name"),
                        rs.getInt("Discount"),
                        rs.getDate(4).toLocalDate()
                );
            }
        } catch (Exception e) {
        }
        return null;
    }

    public int createVoucher(Voucher voucher) {
        String query = "INSERT INTO Vouchers (Voucher_ID, Voucher_Name, Discount, Expiry_Date)\n"
                + "VALUES ((SELECT COALESCE(MAX(Voucher_ID), 0) + 1 FROM Vouchers), ?, ?, ?)";
        Object[] params = {
            voucher.getName(),
            voucher.getDiscount(),
            voucher.getExpiry()
        };
        try {
            return execQuery(query, params);
        } catch (SQLException e) {
        }
        return 0;
    }

    public int editVoucher(Voucher voucher) {
        String query = "UPDATE Vouchers SET Voucher_Name = ?, Discount = ?, Expiry_Date = ? WHERE Voucher_ID = ?";
        Object[] params = {
            voucher.getName(),
            voucher.getDiscount(),
            voucher.getExpiry(),
            voucher.getVoucherId()
        };
        try {
            return execQuery(query, params);
        } catch (SQLException e) {
        }
        return 0;
    }

    public int deleteVoucher(int voucherId) {
        String query = "DELETE FROM Vouchers WHERE Voucher_ID = ?";
        Object[] params = {voucherId};
        try {

            return execQuery(query, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
