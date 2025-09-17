<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!-- Contenitore principale -->
<div class="prodotti-container">
  <!-- Pulsante per creare un nuovo prodotto -->
  <a href="${pageContext.request.contextPath}/prodotti/create" class="btn primary">Crea Prodotto</a>
  <h3 style="color: var(--primary-light)">Clicca sull'id per eliminare o modificare un prodotto</h3>

  <!-- Wrapper scrollabile per la tabella -->
  <div class="table-wrapper">
    <table class="prodotti-table">
      <thead>
      <tr>
        <th>IdProdotto</th>
        <th>Dimensione Schermo</th>
        <th>Connettività</th>
        <th>Prezzo</th>
        <th>Modello</th>
        <th>Marca</th>
        <th>Sistema Operativo</th>
        <th>Qt Disponibile</th>
        <th>Colore</th>
        <th>Storage</th>
        <th>RAM</th>
        <th>Percentuale Sconto</th>
        <th>IdCategoria</th>
        <th>Immagine 1</th>
        <th>Immagine 2</th>
        <th>Immagine 3</th>
        <th>Immagine 4</th>
      </tr>
      </thead>
      <tbody>
      <c:choose>
        <c:when test="${empty prodotti}">
          <tr>
            <td colspan="15">Nessun prodotto presente</td>
          </tr>
        </c:when>
        <c:otherwise>
          <c:forEach items="${prodotti}" var="prodotto">
            <tr>
              <td><a href="${pageContext.request.contextPath}/prodotti/manage?id=${prodotto.idProdotto}">${prodotto.idProdotto}</a></td>
              <td>${prodotto.dimensioneSchermo}</td>
              <td>${prodotto.connettivita}</td>
              <td>${prodotto.prezzo}</td>
              <td>${prodotto.modello}</td>
              <td>${prodotto.marca}</td>
              <td>${prodotto.sistemaOperativo}</td>
              <td>${prodotto.qtDisponibile}</td>
              <td>${prodotto.colore}</td>
              <td>${prodotto.storage}</td>
              <td>${prodotto.ram}</td>
              <td>${prodotto.percentualeSconto}</td>
              <td>${prodotto.categoria.idCategoria}</td>
              <td>${prodotto.immagine1}</td>
              <td>${prodotto.immagine2}</td>
              <td>${prodotto.immagine3}</td>
              <td>${prodotto.immagine4}</td>


            </tr>
          </c:forEach>
        </c:otherwise>
      </c:choose>
      </tbody>
    </table>
  </div>
</div>