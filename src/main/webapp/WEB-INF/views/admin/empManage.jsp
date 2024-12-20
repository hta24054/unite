<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>직원 관리</title>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="admin_leftbar.jsp"/>
    <style>
        .container {
            max-width: 1000px;
            margin-top: 20px;
        }

        #main_title {
            color: #334466;
            margin-left: 500px;
            margin-bottom: 20px;
            font-weight: bold;
        }

        /* 버튼 그룹 스타일 */
        .button-group {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
            justify-content: flex-end; /* 우측 정렬 */
        }

    </style>
</head>
<body>
<h2 id="main_title">직원 관리</h2>
<div class="container">
    <!-- 우측 상단 버튼 그룹 -->
    <div class="button-group">
        <button type="button" class="btn btn-success" onclick="insertEmp()">등록</button>
        <button type="button" class="btn btn-info" onclick="editEmp()">수정</button>
        <button type="button" class="btn btn-danger" onclick="deleteEmp()">삭제</button>
    </div>

    <!-- 조직도 포함 -->
    <jsp:include page="../common/empTree.jsp"/>
</div>

<script>
    $(document).ready(function () {
        // "선택" 열 추가
        $('#employeeTableContainer thead tr').prepend('<th>선택</th>');

        // 직원 목록 행에 라디오 버튼을 추가하는 함수
        function addRadioButtons() {
            const rows = $('#employeeTableBody tr');

            // 직원 목록이 비어 있는지 확인 (단순히 한 줄에 "직원이 없습니다." 텍스트가 포함된 경우)
            if (rows.length === 1 && rows.find('td').text().trim() === "직원이 없습니다.") {
                // "직원이 없습니다." 메시지 외에 다른 라디오 버튼을 추가하지 않음
                return;
            }

            rows.each(function () {
                const empId = $(this).find('td:eq(0)').text().trim(); // 첫 번째 열에서 empId 값 추출

                // empId가 비어있는 경우 라디오 버튼 추가 생략
                if (!empId || empId === "직원이 없습니다.") return;

                // 라디오 버튼이 없는 경우에만 추가
                if (!$(this).find('input[type="radio"]').length) {
                    $(this).prepend('<td><input type="radio" name="selectedEmp" value="' + empId + '"></td>'); // 라디오 버튼 추가
                    $(this).find('td:eq(1)').hide(); // empId 열을 숨김 처리
                }
            });
        }

        // 직원 목록이 업데이트될 때마다 라디오 버튼 추가
        $(document).on('ajaxSuccess', function () {
            addRadioButtons();
        });

        // 초기 페이지 로딩 시에도 라디오 버튼을 추가
        addRadioButtons();
    });

    const contextPath = "${pageContext.request.contextPath}";

    function insertEmp() {
        window.location.href = contextPath + '/admin/emp-manage/register';
    }

    function editEmp() {
        const empId = $('input[name="selectedEmp"]:checked').val();

        if (empId) {
            window.location.href = contextPath + '/admin/emp-manage/edit?empId=' + empId;
        } else {
            alert("수정할 직원을 선택해 주세요.");
        }
    }

    function deleteEmp() {
        const empId = $('input[name="selectedEmp"]:checked').val();
        console.log(empId)
        if (confirm("직원을 삭제하시겠습니까?")) {
            if (empId) {
                $.ajax({
                    url: contextPath + '/admin/emp-manage/fire',
                    type: 'POST',
                    data: {empId: empId},
                    success: function (data) {
                        alert(data.message);
                        location.reload();
                    },
                    error: function () {
                        alert("직원 삭제를 실패했습니다.");
                    }
                });
            } else {
                alert("삭제할 직원을 선택해 주세요.");
            }
        }
    }
</script>
</body>
</html>