<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>부서 인사정보</title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<script
	src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.38.0/jquery.fancytree-all-deps.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.38.0/skin-win8/ui.fancytree.min.css">

<jsp:include page="empInfo_leftbar.jsp" />

<style>
.main-container {
    display: flex;
    gap: 10px; /* 간격을 줄임 */
}
.content {
    width: 35%; /* 조직도의 너비 조정 */
    margin-right: 0; /* 오른쪽 여백 제거 */
}
#tree {
    border: 2px solid black;
    height: 1000px; /* 고정된 높이 설정 */
    overflow-y: auto; /* 세로 스크롤 추가 */
    margin-bottom: 30px;
    margin-left: 0; /* 왼쪽 여백 제거 */
}
#employeeTableContainer {
    width: 65%; /* 정보 테이블의 너비 조정 */
    margin-top: 30px;
    margin-right: 0; /* 오른쪽 여백 제거 */
}
h2 {
    text-align: left;
    color: rgb(51, 68, 102);
    margin-left: 0; /* 제목의 왼쪽 여백 제거 */
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
.title {
    color: #334466;
    font-size: 18px;
    margin: 0;
    font-weight: bold;
    border-bottom: 1px solid black;
    padding-bottom: 10px;
}
#tree_table {
    text-align: center;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}
</style>




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
											updateEmployeeTable(data);
										},
										error : function() {
											console.log("AJAX 요청 실패");
										}
									});
						}

						function updateEmployeeTable(data) {
							const tableBody = $('#employeeTableBody');
							tableBody.empty();
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
				<table class="table table-bordered mt-4" id="tree_table">
					<thead>
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
