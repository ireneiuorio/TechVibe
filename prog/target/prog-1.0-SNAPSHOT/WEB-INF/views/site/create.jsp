<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <title>Registrazione Cliente</title>
    <jsp:include page="/WEB-INF/views/partials/head.jsp">
        <jsp:param name="styles" value="site"/>
    </jsp:include>
</head>
<body>
<main class="app">
    <%@ include file="../partials/site/header.jsp" %>

    <section class="chi-siamo-container">
        <div class="auth-card">
            <h2>Registrazione Cliente</h2>

            <!-- Messaggi -->
            <c:if test="${not empty error}">
                <div class="notification error">
                    <c:out value="${error}"/>
                </div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="card privacy" style="text-align:center;">
                    <c:out value="${success}"/>
                </div>
            </c:if>

            <!-- Form -->
            <form class="auth-form" action="${pageContext.request.contextPath}/pages/create" method="post" novalidate>

                <label for="nome">
                    <span>Nome</span>
                    <input type="text" id="nome" name="nome" value="${param.nome}" required maxlength="60">
                </label>

                <label for="cognome">
                    <span>Cognome</span>
                    <input type="text" id="cognome" name="cognome" value="${param.cognome}" required maxlength="60">
                </label>

                <label for="email">
                    <span>Email</span>
                    <input type="email" id="email" name="email" value="${param.email}" required maxlength="255" placeholder="es. nome@email.it">
                </label>

                <label for="password">
                    <span>Password</span>
                    <input type="password" id="password" name="password" required minlength="8" placeholder="min 8 caratteri">
                </label>

                <label for="confirm">
                    <span>Conferma password</span>
                    <input type="password" id="confirm" name="confirm" required minlength="8">
                </label>

                <label for="telefono">
                    <span>Telefono</span>
                    <input type="tel" id="telefono" name="telefono" value="${param.telefono}" maxlength="30" placeholder="+39 ...">
                </label>

                <label for="indirizzoSpedizione">
                    <span>Indirizzo di spedizione</span>
                    <input type="text" id="indirizzoSpedizione" name="indirizzospedizione" value="${param.indirizzospedizione}" maxlength="255" placeholder="Via, n°, CAP, Città">
                </label>

                <button type="submit" class="btn primary">Crea account</button>
            </form>

            <!-- Link login -->
            <div style="text-align:center; margin-top:1rem; font-size:.95rem;">
                Hai già un account?
                <a href="${pageContext.request.contextPath}/pages/accediutente"
                   style="color:var(--primary-light); text-decoration:none; font-weight:600;">
                    Accedi
                </a>
            </div>
        </div>
    </section>

    <%@ include file="../partials/site/footer.jsp" %>
</main>
</body>
</html>
