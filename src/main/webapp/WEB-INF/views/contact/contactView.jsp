<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>주소록 조회</title>
<jsp:include page="../common/header.jsp" />
<jsp:include page="contact_leftbar.jsp" />


<script
	src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>

<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.38.0/jquery.fancytree-all-deps.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.38.0/skin-win8/ui.fancytree.min.css">

<style>
.content-container {
	display: flex;
	margin-right: 0px;
	gap: 100px;
}

#treeContainer {
	width: 45%;
}

#employeeTableContainer {
	width: 55%;
	margin-right: 0%;
}

.table {
	width: 100%;
	table-layout: auto; /* 자동으로 열 너비 조정 */
}

.title {
	color: #334466;
	font-size: 18px;
	font-weight: bold;
	border-bottom: 1px solid black;
	padding-bottom: 10px;
	margin-bottom: 25px;
}

#tree_table {
	text-align: center;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

#tree {
	border: 1px solid black;
}

.table-container {
	max-height: 600px;
	overflow-y: auto;
}

th, td {
	text-align: center;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

#searchInput {
	border: 1px solid black;
}

.btn.active {
	background-color: #28a745 !important; /* 활성화된 버튼 배경색 */
	color: black !important; /* 활성화된 버튼 글자색 */
	
	border: 1px solid #007bb5 !important; /* 활성화된 버튼 테두리 */
}
</style>
</head>
<body>
	<div class="container mt-4">
		<div class="content-container">
			<div id="treeContainer">
				<h5 class="title" id="tree_title">조직도</h5>
				<div id="tree"></div>
			</div>

			<div id="employeeTableContainer">
				<h5 class="title">직원 목록</h5>

				<input type="text" id="searchInput" class="form-control mb-3"
					placeholder="이름을 검색하세요">
				<div class="btn-group mb-3">
					<button class="btn btn-primary" id="nameSortBtn">이름순</button>
					<button class="btn btn-secondary" id="jobSortBtn">직급순</button>
				</div>
				<div class="table-container">
					<table class="table table-bordered mt-4" id="tree_table">
						<thead id="employeeTableHead">
							<tr>
								<th style="display: none;">id</th>
								<th>이름</th>
								<th>부서</th>
								<th>직급</th>
								<th>휴대폰</th>
								<th>내선번호</th>
							</tr>
						</thead>
						<tbody id="employeeTableBody">
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<script>
		$(document).ready(function() {
			$("#tree").fancytree({
				source : [ {
					title : "대표이사",
					key : "1000",
					folder : true,
					children : [ {
						title : "부사장",
						key : "1001",
						folder : true
					}, {
						title : "경영기획본부",
						key : "1100",
						folder : true,
						children : [ {
							title : "재무관리팀",
							key : "1110",
							folder : true
						}, {
							title : "인사관리팀",
							key : "1120",
							folder : true
						} ]
					}, {
						title : "SI사업본부",
						key : "1200",
						folder : true,
						children : [ {
							title : "신용평가팀",
							key : "1210",
							folder : true
						}, {
							title : "금융SI팀",
							key : "1220",
							folder : true
						}, {
							title : "비금융SI팀",
							key : "1230",
							folder : true
						}, {
							title : "SM팀",
							key : "1240",
							folder : true
						} ]
					}, {
						title : "영업본부",
						key : "1300",
						folder : true,
						children : [ {
							title : "솔루션영업팀",
							key : "1310",
							folder : true
						}, {
							title : "SI영업팀",
							key : "1320",
							folder : true
						}, {
							title : "SM영업팀",
							key : "1330",
							folder : true
						} ]
					}, {
						title : "R&D본부",
						key : "1400",
						folder : true,
						children : [ {
							title : "연구개발팀",
							key : "1410",
							folder : true
						} ]
					} ]
				} ],
				click : function(event, data) {
					const deptId = data.node.key;
					loadEmployees(deptId);
					switchToViewAllButton();
				}
			});
			showInitialMessage();

			// 처음 접속 시 모든 직원 정보를 로드하고 정렬 버튼 표시
			loadAllEmployeesByName();
			showSortButtons();

			// 버튼 클릭 이벤트 할당
			$('#nameSortBtn').click(function() {
				$(this).addClass('active');
				$('#jobSortBtn').removeClass('active');
				const buttonText = $(this).text();
				if (buttonText === "전체 보기") {
					loadAllEmployeesByName();
					$(this).text("이름순");
					$('#jobSortBtn').show();
				} else {
					loadAllEmployeesByName(); // 이름순 정렬 
				}
			});
			$('#jobSortBtn').click(function() {
				$(this).addClass('active');
				$('#nameSortBtn').removeClass('active');
				loadAllEmployeesByJob(); // 직급순 정렬 
			});
		});

		function loadEmployees(deptId) {
			$.ajax({
				url : '${pageContext.request.contextPath}/contact/deptbyjob',
				method : 'GET',
				data : {
					deptId : deptId
				},
				success : function(data) {
					console.log("AJAX 응답 데이터:", data); // 응답 데이터 로깅
					updateEmployeeTable(data.empList, data.jobName,
							data.deptName);
				},
				error : function(xhr, status, error) {
					console.error("AJAX 요청 실패:", status, error);
				}
			});
		}

		function loadAllEmployeesByName() {
			$
					.ajax({
						url : '${pageContext.request.contextPath}/contact/allemployeesbyname',
						method : 'GET',
						success : function(data) {
							updateEmployeeTable(data.empList, data.jobName,
									data.deptName);
							showSortButtons(); // 전체 목록을 불러올 때 정렬 버튼을 다시 표시
						},
						error : function(xhr, status, error) {
							console.error("정렬 요청 실패: ", status, error);
						}
					});
		}

		function loadAllEmployeesByJob() {
			$
					.ajax({
						url : '${pageContext.request.contextPath}/contact/allemployeesbyjob',
						method : 'GET',
						success : function(data) {
							updateEmployeeTable(data.empList, data.jobName,
									data.deptName);
							showSortButtons(); // 전체 목록을 불러올 때 정렬 버튼을 다시 표시
						},
						error : function(xhr, status, error) {
							console.error("정렬 요청 실패: ", status, error);
						}
					});
		}

		function updateEmployeeTable(empList, jobName, deptName) {
			const tableBody = $('#employeeTableBody');
			tableBody.empty();
			$('#noDataMessage').remove();

			if (empList.length === 0) {
				tableBody.append("<tr><td colspan='6'>직원이 없습니다.</td></tr>");
			} else {
				$.each(empList,
						function(index, emp) {
							let html = "<tr>";
							html += "<td style='display: none;'>" + emp.empId
									+ "</td>";
							html += "<td>" + emp.ename + "</td>";
							html += "<td>" + deptName[emp.deptId] + "</td>";
							html += "<td>" + jobName[emp.jobId] + "</td>";
							html += "<td>" + emp.mobile + "</td>";
							html += "<td>" + emp.tel + "</td>";
							html += "</tr>";
							tableBody.append(html);
						});
				$('#tree_table').css('table-layout', 'auto');
			}
		}

		function showInitialMessage() {
			const tableBody = $('#employeeTableBody');
			tableBody.empty();
			$('#employeeTableContainer')
					.append(
							"<div id='noDataMessage' style='text-align: center; padding: 20px; font-weight: bold;'>부서를 선택해주세요</div>");
		}

		function searchEmployees(query) {
			const url = '${pageContext.request.contextPath}/contact/search';
			$.ajax({
				url : url,
				method : 'GET',
				data : {
					query : query
				},
				success : function(data) {
					updateEmployeeTable(data.empList, data.jobName,
							data.deptName);
				}
			});
		}
		function switchToViewAllButton() {
			$('#nameSortBtn').text("전체 보기");
			$('#jobSortBtn').hide();
		}

		function hideSortButtons() {
			$('.btn-group').hide(); // 정렬 버튼 숨기기
		}

		function showSortButtons() {
			$('.btn-group').show(); // 정렬 버튼 다시 표시하기
		}

		$('#searchInput').on('keyup', function() {
			const query = $(this).val();
			if (query.length > 1) { // 최소 2자 이상 입력 시 검색
				searchEmployees(query);
			} else if (query.length === 0) { //검색어가 지워지면 전체 목록을 다시 로드 
				loadAllEmployeesByName();
			}
		});
	</script>
</body>
</html>



