<%-- 
    Document   : profile
    Created on : Oct 30, 2024, 12:35:40 PM
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
        <title>Astatine 04 - Profile</title>
        <link rel="shortcut icon" type="image/png" href="assets/img/Tittle-web-icon/Logo_Dark.ico" />

        <!-- Google font -->
        <link href="https://fonts.googleapis.com/css?family=Montserrat:400,500,700" rel="stylesheet">

        <!-- CSS Link -->
        <link rel="stylesheet" href="assets/css/Admin.css">
        <link rel="stylesheet" href="assets/css/styleProfiles.css"/>
        <link rel="stylesheet" href="./assets/css/AdminStyle.css"/>

        <!-- Icon New-->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

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
                    <a href="Profile?action=edit">
                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#e8eaed">
                        <path
                            d="M480-480q-66 0-113-47t-47-113q0-66 47-113t113-47q66 0 113 47t47 113q0 66-47 113t-113 47ZM160-240v-32q0-34 17.5-62.5T224-378q62-31 126-46.5T480-440q66 0 130 15.5T736-378q29 15 46.5 43.5T800-272v32q0 33-23.5 56.5T720-160H240q-33 0-56.5-23.5T160-240Zm80 0h480v-32q0-11-5.5-20T700-306q-54-27-109-40.5T480-360q-56 0-111 13.5T260-306q-9 5-14.5 14t-5.5 20v32Zm240-320q33 0 56.5-23.5T560-640q0-33-23.5-56.5T480-720q-33 0-56.5 23.5T400-640q0 33 23.5 56.5T480-560Zm0-80Zm0 400Z" />
                        </svg>
                        <span>Profile</span>
                    </a>
                </li>
                <li>
                    <a href="Profile?action=password">
                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368"><path d="M240-80q-33 0-56.5-23.5T160-160v-400q0-33 23.5-56.5T240-640h40v-80q0-83 58.5-141.5T480-920q83 0 141.5 58.5T680-720v80h40q33 0 56.5 23.5T800-560v400q0 33-23.5 56.5T720-80H240Zm0-80h480v-400H240v400Zm240-120q33 0 56.5-23.5T560-360q0-33-23.5-56.5T480-440q-33 0-56.5 23.5T400-360q0 33 23.5 56.5T480-280ZM360-640h240v-80q0-50-35-85t-85-35q-50 0-85 35t-35 85v80ZM240-160v-400 400Z"/></svg>
                        <span>Password</span>
                    </a>
                </li>
                <li>
                    <a href="Profile?action=order">
                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368">
                        <path
                            d="M280-80q-33 0-56.5-23.5T200-160q0-33 23.5-56.5T280-240q33 0 56.5 23.5T360-160q0 33-23.5 56.5T280-80Zm400 0q-33 0-56.5-23.5T600-160q0-33 23.5-56.5T680-240q33 0 56.5 23.5T760-160q0 33-23.5 56.5T680-80ZM246-720l96 200h280l110-200H246Zm-38-80h590q23 0 35 20.5t1 41.5L692-482q-11 20-29.5 31T622-440H324l-44 80h440q17 0 28.5 11.5T760-320q0 17-11.5 28.5T720-280H280q-45 0-68-39.5t-2-78.5l54-98-144-304H80q-17 0-28.5-11.5T40-840q0-17 11.5-28.5T80-880h65q11 0 21 6t15 17l27 57Zm134 280h280-280Z" />
                        </svg>
                        <span>Order</span>
                    </a>
                </li>
            </ul>
        </nav>
        <main class="table">
            <section class="table_header">
                <h1>User Management.</h1>
                <div class="input-group">
                    <input type="search" id="search" placeholder="Search">
                    <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368"><path d="M380-320q-109 0-184.5-75.5T120-580q0-109 75.5-184.5T380-840q109 0 184.5 75.5T640-580q0 44-14 83t-38 69l224 224q11 11 11 28t-11 28q-11 11-28 11t-28-11L532-372q-30 24-69 38t-83 14Zm0-80q75 0 127.5-52.5T560-580q0-75-52.5-127.5T380-760q-75 0-127.5 52.5T200-580q0 75 52.5 127.5T380-400Z"/></svg>
                </div>
                <div class="account-group">
                    <div> Hi,<span style="color: #21A691; font-weight: 500;">${user.fullname}</span>!</div>
                    <div>
                        <img src="../assets/img/default-avatar.png" width="35px" height="35px" alt="Avatar User"/>
                    </div>                    
                </div>
            </section>
            <div id="editModal" class="modal-container">
                <div class="modal-box"> 
                    <c:if test="${not empty error}">
                        <div class="alert">
                            <span class="closebtn">&times;</span>  
                            <strong>Error!</strong> ${error}
                        </div>
                        <c:remove var="error" scope="session"/>
                    </c:if>
                    <c:if test="${not empty successMessage}">
                        <div class="alert success">
                            <span class="closebtn">&times;</span>  
                            <strong>Success!</strong> ${successMessage}
                        </div>
                        <c:remove var="successMessage" scope="session"/>
                    </c:if>
                    <h1>Profile Information</h1>
                    <div class="form">
                        <form action="Profile?action=edit" method="post">

                            <input type="hidden" name="action" value="update">
                            <input type="hidden" name="id" id="userId" value="${user.userId}">

                            <div class="top-left">
                                <!-- Full Name -->
                                <label for="fullname">Full Name:</label>
                                <input type="text" id="fullname" name="fullname" value="${user.fullname}"><br>

                                <!-- Email -->
                                <label for="email">Email:</label>
                                <input type="email" id="email" name="email" value="${user.email}"><br>

                                <!-- Phone -->
                                <label for="phone">Phone:</label>
                                <input type="text" id="phone" name="phone" value="${user.phone}"><br>

                                <!-- Country -->
                                <label for="country">Country:</label>
                                <input type="text" id="country" name="country" value="${user.country}"><br>
                            </div>

                            <div class="top-right">
                                <!-- Street -->
                                <label for="street">Street:</label>
                                <input type="text" id="street" name="street" value="${user.street}"><br>

                                <!-- Ward -->
                                <label for="ward">Ward:</label>
                                <input type="text" id="ward" name="ward" value="${user.ward}"><br>

                                <!-- District -->
                                <label for="district">District:</label>
                                <input type="text" id="district" name="district" value="${user.district}"><br>

                                <!-- City -->
                                <label for="city">City:</label>
                                <input type="text" id="city" name="city" value="${user.city}"><br>
                            </div>

                            <!-- Save Button -->
                            <button type="submit" class="submit-btn"  onclick="location.href = 'Profile?action=edit'">
                                Save Changes
                            </button>

                        </form>
                    </div>
                </div>
            </div>
        </main>
        <script>
            var close = document.getElementsByClassName("closebtn");
            var i;

            for (i = 0; i < close.length; i++) {
                close[i].onclick = function () {
                    var div = this.parentElement;
                    div.style.opacity = "0";
                    setTimeout(function () {
                        div.style.display = "none";
                    }, 600);
                }
            }
        </script>
    </body>
    <!-- JS Link-->
    <script type="text/javascript" src="./assets/js/JSRemake/adminJS.js" defer></script>
    <script type="text/javascript" src="./assets/js/JSRemake/adminDragDropImage.js" defer></script>
</html>

