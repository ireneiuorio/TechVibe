package techvibe.Controller;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import techvibe.Model.categoria.Categoria;
import techvibe.Model.components.Paginator;
import techvibe.http.Controller;
import techvibe.http.ErrorHandler;
import techvibe.Model.prodotto.Prodotto;
import techvibe.Model.prodotto.ProdottoDao;
import techvibe.Model.prodotto.ProdottoSearch;
import techvibe.Model.prodotto.SqlProdottoDao;
import techvibe.Model.search.Condition;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
                //Verifica si tratti di un Admin
                authorize(request.getSession(false));
                int intPage=parsePage(request);
                Paginator paginator=new Paginator(intPage,10);
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


            case "/searchsmartphone":
                // Non serve autorizzazione per la ricerca pubblica
                // authorize(request.getSession(false)); // Rimuovi se è per utenti pubblici

                List<Condition> conditions = new ProdottoSearch().buildSearch(request);
                List<Prodotto> searchProdotti = null;

                try {
                    if (conditions.isEmpty()) {
                        // Se nessun filtro, mostra tutti i prodotti con paginazione
                        int page = parsePage(request); // Riutilizza la tua logica di paginazione
                        Paginator paginator1 = new Paginator(page, 12); // 12 prodotti per pagina
                        searchProdotti = prodottoDao.fetchProdotti(paginator1);

                        // Aggiungi info paginazione per la JSP
                        int totalCount = prodottoDao.countAll();
                        request.setAttribute("pages", paginator1.getPages(totalCount));
                        request.setAttribute("currentPage", page);
                    } else {
                        // Con filtri, mostra tutti i risultati (nessuna paginazione)
                        searchProdotti = prodottoDao.search(conditions);
                    }

                } catch (SQLException e) {
                    // In caso di errore, mostra lista vuota
                    searchProdotti = new ArrayList<>();
                    request.setAttribute("errorMessage", "Errore nella ricerca. Riprova più tardi.");
                }

                request.setAttribute("prodotti", searchProdotti);

                // Forward alla tua JSP con il nuovo layout
                request.getRequestDispatcher("/WEB-INF/views/site/smartphone.jsp").forward(request, response);
                break;


            case "/searchtablet":


                //Restituisce una lista di condizioni che servono per filtrare i prodotti nel DB
                List<Condition> conditions1 = new ProdottoSearch().buildSearch(request);
                List<Prodotto> risultati;

                try {
                    //Nessuna condizione di ricerca:mostro tutti i prodotti
                    if (conditions1.isEmpty()) {
                        int page = Math.max(1, parsePage(request));
                        Paginator paginator1 = new Paginator(page, 12);
                        risultati = prodottoDao.fetchProdotti(paginator1);
                        int totalCount = prodottoDao.countAll();
                        request.setAttribute("pages", paginator1.getPages(totalCount));
                        request.setAttribute("currentPage", page);
                    } else {
                        //ottenere solo i prodotti che rispettano la condizione
                        risultati = prodottoDao.search(conditions1);
                    }
                } catch (SQLException e) {
                    risultati = new ArrayList<>();
                    request.setAttribute("errorMessage", "Errore nella ricerca. Riprova più tardi.");
                }


                request.setAttribute("prodotti", risultati);
                request.getRequestDispatcher("/WEB-INF/views/site/tablet.jsp").forward(request, response);
                break;

            case "/prodotto":
                int idProdotto = Integer.parseInt(request.getParameter("id"));

                Optional<Prodotto> optionalProdotto1;
                try {
                    optionalProdotto1 = prodottoDao.fetchProdotto(idProdotto);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                //nel caso il prodotto esiste recupera l'oggetto e fa la forward
                if (optionalProdotto1.isPresent()) {
                    request.setAttribute("prodotto", optionalProdotto1.get());
                    request.getRequestDispatcher("/WEB-INF/views/site/prodotto.jsp").forward(request, response);
                } else {
                    response.sendError(404, "Prodotto non trovato");
                }
                break;



            case "/manage":
                authorize(request.getSession(false));
                int idd = Integer.parseInt(request.getParameter("id"));

                try {
                    Optional<Prodotto> prodotto = prodottoDao.fetchProdotto(idd);
                    if (prodotto.isPresent()) {
                        request.setAttribute("prodotto", prodotto.get());
                        request.getRequestDispatcher("/WEB-INF/views/crm/manage-prodotto.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/prodotti/");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "/edit":
                authorize(request.getSession(false));
                int id1 = Integer.parseInt(request.getParameter("id"));

                try {
                    Optional<Prodotto> prodotto = prodottoDao.fetchProdotto(id1);
                    if (prodotto.isPresent()) {
                        request.setAttribute("prodotto", prodotto.get());
                        request.getRequestDispatcher("/WEB-INF/views/crm/edit-prodotto.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/prodotti/");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "/delete":
                authorize(request.getSession(false));
                int delId = Integer.parseInt(request.getParameter("id"));

                try {
                    prodottoDao.deleteProdotto(delId);
                    response.sendRedirect(request.getContextPath() + "/prodotti/");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "/offerte":
                List<Prodotto> prodottiInOfferta = new ArrayList<>();

                try {
                    prodottiInOfferta = prodottoDao.fetchProdottiInOfferta();
                } catch (SQLException e) {
                    prodottiInOfferta = new ArrayList<>();
                    request.setAttribute("errorMessage", "Errore nel caricamento delle offerte.");
                }

                request.setAttribute("prodotti", prodottiInOfferta);
                request.getRequestDispatcher("/WEB-INF/views/site/offerte.jsp").forward(request, response);
                break;

            default:
                request.getRequestDispatcher("/WEB-INF/views/crm/secret.jsp").forward(request, response);
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
                    prodotto.setPercentualeSconto(safeDouble(request.getParameter("percentualeSconto")));
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
                        response.sendRedirect(request.getContextPath() + "/prodotti/?page=1");
                    } else {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore Server");
                    }

                    break;
                }


                case "/update":
                    authorize(request.getSession(false));

                    // Recupera parametri
                    int id = Integer.parseInt(request.getParameter("id"));

                    // Recupera prodotto esistente dal database
                    Optional<Prodotto> optProdotto;
                    try {
                        optProdotto = prodottoDao.fetchProdotto(id);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    if (!optProdotto.isPresent()) {
                        response.sendRedirect(request.getContextPath() + "/prodotti/");
                        return;
                    }

                    Prodotto prodotto = optProdotto.get();

                    // Aggiorna tutti i campi con i valori dal form
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
                    prodotto.setPercentualeSconto(safeDouble(request.getParameter("percentualeSconto")));

                    // Aggiorna categoria
                    Categoria categoria = new Categoria();
                    categoria.setIdCategoria(safeInt(request.getParameter("idCategoria")));
                    prodotto.setCategoria(categoria);

                    // Validazione minima: modello e marca non vuoti e prezzo non negativo
                    if (prodotto.getModello().isEmpty() || prodotto.getMarca().isEmpty() || prodotto.getPrezzo() <= 0) {
                        response.sendRedirect(request.getContextPath() + "/prodotti/edit?id=" + id + "&error=validation");
                        return;
                    }

                    // Salva nel database
                    try {// fa l'update nel db
                        boolean success = prodottoDao.updateProdotto(prodotto);
                        if (success) {
                            response.sendRedirect(request.getContextPath() + "/prodotti/manage?id=" + id);
                        } else {
                            response.sendRedirect(request.getContextPath() + "/prodotti/edit?id=" + id + "&error=update_failed");
                        }
                    } catch (SQLException e) {
                        response.sendRedirect(request.getContextPath() + "/prodotti/edit?id=" + id + "&error=database");
                    }
                    break;


                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    //Mai null: trasforma null in "" e fa trim().
    private String safe(String s) {
        return s == null ? "" : s.trim();
    }

    //Supporta la virgola come separatore decimale (es. 12,5 → 12.5).
    //Su input non numerico ritorna 0.0
    private double safeDouble(String v) {
        if (v == null) return 0d;
        v = v.trim().replace(',', '.');
        try { return Double.parseDouble(v); } catch (NumberFormatException e) { return 0d; }
    }

    //Su input non valido ritorna 0
    private int safeInt(String v) {
        try { return Integer.parseInt(v); } catch (Exception e) { return 0; }


    }

}
