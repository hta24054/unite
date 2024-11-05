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
			// 서버에서 전달된 프로젝트 이름이 존재할 때
			var projectName = "${left}";
			if (projectName) {
				// hidden 요소를 보이게 하고, 프로젝트 이름을 삽입
				$("#currentProjectName").text(projectName).show();
			}
		});
	</script>
</head>
<body>
	<div class="container">
		<div class="d-flex justify-content-between align-items-center">
			<h3>홈페이지 프로젝트</h3>
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
						        <td>${project.memberProgressRate}</td>
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
						<tr>
							<td>#002</td>
							<td>이순신 책임</td>
							<td>파일2.txt</td>
						</tr>
						<tr>
							<td>#003</td>
							<td>강감찬 책임</td>
							<td>파일3.txt</td>
						</tr>
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
</body>
</html>
