<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<meta charset="UTF-8">
	<title>진행 과정 상세</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
	<jsp:include page="../common/header.jsp"/>
	<jsp:include page="project_leftbar.jsp"/>
	<style>
		.table { width: 100%; margin: auto; }
		table, td, th { border-collapse: collapse; }
		h2 { text-align: left; color: black; margin: 0; }
		.btn-group { float: right; margin-left: 20px; }
		.caption-container { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; }
	</style>
</head>
<body>
	<div class="container">
		<div class="caption-container">
			<h2>진행 과정 상세</h2>
			<div class="btn-group">
				<c:if test="${user.role == '책임자'}">
					<button class="btn btn-primary">관리</button>
				</c:if>
				<button class="btn btn-success">글작성</button>
				<button class="btn btn-warning">수정</button>
				<button class="btn btn-danger">삭제</button>
			</div>
		</div>

		<table class="table">
			<thead>
				<tr>
					<th>번호</th>
					<th>작성자</th>
					<th>제목</th>
					<th>내용</th>
					<th>등록일</th>
					<th>변경일</th>
					<th>첨부파일</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><input type="checkbox">#001</td>
					<td>홍길동 책임 외 2명</td>
					<td>홍길동 책임 외 2명</td>
					<td>60%</td>
					<td>2024-02-05</td>
					<td>홍길동 책임 외 2명</td>
					<td>파일1.txt</td>
				</tr>
				<tr>
					<td><input type="checkbox">#002</td>
					<td>홍길동 책임 외 2명</td>
					<td>앱 프로젝트</td>
					<td>40%</td>
					<td>2024-02-05</td>
					<td>홍길동 책임 외 2명</td>
					<td>파일2.txt</td>
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>
