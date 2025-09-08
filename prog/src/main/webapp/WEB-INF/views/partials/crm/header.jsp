<!-- Header CRM -->
<header class="top-bar grid-x align-center" id="top-bar">
    <img id="hamburger" src="<%= request.getContextPath() %>/icons/menu.svg" alt="Menu">

    <span class="account">
        <img src="<%= request.getContextPath() %>/icons/account.svg" alt="account">
        ${utenteSession.nome.concat(" ").concat(utenteSession.cognome)}
    </span>
</header>
