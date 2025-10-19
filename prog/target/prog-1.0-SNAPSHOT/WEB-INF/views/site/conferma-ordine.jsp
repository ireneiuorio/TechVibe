<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Ordine Confermato - TechVibe</title>
  <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
  <jsp:include page="../partials/head.jsp">
    <jsp:param name="title" value="Ordine Confermato"/>
    <jsp:param name="styles" value="site,ordine-confermato"/>
  </jsp:include>
</head>

<body>
<%@include file="../partials/site/header.jsp"%>

<div class="contenitore-ordine">
  <div class="card-conferma">

    <!-- Icona di successo -->
    <div class="icona-successo">✓</div>

    <h1 class="titolo-ordine">Ordine Confermato!</h1>

    <p class="testo-sottotitolo">
      Grazie per il tuo acquisto! Il tuo ordine è stato elaborato con successo.
    </p>

    <!-- Box numero ordine -->
    <div class="box-numero-ordine">
      <div class="etichetta-numero">Numero Ordine</div>
      <div class="valore-numero">#<c:out value="${param.ordineId}" default="N/A"/></div>
      <div class="stato-confermato">Stato: Confermato</div>
    </div>

    <!-- Informazioni importanti -->
    <div class="sezione-info">
      <h3 class="titolo-sezione">Cosa succede ora?</h3>
      <div class="lista-step">
        <div class="step">
          <div class="badge-step">1</div>
          <span class="testo-step">Riceverai una email di conferma entro pochi minuti</span>
        </div>
        <div class="step">
          <div class="badge-step">2</div>
          <span class="testo-step">Il tuo ordine verrà processato entro 24 ore</span>
        </div>
        <div class="step">
          <div class="badge-step">3</div>
          <span class="testo-step">Ti invieremo tutti gli aggiornamenti sulla spedizione</span>
        </div>
        <div class="step">
          <div class="badge-step">4</div>
          <span class="testo-step">Consegna prevista in 2-5 giorni lavorativi</span>
        </div>
      </div>
    </div>

    <!-- Pulsanti azione -->
    <div class="azioni">
      <a href="${pageContext.request.contextPath}/utente/profile" class="btn-primario">
        Il Mio Profilo
      </a>

      <a href="${pageContext.request.contextPath}/pages" class="btn-secondario">
        Continua lo Shopping
      </a>
    </div>

    <!-- Messaggio di ringraziamento -->
    <div class="box-grazie">
      <p class="testo-grazie">
        <strong>Grazie per aver scelto TechVibe!</strong><br>
        La tua fiducia è importante per noi. Ti garantiamo prodotti di qualità e un servizio eccellente.
      </p>
    </div>

    <!-- Supporto -->
    <div class="box-supporto">
      <p class="testo-supporto">
        Hai domande sul tuo ordine? Contatta il supporto
      </p>
    </div>
  </div>
</div>

<%@include file="../partials/site/footer.jsp"%>
</body>
</html>
