<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>나의 인사정보</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/empInfo.css">
<script
	src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
<jsp:include page="../common/header.jsp" />
<jsp:include page="empInfo_leftbar.jsp" />
<html>
<head>
<style>
table, .table {
	width: 90%;
	border-collapse: collapse;
	margin: 0 5%;
	border-radius: 10px;
}

/* 이미지 크기를 고정 */
#photo img {
    width: 170px; /* 원하는 너비로 설정 */
    height: 200px; /* 원하는 높이로 설정 */
    object-fit: cover; /* 이미지가 셀 크기에 맞게 잘리더라도 유지 */
}

/* 테이블 셀 크기를 고정 */
#photo {
    width: 7%; /* 셀 너비 고정 */
    height: 7%; /* 셀 높이 고정 */
    vertical-align: middle; /* 수직 정렬 설정 */
}

th {
	background-color: #f2f2f2;
	font-weight: bold;
}

table {
	border-radius: 10px;
	margin-top: 10px;
}

td, th {
	border: 2px solid black;
	padding: 10px;
	color: black;
	vertical-align: middle;
	text-align: center;
	width: 14%;
}

#tr {
	border-top: 2px solid black;
}

.table th {
	padding: 10px;
}

.table td {
	vertical-align: middle;
	padding: 5px;
}

h2 {
	text-align: left;
	color: rgb(51, 68, 102);
	margin: 0;
} /* h2의 기본 여백 제거 */
caption {
	caption-side: top;
	margin-bottom: 15px;
} /* 캡션과 테이블 간격 설정 */
input[readonly] {
	border: none;
	text-align: center;
} /* 테두리 제거 */
select[disabled] {
	border: none; /* 기본 테두리 제거 */
	outline: none; /* 포커스 테두리 제거 */
	background-color: transparent; /* 배경색 제거 */
	-webkit-appearance: none; /* 웹킷 브라우저 (Chrome, Safari 등) 기본 스타일 제거 */
	-moz-appearance: none; /* Firefox 브라우저 기본 스타일 제거 */
	appearance: none; /* 기타 브라우저 기본 스타일 제거 */
	color: black;
	text-align: center;
}

button#editButton {
	background-color: green;
	border: 0px;
	margin-top: 1%;
	border-radius: 10px;
	color: white;
	border-radius: 10px;
}

button#cancelButton {
	background-color: red;
	border: 0px;
	border-radius: 10px;
	color: white;
}

button#saveButton {
	margin-right: 5%;
	margin-top: 1%;
	border: 0px;
	border-radius: 10px;
}

button#saveButton:disabled:hover {
	background-color: white; /* 비활성화 상태에서도 원래 배경색 유지 */
	cursor: not-allowed;
} /* 비활성화 상태임을 나타내는 커서 */
</style>

<script>
	$(document).ready(
			function() {
				var originalValues = {};

				// 원래 값을 저장하는 함수
				function saveOriginalValues() {
					$(".editable").each(function() {
						var inputName = $(this).attr("name");
						originalValues[inputName] = $(this).val();
					});
				}

				// 유효성 검사 함수
				function validateEmail(email) {
					const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
					return emailRegex.test(email);
				}

				function validatePhoneNumber(phoneNumber) {
					const phoneNumberRegex = /^\d{2,4}-\d{3,4}-\d{4}$/;
					return phoneNumberRegex.test(phoneNumber);
				}

				function validateTelNumber(telNumber) {
					const telNumberRegex = /^\d{2,3}-\d{4}-\d{4}$/;
					return telNumberRegex.test(telNumber);
				}

				function validateForm() {
					const $email = $("#email");
					const $phoneNumber = $("#mobile");
					const $telNumber = $("#tel");
					const $emergencyNumber = $("#mobile2");

					let isValid = true;

					if(!validateEmail($email.val())) {
						alert("유효한 이메일을 입력하세요.");
						$email.focus();
						isValid = false;
					}

					if (!validatePhoneNumber($phoneNumber.val())) {
						alert("유효한 휴대폰 번호를 입력하세요. 예: 010-1234-5678");
						$phoneNumber.focus();
						isValid = false;
					}

					if (!validateTelNumber($telNumber.val())) {
						alert("유효한 내선 번호를 입력하세요. 예: 123-4567-8910");
						$telNumber.focus();
						isValid = false;
					}

					if (!validatePhoneNumber($emergencyNumber.val())) {
						alert("유효한 긴급 연락처를 입력하세요. 예: 010-1234-5678");
						$emergencyNumber.focus();
						isValid = false;
					}

					return isValid;
				}

				// 수정 버튼 클릭 시
				$("#editButton").click(
						function() {
							if ($(this).text() === "수정") {
								saveOriginalValues();
								$(".editable").removeAttr("readonly")
										.removeAttr("disabled").css("border",
												"1px solid #000");
								$(this).text("취소").css("background-color",
										"red").attr("id", "cancelButton");
								$("#saveButton").removeAttr("disabled"); // 저장 버튼 활성화
							} else if ($(this).text() === "취소") {
								$(".editable").each(
										function() {
											var inputName = $(this)
													.attr("name");
											$(this).val(
													originalValues[inputName]);
											$(this)
													.attr("readonly",
															"readonly").attr(
															"disabled",
															"disabled").css(
															"border", "none");
										});
								$(this).text("수정").css("background-color",
										"green").attr("id", "editButton");
								$("#saveButton").attr("disabled", "disabled"); // 저장 버튼 비활성화
							}
						});

				// 저장 버튼 클릭 시 유효성 검사
				$("#saveButton").click(function() {
					return validateForm();
				});
			});
