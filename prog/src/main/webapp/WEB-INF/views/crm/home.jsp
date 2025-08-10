<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<jsp:include page="../partials/head.jsp">
    <jsp:param name="title" value="TechVibe"/>
    <jsp:param name="styles" value="crm,dashboard"/>
    <jsp:param name="scripts" value="crm,home"/>
</jsp:include>



<body>

<main class="app">
    <%@include file="../partials/crm/sidebar.jsp"%>
    <section class="content grid-y" >
        <%@include file="../partials/crm/header.jsp"%>
        <div class="body grid-x justify-center">

            <jsp:include page="../partials/crm/statscard.jsp">
                <jsp:param name="title" value="Clienti Registrati"/>
                <jsp:param name="stat" value="24"/>
            </jsp:include>

            <jsp:include page="../partials/crm/statscard.jsp">
                <jsp:param name="title" value="Prodotti in magazzino"/>
                <jsp:param name="stat" value="135"/>
            </jsp:include>

            <jsp:include page="../partials/crm/statscard.jsp">
                <jsp:param name="title" value="Incasso mensile"/>
                <jsp:param name="stat" value="0 Euro"/>
            </jsp:include>


            <jsp:include page="../partials/crm/statscard.jsp">
                <jsp:param name="title" value="Ordini Mensili"/>
                <jsp:param name="stat" value="133"/>
            </jsp:include>

        </div>

                <%@include file="../partials/crm/footer.jsp"%>
    </section>

</main>

</body>
</html>
