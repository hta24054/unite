<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello World!" %></h1>
<h1>아이디 : ${member1.id}</h1>
<h1>나이 : ${member1.age}</h1>
<h1>아이디 : ${member2.id}</h1>
<h1>나이 : ${member2.age}ㅇㅇ</h1>
<br/>
<a href="hello-servlet">Hello Servlet</a>
</body>
</html>