<!-- Navbar -->
<header class="navbar">
    <div class="logo">
        <a href="${pageContext.request.contextPath}/pages" style="color: white">TechVibe</a>
    </div>
    <nav class="nav-links">
        <a href="${pageContext.request.contextPath}/pages">Home</a>


        <!-- Categorie con dropdown -->
        <div class="dropdown">
            <a href="#" class="dropbtn" aria-haspopup="true" aria-expanded="false">Categorie</a>
            <div class="dropdown-content" role="menu" aria-label="Categorie">
                <a role="menuitem" href="${pageContext.request.contextPath}/pages/smartphone">Smartphone</a>
                <a role="menuitem" href="${pageContext.request.contextPath}/pages/prodotti?cat=tablet">Tablet</a>
            </div>
        </div>

        <a href="#">Offerte</a>
        <a href="${pageContext.request.contextPath}/pages/contatti">Contatti</a>
    </nav>
    <div class="menu">

        <a href="${pageContext.request.contextPath}/pages/accedi" class="shopping-cart">
            <img id="ute" src="<%= request.getContextPath() %>/icons/account.svg" alt="ute">

        </a>

        <a href="${pageContext.request.contextPath}/pages/carrello" class="shopping-cart">
            <img id="car" src="<%= request.getContextPath() %>/icons/sc.svg" alt="Carrello">
            <span class="badge" id="cart-count">0</span>
        </a>


    </div>
</header>
