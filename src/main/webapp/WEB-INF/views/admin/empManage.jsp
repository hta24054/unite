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
            $('#employeeTableBody tr').each(function () {
                const empId = $(this).find('td:eq(0)').text().trim(); // 첫 번째 열에서 empId 값 추출
                $(this).prepend('<td><input type="radio" name="selectedEmp" value="' + empId + '"></td>'); // 라디오 버튼 추가
                $(this).find('td:eq(1)').hide(); // empId 열을 숨김 처리
            });
        }

        // 직원 목록이 업데이트될 때마다 라디오 버튼 추가
        $(document).on('ajaxSuccess', function () {
            addRadioButtons();
        });

        // 초기 페이지 로딩 시에도 라디오 버튼을 추가
        addRadioButtons();
    });
    // "선택" 열을 테이블 헤더에 추가

    const contextPath = "${pageContext.request.contextPath}";

    function insertEmp() {
        alert("등록 버튼 클릭됨");
        // 직원 등록 페이지로 이동하는 GET 요청
        window.location.href = contextPath + '/admin/emp/insert';
    }

    function editEmp() {
        alert("수정 버튼 클릭됨");
        const empId = $('input[name="selectedEmp"]:checked').val();

        if (empId) {
            const url = contextPath + '/admin/emp/edit?empId=' + empId; // empId 파라미터 추가
            window.location.href = url;
        } else {
            alert("수정할 직원을 선택해 주세요.");
        }
    }

    function deleteEmp() {
        alert("삭제 버튼 클릭됨");
        // 직원 삭제 처리하는 POST 요청
        const url = '/삭제_URL'; // 직원 삭제 URL 입력
        const selectedEmp = $('input[name="employeeSelect"]:checked').val();

        if (selectedEmp) {
            fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({empId: selectedEmp.value}) // 선택된 직원 ID 전송
            })
                .then(response => {
                    if (response.ok) {
                        alert("삭제가 완료되었습니다.");
                        location.reload(); // 페이지를 새로고침하여 목록 갱신
                    } else {
                        alert("삭제 중 오류가 발생했습니다.");
                    }
                })
                .catch(error => console.error('삭제 오류:', error));
        } else {
            alert("삭제할 직원을 선택해 주세요.");
        }
    }
</script>
</body>
</html>