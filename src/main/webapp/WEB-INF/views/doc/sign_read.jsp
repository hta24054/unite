<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sign.css">
    <title>결재자</title>
    <style>
        .sign-time {
            font-size: 12px;
            height: 40px;
            min-height: 40px;
        }
    </style>
</head>
<body>
<div class="approval-table-container">
    <table class="approval-table table table-bordered" id="approvalTable">
        <tr>
            <td rowspan="3" class="label-cell" id="title">결재</td>
            <c:forEach var="sign" items="${signList}" varStatus="status">
                <th class="label-cell">
                    <c:choose>
                        <c:when test="${status.index == 0}">
                            기안자
                        </c:when>
                        <c:otherwise>
                            결재자${status.index}
                        </c:otherwise>
                    </c:choose>
                </th>
            </c:forEach>
        </tr>
        <tr>
            <c:forEach var="sign" items="${signList}">
                <td class="name">${nameMap[sign.empId]}</td>
            </c:forEach>
        </tr>
        <tr>
            <c:forEach var="sign" items="${signList}">
                <td class="date">
                    <span class="sign-time" data-sign-time="${sign.signTime}">&nbsp;</span>
                </td>
            </c:forEach>
        </tr>

    </table>
</div>

<script>
    $(document).ready(function () {
        $(".sign-time").each(function () {
            const signTime = $(this).data("sign-time");
            if (signTime) {
                const [date, time] = signTime.split("T");
                $(this).html(date + '<br>' + time);
            }
        });
    });
</script>
</body>
</html>