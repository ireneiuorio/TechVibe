<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty alert and not empty alert.messages}">
  <div class="notification ${alert.type}">
    <ol class="cell">
      <c:forEach var="msg" items="${alert.messages}">
        <li>${msg}</li>
      </c:forEach>
    </ol>
    <span id="notification-close" class="close">
            <img id="hamburger1" src="<%= request.getContextPath() %>/icons/x.svg" alt="Chiudi">
        </span>
  </div>
</c:if>
