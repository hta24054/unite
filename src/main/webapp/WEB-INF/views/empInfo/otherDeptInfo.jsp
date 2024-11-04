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
table {
	width: 100%;
	border-collapse: collapse;
	margin: auto;
}
.table {
	
}
td, th {
	border-collapse: collapse;
	border: 1px solid black;
	padding: 25px;
	color: black;
	text-align: center;
	height: 50px;
	width: 14%;
}

h2 {
	text-align: left;
	color: rgb(51, 68, 102);
	margin: 0;
} /* h2의 기본 여백 제거 */
caption {
	caption-side: top;
	margin-bottom: 30px;
} /* 캡션과 테이블 간격 설정 */
</style>

</head>
<body>
	<div class="main-container">
		<div class="content">
			<table class="table">
				<caption>
					<h2>타 부서 인사정보</h2>
				</caption>

				<tr>
					<td rowspan="4" width="10%"></td>
					<th>이름</th>

					<th>성별</th>

					<th>이메일</th>

					<th>내선번호</th>

				</tr>
				<tr>
					<td>${empinfo.ename}</td>
					<td>${empinfo.gender}</td>
					<td>${empinfo.email}</td>
					<td>${empinfo.tel}</td>


				</tr>
				<tr>
					<th>소속</th>
					<th>사번</th>
					<th>부서/직책</th>
					<th>휴대폰번호</th>

				</tr>
				<tr>
					<td>${empinfo.company}</td>
					<td>${empinfo.deptId}</td>
					<td>${empinfo.deptName}${empinfo.jobName}</td>
					<td>${empinfo.mobile}</td>

				</tr>
			</table>


			<table class="">
				<tr>
					<th>입사일</th>
					<td>${empinfo.hireDate}</td>
					<th rowspan="2">계좌번호</th>
					<td rowspan="2">${empinfo.bank}${empinfo.account}</td>
					<th>긴급연락처</th>
					<td>${empinfo.mobile2}</td>
				</tr>

				<tr>
					<th>채용구분</th>
					<td>${empinfo.hireType}</td>
					<th>직원구분</th>
					<td>${empinfo.etype}</td>
				</tr>
				<tr>
					<th>생년월일</th>
					<td>${birthDay}</td>
					<th>주소</th>
					<td>${address}</td>
					<th rowspan="2">자격증</th>
					<td rowspan="2">${certName}</td>
				</tr>
				<tr>
					<th>최종학력</th>
					<td>${school}</td>
					<th>결혼여부</th>
					<td>${married }</td>
				</tr>
				<tr>
					<th>전공</th>
					<td>${major }</td>
					<th>자녀</th>
					<td>${child }</td>
					<th>외국어능력</th>
					<td>${langName }</td>
				</tr>


			</table>

			<form action="updateInfo" method="post">
				<input type="submit" value="수정">
			</form>
		</div>
	</div>
</body>
</html>