
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="/WEB-INF/views/partials/head.jsp">
        <jsp:param name="title" value="Homepage"/>
    </jsp:include>


</head>
<body>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.getRequestDispatcher("/WEB-INF/views/pages/index.jsp").forward(request, response);
    return;
%>




</body>
</html>
