<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<div class="sidebar">
    <h3 style="color:rgb(51, 68, 102)">캘린더</h3><br>
    <ul class="list-group" style="list-style-type: disc; padding-left: 20px;">
        <li class="left" style="border: none; list-style: none;">
        	<button type="button" class="btn btn-lg" data-toggle="modal" data-target="#scheduleModal" style="margin-left: -20px; background-color: #334466; color: #fff;">일정 등록</button>
        </li>
        <li class="left" style="border: none;">
        	<a href="${pageContext.request.contextPath}/schedule/scheduleShare">공유 일정 등록</a>
        </li>
    </ul>
</div>