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
	<script>
	    var contextPath = "${pageContext.request.contextPath}";
	    var projectName = "${left}";
	</script>
	<script src="${pageContext.request.contextPath }/js/project_detail.js"></script>
	<jsp:include page="../common/header.jsp"/>
	<jsp:include page="project_leftbar.jsp"/>
	<style>
		.table { width: 100%; margin-bottom: 30px; }
		table, td, th { border-collapse: collapse; }
		.notification { height: 100%; }
		caption { caption-side: top; }
		.notification-content { border: 1px solid #ccc; border-radius: 8px; padding: 10px; height: 480px;}<%--height--%>
	</style>
</head>
<body>
	<div class="container">
		<div class="d-flex justify-content-between align-items-center">
			<h3><c:out value="${left}"/></h3>
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
						        <td><span class="task-content <c:if test='${project.isManager}'>clickable</c:if>" 
						                  data-id="${project.projectId}" 
						                  data-memberid="${project.memberId}" 
						                  data-content="${project.memberDesign}">
						                ${project.memberDesign}업무가 지정되지 않았습니다
						            </span>
				                </td>
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
	
	<!-- 업무 내용 입력 모달 -->
	<div class="modal fade" id="taskContentModal" tabindex="-1" role="dialog" aria-labelledby="taskContentModalLabel" aria-hidden="true">
	    <div class="modal-dialog" role="document">
	        <div class="modal-content">
	            <div class="modal-header">
	                <h5 class="modal-title" id="taskContentModalLabel">업무 내용 수정</h5>
	                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	                    <span aria-hidden="true">&times;</span>
	                </button>
	            </div>
	            <div class="modal-body">
	                <input type="text" id="taskContentInput" class="form-control" placeholder="업무 내용을 입력하세요" />
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
	                <button type="button" class="btn btn-primary" id="saveTaskContentBtn">저장</button>
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