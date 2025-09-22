
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title></title>
</head>
<jsp:include page="../partials/head.jsp">
    <jsp:param name="title" value="Dashboard - TechVibe"/>
    <jsp:param name="styles" value="crm,dashboard"/>
    <jsp:param name="scripts" value="crm,home"/>
</jsp:include>

<style>
    .dashboard-container {
        margin-bottom: 5rem;
    }

    .welcome-header {
        background: var(--primary-light);
        color: white;
        padding: 2rem;
        border-radius: 12px;
        margin-bottom: 2rem;
        text-align: center;
    }

    .welcome-header h2 {
        margin: 0 0 0.5rem 0;
        font-size: 2.2rem;
    }

    .welcome-header p {
        margin: 0;
        opacity: 0.9;
        font-size: 1.1rem;
    }

    .info-grid {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 2rem;
        margin-bottom: 2rem;
    }

    .info-card {
        background: white;
        border: 1px solid #eee;
        border-radius: 12px;
        padding: 2rem;
    }

    .info-item {
        margin-bottom: 1.5rem;
    }

    .info-label {
        font-weight: 600;
        color: var(--primary-light);
        margin-bottom: 0.5rem;
    }

    .info-value {
        color: #333;
        font-size: 1rem;
        padding: 0.75rem 1rem;
        background: var(--light-gray);
        border-radius: 8px;
    }

    .status-online {
        color: #28a745;
        font-weight: 600;
    }

    @media (max-width: 768px) {
        .info-grid {
            grid-template-columns: 1fr;
        }

        .welcome-header {
            padding: 1.5rem;
        }

        .welcome-header h2 {
            font-size: 1.8rem;
        }
    }
</style>

<body>

<main class="app">
    <%@include file="../partials/crm/sidebar.jsp"%>
    <section class="content grid-y">
        <%@include file="../partials/crm/header.jsp"%>
        <div class="body grid-x justify-center">

            <section class="grid-y cell w75 dashboard-container">

                <jsp:include page="../partials/site/alert.jsp" />

                <!-- Header Benvenuto -->
                <div class="welcome-header">
                    <h2>Benvenuto, ${sessionScope.utenteSession.nome}!</h2>
                    <p>Dashboard Amministratore - <fmt:formatDate value="<%=new java.util.Date()%>" pattern="EEEE, dd MMMM yyyy"/></p>
                </div>

                <!-- Informazioni Sistema -->
                <div class="info-grid">
                    <div class="info-card">
                        <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Sessione Attiva</h3>

                        <div class="info-item">
                            <div class="info-label">Utente</div>
                            <div class="info-value">${sessionScope.utenteSession.nome} ${sessionScope.utenteSession.cognome}</div>
                        </div>

                        <div class="info-item">
                            <div class="info-label">Ruolo</div>
                            <div class="info-value">Amministratore</div>
                        </div>

                        <div class="info-item">
                            <div class="info-label">Login effettuato</div>
                            <div class="info-value">
                                <fmt:formatDate value="<%=new java.util.Date()%>" pattern="HH:mm"/>
                            </div>
                        </div>
                    </div>

                    <div class="info-card">
                        <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Stato Sistema</h3>

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