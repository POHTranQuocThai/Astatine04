/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

function showToast(message, duration = 3000) {
    const toastContainer = document.getElementById("toast-container");
    const toast = document.createElement("div");
    toast.className = "toast";
    toast.innerText = message;
    
    toastContainer.appendChild(toast);

    setTimeout(() => {
        toast.remove();
    }, duration);
}
