package techvibe.prodotto;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import techvibe.categoria.Categoria;
import techvibe.components.Paginator;
import techvibe.http.CommonValidator;
import techvibe.http.Controller;
import techvibe.http.ErrorHandler;
import techvibe.search.Condition;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebServlet("/prodotti/*")
@MultipartConfig
public class ProdottoServlet extends Controller implements ErrorHandler {

    @Resource(name = "jdbc/TechVibe")
    protected DataSource source;
    private ProdottoDao<SQLException> prodottoDao;

    public void init()throws ServletException
    {
        super.init();
        prodottoDao =new SqlProdottoDao(source);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo() != null ? request.getPathInfo() : "/";

        switch (path) {

            case "/":
                authorize(request.getSession(false));
                int intPage=parsePage(request);
                Paginator paginator=new Paginator(intPage,30);
                int size= 0;
                try {
                    size = prodottoDao.countAll();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                request.setAttribute("pages",paginator.getPages(size));
                List<Prodotto> prodotti;
                try {
                    prodotti=prodottoDao.fetchProdotti(paginator);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                request.setAttribute("prodotti",prodotti);
                request.getRequestDispatcher("/WEB-INF/views/crm/prodotti.jsp").forward(request,response);
                break;


            case "/create":
                authorize(request.getSession(false));
                request.getRequestDispatcher("/WEB-INF/views/crm/prodotto.jsp").forward(request,response);
               break;



            case "/show":
                authorize(request.getSession(false));
                validate(CommonValidator.validateId(request));
                int id=Integer.parseInt(request.getParameter("idp"));
                Optional<Prodotto> optionalProdotto;
                try {
                    optionalProdotto=prodottoDao.fetchProdotto(id);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if(optionalProdotto.isPresent())
                {
                    request.setAttribute("prodotto",optionalProdotto.get());
                    request.getRequestDispatcher("/WEB-INF/views/crm/prodotto.jsp").forward(request,response);
                }else{
                    notFound();
                }
                break;

            case"/search":
                List<Condition> conditions=new ProdottoSearch().buildSearch(request);
                List<Prodotto> searchProdotti= null;
                try {
                    searchProdotti = conditions.isEmpty() ?
                            prodottoDao.fetchProdotti(new Paginator(1,50)):
                            prodottoDao.search(conditions);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                request.setAttribute("prodotti",searchProdotti);
                request.getRequestDispatcher("/WEB-INF/views/site/search.jsp").forward(request,response);
                break;

            default:
                request.getRequestDispatcher("/WEB-INF/views/crm/prodotti.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String path = request.getPathInfo() != null ? request.getPathInfo() : "/";
            switch (path) {
                case "/create": {
                    ProdottoDao<SQLException> prodottoDao = new SqlProdottoDao(source);
                    Prodotto prodotto = new Prodotto();

                    // Campi prodotto
                    prodotto.setModello(safe(request.getParameter("modello")));
                    prodotto.setMarca(safe(request.getParameter("marca")));
                    prodotto.setSistemaOperativo(safe(request.getParameter("sistemaOperativo")));
                    prodotto.setConnettivita(safe(request.getParameter("connettivita")));
                    prodotto.setColore(safe(request.getParameter("colore")));
                    prodotto.setStorage(safeInt(request.getParameter("storage")));
                    prodotto.setRam(safeInt(request.getParameter("ram")));
                    prodotto.setQtDisponibile(safeInt(request.getParameter("qtDisponibile")));
                    prodotto.setPrezzo(safeDouble(request.getParameter("prezzo")));
                    prodotto.setDimensioneSchermo(safeDouble(request.getParameter("dimensioneSchermo")));

                    // Quattro immagini
                    prodotto.setImmagine1(safe(request.getParameter("immagine1")));
                    prodotto.setImmagine2(safe(request.getParameter("immagine2")));
                    prodotto.setImmagine3(safe(request.getParameter("immagine3")));
                    prodotto.setImmagine4(safe(request.getParameter("immagine4")));

                    // Categoria
                    Categoria categoria = new Categoria();
                    categoria.setIdCategoria(safeInt(request.getParameter("idCategoria")));
                    prodotto.setCategoria(categoria);

                    // Salva
                    if (prodottoDao.createProdotto(prodotto)) {
                        response.sendRedirect(request.getContextPath() + "/prodotti");
                    } else {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore Server");
                    }
                    break;
                }
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }

    private double safeDouble(String v) {
        if (v == null) return 0d;
        v = v.trim().replace(',', '.');
        try { return Double.parseDouble(v); } catch (NumberFormatException e) { return 0d; }
    }

    private int safeInt(String v) {
        try { return Integer.parseInt(v); } catch (Exception e) { return 0; }
    }
}
