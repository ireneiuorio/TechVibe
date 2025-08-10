
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title></title>
</head>
<jsp:include page="../partials/head.jsp">
    <jsp:param name="title" value="TechVibe"/>
    <jsp:param name="styles" value="crm,prodotti"/>
    <jsp:param name="scripts" value="crm,home"/>
</jsp:include>



<body>

<main class="app">
    <%@include file="../partials/crm/sidebar.jsp"%>
    <section class="content grid-y" >
        <%@include file="../partials/crm/header.jsp"%>
        <div class="body grid-x justify-center">
            <section class="grid-y cell prodotti-table">
                <%@include file="../prodotto/table.jsp"%>
            </section>





        </div>

        <%@include file="../partials/crm/footer.jsp"%>
    </section>

</main>

</body>
</html>

