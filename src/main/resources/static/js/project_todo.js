$(document).ready(function() {
	let currentProjectId, todoId;

	// 진행률 클릭 시
	$(".progress-btn").click(function() {
		todoId = $(this).data("todo-id");  // todo ID
		const currentRate = $(this).data("progress-rate");  // 현재 진행률
		currentProjectId = $(this).data("projectid");
		console.log("todoId: " + todoId);
		console.log("currentRate: " + currentRate);
		console.log("projectId: " + currentProjectId);

		// 모달에 진행률 설정
		$("#progressRate").val(currentRate); // progressRate에서 progressInput으로 수정
		$("#progressModal").modal("show");
	});

	// 진행률 저장 버튼 클릭 시
	$("#saveProgressBtn").click(function() {
		const newProgressRate = $("#progressRate").val();  // 새로 입력된 진행률
		console.log("New progress rate: " + newProgressRate);

		if (!newProgressRate) {
			alert("진행률을 입력해 주세요.");
			return;
		}

		// 서버에 요청
		$.ajax({
			url: "/api/project/todoprogress",  // 서버 요청 URL
			type: "POST",
			data: {
				projectId: currentProjectId,      // 프로젝트 ID
				todoId: todoId,
				memberProgressRate: newProgressRate // 새로운 진행률
			},
			success: function(response) {
				console.log("Response from server: ", response);

				if (response.success) {
					// 서버에서 성공적으로 응답 받은 경우, 해당 로그인한 사용자의 진행률만 업데이트
					const progressRateElement = $(`.progress-btn[data-projectid='${currentProjectId}'][data-todo-id='${todoId}']`);
					console.log(todoId);
					if (progressRateElement.length > 0) {
						// 진행률 텍스트와 스타일을 업데이트
						progressRateElement.find(".progress-bar").css("width", newProgressRate + "%").text(newProgressRate + "%");

						// 진행률에 맞춰 CSS 클래스 업데이트
						if (newProgressRate >= 100) {
							progressRateElement.find(".progress-bar")
								.removeClass('bg-warning bg-danger')
								.addClass('bg-success')
								.css('color', 'white'); // 글씨 색깔 변경
						} else if (newProgressRate >= 50) {
							progressRateElement.find(".progress-bar")
								.removeClass('bg-danger bg-success')
								.addClass('bg-warning')
								.css('color', 'black'); // 글씨 색깔 변경
						} else {
							progressRateElement.find(".progress-bar")
								.removeClass('bg-warning bg-success')
								.addClass('bg-danger')
								.css('color', 'white'); // 글씨 색깔 변경
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
			error: function() {
				console.log("오류가 발생했습니다.");
				$("#progressModal").modal("hide");
			}
		});
	});

	//투두 수정
	$(".edit-btn").on("click", function () {
		var todoId = $(this).data("todoid");
		var todoSubject = $(this).data("todo-subject");
		var projectId = $(this).data("projectid");

		// 모달에 기존 제목 표시
		$("#todoSubject").val(todoSubject);

		// 모달을 열기
		$("#editTodoModal").modal("show");

		// 저장 버튼 클릭 시 이벤트 핸들러는 한 번만 바인딩
		$("#saveTodoBtn").off("click").on("click", function () { // 기존 핸들러 제거 후 새로 바인딩
			var newSubject = $("#todoSubject").val();

			if (newSubject.trim() !== "") {
				$.ajax({
					url: "/project/updateTodo",
					method: "POST",
					data: {
						todoId: todoId,
						newSubject: newSubject,
						projectId: projectId
					},
					success: function (response) {
						if (response.success) {
							$("span[data-todoid='" + todoId + "']").text(newSubject); // .todoSubject로 텍스트 수정

							// 모달 닫기
							$("#editTodoModal").modal("hide");

							alert("할 일이 수정되었습니다.");
						} else {
							alert("수정 실패");
						}
					},
					error: function () {
						alert("서버 오류가 발생했습니다.");
					}
				});
			} else {
				alert("제목을 입력해주세요.");
			}
		});
	});


	$(".delete-btn").on("click", function () {
		var todoId = $(this).data("todo-id");
		var projectId = $(this).data("projectid");
		// 삭제 확인 메시지
		if (confirm("정말 이 할 일을 삭제하시겠습니까?")) {
			$.ajax({
				url: "/project/deleteTodo", // 삭제 요청 URL (서버에서 처리하는 경로)
				method: "POST", // HTTP 메서드 (삭제는 보통 POST로 처리)
				data: {
					todoId: todoId
				},
				success: function (response) {
					if (response.success) {
						// 삭제 성공 시 해당 할 일을 UI에서 제거
						$("span[data-todoid='" + todoId + "']").closest("li").remove();
						alert("할 일이 삭제되었습니다.");
						location.href="/project/todo?projectId="+projectId;
					} else {
						alert("삭제 실패");
					}
				},
				error: function () {
					alert("서버 오류가 발생했습니다.");
				}
			});
		}
	});

});
