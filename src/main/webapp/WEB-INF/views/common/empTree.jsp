<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>조직도 및 직원 검색</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.38.0/jquery.fancytree-all-deps.min.js"></script>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.38.0/skin-win8/ui.fancytree.min.css"/>

    <style>
        .content-container {
            display: flex; /* 조직도와 직원 목록을 가로로 배치 */
            gap: 30px; /* 조직도와 직원 목록 사이의 간격 */
        }

        #treeContainer, #employeeTableContainer {
            width: 50%; /* 두 컨테이너의 너비를 같게 설정 */
        }

        /* 테이블 스타일 */
        table.table {
            width: 100%;
        }

        /* 제목 스타일 */
        .title {
            color: #334466;
            font-size: 18px;
            margin: 0; /* 기본 마진 제거 */
            font-weight: bold;
            border-bottom: 1px solid black;
            padding-bottom: 10px;
        }

        #tree_title {
            margin-bottom: 25px;
        }
    </style>
</head>
<body>
<div class="container mt-4">
    <div class="content-container">
        <!-- 조직도 영역 -->
        <div id="treeContainer">
            <h5 class="title" id="tree_title">조직도</h5>
            <div id="tree"></div>
        </div>

        <!-- 직원 목록 테이블 영역 -->
        <div id="employeeTableContainer">
            <h5 class="title">직원 목록</h5>
            <table class="table table-bordered mt-4">
                <thead>
                <tr>
                    <th>이름</th>
                    <th>내선번호</th>
                    <th>직급</th>
                </tr>
                </thead>
                <tbody id="employeeTableBody">
                <!-- 직원 정보가 AJAX로 로드되어 여기에 추가됩니다. -->
                </tbody>
            </table>
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
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

            function loadEmployees(deptId) {
                $.ajax({
                    url: '${pageContext.request.contextPath}/emp/empTree',
                    method: 'GET',
                    data: {deptId: deptId},
                    success: function (data) {
                        updateEmployeeTable(data.empList, data.jobName);
                    }
                });
            }

            function updateEmployeeTable(empList, jobName) {
                const tableBody = $('#employeeTableBody');
                tableBody.empty();

                if (empList.length === 0) {
                    tableBody.append("<tr><td colspan='3'>직원이 없습니다.</td></tr>");
                } else {
                    $.each(empList, function (index, value) {
                        let html = "<tr>";
                        html += "<td>" + value.ename + "</td>";
                        html += "<td>" + value.tel + "</td>";
                        html += "<td>" + jobName[value.jobId] + "</td>";
                        html += "</tr>";
                        tableBody.append(html);
                    });
                }
            }

            // 초기 메시지
            function showInitialMessage() {
                const tableBody = $('#employeeTableBody');
                tableBody.empty();
                tableBody.append("<tr><td colspan='3'>부서를 선택해주세요</td></tr>");
            }
        })
    });
</script>
</body>
</html>
