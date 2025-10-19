<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Gestisci Prodotti</title>
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="TechVibe"/>
        <jsp:param name="styles" value="crm,prodotti"/>
        <jsp:param name="scripts" value="crm,home"/>
    </jsp:include>

    <style >
        /* Per tutti i link */
        a, a:visited {
            color: var(--primary-light);
            text-decoration: none;
        }

        /* Per i link nella navbar */
        .nav-links a:visited, .menu a:visited {
            color: white;
        }

    </style>
</head>

<body>
<main class="app">

    <%@ include file="../partials/crm/sidebar.jsp" %>

    <section class="content grid-y">
        <%@ include file="../partials/crm/header.jsp" %>

        <div class="body grid-x justify-center">
            <section class="grid-y cell prodotti">
                <%@ include file="../prodotto/table.jsp" %>

                <jsp:include page="../partials/paginator.jsp">
                    <jsp:param name="resource" value="prodotti"/>
                </jsp:include>
            </section>
        </div>



    </section>

</main>
</body>
</html>
