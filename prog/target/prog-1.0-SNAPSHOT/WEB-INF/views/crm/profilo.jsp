<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title></title>

    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="Profilo Amministratore"/>
        <jsp:param name="styles" value="crm,dashboard"/>
        <jsp:param name="scripts" value="crm"/>
    </jsp:include>

    <style>
        .admin-container {
            margin-bottom: 5rem;
        }

        .admin-header {
            background: var(--primary-light);
            color: white;
            padding: 2rem;
            border-radius: 12px;
            margin-bottom: 2rem;
            text-align: center;
        }

        .admin-header h2 {
            margin: 0 0 0.5rem 0;
            font-size: 2.2rem;
        }

        .admin-header p {
            margin: 0;
            opacity: 0.9;
            font-size: 1.1rem;
        }

        .admin-tabs {
            display: flex;
            background: var(--light-gray);
            border-radius: 12px;
            padding: 0.5rem;
            margin-bottom: 2rem;
        }

        .tab-button {
            flex: 1;
            padding: 1rem;
            background: none;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
            transition: all 0.3s;
        }

        .tab-button.active {
            background: var(--primary-light);
            color: white;
        }

        .tab-content {
            display: none;
        }

        .tab-content.active {
            display: block;
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

        .edit-form {
            display: grid;
            gap: 1.5rem;
        }

        .form-group {
            display: flex;
            flex-direction: column;
            gap: 0.5rem;
        }

        .form-group label {
            font-weight: 600;
            color: var(--primary-light);
        }

        .form-group input {
            padding: 0.75rem 1rem;
            border: 1px solid #ddd;
            border-radius: 8px;
            font-size: 1rem;
        }

        .form-group input:focus {
            border-color: var(--primary-light);
            outline: none;
        }

        .success-message {
            background: #d4edda;
            color: #155724;
            padding: 1rem;
            border-radius: 8px;
            margin-bottom: 1rem;
            border-left: 4px solid #28a745;
        }

        .error-message {
            background: #f8d7da;
            color: #721c24;
            padding: 1rem;
            border-radius: 8px;
            margin-bottom: 1rem;
            border-left: 4px solid #dc3545;
        }



        .requirements-list {
            background: #e7f3ff;
            border: 1px solid #b6d7ff;
            border-radius: 8px;
            padding: 1rem;
            margin-bottom: 1rem;
            font-size: 0.9rem;
            color: #0066cc;
        }

        .requirements-list ul {
            margin: 0.5rem 0 0 1.5rem;
            padding: 0;
        }

        .btn-danger {
            background: var(--primary-light);
            color: white;
            border: none;
            padding: 1rem 2rem;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
            font-size: 1rem;
            transition: all 0.3s;
        }

        .btn-danger:hover {
           background:var(--primary-light);
        }

        @media (max-width: 768px) {
            .admin-tabs {
                flex-direction: column;
            }

            .info-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>

<main class="app">
    <%@include file="../partials/crm/sidebar.jsp"%>
    <section class="content grid-y">
        <%@include file="../partials/crm/header.jsp"%>
        <div class="body grid-x justify-center">

            <section class="grid-y cell w75 admin-container">

                <jsp:include page="../partials/site/alert.jsp" />

                <!-- Header Admin -->
                <div class="admin-header">
                    <h2>Profilo Amministratore</h2>
                    <p>Gestisci le tue credenziali di accesso</p>
                </div>

                <!-- MESSAGGI -->
                <c:if test="${param.success == 'password_changed'}">
                    <div class="success-message">Password cambiata con successo!</div>
                </c:if>

                <c:if test="${param.success == 'email_changed'}">
                    <div class="success-message">Email cambiata con successo!</div>
                </c:if>

                <!-- Errori Password -->
                <c:if test="${param.error == 'missing_fields'}">
                    <div class="error-message">Tutti i campi sono obbligatori</div>
                </c:if>
                <c:if test="${param.error == 'password_mismatch'}">
                    <div class="error-message">Le nuove password non coincidono</div>
                </c:if>
                <c:if test="${param.error == 'password_too_short'}">
                    <div class="error-message">La nuova password deve essere di almeno 6 caratteri</div>
                </c:if>
                <c:if test="${param.error == 'wrong_current_password'}">
                    <div class="error-message">Password attuale non corretta</div>
                </c:if>
                <c:if test="${param.error == 'update_failed'}">
                    <div class="error-message">Errore durante l'aggiornamento</div>
                </c:if>
                <c:if test="${param.error == 'user_not_found'}">
                    <div class="error-message">Errore: utente non trovato. Prova a fare nuovamente login.</div>
                </c:if>

                <!-- Errori Email -->
                <c:if test="${param.error == 'invalid_email_format'}">
                    <div class="error-message">Formato email non valido</div>
                </c:if>
                <c:if test="${param.error == 'email_exists'}">
                    <div class="error-message">Questa email è già in uso da un altro utente</div>
                </c:if>
                <c:if test="${param.error == 'same_email'}">
                    <div class="error-message">La nuova email è identica a quella attuale</div>
                </c:if>

                <!-- Tabs -->
                <div class="admin-tabs">
                    <button class="tab-button active" onclick="showTab('info')">Informazioni</button>
                    <button class="tab-button" onclick="showTab('email')">Cambia Email</button>
                    <button class="tab-button" onclick="showTab('password')">Cambia Password</button>
                </div>

                <!-- Tab Informazioni -->
                <div id="tab-info" class="tab-content active">
                    <div class="info-grid">
                        <div class="info-card">
                            <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Dati Personali</h3>

                            <div class="info-item">
                                <div class="info-label">Nome Completo</div>
                                <div class="info-value">${admin.nome} ${admin.cognome}</div>
                            </div>

                            <div class="info-item">
                                <div class="info-label">Email</div>
                                <div class="info-value">${admin.email}</div>
                            </div>

                            <div class="info-item">
                                <div class="info-label">Ruolo</div>
                                <div class="info-value">Amministratore</div>
                            </div>
                        </div>

                        <div class="info-card">
                            <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Sicurezza Account</h3>

                            <div class="info-item">
                                <div class="info-label">Stato Account</div>
                                <div class="info-value">${admin.stato}</div>
                            </div>

                            <div class="info-item">
                                <div class="info-label">Tipo Account</div>
                                <div class="info-value">Amministratore</div>
                            </div>


                        </div>
                    </div>
                </div>

                <!-- Tab Cambio Email -->
                <div id="tab-email" class="tab-content">
                    <div class="info-card">
                        <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Cambia Email</h3>


                        <form method="POST" action="${pageContext.request.contextPath}/utente/admin-change-email" class="edit-form" onsubmit="return validateEmailForm()">
                            <div class="form-group">
                                <label for="currentPasswordEmail">Password Attuale</label>
                                <input type="password" id="currentPasswordEmail" name="currentPassword" required placeholder="Inserisci la tua password attuale">
                            </div>

                            <div class="form-group">
                                <label for="newEmail">Nuova Email</label>
                                <input type="email" id="newEmail" name="newEmail" required placeholder="Inserisci la nuova email">
                            </div>

                            <button type="submit" class="btn-danger">Cambia Email</button>
                        </form>
                    </div>
                </div>

                <!-- Tab Cambio Password -->
                <div id="tab-password" class="tab-content">
                    <div class="info-card">
                        <h3 style="color: var(--primary-light); margin-bottom: 1.5rem;">Cambia Password</h3>

                        <div class="requirements-list">
                            <strong>Requisiti per la password:</strong>
                            <ul>
                                <li>Minimo 6 caratteri</li>
                                <li>Si consiglia di usare lettere maiuscole, minuscole, numeri e simboli</li>
                                <li>Non usare password facilmente indovinabili</li>
                            </ul>
                        </div>

                        <form method="POST" action="${pageContext.request.contextPath}/utente/admin-change-password" class="edit-form" onsubmit="return validatePasswordForm()">
                            <div class="form-group">
                                <label for="currentPassword">Password Attuale</label>
                                <input type="password" id="currentPassword" name="currentPassword" required placeholder="Inserisci la password attuale">
                            </div>

                            <div class="form-group">
                                <label for="newPassword">Nuova Password</label>
                                <input type="password" id="newPassword" name="newPassword" required minlength="6" placeholder="Inserisci la nuova password">
                            </div>

                            <div class="form-group">
                                <label for="confirmPassword">Conferma Nuova Password</label>
                                <input type="password" id="confirmPassword" name="confirmPassword" required minlength="6" placeholder="Conferma la nuova password">
                            </div>

                            <button type="submit" class="btn-danger">Cambia Password</button>
                        </form>
                    </div>
                </div>

            </section>
        </div>
    </section>
</main>

<script>
    function showTab(tabName) {
        // Nascondi tutti i tab content
        document.querySelectorAll('.tab-content').forEach(tab => {
            tab.classList.remove('active');
        });

        // Rimuovi active da tutti i bottoni
        document.querySelectorAll('.tab-button').forEach(btn => {
            btn.classList.remove('active');
        });

        // Mostra il tab selezionato
        document.getElementById('tab-' + tabName).classList.add('active');

        // Attiva il bottone corrispondente
        event.target.classList.add('active');
    }

    function validatePasswordForm() {
        const newPassword = document.getElementById('newPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;

        if (newPassword !== confirmPassword) {
            alert('Le nuove password non coincidono');
            return false;
        }

        if (newPassword.length < 6) {
            alert('La password deve essere di almeno 6 caratteri');
            return false;
        }

        return confirm('Sei sicuro di voler cambiare la password?');
    }

    function validateEmailForm() {
        const newEmail = document.getElementById('newEmail').value;
        const emailRegex = /^[A-Za-z0-9+_.-]+@(.+)$/;

        if (!emailRegex.test(newEmail)) {
            alert('Formato email non valido');
            return false;
        }

        return confirm('Sei sicuro di voler cambiare l\'email? Dovrai usare la nuova email per accedere.');
    }

    // Gestione tab iniziale basata su parametri URL
    document.addEventListener('DOMContentLoaded', function() {
        const urlParams = new URLSearchParams(window.location.search);
        const tab = urlParams.get('tab');

        if (tab === 'email') {
            showTab('email');
        } else if (tab === 'password') {
            showTab('password');
        } else {
            showTab('info');
        }
    });
</script>

</body>
</html>