<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>Hello aasdfasdfWorld!</h2>


<pre>
<c:forEach var="name" items="${list}" varStatus="statusName" >
	<c:out value="${name}" />		<c:out value="${statusName}" />
</c:forEach>
</pre>

</body>
</html>
