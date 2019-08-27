<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<meta charset="ISO-8859-1">
<title>How Many Wins</title>
</head>
<body>

<div id="container"> 
 <table class="table">
	<tbody>
		<tr>
			 <c:forEach var="i" items="${winuser}">
			   <td><a href="/winLeader?id=${i.id}">${i.name}</a></td>
			   <td>${i.wins }</td>
			   <td>${i.losses }</td>
			 </c:forEach>
	   </tr>
	</tbody>
 </table>
</div> 

</body>
</html>