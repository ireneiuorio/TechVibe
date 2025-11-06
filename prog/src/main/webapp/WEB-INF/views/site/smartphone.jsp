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


</head><!--PageServlet-->
<body>
<main class="app">
    <%@ include file="../partials/site/header.jsp" %>

    <div class="catalog-container">

        <!-- TOGGLE MOBILE FILTRI -->
        <button class="mobile-filter-toggle" onclick="toggleMobileFiltri()">
            Mostra/Nascondi Filtri
        </button>

        <!-- SIDEBAR FILTRI -->
        <aside class="filtri-sidebar" id="filtri-sidebar">
            <h3 class="filtri-header">Filtra Smartphone</h3>

            <form method="GET" action="${pageContext.request.contextPath}/prodotti/searchsmartphone">

                <input type="hidden" name="IdCategoria" value="1">
                <!-- MARCA -->
                <div class="filtro-gruppo">
                    <label for="marca">Marca</label>
                    <select name="Marca" id="marca">
                        <option value="">Tutte le marche</option>
                        <option value="Apple" ${param.Marca == 'Apple' ? 'selected' : ''}>Apple</option>
                        <option value="Samsung" ${param.Marca == 'Samsung' ? 'selected' : ''}>Samsung</option>
                        <option value="Xiaomi" ${param.Marca == 'Xiaomi' ? 'selected' : ''}>Xiaomi</option>
                        <option value="Huawei" ${param.Marca == 'Oppo' ? 'selected' : ''}>Huawei</option>
                        <option value="Google" ${param.Marca == 'Google' ? 'selected' : ''}>Google</option>
                    </select>
                </div>

                <!-- MODELLO -->
                <div class="filtro-gruppo">
                    <label for="modello">Modello</label>
                    <input type="text" name="Modello" id="modello"
                           placeholder="es: iPhone, Galaxy..."
                           value="${param.Modello}">
                </div>

                <!-- SISTEMA OPERATIVO -->
                <div class="filtro-gruppo">
                    <label for="so">Sistema Operativo</label>
                    <select name="SistemaOperativo" id="so">
                        <option value="">Tutti i sistemi</option>
                        <option value="iOS" ${param.SistemaOperativo == 'iOS' ? 'selected' : ''}>iOS</option>
                        <option value="Android" ${param.SistemaOperativo == 'Android' ? 'selected' : ''}>Android</option>
                    </select>
                </div>

                <!-- PREZZO -->
                <div class="filtro-gruppo">
                    <label>Prezzo (€)</label>
                    <div class="prezzo-range">
                        <input type="number" name="minPrice" placeholder="Min"
                               value="${param.minPrice}" min="0">
                        <span>-</span>
                        <input type="number" name="maxPrice" placeholder="Max"
                               value="${param.maxPrice}" min="0">
                    </div>
                </div>

                <!-- RAM -->
                <div class="filtro-gruppo">
                    <label for="ram">RAM Minima (GB)</label>
                    <select name="Ram" id="ram">
                        <option value="">Qualsiasi RAM</option>
                        <option value="4" ${param.Ram == '4' ? 'selected' : ''}>4 GB+</option>
                        <option value="6" ${param.Ram == '6' ? 'selected' : ''}>6 GB+</option>
                        <option value="8" ${param.Ram == '8' ? 'selected' : ''}>8 GB+</option>
                        <option value="12" ${param.Ram == '12' ? 'selected' : ''}>12 GB+</option>
                        <option value="16" ${param.Ram == '16' ? 'selected' : ''}>16 GB+</option>
                    </select>
                </div>

                <!-- STORAGE -->
                <div class="filtro-gruppo">
                    <label for="storage">Storage Minimo (GB)</label>
                    <select name="StorageDispositivo" id="storage">
                        <option value="">Qualsiasi Storage</option>
                        <option value="64" ${param.StorageDispositivo == '64' ? 'selected' : ''}>64 GB+</option>
                        <option value="128" ${param.StorageDispositivo == '128' ? 'selected' : ''}>128 GB+</option>
                        <option value="256" ${param.StorageDispositivo == '256' ? 'selected' : ''}>256 GB+</option>
                        <option value="512" ${param.StorageDispositivo == '512' ? 'selected' : ''}>512 GB+</option>
                        <option value="1024" ${param.StorageDispositivo == '1024' ? 'selected' : ''}>1 TB+</option>
                    </select>
                </div>

                <!-- COLORE -->
                <div class="filtro-gruppo">
                    <label for="colore">Colore</label>
                    <input type="text" name="Colore" id="colore"
                           placeholder="es: Nero..."
                           value="${param.colore}">
                </div>

                <!-- BOTTONI AZIONE -->
                <div class="filtri-actions">
                    <button type="submit" class="btn-cerca"> Cerca</button>
                </div>
            </form>
        </aside>

        <!-- AREA PRODOTTI -->
        <section class="prodotti-area">
            <div class="prodotti-header">
                <h2>Risultati ricerca</h2>
                <div class="prodotti-count">
                    <c:choose>
                        <c:when test="${not empty prodotti}">
                            ${fn:length(prodotti)} prodotti trovati
                        </c:when>
                        <c:otherwise>
                            Nessun prodotto trovato
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="prodotti-grid">
                <c:forEach var="p" items="${prodotti}">
                    <c:set var="imgRaw" value="${p.immagine1}" />

                    <c:choose>
                        <c:when test="${not empty imgRaw}">
                            <c:set var="imgSrc" value="${pageContext.request.contextPath}/img/${imgRaw}" />
                        </c:when>
                        <c:otherwise>
                            <c:set var="imgSrc" value="${pageContext.request.contextPath}/img/placeholder.png" />
                        </c:otherwise>
                    </c:choose>

                    <div class="card product-card">
                        <a href="${pageContext.request.contextPath}/pages/prodotto?id=${p.idProdotto}">
                            <img src="${imgSrc}" alt="${p.marca} ${p.modello}" class="product-image">
                            <h3 style="margin:0;font-size:1.1rem;color:var(--primary-light);">
                                    ${p.marca} ${p.modello}
                            </h3>
                        </a>

                        <div class="details" style="margin:.75rem 0;border-radius:10px;padding:.4rem 0;">
                            <!-- Storage info - piccolo e discreto -->
                            <div style="margin-bottom:.4rem;font-size:0.75rem;color:white;opacity:0.8;">
                                <c:choose>
                                    <c:when test="${not empty p.storage}">
                                        ${p.storage}GB
                                    </c:when>
                                    <c:otherwise>
                                        Storage N/A
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <!-- Sezione Prezzo con Sconto -->
                            <c:choose>
                                <c:when test="${p.inSconto}">
                                    <!-- Prodotto in sconto -->
                                    <div>
                                        <!-- Prezzo originale barrato -->
                                        <div style="font-size:0.9rem;text-decoration:line-through;color:var(--white);margin-bottom:2px;">
                                            €<fmt:formatNumber value="${p.prezzoOriginale}" type="number" minFractionDigits="2"/>
                                        </div>
                                        <!-- Prezzo scontato -->
                                        <div style="font-size:1.2rem;font-weight:600;color:var(--white);">
                                            €<fmt:formatNumber value="${p.prezzoFinale}" type="number" minFractionDigits="2"/>
                                        </div>
                                        <!-- Badge sconto -->
                                        <div style="margin-top:4px;">
                                            <span style="background:var(--primary-light);color:white;padding:2px 6px;border-radius:3px;font-size:0.7rem;font-weight:bold;">
                                                -${p.percentualeSconto}%
                                            </span>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <!-- Prezzo normale -->
                                    <div style="font-size:1.2rem;font-weight:600;color:white;">
                                        <fmt:formatNumber value="${p.prezzo}" type="currency" currencySymbol="€"
                                                          minFractionDigits="2" maxFractionDigits="2"/>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <button class="btn primary add-to-cart"
                                data-id="${p.idProdotto}" data-qty="1"
                                style="width:100%;">
                            Aggiungi al carrello
                        </button>
                    </div>
                </c:forEach>

                <c:if test="${empty prodotti}">
                    <div class="card" style="grid-column: 1 / -1; text-align:center; padding: 3rem;">
                        <h3>Nessuno smartphone trovato</h3>
                        <p>Prova a modificare i filtri di ricerca!</p>
                    </div>
                </c:if>
            </div>
        </section>
    </div>

    <%@ include file="../partials/site/footer.jsp" %>
</main>

<script>
    //Apre e chiude il menù filtri su Mobile
    function toggleMobileFiltri() {
        const sidebar = document.getElementById('filtri-sidebar');
        sidebar.classList.toggle('show'); //Se una classe non c'è la aggiunge se c'è la rimuove
    }

    // Chiudi filtri mobile quando si clicca fuori
    //document e non button quindi click ovunque nella pagina
    document.addEventListener('click', function(event) {
        const sidebar = document.getElementById('filtri-sidebar');
        const toggle = document.querySelector('.mobile-filter-toggle');

        if (window.innerWidth <= 768) {
            //Controlliamo se l''elemnto cliccato è dentro alla sidebar perchè se è dentro non vogliamo che si chiuda
            if (!sidebar.contains(event.target) && !toggle.contains(event.target)) {
                sidebar.classList.remove('show');
            }
        }
    });
</script>

</body>
</html>