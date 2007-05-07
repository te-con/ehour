<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>

<head>
	<script>
		location.href = "<c:url value="/login.do" />";
	</script>
</head>

<body>
	If you're not forward automatically, <a href="<c:url value="/login.do" />">click here</a>
</body>

</html>