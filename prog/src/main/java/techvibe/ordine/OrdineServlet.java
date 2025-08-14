package techvibe.ordine;

import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Or;
import techvibe.components.Paginator;
import techvibe.http.CommonValidator;
import techvibe.http.Controller;
import techvibe.http.ErrorHandler;
import techvibe.prodotto.ProdottoDao;
import techvibe.prodotto.SqlProdottoDao;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "OrdineServlet", value = "/ordini/*")
public class OrdineServlet extends Controller implements ErrorHandler {


    @Resource(name = "jdbc/TechVibe")
    protected DataSource source;
    private OrdineDao<SQLException> ordineDao;

    public void init()throws ServletException
    {
        super.init();
        ordineDao =new SqlOrdineDao(source);

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            String path = request.getPathInfo() != null ? request.getPathInfo() : "/";
            switch(path)
            {
                case"/":
                    authorize(request.getSession(false));
                    validate(CommonValidator.validatePage(request));
                    int intPage=parsePage(request);
                    int size=ordineDao.countAll();
                    Paginator paginator =new Paginator(intPage,50);
                    List<Ordine> ordini=ordineDao.fetchOrdini(paginator);
                    request.setAttribute("ordini",ordini);
                    request.setAttribute("pages",paginator.getPages(size));
                    request.getRequestDispatcher("/WEB-INF/views/crm/ordini.jsp").forward(request,response);
                    break;

                case"/show":



            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta
    }
}