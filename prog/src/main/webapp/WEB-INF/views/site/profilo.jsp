<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="it">
<head>
  <title>Il mio Profilo - TechVibe</title>
  <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
  <jsp:include page="/WEB-INF/views/partials/head.jsp">
    <jsp:param name="styles" value="site"/>
  </jsp:include>

  <style>
    .profile-container {
      max-width: 800px;
      margin: 2rem auto;
      padding: 0 1rem;
    }

    .profile-header {
      text-align: center;
      margin-bottom: 2rem;
    }

    .profile-title {
      color: var(--primary-light);
      font-size: 2rem;
      margin-bottom: 0.5rem;
    }

    .profile-subtitle {
      color: #666;
      font-size: 1.1rem;
    }

    .profile-tabs {
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

    .info-card {
      background: white;
      border: 1px solid #eee;
      border-radius: 12px;
      padding: 2rem;
      margin-bottom: 2rem;
    }

    .info-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 1.5rem;
    }

    .info-item {
      display: flex;
      flex-direction: column;
      gap: 0.5rem;
    }

    .info-label {
      font-weight: 600;
      color: var(--primary-light);
      font-size: 0.9rem;
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

    .form-group input {
      padding: 0.75rem 1rem;
      border: 1px solid #ddd;
      border-radius: 8px;
      font-size: 1rem;
    }

    .form-group input:focus {
      border-color: var(--primary-light);
      outline: none;
    }

    .btn-save {
      background: var(--primary-light);
      color: white;
      border: none;
      padding: 1rem 2rem;
      border-radius: 8px;
      cursor: pointer;
      font-weight: 600;
      justify-self: start;
    }

    .btn-edit {
      background: #28a745;
      color: white;
      border: none;
      padding: 0.75rem 1.5rem;
      border-radius: 8px;
      cursor: pointer;
      font-weight: 600;
    }

    .success-message {
      background: #d4edda;
      color: #155724;
      padding: 1rem;
      border-radius: 8px;
      margin-bottom: 1rem;
    }

    .error-message {
      background: #f8d7da;
      color: #721c24;
      padding: 1rem;
      border-radius: 8px;
      margin-bottom: 1rem;
    }

    .order-card {
      border: 1px solid #eee;
      border-radius: 12px;
      padding: 1.5rem;
      margin-bottom: 1rem;
      background: #fafafa;
      transition: all 0.3s ease;
    }

    .order-card:hover {
      box-shadow: 0 2px 8px rgba(0,0,0,0.1);
      transform: translateY(-2px);
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

    .empty-orders-icon {
      font-size: 3rem;
      margin-bottom: 1rem;
      opacity: 0.5;
    }

    @media (max-width: 768px) {
      .info-grid, .order-details {
        grid-template-columns: 1fr;
      }

      .profile-tabs {
        flex-direction: column;
      }

      .order-header {
        flex-direction: column;
        gap: 0.5rem;
        align-items: flex-start;
      }
    }
  </style>
</head>
<body>
<main class="app">
  <%@ include file="../partials/site/header.jsp" %>

  <div class="profile-container">
    <div class="profile-header">
      <h1 class="profile-title">Ciao, ${utente.nome}!</h1>
      <p class="profile-subtitle">Gestisci il tuo account e le tue preferenze</p>
    </div>

    <!-- TABS -->
    <div class="profile-tabs">
      <button class="tab-button active" onclick="showTab('edit')">Modifica Profilo</button>
      <button class="tab-button" onclick="showTab('orders')">I miei Ordini</button>
    </div>

    <!-- MESSAGGI -->
    <c:if test="${not empty successMessage}">
      <div class="success-message">${successMessage}</div>
    </c:if>

    <c:if test="${not empty errorMessage}">
      <div class="error-message">${errorMessage}</div>
    </c:if>

    <!-- TAB MODIFICA -->
    <div id="tab-edit" class="tab-content active">
      <div class="info-card">
        <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Modifica Profilo</h3>

        <form method="POST" action="${pageContext.request.contextPath}/utente/update" class="edit-form">
          <div class="form-group">
            <label for="nome">Nome</label>
            <input type="text" id="nome" name="nome" value="${utente.nome}" required>
          </div>

          <div class="form-group">
            <label for="cognome">Cognome</label>
            <input type="text" id="cognome" name="cognome" value="${utente.cognome}" required>
          </div>

          <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" value="${utente.email}" required>
          </div>

          <div class="form-group">
            <label for="telefono">Telefono</label>
            <input type="tel" id="telefono" name="telefono" value="${utente.telefono}">
          </div>

          <div class="form-group">
            <label for="indirizzo">Indirizzo di Spedizione</label>
            <input type="text" id="indirizzo" name="indirizzo" value="${utente.indirizzoSpedizione}">
          </div>

          <button type="submit" class="btn-save">Salva Modifiche</button>
        </form>
      </div>
    </div>

    <!-- TAB ORDINI -->
    <div id="tab-orders" class="tab-content">
      <div class="info-card">
        <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">
          I miei Ordini
          <c:if test="${not empty ordiniUtente}">
            <span style="color: #666; font-size: 0.9rem; font-weight: normal;">(${ordiniUtente.size()} ordini)</span>
          </c:if>
        </h3>

        <c:choose>
          <c:when test="${not empty ordiniUtente}">
            <c:forEach var="ordine" items="${ordiniUtente}">
              <a href="${pageContext.request.contextPath}/utente/ordine?id=${ordine.idOrdine}" style="text-decoration: none; color: inherit;">
                <div class="order-card" style="cursor: pointer;">
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

                  <!-- Indicatore che è cliccabile -->
                  <div style="text-align: right; margin-top: 1rem; padding-top: 1rem; border-top: 1px solid #eee;">
                    <span style="color: var(--primary-light); font-size: 0.9rem;">
                      Clicca per vedere i dettagli →
                    </span>
                  </div>
                </div>
              </a>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <div class="empty-orders">
              <div class="empty-orders-icon">📦</div>
              <h4 style="margin-bottom: 1rem; color: #333;">Nessun ordine trovato</h4>
              <p style="margin-bottom: 2rem;">Non hai ancora effettuato ordini.</p>
              <a href="${pageContext.request.contextPath}/pages"
                 style="background: var(--primary-light); color: white; padding: 1rem 2rem;
                        text-decoration: none; border-radius: 8px; font-weight: 600; display: inline-block;">
                Inizia a fare shopping!
              </a>
            </div>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
  </div>

  <%@ include file="../partials/site/footer.jsp" %>
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

  // Hover effects for order cards
  document.addEventListener('DOMContentLoaded', function() {
    const orderCards = document.querySelectorAll('.order-card');
    orderCards.forEach(card => {
      card.addEventListener('mouseenter', function() {
        this.style.backgroundColor = '#ffffff';
      });
      card.addEventListener('mouseleave', function() {
        this.style.backgroundColor = '#fafafa';
      });
    });
  });
</script>

</body>
</html>