<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style> /* mantiene la navbar bloccata durante lo scorll*/
    .navbar {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        z-index: 1000;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    }
    body {
        padding-top: 70px;
    }
</style>
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
                <a role="menuitem" href="${pageContext.request.contextPath}/pages/tablet">Tablet</a>
            </div>
        </div>

        <a href="#">Offerte</a>
        <a href="${pageContext.request.contextPath}/pages/contatti">Contatti</a>
    </nav>
    <div class="menu">
        <c:choose>
            <c:when test="${not empty sessionScope.utenteSession}">
            <a href="${pageContext.request.contextPath}/utente/logout" class="shopping-cart">
                <img id="ute" src="<%= request.getContextPath() %>/icons/exit.svg" alt="exit">
                <a href="${pageContext.request.contextPath}/utente/profile" class="shopping-cart">
                    <img id="ute1" src="<%= request.getContextPath() %>/icons/account.svg" alt="ute">
                </a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/pages/accedi" class="shopping-cart">
                    <img id="uten" src="<%= request.getContextPath() %>/icons/account.svg" alt="ute">
                </a>
            </c:otherwise>
        </c:choose>

        <a href="${pageContext.request.contextPath}/pages/carrello" class="shopping-cart">
            <img id="car" src="<%= request.getContextPath() %>/icons/sc.svg" alt="Carrello">
            <span class="badge" id="cart-count">0</span>
        </a>
    </div>
</header>