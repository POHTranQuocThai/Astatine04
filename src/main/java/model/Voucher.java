/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Tran Quoc Thai - CE181618
 */
import java.time.LocalDate;

public class Voucher {
    private int voucherId;
    private String name;
    private int discount;
    private LocalDate expiry;  // Sử dụng LocalDate thay vì String
    private int orderId;

    public Voucher() {
    }

    public Voucher(int voucherId, String name, int discount, LocalDate expiry, int orderId) {
        this.voucherId = voucherId;
        this.name = name;
        this.discount = discount;
        this.expiry = expiry;
        this.orderId = orderId;
    }

    public Voucher(int voucherId, String name, int discount, LocalDate expiry) {
        this.voucherId = voucherId;
        this.name = name;
        this.discount = discount;
        this.expiry = expiry;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public LocalDate getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDate expiry) {
        this.expiry = expiry;
    }

    

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


     
    
}