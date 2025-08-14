<header class="top-bar grid-x align-center" id="top-bar">
    <img id="hamburger" src="<%= request.getContextPath() %>/icons/menu.svg" alt="Menu">
    <label class="field command">
        <input type="text" placeholder="Cerca comandi">
    </label>
    <span class="account">
        <img src="<%= request.getContextPath() %>/icons/account.svg" alt="account">
        ${utenteSession.nome.concat(" ").concat(utenteSession.cognome)}
    </span>
</header>