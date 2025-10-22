<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="it">
<head>
    <title>Home TechVibe</title>
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">

    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="TechVibe"/>
        <jsp:param name="styles" value="site,dashboard,home-techvibe"/>
        <jsp:param name="scripts" value="crm,home"/>
    </jsp:include>

</head>

<body>

<%@ include file="../partials/site/header.jsp" %>

<!-- Hero -->
<section class="hero-home">
    <h1 class="hero-titolo">Benvenuto su TechVibe</h1>
    <p class="hero-sottotitolo">Il miglior e-commerce dedicato a smartphone e tablet, sempre a portata di click</p>
</section>

<!-- Vetrina Offerte -->
<c:if test="${not empty prodottiVetrina}">
    <section class="sezione-offerte">
        <div class="contenitore-centrato">
            <h2 class="offerte-titolo">Offerte del Momento</h2>
            <p class="offerte-sottotitolo">Prodotti selezionati a prezzi speciali</p>

            <div class="griglia-prodotti">
                <c:forEach var="p" items="${prodottiVetrina}">
                    <c:set var="imgRaw" value="${p.immagine1}" />
                    <c:choose>
                        <c:when test="${not empty imgRaw}">
                            <c:set var="imgSrc" value="${pageContext.request.contextPath}/img/${imgRaw}" />
                        </c:when>
                        <c:otherwise>
                            <c:set var="imgSrc" value="${pageContext.request.contextPath}/img/placeholder.png" />
                        </c:otherwise>
                    </c:choose>

                    <div class="card card-prodotto">
                        <!-- Badge sconto -->
                        <div class="badge-sconto">-${p.percentualeSconto}%</div>

                        <a href="${pageContext.request.contextPath}/pages/prodotto?id=${p.idProdotto}" class="link-prodotto">
                            <img src="${imgSrc}" alt="${p.marca} ${p.modello}" class="immagine-prodotto">
                            <h3 class="nome-prodotto">${p.marca} ${p.modello}</h3>
                        </a>

                        <div class="dettagli-prezzo">
                            <!-- Storage info -->
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

                            <!-- Prezzi -->
                            <div class="prezzo-originale">
                                €<fmt:formatNumber value="${p.prezzoOriginale}" type="number" minFractionDigits="2"/>
                            </div>

                            <div class="prezzo-finale">
                                €<fmt:formatNumber value="${p.prezzoFinale}" type="number" minFractionDigits="2"/>
                            </div>
                        </div>

                        <button class="btn primary add-to-cart btn-full"
                                data-id="${p.idProdotto}" data-qty="1">
                            Aggiungi al carrello
                        </button>
                    </div>
                </c:forEach>
            </div>

            <a href="${pageContext.request.contextPath}/prodotti/offerte" class="btn-cta-offerte">
                Vedi Tutte le Offerte
            </a>
        </div>
    </section>
</c:if>

<!-- Sezione categorie -->
<section class="sezione-categorie categorie">
    <h2 class="titolo-categorie">Le nostre categorie</h2>

    <div class="griglia-categorie">
        <a href="${pageContext.request.contextPath}/pages/smartphone" class="card-link">
            <div class="card-categoria">
                <p>Smartphone</p>
            </div>
        </a>

        <a href="${pageContext.request.contextPath}/pages/tablet" class="card-link">
            <div class="card-categoria">
                <p>Tablet</p>
            </div>
        </a>
    </div>
</section>

<%@ include file="../partials/site/footer.jsp" %>

</body>
</html>
