<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title> Home TechVibe</title>
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">

    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="TechVibe"/>
        <jsp:param name="styles" value="site,dashboard"/>
        <jsp:param name="scripts" value="crm,home"/>
    </jsp:include>

</head>

<body>

<%@include file="../partials/site/header.jsp"%>


<section style="background: white; color: var(--primary-light); text-align: center; padding: 3rem 2rem;">
    <h1 style="font-size: 2.5rem; margin: 0 0 1rem 0; font-weight: bold;">Benvenuto su TechVibe</h1>
    <p style="font-size: 1.2rem; margin: 0; opacity: 0.9;color:#666;">Il miglior e-commerce dedicato a smartphone e tablet, sempre a portata di click</p>
</section>



<!-- Vetrina Offerte -->
<c:if test="${not empty prodottiVetrina}">
    <section style="padding: 3rem 2rem; background:var(--white);">
        <div style="max-width: 1200px; margin: 0 auto; text-align: center;">
            <h2 style="color: var(--primary-light); font-size: 2rem; margin: 0 0 0.5rem 0;">Offerte del Momento</h2>
            <p style="color: #666; margin: 0 0 2rem 0; font-size: 1.1rem;">Prodotti selezionati a prezzi speciali</p>

            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 1.5rem; margin-bottom: 2rem;">
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

                    <div class="card product-card" style="background: white; border: 1px solid #eee; border-radius: 15px; padding: 1.25rem; text-align: center; transition: all 0.3s ease; box-shadow: 0 2px 8px rgba(0,0,0,0.05); position: relative;">
                        <!-- Badge sconto -->
                        <div style="position: absolute; top: 10px; right: 10px; background: var(--primary-light); color: white; padding: 4px 8px; border-radius: 8px; font-weight: 600; font-size: 0.8rem;">
                            -${p.percentualeSconto}%
                        </div>

                        <a href="${pageContext.request.contextPath}/pages/prodotto?id=${p.idProdotto}" style="text-decoration: none; color: inherit;">
                            <img src="${imgSrc}" alt="${p.marca} ${p.modello}" style="width: 100%; height: 150px; object-fit: cover; margin-bottom: 1rem; border-radius: 8px;">
                            <h3 style="margin: 0 0 0.5rem 0; font-size: 1.1rem; color: var(--primary-light);">${p.marca} ${p.modello}</h3>
                        </a>

                        <div class="details" style="margin: 0.75rem 0; border-radius: 10px; padding: 0.4rem 0; background-color: var(--primary-light);">
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

                        <button class="btn primary add-to-cart"
                                data-id="${p.idProdotto}" data-qty="1"
                                style="width: 100%;">
                            Aggiungi al carrello
                        </button>
                    </div>
                </c:forEach>
            </div>

            <a href="${pageContext.request.contextPath}/prodotti/offerte"
               style="display: inline-block; background: var(--primary-light); color: white; padding: 12px 30px; border-radius: 8px; text-decoration: none; font-weight: 600; font-size: 1.1rem; transition: all 0.3s;">
                Vedi Tutte le Offerte
            </a>
        </div>
    </section>
</c:if>

<!-- Sezione categorie -->
<section class="categorie">
    <h2 style="color: var(--primary-light)">Le nostre categorie</h2>

    <div class="grid">
        <a href="${pageContext.request.contextPath}/pages/smartphone"
           class="card-link">
            <div class="card">
                <p style="color: black">Smartphone</p>
            </div>
        </a>

        <a href="${pageContext.request.contextPath}/pages/tablet"
           class="card-link">
            <div class="card">
                <p style="color: black">Tablet</p>
            </div>
        </a>
    </div>
</section>

<%@include file="../partials/site/footer.jsp"%>

<style>
    /* Hover effect per le card offerte */
    .card.product-card:hover {
        transform: translateY(-8px);
        box-shadow: 0 8px 20px rgba(0,0,0,0.1);
        border-color: var(--primary-light);
    }

    /* Hover effect per il pulsante offerte */
    a[href*="offerte"]:hover {
        background: #187bcd !important;
    }

    /* Responsive per la vetrina offerte */
    @media (max-width: 768px) {
        section[style*="background: var(--light-gray)"] div[style*="grid-template-columns"] {
            grid-template-columns: 1fr !important;
            gap: 1rem !important;
        }
    }
</style>

</body>
</html>