<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Il mio profilo</title>
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">

    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="Profilo Amministratore"/>
        <jsp:param name="styles" value="crm,dashboard,profile-admin"/>
        <jsp:param name="scripts" value="crm"/>
    </jsp:include>
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
                    <button class="tab-button active" onclick="showTab(event,'info')">Informazioni</button>
                    <button class="tab-button" onclick="showTab(event,'email')">Cambia Email</button>
                    <button class="tab-button" onclick="showTab(event,'password')">Cambia Password</button>
                </div>

                <!-- Tab Informazioni -->
                <div id="tab-info" class="tab-content active">
                    <div class="info-grid">
                        <div class="info-card">
                            <h3 class="section-title">Dati Personali</h3>

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
                            <h3 class="section-title">Sicurezza Account</h3>

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
                        <h3 class="section-title">Cambia Email</h3>

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
                        <h3 class="section-title">Cambia Password</h3>

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
    function showTab(evt, tabName) {
        document.querySelectorAll('.tab-content').forEach(tab => tab.classList.remove('active'));//Nasconde tutti i contenuti della tab content
        document.querySelectorAll('.tab-button').forEach(btn => btn.classList.remove('active'));//Disattiva tutti io bottoni
        document.getElementById('tab-' + tabName).classList.add('active');//Mostra il contenuto della tab richiesta
        if (evt && evt.currentTarget) evt.currentTarget.classList.add('active');//Attiva il bottone cliccato
    }

    //VALIDAZIONE PASSWORD PRIMA DI INVIARE IL FORM
    function validatePasswordForm() {
        //Prende i valori in input
        const newPassword = document.getElementById('newPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;

        //Controlla se le password coincidono
        if (newPassword !== confirmPassword) {
            alert('Le nuove password non coincidono');
            return false;
        }
        //Controlla la lunghezza minima
        if (newPassword.length < 6) {
            alert('La password deve essere di almeno 6 caratteri');
            return false;
        }
        //Chiede la conferma finale
        return confirm('Sei sicuro di voler cambiare la password?');
    }

    //VALIDA IL CAMBIO PASSWORD
    function validateEmailForm() {
        //Prendi il valore dell'email
        const newEmail = document.getElementById('newEmail').value;
        //Definisce il pattern di validazione
        const emailRegex = /^[A-Za-z0-9+_.-]+@(.+)$/;
        //Testa l'email con la regex
        if (!emailRegex.test(newEmail)) {//Se l'email non rispetta ritorna true
            alert('Formato email non valido');
            return false;
        }
        return confirm('Sei sicuro di voler cambiare l\'email? Dovrai usare la nuova email per accedere.');
    }

    //Quando la pagina finisce di caricare, esegui questo codice
    document.addEventListener('DOMContentLoaded', function() {
        const urlParams = new URLSearchParams(window.location.search);//Legge i parametri dell'URL
       //Prende il parametro tab
        const tab = urlParams.get('tab');
       //In base al parametro dell'URL apri la tab giusta
        if (tab === 'email') showTab(null,'email');
        else if (tab === 'password') showTab(null,'password');
        else showTab(null,'info');//Default tab info
    });
</script>

</body>
</html>
