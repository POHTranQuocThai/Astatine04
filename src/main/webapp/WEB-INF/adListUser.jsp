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
            <link rel="stylesheet" href="./assets/css/adModal.css"/>

            <!-- Icon New-->
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
            <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200&icon_names=arrow_drop_down" />
            <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>    
            
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
            <script src="https://cdn.jsdelivr.net/npm/toastify-js"></script>

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
                    <li class="active">
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
                    <li>
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
                                    <a data-user-id="${user.userId}" class="table-btn edit" style="text-decoration: none; cursor: pointer">
                                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368">
                                        <path d="M200-200h57l391-391-57-57-391 391v57Zm-40 80q-17 0-28.5-11.5T120-160v-97q0-16 6-30.5t17-25.5l505-504q12-11 26.5-17t30.5-6q16 0 31 6t26 18l55 56q12 11 17.5 26t5.5 30q0 16-5.5 30.5T817-647L313-143q-11 11-25.5 17t-30.5 6h-97Zm600-584-56-56 56 56Zm-141 85-28-29 57 57-29-28Z"/>
                                        </svg>
                                    </a>


                                    <!-- Delete Icon -->
                                    <a data-user-id="${user.userId}" class="table-btn delete" style="text-decoration: none; cursor: pointer">
                                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368">
                                        <path d="M280-120q-33 0-56.5-23.5T200-200v-520q-17 0-28.5-11.5T160-760q0-17 11.5-28.5T200-800h160q0-17 11.5-28.5T400-840h160q17 0 28.5 11.5T600-800h160q17 0 28.5 11.5T800-760q0 17-11.5 28.5T760-720v520q0 33-23.5 56.5T680-120H280Zm400-600H280v520h400v-520ZM400-280q17 0 28.5-11.5T440-320v-280q0-17-11.5-28.5T400-640q-17 0-28.5 11.5T360-600v280q0 17 11.5 28.5T400-280Zm160 0q17 0 28.5-11.5T600-320v-280q0-17-11.5-28.5T560-640q-17 0-28.5 11.5T520-600v280q0 17 11.5 28.5T560-280ZM280-720v520-520Z"/>
                                        </svg>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <div class="modal delete" id="deleteUserModal">
                    <div class="modal-box" style="padding: 0">
                        <form action="User?action=delete" method="post">
                            <h1>Delete User</h1>
                            <input type="hidden" name="id" value="${user.userId}">
                            <img src="../assets/img/Gif/brain.gif" alt="alt" style="width: 100px; height: 100px; margin: auto 0;"/>

                            <div class="modal-body">
                                <p>Do you want to delete <span style="font-weight: 500; color: #21A691">${user.fullname}</span>?</p>
                            </div>

                            <div class="center-bottom-delete">
                                <!-- Save Button -->
                                <button class="submit-btn" id="save">Save</button>
                                <!-- Close Button -->
                                <button class=" close-btn" id="close">Close</button>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="modal" id="editUserModal">
                    <div class="modal-box"> 
                        <div class="form">
                            <h1>Edit User</h1>
                            <form id="editUserForm" action="User?action=edit&id=${user.userId}" method="post">
                                <input type="hidden" name="action" value="edit">
                                <input type="hidden" name="id" id="userId" value="${user.userId}">
                                <input type="hidden" id="password" name="password" value="${user.password}">
                                <input type="hidden" id="country" name="country" value="${user.country}">
                                <div class="label-form-top"><h3>Personal Information</h3></div>
                                <div class="center-bottom user">
                                    <div class="top-left">
                                        <!-- Full Name -->
                                        <label for="fullname">Full Name:</label>
                                        <input type="text" id="fullname" name="fullname" value="${user.fullname}" placeholder="Full Name">
                                        <div class="admin-check">
                                            <!-- Admin Flag -->
                                            <label for="isAdmin">Admin:</label>
                                            <input type="checkbox" id="isAdmin" name="isAdmin" ${user.isAdmin ? 'checked' : ''}>
                                        </div>
                                    </div>
                                    <div class="top-right">
                                        <!-- Email -->
                                        <label for="email">Email:</label>
                                        <input type="email" id="email" name="email" value="${user.email}" placeholder="Enail" readonly>                          
                                        <!-- Phone -->
                                        <label for="phone">Phone:</label>
                                        <input type="text" id="phone" name="phone" value="${user.phone}" placeholder="Phone">
                                    </div>
                                </div>

                                <div class="label-form-bottom"><h3>Address</h3></div>
                                <div class="center-bottom user">
                                    <div class="top-left">
                                        <!-- Street -->
                                        <label for="street">Street:</label>
                                        <input type="text" id="street" name="street" value="${user.street}" placeholder="Street">

                                        <!-- Ward -->
                                        <label for="ward">Ward:</label>
                                        <input type="text" id="ward" name="ward" value="${user.ward}" placeholder="Ward">
                                    </div>
                                    <div class="top-right">
                                        <!-- District -->
                                        <label for="district">District:</label>
                                        <input type="text" id="district" name="district" value="${user.district}" placeholder="District">

                                        <!-- City -->
                                        <label for="city">City:</label>
                                        <input type="text" id="city" name="city" value="${user.city}" placeholder="City">
                                    </div>
                                </div>
                                <div class="center-bottom-delete">
                                    <!-- Save Button -->
                                    <button class="submit-btn" id="save">Save</button>
                                    <!-- Close Button -->
                                    <button class=" close-btn" id="close">Close</button>
                                </div>
                            </form>
                        </div>
                    </div>  
                </div>
                <!--                <div class="pagination">
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
        </div>            -->
            </section>               
        </main>
    </body>
    <!-- JS Link-->
    <script type="text/javascript" src="./assets/js/JSRemake/adminTable.js"></script>
    <script type="text/javascript" src="../assets/js/Admin/submitUser.js"></script>
</html>
