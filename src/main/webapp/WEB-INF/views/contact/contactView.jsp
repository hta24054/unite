<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Address Book</title>
<jsp:include page="../common/header.jsp" />
<jsp:include page="contact_leftbar.jsp" />

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

<style>
table {
	width: 100%;
	border-collapse: collapse;
}


</style>
</head>
<body>

	<h1>Address Book</h1>
	<!-- JSP에서 contextPath를 JavaScript 변수로 설정 -->
	<!-- JavaScript 코드 시작 -->
	<script>
		// 페이지 로드 시 모든 사원의 주소록 정보를 가져옵니다.
		$(document).ready(function() {
			loadContacts('ename'); // 초기 정렬 기준을 'ename'으로 설정합니다.
		});

		function loadContacts(orderBy) {
			console.log('orderBy 파라미터:', orderBy); // 추가 로그

			if (!orderBy) {
				console.error('orderBy 파라미터가 설정되지 않았습니다.');
				return;
			}

			$.ajax({
				url : `${pageContext.request.contextPath}/contact/view`,
				method : 'GET',
				data : {
					orderBy : orderBy
				},
				success : function(data) {
					console.log('데이터를 성공적으로 가져왔습니다:', data); // 응답 데이터 로그
					const tbody = $('#employeeTableBody');
					tbody.empty();

					$.each(data, function(index, contact) {
						const row = $('<tr></tr>');
						row.append(`<td>${contact.emp.ename}</td>`);
						row.append(`<td>${contact.emp.mobile}</td>`);
						row.append(`<td>${contact.emp.tel}</td>`);
						row.append(`<td>${contact.dept.deptName}</td>`);
						row.append(`<td>${contact.job.jobName}</td>`);
						tbody.append(row);
					});
				},
				error : function() {
					console.error('AJAX 요청 실패');
				}
			});
		}

		function loadContactsByDept(departmentName) {
			console.log('departmentName 파라미터:', departmentName); // 추가 로그

			if (!departmentName) {
				console.error('departmentName 파라미터가 설정되지 않았습니다.');
				return;
			}

			$.ajax({
				url : `${pageContext.request.contextPath}/contact/viewdept`,
				method : 'GET',
				data : {
					departmentName : departmentName
				},
				success : function(data) {
					console.log('데이터를 성공적으로 가져왔습니다:', data); // 응답 데이터 로그
					const tbody = $('#employeeTableBody');
					tbody.empty();

					$.each(data, function(index, contact) {
						const row = $('<tr></tr>');
						row.append(`<td>${contact.emp.ename}</td>`);
						row.append(`<td>${contact.emp.mobile}</td>`);
						row.append(`<td>${contact.emp.tel}</td>`);
						row.append(`<td>${contact.dept.deptName}</td>`);
						row.append(`<td>${contact.job.jobName}</td>`);
						tbody.append(row);
					});
				},
				error : function() {
					console.error('AJAX 요청 실패');
				}
			});
		}

		function searchByName() {
			const name = $('#searchInput').val();
			console.log('name 파라미터:', name); // 추가 로그

			$.ajax({
				url : `${pageContext.request.contextPath}/contact/viewname`,
				method : 'GET',
				data : {
					name : name
				},
				success : function(data) {
					console.log('데이터를 성공적으로 가져왔습니다:', data); // 응답 데이터 로그
					const tbody = $('#employeeTableBody');
					tbody.empty();

					$.each(data, function(index, contact) {
						const row = $('<tr></tr>');
						row.append(`<td>${contact.emp.ename}</td>`);
						row.append(`<td>${contact.emp.mobile}</td>`);
						row.append(`<td>${contact.emp.tel}</td>`);
						row.append(`<td>${contact.dept.deptName}</td>`);
						row.append(`<td>${contact.job.jobName}</td>`);
						tbody.append(row);
					});
				},
				error : function() {
					console.error('AJAX 요청 실패');
				}
			});
		}

		$(document).ready(function() {
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
						loadContactsByDept(department); // 선택된 부서명을 사용
					}
				});
			} else {
				console.error('fancytree 플러그인이 로드되지 않았습니다.');
			}
		});
	</script>
	<div>
		<label for="searchInput">이름 검색:</label> <input type="text"
			id="searchInput" onkeyup="searchByName()" placeholder="이름을 입력하세요">
	</div>
	<div>
		<label for="deptSelect">부서 선택:</label> <select id="deptSelect"
			onchange="loadContactsByDept()">
			<option value="1">부서 1</option>
			<option value="2">부서 2</option>
			<!-- 필요한 부서 옵션 추가 -->
		</select>
	</div>
	<button onclick="loadContacts('ename')">이름순</button>
	<button onclick="loadContacts('job_name')">직급순</button>
	<table id="contactTable">
		<thead>
			<tr>
				<th data-orderby="ename">이름</th>
				<th data-orderby="mobile">휴대폰</th>
				<th data-orderby="tel">내선번호</th>
				<th data-orderby="deptName">부서</th>
				<th data-orderby="jobName">직급</th>
			</tr>
		</thead>
		<tbody id="employeeTableBody">
			<!-- 직원 정보가 여기에 표시됩니다 -->
		</tbody>
	</table>

	<!-- 조직도 -->
	<div id="tree"></div>


	<!-- JavaScript 코드 끝 -->
</body>
</html>
