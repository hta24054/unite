$(document).ready(function () {
    // 검색 버튼 클릭 이벤트
    $('#searchButton').on('click', function (event) {
        event.preventDefault(); // 폼 제출 막기
        const searchQuery = $('#searchInput').val().trim();
        executeSearch(searchQuery);
    });

    // 검색 입력 필드에서 엔터 키 입력 처리
    $('#searchInput').on('keypress', function (event) {
        if (event.which === 13) { // Enter 키 코드
            const searchQuery = this.value.trim();
            executeSearch(searchQuery);
        }
    });

    $("#tree").fancytree({
        source: [
            {
                title: "대표이사", key: "1000", expanded: true, folder: true, children: [
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

    // 부서 선택 시 직원 목록 로드
    function loadEmployees(deptId) {
        $.ajax({
            url: '../emp/empTree',
            method: 'GET',
            data: {deptId: deptId},
            success: function (data) {
                updateEmployeeTable(data);
            }
        });
    }

    // 검색 실행 함수
    function executeSearch(searchQuery) {
        // 검색어가 비어 있으면 초기 메시지 표시 후 종료
        if (!searchQuery || searchQuery === "") {
            showInitialMessage();
            return;
        }

        // 검색어를 백엔드로 전송
        $.ajax({
            url: '../emp/empTree-search',
            method: 'GET',
            data: {query: searchQuery},
            success: function (data) {
                updateEmployeeTable(data);
            }
        });
    }

// 직원 테이블 업데이트
    function updateEmployeeTable(data) {
        const tableBody = $('#employeeTableBody');
        tableBody.empty(); // 기존 테이블 내용 비우기
        $('#noDataMessage').remove(); // 기존 메시지가 있다면 제거

        // 데이터가 비어 있거나 배열이 아닐 경우 처리
        if (!data || !Array.isArray(data) || data.length === 0) {
            tableBody.append("<tr><td colspan='4'>직원이 없습니다.</td></tr>");
            return;
        }
        $.each(data, function (index, emp) {
            let html = "<tr>";
            html += "<td style='display: none;'>" + emp.empId + "</td>"; // 숨긴 empId 열
            html += "<td>" + emp.ename + "</td>";
            html += "<td>" + emp.tel + "</td>";
            html += "<td>" + emp.jobName + "</td>"; // emp 객체의 jobName 사용
            html += "</tr>";
            tableBody.append(html);
        });
    }


// 초기 메시지 표시 함수
    function showInitialMessage() {
        const tableBody = $('#employeeTableBody');
        tableBody.empty(); // 기존 테이블 내용 비우기
        $('#noDataMessage').remove(); // 기존 메시지가 있다면 제거
        $('#employeeTableContainer').append(
            "<div id='noDataMessage' style='text-align: center; padding: 20px; font-weight: bold;'>부서를 선택해주세요</div>"
        );
    }
})
