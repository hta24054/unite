document.addEventListener('DOMContentLoaded', function () {
	(function(){
		$(function(){
		  	const $scheduleModal = $("#scheduleModal");
			const $scheduleName = $("#schedule_name");
			const $start = $("#startAt"); 
			const $end = $("#endAt");
			const $allDay = $("#allDay");
			const $bgColor = $("#bgColor");
			const $description = $("#description");
			
		    const calendarEl = document.getElementById('calendar'); // calendar element 취득
			
			const calendar = new FullCalendar.Calendar(calendarEl, {
			    //height: '700px', // calendar 높이 설정
		        expandRows: true, // 화면에 맞게 높이 재설정
		        slotMinTime: '08:00', // Day 캘린더에서 시작 시간
		        slotMaxTime: '22:00', // Day 캘린더에서 종료 시간
		        defaultAllDay: true, // 종일 이벤트
		        //timeZone: 'UTC',
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
	                $scheduleModal.modal('show');
	            },
	            eventSources: [{
					events: function(info, successCallback, failureCallback) {
				        // 서버에서 일정 데이터를 가져온다.
				        $.ajax({
					        url: "ScheduleListAction",
					        type: "get",
					        dataType: "json",
					        data: {
								"schedule_id" : $("#schedule_id").val(),
								"emp_id" : $("#emp_id").val(),
								title: $scheduleName.val(),
								start: moment(info.startStr).format('YYYY-MM-DD HH:mm'),
								end: moment(info.endStr).format('YYYY-MM-DD HH:mm'),
								allDay: $allDay.is(":checked"), // 체크박스인 경우 true/false
							    backgroundColor: $bgColor.val(),
							    description: $description.val()
							},
					        success: function(data) {
					            console.log("data", data)
					            successCallback(data);
					        },
					        error: function(error) {
					            console.log('이벤트 데이터를 불러오는 중 오류 발생:', error);
					        }
					    });
				    },
				}],
			    dateClick: function(info) { // 일자셀 클릭 함수
		            console.log("dateClick info", info)
		        },
			    
				eventClick: function(info) {  
					console.log("eventClick info", info.event);
					
					if (info) {
						$scheduleName.val(info.event.title);
						
						
						$bgColor.val(info.event.extendedProps.bgColor);
						$description.val(info.event.extendedProps.description);
						$scheduleModal.modal('show');
					}
				},
				eventDidMount: function(info) {
				    console.log("info.event.extendedProps", info.event.extendedProps);
				}
			});
			calendar.render();
	
			//모달창 이벤트
			$("#btnRegister").on("click", function () {
		  	    // 입력값 객체 생성
				const eventData = {
				  	title: $scheduleName.val(),
				    start: moment($start.val(), 'YYYY-MM-DDTHH:mm').format('YYYY-MM-DD HH:mm'),
					end: moment($end.val(), 'YYYY-MM-DDTHH:mm').format('YYYY-MM-DD HH:mm'),
				    allDay: $allDay.is(":checked"), // 체크박스인 경우 true/false
				    bgColor: $bgColor.val(),
				    description: $description.val()
				};
		
				// 끝나는 날짜가 시작하는 날짜보다 이전인지 검증
				if (new Date(eventData.start) > new Date(eventData.end)) {
				    alert("끝나는 날짜/시간이 시작 날짜/시간보다 이전입니다. 다시 확인해 주세요.");
				    return false;
				}
				
				// 이벤트 추가
				calendar.addEvent(eventData);
				
				const eventsFromCalendar = $('#calendar').fullCalendar('clientEvents');
		        
			    $.ajax({
					url: "ScheduleAddProcessAction",
					type: "post",
		            dataType: "json",
		            data: { 
						 eventsJson: JSON.stringify(eventsFromCalendar),
					},
		            success: function(data) {
						console.log("data", data);
		                $scheduleModal.modal("hide");
		            },
		            error: function(){
						console.log('에러');
					}
			    });
				
				// 모달 닫기 및 입력 필드 초기화
				$scheduleModal.modal("hide");
				$scheduleName.val("");
				$start.val("");
				$end.val("");
				$allDay.prop("checked", false);
				$bgColor.val("");
				$description.val("");
			});

			
			//form 유효성 체크
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
				
				const start = moment($start.val(), 'YYYY-MM-DDTHH:mm'); 
				const end = moment($end.val(), 'YYYY-MM-DDTHH:mm'); 

				// 서버에서 일정 데이터를 가져온다.
		        $.ajax({
			        url: "ScheduleListAction",
			        type: "get",
			        dataType: "json",
			        data: {
						"schedule_id" : $("#schedule_id").val(),
						"emp_id" : $("#emp_id").val(), 
						title: $scheduleName.val(),
						start: start.format('YYYY-MM-DD HH:mm'),
						end: end.format('YYYY-MM-DD HH:mm'),
						//allDay: $allDay.is(":checked"), // 체크박스인 경우 true/false
					    backgroundColor: $bgColor.val(),
					    description: $description.val()
					},
			        success: function(data) {
			            console.log("data", data);
			            calendar.addEvent(data);
			            $scheduleModal.modal("hide");
			            $scheduleName.val("");
						$start.val("");
						$end.val("");
						$allDay.prop("checked", false);
						$bgColor.val("");
						$description.val("");
			        },
			        error: function(error) {
			            console.log('이벤트 데이터를 불러오는 중 오류 발생:', error);
			        }
			    });
			    
			    $scheduleModal.modal("hide");
			    $scheduleName.val("");
				$start.val("");
				$end.val("");
				$allDay.prop("checked", false);
				$bgColor.val("");
				$description.val("");
			});
			
		});
	})();	
});
