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

            default:
                request.getRequestDispatcher("/WEB-INF/views/crm/prodotti.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            String path = request.getPathInfo() != null ? request.getPathInfo() : "/";
            switch (path) {
                case "/create": {
                    ProdottoDao<SQLException> prodottoDao = new SqlProdottoDao(source);
                    Prodotto prodotto = new Prodotto();

                    // Campi prodotto (i "name" in JSP DEVONO essere questi minuscoli)
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

                    // Immagini dalla JSP multipla: name="gallery"
                    String[] gallery = request.getParameterValues("gallery"); // fino a 4
                    if (gallery == null) gallery = new String[0];

                    // Mappa su img1..img4 (null se mancanti)
                    String img1 = gallery.length > 0 ? gallery[0] : null;
                    String img2 = gallery.length > 1 ? gallery[1] : null;
                    String img3 = gallery.length > 2 ? gallery[2] : null;
                    String img4 = gallery.length > 3 ? gallery[3] : null;


                    // Assicurati che in Prodotto.java esistano questi setter/getter
                    prodotto.setCover(img1);
                    prodotto.setImmagine1(img2);
                    prodotto.setImmagine2(img3);
                    prodotto.setImmagine3(img4);

                    // Categoria
                    Categoria categoria = new Categoria();
                    categoria.setIdCategoria(safeInt(request.getParameter("idCategoria")));
                    prodotto.setCategoria(categoria);

                    // Debug utile
                    System.out.printf("CREATE -> modello=%s, prezzo=%s, cat=%s, imgs=%s%n",
                            prodotto.getModello(),
                            request.getParameter("prezzo"),
                            request.getParameter("idCategoria"),
                            Arrays.toString(gallery));

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
