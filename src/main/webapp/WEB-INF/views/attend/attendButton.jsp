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

        #horizon {
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
    <h3 id="horizon"><span id="currentTime">&nbsp;</span></h3>
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
<script>
    // 오늘 날짜 표시
    $(document).ready(function () {
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
        const day = String(now.getDate()).padStart(2, '0');

        const formattedDate = year + "-" + month + "-" + day;
        $("#currentDay").text(formattedDate);

        // 페이지 로딩 시 AJAX로 출근 기록 확인
        $.ajax({
            url: "${pageContext.request.contextPath}/attend/attendInfo", // 서버에서 출근 기록을 가져오는 URL
            method: "GET",
            dataType: "json",
            success: function (data) {
                // 서버에서 반환된 출근 기록이 있을 경우 화면에 표시
                console.log(data.status)
                if (data.status === 'empty') {//출근 안한경우
                    $("#workType").prop("disabled", false);
                } else if (data.status === 'checkIn') { //출근상태
                    const inTime = getLocalTimeToString(data.inTime);
                    $("#startTime").text(inTime);
                    $("#startButton").addClass("button-inactive").removeClass("button-active");
                    $("#endButton").addClass("button-active").removeClass("button-inactive");
                    // 서버에서 받아온 근무 구분 값을 선택
                    $("#workType").val(data.type).prop("disabled", true);
                } else if (data.status === 'checkOut') { //퇴근까지 마친 상태
                    const inTime = getLocalTimeToString(data.inTime);
                    const outTime = getLocalTimeToString(data.outTime);
                    $("#startTime").text(inTime);
                    $("#endTime").text(outTime);
                    $("#startButton").addClass("button-inactive").removeClass("button-active");
                    $("#endButton").addClass("button-inactive").removeClass("button-active");
                    // 서버에서 받아온 근무 구분 값을 선택
                    $("#workType").val(data.type).prop("disabled", true);
                }
            },
            error: function () {
                console.log("출근 기록을 가져오는 데 실패했습니다.");
            }
        });
    });

    //시간 표시 설정
    function getLocalTimeToString(localDateTimeString) {
        // LocalDateTime 문자열에서 날짜와 시간 분리
        const [datePart, timePart] = localDateTimeString.split("T");
        const [year, month, day] = datePart.split("-").map(Number);
        const [hour, minute, second] = timePart.split(":").map(Number);

        // 로컬 시간대로 Date 객체 생성 (주의: month는 0부터 시작하므로 -1 필요)
        const date = new Date(year, month - 1, day, hour, minute, second);

        // 로컬 시간대에 맞춰 '오전/오후 HH:mm:ss' 형식으로 반환
        return date.toLocaleTimeString("ko-KR", {
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            hour12: true // 12시간 형식 (오전/오후)
        });
    }

    // 현재 시각 표시
    setInterval(() => {
        const now = new Date();
        $("#currentTime").text(now.toLocaleTimeString());
    }, 100);

    // 출근/퇴근 버튼 상태 전환
    $("#startButton").click(function () {
        const now = new Date().toLocaleTimeString();
        $("#startTime").text(now);
        $(this).addClass("button-inactive").removeClass("button-active");
        $("#endButton").addClass("button-active").removeClass("button-inactive");
        let type = $("#workType").val();

        // 출근 시간 저장을 위한 AJAX 요청
        $.ajax({
            url: "${pageContext.request.contextPath}/attend/attendIn",
            method: "POST",
            data: {attendType: type},
            success: function (data) {
                if (data.status === 'success') {
                    console.log("출근 요청 정상, 출근 처리 정상");
                } else {
                    console.log("출근 요청 정상, 출근 처리 비정상");
                }
            },
            error: function () {
                console.log("출근 요청 비정상");
            }
        });
    });

    $("#endButton").click(function () {
        const now = new Date().toLocaleTimeString();
        $("#endTime").text(now);
        $(this).addClass("button-inactive").removeClass("button-active");
        $("#startButton").addClass("button-inactive").removeClass("button-active");

        // 퇴근 시간 저장을 위한 AJAX 요청
        $.ajax({
            url: "${pageContext.request.contextPath}/attend/attendOut",
            method: "POST",
            success: function (data) {
                if (data.status === 'success') {
                    console.log("퇴근 요청 정상, 퇴근 처리 정상");
                } else {
                    console.log("퇴근 요청 정상, 퇴근 처리 비정상");
                }
            },
            error: function () {
                console.log("퇴근 요청 비정상, 퇴근 처리 정상");
            }
        });
    });
</script>
</body>

</html>
