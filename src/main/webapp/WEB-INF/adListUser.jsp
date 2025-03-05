<%-- 
    Document   : adListUser
    Created on : Oct 30, 2024, 1:02:04 PM
    Author     : Ma Tan Loc - CE181795
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <jsp:useBean id="a" class="DAO.UserDAO" scope="request"></jsp:useBean>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">

            <!-- Tittle Web-->
            <title>Astatine 04 - Admin</title>
            <link rel="shortcut icon" type="image/png" href="assets/img/Tittle-web-icon/Logo_Dark.ico" />

            <!-- Google font -->
            <link href="https://fonts.googleapis.com/css?family=Montserrat:400,500,700" rel="stylesheet">

            <!-- CSS Link -->
            <link rel="stylesheet" href="./assets/css/Admin.css">
            <link rel="stylesheet" href="./assets/css/AdminStyle.css"/>

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
                    <li class="active">
                        <a href="User">
                            <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#e8eaed"><path d="M480-480q-66 0-113-47t-47-113q0-66 47-113t113-47q66 0 113 47t47 113q0 66-47 113t-113 47ZM160-240v-32q0-34 17.5-62.5T224-378q62-31 126-46.5T480-440q66 0 130 15.5T736-378q29 15 46.5 43.5T800-272v32q0 33-23.5 56.5T720-160H240q-33 0-56.5-23.5T160-240Zm80 0h480v-32q0-11-5.5-20T700-306q-54-27-109-40.5T480-360q-56 0-111 13.5T260-306q-9 5-14.5 14t-5.5 20v32Zm240-320q33 0 56.5-23.5T560-640q0-33-23.5-56.5T480-720q-33 0-56.5 23.5T400-640q0 33 23.5 56.5T480-560Zm0-80Zm0 400Z" /></svg>
                            <span>User</span>
                        </a>
                    </li>
                    <li>
                        <a href="Product">
                            <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368"><path d="M400-200q-17 0-28.5-11.5T360-240q0-17 11.5-28.5T400-280h400q17 0 28.5 11.5T840-240q0 17-11.5 28.5T800-200H400Zm0-240q-17 0-28.5-11.5T360-480q0-17 11.5-28.5T400-520h400q17 0 28.5 11.5T840-480q0 17-11.5 28.5T800-440H400Zm0-240q-17 0-28.5-11.5T360-720q0-17 11.5-28.5T400-760h400q17 0 28.5 11.5T840-720q0 17-11.5 28.5T800-680H400ZM200-160q-33 0-56.5-23.5T120-240q0-33 23.5-56.5T200-320q33 0 56.5 23.5T280-240q0 33-23.5 56.5T200-160Zm0-240q-33 0-56.5-23.5T120-480q0-33 23.5-56.5T200-560q33 0 56.5 23.5T280-480q0 33-23.5 56.5T200-400Zm0-240q-33 0-56.5-23.5T120-720q0-33 23.5-56.5T200-800q33 0 56.5 23.5T280-720q0 33-23.5 56.5T200-640Z" /></svg>
                            <span>Products</span>
                        </a>
                    </li>
                    <li>
                        <a href="Order">
                            <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368"><path d="M280-80q-33 0-56.5-23.5T200-160q0-33 23.5-56.5T280-240q33 0 56.5 23.5T360-160q0 33-23.5 56.5T280-80Zm400 0q-33 0-56.5-23.5T600-160q0-33 23.5-56.5T680-240q33 0 56.5 23.5T760-160q0 33-23.5 56.5T680-80ZM246-720l96 200h280l110-200H246Zm-38-80h590q23 0 35 20.5t1 41.5L692-482q-11 20-29.5 31T622-440H324l-44 80h440q17 0 28.5 11.5T760-320q0 17-11.5 28.5T720-280H280q-45 0-68-39.5t-2-78.5l54-98-144-304H80q-17 0-28.5-11.5T40-840q0-17 11.5-28.5T80-880h65q11 0 21 6t15 17l27 57Zm134 280h280-280Z" /></svg>
                            <span>Order</span>
                        </a>
                    </li>
                    <li>
                        <a href="Coupon">
                            <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368"><path d="M856-390 570-104q-12 12-27 18t-30 6q-15 0-30-6t-27-18L103-457q-11-11-17-25.5T80-513v-287q0-33 23.5-56.5T160-880h287q16 0 31 6.5t26 17.5l352 353q12 12 17.5 27t5.5 30q0 15-5.5 29.5T856-390ZM513-160l286-286-353-354H160v286l353 354ZM260-640q25 0 42.5-17.5T320-700q0-25-17.5-42.5T260-760q-25 0-42.5 17.5T200-700q0 25 17.5 42.5T260-640Zm220 160Zm68 192 112-112q11-11 17.5-26t6.5-32q0-34-24-58t-58-24q-19 0-37.5 11T520-492q-30-28-47-38t-35-10q-34 0-58 24t-24 58q0 17 6.5 32t17.5 26l112 112q12 12 28 12t28-12Z"/></svg>
                            <span>Coupon</span>
                        </a>
                    </li>
                </ul>
            </nav>

            <!-- Admin User -->   
            <main class="table">
                <section class="table_header">
                    <h1>User Management.</h1>
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
                    <table>
                        <thead>
                            <tr>
                                <th>ID <span class="material-symbols-rounded">arrow_drop_down</span></th>
                                <th>Full Name <span class="material-symbols-rounded">arrow_drop_down</span></th>
                                <th>Phone</th>
                                <th>Email</th>
                                <th>City <span class="material-symbols-rounded">arrow_drop_down</span></th>
                                <th>Status</th>
                                <th>Function</th>
                            </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${listPr}" var="user">
                            <tr>
                                <td>${user.userId}</td>
                                <td>${user.fullname}</td>
                                <td>${user.phone}</td>
                                <td>${user.email}</td>
                                <td>${user.city}</td>
                                <td><p class="status ${user.isAdmin ? 'Admin' : 'User'}">${user.isAdmin ? 'Admin' : 'User'}</p></td>
                                <td>
                                    <!-- Edit Icon -->
                                    <a href = 'User?action=edit&id=${user.userId}'" class="table-btn" style="text-decoration: none;">
                                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368">
                                        <path d="M200-200h57l391-391-57-57-391 391v57Zm-40 80q-17 0-28.5-11.5T120-160v-97q0-16 6-30.5t17-25.5l505-504q12-11 26.5-17t30.5-6q16 0 31 6t26 18l55 56q12 11 17.5 26t5.5 30q0 16-5.5 30.5T817-647L313-143q-11 11-25.5 17t-30.5 6h-97Zm600-584-56-56 56 56Zm-141 85-28-29 57 57-29-28Z"/>
                                        </svg>
                                    </a>


                                    <!-- Delete Icon -->
                                    <a href = 'User?action=delete&id=${user.userId}'" class="table-btn" style="text-decoration: none;">
                                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368">
                                        <path d="M280-120q-33 0-56.5-23.5T200-200v-520q-17 0-28.5-11.5T160-760q0-17 11.5-28.5T200-800h160q0-17 11.5-28.5T400-840h160q17 0 28.5 11.5T600-800h160q17 0 28.5 11.5T800-760q0 17-11.5 28.5T760-720v520q0 33-23.5 56.5T680-120H280Zm400-600H280v520h400v-520ZM400-280q17 0 28.5-11.5T440-320v-280q0-17-11.5-28.5T400-640q-17 0-28.5 11.5T360-600v280q0 17 11.5 28.5T400-280Zm160 0q17 0 28.5-11.5T600-320v-280q0-17-11.5-28.5T560-640q-17 0-28.5 11.5T520-600v280q0 17 11.5 28.5T560-280ZM280-720v520-520Z"/>
                                        </svg>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <div class="pagination">
                    <c:if test="${index > 1}">
                        <a href="User?index=1" class="pagination-link"><i class='bx bxs-chevrons-left' ></i></a>
                    </c:if>
                    <c:if test="${index > 1}">
                        <a href="User?index=${index - 1}" class="pagination-link"><i class='bx bxs-chevron-left'></i></a>
                    </c:if>

                    <c:forEach begin="${index > 2 ? index - 2 : 1}" 
                               end="${index + 2 > a.getNumberPage() ? a.getNumberPage() : index + 2}" var="i">
                        <a href="User?index=${i}" class="pagination-link ${index == i ? 'active' : ''}">${i}</a>
                    </c:forEach>

                    <c:if test="${index < a.getNumberPage()}">
                        <a href="User?index=${index + 1}" class="pagination-link"><i class='bx bxs-chevron-right' ></i></i>
                        </a>
                    </c:if>
                    <c:if test="${index < a.getNumberPage()}">
                        <a href="User?index=${a.getNumberPage()}" class="pagination-link"><i class='bx bxs-chevrons-right' ></i></a>
                    </c:if>
                </div>            
            </section>               
        </main>
    </body>
    <!-- JS Link-->
    <script type="text/javascript" src="./assets/js/JSRemake/adminTable.js"></script>
</html>
