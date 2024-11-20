<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="accordion">
  <div class="accordion-item">
    <div class="accordion-title" onclick="toggleAccordion(this)">
      전사게시판
    </div>
    <div class="accordion-content">
    	<div class="accordion-a">
    		<a href="${pageContext.request.contextPath}/board/list1">공지사항</a>
    	</div>
    	<div class="accordion-a">
    		<a href="${pageContext.request.contextPath}/board/list2">주간시간표</a>
    	</div>
    	<div class="accordion-a">
    		<a href="${pageContext.request.contextPath}/board/list3">FAQ</a>
    	</div>
    </div>
  </div>
  <div class="accordion-item">
    <div class="accordion-title" onclick="toggleAccordion(this)">
      일반게시판
    </div>
    <div class="accordion-content">
        <div class="accordion-a">
    		<a href="${pageContext.request.contextPath}/board/list4">일반게시판</a>
    	</div>
    </div>
  </div>
  <div class="accordion-item">
    <div class="accordion-title" onclick="toggleAccordion(this)">
      부서게시판
    </div>
    <div class="accordion-content">
        <div class="accordion-a">
    		<a href="${pageContext.request.contextPath}/board/list5">솔루션영업팀</a>
    	</div>
    </div>
  </div>
</div>

<script>
  function toggleAccordion(element) {
    const content = element.nextElementSibling;
    const isOpen = content.style.display === "block";
    

    // 선택한 아코디언 콘텐츠 열기/닫기
    if (!isOpen) {
      content.style.display = "block";
    }else{
      content.style.display = "none";
    }
  }
</script>