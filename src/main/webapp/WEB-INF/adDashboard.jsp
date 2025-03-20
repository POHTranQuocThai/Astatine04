<%-- 
    Document   : adDashboard
    Created on : Mar 12, 2025, 10:54:56 AM
    Author     : Ma Tan Loc - CE181795
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>

<!DOCTYPE HTML>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- Tittle Web-->
        <title>Astatine 04 - Admin</title>
        <link rel="shortcut icon" type="image/png" href="assets/img/Tittle-web-icon/Logo_Dark.ico" />

        <!-- Google font -->
        <link href="https://fonts.googleapis.com/css?family=Montserrat:400,500,700" rel="stylesheet">

        <!-- CSS Link -->
        <link rel="stylesheet" href="./assets/css/Admin.css">
        <link rel="stylesheet" href="./assets/css/AdminStyle.css"/>
        <link rel="stylesheet" href="./assets/css/adDashboard.css"/>


        <!-- Icon New-->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200&icon_names=arrow_drop_down" />
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>        

        <script>
            window.onload = function () {
                var dataPoints = [];
                var dataPointsProduct = [];
                CanvasJS.addColorSet("Astatine",
                        [//colorSet Array
                            "#21A691",
                            "#1a8474",
                            "#177465",
                            "#63c0b2",
                            "#a6dbd3"
                        ]);
                // Chart 1: Tổng số lượng sản phẩm bán ra trong tháng
                var stockChart = new CanvasJS.StockChart("chartContainer1", {
                    theme: "light ",
                    zoomEnabled: true,
                    backgroundColor: "transparent",
                    navigator: {
                        slider: {
                            maskColor: "#e8f6f4",
                            outlineColor: "#bce4de"
                        },
                    },
                    title: {
                        text: "Sale Reports",
                        horizontalAlign: "left",
                        padding: 15,
                        fontFamily: "Montserrat",
                        fontWeight: 700
                    },
                    axisY: {
                        interlacedColor: "#f5f5f5",
                        gridColor: "#eee",
                        labelFontFamily: "Montserrat"
                    },
                    charts: [{
                            data: [{
                                    type: "splineArea",
                                    toolTipContent: "Date {x}: {y} Orders",
                                    color: "#21A691",
                                    markerSize: 5,
                                    fillOpacity: 0.5,
                                    dataPoints: dataPoints
                                }]
                        }],
                    rangeSelector: {
                        buttonStyle: {
                            backgroundColor: "#f4f4f4",
                            borderThickness: 0,
                            padding: 6,
                            spacing: 6,
                            backgroundColorOnHover: "#21A691",
                            backgroundColorOnSelect: "#21A691",
                            fontFamily: "Montserrat"
                        },
                        buttons: [
                            {range: 1, rangeType: "month", label: "1 Month"},
                            {range: 3, rangeType: "month", label: "3 Months"},
                            {range: 6, rangeType: "month", label: "6 Months"},
                            {rangeType: "all", label: "Show All"}
                        ],
                        inputFields: {
                            enabled: false,
                            style: {
                                borderThickness: 0,
                                backgroundColor: "#f4f4f4",
                                padding: 6,
                                spacing: 6,
                                fontFamily: "Montserrat"
                            },
                            startValue: new Date(2023, 0, 1), // Chỉnh sửa theo khoảng thời gian của dữ liệu
                            endValue: new Date()
                        }
                    }
                });
                var chart2 = new CanvasJS.Chart("chartContainer2", {
                    colorSet: "Astatine",
                    theme: "light1",
                    animationEnabled: true,
                    //                    exportFileName: "Top_5_Selling_Products",
                    //                    exportEnabled: true,
                    toolbar: {
                        itemBackgroundColorOnHover: "#21A691",
                        fontColorOnHover: "#fff"
                    },
                    title: {
                        text: "Top 5 Selling Products",
                        padding: 15,
                        fontWeight: 700,
                        fontFamily: "Montserrat"
                    },
                    legend: {
                        horizontalAlign: "centert", // "center" , "right"
                        verticalAlign: "bottom", // "top" , "bottom"
                        fontFamily: "Montserrat",
                        fontWeight: 500
                    },
                    data: [{
                            type: "pie",
                            showInLegend: true,
                            legendText: "{label}",
                            toolTipContent: "{label}: <strong>{y}%</strong>",
                            indexLabel: "{y}%",
                            indexLabelFontFamily: "Montserrat",
                            toolTipFontFamily: "Montserrat",
                            dataPoints: dataPointsProduct
                        }]
                });
                function loadData() {
                    fetch("OrderData")
                            .then(response => response.json())
                            .then(data => {
                                console.log(data);
                                let totalAmountByDay = {};
                                let currentMonth = new Date().getMonth() + 1;
                                data.orders.forEach(item => {
                                    let orderDate = new Date(item.timestamp);
                                    let orderDay = orderDate.getDate();
                                    let orderMonth = orderDate.getMonth() + 1;
                                    if (orderMonth === currentMonth) {
                                        totalAmountByDay[orderDay] = (totalAmountByDay[orderDay] || 0) + item.amount;
                                    }
                                });
                                Object.keys(totalAmountByDay).forEach(day => {
                                    dataPoints.push({
                                        x: new Date(new Date().getFullYear(), currentMonth - 1, parseInt(day)),
                                        y: totalAmountByDay[day]
                                    });
                                });
                                let total = 0;
                                let totalPercent = 0;
                                data.products.forEach(item => {
                                    total += item.y;
                                });
                                data.products.forEach((item, index) => {
                                    let percent = ((item.y / total) * 100).toFixed(2);
                                    percent = parseFloat(percent);
                                    totalPercent += percent;
                                    if (index === data.products.length - 1 && totalPercent !== 100) {
                                        percent += 100 - totalPercent;
                                    }

                                    dataPointsProduct.push({
                                        label: item.label,
                                        y: percent
                                    });
                                });
                                console.log(dataPointsProduct);
                                stockChart.render();
                                chart2.render();
                            })
                            .catch(error => console.error("Lỗi khi lấy dữ liệu:", error));
                }

                loadData();
            };
        </script>
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

        <!-- Admin Products -->
        <main class="table">
            <section class="table_header">
                <h1>Dashboard.</h1>
                <div class="input-group">
                    <input type="search" id="search" placeholder="Search" >
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
                <div class="table_bodyContainer">
                    <div class="chartContainer">
                        <div class="chartContainer-inner" id="chartContainer1" style="height: 370px; width: 70%;"></div>
                        <div class="chartContainer-inner" id="chartContainer2" style="height: 370px; width: 30%;"></div>
                    </div>
                    <div class="title">
                        <h3>Top 5 Selling Products.</h3>
                    </div>
                    <div class="tableContainer">
                        <table>
                            <thead>
                                <tr>
                                    <th>Product Name </th>
                                    <th>Type </th>
                                    <th>Brand </th>
                                    <th>Sold </th>
                                    <th>Stock </th>
                                    <th>Price </th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="p" items="${productList}">
                                    <tr>
                                        <td>${p.productName}</td>
                                        <td>${p.type}</td>
                                        <td>${p.brand}</td>
                                        <td>${p.selled}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${p.countInStock == 0}">
                                                    <span class="status OutOfStock">Out of Stock</span>
                                                </c:when>
                                                <c:when test="${p.countInStock <= 5}">
                                                    <span class="status LowStock">Low Stock</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="status InStock">In Stock</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>

                                        <td><fmt:formatNumber value="${p.price}" pattern="#,###" /> VNĐ</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </section>
        </main>
        <script type="text/javascript" src="https://canvasjs.com/assets/script/jquery-1.11.1.min.js"></script>
        <script type="text/javascript" src="https://cdn.canvasjs.com/canvasjs.stock.min.js"></script>
        <script src="https://canvasjs.com/assets/script/jquery-1.11.1.min.js"></script>
        <script src="https://cdn.canvasjs.com/canvasjs.min.js"></script>
        <script type="text/javascript" src="assets/js/JSRemake/adminTable.js"></script>
    </body>
</html>               
