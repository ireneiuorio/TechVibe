<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Modifica Prodotto</title>
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">

    <

    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="Dettaglio Prodotto"/>
        <jsp:param name="styles" value="crm,dashboard,manage-prodotto"/>
        <jsp:param name="scripts" value="crm"/>
    </jsp:include>
</head>
<body>

<main class="app">
    <%@include file="../partials/crm/sidebar.jsp"%>
    <section class="content grid-y">
        <%@include file="../partials/crm/header.jsp"%>
        <div class="body grid-x justify-center">

            <section class="grid-y cell w75 prodotto-container">

                <jsp:include page="../partials/site/alert.jsp" />

                <!-- Header Prodotto -->
                <div class="prodotto-header">
                    <div class="prodotto-info">
                        <h2>${prodotto.marca} ${prodotto.modello}</h2>
                        <p class="muted-meta">ID: ${prodotto.idProdotto} | ${prodotto.categoria.nomeCategoria}</p>
                    </div>
                    <div>
                        <span class="prodotto-status ${prodotto.qtDisponibile > 0 ? 'status-disponibile' : 'status-esaurito'}">
                            ${prodotto.qtDisponibile > 0 ? 'DISPONIBILE' : 'ESAURITO'}
                        </span>
                        <c:if test="${prodotto.percentualeSconto > 0}">
                            <span class="prodotto-status status-sconto">
                                SCONTO ${prodotto.percentualeSconto}%
                            </span>
                        </c:if>
                    </div>
                </div>

                <!-- Tabs -->
                <div class="prodotto-tabs">
                    <button class="tab-button active" onclick="showTab(event,'info')">Informazioni</button>
                    <button class="tab-button" onclick="showTab(event,'tech')">Specifiche Tecniche</button>
                    <button class="tab-button" onclick="showTab(event,'edit')">Modifica Prodotto</button>
                </div>

                <!-- Tab Informazioni -->
                <div id="tab-info" class="tab-content active">
                    <div class="info-grid">
                        <div class="info-card">
                            <h3 class="section-title">Dati Prodotto</h3>

                            <div class="info-item">
                                <div class="info-label">Marca e Modello</div>
                                <div class="info-value">${prodotto.marca} ${prodotto.modello}</div>
                            </div>

                            <div class="info-item">
                                <div class="info-label">Quantità Disponibile</div>
                                <div class="info-value">${prodotto.qtDisponibile} pezzi</div>
                            </div>
                        </div>

                        <div class="info-card">
                            <h3 class="section-title">Prezzo</h3>

                            <div class="info-item">
                                <div class="info-label">Prezzo Finale</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${prodotto.percentualeSconto > 0}">
                                            <span class="prezzo-scontato">
                                                <fmt:formatNumber value="${prodotto.prezzo * (1 - prodotto.percentualeSconto/100)}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                                            </span>
                                            <span class="prezzo-originale">
                                                <fmt:formatNumber value="${prodotto.prezzo}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:formatNumber value="${prodotto.prezzo}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <div class="info-item">
                                <div class="info-label">Sconto Applicato</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${prodotto.percentualeSconto > 0}">
                                            ${prodotto.percentualeSconto}%
                                        </c:when>
                                        <c:otherwise>
                                            Nessuno
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Tab Specifiche Tecniche -->
                <div id="tab-tech" class="tab-content">
                    <div class="info-grid">
                        <div class="info-card">
                            <h3 class="section-title">Hardware</h3>

                            <div class="info-item">
                                <div class="info-label">RAM</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${prodotto.ram > 0}">
                                            ${prodotto.ram} GB
                                        </c:when>
                                        <c:otherwise>
                                            <em class="valore-non-specificato">Non specificato</em>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <div class="info-item">
                                <div class="info-label">Storage</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${prodotto.storage > 0}">
                                            ${prodotto.storage} GB
                                        </c:when>
                                        <c:otherwise>
                                            <em class="valore-non-specificato">Non specificato</em>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <div class="info-item">
                                <div class="info-label">Dimensione Schermo</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${prodotto.dimensioneSchermo > 0}">
                                            ${prodotto.dimensioneSchermo}" pollici
                                        </c:when>
                                        <c:otherwise>
                                            <em class="valore-non-specificato">Non specificato</em>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>

                        <div class="info-card">
                            <h3 class="section-title">Design e Connettività</h3>

                            <div class="info-item">
                                <div class="info-label">Colore</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${not empty prodotto.colore}">
                                            ${prodotto.colore}
                                        </c:when>
                                        <c:otherwise>
                                            <em class="valore-non-specificato">Non specificato</em>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <div class="info-item">
                                <div class="info-label">Sistema Operativo</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${not empty prodotto.sistemaOperativo}">
                                            ${prodotto.sistemaOperativo}
                                        </c:when>
                                        <c:otherwise>
                                            <em class="valore-non-specificato">Non specificato</em>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <div class="info-item">
                                <div class="info-label">Connettività</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${not empty prodotto.connettivita}">
                                            ${prodotto.connettivita}
                                        </c:when>
                                        <c:otherwise>
                                            <em class="valore-non-specificato">Non specificato</em>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Tab Modifica Prodotto -->
                <div id="tab-edit" class="tab-content">
                    <div class="info-card">
                        <h3 class="section-title">Modifica Prodotto</h3>

                        <form method="POST" action="${pageContext.request.contextPath}/prodotti/update" class="edit-form">
                            <input type="hidden" name="id" value="${prodotto.idProdotto}">

                            <div class="grid-2">
                                <div class="form-group">
                                    <label for="modello">Modello</label>
                                    <input type="text" id="modello" name="modello" value="${prodotto.modello}" required>
                                </div>

                                <div class="form-group">
                                    <label for="marca">Marca</label>
                                    <input type="text" id="marca" name="marca" value="${prodotto.marca}" required>
                                </div>
                            </div>

                            <div class="grid-2">
                                <div class="form-group">
                                    <label for="prezzo">Prezzo</label>
                                    <input type="number" step="0.01" id="prezzo" name="prezzo" value="${prodotto.prezzo}" required>
                                </div>

                                <div class="form-group">
                                    <label for="qtDisponibile">Quantità</label>
                                    <input type="number" id="qtDisponibile" name="qtDisponibile" value="${prodotto.qtDisponibile}" required>
                                </div>
                            </div>

                            <div class="grid-2">
                                <div class="form-group">
                                    <label for="percentualeSconto">Sconto %</label>
                                    <input type="number" step="0.01" id="percentualeSconto" name="percentualeSconto" value="${prodotto.percentualeSconto}">
                                </div>

                                <div class="form-group">
                                    <label for="idCategoria">Categoria</label>
                                    <select id="idCategoria" name="idCategoria" required>
                                        <option value="1" ${prodotto.categoria.idCategoria == 1 ? 'selected' : ''}>Smartphone</option>
                                        <option value="2" ${prodotto.categoria.idCategoria == 2 ? 'selected' : ''}>Tablet</option>
                                    </select>
                                </div>
                            </div>

                            <div class="grid-2">
                                <div class="form-group">
                                    <label for="colore">Colore</label>
                                    <input type="text" id="colore" name="colore" value="${prodotto.colore}">
                                </div>

                                <div class="form-group">
                                    <label for="sistemaOperativo">Sistema Operativo</label>
                                    <input type="text" id="sistemaOperativo" name="sistemaOperativo" value="${prodotto.sistemaOperativo}">
                                </div>
                            </div>

                            <div class="grid-2">
                                <div class="form-group">
                                    <label for="ram">RAM (GB)</label>
                                    <input type="number" id="ram" name="ram" value="${prodotto.ram}">
                                </div>

                                <div class="form-group">
                                    <label for="storage">Storage (GB)</label>
                                    <input type="number" id="storage" name="storage" value="${prodotto.storage}">
                                </div>
                            </div>

                            <div class="grid-2">
                                <div class="form-group">
                                    <label for="dimensioneSchermo">Schermo (pollici)</label>
                                    <input type="number" step="0.1" id="dimensioneSchermo" name="dimensioneSchermo" value="${prodotto.dimensioneSchermo}">
                                </div>

                                <div class="form-group">
                                    <label for="connettivita">Connettività</label>
                                    <input type="text" id="connettivita" name="connettivita" value="${prodotto.connettivita}">
                                </div>
                            </div>

                            <button type="submit" class="btn primary">Salva Modifiche</button>
                            <button class="btn btn-danger" type="button" onclick="confirmDelete()">
                                Elimina Prodotto
                            </button>
                        </form>
                    </div>
                </div>

                <!-- Azioni -->
                <div class="actions">
                    <a href="${pageContext.request.contextPath}/prodotti/" class="link-back">
                        ← Torna alla Lista
                    </a>
                </div>

            </section>
        </div>
    </section>
</main>

<script>
    function showTab(evt, tabName) {
        document.querySelectorAll('.tab-content').forEach(tab => tab.classList.remove('active'));
        document.querySelectorAll('.tab-button').forEach(btn => btn.classList.remove('active'));
        document.getElementById('tab-' + tabName).classList.add('active');
        if (evt && evt.currentTarget) evt.currentTarget.classList.add('active');
    }

    function confirmDelete() {
        if (confirm('Eliminare questo prodotto?')) {
            window.location.href = '${pageContext.request.contextPath}/prodotti/delete?id=${prodotto.idProdotto}';
        }
    }
</script>

</body>
</html>
