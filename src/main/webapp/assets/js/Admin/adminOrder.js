let currentPage = 1;
let itemsPerPage = 6;
let orderData = [];

document.addEventListener("DOMContentLoaded", function () {
    loadOrderData();
});

function loadOrderData() {
    let rows = document.querySelectorAll("tbody tr");
    orderData = Array.from(rows).map(row => ({
            element: row,
            orderDate: new Date(row.cells[3].textContent.trim()),
            totalPrice: parseFloat(row.cells[4].textContent.replace(/\D/g, ""))
        }));
    applyPagination();
}

function applyPagination() {
    let start = (currentPage - 1) * itemsPerPage;
    let end = start + itemsPerPage;

    orderData.forEach((order, index) => {
        order.element.style.display = (index >= start && index < end) ? "" : "none";
    });

    renderPagination();
}

function renderPagination() {
    let totalPages = Math.ceil(orderData.length / itemsPerPage);
    let paginationContainer = document.getElementById("pagination");
    paginationContainer.innerHTML = "";

    for (let i = 1; i <= totalPages; i++) {
        let btn = document.createElement("button");
        btn.textContent = i;
        btn.classList.add("page-btn");
        if (i === currentPage)
            btn.classList.add("active");
        btn.onclick = function () {
            currentPage = i;
            applyPagination();
        };
        paginationContainer.appendChild(btn);
    }
}

function filterByDate() {
    let startDate = new Date(document.getElementById("startDate").value);
    let endDate = new Date(document.getElementById("endDate").value);

    orderData.forEach(order => {
        let orderDate = order.orderDate;
        order.element.style.display = (orderDate >= startDate && orderDate <= endDate) ? "" : "none";
    });
}

function changeItemsPerPage() {
    let selectedValue = document.getElementById("itemsPerPage").value;

    if (selectedValue === "all") {
        itemsPerPage = orderData.length;
        currentPage = 1;
        orderData.forEach(order => order.element.style.display = "");
        document.getElementById("pagination").innerHTML = "";
    } else {
        itemsPerPage = parseInt(selectedValue);
        currentPage = 1;
        applyPagination();
    }
}

const table = document.querySelector('#table');
const excel_btn = document.querySelector('#toEXCEL');

const toExcel = (table) => {
    let rows = table.querySelectorAll('tbody tr');
    let visibleRows = [...rows].filter(row => row.style.display !== "none");

    let headers = [...table.querySelectorAll('thead th')].slice(0, -2)
            .map(th => th.innerText.replace("arrow_drop_down", ""))
            .join("\t");

    let body = visibleRows.map(row => {
        let cells = row.querySelectorAll('td');
        let filteredCells = [...cells].slice(0, -2)
                .map(cell => cell.innerText.trim());
        return filteredCells.join("\t");
    });
    let totalRevenue = visibleRows.reduce((sum, row) => {
        let totalPrice = parseFloat(row.cells[4].textContent.replace(/\D/g, "")) || 0; 
        return sum + totalPrice;
    }, 0);

    let sumRow = `\nSUM Total Price:\t\t\t\t${totalRevenue.toLocaleString()} VNĐ`;


    return headers + "\n" + body.join("\n") + sumRow;
};

const getExcelFileName = () => {
    let nowDate = new Date().toISOString().split("T")[0];
    let rows = document.querySelectorAll("tbody tr");
    let orderDates = [...rows].map(row => new Date(row.cells[3].textContent.trim()));
    let oldestDate = orderDates.length ? new Date(Math.min(...orderDates)).toISOString().split("T")[0] : nowDate;

    let startDateInput = document.getElementById("startDate").value;
    let endDateInput = document.getElementById("endDate").value;

    if (startDateInput && endDateInput) {
        return `Total_Orders_From(${startDateInput})_To(${endDateInput}).xlsx`;
    } else {
        return `Total_Orders_From(${oldestDate})_To(${nowDate}).xlsx`;
    }
};

// Gán sự kiện click cho nút xuất Excel
excel_btn.onclick = () => {
    const excelData = toExcel(document.querySelector("table"));
    const filename = getExcelFileName(); 
    downloadFile(excelData, filename);
};

const downloadFile = (data, filename) => {
    const blob = new Blob([data], {type: "text/csv;charset=utf-8;"});
    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.download = filename;
    link.style.display = "none";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
};

