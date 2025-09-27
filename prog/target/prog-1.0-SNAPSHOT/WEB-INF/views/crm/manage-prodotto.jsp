<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title></title>

    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="Dettaglio Prodotto"/>
        <jsp:param name="styles" value="crm,dashboard"/>
        <jsp:param name="scripts" value="crm"/>
    </jsp:include>

    <style>
        .prodotto-container {
            margin-bottom: 5rem;
        }

        .prodotto-header {
            background: var(--light-gray);
            padding: 2rem;
            border-radius: 12px;
            margin-bottom: 2rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .prodotto-info h2 {
            margin: 0;
            color: var(--primary-light);
        }

        .prodotto-status {
            padding: 0.5rem 1rem;
            border-radius: 20px;
            font-weight: 600;
            text-transform: uppercase;
            font-size: 0.8rem;
        }

        .status-disponibile {
            background: #d4edda;
            color: #155724;
        }

        .status-esaurito {
            background: #f8d7da;
            color: #721c24;
        }

        .prodotto-tabs {
            display: flex;
            background: var(--light-gray);
            border-radius: 12px;
            padding: 0.5rem;
            margin-bottom: 2rem;
        }

        .tab-button {
            flex: 1;
            padding: 1rem;
            background: none;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
            transition: all 0.3s;
        }

        .tab-button.active {
            background: var(--primary-light);
            color: white;
        }

        .tab-content {
            display: none;
        }

        .tab-content.active {
            display: block;
        }

        .info-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 2rem;
            margin-bottom: 2rem;
        }

        .info-card {
            background: white;
            border: 1px solid #eee;
            border-radius: 12px;
            padding: 2rem;
        }

        .info-item {
            margin-bottom: 1.5rem;
        }

        .info-label {
            font-weight: 600;
            color: var(--primary-light);
            margin-bottom: 0.5rem;
        }

        .info-value {
            color: #333;
            font-size: 1rem;
            padding: 0.75rem 1rem;
            background: var(--light-gray);
            border-radius: 8px;
        }

        .edit-form {
            display: grid;
            gap: 1.5rem;
        }

        .form-group {
            display: flex;
            flex-direction: column;
            gap: 0.5rem;
        }

        .form-group label {
            font-weight: 600;
            color: var(--primary-light);
        }

        .form-group input, .form-group select {
            padding: 0.75rem 1rem;
            border: 1px solid #ddd;
            border-radius: 8px;
            font-size: 1rem;
        }

        .actions {
            display: flex;
            gap: 1rem;
            margin-top: 2rem;
        }

        .prezzo-scontato {
            color: #28a745;
            font-weight: bold;
        }

        .prezzo-originale {
            text-decoration: line-through;
            color: #999;
            margin-left: 0.5rem;
        }
    </style>
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
                        <p style="margin: 0.5rem 0 0 0; color: #666;">ID: ${prodotto.idProdotto} | ${prodotto.categoria.nomeCategoria}</p>
                    </div>
                    <div>
                        <span class="prodotto-status ${prodotto.qtDisponibile > 0 ? 'status-disponibile' : 'status-esaurito'}">
                            ${prodotto.qtDisponibile > 0 ? 'DISPONIBILE' : 'ESAURITO'}
                        </span>
                        <c:if test="${prodotto.percentualeSconto > 0}">
                            <span class="prodotto-status" style="background: #fff3cd; color: #856404; margin-left: 0.5rem;">
                                SCONTO ${prodotto.percentualeSconto}%
                            </span>
                        </c:if>
                    </div>
                </div>

                <!-- Tabs -->
                <div class="prodotto-tabs">
                    <button class="tab-button active" onclick="showTab('info')">Informazioni</button>
                    <button class="tab-button" onclick="showTab('tech')">Specifiche Tecniche</button>
                    <button class="tab-button" onclick="showTab('edit')">Modifica Prodotto</button>
                </div>

                <!-- Tab Informazioni -->
                <div id="tab-info" class="tab-content active">
                    <div class="info-grid">
                        <div class="info-card">
                            <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Dati Prodotto</h3>

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
                            <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Prezzo</h3>

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
                            <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Hardware</h3>

                            <div class="info-item">
                                <div class="info-label">RAM</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${prodotto.ram > 0}">
                                            ${prodotto.ram} GB
                                        </c:when>
                                        <c:otherwise>
                                            <em style="color: #999;">Non specificato</em>
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
                                            <em style="color: #999;">Non specificato</em>
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
                                            <em style="color: #999;">Non specificato</em>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>

                        <div class="info-card">
                            <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Design e Connettività</h3>

                            <div class="info-item">
                                <div class="info-label">Colore</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${not empty prodotto.colore}">
                                            ${prodotto.colore}
                                        </c:when>
                                        <c:otherwise>
                                            <em style="color: #999;">Non specificato</em>
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
                                            <em style="color: #999;">Non specificato</em>
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
                                            <em style="color: #999;">Non specificato</em>
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
                        <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Modifica Prodotto</h3>

                        <form method="POST" action="${pageContext.request.contextPath}/prodotti/update" class="edit-form">
                            <input type="hidden" name="id" value="${prodotto.idProdotto}">

                            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                                <div class="form-group">
                                    <label for="modello">Modello</label>
                                    <input type="text" id="modello" name="modello" value="${prodotto.modello}" required>
                                </div>

                                <div class="form-group">
                                    <label for="marca">Marca</label>
                                    <input type="text" id="marca" name="marca" value="${prodotto.marca}" required>
                                </div>
                            </div>

                            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                                <div class="form-group">
                                    <label for="prezzo">Prezzo</label>
                                    <input type="number" step="0.01" id="prezzo" name="prezzo" value="${prodotto.prezzo}" required>
                                </div>

                                <div class="form-group">
                                    <label for="qtDisponibile">Quantità</label>
                                    <input type="number" id="qtDisponibile" name="qtDisponibile" value="${prodotto.qtDisponibile}" required>
                                </div>
                            </div>

                            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
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

                            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                                <div class="form-group">
                                    <label for="colore">Colore</label>
                                    <input type="text" id="colore" name="colore" value="${prodotto.colore}">
                                </div>

                                <div class="form-group">
                                    <label for="sistemaOperativo">Sistema Operativo</label>
                                    <input type="text" id="sistemaOperativo" name="sistemaOperativo" value="${prodotto.sistemaOperativo}">
                                </div>
                            </div>

                            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                                <div class="form-group">
                                    <label for="ram">RAM (GB)</label>
                                    <input type="number" id="ram" name="ram" value="${prodotto.ram}">
                                </div>

                                <div class="form-group">
                                    <label for="storage">Storage (GB)</label>
                                    <input type="number" id="storage" name="storage" value="${prodotto.storage}">
                                </div>
                            </div>

                            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
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
                            <button class="btn" type="button" onclick="confirmDelete()" style="background:var(--primary-light); color: white;">
                                Elimina Prodotto
                            </button>
                        </form>
                    </div>
                </div>

                <!-- Azioni -->
                <div class="actions">
                    <a href="${pageContext.request.contextPath}/prodotti/" style="text-decoration: none;color:var(--primary-light)">
                        ← Torna alla Lista
                    </a>


                </div>

            </section>
        </div>
    </section>
</main>

<script>
    function showTab(tabName) {
        // Nascondi tutti i tab content
        document.querySelectorAll('.tab-content').forEach(tab => {
            tab.classList.remove('active');
        });

        // Rimuovi active da tutti i bottoni
        document.querySelectorAll('.tab-button').forEach(btn => {
            btn.classList.remove('active');
        });

        // Mostra il tab selezionato
        document.getElementById('tab-' + tabName).classList.add('active');

        // Attiva il bottone corrispondente
        event.target.classList.add('active');
    }

    function confirmDelete() {
        if (confirm('Eliminare questo prodotto?')) {
            window.location.href = '${pageContext.request.contextPath}/prodotti/delete?id=${prodotto.idProdotto}';
        }
    }
</script>

</body>
</html>