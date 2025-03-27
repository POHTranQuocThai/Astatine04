/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.VoucherDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;
import model.Voucher;

/**
 *
 * @author Ma Tan Loc - CE181795
 */
public class adCouponServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        VoucherDAO vDAO = new VoucherDAO();
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        if (action == null) {
            action = "list"; // Default action
        }

        if ("list".equals(action)) {
            List<Voucher> list = vDAO.getAllVoucher();
            request.getSession().setAttribute("couponList", list);
            request.getRequestDispatcher("/WEB-INF/adCoupon.jsp").forward(request, response);
            return; // Exit after forwarding
        }

        Voucher voucher = null;
        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                voucher = vDAO.getVoucherById(id);
            } catch (NumberFormatException e) {
                response.sendRedirect("Coupon");
                return;
            }
        }

        if (voucher == null && !"create".equals(action)) {
            response.sendRedirect("Coupon");
            return;
        }

        switch (action) {
            case "edit":
                request.getSession().setAttribute("coupon", voucher);
                request.getRequestDispatcher("/WEB-INF/adCouponEdit.jsp").forward(request, response);
                break;
            case "create":
                request.getRequestDispatcher("/WEB-INF/adCouponCreate.jsp").forward(request, response);
                break;
            case "delete":
                request.getSession().setAttribute("coupon", voucher);
                request.getRequestDispatcher("/WEB-INF/adCouponDelete.jsp").forward(request, response);
                break;
            default:
                response.sendRedirect("Coupon");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        VoucherDAO vDAO = new VoucherDAO();
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");
        LocalDate expiryDate = null;
        if ("create".equals(action)) {
            String name = request.getParameter("voucherName");
            String discountStr = request.getParameter("discount");
            String expiry = request.getParameter("expiry");
            expiryDate = LocalDate.parse(expiry);

            if (name == null || discountStr == null || expiry == null || name.isEmpty() || discountStr.isEmpty() || expiry.isEmpty()) {
                request.getSession().setAttribute("error", "All fields are required.");
                request.getRequestDispatcher("/WEB-INF/adCouponCreate.jsp").forward(request, response);
                return;
            }

            int discount;
            try {
                discount = Integer.parseInt(discountStr);
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("error", "Invalid discount value.");
                request.getRequestDispatcher("/WEB-INF/adCouponCreate.jsp").forward(request, response);
                return;
            }

            Voucher newVoucher = new Voucher(0, name, discount, expiryDate);
            int result = vDAO.createVoucher(newVoucher);
            System.out.println(result);

            if (result > 0) {
                request.getSession().setAttribute("success", "Create voucher successful!");
                response.sendRedirect("Coupon?action=list");
            } else {
                request.getSession().setAttribute("error", "Unable to create voucher");
                request.getRequestDispatcher("/WEB-INF/adCouponCreate.jsp").forward(request, response);
            }
            return;
        }

        if (idParam != null && !idParam.isEmpty()) {
                System.out.println("ac"+action);
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                int voucherId = Integer.parseInt(idParam);
                System.out.println("vv" + id);
                System.out.println("ac"+action);
                if ("delete".equals(action)) {
                    System.out.println("v" + id);
                    int result = vDAO.deleteVoucher(id);
                    System.out.println("t" + result);
                    if (result > 0) {
                        request.getSession().setAttribute("success", "Delete voucher successful!");
                        response.sendRedirect("Coupon?action=list");
                    } else {
                        request.getSession().setAttribute("error", "Unable to delete voucher");
                        request.getRequestDispatcher("/WEB-INF/adCouponDelete.jsp").forward(request, response);
                    }
                }
                if ("edit".equals(action)) {
                    String name = request.getParameter("voucherName");
                    String discountStr = request.getParameter("discount");
                    String expiry = request.getParameter("expiry");
                    expiryDate = LocalDate.parse(expiry);
                    if (name == null || discountStr == null || expiry == null || name.isEmpty() || discountStr.isEmpty() || expiry.isEmpty()) {
                        request.getSession().setAttribute("error", "All fields are required.");
                        request.getRequestDispatcher("/WEB-INF/adCouponEdit.jsp").forward(request, response);
                        return;
                    }

                    int discount;
                    try {
                        discount = Integer.parseInt(discountStr);
                    } catch (NumberFormatException e) {
                        request.getSession().setAttribute("error", "Invalid discount value.");
                        request.getRequestDispatcher("/WEB-INF/adCouponEdit.jsp").forward(request, response);
                        return;
                    }

                    Voucher editedVoucher = new Voucher(voucherId, name, discount, expiryDate);
                    int result = vDAO.editVoucher(editedVoucher);

                    if (result > 0) {
                        request.getSession().setAttribute("success", "Edit voucher successful!");
                        response.sendRedirect("Coupon?action=list");
                    } else {
                        request.getSession().setAttribute("error", "Unable to edit voucher");
                        request.getRequestDispatcher("/WEB-INF/adCouponEdit.jsp").forward(request, response);
                    }
                    return;
                }
                
            } catch (NumberFormatException e) {
                response.sendRedirect("Coupon?action=list");
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
