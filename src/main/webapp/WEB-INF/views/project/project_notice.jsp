<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
    <style>
		.styled-table2 {
		    width: 100%;  /* 테이블 크기가 부모에 맞게 확장 */
		    border-collapse: collapse;  /* 테이블 셀 간격 없애기 */
		}
		
		.styled-table2 td {
		    padding: 10px;  /* 셀 안쪽 여백 */
		    border-bottom: 1px solid #ccc;  /* 각 셀 밑줄 */
		    word-wrap: break-word;  /* 긴 텍스트가 셀 안에서 자동으로 줄 바꿈 */
		}
		
		/* 필요시 테이블 내용이 너무 길면 감싸는 div도 늘어나도록 */
		.notification-content {
		    overflow: hidden;
		}
		
		/* 3번째 td에만 밑줄을 추가하고 싶을 경우 */
		.styled-table2 td:nth-child(3) {
		    border-bottom: 2px solid #ccc;  /* 세 번째 td에 더 두꺼운 밑줄 */
		}
    </style>
</head>
<body>
    <div class="col-md-4 notification home_notice">
        <h5>프로젝트 알림</h5>
        <div class="notification-content">
            <table class="styled-table2">
                <tbody>
                    <tr><td>게시글이 없습니다</td><td></td></tr>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
