<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>프로젝트 생성</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        .modal-lg {
            max-width: 100%;
        }
        .modal-body {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
        }
        .employee-table {
            flex: 1;
            height: 400px;
            overflow-y: auto;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 10px;
            text-align: left;
            border: 1px solid #ddd;
        }
    </style>
    <script>
    $(document).ready(function () {
        // 조직도 아이콘 클릭 이벤트 처리
        $('.orgChartIcon').on('click', function(event) {
            event.preventDefault();
            const targetId = $(this).data('target');
            localStorage.setItem('selectedInputId', targetId);
            $('#orgChartModal').modal('show');
        });

        // "선택" 열 추가
        $('#employeeTableContainer thead tr').prepend('<th>선택</th>');

        // 직원 목록 행에 체크박스를 추가하는 함수
        function addCheckboxes() {
            $('#employeeTableBody tr').each(function () {
                const empId = $(this).find('td:eq(0)').text().trim();
                $(this).prepend('<td><input type="checkbox" name="selectedEmp" value="' + empId + '"></td>');
                $(this).find('td:eq(1)').hide();  // empId 열 숨기기
            });
        }

        // 최초 로딩 시 체크박스 추가
        addCheckboxes();	

        // AJAX 후 체크박스를 다시 추가
        $(document).on('ajaxSuccess', function () {
            addCheckboxes();
        });

        // 추가 버튼 클릭 시 선택된 직원의 이름을 textarea에 추가
        $('#addEmpBtn').on('click', function() {
            const selectedEmpNames = $('input[name="selectedEmp"]:checked')
                .map(function () {
                    return $(this).closest('tr').find('td:eq(2)').text().trim();  // 직원 이름
                })
                .get();

            if (selectedEmpNames.length > 0) {
                const empTextArea = $('#empTextArea');
                let existingText = empTextArea.val();
                selectedEmpNames.forEach(function(empName) {
                    if (existingText.indexOf(empName) === -1) {
                        existingText += empName + '\n';
                    }
                });
                empTextArea.val(existingText); // 이름 추가
            } else {
                alert('직원을 선택해 주세요.');
            }
        });

        // 삭제 버튼 클릭 시 선택된 직원 이름을 textarea에서 삭제
        $('#deleteEmpBtn').on('click', function() {
            const empTextArea = $('#empTextArea');
            const selectedEmpNames = $('input[name="selectedEmp"]:checked')
                .map(function () {
                    return $(this).closest('tr').find('td:eq(2)').text().trim();  // 직원 이름
                })
                .get();

            if (selectedEmpNames.length > 0) {
                let existingText = empTextArea.val();
                selectedEmpNames.forEach(function(empName) {
                    existingText = existingText.replace(empName + '\n', ''); // 이름 삭제
                });
                empTextArea.val(existingText); // 삭제된 이름 업데이트
            } else {
                alert('삭제할 직원을 선택해 주세요.');
            }
        });

        // 등록 버튼 클릭 시 textarea에 있는 직원 정보를 실제 폼에 등록
        $('#insertEmpBtn').on('click', function() {
		    const targetInputId = localStorage.getItem('selectedInputId');
		    if (!targetInputId) {
		        alert('유효한 입력 필드가 선택되지 않았습니다.');
		        return;
		    }
		
		    const empTextArea = $('#empTextArea');
		    const selectedEmpNames = empTextArea.val().trim();
		
		    if (selectedEmpNames === '') {
		        alert('직원 정보를 추가해 주세요.');
		        return;
		    }
		
		    if (targetInputId === 'manager') {
		        const empNamesArray = selectedEmpNames.split('\n').filter(name => name.trim() !== '');
		        if (empNamesArray.length > 1) {
		            alert('책임자는 한 명만 지정할 수 있습니다.');
		            return;
		        }
		    }
		
		    const targetInput = $('#' + targetInputId);
		    targetInput.prop('readonly', false);
		    targetInput.val(selectedEmpNames);
		    targetInput.prop('readonly', true);
		
		    alert('등록된 직원: ' + selectedEmpNames);
		    $('#orgChartModal').modal('hide');
		});
        
        $('#projectForm').on('submit', function(e) {
            const manager = $('#manager').val().trim();
            if (!manager) {
                alert('책임자를 지정해 주세요.');
                e.preventDefault();  // 제출 취소
            }
        });
        
    });

