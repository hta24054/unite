const contextPath = /*[[@{/}]]*/ '';

$(document).ready(function () {
    let calendar;
    let events = [];
    let holidayEvents = []; // 공휴일 이벤트 배열
    let isAllDayChk, startDate, endDate;
    let isHolidayDataLoaded = false;  // 공휴일 데이터 로드 여부

    initCalendar();
    fetchAllData();

    // 개인/공유/부서 일정, 공휴일 불러오기
    function fetchAllData() {
        events = []; // 배열 초기화

        const startDate = moment().startOf('month').subtract(7, 'days').format('YYYY-MM-DD');
        const endDate = moment().endOf('month').add(7, 'days').format('YYYY-MM-DD');

        Promise.all([
            fetchListData(startDate, endDate),
            fetchSharedListData()
        ]).then(() => {
            holidayCurrentMonth();
        });
    }

    // 공휴일 연도, 월
    function holidayCurrentMonth() {
        const year = moment().startOf('year').format('YYYY');
        const month = moment().endOf('month').format('MM');
        fetchHolidayData(year, month);
    }

    // 공휴일 불러오기
    function fetchHolidayData(year, month) {
        $.ajax({
            url: contextPath + "/schedule/getHoliday",
            type: "GET",
            data: {
                year: year,
                month: month
            },
            dataType: "json",
            success: function (data) {
                holidayEvents = [];

                if (data != null) {
                    data.forEach(function (holiday) {
                        holidayEvents.push({
                            title: holiday.holidayName,
                            start: holiday.holidayDate,
                            allDay: true,
                            color: 'red',
                            editable: false,  // 드래그 불가
                            droppable: false, // 다른 날짜로 드래그 불가
                            extendedProps: {
                                isHoliday: true,
                            }
                        });
                    });
                }

                // 기존 공휴일 데이터 소스를 제거
                if (calendar && calendar.getEventSourceById("holidayList") != null) {
                    calendar.getEventSourceById("holidayList").remove();
                }

                if (calendar && calendar.addEventSource) {
                    // 새로운 공휴일 이벤트 추가
                    calendar.addEventSource({
                        id: "holidayList",
                        events: holidayEvents // 새로운 공휴일 이벤트를 추가
                    });
                }

                // 캘린더 이벤트 갱신
                if (typeof calendar !== 'undefined' && calendar !== null) {
                    // 캘린더가 있을 때만 refetchEvents 호출
                    if (typeof calendar.refetchEvents === 'function') {
                        calendar.refetchEvents();
                    }
                }
            },
            error: function (error) {
                console.log('공휴일 불러오기 오류', error);
            }
        });
    }

    // 일정 리스트 불러오기
    function fetchListData(startDate, endDate) {
        $.ajax({
            url:  contextPath + "/schedule/scheduleList",
            type: "get",
            dataType: "json",
            data: {
                emp_id: $("#emp_id").val(),
                startDate: startDate,
                endDate: endDate
            },
            success: function (data) {
                if (data != null) {
                    for (let i = 0; i < data.length; i++) {
                        const schedule = data[i];
                        // 중복 체크: schedule_id로 중복 여부 확인
                        if (!events.some(event => event.id === schedule.scheduleId)) {
                            events.push({
                                title: schedule.scheduleName,
                                start: schedule.scheduleStart,
                                end: schedule.scheduleEnd,
                                backgroundColor: schedule.scheduleColor,
                                description: schedule.scheduleContent,
                                allDay: schedule.scheduleAllDay,
                                id: schedule.scheduleId,
                                extendedProps: {
                                    isShared: false, // 공유 일정
                                },
                            });
                        }
                    }
                }

                // 기존 일정 데이터 소스를 제거
                if (calendar.getEventSourceById("scheduleList") != null) {
                    calendar.getEventSourceById("scheduleList").remove();
                }

                // 새로운 일정 이벤트 추가
                calendar.addEventSource({
                    id: "scheduleList",
                    events: events
                });
            },
            error: function (error) {
                console.log('개인 일정 리스트 불러오기 오류', error);
            }
        });
    }

    // 공유 일정 리스트 불러오기
    function fetchSharedListData() {
        $.ajax({
            url: contextPath + "/schedule/sharedScheduleList",
            type: "get",
            dataType: "json",
            data: {
                emp_id: $("#emp_id").val()
            },
            success: function(data) {
                if (data != null) {
                    for (let i = 0; i < data.length; i++) {
                        // 중복 체크: schedule_id로 중복 여부 확인
                        if (!events.some(event => event.id === data[i].scheduleId)) {
                            const isEditable = data[i].empId === $("#emp_id").val();
                            const isDroppable = isEditable;

                            events.push({
                                id: data[i].scheduleId,
                                title: data[i].scheduleName,
                                start: data[i].scheduleStart,
                                end: data[i].scheduleEnd,
                                backgroundColor: data[i].scheduleColor,
                                description: data[i].scheduleContent,
                                allDay: data[i].scheduleAllDay,
                                editable: isEditable, // 등록자만 수정 가능
                                droppable: isDroppable, // 등록자만 드래그 가능
                                extendedProps: {
                                    isShared: true, // 공유 일정
                                    shareEmpNames: data[i].shareEmpNames, // 공유자 이름 목록
                                    empId: data[i].empId, // 본인의 empId (등록자 empId)
                                    empIdName: data[i].empIdName, // 본인 이름
                                },
                            })
                        }
                    }
                }

                holidayCurrentMonth();
                initCalendar();
            },
            error: function (error) {
                console.log("공유 일정 리스트 불러오기 오류", error);
            }
        });
    }

    let isDeptSchedule = false; // 부서 일정 체크박스
    // 체크박스 클릭 시 부서 일정 리스트 불러오기
    $('#departmentScheduleCheckbox').on('change', function() {
        isDeptSchedule = $(this).prop('checked');

        if (isDeptSchedule) {
            fetchDeptListData();
        } else {
            // 부서 일정 제거
            events = events.filter(event => !event.extendedProps.isDept);
            initCalendar();
        }

        fetchAllData();
    });

    // 부서 일정 리스트 불러오기
    function fetchDeptListData() {
        $.ajax({
            url: contextPath + "/schedule/deptScheduleList",
            type: "get",
            dataType: "json",
            data: {
                emp_id: $("#emp_id").val(),  // 로그인된 직원 ID
                dept_id: $('#dept_id').val(),
            },
            success: function(data) {
                if (data != null) {
                    for (let i = 0; i < data.length; i++) {

                        // 중복 체크: schedule_id로 중복 여부 확인
                        if (!events.some(event => event.id === data[i].scheduleId)) {
                            events.push({
                                id: data[i].scheduleId,
                                title: data[i].scheduleName,
                                start: data[i].scheduleStart,
                                end: data[i].scheduleEnd,
                                backgroundColor: data[i].scheduleColor,
                                description: data[i].scheduleContent,
                                allDay: data[i].scheduleAllDay,
                                editable: false, // 수정 불가
                                droppable: false, // 드래그 불가
                                extendedProps: {
                                    isDept: true, // 부서 일정
                                    isShared: false
                                },
                            })
                        }
                    }
                }

                initCalendar();
            },
            error: function (error) {
                console.log("부서 일정 리스트 불러오기 오류", error);
            }
        });
    }

    // 일정 등록
    function addEvent(eventData) {
        $.ajax({
            url: contextPath + "/schedule/scheduleAdd",
            type: "post",
            dataType: "json",
            data: {
                empId: $("#emp_id").val(),
                scheduleName: eventData.schedule_name,
                scheduleStart: moment(eventData.startAt).format('YYYY-MM-DD HH:mm'),
                scheduleEnd: moment(eventData.endAt).format('YYYY-MM-DD HH:mm'),
                scheduleColor: eventData.bgColor,
                scheduleContent: eventData.description,
                scheduleAllDay: eventData.allDay ? 1 : 0 // true이면 1, false이면 0
            },
            success: function (data) {
                if (data != null && data.length > 0) {
                    for (let i = 0; i < data.length; i++) {
                        const isAllDay = data[i].schedule_allDay === 1;
                        events.push({
                            id: data[i].scheduleId,
                            title: data[i].scheduleName,
                            start: data[i].scheduleStart,
                            end: data[i].scheduleEnd || data[i].scheduleStart, // 종료시간이 같으면 시작시간을 사용
                            backgroundColor: data[i].scheduleColor,
                            description: data[i].scheduleContent,
                            allDay: isAllDay
                        });
                    }
                }

                // 일정 등록 후 공휴일 데이터가 사라지지 않도록, 기존 공휴일 데이터 추가
                if (isHolidayDataLoaded) {
                    calendar.getEventSourceById('holidayList').remove();
                    // 공휴일 이벤트를 다시 캘린더에 추가
                    calendar.addEventSource({
                        id: 'holidayList',
                        events: holidayEvents
                    });
                } else {
                    // 공휴일 데이터를 처음 로드할 때
                    holidayCurrentMonth();
                }

                window.holidayDataLoaded = false;
                fetchAllData();
                // holidayCurrentMonth();
                $("#scheduleModal").modal("hide");

                // 부서 일정 체크
                if (isDeptSchedule) {
                    fetchDeptListData();
                }
            },
            error: function () {
                console.log("일정 등록 오류");
            }
        });
    }

    // 일정 수정
    function updateEvent(eventData) {
        const startAt = moment($("#startAt").val()).format('YYYY-MM-DD HH:mm');
        const endAt = moment($("#endAt").val() || $("#startAt").val()).format('YYYY-MM-DD HH:mm');

        $.ajax({
            url: contextPath + "/schedule/scheduleUpdate",
            type: "post",
            dataType: "json",
            data: {
                empId: $("#emp_id").val(),
                scheduleId: eventData.schedule_id,
                scheduleName: eventData.schedule_name,
                scheduleStart: startAt,
                scheduleEnd: endAt,
                scheduleColor: eventData.bgColor,
                scheduleContent: eventData.description,
                scheduleAllDay: eventData.allDay ? 1 : 0
            },
            success: function (data) {
                window.holidayDataLoaded = false;
                fetchAllData();
                $("#scheduleModal").modal("hide");

                // 부서 일정 체크
                if (isDeptSchedule) {
                    fetchDeptListData();
                }
            },
            error: function (error) {
                console.log("일정 수정 오류", error);
            }
        });
    }

    // 일정 수정 - 드래그 이벤트 (시작/종료 날짜 수정)
    function updateDragEvent(info) {
        const startAt = moment(info.event.start).format('YYYY-MM-DD HH:mm');
        let endAt;

        if (info.event.allDay) {
            endAt = startAt;
        } else {
            endAt = info.event.end ? moment(info.event.end).format('YYYY-MM-DD HH:mm') : startAt;
        }

        $.ajax({
            url: contextPath + "/schedule/scheduleDragUpdate",
            type: "post",
            dataType: "json",
            data: {
                empId: $("#emp_id").val(),
                scheduleId: info.event.id,
                scheduleStart: startAt,
                scheduleEnd: endAt,
                scheduleAllDay: info.event.allDay ? 1 : 0
            },
            success: function () {
                window.holidayDataLoaded = false;

                // 일정 수정 후 캘린더 업데이트
                const event = calendar.getEventById(info.event.id);
                if (event) {
                    // 이벤트 속성 한 번에 갱신
                    event.setProp({
                        start: startAt,  // 시작 시간 업데이트
                        end: endAt,      // 종료 시간 업데이트
                        allDay: info.event.allDay // AllDay 설정 업데이트
                    });

                    calendar.render();
                }

                // 부서 일정 체크: isDeptSchedule이 true일 경우만 한 번 호출하도록 방지
                if (isDeptSchedule) {
                    if (!window.isDeptListLoading) {
                        window.isDeptListLoading = true;
                        fetchDeptListData();
                    }
                }
            },
            error: function () {
                alert("drag 일정 업데이트 중 오류가 발생했습니다.");
            }
        });
    }

    // 일정 삭제
    function deleteEvent(eventData) {
        if (confirm("정말 삭제하시겠습니까?")) {
            $.ajax({
                url: contextPath + "/schedule/scheduleDelete",
                type: "post",
                dataType: "json",
                data: {
                    scheduleId: eventData.schedule_id,
                },
                success: function (data) {
                    window.holidayDataLoaded = false;

                    const event = calendar.getEventById(eventData.schedule_id);
                    if (event) {
                        event.remove();
                    }

                    if (isDeptSchedule) {
                        fetchDeptListData();
                    }

                    fetchAllData();
                    holidayCurrentMonth();
                    initCalendar();

                    $("#scheduleModal").modal("hide");
                },
                error: function (error) {
                    alert("일정 삭제 중 오류가 발생했습니다.");
                }
            });
        }
    }

    // 팝업
    function openDetailModal(event) {
        $(".modal-body").find(".form-group.share-info").remove(); // 공유자 정보

        $(".modal-header").find("h5").text("일정 등록");
        $("form[name='scheduleEvent'] input, form[name='scheduleEvent'] select, form[name='scheduleEvent'] textarea").prop("disabled", false);
        $(".modal-body").find(".btn_wrap").html(`
	        <button type="reset" class="btn btn-secondary">취소</button>
	        <button type="button" id="btnRegister" class="btn btn-info">등록</button>
	    `);

        // 일정 등록
        if (!event) {
            $("#schedule_id").val("");
            $("#schedule_name").val("");
            $("#startAt, #endAt").prop("type", "datetime-local").val("");
            $("#description").val("");
            $("#bgColor").val("#1e3a8a");
            $("#allDay").prop("checked", false);
            return;
        }

        // 상세 일정
        $("#schedule_id").val(event.id);
        $("#schedule_name").val(event.title);

        const startDate = moment(event.start).format("YYYY-MM-DD");
        const startDateTime = moment(event.start).format("YYYY-MM-DDTHH:mm");
        const endDateTime = moment(event.end || event.start).format("YYYY-MM-DDTHH:mm");
        // allDay 체크 여부
        if (event.allDay) {
            $("#allDay").prop("checked", true);
            $("#startAt, #endAt").prop("type", "date");
            $("#startAt").val(startDate);
            $("#endAt").val(startDate);
        } else {
            $("#allDay").prop("checked", false);
            $("#startAt, #endAt").prop("type", "datetime-local");
            $("#startAt").val(startDateTime);
            $("#endAt").val(endDateTime);
        }

        $("#bgColor").val(event.backgroundColor);
        const description = event.extendedProps && event.extendedProps.description ? event.extendedProps.description : '';
        $("#description").val(description);

        // 공유 일정
        if (event.extendedProps.isShared === true) {
            $(".modal-header").find("h5").text("공유 일정");

            const shareInfoHtml = `
                <div class="form-group share-info">
                    <p style="margin-bottom: 5px;">공유자 정보</p>
                    <ul>
                        <li><span>등록자:</span> ${event.extendedProps.empIdName}</li>
                        <li><span>참여자:</span> ${event.extendedProps.shareEmpNames}</li>
                    </ul>
                </div>
            `;
            $(".modal-body").find(".form-group:nth-of-type(1)").after(shareInfoHtml);

            // 등록자/참여자 구별
            if (event.extendedProps.empId === $("#emp_id").val()) {  // 등록자일 경우
                $(".modal-body").find(".btn_wrap").html(`
                    <button type="button" id="btnUpdate" class="btn btn-primary">수정</button>
                    <button type="button" id="btnDelete" class="btn btn-danger">삭제</button>
                `);

            } else { // 참여자일 경우
                $("form[name='scheduleEvent'] input, form[name='scheduleEvent'] select, form[name='scheduleEvent'] textarea").prop("disabled", true);
                $(".modal-body").find(".btn_wrap").html(``);
            }

            $(".modal-body").find(".form-group.color-group").show();
            $(".modal-body").find(".form-group.share-info").show();

        } else if(event.extendedProps.isDept === true) { // 부서 일정
            $(".modal-header").find("h5").text("부서 일정");
            $(".modal-body").find(".form-group.color-group").hide();
            $(".modal-body").find(".btn_wrap").html(`
	            <button type="button" id="btnUpdate" class="btn btn-primary">수정</button>
	            <button type="button" id="btnDelete" class="btn btn-danger">삭제</button>
	        `);
        } else {
            $(".modal-header").find("h5").text("상세 일정");
            $("form[name='scheduleEvent'] input, form[name='scheduleEvent'] select, form[name='scheduleEvent'] textarea").prop("disabled", false);
            $(".modal-body").find(".btn_wrap").html(`
	            <button type="reset" class="btn btn-secondary">취소</button>
	            <button type="button" id="btnUpdate" class="btn btn-primary">수정</button>
	            <button type="button" id="btnDelete" class="btn btn-danger">삭제</button>
	        `);
            $(".modal-body").find(".form-group.color-group").show();
        }

        // 일정 수정
        $("#btnUpdate").off("click").on("click", function () {
            const eventData = {
                schedule_id: $("#schedule_id").val(),
                schedule_name: $("#schedule_name").val(),
                startAt: moment($("#startAt").val()).format('YYYY-MM-DD HH:mm'),
                endAt: moment($("#endAt").val()).format('YYYY-MM-DD HH:mm'),
                bgColor: $("#bgColor").val(),
                description: $("#description").val(),
                allDay: $("#allDay").prop("checked")
            };
            updateEvent(eventData);
        });

        // 일정 삭제
        $("#btnDelete").off("click").on("click", function () {
            const eventData = {
                schedule_id: $("#schedule_id").val()
            };
            deleteEvent(eventData);
        });

        // 모달 열기
        $("#scheduleModal").modal('show');
    }

    $(".btn.btn-lg[data-bs-toggle='modal']").on("click", function () {
        initializeModalForRegistration();
    });

    // 등록 버튼 클릭 시 모달 강제 초기화
    function initializeModalForRegistration() {
        $("#schedule_id").val("");
        $("#schedule_name").val("");
        $("#startAt").val("");
        $("#endAt").val("");
        $("#description").val("");
        $("#bgColor").val("#1e3a8a");
        $("#allDay").prop("checked", false);

        $("form[name='scheduleEvent'] input, form[name='scheduleEvent'] select, form[name='scheduleEvent'] textarea").prop("disabled", false);

        $(".modal-header").find("h5").text("일정 등록");
        $(".modal-body").find(".btn_wrap").html(`
	        <button type="reset" class="btn btn-secondary">취소</button>
	        <button type="submit" class="btn btn-info" id="btnRegister">등록</button>
	    `);

        $(".modal-body").find(".form-group.share-info").remove();
        $(".modal-body").find(".form-group.color-group").show();

        // 등록 버튼에 이벤트 바인딩
        $("#btnRegister").off("click").on("click", function (e) {
            e.preventDefault();

            const eventData = {
                schedule_name: $("#schedule_name").val(),
                startAt: $("#startAt").val(),
                endAt: $("#endAt").val(),
                bgColor: $("#bgColor").val(),
                description: $("#description").val(),
                allDay: $("#allDay").prop("checked"),
            };

            if (validateForm()) {
                addEvent(eventData);
            }
        });
    }

    // 모달 닫을 때 초기화
    $("#scheduleModal").on("hidden.bs.modal", function () {
        // 모달을 닫을 때 폼을 초기화
        $("form[name='scheduleEvent']").each(function () {
            this.reset();
        });

        $("#startAt, #endAt").prop("type", "datetime-local");
        $(".modal-body").find(".form-group.share-info").hide();
    });

    // form 유효성 검사
    function validateForm() {
        const $scheduleName = $("#schedule_name");
        const $start = $("#startAt");
        const $end = $("#endAt");

        if ($scheduleName.val().trim() === "") {
            alert("일정명을 입력하세요");
            $scheduleName.focus();
            return false;
        }

        if ($start.val().trim() === "") {
            alert("시작 날짜/시간을 선택하세요");
            $start.focus();
            return false;
        }

        if (new Date($start.val()) > new Date($end.val())) {
            alert("끝나는 날짜/시간이 시작 날짜/시간보다 이전입니다. 다시 확인해 주세요.");
            return false;
        }

        if ($end.val().trim() === "") {
            alert("종료 날짜/시간을 선택하세요");
            $end.focus();
            return false;
        }
        return true;
    }

    // 종일 체크박스 상태 변경 시
    $("#allDay").on("change", function () {
        isAllDayChk = $(this).prop("checked");
        startDate = $("#startAt").val();

        if (isAllDayChk) {
            $("#startAt, #endAt").prop("type", "date");
        } else {
            $("#startAt, #endAt").prop("type", "datetime-local");
        }

        if (isAllDayChk && startDate) {
            $("#endAt").val(startDate);
        }
    });

    // 시작 날짜 변경 시
    $("#startAt").on("change", function () {
        startDate = $(this).val();
        isAllDayChk = $("#allDay").prop("checked");

        if (isAllDayChk && startDate) {
            $("#endAt").val(startDate);
        }
    });

    // 종료 날짜 변경 시
    $("#endAt").on("change", function () {
        startDate = $("#startAt").val();
        endDate = $(this).val();
        isAllDayChk = $("#allDay").prop("checked");

        if (isAllDayChk) {
            if (startDate !== endDate) {
                alert("시작 날짜와 종료 날짜는 같아야 합니다.");
                $(this).focus();
                $("#btnRegister").prop("disabled", true);
            } else {
                $("#btnRegister").prop("disabled", false);
            }
        }
    });

    function loadCalendarEvents(direction) {
        const currentStart = moment(calendar.getDate()).startOf('month');  // 현재 월의 시작
        const currentEnd = moment(calendar.getDate()).endOf('month');  // 현재 월의 끝

        let startDate, endDate;

        if (direction === 'prev' || direction === 'next') {
            startDate = moment(currentStart)
                .subtract(1, 'month')
                .endOf('month')
                .subtract(7, 'days')
                .format('YYYY-MM-DD');

            endDate = moment(currentEnd)
                .add(1, 'month')
                .startOf('month')
                .add(7, 'days')
                .format('YYYY-MM-DD');
        } else {
            startDate = moment(currentStart).subtract(7, 'days').format('YYYY-MM-DD');
            endDate = moment(currentEnd).add(7, 'days').format('YYYY-MM-DD');
        }

        // 모든 이벤트 소스 제거
        let sources = calendar.getEventSources();
        sources.forEach(source => source.remove());

        // 일정 데이터를 가져와서 업데이트
        fetchListData(startDate, endDate);
    }

    // 캘린더 생성
    function initCalendar() {
        const calendarEl = document.getElementById('calendar');
        if (!calendarEl || $('#calendar').length === 0) return;

        calendar = new FullCalendar.Calendar(calendarEl, {
            expandRows: true, // 화면에 맞게 높이 재설정
            slotMinTime: '08:00', // Day 캘린더에서 시작 시간
            slotMaxTime: '22:00', // Day 캘린더에서 종료 시간
            defaultAllDay: true, // 종일 이벤트
            timeZone: 'local',
            headerToolbar: {
                left: 'customPrev,customNext',
                center: 'title',
                right: 'today'
            },
            customButtons: {
                customPrev: {
                    text: '<',
                    click: function() {
                        calendar.prev();

                        // 캘린더에서 갱신된 날짜를 가져와서, 해당 날짜 기준으로 다음 달의 시작일과 종료일을 계산
                        const startMonth = moment(calendar.getDate()).startOf('year').format('YYYY');
                        const endMonth = moment(calendar.getDate()).endOf('month').format('MM');
                        isHolidayDataLoaded = false;
                        fetchHolidayData(startMonth, endMonth);

                        loadCalendarEvents('prev');
                    }
                },
                customNext: {
                    text: '>',
                    click: function() {
                        calendar.next();


                        // 캘린더에서 갱신된 날짜를 가져와서, 해당 날짜 기준으로 다음 달의 시작일과 종료일을 계산
                        const startMonth = moment(calendar.getDate()).startOf('year').format('YYYY');
                        const endMonth = moment(calendar.getDate()).endOf('month').format('MM');
                        isHolidayDataLoaded = false;
                        fetchHolidayData(startMonth, endMonth);

                        loadCalendarEvents('next');
                    }
                }
            },
            buttonText: {
                today: '오늘'
            },
            initialView: 'dayGridMonth', // 초기 로드 될때 보이는 캘린더 화면(기본 설정: 달)
            editable: true, // 수정 가능
            selectable: false, // 달력 일자 드래그 설정
            nowIndicator: true, // 현재 시간 마크
            dayMaxEvents: true, // 이벤트가 오버되면 높이 제한 (+ 몇 개식으로 표현)
            locale: 'ko', // 한국어 설정
            events: events, // 전역 이벤트 배열 사용
            fixedWeekCount: false, // 이전달, 다음달의 날짜 한 주 설정
            dateClick: function (info) {
                // 현재 시간과 클릭된 날짜
                let currentDate = moment(); // 현재 날짜와 시간
                let clickedDate = moment(info.dateStr); // 클릭한 날짜
                let startDate = clickedDate.set({
                    hour: currentDate.hour(),
                    minute: currentDate.minute()
                });

                $(".modal-header").find("h5").text("일정 등록");
                $(".modal-body").find(".btn_wrap").html(`
		            <button type="reset" class="btn btn-secondary">취소</button>
		            <button type="submit" class="btn btn-info" id="btnRegister">등록</button>
		        `);

                $("#schedule_name").val("");
                $("#startAt").prop("type", "datetime-local").val(startDate.format("YYYY-MM-DD HH:mm"));
                // $("#endAt").prop("type", "datetime-local").val("");
                $("#description").val("");
                $("#bgColor").val("#1e3a8a");
                $("#allDay").prop("checked", false);
                $("form[name='scheduleEvent'] input, form[name='scheduleEvent'] select, form[name='scheduleEvent'] textarea").prop("disabled", false);
                $(".modal-body").find(".form-group.color-group").show();

                $("#scheduleModal").modal("show");

                // 등록 버튼에 이벤트 바인딩
                $("#btnRegister").off("click").on("click", function (e) {
                    e.preventDefault();
                    const eventData = {
                        schedule_name: $("#schedule_name").val(),
                        startAt: $("#startAt").val(),
                        endAt: $("#endAt").val(),
                        bgColor: $("#bgColor").val(),
                        description: $("#description").val(),
                        allDay: $("#allDay").prop("checked")
                    };
                    if (validateForm()) {
                        addEvent(eventData);
                    }
                });
            },
            eventClick: function (info) {
                if (!info.event.extendedProps.isHoliday) {
                    openDetailModal(info.event);
                }
            },
            eventChange: function (info) {
                updateDragEvent(info);
            },
            eventDataTransform: function (event) { // 이벤트 데이터 변환 함수
                // allDay 이벤트일 경우 종료 날짜를 하루 더 추가
                if (event.allDay) {
                    event.end = moment(event.end).add(1, 'days'); // 종료 날짜를 하루 더함
                }
                return event; // 수정된 이벤트 반환
            },
            eventRender: function(info) {
                info.el.style.transition = 'none';
            }
        });

        //선택 상태 해제
        calendar.unselect();
        calendar.render();
    }
});