</script>


</head>
<body>
	<div class="main-container">
		<div class="content">

			<%-- URL 파라미터로 전달된 empId 값과 현재 로그인한 사용자의 정보를 비교 --%>
			<%-- URL 파라미터로 전달된 empId 값과 현재 로그인한 사용자의 정보를 비교 --%>
			<c:set var="paramEmpId" value="${details.emp.empId}" />
			<c:set var="currentEmpId" value="${sessionScope.id}" />
			<c:set var="paramDeptId" value="${details.emp.deptId}" />
			<c:set var="currentDeptId" value="${sessionScope.deptId}" />


			<%-- 상단 텍스트 설정 --%>
			<c:choose>
				<c:when test="${paramEmpId.toString() == currentEmpId.toString()}">
					<c:set var="pageTitle" value="나의 인사정보" />
				</c:when>
				<c:when test="${paramDeptId.toString() == currentDeptId.toString()}">
					<c:set var="pageTitle" value="부서원 인사정보" />
				</c:when>
				<c:otherwise>
					<c:set var="pageTitle" value="타 부서원 인사정보" />
				</c:otherwise>
			</c:choose>



			<form action="${pageContext.request.contextPath}/empInfo/update"
				method="post">
				<input type="hidden" name="id" value="${details.emp.empId}">
				<table class="table">
					<caption>
						<h2>${pageTitle}</h2>
					</caption>
					<tr id="tr">
						<th rowspan="4" id="photo"><img
							src="${pageContext.request.contextPath}/emp/profile-image?UUID=${details.emp.imgUUID}"></th>
						<th>이름</th>
						<th>성별</th>
						<th>이메일</th>
						<th>내선번호</th>
					</tr>
					<tr>
						<td>${details.emp.ename}</td>
						<td><select name="gender" disabled>
								<option value="남" ${details.emp.gender == '남' ? 'selected' : ''}>남성</option>
								<option value="여" ${details.emp.gender == '여' ? 'selected' : ''}>여성</option>
						</select></td>
						<td><input type="text" name="email" class="editable"
							id="email" value="${details.emp.email}" readonly></td>
						<td><input type="text" name="tel" class="editable" id="tel"
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
							id="mobile" value="${details.emp.mobile}" readonly></td>
					</tr>
				</table>

				<table>
					<tr>
						<th>입사일</th>
						<td><input type="text" name="hiredate"
							placeholder="YYYY/MM/DD" value="${details.emp.hireDate}" readonly></td>

						<th rowspan="2">계좌번호</th>
						<td rowspan="2">${details.emp.bank}<br>${details.emp.account}</td>
						<th>긴급연락처</th>
						<td><input type="text" name="mobile2" class="editable"
							id="mobile2" value="${details.emp.mobile2}" readonly></td>
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
						<th>자격증</th>
						<td><c:forEach var="cert" items="${details.certList}">
								<span>${cert.certName}</span>
								<br />
							</c:forEach></td>


					</tr>

					<tr>
						<th>최종학력</th>
						<td>${details.emp.school}</td>
						<th>결혼여부</th>
						<td><select name="married" class="editable" disabled>
								<option value="Y"
									${details.emp.married == '1' ? 'selected' : ''}>Y</option>
								<option value="N"
									${details.emp.married == '0' ? 'selected' : ''}>N</option>
						</select></td>
						<th rowspan="2">외국어능력</th>
						<td rowspan="2"><c:forEach var="lang"
								items="${details.langList}">
								<span>${lang.langName}</span>
								<br />
							</c:forEach></td>
					</tr>
					<tr>
						<th>전공</th>
						<td>${details.emp.major}</td>
						<th>자녀</th>
						<td><select name="child" disabled>
								<option value="1" ${details.emp.child == '1' ? 'selected' : ''}>Y</option>
								<option value="0" ${details.emp.child == '0' ? 'selected' : ''}>N</option>
						</select></td>

					</tr>
				</table>

				<c:if test="${details.emp.empId == sessionScope.id}">
					<button type="button" id="editButton">수정</button>
					<button type="submit" id="saveButton" disabled>저장</button>
				</c:if>


			</form>
		</div>
	</div>
</body>
</html>