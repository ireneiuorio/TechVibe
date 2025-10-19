<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="it">
<head>
    <title>Pannello di Controllo · TechVibe</title>
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
</head>

<jsp:include page="../partials/head.jsp">
    <jsp:param name="title" value="Pannello di Controllo · TechVibe"/>
    <jsp:param name="styles" value="crm,dashboard,home-dashboardcrm"/>
    <jsp:param name="scripts" value="crm,home"/>
</jsp:include>

<body>
<main class="app">
    <%@ include file="../partials/crm/sidebar.jsp" %>

    <section class="content grid-y">
        <%@ include file="../partials/crm/header.jsp" %>

        <div class="body grid-x justify-center">
            <section class="grid-y cell w75 dashboard-container">

                <jsp:include page="../partials/site/alert.jsp" />

                <!-- Testata di benvenuto -->
                <div class="welcome-header">
                    <h2>Benvenuto, ${sessionScope.utenteSession.nome}!</h2>
                    <p>Pannello Amministratore ·
                        <fmt:formatDate value="<%= new java.util.Date() %>" pattern="EEEE, dd MMMM yyyy"/>
                    </p>
                </div>

                <!-- Informazioni di sistema -->
                <div class="info-grid">
                    <div class="info-card">
                        <h3 class="section-title">Sessione attiva</h3>

                        <div class="info-item">
                            <div class="info-label">Utente</div>
                            <div class="info-value">
                                ${sessionScope.utenteSession.nome} ${sessionScope.utenteSession.cognome}
                            </div>
                        </div>

                        <div class="info-item">
                            <div class="info-label">Ruolo</div>
                            <div class="info-value">Amministratore</div>
                        </div>

                        <div class="info-item">
                            <div class="info-label">Accesso effettuato alle</div>
                            <div class="info-value">
                                <fmt:formatDate value="<%= new java.util.Date() %>" pattern="HH:mm"/>
                            </div>
                        </div>
                    </div>

                    <div class="info-card">
                        <h3 class="section-title">Stato del sistema</h3>

                        <div class="info-item">
                            <div class="info-label">Piattaforma</div>
                            <div class="info-value">TechVibe E-commerce</div>
                        </div>

                        <div class="info-item">
                            <div class="info-label">Database</div>
                            <div class="info-value status-online">Connesso</div>
                        </div>
                    </div>
                </div>

            </section>
        </div>
    </section>
</main>
</body>
</html>
