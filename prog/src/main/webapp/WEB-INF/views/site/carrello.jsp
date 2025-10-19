<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
  <title>Il tuo Carrello - TechVibe</title>
  <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
  <jsp:include page="../partials/head.jsp">
    <jsp:param name="title" value="Carrello"/>
    <jsp:param name="styles" value="site,carrello"/>
  </jsp:include>
</head>

<body>
<%@include file="../partials/site/header.jsp"%>

<div class="contenitore-carrello">
  <h1 class="titolo-carrello">Il tuo Carrello</h1>

  <c:choose>
    <c:when test="${empty carrello.items}">
      <!-- Carrello vuoto -->
      <div class="carrello-vuoto">
        <h2>Il tuo carrello è vuoto</h2>
        <p>Aggiungi alcuni prodotti per iniziare!</p>
        <a href="${pageContext.request.contextPath}/pages" class="btn-continua">
          Continua lo Shopping
        </a>
      </div>
    </c:when>
    <c:otherwise>
      <!-- Carrello con prodotti -->
      <div class="griglia-carrello">

        <!-- Lista prodotti -->
        <div>
          <c:forEach var="item" items="${carrello.items}">
            <div class="card-prodotto" data-prodotto-id="${item.prodotto.idProdotto}">

              <!-- Immagine prodotto -->
              <div>
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
                     class="img-prodotto">
              </div>

              <!-- Dettagli prodotto -->
              <div class="dettagli-prodotto">
                <h3>${item.prodotto.marca} ${item.prodotto.modello}</h3>

                <div class="info-prodotto">
                  <c:if test="${not empty item.prodotto.storage}">
                    <div>Storage: ${item.prodotto.storage}GB</div>
                  </c:if>
                  <c:if test="${not empty item.prodotto.colore}">
                    <div>Colore: ${item.prodotto.colore}</div>
                  </c:if>
                </div>

                <!-- Prezzo unitario -->
                <div class="prezzo-unitario">
                  <fmt:formatNumber value="${item.prodotto.prezzo}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                </div>

                <!-- Controlli quantità e rimozione -->
                <div class="controlli-prodotto">
                  <div>
                    <label class="etichetta-quantita">Quantità:</label>
                    <input type="number"
                           class="quantita-carrello"
                           data-prodotto-id="${item.prodotto.idProdotto}"
                           value="${item.quantita}"
                           min="1">
                  </div>

                  <button class="remove-from-cart" data-id="${item.prodotto.idProdotto}">
                    Rimuovi
                  </button>
                </div>
              </div>

              <!-- Totale item -->
              <div class="totale-item">
                <div class="totale-riga">
                  <fmt:formatNumber value="${item.total()}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                </div>
                <div class="dettaglio-riga">
                    ${item.quantita} x
                  <fmt:formatNumber value="${item.prodotto.prezzo}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                </div>
              </div>
            </div>
          </c:forEach>

          <!-- Pulsante svuota carrello -->
          <div class="sezione-svuota">
            <button class="svuota-carrello">
              Svuota Carrello
            </button>
          </div>
        </div>

        <!-- Riepilogo ordine -->
        <div class="riepilogo-ordine">

          <h3 class="riepilogo-titolo">Riepilogo Ordine</h3>

          <!-- Subtotale -->
          <div class="riga-subtotale">
            <span>Subtotale (${carrello.items.size()} prodotti):</span>
            <span id="carrello-totale">
              <fmt:formatNumber value="${carrello.total()}" type="currency" currencySymbol="€" minFractionDigits="2"/>
            </span>
          </div>

          <!-- Spedizione -->
          <div class="riga-spedizione">
            <span>Spedizione:</span>
            <span>Gratuita</span>
          </div>

          <!-- Totale finale -->
          <div class="riga-totale">
            <span>Totale:</span>
            <span>
              <fmt:formatNumber value="${carrello.total()}" type="currency" currencySymbol="€" minFractionDigits="2"/>
            </span>
          </div>

          <!-- Pulsanti azione -->
          <div class="azioni-carrello">
            <c:choose>
              <c:when test="${not empty sessionScope.utenteSession}">
                <a href="${pageContext.request.contextPath}/checkout" class="btn-primario">
                  Procedi al Checkout
                </a>
              </c:when>
              <c:otherwise>
                <a href="${pageContext.request.contextPath}/pages/accediutente?redirect=checkout" class="btn-primario">
                  Accedi per Continuare
                </a>
              </c:otherwise>
            </c:choose>

            <a href="${pageContext.request.contextPath}/pages" class="btn-secondario">
              Continua lo Shopping
            </a>
          </div>
        </div>
      </div>
    </c:otherwise>
  </c:choose>
</div>

<%@include file="../partials/site/footer.jsp"%>

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
