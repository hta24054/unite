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
        .table {
            width: 70%;
            margin: auto;
        }

        table, td, th {
            border-collapse: collapse;
        }

        h2 {
            text-align: left;
            color: black;
            margin: 0;
        }

        caption {
            caption-side: top;
            margin-bottom: 30px;
        }

        #main_title {
            color: #334466;
            margin-left: 500px;
            margin-bottom: 20px;
            font-weight: bold;
        }
    </style>
</head>
<body>
<h2 id="main_title">전 직원 휴가 관리(인사부서) - 직원 목록</h2>
<div class="container">
    <jsp:include page="../common/empTree.jsp"/>
</div>
<script>
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
