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
        const toggle = document.getElementById('navbarToggle');
        const navLinks = document.getElementById('navLinks');

        if (toggle && navLinks) {
            toggle.addEventListener('click', function() {
                // Toggle classi
                this.classList.toggle('active');
                navLinks.classList.toggle('open');

                // Aggiorna aria-expanded
                const isOpen = navLinks.classList.contains('open');
                this.setAttribute('aria-expanded', isOpen);
            });

            // Chiudi menu quando si clicca su un link
            const links = navLinks.querySelectorAll('a');
            links.forEach(link => {
                link.addEventListener('click', function() {
                    toggle.classList.remove('active');
                    navLinks.classList.remove('open');
                    toggle.setAttribute('aria-expanded', 'false');
                });
            });
        }
    })();
</script>