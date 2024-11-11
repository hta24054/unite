$(document).ready(function(){
	let calendar;
	const events = []; 
	let isAllDayChk, startDate, endDate;
	let currentDate;
	
	// 일정 리스트 불러오기
	function fetchListData(){
		$.ajax({
	        url: "ScheduleListAction",
	        type: "get",
	        dataType: "json",
	        data: {
				emp_id: $("#emp_id").val(),
			},
	        success: function(data) {
	            console.log("success data", data);
	            //callback(data); // 데이터가 성공적으로 로드된 후 콜백 함수 호출
	            
	            if (data != null) {
			        for (let i = 0; i < data.length; i++) {
						
			            events.push({
			                title: data[i].schedule_name, 
			                start: data[i].schedule_start, 
			                end: data[i].schedule_end, 
			                backgroundColor: data[i].schedule_color, 
			                description: data[i].schedule_content,
			                allDay: data[i].schedule_allDay 
			            });
			        }
			    }
     	        // 캘린더 초기화
			    initCalendar();
	        },
	        error: function(error) {
	            console.log('일정 리스트 불러오기 오류', error);
	        }
	    });
	}
	
	// 일정 등록
    function addEvent(eventData) {
        $.ajax({
            url: "ScheduleAddProcessAction", 
            type: "post",
            dataType: "json",
            data: {
                emp_id: $("#emp_id").val(),
                schedule_name: eventData.schedule_name,
                startAt: moment(eventData.startAt).format('YYYY-MM-DD HH:mm'),
                endAt: moment(eventData.endAt).format('YYYY-MM-DD HH:mm'), 
                bgColor: eventData.bgColor,
                description: eventData.description,
                allDay: eventData.allDay ? 1 : 0 // true이면 1, false이면 0
            },
            success: function (data) {
                console.log("일정 추가 성공", data);

                if (data != null) {
			        for (let i = 0; i < data.length; i++) {
						//const isAllDay = data[i].schedule_allDay === "true"
						const isAllDay = data[i].schedule_allDay === 1;
						events.push({
			                title: data[i].schedule_name, 
			                start: data[i].schedule_start, 
			                end: data[i].schedule_end,
			                backgroundColor: data[i].schedule_color, 
			                description: data[i].schedule_content,
			                allDay: isAllDay
			            });
			        }
			    }

                $("#scheduleModal").modal("hide"); 
            },
            error: function() {
                console.log("일정 등록 오류");
            }
        });
    }
    
    // 상세 일정 팝업
    function openDetailModal(date) {
        currentDate = date; // 클릭한 날짜 저장
        const scheduleForDate = events.filter(event => moment(event.start).isSame(date, 'day'));

	    if (scheduleForDate.length > 0) { // 일정 있을 경우
	        const event = scheduleForDate[0];
	
	        $("#schedule_name").val(event.title);
	        $("#startAt").val(moment(event.start).format("YYYY-MM-DD HH:mm"));
	        $("#endAt").val(moment(event.end).format("YYYY-MM-DD HH:mm"));
	        $("#description").val(event.description);
	        $("#bgColor").val(event.backgroundColor);
	        
	        // allDay
	        if (event.allDay) {
	            $("#allDay").prop("checked", true);
	            $("#startAt, #endAt").prop("type", "date");
	        } else {
	            $("#allDay").prop("checked", false);
	            $("#startAt, #endAt").prop("type", "datetime-local");
	        }
	        
	        $(".modal-header").find("p").text("상세 일정");
	        $(".modal-body").find(".btn_wrap").html(`
	            <button type="reset" class="btn btn-secondary">취소</button>
	            <button type="button" id="btnUpdate" class="btn btn-primary">수정</button>
	            <button type="button" id="btnDelete" class="btn btn-danger">삭제</button>
	        `);
	        
	        $("#btnUpdate").off("click").on("click", function() {
	            console.log("data Update");
	        });
	        
	        $("#btnDelete").off("click").on("click", function() {
	            console.log("data Delete");
	        });
	    } else { 
	        $(".modal-header").find("p").text("일정 등록");
	        $(".modal-body").find(".btn_wrap").html(`
	            <button type="reset" class="btn btn-secondary">취소</button>
	            <button type="submit" class="btn btn-info" id="btnRegister">등록</button>
	        `);
	    }
	
	    $("#scheduleModal").modal('show');
    }
    
    // 일정 등록 버튼 클릭 시 모달 초기화
	$(".btn.btn-info[data-target='#scheduleModal']").on("click", function() {
	    $(".modal-header").find("p").text("일정 등록"); 
	    $(".modal-body").find(".btn_wrap").html(`
	        <button type="reset" class="btn btn-secondary">취소</button>
	        <button type="submit" class="btn btn-info" id="btnRegister">등록</button>
	    `);
	
	    $("#schedule_name").val("");
	    $("#startAt").val("");
	    $("#endAt").val("");
	    $("#description").val("");
	    $("#bgColor").val("#1e3a8a");
	    $("#allDay").prop("checked", false);
	
	    // 등록 버튼에 이벤트 바인딩
	    $("#btnRegister").off("click").on("click", function(e) {
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
    $("#allDay").on("change", function() {
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
    $("#startAt").on("change", function() {
        startDate = $(this).val();
        isAllDayChk = $("#allDay").prop("checked");

        if (isAllDayChk && startDate) {
            $("#endAt").val(startDate);
        }
    });
    
    // 종료 날짜 변경 시
    $("#endAt").on("change", function() {
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
    
    $('#bgColor').on("change", function () {
	    $(this).css('color', $(this).val());
	});

	// 캘린더 생성
	function initCalendar(){
		const calendarEl = document.getElementById('calendar'); 
		if (!calendarEl) return;
		
		calendar = new FullCalendar.Calendar(calendarEl, {
	        expandRows: true, // 화면에 맞게 높이 재설정
	        slotMinTime: '08:00', // Day 캘린더에서 시작 시간
	        slotMaxTime: '22:00', // Day 캘린더에서 종료 시간
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
	        initialView: 'dayGridMonth', // 초기 로드 될때 보이는 캘린더 화면(기본 설정: 달)
	        editable: true, // 수정 가능
	        selectable: true, // 달력 일자 드래그 설정가능
	        nowIndicator: true, // 현재 시간 마크
	        dayMaxEvents: true, // 이벤트가 오버되면 높이 제한 (+ 몇 개식으로 표현)
	        locale: 'ko', // 한국어 설정
			events: events, // 전역 이벤트 배열 사용
		    dateClick: function(info) {
                console.log("dateClick info", info);
                openDetailModal(moment(info.date)); // 클릭한 날짜로 팝업 열기
            },
            eventClick: function(info) {
                console.log("eventClick info", info);
                openDetailModal(moment(info.event.start)); // 일정 클릭 시 수정 팝업 열기
            },
			eventAdd: function(obj) { // 이벤트가 추가되면 발생하는 이벤트
	         	 console.log("eventAdd obj", obj);
	        },
	        
	        eventChange: function(obj) { // 이벤트가 수정되면 발생하는 이벤트
	         	console.log(obj);
	        },
	        
	        eventRemove: function(obj){ // 이벤트가 삭제되면 발생하는 이벤트
	         	console.log(obj);
	        },
	        select: function(arg) {
                console.log(arg);
            },
            eventDataTransform: function(event) { // 이벤트 데이터 변환 함수
                // allDay 이벤트일 경우 종료 날짜를 하루 더 추가
                if (event.allDay) {
                    event.end = moment(event.end).add(1, 'days'); // 종료 날짜를 하루 더함
                }
                return event; // 수정된 이벤트 반환
            },
		});
		
		//선택 상태 해제
        calendar.unselect();
        
        calendar.render();
	}
	
	fetchListData();
});