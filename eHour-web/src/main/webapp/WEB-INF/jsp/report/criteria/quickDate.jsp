<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
{
	"start": "<fmt:formatDate value="${range.dateStart}" pattern="yyyy-MM-dd" />T00:00:00-00:00",
	"end": "<fmt:formatDate value="${range.dateEndForDisplay}" pattern="yyyy-MM-dd" />T00:00:00-00:00"
}
