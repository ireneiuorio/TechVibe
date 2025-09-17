package techvibe.carrello;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import techvibe.prodotto.Prodotto;
import techvibe.prodotto.SqlProdottoDao;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Optional;

@WebServlet("/carrello/*")
@MultipartConfig
public class CarrelloServlet extends HttpServlet {

    @Resource(name = "jdbc/TechVibe")
    protected DataSource source;

    private SqlProdottoDao prodottoDao;
    private CarrelloDao carrelloDao; // CORREZIONE: SqlCarrelloDao invece di CarrelloDao
    private CarrelloService carrelloService;

    @Override
    public void init() throws ServletException {
        super.init();
        prodottoDao = new SqlProdottoDao(source);
        carrelloDao = new CarrelloDao(source, prodottoDao); // CORREZIONE: SqlCarrelloDao
        carrelloService = new CarrelloService(carrelloDao);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";

        try {
            switch (pathInfo) {

                case "/view":
                    HttpSession session = request.getSession(false);
                    Carrello carrello = carrelloService.getCarrello(session);
                    request.setAttribute("carrello", carrello);
                    request.getRequestDispatcher("/WEB-INF/views/site/carrello.jsp").forward(request, response);
                    break;

                case "/count":
                    response.setContentType("application/json");
                    HttpSession countSession = request.getSession(false);
                    int count = carrelloService.getNumeroArticoli(countSession);
                    PrintWriter countWriter = response.getWriter();
                    countWriter.print("{\"count\": " + count + "}");
                    break;

                case "/totale":
                    response.setContentType("application/json");
                    HttpSession totaleSession = request.getSession(false);
                    double totale = carrelloService.getTotaleCarrello(totaleSession);
                    PrintWriter totaleWriter = response.getWriter();
                    totaleWriter.printf(Locale.US, "{\"totale\": %.2f}", totale);
                    break;

                case "/sync":
                    // Endpoint per sincronizzare il carrello (utile per AJAX periodici)
                    response.setContentType("application/json");
                    HttpSession syncSession = request.getSession(false);
                    if (syncSession != null) {
                        carrelloService.sincronizzaCarrello(syncSession);
                        PrintWriter syncWriter = response.getWriter();
                        syncWriter.print("{\"success\": true, \"message\": \"Carrello sincronizzato\"}");
                    } else {
                        PrintWriter syncWriter = response.getWriter();
                        syncWriter.print("{\"success\": false, \"error\": \"Nessuna sessione trovata\"}");
                    }
                    break;

                default:
                    response.setContentType("application/json");
                    PrintWriter errorWriter = response.getWriter();
                    errorWriter.print("{\"success\": false, \"error\": \"Azione non supportata\"}");
            }
        } catch (Exception e) {
            response.setContentType("application/json");
            PrintWriter exceptionWriter = response.getWriter();
            exceptionWriter.print("{\"success\": false, \"error\": \"Errore del server\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            switch (pathInfo) {
                case "/aggiungi":
                    try {
                        int prodottoId = Integer.parseInt(request.getParameter("prodottoId"));
                        int quantita = Integer.parseInt(request.getParameter("quantita"));

                        if (quantita <= 0) {
                            PrintWriter quantitaWriter = response.getWriter();
                            quantitaWriter.print("{\"success\": false, \"error\": \"Quantità non valida\"}");
                            return;
                        }

                        Optional<Prodotto> optionalProdotto = prodottoDao.fetchProdotto(prodottoId);
                        if (optionalProdotto.isEmpty()) {
                            PrintWriter notFoundWriter = response.getWriter();
                            notFoundWriter.print("{\"success\": false, \"error\": \"Prodotto non trovato\"}");
                            return;
                        }

                        Prodotto prodotto = optionalProdotto.get();

                        if (prodotto.getQtDisponibile() < quantita) {
                            PrintWriter stockWriter = response.getWriter();
                            stockWriter.print("{\"success\": false, \"error\": \"Quantità non disponibile in magazzino\"}");
                            return;
                        }

                        HttpSession session = request.getSession(true);
                        boolean success = carrelloService.aggiungiProdotto(session, prodotto, quantita);

                        if (success) {
                            int nuovoCount = carrelloService.getNumeroArticoli(session);
                            double nuovoTotale = carrelloService.getTotaleCarrello(session);

                            PrintWriter successWriter = response.getWriter();
                            successWriter.printf(Locale.US, "{\"success\": true, \"message\": \"Prodotto aggiunto al carrello\", \"count\": %d, \"totale\": %.2f}",
                                    nuovoCount, nuovoTotale);
                        } else {
                            PrintWriter failWriter = response.getWriter();
                            failWriter.print("{\"success\": false, \"error\": \"Errore nell'aggiunta del prodotto\"}");
                        }

                    } catch (NumberFormatException e) {
                        PrintWriter formatWriter = response.getWriter();
                        formatWriter.print("{\"success\": false, \"error\": \"Parametri non validi\"}");
                    }
                    break;

                case "/rimuovi":
                    try {
                        int prodottoId = Integer.parseInt(request.getParameter("prodottoId"));

                        HttpSession session = request.getSession(false);
                        if (session == null) {
                            PrintWriter sessionWriter = response.getWriter();
                            sessionWriter.print("{\"success\": false, \"error\": \"Nessun carrello trovato\"}");
                            return;
                        }

                        boolean success = carrelloService.rimuoviProdotto(session, prodottoId);

                        if (success) {
                            int nuovoCount = carrelloService.getNumeroArticoli(session);
                            double nuovoTotale = carrelloService.getTotaleCarrello(session);

                            PrintWriter removeSuccessWriter = response.getWriter();
                            removeSuccessWriter.printf(Locale.US, "{\"success\": true, \"message\": \"Prodotto rimosso dal carrello\", \"count\": %d, \"totale\": %.2f}",
                                    nuovoCount, nuovoTotale);
                        } else {
                            PrintWriter removeFailWriter = response.getWriter();
                            removeFailWriter.print("{\"success\": false, \"error\": \"Prodotto non trovato nel carrello\"}");
                        }

                    } catch (NumberFormatException e) {
                        PrintWriter removeFormatWriter = response.getWriter();
                        removeFormatWriter.print("{\"success\": false, \"error\": \"ID prodotto non valido\"}");
                    }
                    break;

                case "/aggiorna":
                    try {
                        int prodottoId = Integer.parseInt(request.getParameter("prodottoId"));
                        int nuovaQuantita = Integer.parseInt(request.getParameter("quantita"));

                        HttpSession session = request.getSession(false);
                        if (session == null) {
                            PrintWriter updateSessionWriter = response.getWriter();
                            updateSessionWriter.print("{\"success\": false, \"error\": \"Nessun carrello trovato\"}");
                            return;
                        }

                        boolean success = carrelloService.aggiornaQuantita(session, prodottoId, nuovaQuantita);

                        if (success) {
                            int nuovoCount = carrelloService.getNumeroArticoli(session);
                            double nuovoTotale = carrelloService.getTotaleCarrello(session);

                            String message = nuovaQuantita == 0 ? "Prodotto rimosso dal carrello" : "Quantità aggiornata";
                            PrintWriter updateSuccessWriter = response.getWriter();
                            updateSuccessWriter.printf(Locale.US, "{\"success\": true, \"message\": \"%s\", \"count\": %d, \"totale\": %.2f}",
                                    message, nuovoCount, nuovoTotale);
                        } else {
                            PrintWriter updateFailWriter = response.getWriter();
                            updateFailWriter.print("{\"success\": false, \"error\": \"Errore nell'aggiornamento\"}");
                        }

                    } catch (NumberFormatException e) {
                        PrintWriter updateFormatWriter = response.getWriter();
                        updateFormatWriter.print("{\"success\": false, \"error\": \"Parametri non validi\"}");
                    }
                    break;

                case "/svuota":
                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        carrelloService.svuotaCarrello(session);
                    }

                    PrintWriter svuotaWriter = response.getWriter();
                    svuotaWriter.print("{\"success\": true, \"message\": \"Carrello svuotato\", \"count\": 0, \"totale\": 0.0}");
                    break;

                case "/collega-utente":
                    // Endpoint per collegare il carrello quando un utente fa login
                    try {
                        HttpSession loginSession = request.getSession(false);
                        if (loginSession == null) {
                            PrintWriter loginSessionWriter = response.getWriter();
                            loginSessionWriter.print("{\"success\": false, \"error\": \"Nessuna sessione trovata\"}");
                            return;
                        }

                        // Assumendo che l'ID utente venga passato come parametro dopo il login
                        int idUtente = Integer.parseInt(request.getParameter("idUtente"));

                        carrelloService.collegaCarrelloAdUtente(loginSession, idUtente);

                        int count = carrelloService.getNumeroArticoli(loginSession);
                        double totale = carrelloService.getTotaleCarrello(loginSession);

                        PrintWriter collegaWriter = response.getWriter();
                        collegaWriter.printf(Locale.US, "{\"success\": true, \"message\": \"Carrello collegato all'utente\", \"count\": %d, \"totale\": %.2f}",
                                count, totale);

                    } catch (NumberFormatException e) {
                        PrintWriter collegaFormatWriter = response.getWriter();
                        collegaFormatWriter.print("{\"success\": false, \"error\": \"ID utente non valido\"}");
                    }
                    break;

                default:
                    PrintWriter defaultWriter = response.getWriter();
                    defaultWriter.print("{\"success\": false, \"error\": \"Azione non supportata\"}");
            }
        } catch (Exception e) {
            PrintWriter generalErrorWriter = response.getWriter();
            generalErrorWriter.print("{\"success\": false, \"error\": \"Errore del server\"}");
            e.printStackTrace();
        }
    }
}