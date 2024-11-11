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
	href="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.38.0/skin-win8/ui.fancytree.min.css" />

<style>
.content-container {
	display: block;
}

#tree {
	margin-bottom: 30px;
}

#employeeTableContainer {
	margin-top: 30px;
}
</style>
<script>
	$(document)
			.ready(
					function() {
						$("#tree").fancytree({
							source : [ {
								title : "대표이사",
								folder : true,
								children : [ {
									title : "부사장",
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
								loadEmployees(department);
							}
						});
						function loadEmployees(department) {
						    console.log("AJAX 요청 시작: 부서명 - " + department);
						    $.ajax({
						        url: '/empInfo/viewotherdept',
						        method: 'GET',
						        data: { departmentName: 'SI사업본부' }, // 테스트를 위해 부서명 직접 설정
						        success: function(data) {
						            console.log("AJAX 요청 성공: 데이터 - " + JSON.stringify(data));
						            updateEmployeeTable(data);
						        },
						        error: function() {
						            console.log("AJAX 요청 실패");
						            alert('직원 정보를 불러오는 데 실패했습니다.');
						        }
						    });
						}


						function updateEmployeeTable(data) {
							console.log("테이블 업데이트: 데이터 - "
									+ JSON.stringify(data));
							const tableBody = $('#employeeTableBody');
							tableBody.empty();

							$
									.each(
											data,
											function(index, emp) {
												var html = "<tr>";
												html += "<td>" + emp.deptName
														+ "</td>";
												html += "<td><a href='${pageContext.request.contextPath}/empInfo/view?id="
														+ emp.empId
														+ "'>"
														+ emp.ename
														+ "</a></td>";
												html += "<td>" + emp.jobName
														+ "</td>";
												html += "<td>" + emp.email
														+ "</td>";
												html += "<td>" + emp.tel
														+ "</td>";
												html += "</tr>";
												tableBody.append(html);
											});
						}
					});
</script>
</head>
<body>
	<div class="container mt-4">
		<h5>조직도</h5>
		<div class="content-container">
			<div id="tree"></div>
			<!-- 조직도 영역 -->
			<div id="employeeTableContainer">
				<h5 class="mt-4">직원 목록</h5>
				<table class="table table-bordered mt-4">
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
