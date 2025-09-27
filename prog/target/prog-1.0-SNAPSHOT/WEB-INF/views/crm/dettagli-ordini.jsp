<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title></title>

    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="Dettaglio Ordine"/>
        <jsp:param name="styles" value="crm,dashboard"/>
        <jsp:param name="scripts" value="crm"/>
    </jsp:include>

    <style>
        .ordine-container {
            margin-bottom: 5rem;
        }

        .ordine-header {
            background: var(--light-gray);
            padding: 2rem;
            border-radius: 12px;
            margin-bottom: 2rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .ordine-info h2 {
            margin: 0;
            color: var(--primary-light);
        }

        .ordine-status {
            padding: 0.5rem 1rem;
            border-radius: 20px;
            font-weight: 600;
            text-transform: uppercase;
            font-size: 0.8rem;
        }

        .status-confermato {
            background: #d4edda;
            color: #155724;
        }

        .status-in-elaborazione {
            background: #fff3cd;
            color: #856404;
        }

        .status-spedito {
            background: #cce7ff;
            color: #004085;
        }

        .status-consegnato {
            background: #d1ecf1;
            color: #0c5460;
        }

        .status-annullato {
            background: #f8d7da;
            color: #721c24;
        }

        .ordine-tabs {
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

        .totale-scontato {
            color: #28a745;
            font-weight: bold;
        }

        .totale-originale {
            text-decoration: line-through;
            color: #999;
            margin-left: 0.5rem;
        }

        .single-column {
            grid-column: 1 / -1;
        }

        .prodotti-list {
            display: flex;
            flex-direction: column;
            gap: 1rem;
        }

        .prodotto-item {
            display: flex;
            gap: 1rem;
            padding: 1rem;
            border: 1px solid #eee;
            border-radius: 8px;
            background: white;
        }

        .prodotto-image {
            flex-shrink: 0;
            width: 80px;
            height: 80px;
        }

        .prodotto-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            border-radius: 6px;
        }

        .prodotto-details {
            flex: 1;
        }

        .prodotto-title {
            font-weight: 500;
            color: var(--primary-light);
            margin-bottom: 0.5rem;
        }

        .prodotto-specs {
            color: #666;
            font-size: 0.9rem;
            margin-bottom: 0.5rem;
        }

        .prodotto-footer {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .prodotto-quantity {
            color: #666;
        }

        .prodotto-price {
            font-weight: 500;
            color: var(--primary-light);
        }
    </style>
</head>
<body>

<main class="app">
    <%@include file="../partials/crm/sidebar.jsp"%>
    <section class="content grid-y">
        <%@include file="../partials/crm/header.jsp"%>
        <div class="body grid-x justify-center">

            <section class="grid-y cell w75 ordine-container">

                <jsp:include page="../partials/site/alert.jsp" />

                <!-- Header Ordine -->
                <div class="ordine-header">
                    <div class="ordine-info">
                        <h2>Ordine #${ordine.idOrdine}</h2>
                        <p style="margin: 0.5rem 0 0 0; color: #666;">
                            Cliente: ${ordine.utente.nome} ${ordine.utente.cognome}
                        </p>
                    </div>
                    <div>
                        <span class="ordine-status ${ordine.stato == 'CONFERMATO' ? 'status-confermato' :
                                                     ordine.stato == 'IN_ELABORAZIONE' ? 'status-in-elaborazione' :
                                                     ordine.stato == 'SPEDITO' ? 'status-spedito' :
                                                     ordine.stato == 'CONSEGNATO' ? 'status-consegnato' :
                                                     'status-annullato'}">
                            ${ordine.stato}
                        </span>
                        <c:if test="${ordine.scontoTotale > 0}">
                            <span class="ordine-status" style="background: #fff3cd; color: #856404; margin-left: 0.5rem;">
                                SCONTO €<fmt:formatNumber value="${ordine.scontoTotale}" minFractionDigits="2"/>
                            </span>
                        </c:if>
                    </div>
                </div>

                <!-- Tabs -->
                <div class="ordine-tabs">
                    <button class="tab-button active" onclick="showTab('info')">Informazioni</button>
                    <button class="tab-button" onclick="showTab('prodotti')">Prodotti Ordinati</button>
                    <button class="tab-button" onclick="showTab('edit')">Modifica Ordine</button>
                </div>

                <!-- Tab Informazioni -->
                <div id="tab-info" class="tab-content active">
                    <div class="info-grid">
                        <div class="info-card">
                            <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Dati Ordine</h3>

                            <div class="info-item">
                                <div class="info-label">ID Ordine</div>
                                <div class="info-value">#${ordine.idOrdine}</div>
                            </div>

                            <div class="info-item">
                                <div class="info-label">Stato</div>
                                <div class="info-value">${ordine.stato}</div>
                            </div>
                        </div>

                        <div class="info-card">
                            <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Totali</h3>

                            <div class="info-item">
                                <div class="info-label">Totale Finale</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${ordine.scontoTotale > 0}">
                                            <span class="totale-scontato">
                                                €<fmt:formatNumber value="${ordine.totale - ordine.scontoTotale}" minFractionDigits="2"/>
                                            </span>
                                            <span class="totale-originale">
                                                €<fmt:formatNumber value="${ordine.totale}" minFractionDigits="2"/>
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            €<fmt:formatNumber value="${ordine.totale}" minFractionDigits="2"/>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <div class="info-item">
                                <div class="info-label">Sconto Applicato</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${ordine.scontoTotale > 0}">
                                            €<fmt:formatNumber value="${ordine.scontoTotale}" minFractionDigits="2"/>
                                        </c:when>
                                        <c:otherwise>
                                            Nessuno
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>

                        <div class="info-card">
                            <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Cliente</h3>

                            <div class="info-item">
                                <div class="info-label">Nome e Cognome</div>
                                <div class="info-value">${ordine.utente.nome} ${ordine.utente.cognome}</div>
                            </div>

                            <div class="info-item">
                                <div class="info-label">Email</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${not empty ordine.utente.email}">
                                            ${ordine.utente.email}
                                        </c:when>
                                        <c:otherwise>
                                            <em style="color: #999;">Non specificato</em>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <div class="info-item">
                                <div class="info-label">Telefono</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${not empty ordine.utente.telefono}">
                                            ${ordine.utente.telefono}
                                        </c:when>
                                        <c:otherwise>
                                            <em style="color: #999;">Non specificato</em>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>

                        <div class="info-card">
                            <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Spedizione e Pagamento</h3>

                            <div class="info-item">
                                <div class="info-label">Metodo di Spedizione</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${not empty ordine.metodoDiSpedizione}">
                                            ${ordine.metodoDiSpedizione}
                                        </c:when>
                                        <c:otherwise>
                                            <em style="color: #999;">Non specificato</em>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <div class="info-item">
                                <div class="info-label">Metodo di Pagamento</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${not empty ordine.metodoDiPagamento}">
                                            ${ordine.metodoDiPagamento}
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

                <!-- Tab Prodotti Ordinati -->
                <div id="tab-prodotti" class="tab-content">
                    <div class="info-card single-column">
                        <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Prodotti Ordinati</h3>

                        <c:choose>
                            <c:when test="${not empty ordine.carrello and not empty ordine.carrello.items}">
                                <div class="prodotti-list">
                                    <c:forEach var="item" items="${ordine.carrello.items}">
                                        <div class="prodotto-item">
                                            <!-- Immagine prodotto -->
                                            <div class="prodotto-image">
                                                <c:choose>
                                                    <c:when test="${not empty item.prodotto.immagine1}">
                                                        <img src="${pageContext.request.contextPath}/img/${item.prodotto.immagine1}"
                                                             alt="${item.prodotto.marca} ${item.prodotto.modello}">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="${pageContext.request.contextPath}/img/placeholder.png"
                                                             alt="Prodotto">
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>

                                            <!-- Dettagli prodotto -->
                                            <div class="prodotto-details">
                                                <div class="prodotto-title">
                                                        ${item.prodotto.marca} ${item.prodotto.modello}
                                                </div>
                                                <div class="prodotto-specs">
                                                    <c:if test="${not empty item.prodotto.categoria}">
                                                        ${item.prodotto.categoria.nomeCategoria}
                                                    </c:if>
                                                    <c:if test="${not empty item.prodotto.storage}">
                                                        • Storage: ${item.prodotto.storage}GB
                                                    </c:if>
                                                    <c:if test="${not empty item.prodotto.colore}">
                                                        • Colore: ${item.prodotto.colore}
                                                    </c:if>
                                                </div>
                                                <div class="prodotto-footer">
                                                    <span class="prodotto-quantity">Quantità: ${item.quantita}</span>
                                                    <span class="prodotto-price">
                                                        <fmt:formatNumber value="${item.total()}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div style="text-align: center; padding: 2rem; color: #666;">
                                    <p>Nessun prodotto trovato per questo ordine.</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Tab Modifica Ordine -->
                <div id="tab-edit" class="tab-content">
                    <div class="info-card">
                        <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Modifica Ordine</h3>

                        <form method="POST" action="${pageContext.request.contextPath}/ordini/update" class="edit-form">
                            <input type="hidden" name="id" value="${ordine.idOrdine}">

                            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                                <div class="form-group">
                                    <label for="stato">Stato Ordine</label>
                                    <select id="stato" name="stato" required>
                                        <option value="CONFERMATO" ${ordine.stato == 'CONFERMATO' ? 'selected' : ''}>Confermato</option>
                                        <option value="IN_ELABORAZIONE" ${ordine.stato == 'IN_ELABORAZIONE' ? 'selected' : ''}>In Elaborazione</option>
                                        <option value="SPEDITO" ${ordine.stato == 'SPEDITO' ? 'selected' : ''}>Spedito</option>
                                        <option value="CONSEGNATO" ${ordine.stato == 'CONSEGNATO' ? 'selected' : ''}>Consegnato</option>
                                        <option value="ANNULLATO" ${ordine.stato == 'ANNULLATO' ? 'selected' : ''}>Annullato</option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label for="scontoTotale">Sconto Totale (€)</label>
                                    <input type="number" step="0.01" id="scontoTotale" name="scontoTotale" value="${ordine.scontoTotale}">
                                </div>
                            </div>

                            <button type="submit" class="btn primary">Salva Modifiche</button>
                            <button class="btn" type="button" onclick="confirmCancel()" style="background:var(--primary-light); color: white;">
                                Annulla Ordine
                            </button>
                        </form>
                    </div>
                </div>

                <!-- Azioni -->
                <div class="actions">
                    <a href="${pageContext.request.contextPath}/ordini/" style="text-decoration: none;color:var(--primary-light)">
                        ← Torna alla Lista Ordini
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

    function confirmCancel() {
        if (confirm('Annullare questo ordine? Questa azione non può essere annullata.')) {
            // Modifica lo stato del form per annullamento
            document.getElementById('stato').value = 'ANNULLATO';
            document.forms[0].submit();
        }
    }
</script>

</body>
</html>