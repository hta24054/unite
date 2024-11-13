<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <style>
        /* 전체 페이지 레이아웃 설정 */
        .container {
            display: grid;
            grid-template-columns: 1fr 2fr 1fr;
            gap: 10px;
            padding: 20px;
            height: 100vh;
            box-sizing: border-box;
        }

        /* 각 영역 스타일 설정 */
        .left-top, .left-bottom {
            background-color: #f8f8f8;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        .center-top, .center-bottom {
            background-color: #ffffff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        .right {
            grid-column: span 1;
            background-color: #ffffff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            display: flex;
            flex-direction: column;
            gap: 20px;
        }

        /* 왼쪽 두 개의 영역 */
        .left-top {
            grid-row: 1 / 2;
        }

        .left-bottom {
            grid-row: 2 / 3;
        }

        /* 가운데 두 개의 영역 */
        .center-top {
            grid-row: 1 / 2;
        }

        .center-bottom {
            grid-row: 2 / 3;
        }
    </style>
    <jsp:include page="common/header.jsp"/>
</head>
<body>
    <div class="container">
        <!-- 왼쪽 위 -->
        <div class="left-top">
            <h2>프로필 정보</h2>
            <!-- 사용자 프로필 정보 -->
        </div>

        <!-- 왼쪽 아래 -->
        <div class="left-bottom">
            <h2>근태 정보</h2>
            <jsp:include page="attend/attendButton.jsp"/>
        </div>

        <!-- 가운데 위 -->
        <div class="center-top">
            <h2>게시판</h2>
            <!-- 게시판 내용 -->
        </div>

        <!-- 가운데 아래 -->
        <div class="center-bottom">
            <h2>결재 대기 문서</h2>
            <!-- 결재 대기 문서 내용 -->
        </div>

        <!-- 오른쪽 전체 -->
        <div class="right">
            <h2>최근 알림</h2>
            <!-- 최근 알림 내용 -->
        </div>
    </div>
</body>
</html>
