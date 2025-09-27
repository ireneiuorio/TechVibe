<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ul class="grid-inline paginator">
  <c:forEach var="page" begin="1" end="${pages}">
    <li>
      <c:choose>
        <c:when test="${page == currentPage}">
          <span class="current-page">${page}</span>
        </c:when>
        <c:otherwise>
          <a href="./?page=${page}">${page}</a>
        </c:otherwise>
      </c:choose>
    </li>
  </c:forEach>
</ul>

<style>

  .paginator {
    list-style: none;
    padding: 0;
    text-align: center;
  }

  .paginator li {
    display: inline-block;
    margin: 0 0.25rem;
  }

  .paginator a {
    display: inline-block;

    color: var(--primary-light);
    text-decoration: none;
    border-radius: 3px;

  }

  .paginator a:hover {
    color: var(--primary-light);
  }

  .paginator .current-page {
    display: inline-block;
    padding: 0.4rem 0.7rem;
    color: var(--primary);
    font-weight: 500;
  }

</style>