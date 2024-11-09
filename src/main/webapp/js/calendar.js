$(document).ready(function(){
	let calendar;
	const events = []; 
	
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
			                allDay: data[i].schedule_allDay === 0
			            });
			        }
			    }
     	        
     	        // 캘린더 초기화
			    initCalendar();
	        },
	        error: function(error) {
	            console.log('이벤트 데이터를 불러오는 중 오류 발생:', error);
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
						if(isAllDay) {
							events.push({
				                title: data[i].schedule_name, 
				                start: data[i].schedule_start, 
				                end: data[i].schedule_end,
				                //end: moment(data[i].schedule_end).add(1, 'days').format('YYYY-MM-DD'), // 종료일 하루 더 추가
				                backgroundColor: data[i].schedule_color, 
				                description: data[i].schedule_content,
				                allDay: isAllDay
				            });
						} else {
							events.push({
				                title: data[i].schedule_name, 
				                start: data[i].schedule_start, 
				                end: data[i].schedule_end,
				                //end: moment(data[i].schedule_end).add(1, 'days').format('YYYY-MM-DD'), // 종료일 하루 더 추가
				                backgroundColor: data[i].schedule_color, 
				                description: data[i].schedule_content,
				                allDay: isAllDay
				            });
						}
						 
			            
			        }
			    }

                $("#scheduleModal").modal("hide"); 
            },
            error: function () {
                console.log("일정 추가 오류");
            }
        });
    }
    
    //form 유효성 검사
    function validateForm() {
		const $scheduleName = $("#schedule_name");
		const $start = $("#startAt"); 
		const $end = $("#endAt");
		const $allDay = $("#allDay");
		const $description = $("#description");

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
     

		/*
        if ($description.val().trim() === "") {
            alert("내용을 입력하세요");
            $description.focus();
            return false;
        }
        */

        return true;
    }
    
    // 등록 버튼 클릭 시 일정 등록
    $("#btnRegister").on("click", function () {
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
    
    // 종일 체크박스 상태 변경 시 
    $("#allDay").on("change", function() {
        const isAllDayChecked = $(this).prop("checked");
        
        if (isAllDayChecked) {
            $("#startAt, #endAt").prop("type", "date");
        } else {
            $("#startAt, #endAt").prop("type", "datetime-local");
        }
    });
    
    // 종일 체크시 시작날짜/종료날짜 확인
    $("#endAt").on("change", function() {
        const startDate = $("#startAt").val();
        const endDate = $("#endAt").val();
        const isAllDayChecked = $("#allDay").prop("checked");

        if (isAllDayChecked && startDate !== endDate) {
            alert("종일 이벤트의 시작 날짜와 종료 날짜는 같아야 합니다.");
            $("#endAt").focus();
            $("#btnRegister").prop("disabled", true); 
        } else {
            $("#btnRegister").prop("disabled", false); 
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
			
			// 이벤트 데이터 변환 함수
            eventDataTransform: function(event) {
                // allDay 이벤트일 경우 종료 날짜를 하루 더 추가
                if (event.allDay) {
                    event.end = moment(event.end).add(1, 'days'); // 종료 날짜를 하루 더함
                }
                return event; // 수정된 이벤트 반환
            }
		});
		
		//선택 상태 해제
        calendar.unselect();
        
        calendar.render();
	}
	
	fetchListData();
});