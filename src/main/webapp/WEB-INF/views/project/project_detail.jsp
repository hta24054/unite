<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
	<jsp:include page="../common/header.jsp"/>
	<jsp:include page="project_leftbar.jsp"/>
	<style>
		.table { width: 100%; margin-bottom: 30px; }
		table, td, th { border-collapse: collapse; }
		.notification { height: 100%; }
		caption { caption-side: top; }
		.notification-content { border: 1px solid #ccc; border-radius: 8px; padding: 10px; height: 480px;}<%--height--%>
	</style>
		<script>
    $(document).ready(function() {
        var projectName = "${left}";
        if (projectName) {
            $("#currentProjectName").text(projectName).show();
        }

        let currentProjectId, memberId;

        // 진행률 클릭 시
        $(".progress-rate.clickable").click(function() {
            currentProjectId = $(this).data("id"); // 프로젝트 ID
            memberId = $(this).data("memberid");   // 멤버 ID
            const currentRate = $(this).data("rate"); // 현재 진행률
            console.log("currentProjectId: " + currentProjectId);
            console.log("memberId: " + memberId);
            console.log("currentRate: " + currentRate);

            // 모달에 진행률 설정
            $("#progressInput").val(currentRate);
            $("#progressModal").modal("show");
        });

        // 진행률 저장 버튼 클릭 시
        $("#saveProgressBtn").click(function() {
            const newProgressRate = $("#progressInput").val(); // 새로 입력된 진행률
            console.log("New progress rate: " + newProgressRate);

            // 값이 없으면 요청을 중단
            if (!newProgressRate) {
                alert("진행률을 입력해 주세요.");
                return;
            }

            $.ajax({
                url: "${pageContext.request.contextPath}/project/updateprogressrate",
                type: "POST",
                data: { 
                    projectId: currentProjectId, 
                    memberId: memberId, 
                    memberProgressRate: newProgressRate 
                },
                success: function(response) {
                    console.log("Response from server: ", response);

                    if (response.success) {
                        // 서버에서 성공적으로 응답 받은 경우, 진행률을 업데이트
                        const progressRateElement = $(`.progress-rate[data-id='${currentProjectId.toString()}']`);
                        console.log("Progress rate element:", progressRateElement); // 요소가 제대로 선택되는지 확인

                        // 선택한 요소가 존재하는지 확인하고 진행률을 업데이트
                        if (progressRateElement.length > 0) {
                            progressRateElement.text(newProgressRate + "%");  // 진행률 텍스트 업데이트
                            progressRateElement.data("rate", newProgressRate);  // data 속성도 업데이트
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

    });
</script>

</head>
<body>
	<div class="container">
		<div class="d-flex justify-content-between align-items-center">
			<h3><c:out value="${left}"/></h3>
			<button class="btn btn-primary">관리</button>
		</div>
		<hr style="margin-top: 40px;"> <!-- 진행률과의 간격 추가 -->
		<div class="row">
			<div class="col-md-8">
				<table class="table">
					<caption><h5>진행률</h5></caption>
					<thead>
						<tr>
							<th>참여자</th>
							<th>업무내용</th>
							<th>진행률</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="project" items="${project}">
						    <tr>
						        <td>${project.participantNames}</td>
						        <td>${project.memberDesign}</td>
						        <td><span class="progress-rate <c:if test='${project.memberId == sessionScope.id}'>clickable</c:if>" 
								          data-id="${project.projectId}" data-memberid="${project.memberId}"
								          data-rate="${project.memberProgressRate}">
								    ${project.memberProgressRate}%</span>
					            </td>
						    </tr>
						</c:forEach>
					</tbody>
				</table>
				<table class="table">
					<caption><h5>진행 과정</h5></caption>
					<thead>
						<tr>
							<th>작성자</th>
							<th>제목</th>
							<th>작성일자</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="project" items="${project2}"> 
						    <tr>
								<td>${project.taskWriter}</td>
								<td>${project.taskTitle}</td>
								<td>${project.taskUpdateDate}</td>
							</tr>
						</c:forEach> 
					</tbody>
				</table>
				<a href="${pageContext.request.contextPath}/project/progress" class="btn btn-info btn-sm float-right">상세보기</a> <!-- 제일 우측 하단에 링크 -->
			</div>
			<div class="col-md-4 notification">
				<h5>알림</h5>
				<div class="notification-content">
					<p>알림 내용이 여기에 표시됩니다.</p>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 진행률 입력 모달 -->
	<div class="modal fade" id="progressModal" tabindex="-1" role="dialog" aria-labelledby="progressModalLabel" aria-hidden="true">
	    <div class="modal-dialog" role="document">
	        <div class="modal-content">
	            <div class="modal-header">
	                <h5 class="modal-title" id="progressModalLabel">진행률 수정</h5>
	                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	                    <span aria-hidden="true">&times;</span>
	                </button>
	            </div>
	            <div class="modal-body">
	                <input type="number" id="progressInput" class="form-control" min="0" max="100" placeholder="진행률을 입력하세요 (0-100)" />
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
	                <button type="button" class="btn btn-primary" id="saveProgressBtn">저장</button>
	            </div>
	        </div>
	    </div>
	</div>
</body>
</html>
