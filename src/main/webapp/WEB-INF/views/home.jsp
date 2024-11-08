<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <style>
        <%--.left {
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
        }--%>
        /* 전체 레이아웃 */
        .container {
            display: flex;
            height: 100vh; padding: 0; 
		    margin: 0; 
		}

        /* 왼쪽 25% */
        .left {
            width: 25%;
            padding: 10px;
            box-sizing: border-box;
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        /* 가운데 50% */
        .center {
            width: 50%;
            padding: 10px;
            box-sizing: border-box;
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        /* 오른쪽 25% */
        .right {
            width: 25%;
            height: 1500px;
            padding: 10px;
            box-sizing: border-box;
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        /* 테이블 기본 스타일 */
        .table {
            border-radius: 10px;
            border: 1px solid #ccc;
            padding: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }

        .table th, .table td {
            padding: 10px;
            text-align: center;
        }

        .table thead {
            background-color: #f4f4f4;
        }

        /* 사이드바 스타일 */
        .sidebar {
            height: calc(100vh - 50px);
            border-right: 2px solid rgb(51, 68, 102);
            padding: 30px 100px 30px 50px;
            margin-top: -50px;
        }

        .left a {
            color: black;
        }

        .button-active {
            background-color: #28a745;
            color: white;
        }

        .button-inactive {
            background-color: #ccc;
            color: white;
        }
    </style>
    <jsp:include page="common/header.jsp"/>
</head>
<body>
<body>
    <div class="container">
        <!-- 왼쪽 25% -->
        <div class="left">
            <!-- 여기에서 기존 테이블을 제거하고 div를 추가 -->
            <div class="table">
                <h4>중앙 테이블 1</h4>
                <table>
                    <thead>
                        <tr>
                            <th>제목</th>
                            <th>내용</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>데이터1</td>
                            <td>데이터1 내용</td>
                        </tr>
                        <tr>
                            <td>데이터2</td>
                            <td>데이터2 내용</td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="table">
                <ul>
                    <li>출근시각: <span id="startTime">미등록</span></li>
                    <li>퇴근시각: <span id="endTime">미등록</span></li>
                    <li>근무구분:
                        <select id="workType" class="form-select">
                            <option value="일반">일반</option>
                            <option value="외근">외근</option>
                            <option value="출장">출장</option>
                        </select>
                    </li>
                </ul>
                <button id="startButton" class="btn button-active">출근하기</button>
                <button id="endButton" class="btn button-inactive">퇴근하기</button>
            </div>
        </div>

        <!-- 가운데 50% -->
        <div class="center">
            <div class="table">
                <h4>중앙 테이블 1</h4>
                <table>
                    <thead>
                        <tr>
                            <th>제목</th>
                            <th>내용</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>데이터1</td>
                            <td>데이터1 내용</td>
                        </tr>
                        <tr>
                            <td>데이터2</td>
                            <td>데이터2 내용</td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="table">
                <h4>중앙 테이블 2</h4>
                <table>
                    <thead>
                        <tr>
                            <th>제목</th>
                            <th>내용</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>데이터1</td>
                            <td>데이터1 내용</td>
                        </tr>
                        <tr>
                            <td>데이터2</td>
                            <td>데이터2 내용</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- 오른쪽 25% -->
        <div class="right">
            <div class="table">
                <h4>오른쪽 테이블 1</h4>
                <table>
                    <thead>
                        <tr>
                            <th>제목</th>
                            <th>내용</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>데이터1</td>
                            <td>데이터1 내용</td>
                        </tr>
                        <tr>
                            <td>데이터2</td>
                            <td>데이터2 내용</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <%--<script>
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

            function sendRequest() {
                // AJAX 요청
                $.ajax({
                    url: '/date-check?year=' + currentYear + '&month=' + currentMonth,
                    method: 'GET',
                    success: function (response) {
                        $("#calendar").html(response);
                    }
                });
            }
        });
    </script> --%>
</body>
</body>
</html>