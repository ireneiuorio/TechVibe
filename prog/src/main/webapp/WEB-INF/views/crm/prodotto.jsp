<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Aggiunta qui in alto -->

<!DOCTYPE html>
<html>
<head>
    <title>Crea Prodotto</title>
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">

    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="Gestione Prodotto"/>
        <jsp:param name="styles" value="crm,dashboard"/>
        <jsp:param name="scripts" value="crm"/>
    </jsp:include>

    <style>
        .product-form {
            margin-bottom: 4rem;
        }
    </style>
</head>
<body>

<main class="app">
    <%@include file="../partials/crm/sidebar.jsp" %>
    <section class="content grid-y">
        <%@include file="../partials/crm/header.jsp" %>
        <div class="body grid-x justify-center">

            <section class="grid-y cell w75">


                <jsp:include page="../partials/site/alert.jsp"/>

                <form method="post" action="${pageContext.request.contextPath}/Upload" enctype="multipart/form-data">

                    <fieldset class="grid-y cell product-form">
                        <legend>Crea Prodotto</legend>

                        <label class="field cell w50">
                            <input id="Modello" name="modello" placeholder="Modello" type="text" required>
                        </label>

                        <label class="field cell w50">
                            <input id="Marca" name="marca" placeholder="Marca" type="text" required>
                        </label>

                        <label class="field cell w50">
                            <input id="SistemaOperativo" name="sistemaOperativo" placeholder="Sistema Operativo"
                                   type="text">
                        </label>

                        <label class="field cell w50">
                            <input id="Connettivita" name="connettivita" placeholder="Connettività" type="text">
                        </label>

                        <label class="field cell w50">
                            <input id="Colore" name="colore" placeholder="Colore" type="text">
                        </label>

                        <label class="field cell w50">
                            <input id="Storage" name="storage" placeholder="Storage" type="number" min="0">
                        </label>

                        <label class="field cell w50">
                            <input id="Ram" name="ram" placeholder="Ram" type="number" min="0">
                        </label>

                        <label class="field cell w50">
                            <input id="QuantitaDisponibile" name="qtDisponibile" placeholder="Quantità disponibile"
                                   type="number" min="0">
                        </label>

                        <label class="field cell w50">
                            <input id="Prezzo" name="prezzo" placeholder="Prezzo" type="number" step="0.01" min="0">
                        </label>

                        <label class="field cell w50">
                            <input id="DimensioneSchermo" name="dimensioneSchermo"
                                   placeholder="Dimensione schermo (pollici)" type="number" step="0.1" min="0">
                        </label>

                        <label class="field cell w50">
                            <input id="PercentualeSconto" name="percentualeSconto" placeholder="Percentuale Sconto %"
                                   type="number" step="0.1" min="0">
                        </label>

                        <label class="field cell w50">
                            <select name="idCategoria" id="CategoriaId">
                                <option value="1">Smartphone</option>
                                <option value="2">Tablet</option>
                            </select>
                        </label>

                        <label>Immagini (max 4):</label>
                        <input type="file" name="file" accept="image/*" multiple
                               onchange="if(this.files.length>4){alert('Massimo 4 immagini'); this.value='';}">


                        <button type="submit" class="cell w75 btn primary">Carica immagini e crea</button>

                    </fieldset>
                </form>


        </div>
    </section>
</main>

</body>
</html>

