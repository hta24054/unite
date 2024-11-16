<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>조직도 및 직원 검색</title>
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
.content-container {
	display: flex;
	gap: 30px;
}

#treeContainer, #employeeTableContainer {
	width: 50%;
}

.table {
	width: 100%;
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
					placeholder="검색어를 입력하세요">
				<div class="btn-group mb-3">
					<button onclick="loadAllEmployees('ename')" class="btn btn-primary">이름순</button>
					<button onclick="loadAllEmployees('job_name')"
						class="btn btn-secondary">직급순</button>
				</div>
				<table class="table table-bordered mt-4" id="tree_table">
					<thead>
						<tr>
							<th style="display: none;">id</th>
							<th>이름</th>
							<th>휴대폰</th>
							<th>내선번호</th>
							<th>부서</th>
							<th>직급</th>
						</tr>
					</thead>
					<tbody id="employeeTableBody">
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<script>
    $(document).ready(function () {
        $("#tree").fancytree({
            source: [
                {
                    title: "대표이사", key: "1000", folder: true, children: [
                        {title: "부사장", key: "1001", folder: true},
                        {
                            title: "경영기획본부", key: "1100", folder: true, children: [
                                {title: "재무관리팀", key: "1110", folder: true},
                                {title: "인사관리팀", key: "1120", folder: true}
                            ]
                        },
                        {
                            title: "SI사업본부", key: "1200", folder: true, children: [
                                {title: "신용평가팀", key: "1210", folder: true},
                                {title: "금융SI팀", key: "1220", folder: true},
                                {title: "비금융SI팀", key: "1230", folder: true},
                                {title: "SM팀", key: "1240", folder: true}
                            ]
                        },
                        {
                            title: "영업본부", key: "1300", folder: true, children: [
                                {title: "솔루션영업팀", key: "1310", folder: true},
                                {title: "SI영업팀", key: "1320", folder: true},
                                {title: "SM영업팀", key: "1330", folder: true}
                            ]
                        },
                        {
                            title: "R&D본부", key: "1400", folder: true, children: [
                                {title: "연구개발팀", key: "1410", folder: true}
                            ]
                        }
                    ]
                }
            ],
            click: function (event, data) {
                const deptId = data.node.key;
                loadEmployees(deptId);
            }
        });
        showInitialMessage();

        // 처음 접속 시 모든 직원 정보를 로드
        loadAllEmployees();

        function loadEmployees(deptId) {
            $.ajax({
                url: '${pageContext.request.contextPath}/contact/view',
                method: 'GET',
                data: { deptId: deptId },
                success: function (data) {
                    console.log("AJAX 응답 데이터:", data); // 응답 데이터 로깅
                    updateEmployeeTable(data.empList, data.jobName, data.deptName);
                },
                error: function (xhr, status, error) {
                    console.error("AJAX 요청 실패:", status, error);
                }
            });
        }

       
	

        function loadAllEmployees(orderBy = 'ename') {
            $.ajax({
                url: '${pageContext.request.contextPath}/contact/allEmployees',
                method: 'GET',
                data: { orderBy: orderBy },
                success: function (data) {
                	
                    updateEmployeeTable(data.empList, data.jobName);
                }
            });
        }

        function searchEmployees(query) {
            const url = '${pageContext.request.contextPath}/contact/search';
            $.ajax({
                url: url,
                method: 'GET',
                data: { query: query },
                success: function (data) {
                    updateEmployeeTable(data.empList, data.jobName);
                }
            });
        }


        function updateEmployeeTable(empList, jobName, deptName) {
            const tableBody = $('#employeeTableBody');
            tableBody.empty();
            $('#noDataMessage').remove();

            console.log("empList:", empList); // empList 로그
            console.log("jobName:", jobName); // jobName 로그
            console.log("deptName:", deptName); // deptName 로그

            if (empList.length === 0) {
                tableBody.append("<tr><td colspan='6'>직원이 없습니다.</td></tr>");
            } else {
                $.each(empList, function (index, emp) {
                    let html = "<tr>";
                    html += "<td style='display: none;'>" + emp.empId + "</td>";
                    html += "<td>" + emp.ename + "</td>";
                    html += "<td>" + emp.mobile + "</td>";
                    html += "<td>" + emp.tel + "</td>";
                    html += "<td>" + jobName[emp.jobId] + "</td>";
                    html += "<td>" + deptName[emp.deptId] + "</td>";
                    html += "</tr>";
                    tableBody.append(html);
                });
            }
        }

        function showInitialMessage() {
            const tableBody = $('#employeeTableBody');
            tableBody.empty();
            $('#employeeTableContainer').append(
                "<div id='noDataMessage' style='text-align: center; padding: 20px; font-weight: bold;'>부서를 선택해주세요</div>"
            );
        }

        $('#searchInput').on('keyup', function() {
            const query = $(this).val();
            if (query.length > 2) { // 최소 3자 이상 입력 시 검색
                searchEmployees(query);
            }
        });

        $('.btn-primary').click(function() {
            loadAllEmployees('ename'); // 이름순 정렬
        });

        $('.btn-secondary').click(function() {
            loadAllEmployees('job_name'); // 직급순 정렬
        });
    });
</script>
</body>
</html>
