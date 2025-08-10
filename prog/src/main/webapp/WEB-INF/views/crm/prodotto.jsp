<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>

<jsp:include page="../partials/head.jsp">
    <jsp:param name="title" value="TechVibe"/>
    <jsp:param name="styles" value="crm,dashboard"/>
    <jsp:param name="scripts" value="crm,home"/>
</jsp:include>

<style>
    .product-form>{
        margin-bottom: 5rem;

    }
</style>
</head>
<body>

<main class="app">
    <%@include file="../partials/crm/sidebar.jsp"%>
    <section class="content grid-y" >
        <%@include file="../partials/crm/header.jsp"%>
        <div class="body grid-x justify-center">

            <form method="get" action="prog_war/prodotti/create">
                <fieldset class="grid-y cell product-form">
                    <legend>Crea Prodotto</legend>
                    <label for="IdProdotto" class="field cell w50">
                        <input id="IdProdotto" name="IdProdotto" placeholder="Id Prodotto" type="text">
                    </label>

                    <label for="Modello" class="field cell w50">
                        <input id="Modello" name="Modello" placeholder="Modello" type="text">
                    </label>

                    <label for="Marca" class="field cell w50">
                        <input id="Marca" name="Marca" placeholder="Marca" type="text">
                    </label>

                    <label for="SistemaOperativo" class="field cell w50">
                        <input id="SistemaOperativo" name="SistemaOperativo" placeholder="Sistema Operativo" type="text">
                    </label>

                    <label for="Connettivita" class="field cell w50">
                        <input id="Connettivita" name="Connettivita" placeholder="Connettività" type="text">
                    </label>

                    <label for="Colore" class="field cell w50">
                        <input id="Colore" name="Colore" placeholder="Colore" type="text">
                    </label>

                    <label for="Storage" class="field cell w50">
                        <input id="Storage" name="Storage" placeholder="Storage" type="text">
                    </label>

                    <label for="Ram" class="field cell w50">
                        <input id="Ram" name="Ram" placeholder="Ram" type="text">
                    </label>

                    <label for="QuantitaDisponibile" class="field cell w50">
                        <input id="QuantitaDisponibile" name="QuantitaDisponibile" placeholder="Quantità disponibile" type="number" min="0">
                    </label>

                    <label for="Prezzo" class="field cell w50">
                        <input id="Prezzo" name="Prezzo" placeholder="Prezzo" type="number" step="0.01" min="0">
                    </label>

                    <label for="CategoriaId" class="field cell w50">
                        <select name="CategoriaId" id="CategoriaId">
                            <option value="1">Smartphone</option>
                            <option value="2">Tablet</option>
                        </select>

                    </label>
                    <label for="cover" class="field cell w50">
                        <input type="file" name="cover" id="cover">
                    </label>

                    <button type="submit" class="cell w50 btn primary">Crea Prodotto</button>

                </fieldset>
            </form>
        </div>

        <%@include file="../partials/crm/footer.jsp"%>
    </section>

</main>

</body>
</html>
