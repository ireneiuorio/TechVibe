<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%
  List<String> uploadedList = (List<String>) request.getAttribute("uploadedList");
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Creazione prodotto</title>
  <script>
    window.addEventListener('DOMContentLoaded', function(){
      document.getElementById('autoForm').submit();
    });
  </script>
</head>
<body>
<form id="autoForm" action="${pageContext.request.contextPath}/prodotti/create" method="post">

  <!-- Campi prodotto -->
  <input type="hidden" name="modello" value="${param.modello}">
  <input type="hidden" name="marca" value="${param.marca}">
  <input type="hidden" name="prezzo" value="${param.prezzo}">
  <input type="hidden" name="dimensioneSchermo" value="${param.dimensioneSchermo}">
  <input type="hidden" name="connettivita" value="${param.connettivita}">
  <input type="hidden" name="sistemaOperativo" value="${param.sistemaOperativo}">
  <input type="hidden" name="qtDisponibile" value="${param.qtDisponibile}">
  <input type="hidden" name="colore" value="${param.colore}">
  <input type="hidden" name="storage" value="${param.storage}">
  <input type="hidden" name="ram" value="${param.ram}">
  <input type="hidden" name="idCategoria" value="${param.idCategoria}">

  <!-- Tutte le immagini -->
  <%
    if (uploadedList != null) {
      for (String img : uploadedList) {
  %>
  <input type="hidden" name="gallery" value="<%= img %>">
  <%
      }
    }
  %>

  <noscript><button type="submit">Continua</button></noscript>
</form>
</body>
</html>
