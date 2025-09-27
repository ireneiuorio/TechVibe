<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
  <title></title>

  <jsp:include page="../partials/head.jsp">
    <jsp:param name="title" value="Dettaglio Utente"/>
    <jsp:param name="styles" value="crm,dashboard"/>
    <jsp:param name="scripts" value="crm"/>
  </jsp:include>

  <style>

    .utente-container {
      margin-bottom: 5rem;
    }

    .utente-header {
      background: var(--light-gray);
      padding: 2rem;
      border-radius: 12px;
      margin-bottom: 2rem;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .utente-info h2 {
      margin: 0;
      color: var(--primary-light);
    }

    .utente-status {
      padding: 0.5rem 1rem;
      border-radius: 20px;
      font-weight: 600;
      text-transform: uppercase;
      font-size: 0.8rem;
    }

    .status-attivo {
      background: #d4edda;
      color: #155724;
    }

    .status-disattivato {
      background: #f8d7da;
      color: #721c24;
    }

    .utente-tabs {
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

    .order-card {
      border: 1px solid #eee;
      border-radius: 12px;
      padding: 1.5rem;
      margin-bottom: 1rem;
      background: #fafafa;
    }

    .order-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 1rem;
      padding-bottom: 1rem;
      border-bottom: 1px solid #eee;
    }

    .order-id {
      font-size: 1.1rem;
      font-weight: bold;
      color: var(--primary-light);
    }

    .order-status {
      background: #28a745;
      color: white;
      padding: 0.5rem 1rem;
      border-radius: 20px;
      font-size: 0.8rem;
      font-weight: 600;
      text-transform: capitalize;
    }

    .order-details {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 1rem;
    }

    .order-detail-item {
      display: flex;
      flex-direction: column;
      gap: 0.25rem;
    }

    .order-detail-label {
      color: #666;
      font-size: 0.9rem;
    }

    .order-detail-value {
      font-weight: 600;
      color: #333;
    }

    .empty-orders {
      text-align: center;
      padding: 3rem 2rem;
      background: #f8f9fa;
      border-radius: 12px;
      color: #666;
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

    .form-group input {
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
  </style>
</head>
<body>

<main class="app">
  <%@include file="../partials/crm/sidebar.jsp"%>
  <section class="content grid-y">
    <%@include file="../partials/crm/header.jsp"%>
    <div class="body grid-x justify-center">

      <section class="grid-y cell w75 utente-container">

        <jsp:include page="../partials/site/alert.jsp" />

        <!-- Header Utente -->
        <div class="utente-header">
          <div class="utente-info">
            <h2>${utente.nome} ${utente.cognome}</h2>
            <p style="margin: 0.5rem 0 0 0; color: #666;">${utente.email}</p>
          </div>
          <div>
                        <span class="utente-status ${utente.attivo ? 'status-attivo' : 'status-disattivato'}">
                          ${utente.attivo ? 'ATTIVO' : 'DISATTIVATO'}
                        </span>
            <c:if test="${utente.admin}">
                            <span class="utente-status" style="background: #fff3cd; color: #856404; margin-left: 0.5rem;">
                                ADMIN
                            </span>
            </c:if>
          </div>
        </div>

        <!-- Tabs -->
        <div class="utente-tabs">
          <button class="tab-button active" onclick="showTab('info')">Informazioni</button>
          <button class="tab-button" onclick="showTab('orders')">Ordini (${not empty ordiniUtente ? ordiniUtente.size() : 0})</button>
          <button class="tab-button" onclick="showTab('edit')">Modifica Contatti</button>
        </div>

        <!-- Tab Informazioni -->
        <div id="tab-info" class="tab-content active">
          <div class="info-grid">
            <div class="info-card">
              <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Dati Personali</h3>

              <div class="info-item">
                <div class="info-label">Nome Completo</div>
                <div class="info-value">${utente.nome} ${utente.cognome}</div>
              </div>

              <div class="info-item">
                <div class="info-label">Email</div>
                <div class="info-value">${utente.email}</div>
              </div>

              <div class="info-item">
                <div class="info-label">Tipo Account</div>
                <div class="info-value">${utente.admin ? 'Amministratore' : 'Utente Normale'}</div>
              </div>
            </div>

            <div class="info-card">
              <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Contatti</h3>

              <div class="info-item">
                <div class="info-label">Telefono</div>
                <div class="info-value">
                  <c:choose>
                    <c:when test="${not empty utente.telefono}">
                      ${utente.telefono}
                    </c:when>
                    <c:otherwise>
                      <em style="color: #999;">Non specificato</em>
                    </c:otherwise>
                  </c:choose>
                </div>
              </div>

              <div class="info-item">
                <div class="info-label">Indirizzo di Spedizione</div>
                <div class="info-value">
                  <c:choose>
                    <c:when test="${not empty utente.indirizzoSpedizione}">
                      ${utente.indirizzoSpedizione}
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

        <!-- Tab Ordini -->
        <div id="tab-orders" class="tab-content">
          <div class="info-card">
            <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">
              Ordini dell'utente
              <c:if test="${not empty ordiniUtente}">
                <span style="color: #666; font-size: 0.9rem; font-weight: normal;">(${ordiniUtente.size()} ordini)</span>
              </c:if>
            </h3>

            <c:choose>
              <c:when test="${not empty ordiniUtente}">
                <c:forEach var="ordine" items="${ordiniUtente}">
                  <div class="order-card">
                    <div class="order-header">
                      <div class="order-id">Ordine #${ordine.idOrdine}</div>
                      <div class="order-status">${ordine.stato}</div>
                    </div>

                    <div class="order-details">
                      <div class="order-detail-item">
                        <span class="order-detail-label">Totale</span>
                        <span class="order-detail-value" style="color: var(--primary-light); font-size: 1.1rem;">
                                                    <c:choose>
                                                      <c:when test="${ordine.totale != null and ordine.totale > 0}">
                                                        <fmt:formatNumber value="${ordine.totale}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                                                      </c:when>
                                                      <c:otherwise>
                                                        €0,00
                                                      </c:otherwise>
                                                    </c:choose>
                                                </span>
                      </div>

                      <div class="order-detail-item">
                        <span class="order-detail-label">Metodo di Pagamento</span>
                        <span class="order-detail-value" style="text-transform: capitalize;">
                                                    <c:out value="${ordine.metodoDiPagamento}" default="Non specificato"/>
                                                </span>
                      </div>

                      <c:if test="${not empty ordine.metodoDiSpedizione}">
                        <div class="order-detail-item">
                          <span class="order-detail-label">Spedizione</span>
                          <span class="order-detail-value" style="text-transform: capitalize;">
                              ${ordine.metodoDiSpedizione}
                          </span>
                        </div>
                      </c:if>

                      <c:if test="${ordine.scontoTotale != null and ordine.scontoTotale > 0}">
                        <div class="order-detail-item">
                          <span class="order-detail-label">Sconto Applicato</span>
                          <span class="order-detail-value" style="color: #28a745;">
                                                        -<fmt:formatNumber value="${ordine.scontoTotale}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                                                    </span>
                        </div>
                      </c:if>
                    </div>
                  </div>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <div class="empty-orders">
                  <div style="font-size: 3rem; margin-bottom: 1rem; opacity: 0.5;"></div>
                  <h4 style="margin-bottom: 1rem; color: #333;">Nessun ordine trovato</h4>
                  <p>Questo utente non ha ancora effettuato ordini.</p>
                </div>
              </c:otherwise>
            </c:choose>
          </div>
        </div>

        <!-- Tab Modifica Contatti -->
        <div id="tab-edit" class="tab-content">
          <div class="info-card">
            <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Modifica Contatti</h3>

            <form method="POST" action="${pageContext.request.contextPath}/utente/update-contacts" class="edit-form">
              <input type="hidden" name="id" value="${utente.idUtente}">

              <div class="form-group">
                <label for="telefono">Telefono</label>
                <input type="tel" id="telefono" name="telefono" value="${utente.telefono}" placeholder="Inserisci numero di telefono">
              </div>

              <div class="form-group">
                <label for="indirizzo">Indirizzo di Spedizione</label>
                <input type="text" id="indirizzo" name="indirizzo" value="${utente.indirizzoSpedizione}" placeholder="Via, Numero, Città, CAP">
              </div>

              <button type="submit" class="btn primary">Aggiorna Contatti</button>
            </form>
          </div>
        </div>

        <!-- Azioni -->
        <div class="actions">
          <a href="${pageContext.request.contextPath}/utente/" style="text-decoration: none;color:var(--primary-light)">
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
</script>

</body>
</html>