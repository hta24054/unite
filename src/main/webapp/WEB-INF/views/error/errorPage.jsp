<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>error.jsp</title>
    <style>
        body {
            background-color: #334466;
            display: flex; /* Flexbox 사용 */
            justify-content: center; /* 수평 중앙 정렬 */
            align-items: center; /* 수직 중앙 정렬 */
            height: 100vh; /* 뷰포트 전체 높이 */
            margin: 0; /* 기본 여백 제거 */
            flex-direction: column; /* 세로로 정렬 */
        }
        span {
            font-size: 50px;
            color: white;
            text-align: center;
            margin-top: 20px; /* 이미지와 텍스트 간 간격 */
        }
    </style>
</head>

<body>
<img src="${pageContext.request.contextPath}/image/logo_header.png" style="width: 300px;">
<span>잘못된 요청입니다.</span>
</body>
</html>