
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="it">
<head>
  <title>Accedi Admin</title>
  <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">

  <jsp:include page="/WEB-INF/views/partials/head.jsp">
    <jsp:param name="styles" value="site"/>
  </jsp:include>

</head>

<body>
<%@ include file="../partials/site/header.jsp" %>
<div class="auth-wrapper">
  <div class="auth-card">
    <h2>Accesso Admin</h2>

    <c:if test="${not empty loginError}">
      <div class="notification error">
        <c:out value="${loginError}"/>
      </div>
    </c:if>

    <form class="auth-form" action="${pageContext.request.contextPath}/utente/secret" method="post">
      <label for="email">
        <span>Email</span>
        <input type="email" id="email" name="email" placeholder="test@test.com" required>
      </label>

      <label for="password">
        <span>Password</span>
        <input type="password" id="password" name="password" placeholder="1234" required>
      </label>

      <button type="submit" class="btn primary">Accedi</button>
    </form>

    <div style="text-align:center; margin-top:1rem; font-size:.95rem;">
      Non sei un admin?
      <a href="${pageContext.request.contextPath}/pages/accediutente"
         style="color:var(--primary-light); text-decoration:none; font-weight:600;">
        Vai alla pagina dedicata ai clienti
      </a>
    </div>
  </div>

</div>


</body>
<%@ include file="../partials/site/footer.jsp" %>
</html>
