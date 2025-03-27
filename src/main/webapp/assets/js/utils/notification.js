function showToast(message, type) {
    Toastify({
        text: message,
        duration: 3000,
        newWindow: true,
        close: true,
        stopOnFocus: true,
        gravity: "top",
        position: "center",
        style: {
            background: type === 'success' ? "#21A691" : "#EE4E4E",
            padding: "10px 30px",
            cursor: "pointer"
        },
        onclick: function(){}
    }).showToast();
}

// Gán vào `window` để có thể gọi ở mọi nơi
window.showToast = showToast;
