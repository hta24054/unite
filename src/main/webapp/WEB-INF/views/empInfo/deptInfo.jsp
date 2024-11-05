<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>empInfo</title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/empInfo.css">

<script
	src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<jsp:include page="../common/header.jsp" />
<jsp:include page="empInfo_leftbar.jsp" />
<meta charset="UTF-8">
<title>인사 정보</title>
<style>
.table {
	width: 20%;
}

body {
	font-family: Arial, sans-serif;
}

.content {
	margin: 20px;
}

h2 {
	text-align: left;
	color: rgb(51, 68, 102);
	margin: 0;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
}

th, td {
	border: 1px solid black;
	padding: 8px;
	text-align: center;
	color: black;
}

th {
	background-color: #f2f2f2;
	font-weight: bold;
}
</style>


</head>
<body>
	<div class="main-container">
		<div class="content">
			<table class="table">
				<h2>부서 인사정보</h2>
				<tr>
					<th width="50%">해당부서</th>
					<td>${empinfo.deptId}</td>
				</tr>
			</table>
			<table class="table2">
				<tr>
					<th>소속</th>
					<th>이름</th>
					<th>직위</th>
					<th>이메일</th>
					<th>내선 번호</th>
				</tr>

				<tbody>
					<c:forEach var="empinfo" items="${empinfoList}">
						<tr>
							<th>${empinfo.deptId}</th>
							<th>${empinfo.ename}</th>
							<th>${empinfo.jobName}</th>
							<th>${empinfo.email}</th>
							<th>${empinfo.tel}</th>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>