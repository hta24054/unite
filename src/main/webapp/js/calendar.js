(function(){
	$(function(){
	  	const $scheduleModal = $("#scheduleModal");
		const $scheduleName = $("#schedule_name");
		const $start = $("#startAt"); 
		const $end = $("#endAt");
		const $allDay = $("#allDay");
		const $bgColor = $("#bgColor");
		const $description = $("#description");
		
		const events = [];
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
	        /*
	        select: function (arg) { // 캘린더에서 드래그로 이벤트를 생성할 수 있다.
            	const title = prompt('일정을 입력해주세요.');
                if (title) {
                    calendar.addEvent({
                        title: title,
                        start: arg.start,
                        end: arg.end,
                        allDay: arg.allDay,
                    })
                }
                const allEvent = calendar.getEvents(); // .getEvents() 함수로 모든 이벤트를 Array 형식으로 가져온다. 
 
                const events = new Array(); // Json 데이터를 받기 위한 배열 선언
                for (var i = 0; i < allEvent.length; i++) {
                         var obj = new Object();     // Json 을 담기 위해 Object 선언
                         // alert(allEvent[i]._def.title); // 이벤트 명칭 알람
                         obj.title = allEvent[i]._def.title; // 이벤트 명칭  ConsoleLog 로 확인 가능.
                         obj.start = allEvent[i]._instance.range.start; // 시작
                         obj.end = allEvent[i]._instance.range.end; // 끝
 
                         events.push(obj);
                }
                
                const jsondata = JSON.stringify(events);
                console.log(jsondata);
                // saveData(jsondata);
 
                $.ajax({
                      url: "${pageContext.request.contextPath}/schedule/ScheduleAddProcessAction",
                      method: "POST",
                      dataType: "json",
                      data: JSON.stringify(events),
                      contentType: 'application/json',
                })
                calendar.unselect();
            },
            */
	        select: function(arg) {
                $scheduleModal.modal('show');
            },
	        /*
		    events: function(info, successCallback, failureCallback) {
				 successCallback(events);
				
				 $.ajax({
		            url: '${pageContext.request.contextPath}/schedule/ScheduleAddProcessAction',
		            type: 'post',
		            dataType: 'json',
		            data: {
						start: moment(info.start).format('YYYY-MM-dd HH:mm:ss'), 
            			end: moment(info.end).format('YYYY-MM-dd HH:mm:ss') 
					},
		            success: function(events) {
		                callback(events);
		                
		            }
		         });
		        
		    },
		   */
		   /*
	       events: function(start, end, timezone, callback) {
		        // 서버에서 일정 데이터를 가져온다.
		        $.ajax({
		            url: '${pageContext.request.contextPath}/schedule/ScheduleList',
		            type: 'get',
		            dataType: 'json',
		            success: function(events) {
		                callback(events);
		            }
		        });
		    },
		    */
		    dateClick: function(info) { // 일자셀 클릭 함수
	            console.log("dateClick info", info)
	        },
		    
			eventClick: function(info) {  
				console.log("eventClick info", info.event);
				
				if (info) {
					$scheduleName.val(info.event.title);
					$description.val(info.event.extendedProps.contents);
					$bgColor.val(info.event.extendedProps.bgColor);
					$scheduleModal.modal('show');
				}
			},
			eventDidMount: function(info) {
			    console.log("info.event.extendedProps", info.event.extendedProps);
			    // {description: "Lecture", department: "BioChemistry"}
			}
		});
		
		calendar.render();
		
		//모달창 이벤트
		$("#btnRegister").on("click", function () {
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
	  
	  	    // 입력값 객체 생성
			const eventData = {
			  	title: $scheduleName.val(),
			    //start: $start.val().replace("T", " "), // T를 공백으로 변경
			    //end: $end.val().replace("T", " "), // T를 공백으로 변경
			    start: moment($start.val(), 'YYYY-MM-DDTHH:mm').format('YYYY-MM-dd HH:mm'),
				end: moment($end.val(), 'YYYY-MM-DDTHH:mm').format('YYYY-MM-dd HH:mm'),
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
				url: "${pageContext.request.contextPath}/schedule/ScheduleAddProcessAction",
				type: "post",
	            dataType: "json",
	            data: { 
					 eventsJson: JSON.stringify(eventsFromCalendar),
				},
	            success: function(data) {
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
			//$allDay.prop("checked", false);
			$bgColor.val("");
			$description.val("");
		});
		
		$bgColor.on("change", function(){
			$(this).css("backgound-color", $(this).val());
		});
		
	
		
		
	});
})();