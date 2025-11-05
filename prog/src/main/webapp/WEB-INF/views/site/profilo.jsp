<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="it">
<head>
  <title>Il mio Profilo - TechVibe</title>
  <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
  <jsp:include page="/WEB-INF/views/partials/head.jsp">
    <jsp:param name="styles" value="site,profilo-techvibe"/>
  </jsp:include>
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
      <button class="tab-button" onclick="showTab('edit')" id="tab-edit-btn">Modifica Profilo</button>
      <button class="tab-button" onclick="showTab('password')" id="tab-password-btn">Cambia Password</button>
      <button class="tab-button" onclick="showTab('orders')" id="tab-orders-btn">I miei Ordini</button>
    </div>

    <!-- MESSAGGI -->
    <c:if test="${param.success == 'password_changed'}">
      <div class="success-message">Password cambiata con successo!</div>
    </c:if>
    <c:if test="${param.error == 'missing_fields'}">
      <div class="error-message">Tutti i campi sono obbligatori</div>
    </c:if>
    <c:if test="${param.error == 'password_mismatch'}">
      <div class="error-message">Le nuove password non coincidono</div>
    </c:if>
    <c:if test="${param.error == 'password_too_short'}">
      <div class="error-message">La nuova password deve essere di almeno 6 caratteri</div>
    </c:if>
    <c:if test="${param.error == 'wrong_current_password'}">
      <div class="error-message">Password attuale non corretta</div>
    </c:if>
    <c:if test="${param.error == 'update_failed'}">
      <div class="error-message">Errore durante l'aggiornamento della password</div>
    </c:if>
    <c:if test="${param.error == 'user_not_found'}">
      <div class="error-message">Errore: utente non trovato. Prova a fare nuovamente login.</div>
    </c:if>
    <c:if test="${not empty successMessage}">
      <div class="success-message">${successMessage}</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
      <div class="error-message">${errorMessage}</div>
    </c:if>

    <!-- TAB MODIFICA -->
    <div id="tab-edit" class="tab-content">
      <div class="info-card">
        <h3 class="section-title">Modifica Profilo</h3>

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

    <!-- TAB CAMBIO PASSWORD -->
    <div id="tab-password" class="tab-content">
      <div class="info-card">
        <h3 class="section-title">Cambia Password</h3>

        <div class="password-requirements">
          <ul>
            <li>Minimo 6 caratteri</li>
            <li>Si consiglia di usare lettere maiuscole, minuscole, numeri e simboli</li>
          </ul>
        </div>

        <form method="POST"
              action="${pageContext.request.contextPath}/utente/change-password"
              class="edit-form"
              onsubmit="return validatePasswordForm()">
          <div class="form-group">
            <label for="currentPassword">Password Attuale</label>
            <input type="password" id="currentPassword" name="currentPassword" required>
          </div>

          <div class="form-group">
            <label for="newPassword">Nuova Password</label>
            <input type="password" id="newPassword" name="newPassword" required minlength="6">
          </div>

          <div class="form-group">
            <label for="confirmPassword">Conferma Nuova Password</label>
            <input type="password" id="confirmPassword" name="confirmPassword" required minlength="6">
          </div>

          <button type="submit" class="btn-change-password">Cambia Password</button>
        </form>
      </div>
    </div>

    <!-- TAB ORDINI -->
    <div id="tab-orders" class="tab-content">
      <div class="info-card">
        <h3 class="section-title">
          I miei Ordini
          <c:if test="${not empty ordiniUtente}">
            <span style="color:#666; font-size:.9rem; font-weight:normal;">(${ordiniUtente.size()} ordini)</span>
          </c:if>
        </h3>

        <c:choose>
          <c:when test="${not empty ordiniUtente}">
            <c:forEach var="ordine" items="${ordiniUtente}">
              <a href="${pageContext.request.contextPath}/utente/ordine?id=${ordine.idOrdine}" class="order-card-link">
                <div class="order-card">
                  <div class="order-header">
                    <div class="order-id">Ordine #${ordine.idOrdine}</div>
                    <div class="order-status">${ordine.stato}</div>
                  </div>

                  <div class="order-details">
                    <div class="order-detail-item">
                      <span class="order-detail-label">Totale</span>
                      <span class="order-detail-value order-total-value">
                        <c:choose>
                          <c:when test="${ordine.totale != null and ordine.totale > 0}">
                            <fmt:formatNumber value="${ordine.totale}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                          </c:when>
                          <c:otherwise>€0,00</c:otherwise>
                        </c:choose>
                      </span>
                    </div>

                    <div class="order-detail-item">
                      <span class="order-detail-label">Metodo di Pagamento</span>
                      <span class="order-detail-value text-capitalize">
                        <c:out value="${ordine.metodoDiPagamento}" default="Non specificato"/>
                      </span>
                    </div>

                    <c:if test="${not empty ordine.metodoDiSpedizione}">
                      <div class="order-detail-item">
                        <span class="order-detail-label">Spedizione</span>
                        <span class="order-detail-value text-capitalize">
                            ${ordine.metodoDiSpedizione}
                        </span>
                      </div>
                    </c:if>

                    <c:if test="${ordine.scontoTotale != null and ordine.scontoTotale > 0}">
                      <div class="order-detail-item">
                        <span class="order-detail-label">Sconto Applicato</span>
                        <span class="order-detail-value order-discount">
                          -<fmt:formatNumber value="${ordine.scontoTotale}" type="currency" currencySymbol="€" minFractionDigits="2"/>
                        </span>
                      </div>
                    </c:if>
                  </div>

                  <div class="order-footer">
                    <span class="order-cta">Clicca per vedere i dettagli →</span>
                  </div>
                </div>
              </a>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <div class="empty-orders">
              <div class="empty-orders-icon"></div>
              <h4>Nessun ordine trovato</h4>
              <p>Non hai ancora effettuato ordini.</p>
              <a href="${pageContext.request.contextPath}/pages" class="btn-cta">Inizia a fare shopping!</a>
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
    document.querySelectorAll('.tab-content').forEach(tab => tab.classList.remove('active'));
    // Rimuovi active da tutti i bottoni
    document.querySelectorAll('.tab-button').forEach(btn => btn.classList.remove('active'));
    // Mostra il tab selezionato
    document.getElementById('tab-' + tabName).classList.add('active');
    // Attiva il bottone corrispondente
    document.getElementById('tab-' + tabName + '-btn').classList.add('active');
  }

  function validatePasswordForm() {
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (newPassword !== confirmPassword) {
      alert('Le nuove password non coincidono');
      return false;
    }
    if (newPassword.length < 6) {
      alert('La password deve essere di almeno 6 caratteri');
      return false;
    }
    return true;
  }

  // Scheda iniziale basata su parametri URL
  //Metodo per registrare un listner su un evento
  document.addEventListener('DOMContentLoaded', function() {

    //Oggetto che contiene info sull'URL corrente
    const urlParams = new URLSearchParams(window.location.search);
    //Ottiene il valore del parmetro TAB
    const tab = urlParams.get('tab');

    if (tab === 'password') {
      showTab('password');
    } else if (tab === 'orders') {
      showTab('orders');
    } else {
      showTab('edit');
    }
  });
</script>

</body>
</html>
