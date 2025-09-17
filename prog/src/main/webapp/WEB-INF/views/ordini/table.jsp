<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Contenitore principale -->
<div class="prodotti-container">

    <h3 style="color: var(--primary-light)">Clicca sull'id per gestire un ordine</h3>

    <!-- Wrapper scrollabile per la tabella -->
    <div class="table-wrapper">
        <table class="prodotti-table">
            <thead>
            <tr>
                <th>IdOrdine</th>
                <th>IdUtente</th>
                <th>Stato</th>
                <th>Totale</th>
                <th>ScontoTotale</th>
                <th>MetodoSpedizione</th>
                <th>MetodoPagamento</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty ordini}">
                    <tr>
                        <td colspan="8">Nessun ordine presente</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${ordini}" var="ordine">
                        <tr>
                            <td><a  style="color: var(--primary-light);text-decoration: none" href="${pageContext.request.contextPath}/ordini/show?id=${ordine.idOrdine}">${ordine.idOrdine}</a></td>
                            <td>${ordine.utente.idUtente}</td>
                            <td>${ordine.stato}</td>
                            <td>${ordine.totale}</td>
                            <td>${ordine.scontoTotale}</td>
                            <td>
                                <c:out value="${ordine.metodoDiSpedizione}" default="-" />
                            </td>
                            <td>
                                <c:out value="${ordine.metodoDiPagamento}" default="-" />
                            </td>

                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
</div>