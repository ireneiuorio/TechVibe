<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%
  List<String> uploadedList = (List<String>) request.getAttribute("uploadedList");

 // prende un attributo della request HTTP chiamato "uploadedList" e lo trasforma in una lista di stringhe.
  // Estrai fino a 4 immagini dalla lista
  String img1 = (uploadedList != null && uploadedList.size() > 0) ? uploadedList.get(0) : "";
  String img2 = (uploadedList != null && uploadedList.size() > 1) ? uploadedList.get(1) : "";
  String img3 = (uploadedList != null && uploadedList.size() > 2) ? uploadedList.get(2) : "";
  String img4 = (uploadedList != null && uploadedList.size() > 3) ? uploadedList.get(3) : "";
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
  <input type="hidden" name="percentualeSconto" value="${param.percentualeSconto}">
  <input type="hidden" name="idCategoria" value="${param.idCategoria}">



  <!-- Quattro immagini -->
  <input type="hidden" name="immagine1" value="<%= img1 %>">
  <input type="hidden" name="immagine2" value="<%= img2 %>">
  <input type="hidden" name="immagine3" value="<%= img3 %>">
  <input type="hidden" name="immagine4" value="<%= img4 %>">

  <noscript><button type="submit">Continua</button></noscript>
</form>



</body>
</html>