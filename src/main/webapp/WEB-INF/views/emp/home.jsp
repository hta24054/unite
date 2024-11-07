<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <style>
        .left {
            font-size: 20px;
            line-height: 50px;
        }

        h3 {
            font-size: 30px;
            font-weight: bold;
        }

        .sidebar {
            height: calc(100vh - 50px);
            border-right: 2px solid rgb(51, 68, 102);
            padding: 30px 100px 30px 50px;
            float: left;
            margin-top: -50px;
        }

        .left a {
            color: black;
        }
    </style>
    
    <jsp:include page="../common/header.jsp"/>
</head>
<body>
	<div class="sidebar">
		<div id="box">
		    <h4>현재 시각</h4>
		    <p><span id="currentDay">2024-11-07</span></p>
		    <h3 id="horizon"><span id="currentTime">오후 12:04:36</span></h3>
		    <p>출근시각: <span id="startTime">미등록</span></p>
		    <p>퇴근시각: <span id="endTime">미등록</span></p>
		    <p>근무구분:
		        <select id="workType" class="form-select">
		            <option value="일반">일반</option>
		            <option value="외근">외근</option>
		            <option value="출장">출장</option>
		        </select>
		    </p>
		    <button id="startButton" class="btn button-active">출근하기</button>
		    <button id="endButton" class="btn button-inactive">퇴근하기</button>
		</div>
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
	        const emp = params.get('emp'); // 'emp' 파라미터 값 가져오기
	        if (emp) { // 'emp' 파라미터가 존재할 경우
	            $('#main_title').text('직원 근태 관리-' + emp);
	        }
	    });
</script>
</body>
</html>