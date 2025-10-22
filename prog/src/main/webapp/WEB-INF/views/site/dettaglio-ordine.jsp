<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="it">
<head>
  <title>Dettaglio Ordine #${ordine.idOrdine} - TechVibe</title>
  <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
  <jsp:include page="/WEB-INF/views/partials/head.jsp">
    <jsp:param name="styles" value="site,ordine-dettaglio-site"/>
  </jsp:include>

</head>

<body>
<main class="app">
  <%@ include file="../partials/site/header.jsp" %>

  <div class="sezione-ordine">

    <!-- Pulsante Torna al Profilo -->
    <a href="${pageContext.request.contextPath}/utente/profile" class="link-torna-profilo">
      ← Torna al Profilo
    </a>

    <!-- Intestazione Ordine -->
    <div class="blocco-ordine">
      <h1 class="titolo-ordine">Ordine #${ordine.idOrdine}</h1>

      <div class="griglia-dettagli">
        <div>
          <div class="etichetta">Stato</div>
          <div class="valore-stato">${ordine.stato}</div>
        </div>
        <div>
          <div class="etichetta">Totale</div>
          <div class="valore-totale">
            <fmt:formatNumber value="${ordine.totale}" type="currency" currencySymbol="€" minFractionDigits="2"/>
          </div>
        </div>
        <div>
          <div class="etichetta">Metodo Pagamento</div>
          <div class="valore">
            <c:out value="${ordine.metodoDiPagamento}" default="Non specificato"/>
          </div>
        </div>
        <c:if test="${not empty ordine.metodoDiSpedizione}">
          <div>
            <div class="etichetta">Spedizione</div>
            <div class="valore">${ordine.metodoDiSpedizione}</div>
          </div>
        </c:if>
      </div>
    </div>

    <!-- Sezione Prodotti -->
    <div class="blocco-prodotti">
      <h3 class="titolo-sezione">Prodotti Ordinati</h3>

      <c:choose>
        <c:when test="${not empty ordine.carrello and not empty ordine.carrello.items}">
          <c:forEach var="item" items="${ordine.carrello.items}">
            <div class="scheda-prodotto">
              <!-- Immagine prodotto -->
              <div class="immagine-prodotto">
                <c:choose>
                  <c:when test="${not empty item.prodotto.immagine1}">
                    <img src="${pageContext.request.contextPath}/img/${item.prodotto.immagine1}"
                         alt="${item.prodotto.marca} ${item.prodotto.modello}">
                  </c:when>
                  <c:otherwise>
                    <img src="${pageContext.request.contextPath}/img/placeholder.png" alt="Prodotto">
                  </c:otherwise>
                </c:choose>
              </div>

              <!-- Dettagli prodotto -->
              <div class="dettagli-prodotto">
                <div class="nome-prodotto">
                    ${item.prodotto.marca} ${item.prodotto.modello}
                </div>
                <div class="info-prodotto">
                  <c:if test="${not empty item.prodotto.storage}">Storage: ${item.prodotto.storage}GB</c:if>
                  <c:if test="${not empty item.prodotto.colore}"> • Colore: ${item.prodotto.colore}</c:if>
                </div>
                <div class="riga-prezzo">
                  <span class="quantita">Quantità: ${item.quantita}</span>
                  <span class="totale-parziale">
                    <fmt:formatNumber value="${item.total()}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                  </span>
                </div>
              </div>
            </div>
          </c:forEach>
        </c:when>
        <c:otherwise>
          <div class="messaggio-vuoto">
            <p>Nessun prodotto trovato per questo ordine.</p>
          </div>
        </c:otherwise>
      </c:choose>
    </div>

  </div>

  <%@ include file="../partials/site/footer.jsp" %>
</main>
</body>
</html>
