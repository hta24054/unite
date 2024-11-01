<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>조직도 및 직원 검색</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.38.0/jquery.fancytree-all-deps.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery.fancytree/2.38.0/skin-win8/ui.fancytree.min.css"/>

    <script>
        $(document).ready(function() {
            $("#tree").fancytree({
                source: [
                    {
                        title: "대표이사", folder: true, children: [
                            {title: "부사장", folder: true},
                            {
                                title: "경영기획본부", folder: true, children: [
                                    {title: "재무관리팀", key: "재무부"},
                                    {title: "인사관리팀", key: "인사부"}
                                ]
                            },
                            {
                                title: "SI사업본부", folder: true, children: [
                                    {title: "신용평가팀", key: "평가부"},
                                    {title: "금융SI팀", key: "금융부"},
                                    {title: "비금융SI팀", key: "비금융부"},
                                    {title: "SM팀", key: "SM사업부"}
                                ]
                            },
                            {
                                title: "영업본부", folder: true, children: [
                                    {title: "솔루션영업팀", key: "재무부"},
                                    {title: "SI영업팀", key: "SI부"},
                                    {title: "SM영업팀", key: "SM영업부"}
                                ]
                            },
                            {
                                title: "R&D본부", folder: true, children: [
                                    {title: "연구개발팀", key: "개발부"}
                                ]
                            }
                            
                        ]
                    }
                ],
                click: function(event, data) {
                    const department = data.node.key;
                    loadEmployees(department); // 부서 클릭 시 직원 정보 로드
                }
            });

            function loadEmployees(department) {
                $.ajax({
                    url: 'employ',
                    method: 'GET',
                    data: { department: department },
                    success: function(data) {
                        updateEmployeeTable(data); // 직원 리스트 업데이트
                    },
                    error: function() {
                        alert('직원 정보를 불러오는 데 실패했습니다.');
                    }
                });
            }

            function updateEmployeeTable(data) {
                const tableBody = $('#employeeTableBody');
                tableBody.empty(); // 기존 내용 비우기

                $.each(data, function(key, value) {
                    var html = "<tr>";
                    html += "<td><input type='checkbox' class='employee-checkbox' value='" + value.ename + "'>" + value.ename + "</td>";
                    html += "<td>" + value.mobile + "</td>";
                    html += "<td>" + value.tel + "</td>";
                    html += "<td>" + value.deptId + "</td>";
                    html += "<td>" + value.jobId + "</td>";
                    html += "</tr>";
                    tableBody.append(html); // 각 행을 테이블에 추가
                });
            }

            $('#saveSelected').on('click', function() {
                const selectedNames = [];
                $('.employee-checkbox:checked').each(function() {
                    selectedNames.push($(this).val());
                });
                if (selectedNames.length > 0) {
                    const namesString = selectedNames.join(', ');
                    const inputId = localStorage.getItem('selectedInputId'); // 로컬 저장소에서 input ID 가져오기

                    if (inputId) {
                        const targetInput = parent.document.getElementById(inputId); // 부모 문서에서 해당 input 요소 찾기
                        if (targetInput) {
                            targetInput.value = namesString; // 해당 input에 값 설정
                            alert('선택된 직원: ' + namesString);
                            
                            // 모달 닫기
                            $('#orgChartModal').modal('hide'); // 현재 페이지의 모달을 닫기
                            // 조직도 창 닫기
                            window.parent.$('#orgChartModal').modal('hide'); // 부모 페이지의 모달을 닫기
                            setTimeout(function() {
                                window.close(); // 조직도 창 닫기
                            }, 500); // 0.5초 후에 창을 닫음
                        } else {
                            alert('해당 ID를 가진 input 요소를 찾을 수 없습니다.');
                        }
                    } else {
                        alert('선택된 input ID가 없습니다.');
                    }
                } else {
                    alert('선택된 직원이 없습니다.');
                }
            });





        });
    </script>
</head>
<body>
    <div class="container mt-4">
        <h5>조직도</h5>
        <div id="tree"></div>

        <h5 class="mt-4">직원 목록</h5>
        <table class="table table-bordered mt-4">
            <thead>
                <tr>
                    <th>이름</th>
                    <th>휴대폰</th>
                    <th>내선번호</th>
                    <th>부서</th>
                    <th>직급</th>
                </tr>
            </thead>
            <tbody id="employeeTableBody">
                <!-- 직원 정보가 AJAX로 로드되어 여기에 추가됩니다. -->
            </tbody>
        </table>
        
        <button id="saveSelected" class="btn btn-success mt-2">저장</button>
    </div>
</body>
</html>
