<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>출퇴근바</title>
    <style>
        .button-inactive {
            background-color: #e9ecef;
            pointer-events: none;
        }

        .button-active {
            background-color: #334466;
            color: #fff;
        }
        #horizon{
            border-bottom: black 1px solid;
        }
        #box {
            padding: 30px;
            border: 1px solid black;
        }
    </style>
</head>
<body>
<div id="box">
    <h4>현재 시각</h4>
    <p><span id="currentDay"></span></p>
    <h3 id = "horizon"><span id="currentTime"></span></h3>
    <p>출근시각: <span id="startTime">미등록</span></p>
    <p>퇴근시각: <span id="endTime">미등록</span></p>
    <p>근무구분:
        <select id="workType" class="form-select">
            <option value="일반">일반</option>
            <option value="외근">외근</option>
            <option value="출장">출장</option>
            <option value="휴가">휴가</option>
        </select>
    </p>
    <button id="startButton" class="btn button-active">출근하기</button>
    <button id="endButton" class="btn button-inactive">퇴근하기</button>
</div>
<script>
    //오늘 날짜 표시
    $(document).ready(function() {
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
        const day = String(now.getDate()).padStart(2, '0');

        const formattedDate = year+"-"+month+"-"+day;
        $("#currentDay").text(formattedDate);
    });

    // 현재 시각 표시
    setInterval(() => {
        const now = new Date();
        $("#currentTime").text(now.toLocaleTimeString());
    }, 1000);

    // 출근/퇴근 버튼 상태 전환
    $("#startButton").click(function () {
        const now = new Date().toLocaleTimeString();
        $("#startTime").text(now);
        $(this).addClass("button-inactive").removeClass("button-active");
        $("#endButton").addClass("button-active").removeClass("button-inactive");
    });

    $("#endButton").click(function () {
        const now = new Date().toLocaleTimeString();
        $("#endTime").text(now);
        $(this).addClass("button-inactive").removeClass("button-active");
        $("#startButton").addClass("button-active").removeClass("button-inactive");
    });
</script>
</body>

</html>
