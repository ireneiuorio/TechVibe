<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title> Home TechVibe</title>
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">

<jsp:include page="../partials/head.jsp">
    <jsp:param name="title" value="TechVibe"/>
    <jsp:param name="styles" value="site,dashboard"/>
    <jsp:param name="scripts" value="crm,home"/>
</jsp:include>



</head>

<body>

<%@include file="../partials/site/header.jsp"%>

<!-- Hero section -->
<section class="hero">
    <h1>Benvenuto su TechVibe</h1>
    <p>Il miglior e-commerce dedicato a smartphone e tablet, sempre a portata di click.</p>

    <form action="${pageContext.request.contextPath}/pages/chisiamo" method="get">
        <button type="submit" class="btn primary">Chi Siamo</button>
    </form>

</section>

<!-- Sezione categorie -->
<section class="categorie">
    <h2>Le nostre categorie</h2>


    <div class="grid">




    <a href="${pageContext.request.contextPath}/pages/smartphone"
       class="card-link">
        <div class="card">
            <p style="color: black">Smartphone</p>
        </div>
    </a>

    <a href="${pageContext.request.contextPath}/pages/tablet"
       class="card-link">
        <div class="card">
            <p style="color: black">Tablet</p>
        </div>
    </a>

    </div>


</section>
<%@include file="../partials/site/footer.jsp"%>

</body>
</html>
