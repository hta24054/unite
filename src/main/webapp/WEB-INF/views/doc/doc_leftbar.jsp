<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
</head>
<body>
<div class="sidebar">
    <h3 style="color:rgb(51, 68, 102)">전자문서</h3><br>
    <ul class="list-group" style="list-style-type: disc; padding-left: 20px;">
        <li class="left" style="border: none;"><a href="${pageContext.request.contextPath}/doc/waiting">결재 대기 문서</a></li>

        <!-- 문서 기안 메뉴 -->
        <li class="left" style="border: none;">
            <a href="#" onclick="toggleSubMenu('#submenu-doc-draft')">문서 기안</a>
            <ul class="submenu" id="submenu-doc-draft">
                <li class="left" style="border: none;">
                    <a href="${pageContext.request.contextPath}/doc/general" class="submenu-link">일반 문서</a>
                </li>
                <li class="left" style="border: none;">
                    <a href="${pageContext.request.contextPath}/doc/vacation" class="submenu-link">휴가 신청</a>
                </li>
                <li class="left" style="border: none;">
                    <a href="${pageContext.request.contextPath}/doc/trip" class="submenu-link">출장 신청</a>
                </li>
                <li class="left" style="border: none;">
                    <a href="${pageContext.request.contextPath}/doc/buy" class="submenu-link">구매 신청</a>
                </li>
            </ul>
        </li>

        <li class="left" style="border: none;"><a href="${pageContext.request.contextPath}/doc/in-progress">결재 진행 문서</a></li>

        <!-- 문서 등록 대장 메뉴 -->
        <li class="left" style="border: none;">
            <a href="#" onclick="toggleSubMenu('#submenu-doc-list')">문서등록대장</a>
            <ul class="submenu" id="submenu-doc-list">
                <li class="left" style="border: none;">
                    <a href="${pageContext.request.contextPath}/doc/list/dept" class="submenu-link">부서 기안문서</a>
                </li>
                <li class="left" style="border: none;">
                    <a href="${pageContext.request.contextPath}/doc/list/sign" class="submenu-link">내가 결재한 문서</a>
                </li>
            </ul>
        </li>
    </ul>
</div>
</body>
</html>
