package techvibe.Controller;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import techvibe.Model.carrello.Carrello;
import techvibe.Model.carrello.CarrelloService;
import techvibe.Model.carrello.SqlCarrelloDao;
import techvibe.Model.prodotto.SqlProdottoDao;
import techvibe.Model.ordine.Ordine;
import techvibe.Model.ordine.OrdineDao;
import techvibe.Model.ordine.SqlOrdineDao;
import techvibe.Model.utente.Utente;
import techvibe.Model.utente.UtenteSession;
import techvibe.Model.utente.UtenteDao;
import techvibe.Model.utente.SqlUtenteDao;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Locale;

@WebServlet("/checkout/*")
@MultipartConfig
public class CheckoutServlet extends HttpServlet {

    @Resource(name = "jdbc/TechVibe")
    protected DataSource source;

    private OrdineDao<SQLException> ordineDao;
    private CarrelloService carrelloService; // Aggiungi questo

    @Override
    public void init() throws ServletException {
        super.init();
        ordineDao = new SqlOrdineDao(source);

        // Inizializza il servizio carrello
        SqlProdottoDao prodottoDao = new SqlProdottoDao(source);
        SqlCarrelloDao sqlCarrelloDao = new SqlCarrelloDao(source, prodottoDao);
        carrelloService = new CarrelloService(sqlCarrelloDao);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";

        HttpSession session = request.getSession(false);

        // Verifica login
        if (session == null || session.getAttribute("utenteSession") == null) {
            response.sendRedirect(request.getContextPath() + "/pages/accedi?redirect=checkout");
            return;
        }

        try {
            switch (pathInfo) {
                case "/": {
                    // Usa l'istanza invece del metodo statico
                    Carrello carrello = carrelloService.getCarrello(session);
                    if (carrello == null || carrello.isEmpty()) {
                        response.sendRedirect(request.getContextPath() + "/carrello/view");
                        return;
                    }

                    Utente utente = resolveUtenteFromSession(session);

                    request.setAttribute("carrello", carrello);
                    request.setAttribute("utente", utente);
                    request.getRequestDispatcher("/WEB-INF/views/site/checkout.jsp").forward(request, response);
                    break;
                }

                case "/conferma": {
                    String ordineId = request.getParameter("ordineId");

                    // Non fare fetch dal DB, usa solo l'ID
                    request.setAttribute("ordineId", ordineId);
                    request.getRequestDispatcher("/WEB-INF/views/site/conferma-ordine.jsp").forward(request, response);
                    break;
                }

                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("utenteSession") == null) {
            try (PrintWriter writer = response.getWriter()) {
                writer.print("{\"success\": false, \"error\": \"Utente non autenticato\"}");
            }
            return;
        }

        try {
            switch (pathInfo) {
                case "/conferma":
                    processaOrdine(request, response, session);
                    break;

                default:
                    try (PrintWriter writer = response.getWriter()) {
                        writer.print("{\"success\": false, \"error\": \"Azione non supportata\"}");
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try (PrintWriter writer = response.getWriter()) {
                writer.print("{\"success\": false, \"error\": \"Errore del server\"}");
            }
        }
    }

    private void processaOrdine(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException, SQLException {

        try (PrintWriter writer = response.getWriter()) {

            // Usa l'istanza invece del metodo statico
            Carrello carrello = carrelloService.getCarrello(session);
            if (carrello == null || carrello.isEmpty()) {
                writer.print("{\"success\": false, \"error\": \"Carrello vuoto\"}");
                return;
            }

            Utente utente = resolveUtenteFromSession(session);
            if (utente == null) {
                writer.print("{\"success\": false, \"error\": \"Utente non trovato\"}");
                return;
            }

            // Parametri
            String nome = request.getParameter("nome");
            String cognome = request.getParameter("cognome");
            String indirizzo = request.getParameter("indirizzo");
            String telefono = request.getParameter("telefono");
            String metodoPagamento = request.getParameter("metodoPagamento");

            // Validazione base
            if (isBlank(nome) || isBlank(cognome) || isBlank(indirizzo)
                    || isBlank(telefono) || isBlank(metodoPagamento)) {
                writer.print("{\"success\": false, \"error\": \"Tutti i campi obbligatori devono essere compilati\"}");
                return;
            }

            if (!"carta".equals(metodoPagamento) && !"paypal".equals(metodoPagamento)) {
                writer.print("{\"success\": false, \"error\": \"Metodo di pagamento non valido\"}");
                return;
            }

            // Se carta, check minimi
            if ("carta".equals(metodoPagamento)) {
                String numeroCarta = request.getParameter("numeroCarta");
                String scadenza = request.getParameter("scadenza");
                String cvv = request.getParameter("cvv");
                String intestatario = request.getParameter("intestatario");

                if (numeroCarta == null || numeroCarta.replaceAll("\\s", "").length() < 13
                        || scadenza == null || !scadenza.matches("\\d{2}/\\d{2}")
                        || cvv == null || cvv.length() < 3
                        || isBlank(intestatario)) {
                    writer.print("{\"success\": false, \"error\": \"Dati della carta non validi\"}");
                    return;
                }
            }

            // Crea ordine
            Ordine ordine = new Ordine();
            ordine.setUtente(utente);
            ordine.setCarrello(carrello);
            ordine.setTotale(carrello.total());
            ordine.setScontoTotale(0.0);
            ordine.setMetodoDiSpedizione("standard");
            ordine.setMetodoDiPagamento(metodoPagamento);
            ordine.setStato("in_attesa");

            try {
                boolean success = ordineDao.createOrdine(ordine);

                if (success) {
                    // Usa l'istanza invece del metodo statico
                    carrelloService.svuotaCarrello(session);
                    writer.printf(Locale.US, "{\"success\": true, \"ordineId\": %d, \"totale\": %.2f}",
                            ordine.getIdOrdine(), ordine.getTotale());
                } else {
                    writer.print("{\"success\": false, \"error\": \"Errore nella creazione dell'ordine\"}");
                }
            } catch (Exception e) {
                log("Errore durante il salvataggio dell'ordine", e);
                writer.print("{\"success\": false, \"error\": \"Errore durante il salvataggio\"}");
            }
        }
    }
    // ===== Helpers =====

    private Utente resolveUtenteFromSession(HttpSession session) {
        if (session == null) return null;

        Object sessObj = session.getAttribute("utenteSession");
        try {
            if (sessObj instanceof UtenteSession us) {
                UtenteDao<SQLException> utenteDao = new SqlUtenteDao(source);
                return utenteDao.fetchUtente(us.getId()).orElse(null);
            } else if (sessObj instanceof Utente u) {
                return u;
            } else {
                Object alt = session.getAttribute("accountSession");
                if (alt instanceof UtenteSession us2) {
                    UtenteDao<SQLException> utenteDao = new SqlUtenteDao(source);
                    return utenteDao.fetchUtente(us2.getId()).orElse(null);
                }
            }
        } catch (SQLException e) {
            log("Errore nel caricamento utente da sessione", e);
        }
        return null;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String safe(SupplierWithException<String> sup) {
        try { return sup.get(); } catch (Exception e) { return "(errore getter)"; }
    }

    @FunctionalInterface
    private interface SupplierWithException<T> {
        T get() throws Exception;
    }
}