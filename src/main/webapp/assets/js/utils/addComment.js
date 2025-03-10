function addCommentServer(name, userId, productId, commentText, parentId = 0) {
    const now = new Date();
    const dateString = now.toLocaleDateString("vi-VN");
    const timeString = now.toLocaleTimeString("vi-VN", {
        hour: "2-digit",
        minute: "2-digit",
    });
    const createAt = timeString + " " + dateString
    fetch('Comment', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({user_id: userId, product_id: productId, content: commentText, parent_id: parentId === undefined ? 0 : parentId})
    })
            .then(response => {
                if (!response.ok) {
                    // ❌ Nếu response có lỗi, đọc lỗi từ server
                    return response.text().then(errorMessage => {
                        throw new Error(errorMessage || 'Có lỗi xảy ra!');
                    });
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    // ✅ Lấy commentId từ phản hồi backend
                    let commentId = data.comment_id;
                    // ✅ Tạo comment với ID chính xác
                    var commentDiv = createCommentElement(name, commentText, createAt, commentId, productId);
                    if (parentId) {
                        let parentComment = document.querySelector(`[data-comment-id="${parentId}"]`);
                        parentComment.querySelector(".replies").appendChild(commentDiv);
                    } else {
                        document.getElementById("commentsList").appendChild(commentDiv);
                    }
                    document.getElementById("commentInput").value = "";
                } else {
                    showToast("Error while adding comment!", '');
                }
            })
            .catch(error => {
                // 🛑 Xử lý lỗi nếu có
                console.error('Lỗi khi thêm bình luận:', error);
                showToast(`🚨 Lỗi: ${error.message}`, 'error');
            });
}

function toggleLikeServer(likeContainer, likeCount) {
    let commentDiv = likeContainer.closest('.comment'); // Lấy comment chứa nút like
    let commentId = commentDiv.dataset.commentId;
    let commentOwnerId = commentDiv.dataset.userId;

    console.log(commentId, commentOwnerId);
    fetch('LikeComment', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({user_id: commentOwnerId, comment_id: commentId})
    })
            .then(response => response.text()) // Kiểm tra phản hồi thực tế
            .then(text => {
                console.log("Raw response:", text);
                return JSON.parse(text); // Chuyển về JSON nếu hợp lệ
            })
            .then(data => {
                console.log(data);
                if (data.success) {
                    likeCount.textContent = `(${data.like_count})`;
                    likeContainer.style.color = data.liked ? '#007bff' : 'black';
                } else {
                    alert("Lỗi khi like!");
                }
            });
}

function loadComments(name, productId, userId) {
    fetch(`Comment?product-id=${productId}&userId=${userId}`)
            .then(response => response.json())
            .then(data => {
                console.log("Data from API:", data);
                document.getElementById("commentsList").innerHTML = "";

                let commentMap = new Map();
                let parentComments = [];

                data.comments.forEach(comment => {
                    // 🔹 Định dạng ngày giờ
                    const formattedDate = `${comment.createdAt.date.day.toString().padStart(2, '0')}/` +
                            `${comment.createdAt.date.month.toString().padStart(2, '0')}/` +
                            `${comment.createdAt.date.year}`;
                    const formattedTime = `${comment.createdAt.time.hour.toString().padStart(2, '0')}:` +
                            `${comment.createdAt.time.minute.toString().padStart(2, '0')}:`

                    const dateTimeString = `${formattedTime} ${formattedDate}`;
                    console.log(comment.id);
                    // 🔹 Tạo phần tử bình luận
                    let commentElement = createCommentElement(comment.userName, comment.commentText, dateTimeString, comment.id, productId);

                    // ✅ Kiểm tra nếu `commentElement` đã có nút like thì bỏ qua
                    if (commentElement.querySelector('.like-container')) {
                        console.log('ne');
                        // ✅ Tạo nút like riêng cho từng comment
// ✅ Lấy nút like đúng trong comment hiện tại
                        let likeContainer = commentElement.querySelector('.like-container');
                        let likeCount = commentElement.querySelector('.like-count');

                        if (likeContainer && likeCount) {
                            // ✅ Cập nhật số lượt like
                            likeCount.textContent = `(${comment.likeCount !== undefined ? comment.likeCount : 0})`;

                            // ✅ Kiểm tra trạng thái đã like hay chưa
                            likeContainer.style.color = comment.isLiked ? "#007bff" : "black";

                            // ✅ Thêm sự kiện click like
                            likeContainer.onclick = function () {
                                toggleLikeServer(likeContainer, likeCount);
                            };
                        } else {
                            console.error("Không tìm thấy likeContainer trong commentElement:", commentElement);
                        }

                        // ✅ Gán nút like vào comment
//                        commentElement.appendChild(likeContainer);
                    }

                    commentMap.set(comment.id, commentElement);

                    // 🔹 Xử lý cha - con
                    if (comment.parentId) {
                        if (commentMap.has(comment.parentId)) {
                            commentMap.get(comment.parentId).querySelector(".replies").appendChild(commentElement);
                        } else {
                            parentComments.push({parentId: comment.parentId, element: commentElement});
                        }
                    } else {
                        document.getElementById("commentsList").appendChild(commentElement);
                    }
                });

                // 🔹 Gắn comment con vào comment cha nếu còn thiếu
                parentComments.forEach(reply => {
                    if (commentMap.has(reply.parentId)) {
                        commentMap.get(reply.parentId).querySelector(".replies").appendChild(reply.element);
                    }
                });
            })
            .catch(error => console.error("Error loading comments:", error));
}


