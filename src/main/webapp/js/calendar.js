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
				//data key 값은 request.getParameter("startAt")의 값!
				emp_id: $("#emp_id").val(),
				/*
				schedule_name: $scheduleName.val(),
				startAt: moment(info.startStr).format('YYYY-MM-DD HH:mm'),
				endAt: moment(info.endStr).format('YYYY-MM-DD HH:mm'),
				//allDay: $allDay.is(":checked"), // 체크박스인 경우 true/false
			    bgColor: $bgColor.val(),
			    description: $description.val()*/
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
			                description: data[i].schedule_content 
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
			events: events // 전역 이벤트 배열 사용
		});
		
		//선택 상태를 해제합니다.
        calendar.unselect();
		
		//화면에 보이도록 합니다.
        calendar.render();
	}
	
	// 데이터 로드 후 캘린더 초기화
	fetchListData();
});

/*
document.addEventListener('DOMContentLoaded', function () {
	
	  
	  const $scheduleModal = $("#scheduleModal");
	const $scheduleName = $("#schedule_name");
	const $start = $("#startAt"); 
	const $end = $("#endAt");
	const $allDay = $("#allDay");
	const $bgColor = $("#bgColor");
	const $description = $("#description");
	  

		//모달창 이벤트
		/*
		$("#btnRegister").on("click", function () {
			// 이벤트를 서버로 전송
            $.ajax({
                url: "ScheduleAddProcessAction", 
                type: "post",
                dataType: "json",
                data: {
					emp_id: $("#emp_id").val(), // 현재 로그인한 사용자 ID 전달
                    schedule_name: $scheduleName.val(),
                    startAt: moment($start.val(), 'YYYY-MM-DDTHH:mm').format('YYYY-MM-DD HH:mm'),
                    endAt: moment($end.val(), 'YYYY-MM-DDTHH:mm').format('YYYY-MM-DD HH:mm'),
                    //allDay: $allDay.is(":checked"), // 체크박스인 경우 true/false
                    bgColor: $bgColor.val(),
                    description: $description.val(),
                },
                success: function(data) {
		            console.log("ScheduleAddProcessAction:", data);
		            
		            if (data != null) {
				        for (let i = 0; i < data.length; i++) {
				            calendar.addEvent({
				                title: data[i].schedule_name, 
				                start: data[i].schedule_start, 
				                end: data[i].schedule_end, 
				                backgroundColor: data[i].schedule_color, 
				                description: data[i].schedule_content 
				            });
				        }
				    }
				    
		            $scheduleModal.modal("hide"); 
		           
		        },
                error: function() {
                    console.log('일정 추가 중 오류 발생');
                }
            });
		});*/

		//form 유효성 체크
		/*
		$("form[name=scheduleEvent]").on("submit", function(e){
			e.preventDefault();
			
			if($scheduleName.val().trim() == "") {
		  		alert("일정명을 입력하세요");
		  		$scheduleName.focus();
		  		return false;
	  		}
	  
		    if($start.val().trim() == "") {
				alert("시작날짜/시간을 선택하세요");
				$start.focus();
				return false;
		    }
		    
		    // 끝나는 날짜가 시작하는 날짜보다 이전인지 검증
			if (new Date(eventData.startAt) > new Date(eventData.endAt)) {
			    alert("끝나는 날짜/시간이 시작 날짜/시간보다 이전입니다. 다시 확인해 주세요.");
			    return false;
			}
	  
			if($end.val().trim() == "") {
				alert("종료날짜/시간을 선택하세요");
				$end.focus();
				return false;
			}
	  
			if($description.val().trim() == "") {
				 alert("내용을 입력하세요");
				 $description.focus();
				 return false;
			}
	
			// 이벤트를 서버로 전송
	        $.ajax({
		        url: "ScheduleAddProcessAction",
		        type: "post",
		        dataType: "json",
		        data: {
					emp_id : $("#emp_id").val(), 
					title: $scheduleName.val(),
					startAt: moment($start.val(), 'YYYY-MM-DDTHH:mm'),
					endAt: moment($end.val(), 'YYYY-MM-DDTHH:mm'),
					//allDay: $allDay.is(":checked"), // 체크박스인 경우 true/false
				    backgroundColor: $bgColor.val(),
				    description: $description.val()
				},
		        success: function(data) {
		            console.log("일정 데이터 추가", data);
		            
		             if (data != null) {
				        for (let i = 0; i < data.length; i++) {
				            calendar.addEvent({
				                title: data[i].schedule_name, 
				                start: data[i].schedule_start, 
				                end: data[i].schedule_end, 
				                backgroundColor: data[i].schedule_color, 
				                description: data[i].schedule_content 
				            });
				        }
				    }
		            
		            //calendar.addEvent(data);
		        },
		        error: function(error) {
		            console.log('이벤트 데이터를 불러오는 중 오류 발생', error);
		        }
		    });
		});
		
	
});*/