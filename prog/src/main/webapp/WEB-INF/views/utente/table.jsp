
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Contenitore principale -->
<div class="utenti-container">
  <!-- Pulsante per creare un nuovo utente admin -->
  <a href="${pageContext.request.contextPath}/utente/create" class="btn primary">Crea Admin</a>

  <!-- Wrapper scrollabile per la tabella -->
  <div class="table-wrapper">
    <table class="utenti-table">
      <thead>
      <tr>
        <th>IdUtente</th>
        <th>Nome</th>
        <th>Cognome</th>
        <th>Email</th>
        <th>Telefono</th>
        <th>Indirizzo Spedizione</th>
        <th>Ruolo</th>
        <th>Stato</th>
        <th>Azioni</th>
      </tr>
      </thead>
      <tbody>
      <c:choose>
        <c:when test="${empty listaUtenti}">
          <tr>
            <td colspan="9">Nessun utente presente</td>
          </tr>
        </c:when>
        <c:otherwise>
          <c:forEach items="${listaUtenti}" var="utente">
            <tr>
              <td>
                <a href="${pageContext.request.contextPath}/utente/show?id=${utente.idUtente}" style="text-decoration: none;color: var(--primary-light)">
                    ${utente.idUtente}
                </a>
              </td>
              <td>${utente.nome}</td>
              <td>${utente.cognome}</td>
              <td>${utente.email}</td>
              <td>
                <c:out value="${utente.telefono}" default="-" />
              </td>
              <td>
                <c:out value="${utente.indirizzoSpedizione}" default="-" />
              </td>
              <td>
                <c:choose>
                  <c:when test="${utente.admin}">
                    Admin
                  </c:when>
                  <c:otherwise>
                    Cliente
                  </c:otherwise>
                </c:choose>
              </td>
              <td>
                <c:choose>
                  <c:when test="${utente.stato == 'ATTIVO'}">
                    <span class="stato-attivo">Attivo</span>
                  </c:when>
                  <c:otherwise>
                    <span class="stato-disattivo">Disattivato</span>
                  </c:otherwise>
                </c:choose>
              </td>
              <td>
                <c:if test="${!utente.admin}">
                  <c:choose>
                    <c:when test="${utente.stato == 'ATTIVO'}">
                      <form method="post" action="${pageContext.request.contextPath}/utente/cambiastato" style="display: inline;">
                        <input type="hidden" name="id" value="${utente.idUtente}">
                        <input type="hidden" name="azione" value="disattiva">
                        <button type="submit" class="btn-disattiva" onclick="return confirm('Sei sicuro di voler disattivare questo utente?')">
                          Disattiva
                        </button>
                      </form>
                    </c:when>
                    <c:otherwise>
                      <form method="post" action="${pageContext.request.contextPath}/utente/cambiastato" style="display: inline;">
                        <input type="hidden" name="id" value="${utente.idUtente}">
                        <input type="hidden" name="azione" value="attiva">
                        <button type="submit" class="btn-attiva" onclick="return confirm('Sei sicuro di voler attivare questo utente?')">
                          Attiva
                        </button>
                      </form>
                    </c:otherwise>
                  </c:choose>
                </c:if>
                <c:if test="${utente.admin}">
                  <span class="admin-label">Admin</span>
                </c:if>
              </td>
            </tr>
          </c:forEach>
        </c:otherwise>
      </c:choose>
      </tbody>
    </table>
  </div>
</div>

<style>
  /* Stili per gli stati */
  .stato-attivo {
    color: #28a745;
    font-weight: 500;
  }

  .stato-disattivo {
    color: #dc3545;
    font-weight: 500;
  }

  /* Stili per i pulsanti */
  .btn-attiva {
    background: #28a745;
    color: white;
    border: none;
    padding: 0.4rem 0.8rem;
    border-radius: 4px;
    cursor: pointer;
    font-size: 0.85rem;
    transition: background 0.3s;
  }

  .btn-attiva:hover {
    background: #218838;
  }

  .btn-disattiva {
    background: #dc3545;
    color: white;
    border: none;
    padding: 0.4rem 0.8rem;
    border-radius: 4px;
    cursor: pointer;
    font-size: 0.85rem;
    transition: background 0.3s;
  }

  .btn-disattiva:hover {
    background: #c82333;
  }

  .admin-label {
    color: #6c757d;
    font-style: italic;
    font-size: 0.85rem;
  }

  /* Responsive per la tabella */
  .table-wrapper {
    overflow-x: auto;
  }

  .utenti-table {
    min-width: 800px;
  }
</style>