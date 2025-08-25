<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="it">
<head>
  <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
  <title>Accesso Cliente</title>

  <jsp:include page="/WEB-INF/views/partials/head.jsp">
    <jsp:param name="styles" value="site"/>
  </jsp:include>
</head>

<body>
<main class="app">
  <%@ include file="../partials/site/header.jsp" %>

  <section class="chi-siamo-container">
    <div class="auth-card">
      <h2>Accesso Cliente</h2>

      <c:if test="${not empty loginError}">
        <div class="notification error">
          <c:out value="${loginError}"/>
        </div>
      </c:if>

      <form class="auth-form" action="${pageContext.request.contextPath}/pages/areapersonale" method="post">
        <label for="email">
          <span>Email</span>
          <input type="email" id="email" name="email" placeholder="@" required>
        </label>

        <label for="password">
          <span>Password</span>
          <input type="password" id="password" name="password" placeholder="*****" required>
        </label>

        <button type="submit" class="btn primary">Accedi</button>
      </form>

      <div style="text-align:center; margin-top:1rem; font-size:.95rem;">
        Non sei registrato?
        <a href="${pageContext.request.contextPath}/pages/create"
           style="color:var(--primary-light); text-decoration:none; font-weight:600;">
          Registrati
        </a>
      </div>
    </div>
  </section>

  <%@ include file="../partials/site/footer.jsp" %>
</main>
</body>
</html>
