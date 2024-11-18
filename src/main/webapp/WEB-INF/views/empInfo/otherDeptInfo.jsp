<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>타 부서 인사정보</title>
<jsp:include page="../common/header.jsp" />
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.38.0/jquery.fancytree-all-deps.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.38.0/skin-win8/ui.fancytree.min.css">

<jsp:include page="empInfo_leftbar.jsp" />

<script>
	$(document)
			.ready(
					function() {
						if ($.fn.fancytree) {
							$("#tree").fancytree({
								source : [ {
									title : "대표이사",
									key : "대표이사",
									folder : true,
									children : [ {
										title : "부사장",
										key : "부사장",
										folder : true
									}, {
										title : "경영기획본부",
										key : "경영기획본부",
										folder : true,
										children : [ {
											title : "재무관리팀",
											key : "재무관리팀",
											folder : true
										}, {
											title : "인사관리팀",
											key : "인사관리팀",
											folder : true
										} ]
									}, {
										title : "SI사업본부",
										key : "SI사업본부",
										folder : true,
										children : [ {
											title : "신용평가팀",
											key : "신용평가팀",
											folder : true
										}, {
											title : "금융SI팀",
											key : "금융SI팀",
											folder : true
										}, {
											title : "비금융SI팀",
											key : "비금융SI팀",
											folder : true
										}, {
											title : "SM팀",
											key : "SM팀",
											folder : true
										} ]
									}, {
										title : "영업본부",
										key : "영업본부",
										folder : true,
										children : [ {
											title : "솔루션영업팀",
											key : "솔루션영업팀",
											folder : true
										}, {
											title : "SI영업팀",
											key : "SI영업팀",
											folder : true
										}, {
											title : "SM영업팀",
											key : "SM영업팀",
											folder : true
										} ]
									}, {
										title : "R&D본부",
										key : "R&D본부",
										folder : true,
										children : [ {
											title : "연구개발팀",
											key : "연구개발팀",
											folder : true
										} ]
									} ]
								} ],
								click : function(event, data) {
									const department = data.node.key;
									console.log("선택된 부서: " + department);
									loadEmployees(department); // 선택된 부서명을 사용
								}
							});
						} else {
							console.error('fancytree 플러그인이 로드되지 않았습니다.');
						}

						function loadEmployees(department) {
							console.log("AJAX 요청 시작: 부서명 - " + department);
							$
									.ajax({
										url : '${pageContext.request.contextPath}/empInfo/viewotherdept',
										method : 'GET',
										data : {
											departmentName : department
										}, // 동적으로 선택된 부서명을 사용
										success : function(data) {
											updateEmployeeTable(data,
													department);
										},
										error : function() {
											console.log("AJAX 요청 실패");
										}
									});
						}

						function updateEmployeeTable(data, department) {
							const tableBody = $('#employeeTableBody');
							tableBody.empty();
							if (department) {
								$('#deptName').text(department);
							} else {
								$('#deptName').text('부서 선택');
							}
							$.each(data, function(key, value) {
								var html = "<tr>";
								html += "<td>" + value.mobile + "</td>";
								html += "<td><a href='"
										+ '${pageContext.request.contextPath}'
										+ "/empInfo/view?id=" + value.empId
										+ "'>" + value.ename + "</a></td>";
								html += "<td>" + value.school + "</td>";
								html += "<td>" + value.email + "</td>";
								html += "<td>" + value.tel + "</td>";
								html += "</tr>";
								tableBody.append(html);
							});
						}
					});
</script>
<style>
.main-container {
	display: flex;
	gap: 50px; /* 간격을 줄임 */
	justify-content: center;
}

.content {
	margin-left: 0;
	width: 30%; /* 조직도의 너비 조정 */
}

#tree {
	margin-top: 100px;
	border: 2px solid black;
	width: 100%;
	overflow-y: auto; /* 세로 스크롤 추가 */
	margin-bottom: 30px;
	margin-right: auto;
	border: 2px solid black; /* 왼쪽 여백 제거 */
}

#employeeTableContainer {
	width: 70%; /* 정보 테이블의 너비 조정 */
	margin-top: 100px;
	margin-left: auto; /* 오른쪽 여백 제거 */
}

h2 {
	margin-top: 50px;
	text-align: left;
	color: rgb(51, 68, 102);
	margin-left: auto;
	text-align: left; /* 제목의 왼쪽 여백 제거 */
}

table {
	width: 70%;
	border-collapse: collapse;
	margin-top: 20px;
}

#deptNameTable {
	width: 30%; /* deptName 테이블의 너비 조정 */
	margin-top: 10px;
}

#deptName {
	width: 50%;
	border: 2px solid black; /* 추가된 스타일 */
}

#employeeTableHead th {
	border: 2px solid black; /* 추가된 스타일 */
}

#employeeTableBody td {
	border: 2px solid black; /* 추가된 스타일 */
}

#employeeTableBody tr {
	border: 2px solid black; /* 추가된 스타일 */
}

th, td {
	border: 2px solid black; /* 추가된 스타일 */
	padding: 8px;
	text-align: center;
	color: black;
}

th {
	background-color: #f2f2f2;
	font-weight: bold;
}

#tree_table {
	text-align: center;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	text-align: center;
	width: 1000px;
}
</style>
</head>
<body>
	<div class="container mt-4">
		<div class="main-container">
			<div class="content">
				<h2 class="title">타 부서 인사정보</h2>
				<div id="tree"></div>
				<!-- 조직도 영역 -->
			</div>
			<div id="employeeTableContainer">
				<h5 class="mt-4"></h5>
				<table id="deptNameTable">
					<tr>
						<th>해당부서</th>
						<td id="deptName">부서 선택</td>
					</tr>
				</table>
				<table class="table table-bordered mt-4" id="tree_table">
					<thead id="employeeTableHead">
						<tr>
							<th>소속</th>
							<th>이름</th>
							<th>직위</th>
							<th>이메일</th>
							<th>내선 번호</th>
						</tr>
					</thead>
					<tbody id="employeeTableBody">
						<!-- 직원 정보가 AJAX로 로드되어 여기에 추가됩니다. -->
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>
