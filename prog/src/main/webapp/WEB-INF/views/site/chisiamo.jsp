<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Chi siamo - TechVibe</title>
  <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
  <jsp:include page="../partials/head.jsp">
    <jsp:param name="title" value="Chi siamo"/>
    <jsp:param name="styles" value="site"/>
  </jsp:include>
</head>
<body>
<main class="app">
  <%@ include file="../partials/site/header.jsp" %>

  <section class="chi-siamo-container">
    <h1>Chi siamo</h1>
    <p>
      Benvenuto su TechVibe il tuo punto di riferimento per smartphone e tablet di ultima generazione.
      La nostra missione è offrire prodotti tecnologici di qualità al miglior prezzo, unendo innovazione e affidabilità.
    </p>
    <p>
      Siamo un team giovane e appassionato, convinti che la tecnologia debba essere semplice, accessibile e migliorare la vita di tutti i giorni.
    </p>
    <p>
      Ogni giorno selezioniamo con cura i migliori modelli dei brand più rinomati, garantendo assistenza continua e spedizioni rapide.
    </p>
  </section>

  <%@ include file="../partials/site/footer.jsp" %>
</main>
</body>
</html>
