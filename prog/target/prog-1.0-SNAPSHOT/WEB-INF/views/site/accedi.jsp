<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="it">
<head>

  <title>Chi sei?</title>
  <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
  <jsp:include page="/WEB-INF/views/partials/head.jsp">
    <jsp:param name="styles" value="site"/>
  </jsp:include>

</head>
<body>
<main class="app">
  <%@ include file="../partials/site/header.jsp" %>


  <section class="chi-siamo-container">
    <h1>Seleziona il tipo di accesso</h1>
    <p style="text-align:center;margin-bottom:2rem;">
      Scegli se entrare come <strong>Amministratore</strong> o come <strong>Cliente</strong>.
    </p>


    <p style="text-align:center;margin-bottom:2rem;">
      Non sei registrato?
      <a href="${pageContext.request.contextPath}/pages/create">Registrati qui</a>
    </p>

    <div style="display:flex;flex-wrap:wrap;justify-content:center;gap:2rem;">

      <!-- Card Admin -->
      <a href="${pageContext.request.contextPath}/utente/secret"
         class="card-link">
        <div class="card login-card">
          <h2>Admin</h2>
          <p>Gestisci prodotti, ordini e utenti dal pannello di controllo.</p>
        </div>
      </a>

      <!-- Card Cliente -->
      <a href="${pageContext.request.contextPath}/pages/accediutente"
         class="card-link">
        <div class="card login-card">
          <h2>Cliente</h2>
          <p>Accedi al tuo account per acquistare smartphone e tablet.</p>
        </div>
      </a>



    </div>
  </section>

  <%@ include file="../partials/site/footer.jsp" %>
</main>
</body>
</html>
