<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>

<head>
	<script>
		location.href = "<c:url value="/eh/index.do" />";
	</script>
</head>

<body>
	If you're not forward automatically, <a href="<c:url value="/eh/timesheetOverview.do" />">click here</a>
</body>

</html>