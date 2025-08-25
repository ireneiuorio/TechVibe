<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="it">
<head>
    <title>Catalogo Smartphone</title>
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
    <jsp:include page="/WEB-INF/views/partials/head.jsp">
        <jsp:param name="styles" value="site"/>
    </jsp:include>

    <style>

        .card.product-card {
            background: white;
            border: 1px solid #eee;
            transition: transform 0.3s, box-shadow 0.3s;
            box-shadow: 0 2px 8px rgba(0,0,0,0.05);
        }

        .card.product-card:hover {
            transform: translateY(-8px);
            box-shadow: 0 8px 20px rgba(0,0,0,0.1);
        }

        .product-image {
            width: 100%;
            height: 200px;
            object-fit: contain;
            margin-bottom: 1rem;
            border-radius: 8px;
        }
    </style>
</head>
<body>
<main class="app">
    <%@ include file="../partials/site/header.jsp" %>

    <section class="categorie">
        <h2>Smartphone</h2>

        <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(240px,1fr));gap:1.5rem;margin-top:1rem;">

            <c:forEach var="p" items="${prodotti}">
                <c:set var="imgRaw" value="${p.immagine1}" />

                <!-- LOGICA SEMPLIFICATA PER SOLO IMMAGINE1 -->
                <c:choose>
                    <c:when test="${not empty imgRaw}">
                        <c:set var="imgSrc" value="${pageContext.request.contextPath}/img/${imgRaw}" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="imgSrc" value="${pageContext.request.contextPath}/img/placeholder.png" />
                    </c:otherwise>
                </c:choose>

                <!-- USA LE TUE CLASSI ESISTENTI -->
                <div class="card product-card" style="text-align:center;padding:1.25rem;">
                    <a href="${pageContext.request.contextPath}/pages/prodotto?id=${p.idProdotto}">
                        <img src="${imgSrc}" alt="${p.marca} ${p.modello}" class="product-image">
                        <h3 style="margin:0;font-size:1.2rem;color:var(--primary-light);">
                                ${p.marca} ${p.modello}
                        </h3>
                    </a>

                    <!-- USA LA TUA CLASSE .details ESISTENTE -->
                    <div class="details" style="margin:.75rem 0;border-radius:10px;padding:.4rem 0;">
                        <fmt:formatNumber value="${p.prezzo}" type="currency" currencySymbol="€"
                                          minFractionDigits="2" maxFractionDigits="2"/>
                    </div>

                    <!-- USA LA TUA CLASSE .btn.primary ESISTENTE -->
                    <button class="btn primary add-to-cart"
                            data-id="${p.idProdotto}" data-qty="1"
                            style="width:100%;">
                        Aggiungi al carrello
                    </button>
                </div>
            </c:forEach>

            <c:if test="${empty prodotti}">
                <div class="card" style="text-align:center;">Nessuno smartphone trovato.</div>
            </c:if>

        </div>
    </section>

    <%@ include file="../partials/site/footer.jsp" %>
</main>
</body>
</html>