<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
  <title>Gestisci utente</title>
  <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">

  <!-- CSS esterno della pagina utente -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/crm/manage-utente.css"/>

  <jsp:include page="../partials/head.jsp">
    <jsp:param name="title" value="Dettaglio utente"/>
    <jsp:param name="styles" value="crm,dashboard,manage-utente"/>
    <jsp:param name="scripts" value="crm"/>
  </jsp:include>
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
            <p class="muted-meta">${utente.email}</p>
          </div>
          <div>
            <span class="utente-status ${utente.attivo ? 'status-attivo' : 'status-disattivato'}">
              ${utente.attivo ? 'ATTIVO' : 'DISATTIVATO'}
            </span>
            <c:if test="${utente.admin}">
              <span class="utente-status status-admin">
                ADMIN
              </span>
            </c:if>
          </div>
        </div>

        <!-- Tabs -->
        <div class="utente-tabs">
          <button class="tab-button active" onclick="showTab(event,'info')">Informazioni</button>
          <button class="tab-button" onclick="showTab(event,'orders')">Ordini (${not empty ordiniUtente ? ordiniUtente.size() : 0})</button>
          <button class="tab-button" onclick="showTab(event,'edit')">Modifica contatti</button>
        </div>

        <!-- Tab Informazioni -->
        <div id="tab-info" class="tab-content active">
          <div class="info-grid">
            <div class="info-card">
              <h3 class="section-title">Dati personali</h3>

              <div class="info-item">
                <div class="info-label">Nome completo</div>
                <div class="info-value">${utente.nome} ${utente.cognome}</div>
              </div>

              <div class="info-item">
                <div class="info-label">Email</div>
                <div class="info-value">${utente.email}</div>
              </div>

              <div class="info-item">
                <div class="info-label">Tipo account</div>
                <div class="info-value">${utente.admin ? 'Amministratore' : 'Utente normale'}</div>
              </div>
            </div>

            <div class="info-card">
              <h3 class="section-title">Contatti</h3>

              <div class="info-item">
                <div class="info-label">Telefono</div>
                <div class="info-value">
                  <c:choose>
                    <c:when test="${not empty utente.telefono}">
                      ${utente.telefono}
                    </c:when>
                    <c:otherwise>
                      <em class="valore-non-specificato">Non specificato</em>
                    </c:otherwise>
                  </c:choose>
                </div>
              </div>

              <div class="info-item">
                <div class="info-label">Indirizzo di spedizione</div>
                <div class="info-value">
                  <c:choose>
                    <c:when test="${not empty utente.indirizzoSpedizione}">
                      ${utente.indirizzoSpedizione}
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

        <!-- Tab Ordini -->
        <div id="tab-orders" class="tab-content">
          <div class="info-card">
            <h3 class="section-title">
              Ordini dell'utente
              <c:if test="${not empty ordiniUtente}">
                <span class="muted-count">(${ordiniUtente.size()} ordini)</span>
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
                        <span class="order-detail-value order-total">
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
                        <span class="order-detail-label">Metodo di pagamento</span>
                        <span class="order-detail-value text-cap">
                          <c:out value="${ordine.metodoDiPagamento}" default="Non specificato"/>
                        </span>
                      </div>

                      <c:if test="${not empty ordine.metodoDiSpedizione}">
                        <div class="order-detail-item">
                          <span class="order-detail-label">Spedizione</span>
                          <span class="order-detail-value text-cap">
                              ${ordine.metodoDiSpedizione}
                          </span>
                        </div>
                      </c:if>

                      <c:if test="${ordine.scontoTotale != null and ordine.scontoTotale > 0}">
                        <div class="order-detail-item">
                          <span class="order-detail-label">Sconto applicato</span>
                          <span class="order-detail-value order-discount">
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
                  <h4>Nessun ordine trovato</h4>
                  <p>Questo utente non ha ancora effettuato ordini.</p>
                </div>
              </c:otherwise>
            </c:choose>
          </div>
        </div>

        <!-- Tab Modifica Contatti -->
        <div id="tab-edit" class="tab-content">
          <div class="info-card">
            <h3 class="section-title">Modifica contatti</h3>

            <form method="POST" action="${pageContext.request.contextPath}/utente/update-contacts" class="edit-form">
              <input type="hidden" name="id" value="${utente.idUtente}">

              <div class="form-group">
                <label for="telefono">Telefono</label>
                <input type="tel" id="telefono" name="telefono" value="${utente.telefono}" placeholder="Inserisci numero di telefono">
              </div>

              <div class="form-group">
                <label for="indirizzo">Indirizzo di spedizione</label>
                <input type="text" id="indirizzo" name="indirizzo" value="${utente.indirizzoSpedizione}" placeholder="Via, Numero, Città, CAP">
              </div>

              <button type="submit" class="btn primary">Aggiorna contatti</button>
            </form>
          </div>
        </div>

        <!-- Azioni -->
        <div class="actions">
          <a href="${pageContext.request.contextPath}/utente/" class="link-back">← Torna alla lista</a>

          <c:if test="${sessionScope.utenteSession != null && sessionScope.utenteSession.admin}">
            <form method="post"
                  action="${pageContext.request.contextPath}/utente/delete"
                  onsubmit="return confirm('Confermi l\'eliminazione definitiva di ${utente.nome} ${utente.cognome}?');"
                  class="form-delete">
              <input type="hidden" name="id" value="${utente.idUtente}">
              <button type="submit" class="btn danger">Elimina utente</button>
            </form>
          </c:if>
        </div>

      </section>
    </div>
  </section>
</main>

<script>
  function showTab(evt, tabName) {
    document.querySelectorAll('.tab-content').forEach(tab => tab.classList.remove('active'));
    document.querySelectorAll('.tab-button').forEach(btn => btn.classList.remove('active'));

    const el = document.getElementById('tab-' + tabName);
    if (el) el.classList.add('active');

    if (evt && evt.currentTarget) evt.currentTarget.classList.add('active');
  }
</script>

</body>
</html>
