<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

<jsp:useBean id="categoria" class="techvibe.categoria.Categoria" scope="request"/>

<c:set var="isCreate" value="${categoria.idCategoria==0}"/>

<c:if test="${not empty alert}">
  <%@include file="../partials/alert.jsp"%>

</c:if>

<form method="post" action="techvibe/categorie/${isCreate ? 'create':'update'}">

  <c:if test="${not isCreate}">
    <input type="hidden" name="id" value="${categoria.idCategoria}">

  </c:if>

  <fieldset class="grid-x cell category-form">
   <legend>${isCreate ? 'Crea':'Aggiorna'} Categoria </legend>
    <label for = "label" class="field cell w40" >
      <input id="label" name="label" placeholder="Nome" type="text" value="${categoria.nomeCategoria}">
    </label>

    <button type="submit" class="cell w40 btn primary">${isCreate ?'Crea':'Aggiorna' }
    </button>
  </fieldset>

</form>








</form>