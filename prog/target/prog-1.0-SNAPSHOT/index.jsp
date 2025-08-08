
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="/WEB-INF/views/partials/head.jsp">
        <jsp:param name="title" value="Homepage"/>
    </jsp:include>


</head>
<body>

<%
    // Reindirizza il browser alla servlet che gestisce /utente/secret
    response.sendRedirect("./utente/secret");
%>


</body>
</html>
