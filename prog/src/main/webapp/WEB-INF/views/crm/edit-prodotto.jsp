<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
  <title></title>

  <jsp:include page="../partials/head.jsp">
    <jsp:param name="title" value="Gestione Prodotto"/>
    <jsp:param name="styles" value="crm,dashboard"/>
    <jsp:param name="scripts" value="crm"/>
  </jsp:include>

  <style>
    .product-form { margin-bottom: 5rem; }
    .field > span {
      display: block;
      font-size: .7rem;
      margin-bottom: .25rem;
      color: #444;
      font-weight: 400;
    }
  </style>
</head>
<body>

<main class="app">
  <%@include file="../partials/crm/sidebar.jsp"%>
  <section class="content grid-y">
    <%@include file="../partials/crm/header.jsp"%>
    <div class="body grid-x justify-center">
      <section class="grid-y cell w75">

        <jsp:include page="../partials/site/alert.jsp" />

        <form method="post" action="${pageContext.request.contextPath}/prodotti/update">
          <input type="hidden" name="id" value="${prodotto.idProdotto}" />

          <fieldset class="grid-y cell product-form">
            <legend>Modifica Prodotto</legend>

            <label class="field cell w50">
              <span>Modello</span>
              <input id="Modello" name="modello" placeholder="Modello" type="text"
                     value="${prodotto.modello}" required>
            </label>

            <label class="field cell w50">
              <span>Marca</span>
              <input id="Marca" name="marca" placeholder="Marca" type="text"
                     value="${prodotto.marca}" required>
            </label>

            <label class="field cell w50">
              <span>Sistema Operativo</span>
              <input id="SistemaOperativo" name="sistemaOperativo" placeholder="Sistema Operativo" type="text"
                     value="${prodotto.sistemaOperativo}">
            </label>

            <label class="field cell w50">
              <span>Connettività</span>
              <input id="Connettivita" name="connettivita" placeholder="Connettività" type="text"
                     value="${prodotto.connettivita}">
            </label>

            <label class="field cell w50">
              <span>Colore</span>
              <input id="Colore" name="colore" placeholder="Colore" type="text"
                     value="${prodotto.colore}">
            </label>

            <label class="field cell w50">
              <span>Storage (GB)</span>
              <input id="Storage" name="storage" placeholder="Storage" type="number" min="0"
                     value="${prodotto.storage}">
            </label>

            <label class="field cell w50">
              <span>RAM (GB)</span>
              <input id="Ram" name="ram" placeholder="Ram" type="number" min="0"
                     value="${prodotto.ram}">
            </label>

            <label class="field cell w50">
              <span>Quantità disponibile</span>
              <input id="QuantitaDisponibile" name="qtDisponibile" placeholder="Quantità disponibile" type="number" min="0"
                     value="${prodotto.qtDisponibile}">
            </label>

            <label class="field cell w50">
              <span>Prezzo</span>
              <input id="Prezzo" name="prezzo" placeholder="Prezzo" type="number" step="0.01" min="0"
                     value="${prodotto.prezzo}">
            </label>

            <label class="field cell w50">
              <span>Dimensione schermo (pollici)</span>
              <input id="DimensioneSchermo" name="dimensioneSchermo" placeholder="Dimensione schermo (pollici)" type="number" step="0.1" min="0"
                     value="${prodotto.dimensioneSchermo}">
            </label>

            <label class="field cell w50">
              <span>Categoria</span>
              <select name="idCategoria" id="CategoriaId" required>
                <option value="1" ${prodotto.categoria.idCategoria == 1 ? 'selected' : ''}>Smartphone</option>
                <option value="2" ${prodotto.categoria.idCategoria == 2 ? 'selected' : ''}>Tablet</option>
              </select>
            </label>

            <!-- Azioni -->
            <button type="submit" class="cell w75 btn primary">Salva modifiche</button>

          </fieldset>
        </form>

      </section>
    </div>
  </section>
</main>

</body>
</html>
