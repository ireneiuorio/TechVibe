<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Script per contextPath e carrello -->
<script>
    window.contextPath = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/carrello.js"></script>

<!-- Link al CSS della navbar -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">

<!-- Navbar -->
<header class="navbar" id="mainNavbar">
    <div class="logo">
        <a href="${pageContext.request.contextPath}/pages" style="color: white">TechVibe</a>
    </div>

    <nav class="nav-links" id="navLinks">
        <a href="${pageContext.request.contextPath}/pages">Home</a>

        <!-- Dropdown categorie (solo desktop) -->
        <div class="dropdown">
            <a href="#" class="dropbtn" aria-haspopup="true" aria-expanded="false">Categorie</a>
            <div class="dropdown-content" role="menu" aria-label="Categorie">
                <a role="menuitem" href="${pageContext.request.contextPath}/pages/smartphone">Smartphone</a>
                <a role="menuitem" href="${pageContext.request.contextPath}/pages/tablet">Tablet</a>
            </div>
        </div>

        <!-- Link categorie diretti (solo mobile, uno sotto l'altro) -->
        <div class="mobile-category-links">
            <a href="${pageContext.request.contextPath}/pages/smartphone">Smartphone</a>
            <a href="${pageContext.request.contextPath}/pages/tablet">Tablet</a>
        </div>

        <a href="${pageContext.request.contextPath}/prodotti/offerte">Offerte</a>
        <a href="${pageContext.request.contextPath}/pages/contatti">Contatti</a>
    </nav>

    <div class="menu">
        <c:choose>
            <c:when test="${not empty sessionScope.utenteSession}">
                <!-- Utente loggato -->
                <a href="${pageContext.request.contextPath}/utente/logout"
                   class="shopping-cart"
                   aria-label="Logout">
                    <img id="logout"
                         src="${pageContext.request.contextPath}/icons/logout.svg"
                         alt="Logout">
                </a>
                <a href="${pageContext.request.contextPath}/utente/profile"
                   class="shopping-cart"
                   aria-label="Profilo utente">
                    <img id="ute1"
                         src="${pageContext.request.contextPath}/icons/account.svg"
                         alt="Profilo">
                </a>
            </c:when>
            <c:otherwise>
                <!-- Utente non loggato -->
                <a href="${pageContext.request.contextPath}/pages/accedi"
                   class="shopping-cart"
                   aria-label="Accedi">
                    <img id="uten"
                         src="${pageContext.request.contextPath}/icons/account.svg"
                         alt="Accedi">
                </a>
            </c:otherwise>
        </c:choose>

        <!-- Carrello -->
        <a href="${pageContext.request.contextPath}/carrello/view"
           class="shopping-cart"
           aria-label="Carrello">
            <img id="car"
                 src="${pageContext.request.contextPath}/icons/sc.svg"
                 alt="Carrello">
            <span class="badge" id="cart-count">0</span>
        </a>
    </div>

    <!-- Pulsante hamburger con icona esterna -->
    <button class="navbar-toggle" id="navbarToggle" aria-label="Toggle menu" aria-expanded="false">
        <img class="icon-menu" src="${pageContext.request.contextPath}/icons/menu.svg" alt="">
    </button>
</header>

<script>
    // Toggle menu mobile
    (function() {
        const toggle = document.getElementById('navbarToggle');//Bottone hamburger
        const navLinks = document.getElementById('navLinks');//Menù di navigazione

        if (toggle && navLinks) {//Controlla che esistano
            toggle.addEventListener('click', function() {//Click sul bottone hamburger

                //NavbarToggle in CSS
                this.classList.toggle('active');//Aggiunge se non c'è, rimuove se c'è
                navLinks.classList.toggle('open');

                //Se il menù ha la classe open restituisce true
                const isOpen = navLinks.classList.contains('open');
                //Imposta l'attributo aria-expanded sul bottone dice se è aperto o chiuso
                this.setAttribute('aria-expanded', isOpen);
            });

            // Chiudi menu quando si clicca su un link
            const links = navLinks.querySelectorAll('a'); //Chiude un menù quando clicco un link
            links.forEach(link => {
                link.addEventListener('click', function() {//Per ogni link nel menu aggiungi un event listner per il click
                    toggle.classList.remove('active');
                    navLinks.classList.remove('open');
                    toggle.setAttribute('aria-expanded', 'false');
                });
            });
        }
    })();//Chiude il menù e va alla pagina del link






    //GESTIONE DROPDOWN CATEGORIE

    //Seleziona tutti gli elementi dropdown che si trovano in nav-links
    const dropdowns = document.querySelectorAll('.nav-links .dropdown');//restituisce un array con tutti i dropdown trovati

    dropdowns.forEach(dropdown => {
        const btn = dropdown.querySelector('.dropbtn'); //Cerca il dropbtn in ogni dropdown e restituisce solo il primo trovato

        if (btn) {
            // Click sul pulsante dropdown
            btn.addEventListener('click', function(e) {
                e.preventDefault(); //blocca i comportamneto predefinito
                e.stopPropagation();//impedisce che l'evento salga verso i genitori, blocca la propagazione

                // Chiudi altri dropdown aperti
                dropdowns.forEach(other => {
                    if (other !== dropdown) { //Se non è quello cliccato lo chiude
                        other.classList.remove('open');
                        const otherBtn = other.querySelector('.dropbtn');
                        if (otherBtn) otherBtn.setAttribute('aria-expanded', 'false');
                    }
                });

                // Toggle questo dropdown: se la classe opern c'è la rimuove, se non c'è la aggiunge
                dropdown.classList.toggle('open');

                // Aggiorna aria-expanded
                const isOpen = dropdown.classList.contains('open'); //Controlla se è aperto
                btn.setAttribute('aria-expanded', isOpen);
            });
        }
    });

    // Chiudi dropdown quando clicchi fuori
    document.addEventListener('click', function(e) {
        if (!e.target.closest('.dropdown')) { //cerca se l'elemento cliccato è un dropdown se non lo è chiude
            dropdowns.forEach(dropdown => {
                dropdown.classList.remove('open');
                const btn = dropdown.querySelector('.dropbtn');//Rimuoe open da tutti i dropdown
                if (btn) {
                    btn.setAttribute('aria-expanded', 'false');//Resetta aria-expanded su tutti i pulsanti
                }
            });
        }
    });

    // Chiudi dropdown quando clicchi su un link interno
    const dropdownLinks = document.querySelectorAll('.dropdown-content a'); //Seleziona tutti i link dento dropdown
    dropdownLinks.forEach(link => {
        link.addEventListener('click', function() { //per onuno aggiunger un listner al click
            dropdowns.forEach(dropdown => {
                dropdown.classList.remove('open');
                const btn = dropdown.querySelector('.dropbtn');
                if (btn) {
                    btn.setAttribute('aria-expanded', 'false');
                }
            });

            // Chiudi anche il menu mobile se aperto
            if (toggle && navLinks) {
                toggle.classList.remove('active');
                navLinks.classList.remove('open');
                toggle.setAttribute('aria-expanded', 'false');
            }
        });
    });

</script>