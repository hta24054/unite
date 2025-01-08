$(document).ready(function () {
    let currentProjectId, memberId;

    // 업무 내용 클릭 시
    $(".task-content.clickable").click(function () {
        console.log('c');
        currentProjectId = $(this).data("id"); // 프로젝트 ID
        memberId = $(this).data("memberid"); // 로그인한 사용자 ID
        const content = $(this).data("content") || ""; // 현재 업무 내용

        $("#taskContentInput").val(content); // 모달 창에 업무 내용 설정
        $("#taskContentModal").modal("show");
    });

    // 업무 내용 저장 버튼 클릭 시
    $("#saveTaskContentBtn").click(function () {
        const newTaskContent = $("#taskContentInput").val();

        if (!newTaskContent) {
            alert("업무 내용을 입력해주세요.");
            return;
        }

        $.ajax({
            url: "/project/updatetaskdesign",
            type: "POST",
            data: {
                projectId: currentProjectId,
                memberId: memberId,
                taskContent: newTaskContent
            },
            success: function (response) {
                if (response.success) {
                    const taskContentElement = $(`.task-content[data-id='${currentProjectId}'][data-memberid='${memberId}']`);
                    taskContentElement.text(newTaskContent);  // 새로 입력된 내용으로 업데이트
                    taskContentElement.data("content", newTaskContent);  // data-content 업데이트
                    taskContentElement.removeClass('clickable');  // 클릭 가능 상태 제거 (수정 후 더 이상 수정 불가)
                    alert("업무 내용 변경 완료했습니다");
                } else {
                    alert("업무 내용 업데이트에 실패했습니다.");
                }
                $("#taskContentModal").modal("hide");
            },
            error: function () {
                alert("오류가 발생했습니다.");
                $("#taskContentModal").modal("hide");
            }
        });
    });
    // 진행률 클릭 시
    $(".progress-rate.clickable").click(function () {
        currentProjectId = $(this).data("id");  // 프로젝트 ID
        memberId = $(this).data("memberid");    // 로그인한 사용자 ID
        const currentRate = $(this).data("rate");  // 현재 진행률
        console.log("currentProjectId: " + currentProjectId);
        console.log("memberId: " + memberId);
        console.log("currentRate: " + currentRate);

        // 모달에 진행률 설정
        $("#progressInput").val(currentRate);
        $("#progressModal").modal("show");
    });

    // 진행률 저장 버튼 클릭 시
    $("#saveProgressBtn").click(function () {
        const newProgressRate = $("#progressInput").val();  // 새로 입력된 진행률
        console.log("New progress rate: " + newProgressRate);

        if (!newProgressRate) {
            alert("진행률을 입력해 주세요.");
            return;
        }

        // 서버에 요청
        $.ajax({
            url: "/project/updateprogress",  // 서버 요청 URL
            type: "POST",
            data: {
                projectId: currentProjectId,      // 프로젝트 ID
                memberId: memberId,                // 로그인한 사용자 ID
                memberProgressRate: newProgressRate // 새로운 진행률
            },
            success: function (response) {
                console.log("Response from server: ", response);

                if (response.success) {
                    // 서버에서 성공적으로 응답 받은 경우, 해당 로그인한 사용자의 진행률만 업데이트
                    const progressRateElement = $(`.progress-rate[data-id='${currentProjectId}'][data-memberid='${memberId}']`);

                    if (progressRateElement.length > 0) {
                        // 진행률 텍스트와 스타일을 업데이트
                        progressRateElement.find(".progress-bar").css("width", newProgressRate + "%").text(newProgressRate + "%");
                        progressRateElement.data("rate", newProgressRate);  // data 속성도 업데이트

                        // 진행률에 맞춰 CSS 클래스 업데이트
                        if (newProgressRate >= 100) {
                            progressRateElement.find(".progress-bar").removeClass('bg-warning bg-danger').addClass('bg-success');
                        } else if (newProgressRate >= 50) {
                            progressRateElement.find(".progress-bar").removeClass('bg-danger bg-success').addClass('bg-warning');
                        } else {
                            progressRateElement.find(".progress-bar").removeClass('bg-warning bg-success').addClass('bg-danger');
                        }
                    } else {
                        console.log("진행률 요소를 찾을 수 없습니다.");
                    }
                    alert("진행률 변경 완료했습니다");
                } else {
                    console.log("업데이트에 실패했습니다.");
                }

                $("#progressModal").modal("hide");  // 진행률 모달 창 숨기기
            },
            error: function () {
                console.log("오류가 발생했습니다.");
                $("#progressModal").modal("hide");
            }
        });
    });

    $("#writeForm").submit(function (event) {
        event.preventDefault(); // 폼 기본 제출 방지
        const projectId = $("#projectId").val();
        const title = $("#postTitle").val();
        const content = $("#postContent").val();
        const file = $("#postFile")[0].files[0]; // 파일 가져오기 (수정된 부분)
        const formData = new FormData();
        console.log("sdfsd", projectId);
        formData.append("title", title);
        formData.append("content", content);
        formData.append("projectId", projectId);
        if (file) {
            formData.append("file", file);
        }

        $.ajax({
            url: "../projectBoard/write", // 서버 경로
            type: "POST", // HTTP 메서드
            data: formData, // FormData 객체로 전송
            contentType: false, // jQuery가 자동으로 Content-Type을 설정하지 않도록 설정
            processData: false, // 데이터를 쿼리 문자열로 변환하지 않도록 설정
            success: function (response) {
                console.log("Response from server:", response);

                if (response.success) {
                    updatePostList(response.posts); // 새로 고친 게시물 리스트 업데이트
                    $("#writeModal").modal("hide"); // 모달 창 닫기
                    $(".modal-backdrop").remove(); // 모달 배경 흐림 제거
                    window.location.reload();
                } else {
                    alert("저장 실패. 다시 시도해 주세요.");
                }

            },
            error: function (xhr, status, error) {
                console.error("AJAX 오류 발생:", status, error);
                console.error("서버 응답:", xhr.responseText);
                alert("오류가 발생했습니다. 다시 시도해 주세요.");
            }
        });
    });
    // tr 클릭 시 submitForm 호출
    $('.clickable-row').on('click', function () {
        const projectId = $("#projectId").val();
        location.href = "../projectBoard/list?projectId=" + projectId;
    });

    // 게시물 리스트 업데이트
    function updatePostList(posts) {
        const postListContainer = $("#postTable tbody");

        // 기존 목록 지우기
        postListContainer.empty();

        if (Array.isArray(posts) && posts.length > 0) {  // posts가 배열인지 확인
            posts.forEach(post => {
                const postRow = `
	                <tr>
	                    <td><a href="../projectBoard/membertask?memberId=${post.memberId}">${post.memberName}</a></td>
	                    <td>${post.projectTitle}</td> <!-- 기존 taskTitle -> projectTitle로 수정 -->
	                    <td>${post.projectUpdateDate}</td> <!-- 기존 taskUpdateDate -> projectContent로 수정 -->
	                </tr>
	            `;
                postListContainer.append(postRow); // 새로운 글을 테이블에 추가
            });
        } else {
            postListContainer.append("<tr><td colspan='3'>게시글이 없습니다.</td></tr>");
        }
    }

    notice();

    function notice() {
        $.ajax({
            url: contextPath + "/project/notice",
            method: 'GET',
            success: function (notifications) {
                console.log("응답 데이터:", notifications);

                // 알림 데이터를 담을 컨테이너 요소
                const postListContainer = $(".notification-content tbody");

                // 기존 내용을 지우고 새로운 알림을 추가
                postListContainer.empty();

                if (notifications && notifications.length > 0) {
                    // 상위 4개의 데이터만 처리
                    const topNotifications = notifications.slice(0, 4);

                    topNotifications.forEach(notice => {
                        const action = notice.taskUpdateDate
                            ? "수정(변경)하였습니다"
                            : "등록하였습니다";

                        const actionDate = notice.taskUpdateDate
                            ? notice.taskUpdateDate
                            : notice.taskDate;

                        const postRow = `
	                        <tr>
	                        	<td><img src="${contextPath}/emp/profile-image?UUID=${notice.task_file_uuid}"style="width:36px; height: 36px; border-radius:50%; border: 1px solid gray;"></td>
	                            <td>${notice.taskWriter} ${notice.Jobname}님이<br>${notice.ProjectName} - ${notice.taskTitle}을(를)<br>${action}<br><br><small>${actionDate}</small></td>
	                        </tr>
	                    `;
                        postListContainer.append(postRow);
                    });
                } else {
                    // 알림이 없는 경우
                    postListContainer.append(`
	                    <tr>
	                        <td colspan="2">게시글이 없습니다</td>
	                    </tr>
	                `);
                }
            },
            error: function (error) {
                console.error("알림 데이터를 가져오는 데 실패했습니다:", error);
            }
        });
    }


    /*function fetchNotifications() {
        $.ajax({
            url: contextPath + "/project/notification",
            method: 'GET',
            success: function(notifications) {
                const notificationContainer = $('#notificationContainer');
                notificationContainer.empty(); // 기존 알림 초기화
                notifications.forEach(notification => {
                    const notificationElement = `<div>${notification.message} - ${notification.time}</div>`;
                    notificationContainer.append(notificationElement);
                });
            },
            error: function(error) {
                console.error("알림 데이터를 가져오는 데 실패했습니다:", error);
            }
        });
    }

    // 3초마다 알림 업데이트
    setInterval(fetchNotifications, 1000);*/

});