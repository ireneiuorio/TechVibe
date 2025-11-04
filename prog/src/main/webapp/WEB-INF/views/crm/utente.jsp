<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title> Crea admin</title>
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">

    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="Crea Amministratore"/>
        <jsp:param name="styles" value="crm,dashboard"/>
        <jsp:param name="scripts" value="crm"/>
    </jsp:include>

    <style>
        .utente-form {
            margin-bottom: 5rem;
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

                <form method="post" action="${pageContext.request.contextPath}/utente/create">
                    <fieldset class="grid-y cell utente-form">
                        <legend>Crea Amministratore</legend>

                        <label class="field cell w50">
                            <input id="nome" name="nome" placeholder="Nome" type="text" required>
                        </label>

                        <label class="field cell w50">
                            <input id="cognome" name="cognome" placeholder="Cognome" type="text" required>
                        </label>

                        <label class="field cell w50">
                            <input type="email" name="email" id="email" placeholder="Email" required>
                        </label>




                        <label class="field cell w50">
                            <input id="password" name="password" placeholder="Password" type="password" required>
                        </label>

                        <label class="field cell w50">
                            <input id="telefono" name="telefono" placeholder="Telefono" type="tel">
                        </label>

                        <label class="field cell w75">
                            <textarea name="indirizzo" id="indirizzo" placeholder="Indirizzo"></textarea>
                        </label>

                        <button type="submit" class="cell w75 btn primary">Crea Amministratore</button>

                    </fieldset>
                </form>

            </section>
        </div>
    </section>
</main>

</body>
</html>

<script>
    (function () {
        //Prendi il campo email
        const email = document.getElementById('email');

        // Se il server ha rilevato l'email occupata, imposta l'errore nativo
        <% if ("Email già esistente nel sistema".equals(request.getAttribute("errorMessage"))) { %>
        email.setCustomValidity('Email già registrata');
        // Mostra subito il messaggio nativo del browser
        email.reportValidity();
        <% } %>

        // Appena l'utente digita, rimuovi l'errore
        email.addEventListener('input', () => {
            email.setCustomValidity('');
        });
    })();
</script>
