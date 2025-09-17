<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="it">
<head>
  <title>Dettaglio Ordine #${ordine.idOrdine} - TechVibe</title>
  <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
  <jsp:include page="/WEB-INF/views/partials/head.jsp">
    <jsp:param name="styles" value="site"/>
  </jsp:include>
</head>

<body>
<main class="app">
  <%@ include file="../partials/site/header.jsp" %>

  <div style="max-width: 800px; margin: 2rem auto; padding: 0 1rem;">

    <!-- Back Button -->
    <a href="${pageContext.request.contextPath}/utente/profile"
       style="display: inline-flex; align-items: center; color: var(--primary-light); text-decoration: none;
              margin-bottom: 2rem; font-weight: 600;">
      ← Torna al Profilo
    </a>

    <!-- Order Header -->
    <div style="background: white; padding: 2rem; border-radius: 12px; margin-bottom: 2rem;
                box-shadow: 0 2px 8px rgba(0,0,0,0.05); border-left: 5px solid var(--primary-light);">
      <h1 style="color: var(--primary-light); margin-bottom: 1rem;">Ordine #${ordine.idOrdine}</h1>

      <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1.5rem;">
        <div>
          <div style="font-weight: 600; color: #495057; margin-bottom: 0.5rem;">Stato</div>
          <div style="color: #28a745; font-weight: 600; text-transform: capitalize;">${ordine.stato}</div>
        </div>
        <div>
          <div style="font-weight: 600; color: #495057; margin-bottom: 0.5rem;">Totale</div>
          <div style="color: var(--primary-light); font-size: 1.3rem; font-weight: bold;">
            <fmt:formatNumber value="${ordine.totale}" type="currency" currencySymbol="€" minFractionDigits="2"/>
          </div>
        </div>
        <div>
          <div style="font-weight: 600; color: #495057; margin-bottom: 0.5rem;">Metodo Pagamento</div>
          <div style="text-transform: capitalize; color: #6c757d;">
            <c:out value="${ordine.metodoDiPagamento}" default="Non specificato"/>
          </div>
        </div>
        <c:if test="${not empty ordine.metodoDiSpedizione}">
          <div>
            <div style="font-weight: 600; color: #495057; margin-bottom: 0.5rem;">Spedizione</div>
            <div style="text-transform: capitalize; color: #6c757d;">${ordine.metodoDiSpedizione}</div>
          </div>
        </c:if>
      </div>
    </div>

    <!-- Products Section -->
    <div style="background: white; padding: 2rem; border-radius: 12px; margin-bottom: 2rem;
                box-shadow: 0 2px 8px rgba(0,0,0,0.05);">
      <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Prodotti Ordinati</h3>

      <c:choose>
        <c:when test="${not empty ordine.carrello and not empty ordine.carrello.items}">
          <c:forEach var="item" items="${ordine.carrello.items}">
            <div style="display: flex; gap: 1rem; padding: 1rem; border: 1px solid #eee;
                        border-radius: 8px; margin-bottom: 1rem;">
              <!-- Immagine prodotto -->
              <div style="flex-shrink: 0; width: 80px; height: 80px;">
                <c:choose>
                  <c:when test="${not empty item.prodotto.immagine1}">
                    <img src="${pageContext.request.contextPath}/img/${item.prodotto.immagine1}"
                         alt="${item.prodotto.marca} ${item.prodotto.modello}"
                         style="width: 100%; height: 100%; object-fit: cover; border-radius: 6px;">
                  </c:when>
                  <c:otherwise>
                    <img src="${pageContext.request.contextPath}/img/placeholder.png"
                         alt="Prodotto"
                         style="width: 100%; height: 100%; object-fit: cover; border-radius: 6px;">
                  </c:otherwise>
                </c:choose>
              </div>

              <!-- Dettagli prodotto -->
              <div style="flex: 1;">
                <div style="font-weight: 500; color: var(--primary-light); margin-bottom: 0.5rem;">
                    ${item.prodotto.marca} ${item.prodotto.modello}
                </div>
                <div style="color: #666; font-size: 0.9rem; margin-bottom: 0.5rem;">
                  <c:if test="${not empty item.prodotto.storage}">Storage: ${item.prodotto.storage}GB</c:if>
                  <c:if test="${not empty item.prodotto.colore}"> • Colore: ${item.prodotto.colore}</c:if>
                </div>
                <div style="display: flex; justify-content: space-between; align-items: center;">
                  <span style="color: #666;">Quantità: ${item.quantita}</span>
                  <span style="font-weight: 500; color: var(--primary-light);">
                    <fmt:formatNumber value="${item.total()}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                  </span>
                </div>
              </div>
            </div>
          </c:forEach>
        </c:when>
        <c:otherwise>
          <div style="text-align: center; padding: 2rem; color: #666;">
            <p>Nessun prodotto trovato per questo ordine.</p>
          </div>
        </c:otherwise>
      </c:choose>
    </div>


  </div>

  <%@ include file="../partials/site/footer.jsp" %>
</main>

<style>
  @media (max-width: 768px) {
    div[style*="grid-template-columns: 1fr 1fr"] {
      grid-template-columns: 1fr !important;
    }

    div[style*="display: flex"] {
      flex-direction: column !important;
      gap: 0.5rem !important;
    }
  }
</style>

</body>
</html>