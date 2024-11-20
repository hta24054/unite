<%@ page import="java.time.LocalDate" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>전 직원 휴가 관리(인사부서) - 직원 목록</title>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="attend_leftbar.jsp"/>
    <style>
        h2 {
            text-align: left;
            color: black;
            margin: 0;
        }

        #main_title {
            color: #334466;
            margin-left: 500px;
            margin-bottom: 20px;
            font-weight: bold;
        }
        .container {
            display: flex;
            flex-direction: column; /* 요소를 수직 배치 */
        }

        #vac_count_button {
            align-self: flex-end; /* 버튼을 수평으로 우측 정렬 */
            background-color: #334466;
            color: white;
        }
    </style>
</head>
<body>
<h2 id="main_title">전 직원 휴가 관리(인사부서) - 직원 목록</h2>
<div class="container">
    <button type="button" class="btn" id="vac_count_button">전 직원 부여 연차 갱신</button>
    <jsp:include page="../common/empTree.jsp"/>
</div>
<script>
    $('#vac_count_button').click(function () {
        if (!confirm("전 직원 연차 부여 개수를 갱신합니다.")) {
            return false;
        }
        $.ajax({
            url: '${pageContext.request.contextPath}/attend/vacation/set-count',
            method: 'POST',
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (data) {
                if (data.status === 'success') {
                    alert("연차 개수가 갱신되었습니다.");
                    window.location.href = '${pageContext.request.contextPath}/attend/vacation/empList';
                } else {
                    alert("연차 개수 갱신 오류");
                    window.location.href = '${pageContext.request.contextPath}/attend/vacation/empList';
                }
            }, error: function () {
                alert("연차 개수 갱신 오류");
                window.location.href = '${pageContext.request.contextPath}/attend/vacation/empList';
            }
        });
    })

    function updateEmployeeTable(empList, jobName) {
        const tableBody = $('#employeeTableBody');
        tableBody.empty();
        $('#noDataMessage').remove();

        if (empList.length === 0) {
            tableBody.append("<tr><td colspan='4'>직원이 없습니다.</td></tr>");
        } else {
            $.each(empList, function (index, emp) {
                let html = "<tr>";
                html += "<td style='display: none;'>" + emp.empId + "</td>"; // 숨긴 empId 열
                html += "<td><a href='" + getEmployeeDetailUrl(emp.empId) + "'>" + emp.ename + "</a></td>"; // 앵커 태그 추가
                html += "<td>" + emp.tel + "</td>";
                html += "<td>" + jobName[emp.jobId] + "</td>";
                html += "</tr>";
                tableBody.append(html);
            });
        }
    }

    // 직원 상세 페이지 URL 생성 함수
    function getEmployeeDetailUrl(empId) {
        // 직원 상세 정보로 이동하는 URL 생성
        return '${pageContext.request.contextPath}/attend/vacation/emp?empId=' + empId + '&year=<%=LocalDate.now().getYear()%>';
    }
</script>
</body>
</html>
