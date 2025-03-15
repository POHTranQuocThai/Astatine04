/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

document.addEventListener("DOMContentLoaded", function () {
    const statusSelects = document.querySelectorAll(".status-select");

    statusSelects.forEach(select => {
        if (select.value !== "Ordered") {
            select.disabled = true;
            updateSelectColor(select);
        }

        select.addEventListener("change", function () {
            const orderId = this.getAttribute("data-order-id");
            const newStatus = this.value;

            if (newStatus !== "Ordered") {
                fetch(`Order?action=updateStatus&id=${orderId}&status=${newStatus}`, {
                    method: "POST",
                })
                .then(response => response.text())
                .then(data => {
                    console.log("Status updated:", data);
                    select.disabled = true;
                    updateSelectColor(select);
                })
                .catch(error => console.error("Error updating status:", error));
            }
        });
    });

    function updateSelectColor(select) {
        select.classList.remove("delivered", "canceled");

        if (select.value === "Delivered") {
            select.classList.add("delivered");
        } else if (select.value === "Canceled") {
            select.classList.add("canceled");
        }
    }
});


