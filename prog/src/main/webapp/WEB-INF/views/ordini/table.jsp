<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Contenitore principale -->
<div class="prodotti-container">
    <!-- Pulsante per creare un nuovo ordine -->
    <a href="${pageContext.request.contextPath}/ordini/create" class="btn primary">Crea Ordine</a>

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
                <th>Azioni</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty listaOrdini}">
                    <tr>
                        <td colspan="8">Nessun ordine presente</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${listaOrdini}" var="ordine">
                        <tr>
                            <td><a href="${pageContext.request.contextPath}/ordini/show?id=${ordine.idOrdine}">${ordine.idOrdine}</a></td>
                            <td>${ordine.idUtente}</td>
                            <td>${ordine.stato}</td>
                            <td>
                                <fmt:formatNumber value="${ordine.totale}" type="currency" currencySymbol="€"
                                                  minFractionDigits="2" maxFractionDigits="2"/>
                            </td>
                            <td>
                                <fmt:formatNumber value="${ordine.scontoTotale}" type="currency" currencySymbol="€"
                                                  minFractionDigits="2" maxFractionDigits="2"/>
                            </td>
                            <td>
                                <c:out value="${ordine.metodoDiSpedizione}" default="-" />
                            </td>
                            <td>
                                <c:out value="${ordine.metodoDiPagamento}" default="-" />
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/ordini/show?id=${ordine.idOrdine}">Visualizza</a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
</div>