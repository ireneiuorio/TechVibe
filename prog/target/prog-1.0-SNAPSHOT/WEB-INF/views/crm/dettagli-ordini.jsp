<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <title>Dettaglio ordine · TechVibe</title>
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">

    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="Dettaglio ordine"/>
        <jsp:param name="styles" value="crm,dashboard,ordine-dettaglio"/>
        <jsp:param name="scripts" value="crm"/>
    </jsp:include>
</head>
<body>

<main class="app">
    <%@ include file="../partials/crm/sidebar.jsp" %>

    <section class="content grid-y">
        <%@ include file="../partials/crm/header.jsp" %>

        <div class="body grid-x justify-center">
            <section class="grid-y cell w75 ordine-container">

                <jsp:include page="../partials/site/alert.jsp" />

                <!-- Testata ordine -->
                <div class="ordine-header">
                    <div class="ordine-info">
                        <h2>Ordine #${ordine.idOrdine}</h2>
                        <p class="ordine-subtitle">
                            Cliente: ${ordine.utente.nome} ${ordine.utente.cognome}
                        </p>
                    </div>
                    <div>
                        <span class="ordine-status ${
                                ordine.stato == 'CONFERMATO' ? 'status-confermato' :
                                ordine.stato == 'IN_ELABORAZIONE' ? 'status-in-elaborazione' :
                                ordine.stato == 'SPEDITO' ? 'status-spedito' :
                                ordine.stato == 'CONSEGNATO' ? 'status-consegnato' :
                                'status-annullato'
                            }">
                            ${ordine.stato}
                        </span>

                        <c:if test="${ordine.scontoTotale > 0}">
                            <span class="ordine-status status-sconto">
                                SCONTO €<fmt:formatNumber value="${ordine.scontoTotale}" minFractionDigits="2"/>
                            </span>
                        </c:if>
                    </div>
                </div>

                <!-- Tab -->
                <div class="ordine-tabs">
                    <button class="tab-button active" onclick="showTab(event,'info')">Informazioni</button>
                    <button class="tab-button" onclick="showTab(event,'prodotti')">Prodotti ordinati</button>
                    <button class="tab-button" onclick="showTab(event,'edit')">Modifica ordine</button>
                </div>

                <!-- Informazioni -->
                <div id="tab-info" class="tab-content active">
                    <div class="info-grid">
                        <div class="info-card">
                            <h3 class="section-title">Dati ordine</h3>

                            <div class="info-item">
                                <div class="info-label">ID ordine</div>
                                <div class="info-value">#${ordine.idOrdine}</div>
                            </div>

                            <div class="info-item">
                                <div class="info-label">Stato</div>
                                <div class="info-value">${ordine.stato}</div>
                            </div>
                        </div>

                        <div class="info-card">
                            <h3 class="section-title">Totali</h3>

                            <div class="info-item">
                                <div class="info-label">Totale finale</div>
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
                                <div class="info-label">Sconto applicato</div>
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
                            <h3 class="section-title">Cliente</h3>

                            <div class="info-item">
                                <div class="info-label">Nome e cognome</div>
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
                                            <em class="muted">Non specificato</em>
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
                                            <em class="muted">Non specificato</em>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>

                        <div class="info-card">
                            <h3 class="section-title">Spedizione e pagamento</h3>

                            <div class="info-item">
                                <div class="info-label">Metodo di spedizione</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${not empty ordine.metodoDiSpedizione}">
                                            ${ordine.metodoDiSpedizione}
                                        </c:when>
                                        <c:otherwise>
                                            <em class="muted">Non specificato</em>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <div class="info-item">
                                <div class="info-label">Metodo di pagamento</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${not empty ordine.metodoDiPagamento}">
                                            ${ordine.metodoDiPagamento}
                                        </c:when>
                                        <c:otherwise>
                                            <em class="muted">Non specificato</em>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Prodotti ordinati -->
                <div id="tab-prodotti" class="tab-content">
                    <div class="info-card single-column">
                        <h3 class="section-title">Prodotti ordinati</h3>

                        <c:choose>
                            <c:when test="${not empty ordine.carrello and not empty ordine.carrello.items}">
                                <div class="prodotti-list">
                                    <c:forEach var="item" items="${ordine.carrello.items}">
                                        <div class="prodotto-item">
                                            <div class="prodotto-image">
                                                <c:choose>
                                                    <c:when test="${not empty item.prodotto.immagine1}">
                                                        <img src="${pageContext.request.contextPath}/img/${item.prodotto.immagine1}"
                                                             alt="${item.prodotto.marca} ${item.prodotto.modello}">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="${pageContext.request.contextPath}/img/placeholder.png" alt="Prodotto">
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>

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
                                <div class="empty-state">
                                    <p>Nessun prodotto trovato per questo ordine.</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Modifica ordine -->
                <div id="tab-edit" class="tab-content">
                    <div class="info-card">
                        <h3 class="section-title">Modifica ordine</h3>

                        <form method="POST" action="${pageContext.request.contextPath}/ordini/update" class="edit-form">
                            <input type="hidden" name="id" value="${ordine.idOrdine}">

                            <div class="two-cols">
                                <div class="form-group">
                                    <label for="stato">Stato ordine</label>
                                    <select id="stato" name="stato" required>
                                        <option value="CONFERMATO" ${ordine.stato == 'CONFERMATO' ? 'selected' : ''}>Confermato</option>
                                        <option value="IN_ELABORAZIONE" ${ordine.stato == 'IN_ELABORAZIONE' ? 'selected' : ''}>In elaborazione</option>
                                        <option value="SPEDITO" ${ordine.stato == 'SPEDITO' ? 'selected' : ''}>Spedito</option>
                                        <option value="CONSEGNATO" ${ordine.stato == 'CONSEGNATO' ? 'selected' : ''}>Consegnato</option>
                                        <option value="ANNULLATO" ${ordine.stato == 'ANNULLATO' ? 'selected' : ''}>Annullato</option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label for="scontoTotale">Sconto totale (€)</label>
                                    <input type="number" step="0.01" id="scontoTotale" name="scontoTotale" value="${ordine.scontoTotale}">
                                </div>
                            </div>

                            <button type="submit" class="btn primary">Salva modifiche</button>
                            <button class="btn danger" type="button" onclick="confirmCancel()">Annulla ordine</button>
                        </form>
                    </div>
                </div>

                <!-- Azioni -->
                <div class="actions">
                    <a href="${pageContext.request.contextPath}/ordini/" class="link-back">
                        ← Torna alla lista ordini
                    </a>
                </div>

            </section>
        </div>
    </section>
</main>

<script>
    function showTab(evt, tabName) {
        document.querySelectorAll('.tab-content').forEach(t => t.classList.remove('active'));
        document.querySelectorAll('.tab-button').forEach(b => b.classList.remove('active'));
        document.getElementById('tab-' + tabName).classList.add('active');
        evt.currentTarget.classList.add('active');
    }

    function confirmCancel() {
        if (confirm('Annullare questo ordine? Questa azione non può essere annullata.')) {
            document.getElementById('stato').value = 'ANNULLATO';
            document.forms[0].submit();
        }
    }
</script>

</body>
</html>
