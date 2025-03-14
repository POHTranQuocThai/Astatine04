<%-- 
    Document   : orderDetail
    Created on : Mar 11, 2025, 3:02:35 PM
    Author     : Ma Tan Loc - CE181795
--%>

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
                    <p class="date">April 26, 2023</p>
                    <p>Invoice #</p>
                    <p class="invoice-number">BRA-00335</p>
                </div>
            </div>
            <div class="company-info">
                <div class="customer">
                    <p><strong>Nguyen Van A</strong></p>
                    <p>Phone Number: 0995961151</p>
                    <p>Address: 124 Xo viet Nghe Tinh, Quan 1, Ho Chi Minh, Vietnam</p>
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
                    <tr>
                        <td>1.</td>
                        <td>Montly accounting services</td>
                        <td>$150.00</td>
                        <td>1</td>
                        <td>$150.00</td>
                    </tr>
                    <tr>
                        <td>2.</td>
                        <td>Taxation consulting (hour)</td>
                        <td>$60.00</td>
                        <td>2</td>
                        <td>$120.00</td>
                    </tr>
                    <tr>
                        <td>3.</td>
                        <td>Bookkeeping services</td>
                        <td>$50.00</td>
                        <td>1</td>
                        <td>$50.00</td>
                    </tr>
                </tbody>
            </table>
            <div class="totals">
                <p>Total: <strong>$320.00</strong></p>
                <p>Shipping: <strong>$64.00</strong></p>
                <p>Voucher: <strong>20%</strong></p>
                <p class="total">Total: $384.00</p>
            </div>
            <div class="payment-details">
                <p><strong>PAYMENT DETAILS</strong></p>
                <p>Banking or Cash</p>
                <p>Payment Reference: BRA-00335</p>
            </div>
            <div class="footer">
                <p>Astatine04 | Astatine04@info.com </p>
            </div>
        </div>
    </body>
</html>
