<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <title>Registrazione Cliente</title>
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/icons/favicon.png">
    <jsp:include page="/WEB-INF/views/partials/head.jsp">
        <jsp:param name="styles" value="site,registrazione"/>
    </jsp:include>
</head>
<body>
<main class="app">
    <%@ include file="../partials/site/header.jsp" %>

    <section class="chi-siamo-container">
        <div class="auth-card">
            <h2>Registrazione Cliente</h2>

            <!-- Messaggi -->
            <c:if test="${not empty error}">
                <div class="notification error">
                    <c:out value="${error}"/>
                </div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="card privacy success-msg">
                    <c:out value="${success}"/>
                </div>
            </c:if>

            <!-- Form -->
            <form id="form-registrazione"
                  class="auth-form"
                  action="${pageContext.request.contextPath}/pages/create"
                  method="post">

                <label for="nome">
                    <span>Nome</span>
                    <input type="text" id="nome" name="nome" value="${param.nome}"
                           required maxlength="60" autocomplete="given-name">
                </label>

                <label for="cognome">
                    <span>Cognome</span>
                    <input type="text" id="cognome" name="cognome" value="${param.cognome}"
                           required maxlength="60" autocomplete="family-name">
                </label>

                <label for="email">
                    <span>Email</span>
                    <input type="email" id="email" name="email" value="${param.email}"
                           required maxlength="255" inputmode="email" autocomplete="email"
                           placeholder="es. nome@email.it">
                </label>

                <label for="password">
                    <span>Password</span>
                    <input type="password" id="password" name="password"
                           required minlength="8" autocomplete="new-password"
                           placeholder="min 8 caratteri">
                </label>

                <label for="confirm">
                    <span>Conferma password</span>
                    <input type="password" id="confirm" name="confirm"
                           required minlength="8" autocomplete="new-password">
                </label>

                <label for="telefono">
                    <span>Telefono</span>
                    <input type="tel" id="telefono" name="telefono" value="${param.telefono}"
                           maxlength="30" inputmode="tel" autocomplete="tel"
                           placeholder="+39 ...">
                </label>

                <label for="indirizzoSpedizione">
                    <span>Indirizzo di spedizione</span>
                    <input type="text" id="indirizzoSpedizione" name="indirizzospedizione"
                           value="${param.indirizzospedizione}" maxlength="255"
                           autocomplete="address-line1" placeholder="Via, n°, CAP, Città">
                </label>

                <button type="submit" class="btn primary">Crea account</button>
            </form>

            <!-- Link login -->
            <div style="text-align:center; margin-top:1rem; font-size:.95rem;"> Hai già un account? <a
                    href="${pageContext.request.contextPath}/pages/accediutente"
                    style="color:var(--primary-light); text-decoration:none; font-weight:600;"> Accedi </a></div>
        </div>
    </section>

    <%@ include file="../partials/site/footer.jsp" %>
</main>

<!-- Validazione client-side -->
<script>
    (function () {
        const form = document.getElementById('form-registrazione');
        if (!form) return;

        const email = document.getElementById('email');
        const pwd = document.getElementById('password');
        const confirm = document.getElementById('confirm');

        function validaPassword() {
            if (pwd.value.length < 8) {
                pwd.setCustomValidity('La password deve avere almeno 8 caratteri.');
            } else {
                pwd.setCustomValidity('');
            }
            if (confirm.value !== pwd.value) {
                confirm.setCustomValidity('Le password non coincidono.');
            } else {
                confirm.setCustomValidity('');
            }
        }

        function validaEmail() {
            if (email.validity.typeMismatch) {
                email.setCustomValidity('Inserisci un indirizzo email valido.');
            } else {
                email.setCustomValidity('');
            }
        }

        email.addEventListener('input', validaEmail);
        pwd.addEventListener('input', validaPassword);
        confirm.addEventListener('input', validaPassword);

        form.addEventListener('submit', function (e) {
            validaEmail();
            validaPassword();
            if (!form.checkValidity()) {
                e.preventDefault();
                form.reportValidity();
            }
        });
    })();
</script>

</body>
</html>
