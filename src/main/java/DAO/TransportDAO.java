/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import db.DBContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Transport;

/**
 *
 * @author Cao Toàn Thắng - CE180873
 */
public class TransportDAO extends DBContext {

    // lấy tất cả transport
    public ArrayList<Transport> getAllTransport() {
        ArrayList<Transport> listTransport = new ArrayList<>();
        String query = "Select * From Transports";

        try ( ResultSet rs = execSelectQuery(query)) {
            while (rs.next()) {
                listTransport.add(new Transport(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3)));
            }
        } catch (SQLException e) {
        }
        return listTransport;
    }

    // thêm transport mới
    public int createTransport(Transport t) {
        String query = "Insert Into Transports (Transport_Id, Transport_Name, Description)\n"
                + "VALUES ((SELECT COALESCE(MAX(Transport_Id), 0) + 1 FROM Transports), ?, ?)";
        Object[] params = {
            t.getTransportName(),
            t.getDescription()};

        try {
            return execQuery(query, params);
        } catch (SQLException e) {
        }
        return 0;
    }

    // cập nhật transport
    public int updateTransport(Transport t) {
        String query = "Update Transports Set Transport_Name = ?, Description = ? Where Transport_Id = ?";
        Object[] params = {t.getTransportName(), t.getDescription(), t.getTransportId()};
        try {
            return execQuery(query, params);
        } catch (SQLException e) {
        }
        return 0;
    }

    public int deleteTransport(int id) {
        String updateQuery = "UPDATE Orders SET Transport_Id = NULL WHERE Transport_Id = ?";
        String deleteQuery = "DELETE FROM Transports WHERE Transport_Id = ?";

        try {
            execQuery(updateQuery, new Object[]{id});
            return execQuery(deleteQuery, new Object[]{id});
        } catch (SQLException ex) {
            return 0;
        }

    }

    // tìm theo id
    public Transport getTransportById(int id) {
        String query = "Select * From Transports Where Transport_Id = ?";
        Object[] params = {id};

        try ( ResultSet rs = execSelectQuery(query, params)) {
            if (rs.next()) {
                return new Transport(rs.getString("Transport_Name"),
                        rs.getString("Description"),
                        rs.getInt("Transport_Id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