function replyComment(name, userId, productId, parentId, commentDiv) {
    var replyBox = document.createElement("div");
    replyBox.classList.add("reply-box");
    var replyInput = document.createElement("input");
    replyInput.placeholder = "Reply...";
    var sendButton = document.createElement("button");
    sendButton.classList.add("send");
    sendButton.textContent = "Send";
    sendButton.onclick = function () {
        if (replyInput.value.trim() !== "") {
            addCommentServer(name, userId, productId, replyInput.value.trim(), parentId);
            replyBox.remove();
        } else {
            showToast('Please enter reply!', '');
        }
    };
    var cancelButton = document.createElement("button");
    cancelButton.classList.add("cancel");
    cancelButton.textContent = "Cancel";
    cancelButton.onclick = function () {
        replyBox.remove();
    };
    replyBox.appendChild(replyInput);
    replyBox.appendChild(sendButton);
    replyBox.appendChild(cancelButton);
    commentDiv.appendChild(replyBox);
}
function editComment(commentId, contentDiv) {
    // Kiểm tra nếu đã có input tránh tạo nhiều lần
    if (contentDiv.querySelector("input"))
        return;

    let oldContent = contentDiv.textContent;
    contentDiv.innerHTML = ""; // Xóa nội dung cũ

    let inputField = document.createElement("input");
    inputField.type = "text";
    inputField.value = oldContent;
    inputField.style.width = "90%";
    inputField.style.padding = "5px";

    let saveButton = document.createElement("button");
    saveButton.textContent = "Save";
    saveButton.style.marginLeft = "5px";
    saveButton.classList.add("edit-save")
    saveButton.onclick = function () {
        saveEditedComment(commentId, inputField.value, contentDiv);
    };

    contentDiv.appendChild(inputField);
    contentDiv.appendChild(saveButton);

    inputField.focus();
    inputField.addEventListener("keypress", function (event) {
        if (event.key === "Enter") {
            saveEditedComment(commentId, inputField.value, contentDiv);
        }
    });
}

// ✅ Hàm lưu chỉnh sửa
function saveEditedComment(commentId, newContent, contentDiv) {
    if (!newContent.trim()) {
        showToast("Content cannot be empty!", "");
        return;
    }

    fetch('/editComment', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({commentId, newContent})
    })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showToast("Comment updated successfully!", 'success');
                    contentDiv.innerHTML = newContent; // Cập nhật nội dung mới
                } else {
                    showToast("Failed to update comment!", '');
                }
            })
            .catch(error => console.error('Error:', error));
}


function deleteComment(commentId, commentDiv) {
    if (!confirm("Are you sure you want to delete this comment?")) {
        return;
    }

    fetch('/deleteComment', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({commentId})
    })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    commentDiv.remove(); // Xóa UI
                    showToast("Comment deleted successfully!", "success");
                } else {
                    showToast("Failed to delete comment!", "");
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showToast("Error deleting comment!", "");
            });
}
