<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>근태 관리</title>
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
            width: 600px;
            text-align: center;
        }

        #report th {
            width: 20%;
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
            width: 20%;
        }
    </style>
</head>
<body>
<h2 id="main_title">근태 관리</h2>
<div class="container">
    <!-- 연도와 월 변경 -->
    <div class="text-center my-4">
        <button id="prevMonth" class="btn btn-outline-dark">&lt;</button>
        <span id="currentYearMonth" style="font-weight: bold; font-size: 25px">&nbsp;&nbsp;${param.year}년 ${param.month}월&nbsp;&nbsp;</span>
        <button id="nextMonth" class="btn btn-outline-dark">&gt;</button>
    </div>
    <%--    요약 테이블--%>
    <table class="table table-striped table-bordered shadow-sm p-3 mb-5 bg-body rounded" id="report">
        <thead>
        <tr>
            <th>당월 근무일</th>
            <th>근무</th>
            <th>휴가</th>
            <th>결근</th>
            <th>지각, 조퇴</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>${allWorkDate}</td>
            <td>${myWorkDate}</td>
            <td>${vacation}</td>
            <td>${absent}</td>
            <td>${lateOrLeaveEarly}</td>
        </tr>
        </tbody>
    </table>
    <br>
    <br>
    <!-- 근태 관리 테이블 -->
    <table class="table table-striped table-bordered shadow-sm p-3 mb-5 bg-body rounded" id="specify">
        <thead>
        <tr>
            <th>날짜</th>
            <th>출근 시각</th>
            <th>퇴근 시각</th>
            <th>근무 시간</th>
            <th>근무 유형</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="attend" items="${attendList}">
            <tr>
                <td class="attend-date">${attend.attendDate}</td>
                <td>
                    <c:choose>
                        <c:when test="${not empty attend.attendIn}">
                            ${fn:substring(attend.attendIn, 11, 16)}
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${not empty attend.attendOut}">
                            ${fn:substring(attend.attendOut, 11, 16)}
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose>
                </td>
                <td class="work-time">
                    <c:choose>
                        <c:when test="${not empty attend.workTime.seconds}">${attend.workTime.seconds}</c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose>
                </td>
                <td class="attend-type">
                    <c:choose>
                        <c:when test="${not empty attend.attendType.typeName}">
                            <c:if test="${attend.attendType.typeName == '결근'}">
                                <span style="color: red;">${attend.attendType.typeName}</span>
                            </c:if>
                            <c:if test="${attend.attendType.typeName != '결근'}">
                                ${attend.attendType.typeName}
                            </c:if>
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>


<script>
    $(document).ready(function () {
        let currentMonth = ${param.month};
        let currentYear = ${param.year};

        $(".table tbody tr").each(function () {
            const $row = $(this);
            const $dateCell = $row.find(".attend-date");
            const $typeCell = $row.find(".attend-type");
            const $workTimeCell = $row.find(".work-time");

            // 근무 시간을 시:분:초 형식으로 변환
            const totalSeconds = parseInt($workTimeCell.text(), 10);
            if (!isNaN(totalSeconds)) {
                const hours = String(Math.floor(totalSeconds / 3600));
                const minutes = String(Math.floor((totalSeconds % 3600) / 60)).padStart(2, '0');
                const seconds = String(totalSeconds % 60).padStart(2, '0');
                // const formattedTime = hours + "시간 " + minutes + "분 " + seconds + "초";
                const formattedTime = hours + "시간 " + minutes + "분 "; //초는 빼고 출력
                $workTimeCell.text(formattedTime);
            }

            // 날짜 셀 색상 지정 (토요일, 일요일, 휴일 확인)
            const dateText = $dateCell.text();
            const date = new Date(dateText);
            const dayOfWeek = date.getDay();

            if (dayOfWeek === 6) { // 토요일
                $dateCell.css("color", "blue");
            } else if (dayOfWeek === 0 || ($typeCell.length && $typeCell.text().trim() === "휴일")) { // 일요일 또는 휴일
                $dateCell.css("color", "red");
            }
        });

        // 이전 달 버튼 클릭
        $('#prevMonth').click(function () {
            if (currentMonth === 1) {
                currentMonth = 12;
                currentYear -= 1;
            } else {
                currentMonth -= 1;
            }
            sendRequest();
        });

        // 다음 달 버튼 클릭
        $('#nextMonth').click(function () {
            if (currentMonth === 12) {
                currentMonth = 1;
                currentYear += 1;
            } else {
                currentMonth += 1;
            }
            sendRequest();
        });

        // GET 요청 보내기
        function sendRequest() {
            // 현재 URL의 쿼리 파라미터 가져오기
            const urlParams = new URLSearchParams(window.location.search);

            // year와 month 파라미터 업데이트
            urlParams.set('year', currentYear);
            urlParams.set('month', currentMonth);

            // emp 파라미터가 있는 경우 그대로 유지, 없는 경우 추가하지 않음
            const params = urlParams.toString();

            // 새 URL로 이동
            window.location.href = "?" + params;
        }

        // title 수정(직원근태관리 일 경우 param에 있는 값)
        const params = new URLSearchParams(window.location.search);
        const emp = params.get('empId'); // 'emp' 파라미터 값 가져오기
        if (emp) { // 'emp' 파라미터가 존재할 경우
            $('#main_title').text('직원 근태 관리 - ' + '${targetEmp.ename}');
        }
    });
</script>
</body>
</html>
