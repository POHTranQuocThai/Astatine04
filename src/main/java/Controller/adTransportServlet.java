/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.TransportDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Transport;

/**
 *
 * @author Ma Tan Loc - CE181795
 */
public class adTransportServlet extends HttpServlet {

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
        TransportDAO tDao = new TransportDAO();
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        if (action == null) {
            action = "list";
        }

        if ("list".equals(action)) {
            List<Transport> list = tDao.getAllTransport();
            request.setAttribute("transportList", tDao.getAllTransport());
            request.getRequestDispatcher("/WEB-INF/adTransportList.jsp").forward(request, response);
            return;
        }

        Transport transport = null;

        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                transport = tDao.getTransportById(id);
            } catch (NumberFormatException e) {
                response.sendRedirect("Coupon");
                return;
            }
        }

        if (transport == null && !"create".equals(action)) {
            response.sendRedirect("Coupon");
            return;
        }

        switch (action) {
            case "edit":
                request.setAttribute("transport", transport);
                request.getRequestDispatcher("/WEB-INF/adTransportEdit.jsp").forward(request, response);
                break;
            case "create":
                request.getRequestDispatcher("/WEB-INF/adTransportCreate.jsp").forward(request, response);
                break;
            case "delete":
                request.setAttribute("transport", transport);
                request.getRequestDispatcher("/WEB-INF/adTransportDelete.jsp").forward(request, response);

                break;
            default:
                response.sendRedirect("Transport");
        }
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
        TransportDAO tDao = new TransportDAO();
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        if (action.equals("create")) {
            String transportName = request.getParameter("transportName");
            String description = request.getParameter("description");
            Transport transport = new Transport(0, transportName, description);
            int result = tDao.createTransport(transport);
            if (result > 0) {
                System.out.println("Create transport: Name = " + transportName + ", Description = " + description);
                response.sendRedirect("Transport?action=list");
            } else {
                System.out.println("Create failed!");
                request.setAttribute("error", "Failed to create transport");
                request.getRequestDispatcher("/WEB-INF/adTransportCreate.jsp").forward(request, response);
            }
            return;
        }

        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("Transport?action=list");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);

            if (action.equals("edit")) {
                String transportName = request.getParameter("transportName");
                String description = request.getParameter("description");

                System.out.println("Updating transport: ID = " + id + ", Name = " + transportName + ", Description = " + description);

                Transport transport = new Transport(id, transportName, description);
                int result = tDao.updateTransport(transport);
                if (result > 0) {
                    System.out.println("Update successful!");
                    response.sendRedirect("Transport?action=list");
                } else {
                    System.out.println("Update failed!");
                    request.setAttribute("error", "Failed to update transport");
                    request.getRequestDispatcher("/WEB-INF/adTransportEdit.jsp").forward(request, response);
                }
                return;
            }

            if (action.equals("delete")) {
                int result = tDao.deleteTransport(id);
                if (result > 0) {
                    response.sendRedirect("Transport?action=list");
                } else {
                    request.setAttribute("error", "Failed to delete transport");
                    request.setAttribute("transportList", tDao.getAllTransport());
                    request.getRequestDispatcher("/WEB-INF/adTransportList.jsp").forward(request, response);
                }
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("Transport?action=list");
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
