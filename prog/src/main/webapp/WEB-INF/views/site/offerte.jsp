<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="it">
<head>
  <title>Offerte Speciali - TechVibe</title>
  <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
  <jsp:include page="/WEB-INF/views/partials/head.jsp">
    <jsp:param name="styles" value="site"/>
  </jsp:include>
</head>
<body>
<main class="app">
  <%@ include file="../partials/site/header.jsp" %>

  <!-- HERO SECTION OFFERTE -->
  <section style="background: var(--primary-light); color: white; text-align: center; padding: 3rem 2rem;">
    <h1 style="font-size: 2.5rem; margin: 0 0 1rem 0; font-weight: bold;">Offerte Speciali</h1>
    <p style="font-size: 1.2rem; margin: 0; opacity: 0.9;">Scopri i migliori prodotti tech a prezzi scontati</p>
  </section>

  <div style="max-width: 1400px; margin: 0 auto; padding: 2rem;">

    <!-- HEADER RISULTATI -->
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; padding: 0 1rem;">
      <h2 style="color: var(--primary-light); font-size: 2rem; margin: 0;">Prodotti in Offerta</h2>
      <div style="color: #666; font-size: 0.9rem;">
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
    <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 2rem; padding: 0 1rem;">
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
        <div class="card product-card" style="background: white; border: 1px solid #eee; border-radius: 15px; padding: 1.25rem; text-align: center; transition: all 0.3s ease; box-shadow: 0 2px 8px rgba(0,0,0,0.05); position: relative;">

          <!-- BADGE SCONTO DISCRETO -->
          <div style="position: absolute; top: 10px; right: 10px; background: var(--primary-light); color: white; padding: 4px 8px; border-radius: 8px; font-weight: 600; font-size: 0.8rem;">
            -${p.percentualeSconto}%
          </div>

          <a href="${pageContext.request.contextPath}/pages/prodotto?id=${p.idProdotto}" style="text-decoration: none; color: inherit;">
            <img src="${imgSrc}" alt="${p.marca} ${p.modello}" style="width: 100%; height: 180px; object-fit: cover; margin-bottom: 1rem; border-radius: 8px;">
            <h3 style="margin: 0 0 1rem 0; font-size: 1.1rem; color: var(--primary-light); font-weight: 600;">
                ${p.marca} ${p.modello}
            </h3>
          </a>

          <!-- SEZIONE PREZZO SOBRIA -->
          <div class="details" style="margin: 0.75rem 0; border-radius: 10px; padding: 0.4rem 0; background-color: var(--primary-light); color: var(--white);">
            <!-- Storage info -->
            <div style="margin-bottom: 0.4rem; font-size: 0.75rem; color: white; opacity: 0.8;">
              <c:choose>
                <c:when test="${not empty p.storage}">
                  ${p.storage}GB
                </c:when>
                <c:otherwise>
                  Storage N/A
                </c:otherwise>
              </c:choose>
            </div>

            <!-- Prezzo originale barrato -->
            <div style="font-size: 0.9rem; text-decoration: line-through; color: white; opacity: 0.7; margin-bottom: 2px;">
              €<fmt:formatNumber value="${p.prezzoOriginale}" type="number" minFractionDigits="2"/>
            </div>

            <!-- Prezzo scontato -->
            <div style="font-size: 1.2rem; font-weight: 600; color: white;">
              €<fmt:formatNumber value="${p.prezzoFinale}" type="number" minFractionDigits="2"/>
            </div>
          </div>

          <!-- PULSANTE STANDARD -->
          <button class="btn primary add-to-cart"
                  data-id="${p.idProdotto}" data-qty="1"
                  style="width: 100%;">
            Aggiungi al carrello
          </button>
        </div>
      </c:forEach>

      <!-- MESSAGGIO SE NESSUNA OFFERTA -->
      <c:if test="${empty prodotti}">
        <div style="grid-column: 1 / -1; text-align: center; padding: 4rem 2rem; background: var(--light-gray); border-radius: 15px; border: 2px dashed #ddd;">
          <h3 style="color: var(--primary-light); margin-bottom: 1rem;">Nessuna offerta al momento</h3>
          <p style="color: #666; margin-bottom: 2rem;">Le nostre migliori offerte torneranno presto</p>
          <a href="${pageContext.request.contextPath}/pages/smartphone" style="background: var(--primary-light); color: white; padding: 12px 24px; border-radius: 8px; text-decoration: none; font-weight: 600;">
            Esplora il Catalogo
          </a>
        </div>
      </c:if>
    </div>

    <!-- SEZIONE INFORMATIVA -->
    <c:if test="${not empty prodotti}">
      <div style="margin-top: 3rem; padding: 2rem; background: var(--light-gray); border-radius: 15px; text-align: center;">
        <h3 style="color: var(--primary-light); margin-bottom: 1rem;">Affrettati</h3>
        <p style="color: #666; font-size: 1.1rem; margin: 0;">
          Le offerte sono limitate nel tempo e nelle quantità disponibili.
        </p>
      </div>
    </c:if>
  </div>

  <%@ include file="../partials/site/footer.jsp" %>
</main>

<style>
  /* Hover effect per le card offerte */
  .card.product-card:hover {
    transform: translateY(-8px);
    box-shadow: 0 8px 20px rgba(0,0,0,0.1);
    border-color: var(--primary-light);
  }

  /* Hover effect per il pulsante */
  .btn.primary:hover {
    background: var(--primary-light) !important;
  }

  /* Responsive */
  @media (max-width: 768px) {
    .card.product-card {
      margin: 0 0.5rem;
    }
  }
</style>

</body>
</html>