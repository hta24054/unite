$(document).ready(function () {
    let calendar;
    let events = [];
    let isAllDayChk, startDate, endDate;

    const $resourceTypeCategory = $("#resourceTypeCategory");
    const $resourceNameCategory = $("#resourceNameCategory");
    const $resourceType = $("#resourceType");
    const $resourceName = $("#resourceName");

    initCalendar();
    getResourceList();
    getReservationList();

    // 자원 목록 불러오기
    function getResourceList() {
        $.ajax({
            url: "getResourceList",
            type: "get",
            dataType: "json",
            success: function (data) {
                $resourceType.empty();
                $resourceType.append('<option value="">분류명</option>');
                $resourceName.hide();

                $resourceTypeCategory.empty();
                $resourceTypeCategory.append('<option value="">분류명</option>');
                $resourceNameCategory.hide();

                // 중복 제거 Set
                const uniqueResourceType = new Set();
                data.forEach(function (resource) {
                    if (!uniqueResourceType.has(resource.resourceType)) {
                        uniqueResourceType.add(resource.resourceType);
                        $resourceType.append('<option value="' + resource.resourceType + '">' + resource.resourceType + '</option>');
                        $resourceTypeCategory.append('<option value="' + resource.resourceType + '">' + resource.resourceType + '</option>');
                    }
                });
            },
            error: function () {
                alert("자원 목록 불러오기 실패");
            }
        });
    }

    // 자원 선택 시 해당 자원명 불러오기
    $resourceTypeCategory.on("change", function () {
        const $selectedVal = $(this).val();
        $.ajax({
            url: "resourceSelectChange",
            type: "get",
            dataType: "json",
            data: {
                resourceType: $selectedVal,
            },
            success: function (data) {
                $resourceNameCategory.empty();
                $resourceNameCategory.append('<option value="">자원명</option>');

                // resource_id를 value로 설정
                data.forEach(function (resource) {
                    $resourceNameCategory.append('<option value="' + resource.resourceId + '">' + resource.resourceName + '</option>');
                });

                $resourceNameCategory.show();
            },
            error: function () {
                alert("자원명 불러오기 실패");
            }
        });
    });

    // 자원 선택 시 해당 자원에 대한 예약 목록 불러오기
    $resourceNameCategory.on("change", function () {
        const selectedResourceId = $(this).val(); // 선택된 자원 ID
        const selectedResourceName = $("#resourceNameCategory option:selected").text(); // 선택된 자원 이름

        if (selectedResourceId && selectedResourceName) {
            getReservationList(selectedResourceId, selectedResourceName); // 자원 ID와 자원 이름에 맞는 예약 목록을 불러오기
        } else {
            getReservationList(); // 자원 ID나 이름이 선택되지 않으면 모든 예약 목록을 불러오기
        }
    });

    // 자원 선택 시 해당 자원명 불러오기
    $resourceType.on("change", function () {
        const $selectedVal = $(this).val();
        $.ajax({
            url: "resourceSelectChange",
            type: "get",
            dataType: "json",
            data: {
                resourceType: $selectedVal,
            },
            success: function (data) {
                $resourceName.empty();
                $resourceName.append('<option value="">자원명</option>');

                // resource_id를 value로 설정
                data.forEach(function (resource) {
                    $resourceName.append('<option value="' + resource.resourceId + '">' + resource.resourceName + '</option>');
                });

                $resourceName.show();
            },
            error: function () {
                alert("자원명 불러오기 실패");
            }
        });
    });

    // 자원 예약 목록 불러오기
    function getReservationList(resourceId = null, resourceName = null) {
        $.ajax({
            url: "getReservationList",
            type: "get",
            dataType: "json",
            data: {
                resourceId: resourceId,   // 자원 ID
                resourceName: resourceName, // 자원 이름
                empId: $("#emp_id").val(), // 로그인한 사용자의 empId
            },
            success: function (data) {
                events = [];
                if (data != null && data.length > 0) {
                    for (let i = 0; i < data.length; i++) {
                        // 본인 예약 여부 확인
                        let isReservation = data[i].isMyReservation;

                        // 예약 색상 : 본인 예약은 '#29aeff', 다른 예약은 '#0e4377'
                        let reservationColor = isReservation ? '#29aeff' : '#0e4377';

                        events.push({
                            id: data[i].reservationId,
                            allDay: data[i].reservationAllDay,
                            start: data[i].reservationStart,
                            end: data[i].reservationEnd,
                            backgroundColor: reservationColor, // 색상 적용
                            extendedProps: {
                                reservationInfo: data[i].reservationInfo,
                                resourceId: data[i].resourceId,
                                empId: data[i].empId,
                                isMyReservation: isReservation
                            }
                        });
                    }
                }

                initCalendar(events);
            },
            error: function () {
                alert("자원 예약 목록 불러오기 실패");
            }
        });
    }

    // 자원 예약 하기
    function resourceReservation(eventData) {
        $.ajax({
            url: "resourceReservation",
            type: "post",
            dataType: "json",
            headers: {
                'Content-Type': 'application/json',
            },
            data: JSON.stringify({
                empId: $("#emp_id").val(),
                reservationAllDay: eventData.allDay ? 1 : 0, // reservationAllDay로 변경
                reservationStart: moment(eventData.startAt).format('YYYY-MM-DD HH:mm'), // reservationStart로 변경
                reservationEnd: moment(eventData.endAt).format('YYYY-MM-DD HH:mm'), // reservationEnd로 변경
                resourceId: $("#resourceName").val(),
                resourceName: $("#resourceName option:selected").text(),
                reservationInfo: $("#reservationInfo").val()
            }),
            success: function (data) {
                if (data === 0) {
                    alert('이미 예약된 자원입니다.');
                    return;  // 이미 예약된 자원이면 예약을 진행하지 않음
                }

                // 자원 등록 후, 해당 자원의 예약 목록을 불러옴
                const selectedResourceId = $("#resourceName").val(); // 선택된 자원 ID
                const selectedResourceName = $("#resourceName option:selected").text(); // 선택된 자원 이름

                // 등록한 자원의 예약 목록을 불러옴
                getReservationList(selectedResourceId, selectedResourceName);

                $("#reservationModal").modal("hide");
            },
            error: function () {
                alert("자원 예약 오류");
            }
        });
    }

    // 자원예약 상세정보 팝업
    function openInfoModal(event) {
        $.ajax({
            url: "getReservationModal",
            type: "get",
            dataType: "json",
            data: {
                reservationId: event.id,
                empId: $("#emp_id").val(),
            },
            success: function (data) {
                if (data) {
                    $("#reservationDetailModal").find(".modal-header").find("h5").text("예약 정보");

                    let _html = "<ul class='deatail_list'>" +
                        "<li>분류명: " + data.resourceType + "</li>" +
                        "<li>자원명: " + data.resourceName + "</li>" +
                        "<li>시작 일시: " + moment(event.start).format("YYYY-MM-DD HH:mm") + "</li>";

                    // 종료 일시 설정 (시작 일시와 동일한 경우)
                    _html += "<li>종료 일시: " +
                                moment(event.end && !moment(event.start).isSame(event.end) ? event.end : event.start).format("YYYY-MM-DD HH:mm") +
                             "</li>";

                    _html += "<li>예약자: " + data.ename + "</li>" +
                             "<li>사용용도: " + data.reservationInfo || "" + "</li>";

                    // 자원정보 존재할 경우
                    if (data.resourceInfo) {
                        _html += "<li>자원정보: " + data.resourceInfo + "</li>";
                    }

                    _html += "</ul>" +
                        "<div class='d-flex justify-content-center mt-3'>" +
                        "<button type='button' id='btnCancel' class='btn btn-danger'>예약취소</button>" +
                        "</div>";

                    $("#reservationDetailModal").find(".modal-body").html(_html);
                    $("#reservationDetailModal").modal("show");

                    // 예약 취소
                    $("#btnCancel").off("click").on("click", function () {
                        cancelReservation(event);
                    });

                } else {
                    alert("예약 정보를 찾을 수 없습니다.");
                }
            },
            error: function () {
                alert("예약 정보 팝업 불러오기 실패");
            }
        });
    }

    // 예약 취소
    function cancelReservation(event) {
        const logInEmpId = $("#emp_id").val(); // 현재 로그인한 emp_id

        // 예약 정보의 emp_id와 로그인 사용자 emp_id가 일치하는지 확인
        if (String(event.extendedProps.empId) !== String(logInEmpId)) {
            alert("예약자만 취소할 수 있습니다.");
            return;
        }

        if (confirm("정말 취소하시겠습니까?")) {
            $.ajax({
                url: "cancelReservation",
                type: "post",
                dataType: "json",
                data: {
                    reservationId: event.id,  // 예약 ID
                    emp_id: $("#emp_id").val(), //예약자 emp_id
                },
                success: function (data) {
                    if (data === 1) {
                        alert("예약이 취소되었습니다.");
                        getReservationList();
                        $("#reservationDetailModal").modal("hide");
                    } else {
                        alert("예약 취소 실패");
                    }
                },
                error: function (error) {
                    alert("예약 취소 오류");
                }
            });
        }
    }

    // 예약 하기 모달 등록 버튼 클릭 시 초기화
    $(".btn.btn-info[data-target='#reservationModal']").on("click", function () {
        $(".modal-header").find("h5").text("예약 하기");
        $(".modal-body").find(".btn_wrap").html(`
	        <button type="reset" class="btn btn-secondary">취소</button>
	        <button type="submit" class="btn btn-info" id="btnRegister">등록</button>
	    `);

        $("#allDay").prop("checked", false);
        $("#startAt").prop("type", "datetime-local").val("");
        $("#endAt").val("");
        $resourceType.val("");
        $resourceName.hide().empty().append('<option value="">자원명</option>');
        $("#reservationInfo").val("");

        // 등록 버튼에 이벤트 바인딩
        $("#btnRegister").off("click").on("click", function (e) {
            e.preventDefault();
            const eventData = {
                allDay: $("#allDay").prop("checked"),
                startAt: $("#startAt").val(),
                endAt: $("#endAt").val(),
                resourceName: $resourceName.val(),
                reservationInfo: $("#reservationInfo").val(),
                empId: $("#emp_id").val()
            };

            if (validateForm()) {
                resourceReservation(eventData);
            }
        });
    });


    $(".close").on("click", function () {
        $("form").each(function () {
            this.reset();
        });

        $("#charCount").text("0/100");
    });

    $("#reservationModal").on("hidden.bs.modal", function () {
        $(this).find("form").each(function () {
            this.reset();
        });
    });

    // form 유효성 검사
    function validateForm() {
        const $start = $("#startAt");
        const $end = $("#endAt");
        const $resourceType = $("#resourceType");
        const $resourceName = $("#resourceName");

        if ($start.val().trim() === "") {
            alert("시작 일시를 선택하세요");
            $start.focus();
            return false;
        }

        if ($end.val().trim() === "") {
            alert("종료 일시를 선택하세요");
            $end.focus();
            return false;
        }

        if (new Date($start.val()) > new Date($end.val())) {
            alert("종료 일시가 시작 시작 일시보다 이전입니다. 다시 확인해 주세요.");
            $start.focus();
            return false;
        }

        // if (!$("#allDay").prop("checked") && $start.val() === $end.val()) {
        //     alert("시작 일시와 종료 일시는 동일할 수 없습니다. 다시 확인해 주세요.");
        //     $end.focus();
        //     return false;
        // }

        if ($resourceType.val().trim() === "") {
            alert("분류명을 선택하세요");
            $resourceType.focus();
            return false;
        }

        if ($resourceName.val().trim() === "") {
            alert("자원명을 선택하세요");
            $resourceName.focus();
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

    // 사용용도 입력 시
    $("#reservationInfo").on("input", function () {
        const count = $(this).val().length;
        const max = parseInt($(this).attr("maxlength"), 10);

        // 문자 수 업데이트
        $("#charCount").text(`${count}/${max}`);

        // 100자 초과 시 
        if (count > max) {
            alert("입력은 최대 100자까지만 가능합니다");
            $(this).val($(this).val().substring(0, max));
            $("#charCount").text(`${max}/${max}`);
        }
    });

    // 캘린더 생성
    function initCalendar() {
        const calendarEl = document.getElementById('calendar');
        if (!calendarEl) return;

        calendar = new FullCalendar.Calendar(calendarEl, {
            expandRows: true, // 화면에 맞게 높이 재설정
            slotMinTime: '08:00', // Day 캘린더에서 시작 시간
            slotMaxTime: '24:00', // Day 캘린더에서 종료 시간
            slotLabelFormat: 'HH:mm',
            defaultAllDay: true, // 종일 이벤트
            timeZone: 'local',
            headerToolbar: {
                left: 'prev,next',
                center: 'title',
                right: 'today'
            },
            buttonText: {
                today: '오늘'
            },
            initialView: 'timeGridWeek', // 초기 로드 될때 보이는 캘린더 화면
            editable: false, // 수정 불가능
            selectable: false, // 선택 불가능
            nowIndicator: true, // 현재 시간 마크
            dayMaxEvents: true, // 이벤트가 오버되면 높이 제한 (+ 몇 개식으로 표현)
            locale: 'ko', // 한국어 설정
            events: events, // 전역 이벤트 배열 사용
            dateClick: function (info) {
                // 현재 시간과 클릭된 날짜
                let currentDate = moment(); // 현재 날짜와 시간
                let clickedDate = moment(info.dateStr); // 클릭한 날짜
                let startDate = clickedDate.set({
                    hour: currentDate.hour(),
                    minute: currentDate.minute()
                });

                if (!info.event) { // 빈 셀 클릭 시
                    $(".modal-header").find("h5").text("예약 하기");
                    $(".modal-body").find(".btn_wrap").html(`
				        <button type="reset" class="btn btn-secondary">취소</button>
				        <button type="submit" class="btn btn-info" id="btnRegister">등록</button>
				    `);

                    $("#allDay").prop("checked", false);
                    $("#startAt").prop("type", "datetime-local").val(startDate.format("YYYY-MM-DD HH:mm"));
                    $("#endAt").prop("type", "datetime-local").val("");
                    $("#description").val("");
                    $resourceType.val("");
                    $resourceName.hide().empty().append('<option value="">자원명</option>');
                    $("#reservationInfo").val("");
                    $("#charCount").text("0/100");

                    $("#reservationModal").modal("show");

                    // 등록 버튼에 이벤트 바인딩
                    $("#btnRegister").off("click").on("click", function (e) {
                        e.preventDefault();
                        const eventData = {
                            allDay: $("#allDay").prop("checked"),
                            startAt: $("#startAt").val(),
                            endAt: $("#endAt").val(),
                            resourceName: $resourceName.val(),
                            reservationInfo: $("#reservationInfo").val(),
                            empId: $("#emp_id").val()
                        };

                        if (validateForm()) {
                            resourceReservation(eventData);
                        }
                    });
                }
            },
            eventClick: function (info) {
                openInfoModal(info.event);
            },
        });

        //선택 상태 해제
        calendar.unselect();
        calendar.render();
    }
});