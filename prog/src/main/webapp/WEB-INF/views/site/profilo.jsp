<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

    @media (max-width: 768px) {
      .info-grid {
        grid-template-columns: 1fr;
      }

      .profile-tabs {
        flex-direction: column;
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
      <button class="tab-button" onclick="showTab('edit')">Modifica Profilo</button>
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
    <div id="tab-edit" class="tab-content">
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
        <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">I miei Ordini</h3>
        <p style="color: #666; text-align: center; padding: 2rem;">
          Non hai ancora effettuato ordini. <br>
          <a href="${pageContext.request.contextPath}/pages/smartphone" style="color: var(--primary-light);">
            Inizia a fare shopping!
          </a>
        </p>
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
</script>

</body>
</html>