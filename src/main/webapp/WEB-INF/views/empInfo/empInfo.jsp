<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>나의 인사정보</title>
<link rel="stylesheet" type="text/css"
      href="${pageContext.request.contextPath}/css/empInfo.css">
<script
        src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
<jsp:include page="../common/header.jsp"/>
<jsp:include page="empInfo_leftbar.jsp"/>
<html>
<head>
    <style>
        table, .table {
            width: 90%;
            border-collapse: collapse;
            margin: 0 5%;
        }

        #photo {
            width: 5%;
        }

        th {
            background-color: #f2f2f2;
            font-weight: bold;
        }

        table {
            margin-top: 10px;
        }

        td, th {
            border: 2px solid black;
            padding: 10px;
            color: black;
            vertical-align: middle;
            text-align: center;
            width: 14%;
        }

        #tr {
            border-top: 2px solid black;
        }

        .table th {
            padding: 10px;
        }

        .table td {
            vertical-align: middle;
            padding: 5px;
        }

        h2 {
            text-align: left;
            color: rgb(51, 68, 102);
            margin: 0;
        }

        /* h2의 기본 여백 제거 */
        caption {
            caption-side: top;
            margin-bottom: 15px;
        }

        /* 캡션과 테이블 간격 설정 */
        input[readonly] {
            border: none;
            text-align: center;
        }

        /* 테두리 제거 */
        select[disabled] {
            border: none; /* 기본 테두리 제거 */
            outline: none; /* 포커스 테두리 제거 */
            background-color: transparent; /* 배경색 제거 */
            -webkit-appearance: none; /* 웹킷 브라우저 (Chrome, Safari 등) 기본 스타일 제거 */
            -moz-appearance: none; /* Firefox 브라우저 기본 스타일 제거 */
            appearance: none; /* 기타 브라우저 기본 스타일 제거 */
            color: black;
            text-align: center;
        }

        button#editButton {
            background-color: green;
            color: white;
        }

        button#cancelButton {
            background-color: red;
            color: white;
        }

        button#saveButton {
            margin-right: 5%;
        }
    </style>

    <script>
        $(document).ready(
            function () {
                var originalValues = {};

                // 원래 값을 저장하는 함수
                function saveOriginalValues() {
                    $(".editable").each(function () {
                        var inputName = $(this).attr("name");
                        originalValues[inputName] = $(this).val();
                    });
                }

                // 수정 버튼 클릭 시
                $("#editButton").click(
                    function () {
                        if ($(this).text() === "수정") {
                            saveOriginalValues();
                            $(".editable").removeAttr("readonly")
                                .removeAttr("disabled").css("border",
                                "1px solid #000");
                            $(this).text("취소").css("background-color",
                                "red").attr("id", "cancelButton");
                        } else if ($(this).text() === "취소") {
                            $(".editable").each(
                                function () {
                                    var inputName = $(this)
                                        .attr("name");
                                    $(this).val(
                                        originalValues[inputName]);
                                    $(this)
                                        .attr("readonly",
                                            "readonly").attr(
                                        "disabled",
                                        "disabled").css(
                                        "border", "none");
                                });
                            $(this).text("수정").css("background-color",
                                "green").attr("id", "editButton");
                        }
                    });
            });
    </script>


