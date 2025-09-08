<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="Gestione Prodotto"/>
        <jsp:param name="styles" value="crm,dashboard,azionicrm"/>
        <jsp:param name="scripts" value="crm,home"/>
    </jsp:include>

    

</head>
<body>

<main class="app">
    <%@ include file="../partials/crm/sidebar.jsp" %>
    <section class="content grid-y">
        <%@ include file="../partials/crm/header.jsp" %>
        <div class="body grid-x justify-center">
            <section class="grid-y cell w75">



                <!-- HERO SEMPLICE -->
                <header class="product-hero">
                    <h1>Prodotto #${prodotto.idProdotto}</h1>
                    <h2>${prodotto.marca} ${prodotto.modello}</h2>
                    <div class="availability-badge">
                        Disponibilità: <span id="qty">${prodotto.qtDisponibile}</span>
                    </div>
                </header>

                <!-- GRID INFO -->
                <div class="product-grid">
                    <!-- PREZZO -->
                    <div class="info-card">
                        <h4>Prezzo</h4>
                        <p class="price-display">
                            <fmt:formatNumber value="${prodotto.prezzo}" type="currency" currencySymbol="€" minFractionDigits="2" maxFractionDigits="2"/>
                        </p>
                    </div>

                    <!-- SPECIFICHE -->
                    <div class="info-card">
                        <h4>Specifiche</h4>
                        <ul class="specs-list">
                            <c:if test="${not empty prodotto.colore}">
                                <li><strong>Colore</strong><span>${prodotto.colore}</span></li>
                            </c:if>
                            <c:if test="${not empty prodotto.storage}">
                                <li><strong>Storage</strong><span>${prodotto.storage} GB</span></li>
                            </c:if>
                            <c:if test="${not empty prodotto.ram}">
                                <li><strong>RAM</strong><span>${prodotto.ram} GB</span></li>
                            </c:if>
                        </ul>
                    </div>


                </div>

                <!-- AZIONI -->
                <div class="actions-grid">
                    <a class="btn" href="${pageContext.request.contextPath}/prodotti/edit?id=${prodotto.idProdotto}">Modifica</a>
                    <a class="btn" href="${pageContext.request.contextPath}/prodotti/">Dettagli</a>
                    <button class="btn" type="button" onclick="confirmDelete()">Elimina</button>
                </div>

                <!-- RITORNO -->
                <div style="text-align:center;">
                    <a href="${pageContext.request.contextPath}/prodotti/" class="back-link">← Torna all’elenco</a>
                </div>

            </section>
        </div>
    </section>
</main>

<script>

    function confirmDelete() {
        if (confirm('Eliminare questo prodotto?')) {
            window.location.href = '${pageContext.request.contextPath}/prodotti/delete?id=${prodotto.idProdotto}';
        }
    }

    // init
    (function () {
        const initial = parseInt((qtDisplay.textContent || '0').trim(), 10);
        updateMinusState(initial);
    })();
</script>

</body>
</html>
