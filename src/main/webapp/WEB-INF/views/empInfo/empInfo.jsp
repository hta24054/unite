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
	width: 70%;
	margin: auto;
}

table, td, th {
	border-collapse: collapse;
}

h2 {
	text-align: left;
	color: black;
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
					<h2>나의 인사정보</h2>
				</caption>

				<tr>
					<th>이름</th>
					<td>${empinfo.ename}</td>
					<th>성별</th>
					<td>${empinfo.gender}</td>
					<th>이메일</th>
					<td>${empinfo.email}</td>
					<th>내선번호</th>
					<td>${empinfo.tel}</td>
				</tr>
				<tr>
					<th>소속</th>
					<th>${empinfo.company}</td>
					<th>부서/직책</th>
					<td>${deptName}  "" + 책임</td>
					<th>휴대폰번호</th>
					<td>010-1234-1234</td>
				</tr>

				<tr>
					<th>입사일</th>
					<td>2011/04/01</td>
					<th>채용구분</th>
					<td>경력</td>
					<th>계좌번호</th>
					<td>국민은행 1234-1234-41233</td>
					<th>긴급연락처</th>
					<td>010-3333-5555</td>
				</tr>

				<tr>
					<th>직원구분</th>
					<td>정규직</td>
					<th>생년월일</th>
					<td>1978-04-06</td>
					<th>주소</th>
					<td>서울특별시 성동구 왕십리로 241</td>
				</tr>
				<tr>
					<th>최종학력</th>
					<td>연세대학교</td>
					<th>결혼여부</th>
					<td>N</td>
					<th>자격증</th>
					<td>정보처리기사/PMP/CISA</td>
					<th>전공</th>
					<td>컴퓨터 공학과</td>
					<th>외국어능력</th>
					<td>중국어</td>
				</tr>


			</table>

			<form action="updateInfo" method="post">
				<input type="submit" value="수정">
			</form>
		</div>
	</div>
</body>
</html>