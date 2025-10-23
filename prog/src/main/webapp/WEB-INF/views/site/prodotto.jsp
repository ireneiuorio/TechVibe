
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="it">
<head>
    <title>${prodotto.marca} ${prodotto.modello} - TechVibe</title>
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
    <jsp:include page="/WEB-INF/views/partials/head.jsp">
        <jsp:param name="styles" value="dashboard,site,prodotto-dettaglio-techvibe"/>
    </jsp:include>
    <!-- nuovo css della pagina -->

</head>
<body>
<main class="app">
    <%@ include file="../partials/site/header.jsp" %>

    <div class="container">

        <!-- SEZIONE IMMAGINI -->
        <div class="immagini">
            <img src="${pageContext.request.contextPath}/img/${not empty prodotto.immagine1 ? prodotto.immagine1 : 'placeholder.png'}"
                 alt="${prodotto.marca} ${prodotto.modello}"
                 class="img-grande"
                 id="img-grande">

            <!-- GALLERIA MINIATURE -->
            <div class="img-piccole">
                <c:if test="${not empty prodotto.immagine1}">
                    <img src="${pageContext.request.contextPath}/img/${prodotto.immagine1}"
                         class="mini attiva"
                         onclick="cambiaImmagine(this.src)">
                </c:if>
                <c:if test="${not empty prodotto.immagine2}">
                    <img src="${pageContext.request.contextPath}/img/${prodotto.immagine2}"
                         class="mini"
                         onclick="cambiaImmagine(this.src)">
                </c:if>
                <c:if test="${not empty prodotto.immagine3}">
                    <img src="${pageContext.request.contextPath}/img/${prodotto.immagine3}"
                         class="mini"
                         onclick="cambiaImmagine(this.src)">
                </c:if>
                <c:if test="${not empty prodotto.immagine4}">
                    <img src="${pageContext.request.contextPath}/img/${prodotto.immagine4}"
                         class="mini"
                         onclick="cambiaImmagine(this.src)">
                </c:if>
            </div>
        </div>

        <!-- SEZIONE DETTAGLI -->
        <div class="info">
            <h1 class="titolo">${prodotto.modello}</h1>
            <div class="marca">di <strong>${prodotto.marca}</strong></div>

            <!-- SEZIONE PREZZO CON SCONTO -->
            <div class="prezzo">
                <c:choose>
                    <c:when test="${prodotto.inSconto}">
                        <div class="box-prezzo">
                            <div class="prezzo-originale">
                                <fmt:formatNumber value="${prodotto.prezzoOriginale}" type="currency" currencySymbol="€"
                                                  minFractionDigits="2" maxFractionDigits="2"/>
                            </div>
                            <div class="prezzo-scontato">
                                <fmt:formatNumber value="${prodotto.prezzoFinale}" type="currency" currencySymbol="€"
                                                  minFractionDigits="2" maxFractionDigits="2"/>
                            </div>
                            <div class="riga-sconto">
                                <span class="badge-sconto">-${prodotto.percentualeSconto}% di sconto</span>
                                <span class="importo-risparmio">
                                    Risparmi <fmt:formatNumber value="${prodotto.importoRisparmiato}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                                </span>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="prezzo-scontato">
                            <fmt:formatNumber value="${prodotto.prezzo}" type="currency" currencySymbol="€"
                                              minFractionDigits="2" maxFractionDigits="2"/>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- STATUS DISPONIBILITÀ -->
            <div class="disponibilita ${prodotto.qtDisponibile > 10 ? 'disponibile' : prodotto.qtDisponibile > 0 ? 'pochi' : 'esaurito'}">
                <c:choose>
                    <c:when test="${prodotto.qtDisponibile > 10}">
                        Disponibile (${prodotto.qtDisponibile} pezzi)
                    </c:when>
                    <c:when test="${prodotto.qtDisponibile > 0}">
                        Pochi pezzi rimasti (${prodotto.qtDisponibile})
                    </c:when>
                    <c:otherwise>
                        Non disponibile
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- SPECIFICHE TECNICHE -->
            <div class="caratteristiche">
                <h3>Caratteristiche:</h3>
                <div class="lista-caratteristiche">
                    <div class="caratteristica">
                        <span class="nome-caratteristica">Sistema:</span>
                        <span class="valore-caratteristica">${prodotto.sistemaOperativo}</span>
                    </div>
                    <div class="caratteristica">
                        <span class="nome-caratteristica">Schermo:</span>
                        <span class="valore-caratteristica">${prodotto.dimensioneSchermo}"</span>
                    </div>
                    <div class="caratteristica">
                        <span class="nome-caratteristica">RAM:</span>
                        <span class="valore-caratteristica">${prodotto.ram} GB</span>
                    </div>
                    <div class="caratteristica">
                        <span class="nome-caratteristica">Memoria:</span>
                        <span class="valore-caratteristica">${prodotto.storage} GB</span>
                    </div>
                    <div class="caratteristica">
                        <span class="nome-caratteristica">Colore:</span>
                        <span class="valore-caratteristica">${prodotto.colore}</span>
                    </div>
                    <div class="caratteristica">
                        <span class="nome-caratteristica">Rete:</span>
                        <span class="valore-caratteristica">${prodotto.connettivita}</span>
                    </div>
                </div>
            </div>

            <!-- SEZIONE ACQUISTO -->
            <div class="box-acquisto">
                <div class="quantita">
                    <label for="quantity">Quantità:</label>
                    <input type="number" id="quantity" name="quantita" value="1" min="1" max="${prodotto.qtDisponibile}" class="input-quantita">
                </div>


                <c:choose>
                    <c:when test="${prodotto.qtDisponibile > 0}">
                        <button type="button"
                                class="btn-carrello add-to-cart"
                                data-id="${prodotto.idProdotto}"
                                data-qty="1"
                                onclick="aggiungiAlCarrelloConQuantita(${prodotto.idProdotto})">
                            Aggiungi al Carrello
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button type="button" class="btn-carrello btn-disabilitato" disabled>
                            Non Disponibile
                        </button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <%@ include file="../partials/site/footer.jsp" %>
</main>

<script>
    // Cambia immagine grande
    function cambiaImmagine(src) {
        const grande = document.getElementById('img-grande');
        if (!grande) return;
        grande.src = src;

        // Aggiorna stato "attiva"
        document.querySelectorAll('.mini').forEach(m => m.classList.remove('attiva'));
        document.querySelectorAll('.mini').forEach(m => { if (m.src === src) m.classList.add('attiva'); });
    }

    // Aggiungi al carrello con quantità
    function aggiungiAlCarrelloConQuantita(prodottoId) {
        const quantita = parseInt(document.getElementById('quantity').value) || 1;
        const btn = document.querySelector('.add-to-cart');
        if (btn) btn.setAttribute('data-qty', quantita);
        // funzione globale del tuo progetto
        if (typeof aggiungiAlCarrello === 'function') {
            aggiungiAlCarrello(prodottoId, quantita);
        }
    }

    // Sync data-qty quando cambia la quantità
    (function () {
        const q = document.getElementById('quantity');
        if (!q) return;
        q.addEventListener('change', function () {
            const btn = document.querySelector('.add-to-cart');
            if (btn) btn.setAttribute('data-qty', this.value);
        });
    })();
</script>

</body>
</html>
