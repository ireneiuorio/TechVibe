<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="it">
<head>
  <title>
    <c:out value="${prodotto.marca}"/> <c:out value="${prodotto.modello}"/> - TechVibe
  </title>
  <jsp:include page="../partials/head.jsp">
    <jsp:param name="title" value="Prodotto"/>
    <jsp:param name="styles" value="site,privacy"/>
  </jsp:include>
</head>
<body>
<main class="app">
  <%@ include file="../partials/site/header.jsp" %>

  <c:choose>
    <c:when test="${not empty prodotto}">
      <section class="privacy container">

        <!-- Scheda prodotto -->
        <article class="card privacy"
                 style="display:grid;grid-template-columns:1fr 1.2fr;gap:1rem;align-items:start;">

          <!-- Colonna immagini -->
          <div>
            <!-- Immagine principale -->
            <div id="viewer" style="border-radius:12px;overflow:hidden;">
              <c:choose>
                <c:when test="${not empty prodotto.cover}">
                  <img id="mainImg" src="${prodotto.cover}"
                       alt="${prodotto.marca} ${prodotto.modello}"
                       style="width:100%;display:block;">
                </c:when>
                <c:otherwise>
                  <div style="width:100%;height:260px;background:#eee;display:flex;align-items:center;justify-content:center;border-radius:12px;">
                    <span style="color:#888;">Nessuna immagine</span>
                  </div>
                </c:otherwise>
              </c:choose>
            </div>

            <!-- Thumbnails -->
            <div style="display:grid;grid-template-columns:repeat(4,1fr);gap:.5rem;margin-top:.75rem;">
              <c:if test="${not empty prodotto.cover}">
                <img class="thumb" src="${prodotto.cover}" alt="cover"
                     style="width:100%;border-radius:10px;cursor:pointer;">
              </c:if>
              <c:if test="${not empty prodotto.immagine1}">
                <img class="thumb" src="${prodotto.immagine1}" alt="immagine1"
                     style="width:100%;border-radius:10px;cursor:pointer;">
              </c:if>
              <c:if test="${not empty prodotto.immagine2}">
                <img class="thumb" src="${prodotto.immagine2}" alt="immagine2"
                     style="width:100%;border-radius:10px;cursor:pointer;">
              </c:if>
              <c:if test="${not empty prodotto.immagine3}">
                <img class="thumb" src="${prodotto.immagine3}" alt="immagine3"
                     style="width:100%;border-radius:10px;cursor:pointer;">
              </c:if>
            </div>
          </div>

          <!-- Colonna dati -->
          <div>
            <h1 style="margin:0 0 .5rem 0;">
                ${prodotto.marca} ${prodotto.modello}
            </h1>

            <!-- Prezzo -->
            <c:if test="${prodotto.prezzo != null}">
              <p style="font-size:1.4rem;margin:.25rem 0 .75rem 0;">
                € <fmt:formatNumber value="${prodotto.prezzo}" type="number" minFractionDigits="2"/>
              </p>
            </c:if>

            <!-- Stato disponibilità -->
            <p style="margin:.25rem 0;">
              <strong>Disponibilità:</strong>
              <c:choose>
                <c:when test="${prodotto.qtDisponibile > 0}">
                  ${prodotto.qtDisponibile} pezzi
                </c:when>
                <c:otherwise>Non disponibile</c:otherwise>
              </c:choose>
            </p>

            <!-- Specifiche principali -->
            <div style="display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:.5rem;margin:1rem 0;">
              <p><strong>Colore:</strong> ${prodotto.colore}</p>
              <p><strong>Storage:</strong> ${prodotto.storage} GB</p>
              <p><strong>RAM:</strong> ${prodotto.ram} GB</p>
              <p><strong>Schermo:</strong> ${prodotto.dimensioneSchermo}"</p>
              <p><strong>Connettività:</strong> ${prodotto.connettivita}</p>
              <p><strong>SO:</strong> ${prodotto.sistemaOperativo}</p>
            </div>

            <!-- Aggiungi al carrello -->
            <form action="${pageContext.request.contextPath}/cart/add" method="post"
                  style="display:flex;gap:.5rem;align-items:center;margin:.5rem 0 0 0;">
              <input type="hidden" name="idProdotto" value="${prodotto.idProdotto}">
              <label>Qt
                <input type="number" name="qty" value="1" min="1"
                       <c:if test="${prodotto.qtDisponibile > 0}">max="${prodotto.qtDisponibile}"</c:if>
                       style="width:80px;margin-left:.25rem;">
              </label>
              <button type="submit" class="btn primary"
                      <c:if test="${prodotto.qtDisponibile == 0}">disabled</c:if>>
                Aggiungi al carrello
              </button>
            </form>
          </div>
        </article>
      </section>
    </c:when>

    <!-- Se per qualche motivo arrivi qui senza prodotto in request -->
    <c:otherwise>
      <section class="privacy container">
        <div class="card privacy">
          <h2>Prodotto non trovato</h2>
          <p>Il prodotto richiesto non è disponibile o è stato rimosso.</p>
        </div>
      </section>
    </c:otherwise>
  </c:choose>

  <%@ include file="../partials/site/footer.jsp" %>
</main>

<!-- JS vanilla per cambiare l'immagine principale cliccando sulle thumbnail -->
<script>
  (function () {
    var main = document.getElementById('mainImg');
    if (!main) return;
    document.querySelectorAll('.thumb').forEach(function (t) {
      t.addEventListener('click', function () {
        main.src = this.src;
        main.alt = this.alt;
      });
    });
  })();
</script>
</body>
</html>
