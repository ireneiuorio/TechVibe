<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="it">
<head>
  <title>Conttati- TechVibe</title>
  <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
  <jsp:include page="../partials/head.jsp">
    <jsp:param name="title" value="Contatti"/>
    <jsp:param name="styles" value="site,privacy"/>
  </jsp:include>
  <meta name="robots" content="noindex">
</head>
<body>
<main class="app">
  <%@ include file="../partials/site/header.jsp" %>

  <section class="privacy container">
    <h1>Contatti</h1>


    <div class="card privacy">
      <h2>Scrivici o chiamaci!</h2>
      <p>
        <strong>TechVibe</strong> – Via xxxxxxxxxxxxxx, Italia<br>
        Email: mailto:privacy@techvibe.it – PEC <em>pec@techvibe.it</em><br>
        Telefono:xxxxxxxxxx
      </p>
    </div>
  </section>

  <%@ include file="../partials/site/footer.jsp" %>
</main>


</body>
</html>
