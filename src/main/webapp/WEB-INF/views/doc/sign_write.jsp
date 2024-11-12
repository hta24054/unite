<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>결재자</title>
    <style>
        .approval-table-container {
            float: right;
            position: relative;
        }

        .approval-table {
            border-collapse: collapse;
            margin-top: 10px;
        }

        .approval-table th, .approval-table td {
            padding: 10px;
            text-align: center;
            vertical-align: middle;
            border-color: black; /* 테두리 검정색 */
        }

        .label-cell {
            background: #f8f9fa;
            font-weight: bold;
        }

        #title {
            writing-mode: vertical-lr;
            font-weight: bold;
            font-size: 15px;
            width: 20px;
        }

        .name {
            width: 100px;
            height: 100px;
            font-size: 15px;
            font-weight: bold;
        }

        .button-container {
            text-align: right;
            margin-top: 10px;
        }

        /* 테두리 색상 검정 */
        .table-bordered, .table-bordered td, .table-bordered th {
            border-color: black !important;
        }
    </style>
</head>
<body>
<div class="approval-table-container">
    <table class="approval-table table table-bordered" id="approvalTable">
        <tr>
            <td rowspan="3" class="label-cell" id="title">결재</td>
            <th class="label-cell">기안자</th>
        </tr>
        <tr>
            <td class="name">${emp.ename}</td>
        </tr>
        <!-- 숨겨진 필드로 기안자의 empId 추가 -->
        <input type="hidden" name="sign[]" value="${emp.empId}">
    </table>

    <!-- 버튼 컨테이너: 버튼을 테이블 우측 하단에 배치 -->
    <div class="button-container">
        <button type="button" class="btn btn-sm btn-success" data-toggle="modal" data-target="#employeeModal">추가</button>
        <button type="button" class="btn btn-sm btn-danger" id="deleteButton">삭제</button>
    </div>
</div>

<!-- 모달 -->
<div class="modal fade" id="employeeModal" tabindex="-1" aria-labelledby="employeeModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="employeeModalLabel">조직도 및 직원 검색</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <!-- 조직도와 직원 목록 포함 -->
                <jsp:include page="../common/empTree.jsp"/>
            </div>
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
        let signCount = 0;
        const maxSignCount = 3; // 최대 결재자 수
        const initialWidth = 120; // 초기 폭 설정

        // 직원 목록에서 더블클릭하여 결재자 추가
        $(document).on("dblclick", "#employeeTableBody tr", function () {
            const empId = $(this).find("td:eq(0)").text(); // 사번 가져오기
            const name = $(this).find("td:eq(1)").text(); // 이름 가져오기

            // 결재자 칸 추가
            addSigner(name, empId);
            $('#employeeModal').modal('hide');
        });

        // 결재자 칸 추가 함수
        function addSigner(name, empId) {
            if (signCount >= maxSignCount) {
                alert("최대 3명까지 결재자를 추가할 수 있습니다.");
                return;
            }

            signCount++;

            // 새로운 결재자 칸 추가
            $('#approvalTable tr:first').append('<th class="label-cell">결재자 ' + signCount + '</th>');
            $('#approvalTable tr:nth-child(2)').append('<td class="name">' + name + '</td>');

            // 숨겨진 필드로 empId 추가
            $('#approvalTable').append('<input type="hidden" name="sign[]" value="' + empId + '">');

            // 테이블의 부모 컨테이너 너비 조정 (좌측으로 확장되도록)
            $('.approval-table').css('width', initialWidth + signCount * 100 + 'px');
        }

        // 결재자 삭제 버튼
        $("#deleteButton").click(function () {
            signCount = 0;

            // 결재자 칸을 모두 삭제하고, 기안자만 남기기
            $('#approvalTable tr:first th:gt(0)').remove();
            $('#approvalTable tr:nth-child(2) td:gt(0)').remove();

            $('input[name="sign[]"]').not(':first').remove();

            $('.approval-table').css('width', initialWidth + 'px'); // 초기 폭으로 설정
        });

        $("#doc_form").on("submit", function (e) {
            const additionalSigners = $('input[name="sign[]"]').length - 1;
            if (additionalSigners < 1) {
                alert("본인 이외에 최소 1명의 결재자를 추가해야 합니다.");
                e.preventDefault();
            }
        });
    });
</script>

</body>
</html>