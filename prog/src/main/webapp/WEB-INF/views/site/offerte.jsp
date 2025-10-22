<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="it">
<head>
  <title>Offerte Speciali - TechVibe</title>
  <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
  <jsp:include page="/WEB-INF/views/partials/head.jsp">
    <jsp:param name="styles" value="site,offerte-techvibe"/>
  </jsp:include>

</head>
<body>
<main class="app">
  <%@ include file="../partials/site/header.jsp" %>

  <!-- HERO SECTION OFFERTE -->
  <section class="hero-offerte">
    <h1 class="hero-titolo">Offerte Speciali</h1>
    <p class="hero-sottotitolo">Scopri i migliori prodotti tech a prezzi scontati</p>
  </section>

  <div class="contenitore-offerte">

    <!-- HEADER RISULTATI -->
    <div class="intestazione-risultati">
      <h2 class="titolo-risultati">Prodotti in Offerta</h2>
      <div class="conteggio-risultati">
        <c:choose>
          <c:when test="${not empty prodotti}">
            ${fn:length(prodotti)} offerte disponibili
          </c:when>
          <c:otherwise>
            Nessuna offerta al momento
          </c:otherwise>
        </c:choose>
      </div>
    </div>

    <!-- GRIGLIA PRODOTTI IN OFFERTA -->
    <div class="griglia-offerte">
      <c:forEach var="p" items="${prodotti}">
        <c:set var="imgRaw" value="${p.immagine1}" />

        <c:choose>
          <c:when test="${not empty imgRaw}">
            <c:set var="imgSrc" value="${pageContext.request.contextPath}/img/${imgRaw}" />
          </c:when>
          <c:otherwise>
            <c:set var="imgSrc" value="${pageContext.request.contextPath}/img/placeholder.png" />
          </c:otherwise>
        </c:choose>

        <!-- CARD PRODOTTO IN OFFERTA -->
        <div class="card card-offerta">
          <!-- BADGE SCONTO -->
          <div class="badge-sconto">-${p.percentualeSconto}%</div>

          <a href="${pageContext.request.contextPath}/pages/prodotto?id=${p.idProdotto}" class="link-prodotto">
            <img src="${imgSrc}" alt="${p.marca} ${p.modello}" class="immagine-prodotto">
            <h3 class="nome-prodotto">${p.marca} ${p.modello}</h3>
          </a>

          <!-- SEZIONE PREZZO -->
          <div class="box-prezzo">
            <div class="info-storage">
              <c:choose>
                <c:when test="${not empty p.storage}">
                  ${p.storage}GB
                </c:when>
                <c:otherwise>
                  Storage N/A
                </c:otherwise>
              </c:choose>
            </div>

            <div class="prezzo-originale">
              €<fmt:formatNumber value="${p.prezzoOriginale}" type="number" minFractionDigits="2"/>
            </div>

            <div class="prezzo-finale">
              €<fmt:formatNumber value="${p.prezzoFinale}" type="number" minFractionDigits="2"/>
            </div>
          </div>

          <!-- PULSANTE -->
          <button class="btn primary add-to-cart btn-full"
                  data-id="${p.idProdotto}" data-qty="1">
            Aggiungi al carrello
          </button>
        </div>
      </c:forEach>

      <!-- MESSAGGIO SE NESSUNA OFFERTA -->
      <c:if test="${empty prodotti}">
        <div class="box-nessuna-offerta">
          <h3>Nessuna offerta al momento</h3>
          <p>Le nostre migliori offerte torneranno presto</p>
          <a href="${pageContext.request.contextPath}/pages/smartphone" class="cta-catalogo">
            Esplora il Catalogo
          </a>
        </div>
      </c:if>
    </div>

    <!-- SEZIONE INFORMATIVA -->
    <c:if test="${not empty prodotti}">
      <div class="box-informativo">
        <h3>Affrettati</h3>
        <p>Le offerte sono limitate nel tempo e nelle quantità disponibili.</p>
      </div>
    </c:if>
  </div>

  <%@ include file="../partials/site/footer.jsp" %>
</main>
</body>
</html>
