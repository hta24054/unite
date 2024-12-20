<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>휴일 설정</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.15/index.global.min.js'></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        .main-container {
            max-width: 800px;
            margin: 20px auto;
        }

        #button-container {
            text-align: right;
            margin-bottom: 10px;
        }

        #calendar-container {
            border: 1px solid #ddd;
            padding: 10px;
            background-color: #f9f9f9;
        }

        #main_title {
            color: #334466;
            margin-left: 500px;
            margin-bottom: 20px;
            font-weight: bold;
        }

        /* 요일 색상 설정 */
        .fc-day-sun a {
            color: red !important;
        }

        .fc-day-sat a {
            color: blue !important;
        }

        .fc-day:not(.fc-day-sat):not(.fc-day-sun) a {
            color: black !important;
        }

        #button-container button {
            background-color: #334466;
            color: white;
        }
    </style>
</head>
<body>
<jsp:include page="../common/header.jsp"/>
<jsp:include page="admin_leftbar.jsp"/>

<h2 id="main_title">휴일 설정</h2>

<div class="main-container">
    <div id="button-container">
        <button type="button" class="btn btn-sm" id="holidayImport">주말 휴일설정</button>
        <button type="button" class="btn btn-sm" id="publicHolidayImport">공휴일 휴일설정</button>
    </div>
    <div id="calendar-container">
        <div id="calendar"></div>
    </div>
</div>

<!-- 휴일 설정 모달 -->
<div class="modal fade" id="scheduleModal" tabindex="-1" aria-labelledby="scheduleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="scheduleModalLabel">휴일 설정</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="holidayForm">
                    <div class="form-group">
                        <label for="holidayReason">사유</label>
                        <input type="text" class="form-control" id="holidayReason" name="holidayReason"
                               placeholder="휴일 사유를 입력하세요" required>
                    </div>
                    <div class="form-group">
                        <label for="holidayDate">날짜</label>
                        <input type="date" class="form-control" id="holidayDate" name="holidayDate" readonly>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" id="saveHoliday">등록</button>
                <button type="button" class="btn btn-danger" id="deleteHoliday">삭제</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>

<script>
    (function () {
        $(function () {
            const $scheduleModal = $("#scheduleModal");
            const $holidayDate = $("#holidayDate");
            const $holidayReason = $("#holidayReason");

            const calendarEl = document.getElementById('calendar');
            const calendar = new FullCalendar.Calendar(calendarEl, {
                expandRows: true,
                slotMinTime: '08:00',
                slotMaxTime: '22:00',
                defaultAllDay: true,
                timeZone: 'local',
                headerToolbar: {
                    left: 'prev,next',
                    center: 'title',
                    right: 'today'
                },
                initialView: 'dayGridMonth',
                locale: 'ko',
                selectable: true,

                events: function (info, successCallback, failureCallback) {
                    const startMonth = info.startStr.substring(0, 7);
                    const endMonth = info.endStr.substring(0, 7);

                    $.ajax({
                        url: "${pageContext.request.contextPath}/admin/holiday/get?start=" + startMonth + "&end=" + endMonth,
                        type: 'GET',
                        dataType: 'json',
                        success: function (data) {
                            const events = data.holidayList.map(holiday => ({
                                title: holiday.holidayName,
                                start: holiday.holidayDate,
                                allDay: true,
                                color: '#334466'
                            }));
                            successCallback(events);
                        },
                        error: function () {
                            failureCallback();
                        }
                    });
                },

                dateClick: function (info) {
                    $holidayDate.val(info.dateStr);
                    $holidayReason.val('');
                    $("#saveHoliday").show();
                    $("#deleteHoliday").hide();
                    $scheduleModal.modal('show');
                },

                eventClick: function (info) {
                    $holidayDate.val(info.event.startStr);
                    $holidayReason.val(info.event.title);
                    $("#saveHoliday").hide();
                    $("#deleteHoliday").show();
                    $scheduleModal.modal('show');
                }
            });
            calendar.render();

            $("#holidayImport").on("click", function () {
                if (confirm("모든 주말이 휴일로 등록됩니다. 진행하시겠습니까?")) {
                    window.location.href = "${pageContext.request.contextPath}/admin/holiday/weekend";
                }
            });

            $("#publicHolidayImport").on("click", function () {
                if (confirm("모든 공휴일이 휴일로 등록됩니다. 진행하시겠습니까?")) {
                    window.location.href = "${pageContext.request.contextPath}/admin/holiday/api";
                }
            });

            $("#saveHoliday").on("click", function () {
                if (!$holidayReason.val()) {
                    alert("사유를 입력하세요");
                    return;
                }

                $.ajax({
                    url: "${pageContext.request.contextPath}/admin/holiday/insert",
                    type: "post",
                    data: {
                        date: $holidayDate.val(),
                        holidayName: $holidayReason.val()
                    },
                    success: function (data) {
                        alert(data.message);
                        $scheduleModal.modal("hide");
                        calendar.refetchEvents();
                    },
                    error: function () {
                        alert("등록에 실패했습니다.");
                    }
                });
            });

            $("#deleteHoliday").on("click", function () {
                if (confirm("정말 삭제하시겠습니까?")) {
                    $.ajax({
                        url: "${pageContext.request.contextPath}/admin/holiday/delete",
                        type: "post",
                        data: {
                            date: $holidayDate.val()
                        },
                        success: function (data) {
                            alert(data.message);
                            $scheduleModal.modal("hide");
                            calendar.refetchEvents();
                        },
                        error: function () {
                            alert("휴일 삭제를 실패했습니다.");
                        }
                    });
                }
            });
        });
    })();
</script>
</body>
</html>
