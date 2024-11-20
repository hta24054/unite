<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>휴가 관리</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
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

        /* h2의 기본 여백 제거 */
        caption {
            caption-side: top;
            margin-bottom: 30px;
        }

        #report {
            width: 500px;
            text-align: center;
            font-weight: bold;
        }

        #report th {
            width: 25%;
        }

        #main_title {
            color: #334466;
            margin-left: 500px;
            margin-bottom: 20px;
            font-weight: bold;
        }

        #specify {
            text-align: center;
        }

        #specify th {
            width: 16.6667%;
        }

        /* 캡션과 테이블 간격 설정 */
    </style>
</head>
<body>
<h2 id="main_title">나의 휴가 관리</h2>
<div class="container">
    <!-- 연도변경 -->
    <div class="text-center my-4">
        <button id="prevYear" class="btn btn-outline-dark">&lt;</button>
        <span id="currentYearMonth"
              style="font-weight: bold; font-size: 25px">&nbsp;&nbsp;${param.year}년&nbsp;&nbsp;</span>
        <button id="nextYear" class="btn btn-outline-dark">&gt;</button>
    </div>
    <%--    요약 테이블--%>
    <table class="table table-striped table-bordered shadow-sm p-3 mb-5 bg-body rounded" id="report">
        <thead>
        <tr>
            <th>총 연차일</th>
            <th>사용일</th>
            <th>잔여일</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>${givenVacCount}</td>
            <td>${usedAnnualVacation}</td>
            <td style="color: blue">${givenVacCount-usedAnnualVacation}</td>
        </tr>
        </tbody>
    </table>
    <br>
    <br>
    <!-- 근태 관리 테이블 -->
    <table class="table table-striped table-bordered shadow-sm p-3 mb-5 bg-body rounded" id="specify">
        <thead>
        <tr>
            <th>No</th>
            <th>휴가구분</th>
            <th>휴가일수</th>
            <th>신청일</th>
            <th>시작일</th>
            <th>종료일</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty vacList}">
            <th colspan="6">휴가기록이 없습니다.</th>
        </c:if>
        <c:if test="${!empty vacList}">
            <c:forEach var="vac" items="${vacList}" varStatus="status">
                <tr>
                    <td>${status.count}</td>
                    <td>${vac.vacationType.typeName}</td>
                    <td>${vac.vacationCount}</td>
                    <td>${vac.vacationApply}</td>
                    <td>${vac.vacationStart}</td>
                    <td>${vac.vacationEnd}</td>
                </tr>
            </c:forEach>
        </c:if>

        </tbody>
    </table>
</div>


<script>
    $(document).ready(function () {
        let currentYear = parseInt('${param.year}'); // 서버에서 가져온 연도
        const todayYear = new Date().getFullYear(); // 현재 연도

        // 이전 연도 버튼 클릭
        $('#prevYear').click(function () {
            currentYear--;
            if (currentYear < 1) {
                return false;
            }
            sendRequest();
        });

        // 다음 연도 버튼 클릭
        $('#nextYear').click(function () {
            currentYear++;
            if (currentYear > todayYear) {
                alert('아직 확인할 수 없습니다.');
                currentYear = todayYear;
                return false;
            }
            sendRequest();
        });

        // GET 요청 보내기
        function sendRequest() {
            // 현재 URL의 쿼리 파라미터 가져오기
            const urlParams = new URLSearchParams(window.location.search);

            // year와 month 파라미터 업데이트
            urlParams.set('year', currentYear);

            // emp 파라미터가 있는 경우 그대로 유지, 없는 경우 추가하지 않음
            const params = urlParams.toString();

            // 새 URL로 이동
            window.location.href = "?" + params;
        }

        // title 수정(직원휴가관리 일 경우 param에 있는 값)
        const params = new URLSearchParams(window.location.search);
        const emp = params.get('empId'); // 'emp' 파라미터 값 가져오기
        if (emp) { // 'emp' 파라미터가 존재할 경우
            $('#main_title').text('직원 휴가 관리 - ' + '${emp.ename}');
        }
    });
</script>
</body>
</html>