</script>


</head>
<body>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="project_leftbar.jsp"/>

    <div class="container mt-4">
        <h2>프로젝트 생성</h2>
        <form id="projectForm" action="doCreate" method="post">
            <div class="form-group row">
                <label for="name" class="col-sm-3 col-form-label">프로젝트명</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control" id="name" name="name" required>
                </div>
            </div>
            <div class="form-group row">
                <label for="startDate" class="col-sm-3 col-form-label">시작일</label>
                <div class="col-sm-9">
                    <input type="date" class="form-control" id="startDate" name="startDate" required>
                </div>
            </div>
            <div class="form-group row">
                <label for="endDate" class="col-sm-3 col-form-label">종료일</label>
                <div class="col-sm-9">
                    <input type="date" class="form-control" id="endDate" name="endDate" required>
                </div>
            </div>
            <div class="form-group row">
                <label for="manager" class="col-sm-3 col-form-label">책임자</label>
                <div class="col-sm-9 d-flex align-items-center">
                    <input type="text" class="form-control" id="manager" name="manager" required readOnly>
                    <a href="#" class="mr-2 orgChartIcon" data-target="manager">
                        <img src="${pageContext.request.contextPath}/image/profile_navy.png" alt="조직도" width="20" height="20">
                    </a>
                </div>
            </div>
            <div class="form-group row">
                <label for="participants" class="col-sm-3 col-form-label">참여자</label>
                <div class="col-sm-9 d-flex align-items-center">
                    <input type="text" class="form-control" id="participants" name="participants" required readOnly>
                    <a href="#" class="mr-2 orgChartIcon" data-target="participants">
                        <img src="${pageContext.request.contextPath}/image/profile_navy.png" alt="조직도" width="20" height="20">
                    </a>
                </div>
            </div>
            <div class="form-group row">
                <label for="viewers" class="col-sm-3 col-form-label">열람자</label>
                <div class="col-sm-9 d-flex align-items-center">
                    <input type="text" class="form-control" id="viewers" name="viewers" required readOnly>
                    <a href="#" class="mr-2 orgChartIcon" data-target="viewers">
                        <img src="${pageContext.request.contextPath}/image/profile_navy.png" alt="조직도" width="20" height="20">
                    </a>
                </div>
            </div>
            <div class="form-group row">
                <label for="description" class="col-sm-3 col-form-label">내용</label>
                <div class="col-sm-9">
                    <textarea class="form-control" id="description" name="description" rows="4" required></textarea>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-12">
                    <button type="submit" class="btn btn-primary w-100">프로젝트 생성</button>
                </div>
            </div>
        </form>
    </div>

    <!-- 조직도 모달 -->
    <div class="modal fade" id="orgChartModal" tabindex="-1" role="dialog" aria-labelledby="orgChartModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="orgChartModalLabel">조직도</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" id="insertEmpBtn">등록</button>
                <jsp:include page="../common/empTree.jsp"/>
            </div>
            <div class="modal-body">
                <div class="employee-table">
                    <table id="employeeTable">
                        <tbody id="employeeTableBody">
                            <!-- 직원 목록이 동적으로 추가됨 -->
                        </tbody>
                    </table>
                </div>
                <div class="text-area-container mt-3">
                    <textarea id="empTextArea" class="form-control" rows="3" cols="100"placeholder="선택된 직원 추가..." readOnly></textarea>
                    <button type="button" id="addEmpBtn" class="btn btn-primary mt-2">추가</button>
                    <button type="button" id="deleteEmpBtn" class="btn btn-danger mt-2">삭제</button>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
