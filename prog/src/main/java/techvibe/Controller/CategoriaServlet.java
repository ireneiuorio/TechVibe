package techvibe.Controller;

import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import techvibe.Model.categoria.Categoria;
import techvibe.Model.categoria.CategoriaDao;
import techvibe.Model.categoria.SqlCategoriaDao;
import techvibe.Model.components.Paginator;
import techvibe.http.CommonValidator;
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
                    //legge il numero di pagina dalla query string


                    //Se esiste un parametro page lo valida
                    String pageParam = request.getParameter("page");
                    if (pageParam != null && !pageParam.isEmpty()) {
                        validate(CommonValidator.validatePage(request));
                    }
                    int intPage = parsePage(request);

                    //crea il paginator
                    Paginator paginator = new Paginator(intPage, 10);

                    //Il Dao fa la query con limit e offset
                   //Prende dal DB le categorie della pagina corrente
                    List<Categoria> categorie = categoriaDao.fetchCategorie(paginator);
                    //Conta il totale delle categorie per calcolare le pagine.
                    int size = categoriaDao.countAll();
                    request.setAttribute("categorie", categorie);

                    //struttura di pagine calcolata dal paginator
                    request.setAttribute("pages", paginator.getPages(size));
                    request.getRequestDispatcher("/WEB-INF/views/crm/categorie.jsp").forward(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}