</head>
<body>
<div class="main-container">
    <div class="content">

        <%-- URL 파라미터로 전달된 empId 값과 현재 로그인한 사용자의 정보를 비교 --%>
        <%-- URL 파라미터로 전달된 empId 값과 현재 로그인한 사용자의 정보를 비교 --%>
        <c:set var="paramEmpId" value="${details.emp.empId}"/>
        <c:set var="currentEmpId" value="${sessionScope.id}"/>
        <c:set var="paramDeptId" value="${details.emp.deptId}"/>
        <c:set var="currentDeptId" value="${sessionScope.deptId}"/>


        <%-- 상단 텍스트 설정 --%>
        <c:choose>
            <c:when test="${paramEmpId.toString() == currentEmpId.toString()}">
                <c:set var="pageTitle" value="나의 인사정보"/>
            </c:when>
            <c:when test="${paramDeptId.toString() == currentDeptId.toString()}">
                <c:set var="pageTitle" value="부서원 인사정보"/>
            </c:when>
            <c:otherwise>
                <c:set var="pageTitle" value="타 부서원 인사정보"/>
            </c:otherwise>
        </c:choose>


        <form action="${pageContext.request.contextPath}/empInfo/update"
              method="post">
            <input type="hidden" name="id" value="${details.emp.empId}">
            <table class="table">
                <caption>
                    <h2>${pageTitle}</h2>
                </caption>
                <tr id="tr">
                    <th rowspan="4" id="photo"><img
                            src="${pageContext.request.contextPath}/emp/profile-image?UUID=${details.emp.imgUUID}"
                            alt="${details.emp.ename}의 사진" width="200"></th>
                    <th>이름</th>
                    <th>성별</th>
                    <th>이메일</th>
                    <th>내선번호</th>
                </tr>
                <tr>
                    <td>${details.emp.ename}</td>
                    <td><select name="gender" disabled>
                        <option value="남" ${details.emp.gender == '남' ? 'selected' : ''}>남성</option>
                        <option value="여" ${details.emp.gender == '여' ? 'selected' : ''}>여성</option>
                    </select></td>
                    <td><input type="text" name="email" class="editable"
                               value="${details.emp.email}" readonly></td>
                    <td><input type="text" name="tel" class="editable"
                               value="${details.emp.tel}" readonly></td>
                </tr>
                <tr>
                    <th>부서</th>
                    <th>사번</th>
                    <th>직책</th>
                    <th>휴대폰번호</th>
                </tr>
                <tr>
                    <td>${details.dept.deptName}</td>
                    <td>${details.emp.empId}</td>
                    <td>${details.job.jobName}</td>
                    <td><input type="text" name="mobile" class="editable"
                               value="${details.emp.mobile}" readonly></td>
                </tr>
            </table>

            <table>
                <tr>
                    <th>입사일</th>
                    <td><input type="text" name="hiredate"
                               placeholder="YYYY/MM/DD" value="${details.emp.hireDate}" readonly></td>

                    <th rowspan="2">계좌번호</th>
                    <td rowspan="2">${details.emp.bank}${details.emp.account}</td>
                    <th>긴급연락처</th>
                    <td><input type="text" name="mobile2" class="editable"
                               value="${details.emp.mobile2}" readonly></td>
                </tr>
                <tr>
                    <th>채용구분</th>
                    <td><select name="hiretype" disabled>
                        <option value="경력"
                        ${details.emp.hireType == '경력' ? 'selected' : ''}>경력
                        </option>
                        <option value="신입"
                        ${details.emp.hireType == '신입' ? 'selected' : ''}>신입
                        </option>
                        <option value="인턴"
                        ${details.emp.hireType == '인턴' ? 'selected' : ''}>인턴
                        </option>
                    </select></td>
                    <th>직원구분</th>
                    <td><select name="etype" disabled>
                        <option value="정규직"
                        ${details.emp.etype == '정규직' ? 'selected' : ''}>정규직
                        </option>
                        <option value="계약직"
                        ${details.emp.etype == '계약직' ? 'selected' : ''}>계약직
                        </option>
                        <option value="퇴직"
                        ${details.emp.etype == '퇴직' ? 'selected' : ''}>퇴직
                        </option>
                    </select></td>
                </tr>
                <tr>
                    <th>생년월일</th>
                    <td><input type="text" name="birthday"
                               placeholder="YYYY/MM/DD" value="${details.emp.birthday}" readonly>
                        <select name="birthday_type" disabled>
                            <option value="양력"
                            ${details.emp.birthdayType == '양력' ? 'selected' : ''}>양력
                            </option>
                            <option value="음력"
                            ${details.emp.birthdayType == '음력' ? 'selected' : ''}>음력
                            </option>
                        </select></td>
                    <th>주소</th>
                    <td><input type="text" name="address" class="editable"
                               value="${details.emp.address}" readonly></td>
                    <th>자격증</th>
                    <td><c:forEach var="cert" items="${details.certList}">
                        <span>${cert.certName}</span>
                        <br/>
                    </c:forEach></td>


                </tr>

                <tr>
                    <th>최종학력</th>
                    <td>${details.emp.school}</td>
                    <th>결혼여부</th>
                    <td><select name="married" class="editable" disabled>
                        <option value="1"
                        ${details.emp.married == '1' ? 'selected' : ''}>Y
                        </option>
                        <option value="0"
                        ${details.emp.married == '0' ? 'selected' : ''}>N
                        </option>
                    </select></td>
                    <th rowspan="2">외국어능력</th>
                    <td rowspan="2"><c:forEach var="lang"
                                               items="${details.langList}">
                        <span>${lang.langName}</span>
                        <br/>
                    </c:forEach></td>
                </tr>
                <tr>
                    <th>전공</th>
                    <td>${details.emp.major}</td>
                    <th>자녀</th>
                    <td><select name="child" disabled>
                        <option value="1" ${details.emp.child == '1' ? 'selected' : ''}>Y</option>
                        <option value="0" ${details.emp.child == '0' ? 'selected' : ''}>N</option>
                    </select></td>

                </tr>
            </table>
            <c:if test="${details.emp.empId == sessionScope.id}">
                <button type="button" id="editButton">수정</button>
                <button type="submit" id="saveButton">저장</button>
            </c:if>
        </form>
    </div>
</div>

</body>
</html>