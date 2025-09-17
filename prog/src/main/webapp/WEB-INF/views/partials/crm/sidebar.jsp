<aside class="sidebar">
  <nav class="menu grid-y align-center" >
    <div class="logo">
      <a href="${pageContext.request.contextPath}/pages" style="color: white;text-decoration:none " >TechVibe</a>
    </div>
    <a href=<%= request.getContextPath() %>/utente/>Gestione Utenti</a>
    <a href="<%= request.getContextPath() %>/prodotti/">Gestione Prodotti</a>
    <a href="<%= request.getContextPath() %>/ordini/">Gestione Ordini</a>
    <a href="<%= request.getContextPath() %>/categorie/"> Gestione Categorie</a>
    <a href="#"> Gestione Profilo</a>
    <a href="<%= request.getContextPath() %>/utente/logout">Logout</a>
  </nav>
</aside>