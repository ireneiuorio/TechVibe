<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1,viewport-fit=cover">
<title>${param.title}</title>
<meta name="description" content="Vendita di smartphone e tablet">
<link rel="icon" type="image/png" href="images/logo.png">

<meta name="format-detection" content="telephone=no"><%-- evita numeri trattati come telefono --%>

<%-- Apple --%>
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-title" content="TechVibe">
<meta name="apple-mobile-web-app-status-bar-style" content="default">
<link rel="apple-touch-icon" href="images/logo.png">
<link rel="apple-touch-startup-image" href="images/logo.png">

<%-- Android --%>
<meta name="theme-color" content="#A8D0E6">

<%-- CSS e JS generico --%>
<link href="css/library.css" rel="stylesheet">
<c:if test="${not empty param.style}">
    <link rel="stylesheet" href="css/${param.style}">
</c:if>

<script src="js/library.js" defer></script>
<c:if test="${not empty param.script}">
    <script src="js/${param.script}" defer></script>
</c:if>


