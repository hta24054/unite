<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello World!" %></h1>
<h2>이순신 출력 O</h2>
<c:if test="${member1.age>40}">
    <h3 style="color: blue">아이디 : ${member1.id}</h3>
    <h3 style="color: blue">나이 : ${member1.age}</h3>
</c:if>
<h2>홍길동은 출력 X</h2>
<c:if test="${member2.age>40}">
    <h3 style="color: blue">아이디 : ${member2.id}</h3>
    <h3 style="color: blue">>나이 : ${member2.age}</h3>
</c:if>
<br/>
<a href="hello-servlet">Hello Servlet</a>
</body>
</html>