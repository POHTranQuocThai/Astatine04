<%-- 
    Document   : adOrder
    Created on : Nov 3, 2024, 4:47:48 PM
    Author     : Ma Tan Loc - CE181795
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:formatNumber value="${order.totalPrice}" type="number" groupingUsed="true"/>
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
        <link rel="stylesheet" href="assets/css/adOrder.css"/>

        <!-- Icon New-->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200&icon_names=arrow_drop_down" />
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>        

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
                <li class="active">
                    <a href="Order">
                        <i class='bx bxs-cart'></i>                        
                        <span>Order</span>
                    </a>
                </li>
                <li>
                    <a href="Coupon">
                        <i class='bx bxs-discount' ></i>                        
                        <span>Voucher</span>
                    </a>
                </li>
                <li>
                    <a href="Transport">
                        <i class='bx bxs-package'></i>                        
                        <span>Voucher</span>
                    </a>
                </li>
            </ul>
        </nav>

        <!-- Admin User -->   
        <main class="table" id="table">
            <section class="table_header">
                <h1>Order Management.</h1>
                <div class="input-group">
                    <input type="search" id="search" placeholder="Search">
                    <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368"><path d="M380-320q-109 0-184.5-75.5T120-580q0-109 75.5-184.5T380-840q109 0 184.5 75.5T640-580q0 44-14 83t-38 69l224 224q11 11 11 28t-11 28q-11 11-28 11t-28-11L532-372q-30 24-69 38t-83 14Zm0-80q75 0 127.5-52.5T560-580q0-75-52.5-127.5T380-760q-75 0-127.5 52.5T200-580q0 75 52.5 127.5T380-400Z"/></svg>
                </div>
                <div class="account-group">
                    Hi, Admin!
                    <div>
                        <img src="../assets/img/default-avatar.png" width="35px" height="35px" alt="Avatar User"/>
                    </div>                    
                </div>
            </section>
            <section class="table_body">
                <div class="filter-container">
                    <label for="startDate">From:</label>
                    <input type="date" id="startDate">

                    <label for="endDate">To:</label>
                    <input type="date" id="endDate">

                    <button onclick="filterByDate()">Filter</button>

                    <select id="itemsPerPage" onchange="changeItemsPerPage()">
                        <option value="6">Show 6 items</option>
                        <option value="12">Show 12 items</option>
                        <option value="all">Show all</option>
                    </select>
                    <div>
                        <button id="toEXCEL">
                            <i class="bi bi-plus-lg" style="font-style: normal;"> Export </i>                      
                        </button> 
                    </div>
                </div>

                <table>
                    <thead>
                        <tr>
                            <th>#<span class="material-symbols-rounded">arrow_drop_down</span></th>
                            <th>Customer Name<span class="material-symbols-rounded">arrow_drop_down</span></th>
                            <th>Order ID <span class="material-symbols-rounded">arrow_drop_down</span></th>
                            <th>Order Date<span class="material-symbols-rounded">arrow_drop_down</span></th>
                            <th>Total Price<span class="material-symbols-rounded">arrow_drop_down</span></th>
                            <th>Status</th>
                            <th>Function</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${orderList}" var="order" varStatus="status">
                            <tr>
                                <td>${status.index + 1}</td> 
                                <td>${order.customerName}</td>
                                <td>${order.orderId}</td>
                                <td>${order.orderDate}</td>
                                <td><fmt:formatNumber value="${order.totalPrice}" pattern="#,###" /> VNĐ</td>
                                <td>
                                    <select class="status-select ${order.status == 'Delivered' ? 'delivered' : ''} ${order.status == 'Canceled' ? 'canceled' : ''}" 
                                            data-order-id="${order.orderId}"
                                            ${order.status != 'Ordered' ? 'disabled' : ''}>
                                        <option value="Ordered" ${order.status == 'Ordered' ? 'selected' : ''}>Ordered</option>
                                        <option value="Delivered" ${order.status == 'Delivered' ? 'selected' : ''}>Delivered</option>
                                        <option value="Canceled" ${order.status == 'Canceled' ? 'selected' : ''}>Canceled</option>
                                    </select>
                                </td>

                                <td>
                                    <!-- Delete Icon -->
                                    <a href='Order?action=delete&id=${order.orderId}' class="table-btn" style="text-decoration: none;">
                                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368">
                                        <path d="M280-120q-33 0-56.5-23.5T200-200v-520q-17 0-28.5-11.5T160-760q0-17 11.5-28.5T200-800h160q0-17 11.5-28.5T400-840h160q17 0 28.5 11.5T600-800h160q17 0 28.5 11.5T800-760q0 17-11.5 28.5T760-720v520q0 33-23.5 56.5T680-120H280Zm400-600H280v520h400v-520ZM400-280q17 0 28.5-11.5T440-320v-280q0-17-11.5-28.5T400-640q-17 0-28.5 11.5T360-600v280q0 17 11.5 28.5T400-280Zm160 0q17 0 28.5-11.5T600-320v-280q0-17-11.5-28.5T560-640q-17 0-28.5 11.5T520-600v280q0 17 11.5 28.5T560-280ZM280-720v520-520Z"/>
                                        </svg>
                                    </a>
                                    <a href='LoadBill?userId=${order.userId}&orderId=${order.orderId}' class="table-btn" style="text-decoration: none;">
                                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368"><path d="M200-800v241-1 400-640 200-200Zm80 400h140q9-23 22-43t30-37H280v80Zm0 160h127q-5-20-6.5-40t.5-40H280v80ZM200-80q-33 0-56.5-23.5T120-160v-640q0-33 23.5-56.5T200-880h320l240 240v100q-19-8-39-12.5t-41-6.5v-41H480v-200H200v640h241q16 24 36 44.5T521-80H200Zm460-120q42 0 71-29t29-71q0-42-29-71t-71-29q-42 0-71 29t-29 71q0 42 29 71t71 29ZM864-40 756-148q-21 14-45.5 21t-50.5 7q-75 0-127.5-52.5T480-300q0-75 52.5-127.5T660-480q75 0 127.5 52.5T840-300q0 26-7 50.5T812-204L920-96l-56 56Z"/></svg>                                    
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <div id="pagination" class="pagination"></div>
            </section>               
        </main>
    </body>

    <!-- JS Link-->
    <script type="text/javascript" src="./assets/js/JSRemake/adminTable.js"></script>
    <script type="text/javascript" src="../assets/js/Admin/adminOrder.js"></script>
    <script type="text/javascript" src="../assets/js/Admin/selectStatus.js"></script>
</html>
