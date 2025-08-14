<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title>TechVibe</title>
    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="TechVibe"/>
        <jsp:param name="styles" value="crm,prodotti"/>
        <jsp:param name="scripts" value="crm,home"/>
    </jsp:include>
</head>

<body>
<main class="app">

    <%@ include file="../partials/crm/sidebar.jsp" %>

    <section class="content grid-y">
        <%@ include file="../partials/crm/header.jsp" %>

        <div class="body grid-x justify-center">
            <section class="grid-y cell prodotti">
                <%@ include file="../prodotto/table.jsp" %>
            </section>
        </div>


        <jsp:include page="../partials/site/paginator.jsp">
            <jsp:param name="resource" value="prodotti"/>
        </jsp:include>

        <%@ include file="../partials/crm/footer.jsp" %>
    </section>

</main>
</body>
</html>
