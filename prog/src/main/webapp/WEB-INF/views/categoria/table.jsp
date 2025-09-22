<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!-- Contenitore principale -->
<div class="prodotti-container">
    <!-- Pulsante per creare un nuovo prodotto -->



    <!-- Wrapper scrollabile per la tabella -->
    <div class="table-wrapper">
        <table class="prodotti-table">
            <thead>
            <tr>
                <th>IdCategoria</th>
                <th>Nome Categoria</th>

            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty categorie}">
                    <tr>
                        <td colspan="15">Nessuna categoria presente</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${categorie}" var="categoria">
                        <tr>
                            <td>${categoria.idCategoria}</td>
                            <td>${categoria.nomeCategoria}</td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
</div>





