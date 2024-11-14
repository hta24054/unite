<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="doc_leftbar.jsp"/>

    <meta charset="UTF-8">
    <title>휴가신청서</title>
    <style>
        .header-cell {
            font-size: 36px;
            font-weight: bold;
            padding: 20px 0;
        }

        .title-input, .form-control {
            font-size: 16px;
        }

        .table-bordered, .table-bordered td, .table-bordered th {
            align-content: center;
            border-color: black !important;
        }

        #info {
            text-align: left;
        }
    </style>
</head>
<body>
<form id="doc_form" enctype="multipart/form-data">
    <div class="container mt-4">
        <div class="text-center mb-4">
            <h1 class="header-cell">휴가신청서</h1>
        </div>

        <div class="row">

            <div class="col-md-6 mb-3">
                <table class="table table-bordered">
                    <tr>
                        <td class="table-secondary font-weight-bold text-center">문서번호</td>
                        <td>${docVacation.docId}</td>
                    </tr>
                    <tr>
                        <td class="table-secondary font-weight-bold text-center">부&nbsp;&nbsp;&nbsp;서</td>
                        <td>${dept.deptName}</td>
                    </tr>
                    <tr>
                        <td class="table-secondary font-weight-bold text-center">기 안 자</td>
                        <td>${writer.ename}</td>
                    </tr>
                    <tr>
                        <td class="table-secondary font-weight-bold text-center">작 성 일</td>
                        <td>${docVacation.docCreateDate.toLocalDate()}</td>
                    </tr>
                </table>
            </div>
            <div class="col-md-6">
                <jsp:include page="sign_read.jsp"/>
            </div>
        </div>

        <table class="table table-bordered mt-4 item_table" id="itemTable">
            <tr>
                <th class="table-secondary font-weight-bold text-center">제목</th>
                <td>${docVacation.docTitle}</td>
            </tr>
            <tr>
                <th class="table-secondary font-weight-bold text-center">휴가 종류</th>
                <td>${docVacation.vacationType}
                </td>
            </tr>
            <tr>
                <th class="table-secondary font-weight-bold text-center">시작일</th>
                <td>${docVacation.vacationStart}</td>
            </tr>
            <tr>
                <th class="table-secondary font-weight-bold text-center">종료일</th>
                <td>${docVacation.vacationEnd}</td>
            </tr>
            <tr>
                <th class="table-secondary font-weight-bold text-center">휴가 일수</th>
                <td>${docVacation.vacationCount}</td>
            </tr>
            <tr>
                <th class="table-secondary font-weight-bold text-center">휴가 사유</th>
                <td>${docVacation.docContent}</td>
            </tr>
            <tr>
                <th class="table-secondary font-weight-bold text-center">첨부 파일</th>
                <td>
                    <c:choose>
                        <c:when test="${docVacation.vacationFileOriginal != null}">
                            <a href="${pageContext.request.contextPath}/doc/download?fileName=${docVacation.vacationFileOriginal}&fileUUID=${docVacation.vacationFileUUID}">
                                    ${docVacation.vacationFileOriginal}
                            </a>
                        </c:when>
                        <c:otherwise>
                            파일 없음
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <th colspan="2" class="table-secondary font-weight-bold" id="info">
                    1. 연차의 사용은 근로기준법에 따라 전년도에 발생한 개인별 잔여 연차에 한하여 사용함을 원칙으로 한다.<br>
                    단, 최초 입사시에는 근로기준법에 따라 발생 예정된 연차를 사용하여 월 1회 사용할 수 있다.<br>
                    2. 경조사 휴가는 증빙서류를 제출할 수 있는 가족관계증명서 또는 청첩장 등 제출<br>
                    3. 공가(예비군/민방위)는 사전에 통지서를, 사후에 참석증을 반드시 제출
                </th>
            </tr>
        </table>
    </div>
    <!-- 버튼 영역 -->
    <jsp:include page="doc_read_button.jsp"/>
</form>
</body>
</html>