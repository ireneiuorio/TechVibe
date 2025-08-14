<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Contenitore principale -->
<div class="utenti-container">
  <!-- Pulsante per creare un nuovo utente -->
  <a href="${pageContext.request.contextPath}/utenti/create" class="btn primary">Crea Utente</a>

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
      </tr>
      </thead>
      <tbody>
      <c:choose>
        <c:when test="${empty listaUtenti}">
          <tr>
            <td colspan="7">Nessun utente presente</td>
          </tr>
        </c:when>
        <c:otherwise>
          <c:forEach items="${listaUtenti}" var="utente">
            <tr>
              <td>
                <a href="${pageContext.request.contextPath}/utenti/show?ida=${utente.idUtente}">
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
            </tr>
          </c:forEach>
        </c:otherwise>
      </c:choose>
      </tbody>
    </table>
  </div>
</div>
