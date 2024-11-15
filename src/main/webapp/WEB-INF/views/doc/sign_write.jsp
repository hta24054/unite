<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="${pageContext.request.contextPath }/js/sign_write.js"></script>
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
        <button type="button" class="btn btn-sm btn-success" data-toggle="modal" data-target="#employeeModal">추가
        </button>
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
</body>
</html>