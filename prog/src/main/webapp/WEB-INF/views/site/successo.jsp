
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <title>Registrazione completata</title>
    <jsp:include page="/WEB-INF/views/partials/head.jsp">
        <jsp:param name="styles" value="site"/>
    </jsp:include>
</head>
<body>
<main class="app">
    <%@ include file="../partials/site/header.jsp" %>

    <section class="chi-siamo-container">
        <div class="card privacy" style="text-align:center;">
            <h1 style="color:var(--primary-light); margin-bottom:1rem;">
                Registrazione completata!
            </h1>
            <br>

            <a href="${pageContext.request.contextPath}/pages/accediutente" class="btn primary">
                Vai al login
            </a>
        </div>
    </section>

    <%@ include file="../partials/site/footer.jsp" %>
</main>
</body>
</html>
