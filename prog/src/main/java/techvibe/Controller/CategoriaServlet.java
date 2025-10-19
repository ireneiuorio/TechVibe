package techvibe.Controller;

import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import techvibe.Model.categoria.Categoria;
import techvibe.Model.categoria.CategoriaDao;
import techvibe.Model.categoria.SqlCategoriaDao;
import techvibe.Model.components.Paginator;
import techvibe.http.Controller;
import techvibe.http.ErrorHandler;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "CategoriaServlet", value = "/categorie/*")
public class CategoriaServlet extends Controller implements ErrorHandler {

    @Resource(name = "jdbc/TechVibe")
    protected DataSource source;


    private CategoriaDao<SQLException> categoriaDao;

    public void init() throws ServletException {
        super.init();
        categoriaDao = new SqlCategoriaDao(source);
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String path = request.getPathInfo() != null ? request.getPathInfo() : "/";

            switch (path) {
                case "/":
                    authorize(request.getSession(false));
                    int intPage = parsePage(request);
                    Paginator paginator = new Paginator(intPage, 10);

                    List<Categoria> categorie = categoriaDao.fetchCategorie(paginator);
                    int size = categoriaDao.countAll();
                    request.setAttribute("categorie", categorie);
                    request.setAttribute("pages", paginator.getPages(size));
                    request.getRequestDispatcher("/WEB-INF/views/crm/categorie.jsp").forward(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}