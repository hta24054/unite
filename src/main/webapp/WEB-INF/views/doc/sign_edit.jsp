<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sign.css">
    <script src="${pageContext.request.contextPath }/js/sign_write.js"></script>
    <title>결재자</title>
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
        <c:forEach var="sign" items="${signList}">
            <input type="hidden" name="sign[]" value="${sign.empId}">
        </c:forEach>
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
                <h5 class="modal-title" id="employeeModalLabel">
                    조직도 및 직원 검색<span style="color: red">(더블클릭으로 추가)</span></h5>
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