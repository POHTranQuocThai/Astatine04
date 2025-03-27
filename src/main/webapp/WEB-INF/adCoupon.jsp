<%-- 
    Document   : adCoupon
    Created on : Nov 3, 2024, 5:59:27 PM
    Author     : Ma Tan Loc - CE181795
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- Tittle Web-->
        <title>Astatine 04 - Admin</title>
        <link rel="shortcut icon" type="image/png" href="assets/img/Tittle-web-icon/Logo_Dark.ico" />

        <!-- Google font -->
        <link href="https://fonts.googleapis.com/css?family=Montserrat:400,500,700" rel="stylesheet">

        <!-- CSS Link -->
        <link rel="stylesheet" href="./assets/css/Admin.css">
        <link rel="stylesheet" href="./assets/css/adOrder.css"/>

        <!-- Icon New-->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200&icon_names=arrow_drop_down" />
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>  
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">

    </head>

    <body>
        <nav id="sidebar">
            <ul>
                <li>
                    <a href="Home" style="padding: 0;" class="logo">
                        <img src="./assets/img/Astatine-white.png" alt="logo"/>
                    </a>
                    <button onclick="toggleSidebar()" id="toggle-btn">
                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368"><path d="m382-480 294 294q15 15 14.5 35T675-116q-15 15-35 15t-35-15L297-423q-12-12-18-27t-6-30q0-15 6-30t18-27l308-308q15-15 35.5-14.5T676-844q15 15 15 35t-15 35L382-480Z" /></svg>
                    </button>
                </li>
                <li>
                    <a href="Dashboard">
                        <i class='bx bxs-dashboard'></i>                        
                        <span>Dashboard</span>
                    </a>
                </li>
                <li>
                    <a href="User">
                        <i class='bx bxs-user'></i>                        
                        <span>User</span>
                    </a>
                </li>
                <li>
                    <a href="Product">
                        <i class='bx bxs-spreadsheet' ></i>
                        <span>Products</span>
                    </a>
                </li>
                <li>
                    <a href="Order">
                        <i class='bx bxs-cart'></i>                        
                        <span>Order</span>
                    </a>
                </li>
                <li class="active">
                    <a href="Coupon">
                        <i class='bx bxs-discount' ></i>                        
                        <span>Voucher</span>
                    </a>
                </li>
                <li>
                    <a href="Transport">
                        <i class='bx bxs-package'></i>                        
                        <span>Transport</span>
                    </a>
                </li>
            </ul>
        </nav>

        <!-- Admin User -->   
        <main class="table">
            <section class="table_header">
                <h1>Coupon Management.</h1>
                <div class="input-group">
                    <input type="search" id="search" placeholder="Search">
                    <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368"><path d="M380-320q-109 0-184.5-75.5T120-580q0-109 75.5-184.5T380-840q109 0 184.5 75.5T640-580q0 44-14 83t-38 69l224 224q11 11 11 28t-11 28q-11 11-28 11t-28-11L532-372q-30 24-69 38t-83 14Zm0-80q75 0 127.5-52.5T560-580q0-75-52.5-127.5T380-760q-75 0-127.5 52.5T200-580q0 75 52.5 127.5T380-400Z"/></svg>
                </div>
                <div class="account-group">
                    Hi, Admin!
                    <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368"><path d="M234-276q51-39 114-61.5T480-360q69 0 132 22.5T726-276q35-41 54.5-93T800-480q0-133-93.5-226.5T480-800q-133 0-226.5 93.5T160-480q0 59 19.5 111t54.5 93Zm246-164q-59 0-99.5-40.5T340-580q0-59 40.5-99.5T480-720q59 0 99.5 40.5T620-580q0 59-40.5 99.5T480-440Zm0 360q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-80q53 0 100-15.5t86-44.5q-39-29-86-44.5T480-280q-53 0-100 15.5T294-220q39 29 86 44.5T480-160Zm0-360q26 0 43-17t17-43q0-26-17-43t-43-17q-26 0-43 17t-17 43q0 26 17 43t43 17Zm0-60Zm0 360Z"/></svg>
                </div>
            </section>
            <section class="table_body">
                <div class="create-btn">
                    <a href = 'Coupon?action=create' class="create-btn--inner">
                        <i class="bi bi-plus-lg" style="font-style: normal;"> Create </i>                      
                    </a> 
                </div>
                <table>
                    <thead>
                        <tr>
                            <th>ID <span class="material-symbols-rounded">arrow_drop_down</span></th>
                            <th>Name <span class="material-symbols-rounded">arrow_drop_down</span></th>
                            <th>Discount <span class="material-symbols-rounded">arrow_drop_down</span></th>
                            <th>Expiration<span class="material-symbols-rounded">arrow_drop_down</span></th>
                            <th>Function</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${couponList}" var="coupon">
                            <tr>
                                <td>${coupon.voucherId}</td>
                                <td>${coupon.name}</td>
                                <td>${coupon.discount}%</td>
                                <td>${coupon.expiry}</td>
                                <td>
                                    <!-- Edit Icon -->
                                    <a href='Coupon?action=edit&id=${coupon.voucherId}' class="table-btn" style="text-decoration: none;">
                                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368">
                                        <path d="M200-200h57l391-391-57-57-391 391v57Zm-40 80q-17 0-28.5-11.5T120-160v-97q0-16 6-30.5t17-25.5l505-504q12-11 26.5-17t30.5-6q16 0 31 6t26 18l55 56q12 11 17.5 26t5.5 30q0 16-5.5 30.5T817-647L313-143q-11 11-25.5 17t-30.5 6h-97Zm600-584-56-56 56 56Zm-141 85-28-29 57 57-29-28Z"/>
                                        </svg>
                                    </a>

                                    <!-- Delete Icon -->
                                    <a href='Coupon?action=delete&id=${coupon.voucherId}' class="table-btn" style="text-decoration: none;">
                                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368">
                                        <path d="M280-120q-33 0-56.5-23.5T200-200v-520q-17 0-28.5-11.5T160-760q0-17 11.5-28.5T200-800h160q0-17 11.5-28.5T400-840h160q17 0 28.5 11.5T600-800h160q17 0 28.5 11.5T800-760q0 17-11.5 28.5T760-720v520q0 33-23.5 56.5T680-120H280Zm400-600H280v520h400v-520ZM400-280q17 0 28.5-11.5T440-320v-280q0-17-11.5-28.5T400-640q-17 0-28.5 11.5T360-600v280q0 17 11.5 28.5T400-280Zm160 0q17 0 28.5-11.5T600-320v-280q0-17-11.5-28.5T560-640q-17 0-28.5 11.5T520-600v280q0 17 11.5 28.5T560-280ZM280-720v520-520Z"/>
                                        </svg>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </section>               
        </main>
    </body>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
    <script type="text/javascript" src="../assets/js/utils/notification.js"></script>
    <script>
                            let notication = '${sessionScope.success}'
                            console.log(notication);
                            if (notication) {
                                showToast(notication, "success")
                            }
    </script>

    <!-- JS Link-->
    <script type="text/javascript" src="./assets/js/JSRemake/adminTable.js"></script>
    <script type="text/javascript" src="./assets/js/JSRemake/adminJS.js"></script>
</html>