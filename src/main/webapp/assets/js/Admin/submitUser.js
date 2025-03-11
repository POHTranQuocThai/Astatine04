document.addEventListener("DOMContentLoaded", function () {
    // Hàm mở modal
    function openModal(modalId) {
        let modal = document.getElementById(modalId);
        if (modal) {
            modal.style.display = "flex"; // Đảm bảo hiện modal lên
            modal.classList.add("active"); // Thêm class nếu cần
        } else {
            console.error("Không tìm thấy modal:", modalId);
        }
    }

    // Hàm đóng modal
    function closeModal(modalId) {
        let modal = document.getElementById(modalId);
        if (modal) {
            modal.style.display = "none";
            modal.classList.remove("active");
        }
    }

    // ============ Xử lý EDIT ============
    document.querySelectorAll(".table-btn.edit").forEach((btn) => {
        btn.addEventListener("click", function (event) {
            event.preventDefault();
            let userId = this.getAttribute("data-user-id");

            fetch(`User?action=edit&id=${userId}`, {
                method: 'GET',
                headers: {'Accept': 'application/json'}
            })
                    .then(response => response.json())
                    .then(user => {
                        if (user) {
                            document.getElementById("userId").value = user.userId;
                            document.getElementById("fullname").value = user.fullname;
                            document.getElementById("email").value = user.email;
                            document.getElementById("phone").value = user.phone;
                            document.getElementById("street").value = user.street;
                            document.getElementById("ward").value = user.ward;
                            document.getElementById("district").value = user.district;
                            document.getElementById("city").value = user.city;
                            document.getElementById("password").value = user.password;
                            document.getElementById("country").value = user.country;
                            document.getElementById("isAdmin").checked = user.isAdmin;

                            openModal("editUserModal"); // Mở modal sau khi load xong
                        }
                    })
                    .catch(error => console.error("Lỗi khi lấy dữ liệu user:", error));
        });
    });

    // ============ Đóng modal Edit ============
    document.querySelector("#editUserModal .close-btn").addEventListener("click", function (event) {
        event.preventDefault();
        document.getElementById("editUserModal").style.display = "none";
        event.stopPropagation(); // Ngăn chặn sự kiện lan sang modal khác
    });

    // ============ Xử lý DELETE ============
    document.querySelectorAll(".table-btn.delete").forEach((btn) => {
        btn.addEventListener("click", function (event) {
            event.preventDefault();
            let userId = this.getAttribute("data-user-id");

            let deleteModal = document.getElementById("deleteUserModal");
            if (deleteModal) {
                document.querySelector("#deleteUserModal input[name='id']").value = userId;
                openModal("deleteUserModal");
            }
        });
    });

// Xử lý đóng modal Delete
    let deleteCloseBtn = document.querySelector("#deleteUserModal .close-btn");
    if (deleteCloseBtn) {
        deleteCloseBtn.addEventListener("click", function (event) {
            event.preventDefault();
            closeModal("deleteUserModal");
        });
    }
});
