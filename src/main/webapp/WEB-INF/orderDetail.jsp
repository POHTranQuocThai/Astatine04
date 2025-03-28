<%-- 
    Document   : orderDetail
    Created on : Mar 11, 2025, 3:02:35 PM
    Author     : Ma Tan Loc - CE181795
--%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- Tittle Web-->
        <title>Astatine 04 | Order Detail</title>
        <link rel="shortcut icon" type="image/png" href="assets/img/Tittle-web-icon/Logo_Dark.ico" />

        <!-- Google font -->
        <link href="https://fonts.googleapis.com/css?family=Montserrat:400,500,700" rel="stylesheet">

        <!-- CSS Link -->
        <link rel="stylesheet" href="assets/css/orderDetail.css">

        <!-- Icon -->
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <div class="brand">
                    <a href="Home" style="padding: 0;" class="logo">
                        <img src="assets/img/Logo_group.png" alt="logo"/>
                    </a>
                </div>
                <div class="invoice-info">
                    <p>Date</p>
                    <p class="date">${Bill.orderDate}</p>
                    <p>Invoice #</p>
                    <p class="invoice-number">BRA-${Bill.orderId}</p>
                </div>
            </div>
            <div class="company-info">
                <div class="customer">
                    <p><strong>${Bill.customerName}</strong></p>
                    <p>Phone Number: ${Bill.phone}</p>
                    <p>Address: ${Bill.street}, ${Bill.ward}, ${Bill.district}, ${Bill.city}, ${Bill.country}</p>
                </div>
                <div style="clear: both;"></div>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Product Name</th>
                        <th>Price</th>
                        <th>Qty.</th>
                        <th>Total Price</th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="total" value="0"></c:set>
                    <c:forEach items="${listProd}" var="list" varStatus="status">
                        <tr>
                            <td>${status.index + 1}.</td>
                            <td>${list.productName}</td>
                            <td><fmt:formatNumber value="${list.price}" pattern="#,###" /> VND</td>
                            <td>${list.amount}</td>
                            <td><fmt:formatNumber value="${list.price * list.amount}" pattern="#,###" /> VND</td>
                        </tr>
                        <c:set var="total" value="${list.price * list.amount + total}"></c:set>
                    </c:forEach>
                </tbody>
            </table>
            <div class="totals">
                <p>Total: <strong><fmt:formatNumber value="${total}" pattern="#,###" /> VND</strong></p>
                <p>Shipping: <strong>${Bill.shipping}</strong></p>
                <p>Voucher: <strong><fmt:formatNumber value="${Bill.discount}" pattern="#,###" />%</strong></p>
                <p class="total">Total: <fmt:formatNumber value="${Bill.totalPrice}" pattern="#,###" /> VND</p>
            </div>
            <div class="payment-details">
                <p><strong>PAYMENT DETAILS</strong></p>
                <p>${Bill.payment}</p>
            </div>
            <div class="payment-details transport">
                <p><strong>TRANSPORTS</strong></p>
                <p>${Bill.transportName}</p>
            </div>
            <div class="footer">
                <p>Astatine04 | Astatine04@info.com </p>
            </div>
        </div>
    </body>
</html>
