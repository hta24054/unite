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
		.table{width: 70%; margin: auto;}
		table, td, th{border-collapse : collapse;}
		h2 { text-align: left; color: black; margin: 0; } /* h2의 기본 여백 제거 */
        caption { caption-side: top; margin-bottom: 30px; } /* 캡션과 테이블 간격 설정 */
	</style>
</head>
<body>
	
	<!-- 임시. 폼 -->
	<table class="table">
		<caption><h2>진행 중 프로젝트<h2></caption>
		<thead>
			<tr>
				<th>코드명</th>
				<th>프로젝트이름</th>
				<th>참여자</th>
				<th>진행률</th>
				<th>마감일</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>#001</td>
				<td><a href="${pageContext.request.contextPath}/project/detail">홈페이지</a></td>
				<td>홍길동 책임 외 2명</td>
				<td>60%</td>
				<td>2024-02-05</td>
			</tr>
			<tr>
				<td>#002</td>
				<td>앱 프로젝트</td>
				<td>홍길동 책임 외 2명</td>
				<td>40%</td>
				<td>2024-02-05</td>
			</tr>
		</tbody>
	</table>
</body>
</html>