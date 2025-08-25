package techvibe.http;

import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import techvibe.components.Paginator;
import techvibe.prodotto.Prodotto;
import techvibe.prodotto.ProdottoDao;
import techvibe.prodotto.SqlProdottoDao;
import techvibe.utente.SqlUtenteDao;
import techvibe.utente.Utente;
import techvibe.utente.UtenteDao;

import javax.sql.DataSource;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "PageServlet", value = "/pages/*")
public class PageServlet extends Controller implements ErrorHandler {

    @Resource(name = "jdbc/TechVibe")
    protected DataSource source;
    private ProdottoDao<SQLException> prodottoDao;
    private UtenteDao<SQLException> utenteDao;

    public void init() throws ServletException {
        super.init();
        prodottoDao = new SqlProdottoDao(source);
        utenteDao = new SqlUtenteDao(source);

    }

    @Override

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String path = request.getPathInfo() != null ? request.getPathInfo() : "/";
            switch (path) {
                case "dashboard":
                    authorize(request.getSession(false));
                    request.getRequestDispatcher("/WEB-INF/views/site/home.jsp").forward(request, response);
                    break;

                case "/":
                    request.getRequestDispatcher("/WEB-INF/views/site/home.jsp").forward(request, response);
                    break;
                case "/chisiamo":
                    request.getRequestDispatcher("/WEB-INF/views/site/chisiamo.jsp").forward(request, response);
                    break;
                case "/privacy":
                    request.getRequestDispatcher("/WEB-INF/views/site/privacy.jsp").forward(request, response);
                    break;
                case "/contatti":
                    request.getRequestDispatcher("/WEB-INF/views/site/contatti.jsp").forward(request, response);
                    break;
                case "/termini":
                    request.getRequestDispatcher("/WEB-INF/views/site/termini.jsp").forward(request, response);

                case "/smartphone":
                    int page = parsePage(request);                 // se hai già questo helper
                    Paginator paginator = new Paginator(page, 12); // 12 per pagina (scegli tu)

                    try {
                        int idSmartphone = 1; // <-- SOSTITUISCI con l'IdCategoria reale per "Smartphone"
                        List<Prodotto> prodotti = prodottoDao.fetchProdottiByCategoria(idSmartphone);



                        request.setAttribute("prodotti", prodotti);
                        request.getRequestDispatcher("/WEB-INF/views/site/smartphone.jsp")
                                .forward(request, response);
                    } catch (SQLException e) {
                        throw new ServletException(e);
                    }
                    break;

                case "/create":
                    request.getRequestDispatcher("/WEB-INF/views/site/create.jsp").forward(request,response);
                    break;

                case "/prodotto": {
                    String idStr = request.getParameter("id");
                    if (idStr == null) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "id mancante");
                        return;
                    }
                    try {
                        int id = Integer.parseInt(idStr);
                        Optional<Prodotto> opt = prodottoDao.fetchProdotto(id);
                        if (opt.isEmpty()) {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Prodotto non trovato");
                            return;
                        }
                        request.setAttribute("prodotto", opt.get());
                        request.getRequestDispatcher("/WEB-INF/views/site/prodotto.jsp").forward(request, response);
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "id non valido");
                    } catch (SQLException e) {
                        throw new ServletException(e);
                    }
                    break;
                }
                case "/accedi":
                    request.getRequestDispatcher("/WEB-INF/views/site/accedi.jsp").forward(request, response);
                case "/accediutente":
                    request.getRequestDispatcher("/WEB-INF/views/site/accediutente.jsp").forward(request, response);

                case "/successo":
                    request.getRequestDispatcher("/WEB-INF/views/site/successo.jsp")
                            .forward(request, response);

                    break;
                default:
                    notFound();

            }

        } catch (InvalidRequestException ex) {
            log(ex.getMessage());
            ex.handle(request, response);
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            String path = request.getPathInfo() != null ? request.getPathInfo() : "/";
            switch (path) {

                case "/create":
                    UtenteDao<SQLException> utenteDao = new SqlUtenteDao(source);
                    Utente utente = new Utente();

                    String password = safe(request.getParameter("password"));
                    String confirm  = safe(request.getParameter("confirm"));

                    // --- Controllo password uguali ---
                    if (password == null || confirm == null || !password.equals(confirm)) {
                        request.setAttribute("error", "Le password non coincidono.");
                        request.getRequestDispatcher("/WEB-INF/views/site/create.jsp").forward(request, response);
                        break;
                    }

                    utente.setNome(safe(request.getParameter("nome")));
                    utente.setCognome(safe(request.getParameter("cognome")));
                    utente.setEmail(safe(request.getParameter("email")));
                    try {
                        utente.setPassword(password);  // la tua logica di hash rimane invariata
                    } catch (NoSuchAlgorithmException ex) {
                        throw new RuntimeException(ex);
                    }
                    utente.setTelefono(safe(request.getParameter("telefono")));
                    utente.setIndirizzoSpedizione(safe(request.getParameter("indirizzospedizione")));

                    try {
                        if (utenteDao.existsByEmail(utente.getEmail())) {
                            request.setAttribute("error", "Email già registrata, usa un altro indirizzo.");
                            request.getRequestDispatcher("/WEB-INF/views/site/create.jsp").forward(request, response);
                            break;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        if (utenteDao.createUtente(utente)) {
                            response.sendRedirect(request.getContextPath() + "/pages/successo");
                        } else {
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore Server");
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;


                default:
                    notFound();
            }

        } catch (RuntimeException | IOException | ServletException e) {
            throw new RuntimeException(e);
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

