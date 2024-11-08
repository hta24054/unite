<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>empinfo</title>
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
<script>
	$(document)
			.ready(
					function() {
						$("#editButton")
								.click(
										function() {
											$(".editable").removeAttr(
													"readonly").removeAttr(
													"disabled").css("border",
													"1px solid #000");
										});
					});
</script>
</head>
<body>
	<div class="main-container">
		<div class="content">
			<form action="${pageContext.request.contextPath}/empInfo/update"
				method="post">
				<input type="hidden" name="id" value="${details.emp.empId}">
				<table class="table">
					<caption>
						<h2>나의 인사정보</h2>
					</caption>
					<tr>
						<td rowspan="4" width="200"><img
							src="${pageContext.request.contextPath}/${details.emp.imgPath}" alt="${details.emp.ename}의 사진"
							width="200" height="200"></td>
						<th>이름</th>
						<th>성별</th>
						<th>이메일</th>
						<th>내선번호</th>
					</tr>
					<tr>
						<td>${details.emp.ename}</td>
						<td><select name="gender" disabled>
								<option value="남"
									${details.emp.gender == '남' ? 'selected' : ''}>남성</option>
								<option value="여"
									${details.emp.gender == '여' ? 'selected' : ''}>여성</option>
						</select></td>
						<td><input type="text" name="email" class="editable"
							value="${details.emp.email}" readonly></td>
						<td><input type="text" name="tel" class="editable"
							value="${details.emp.tel}" readonly></td>
					</tr>
					<tr>
						<th>부서</th>
						<th>사번</th>
						<th>직책</th>
						<th>휴대폰번호</th>
					</tr>
					<tr>
						<td>${details.dept.deptName}</td>
						<td>${details.emp.empId}</td>
						<td>${details.job.jobName}</td>
						<td><input type="text" name="mobile" class="editable"
							value="${details.emp.mobile}" readonly></td>
					</tr>
				</table>

				<table>
					<tr>
						<th>입사일</th>
						<td><input type="text" name="hiredate"
							placeholder="YYYY/MM/DD" value="${details.emp.hireDate}" readonly></td>

						<th rowspan="2">계좌번호</th>
						<td rowspan="2">${details.emp.bank}${details.emp.account}</td>
						<th>긴급연락처</th>
						<td><input type="text" name="mobile2" class="editable"
							value="${details.emp.mobile2}" readonly></td>
					</tr>
					<tr>
						<th>채용구분</th>
						<td><select name="hiretype" disabled>
								<option value="경력"
									${details.emp.hireType == '경력' ? 'selected' : ''}>경력</option>
								<option value="신입"
									${details.emp.hireType == '신입' ? 'selected' : ''}>신입</option>
								<option value="인턴"
									${details.emp.hireType == '인턴' ? 'selected' : ''}>인턴</option>
						</select></td>
						<th>직원구분</th>
						<td><select name="etype" disabled>
								<option value="정규직"
									${details.emp.etype == '정규직' ? 'selected' : ''}>정규직</option>
								<option value="계약직"
									${details.emp.etype == '계약직' ? 'selected' : ''}>계약직</option>
								<option value="퇴직"
									${details.emp.etype == '퇴직' ? 'selected' : ''}>퇴직</option>
						</select></td>
					</tr>
					<tr>
						<th>생년월일</th>
						<td><input type="text" name="birthday"
							placeholder="YYYY/MM/DD" value="${details.emp.birthday}" readonly>
							<select name="birthday_type" disabled>
								<option value="양력"
									${details.emp.birthdayType == '양력' ? 'selected' : ''}>양력</option>
								<option value="음력"
									${details.emp.birthdayType == '음력' ? 'selected' : ''}>음력</option>
						</select></td>
						<th>주소</th>
						<td><input type="text" name="address" class="editable"
							value="${details.emp.address}" readonly></td>
						<th rowspan="2">자격증</th>
						<td rowspan="2"><c:forEach var="cert"
								items="${details.certList}">
								<span>${cert.certName}</span>
								<br />
							</c:forEach></td>


					</tr>

					<tr>
						<th>최종학력</th>
						<td>${details.emp.school}</td>
						<th>결혼여부</th>
						<td><select name="married" class="editable" disabled>
								<option value="1"
									${details.emp.married == '1' ? 'selected' : ''}>Y</option>
								<option value="0"
									${details.emp.married == '0' ? 'selected' : ''}>N</option>
						</select></td>
					</tr>
					<tr>
						<th>전공</th>
						<td>${details.emp.major}</td>
						<th>자녀</th>
						<td><select name="child" disabled>
								<option value="1" ${details.emp.child == '1' ? 'selected' : ''}>Y</option>
								<option value="0" ${details.emp.child == '0' ? 'selected' : ''}>N</option>
						</select></td>
						<th>외국어능력</th>
						<td><c:forEach var="lang" items="${details.langList}">
								<span>${lang.langName}</span>
								<br />
							</c:forEach></td>
					</tr>
				</table>

				<button type="button" id="editButton">수정</button>
				<button type="submit">저장</button>
			</form>
		</div>
	</div>

</body>
</html>
