<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1> 숫자 맞추기 게임.</h1>
	<hr>
	<h3>${message }</h3>
	
	<!-- sessionScope.count 가 null인 경우(즉. 정답을 맞춰서 서버단에서 count 세션을 삭제한 경우) 이 부분은 출력을 안한다! -->
	<c:if test="${sessionScope.count != null}">
		<form method="get" action="guess">
		1부터 100사이의 숫자로 맞춰주세요.<br>
		<input type="text" name="number"><br>
		<input type="submit" value="확인">
		</form>
	</c:if>
	
	<a href="guess">게임 다시 시작하기.</a>
</body>
</html>