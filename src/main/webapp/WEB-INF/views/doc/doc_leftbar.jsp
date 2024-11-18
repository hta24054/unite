<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <style>
        .left { font-size: 20px; line-height: 50px; }
        h3 { font-size: 30px; font-weight: bold; }
        .sidebar {
            height: calc(100vh - 50px);
            border-right: 2px solid rgb(51, 68, 102);
            padding: 30px 100px 30px 50px;
            float: left;
            margin-top: -50px;
        }
        .left a { color: black; text-decoration: none; }
        .left a.active { color: rgb(51, 68, 102); font-weight: bold; }

        /* 하위 메뉴 스타일 */
        .submenu {
            display: none;
            list-style-type: disc;
            padding-left: 40px;
        }
        .submenu .left {
            font-size: 18px;
        }
        .submenu .left a {
            color: black;
        }
    </style>
</head>
<body>
<div class="sidebar">
    <h3 style="color:rgb(51, 68, 102)">관리자</h3><br>
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

<script>
    // 하위 메뉴 열고 닫기
    function toggleSubMenu(submenuId) {
        $(submenuId).toggle();
    }

    // 현재 URL에 따라 메뉴 항목 강조 표시
    $(document).ready(function() {
        const currentPath = window.location.pathname;

        $('.sidebar a').each(function() {
            const link = $(this);
            if (link.attr('href') === currentPath) {
                link.addClass('active');
                link.closest('.submenu').show(); // 하위 메뉴 열기
            }
        });
    });
</script>
</body>
</html>
