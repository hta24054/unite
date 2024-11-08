$(document).ready(function() {
    let currentProjectId, memberId;

    // 업무 내용 클릭 시
    $(".task-content.clickable").click(function() {
        console.log('c');
        currentProjectId = $(this).data("id"); // 프로젝트 ID
        memberId = $(this).data("memberid"); // 로그인한 사용자 ID
        const content = $(this).data("content") || ""; // 현재 업무 내용
        
        $("#taskContentInput").val(content); // 모달 창에 업무 내용 설정
        $("#taskContentModal").modal("show");
    });

    // 업무 내용 저장 버튼 클릭 시
    $("#saveTaskContentBtn").click(function() {
        const newTaskContent = $("#taskContentInput").val();

        if (!newTaskContent) {
            alert("업무 내용을 입력해주세요.");
            return;
        }

        $.ajax({
		    url: contextPath + "/project/updatetaskdesign",
		    type: "POST",
		    data: {
		        projectId: currentProjectId,
		        memberId: memberId,
		        taskContent: newTaskContent
		    },
		    success: function(response) {
		        if (response.success) {
		            const taskContentElement = $(`.task-content[data-id='${currentProjectId}'][data-memberid='${memberId}']`);
            taskContentElement.text(newTaskContent);  // 새로 입력된 내용으로 업데이트
            taskContentElement.data("content", newTaskContent);  // data-content 업데이트
            taskContentElement.removeClass('clickable');  // 클릭 가능 상태 제거 (수정 후 더 이상 수정 불가)
		        } else {
		            alert("업무 내용 업데이트에 실패했습니다.");
		        }
		        $("#taskContentModal").modal("hide");
		    },
		    error: function() {
		        alert("오류가 발생했습니다.");
		        $("#taskContentModal").modal("hide");
		    }
		});
    });
    // 진행률 클릭 시
	$(".progress-rate.clickable").click(function() {
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
	$("#saveProgressBtn").click(function() {
	    const newProgressRate = $("#progressInput").val();  // 새로 입력된 진행률
	    console.log("New progress rate: " + newProgressRate);
	
	    if (!newProgressRate) {
	        alert("진행률을 입력해 주세요.");
	        return;
	    }
	
	    // 서버에 요청
	    $.ajax({
	        url: contextPath + "/project/updateprogress",  // 서버 요청 URL
	        type: "POST",
	        data: { 
	            projectId: currentProjectId,      // 프로젝트 ID
	            memberId: memberId,                // 로그인한 사용자 ID
	            memberProgressRate: newProgressRate // 새로운 진행률
	        },
	        success: function(response) {
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
	            } else {
	                console.log("업데이트에 실패했습니다.");
	            }
	
	            $("#progressModal").modal("hide");  // 진행률 모달 창 숨기기
	        },
	        error: function() {
	            console.log("오류가 발생했습니다.");
	            $("#progressModal").modal("hide");
	        }
	    });
	});
	
	
	//글 작성
	$("#savePostBtn").click(function() {
    const formData = new FormData(document.getElementById("writeForm"));

	    $.ajax({
	        url: contextPath + "/project/write",
	        type: "POST",
	        data: formData,
	        contentType: false,
	        processData: false,
	        success: function(response) {
	            // 새 게시글을 UI에 추가
	            if (response && response.length > 0) {
	                response.forEach(post => {
	                    // 게시글 요소를 동적으로 추가하는 로직
	                    $("#postList").prepend(
	                        `<div class="post-item">
	                            <h3>${post.title}</h3>
	                            <p>${post.content}</p>
	                        </div>`
	                    );
	                });
	            }
	            $("#writeModal").modal("hide");
	        },
	        error: function() {
	            alert("게시글 저장에 실패했습니다.");
	        }
	    });
	});

});