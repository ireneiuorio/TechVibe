<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
  <title>Il tuo Carrello - TechVibe</title>
  <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
  <jsp:include page="../partials/head.jsp">
    <jsp:param name="title" value="Carrello"/>
    <jsp:param name="styles" value="site"/>
  </jsp:include>
</head>

<body>
<%@include file="../partials/site/header.jsp"%>

<div style="max-width: 1200px; margin: 2rem auto; padding: 0 1rem;">
  <h1 style="color: var(--primary-light); margin-bottom: 2rem; text-align: center;">Il tuo Carrello</h1>

  <c:choose>
    <c:when test="${empty carrello.items}">
      <!-- Carrello vuoto -->
      <div style="text-align: center; padding: 3rem; background: #f8f9fa; border-radius: 15px;">
        <h2 style="color: #666; margin-bottom: 1rem;">Il tuo carrello è vuoto</h2>
        <p style="color: #888; margin-bottom: 2rem;">Aggiungi alcuni prodotti per iniziare!</p>
        <a href="${pageContext.request.contextPath}/pages"
           style="display: inline-block; background: var(--primary-light); color: white; padding: 1rem 2rem;
                          text-decoration: none; border-radius: 8px; font-weight: 500;">
          Continua lo Shopping
        </a>
      </div>
    </c:when>
    <c:otherwise>
      <!-- Carrello con prodotti -->
      <div style="display: grid; grid-template-columns: 1fr 350px; gap: 2rem;">

        <!-- Lista prodotti -->
        <div>
          <c:forEach var="item" items="${carrello.items}">
            <div style="display: flex; gap: 1rem; padding: 1.5rem; background: white;
                                    border-radius: 12px; margin-bottom: 1rem; box-shadow: 0 2px 8px rgba(0,0,0,0.05);"
                 data-prodotto-id="${item.prodotto.idProdotto}">

              <!-- Immagine prodotto -->
              <div style="flex-shrink: 0;">
                <c:choose>
                  <c:when test="${not empty item.prodotto.immagine1}">
                    <c:set var="imgSrc" value="${pageContext.request.contextPath}/img/${item.prodotto.immagine1}" />
                  </c:when>
                  <c:otherwise>
                    <c:set var="imgSrc" value="${pageContext.request.contextPath}/img/placeholder.png" />
                  </c:otherwise>
                </c:choose>
                <img src="${imgSrc}"
                     alt="${item.prodotto.marca} ${item.prodotto.modello}"
                     style="width: 120px; height: 120px; object-fit: cover; border-radius: 8px;">
              </div>

              <!-- Dettagli prodotto -->
              <div style="flex: 1;">
                <h3 style="margin: 0 0 0.5rem 0; color: var(--primary-light);">
                    ${item.prodotto.marca} ${item.prodotto.modello}
                </h3>

                <div style="color: #666; font-size: 0.9rem; margin-bottom: 1rem;">
                  <c:if test="${not empty item.prodotto.storage}">
                    <div>Storage: ${item.prodotto.storage}GB</div>
                  </c:if>
                  <c:if test="${not empty item.prodotto.colore}">
                    <div>Colore: ${item.prodotto.colore}</div>
                  </c:if>
                </div>

                <!-- Prezzo unitario -->
                <div style="font-size: 1.1rem; font-weight: 500; color: #333; margin-bottom: 1rem;">
                  <fmt:formatNumber value="${item.prodotto.prezzo}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                </div>

                <!-- Controlli quantità e rimozione -->
                <div style="display: flex; justify-content: space-between; align-items: center;">
                  <div style="display: flex; align-items: center; gap: 0.5rem;">
                    <label style="font-size: 0.9rem; color: #666;">Quantità:</label>
                    <input type="number"
                           class="quantita-carrello"
                           data-prodotto-id="${item.prodotto.idProdotto}"
                           value="${item.quantita}"
                           min="1"
                           style="width: 60px; padding: 0.25rem; border: 1px solid #ddd; border-radius: 4px; text-align: center;">
                  </div>

                  <button class="remove-from-cart"
                          data-id="${item.prodotto.idProdotto}"
                          style="background: #dc3545; color: white; border: none; padding: 0.5rem 1rem;
                                                   border-radius: 6px; cursor: pointer; font-size: 0.9rem;">
                    Rimuovi
                  </button>
                </div>
              </div>

              <!-- Totale item -->
              <div style="flex-shrink: 0; text-align: right;">
                <div style="font-size: 1.3rem; font-weight: bold; color: var(--primary-light);">
                  <fmt:formatNumber value="${item.total()}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                </div>
                <div style="font-size: 0.8rem; color: #888;">
                    ${item.quantita} x <fmt:formatNumber value="${item.prodotto.prezzo}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                </div>
              </div>
            </div>
          </c:forEach>

          <!-- Pulsante svuota carrello -->
          <div style="text-align: center; margin-top: 2rem;">
            <button class="svuota-carrello"
                    style="background: #6c757d; color: white; border: none; padding: 0.75rem 1.5rem;
                                       border-radius: 8px; cursor: pointer; font-size: 0.9rem;">
              Svuota Carrello
            </button>
          </div>
        </div>

        <!-- Riepilogo ordine -->
        <div style="background: white; padding: 2rem; border-radius: 12px; height: fit-content;
                            box-shadow: 0 2px 8px rgba(0,0,0,0.05);">

          <h3 style="margin: 0 0 1.5rem 0; color: var(--primary-light);">Riepilogo Ordine</h3>

          <!-- Subtotale -->
          <div style="display: flex; justify-content: space-between; margin-bottom: 1rem; padding-bottom: 1rem; border-bottom: 1px solid #eee;">
            <span>Subtotale (${carrello.items.size()} prodotti):</span>
            <span id="carrello-totale" style="font-weight: 500;">
                            <fmt:formatNumber value="${carrello.total()}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                        </span>
          </div>

          <!-- Spedizione -->
          <div style="display: flex; justify-content: space-between; margin-bottom: 1rem; color: #666; font-size: 0.9rem;">
            <span>Spedizione:</span>
            <span>Gratuita</span>
          </div>

          <!-- Totale finale -->
          <div style="display: flex; justify-content: space-between; font-size: 1.2rem; font-weight: bold;
                                padding-top: 1rem; border-top: 2px solid var(--primary-light); color: var(--primary-light);">
            <span>Totale:</span>
            <span>
              <fmt:formatNumber value="${carrello.total()}" type="currency" currencySymbol="€" minFractionDigits="2"/>
            </span>
          </div>



          <!-- Pulsanti azione -->
          <div style="margin-top: 2rem; display: flex; flex-direction: column; gap: 1rem;">
            <c:choose>
              <c:when test="${not empty sessionScope.utenteSession}">
                <a href="${pageContext.request.contextPath}/checkout"
                   style="display: block; background: var(--primary-light); color: white; text-align: center;
          padding: 1rem; border-radius: 8px; text-decoration: none; font-size: 1.1rem; font-weight: 500;">
                  Procedi al Checkout
                </a>
              </c:when>
              <c:otherwise>
                <a href="${pageContext.request.contextPath}/pages/accediutente?redirect=checkout"
                   style="display: block; background: var(--primary-light); color: white; text-align: center;
                          padding: 1rem; border-radius: 8px; text-decoration: none; font-size: 1.1rem; font-weight: 500;">
                  Accedi per Continuare
                </a>
              </c:otherwise>
            </c:choose>

            <a href="${pageContext.request.contextPath}/pages"
               style="display: block; background: #f8f9fa; color: var(--primary-light); text-align: center;
                      padding: 1rem; border-radius: 8px; text-decoration: none; border: 1px solid #ddd;">
              Continua lo Shopping
            </a>
          </div>
        </div>
      </div>
    </c:otherwise>
  </c:choose>
</div>

<%@include file="../partials/site/footer.jsp"%>

<style>
  /* Responsive per mobile */
  @media (max-width: 768px) {
    div[style*="grid-template-columns"] {
      grid-template-columns: 1fr !important;
    }

    div[data-prodotto-id] {
      flex-direction: column !important;
      text-align: center;
    }

    div[data-prodotto-id] > div:last-child {
      text-align: center !important;
      margin-top: 1rem;
    }
  }

  /* Hover effects */
  div[data-prodotto-id]:hover {
    box-shadow: 0 4px 12px rgba(0,0,0,0.1) !important;
    transition: box-shadow 0.3s;
  }

  .remove-from-cart:hover {
    background: #c82333 !important;
  }

  .svuota-carrello:hover {
    background: #5a6268 !important;
  }
</style>

<script>
  document.addEventListener('DOMContentLoaded', function() {
    const checkoutBtn = document.getElementById('checkout-btn');
    if (checkoutBtn) {
      checkoutBtn.addEventListener('click', function() {
        window.location.href = '${pageContext.request.contextPath}/checkout';
      });
    }
  });
</script>
</body>
</html>