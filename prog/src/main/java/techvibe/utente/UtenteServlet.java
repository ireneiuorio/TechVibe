package techvibe.utente;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.eclipse.tags.shaded.org.apache.bcel.generic.NEW;
import techvibe.categoria.Categoria;
import techvibe.components.Paginator;
import techvibe.http.Controller;
import techvibe.http.ErrorHandler;
import techvibe.http.InvalidRequestException;
import techvibe.http.CommonValidator;
import techvibe.ordine.Ordine;
import techvibe.ordine.OrdineDao;
import techvibe.ordine.SqlOrdineDao;
import techvibe.prodotto.Prodotto;
import techvibe.prodotto.ProdottoDao;
import techvibe.prodotto.SqlProdottoDao;
// Aggiungi imports per il carrello
import techvibe.carrello.CarrelloService;
import techvibe.carrello.CarrelloDao;

import javax.sql.DataSource;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebServlet("/utente/*")
public class UtenteServlet extends Controller implements ErrorHandler {

    @Resource(name = "jdbc/TechVibe")
    protected DataSource source;
    private SqlUtenteDao utenteDao;
    private CarrelloService carrelloService; // Nuovo servizio carrello

    public void init() throws ServletException {
        super.init();
        utenteDao = new SqlUtenteDao(source);

        // Inizializza il servizio carrello
        SqlProdottoDao prodottoDao = new SqlProdottoDao(source);
        CarrelloDao carrelloDao = new CarrelloDao(source, prodottoDao);
        carrelloService = new CarrelloService(carrelloDao);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo() != null ? request.getPathInfo() : "/";

        switch (path) {

            case "/":
                authorize(request.getSession(false));
                int page = parsePage(request);
                Paginator paginator = new Paginator(page, 10);
                int size = 0;
                try {
                    size = utenteDao.countAll();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                request.setAttribute("pages", paginator.getPages(size));

                List<Utente> listaUtenti = null;
                try {
                    listaUtenti = utenteDao.fetchUtenti(paginator);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                request.setAttribute("listaUtenti", listaUtenti);
                request.getRequestDispatcher("/WEB-INF/views/crm/utenti.jsp").forward(request, response);
                break;

            case "/ordine":
                HttpSession sessionOrdine = request.getSession(false);
                if (sessionOrdine == null || sessionOrdine.getAttribute("utenteSession") == null) {
                    response.sendRedirect(request.getContextPath() + "/utente/signin");
                    return;
                }

                UtenteSession utenteSessionOrdine = (UtenteSession) sessionOrdine.getAttribute("utenteSession");
                String ordineIdParam = request.getParameter("id");

                if (ordineIdParam == null) {
                    response.sendRedirect(request.getContextPath() + "/utente/profile");
                    return;
                }

                try {
                    int ordineId = Integer.parseInt(ordineIdParam);

                    SqlOrdineDao ordineDao = new SqlOrdineDao(source);
                    Optional<Ordine> ordineDettaglio = ordineDao.fetchOrdineConProdottiById(ordineId, utenteSessionOrdine.getId());

                    if (ordineDettaglio.isPresent()) {
                        request.setAttribute("ordine", ordineDettaglio.get());
                        request.getRequestDispatcher("/WEB-INF/views/site/dettaglio-ordine.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/utente/profile");
                    }

                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/utente/profile");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "/create":
                authorize(request.getSession(false)); // Solo admin possono accedere
                request.getRequestDispatcher("/WEB-INF/views/crm/utente.jsp").forward(request, response);
                break;

            case "/show":
                authorize(request.getSession(false));
                validate(CommonValidator.validateId(request));
                int id = Integer.parseInt(request.getParameter("id"));
                Optional<Utente> optionalUtente;
                try {
                    optionalUtente = utenteDao.fetchUtente(id);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if (optionalUtente.isPresent()) {
                    request.setAttribute("utente", optionalUtente.get());

                    // AGGIUNGI QUESTO: Carica gli ordini dell'utente
                    try {
                        SqlOrdineDao ordineDao = new SqlOrdineDao(source);
                        List<Ordine> ordiniUtente = ordineDao.fetchOrdiniByUtente(id);
                        request.setAttribute("ordiniUtente", ordiniUtente);
                    } catch (SQLException e) {
                        throw new RuntimeException("Errore nel caricamento ordini", e);
                    }

                    request.getRequestDispatcher("/WEB-INF/views/crm/utente-dettagli.jsp").forward(request, response);
                } else {
                    notFound();
                }
                break;

            case "/profile":
                // Verifica che l'utente sia loggato
                HttpSession session = request.getSession(false);
                if (session == null || session.getAttribute("utenteSession") == null) {
                    response.sendRedirect(request.getContextPath() + "/utente/signin");
                    return;
                }

                UtenteSession utenteSession = (UtenteSession) session.getAttribute("utenteSession");

                if (utenteSession.isAdmin()) {
                    response.sendRedirect(request.getContextPath() + "/crm/home");
                    return;
                }
                else {
                    // Recupera i dati completi dell'utente dal database
                    try {
                        Optional<Utente> optionalUtente2 = utenteDao.fetchUtente(utenteSession.getId());
                        if (optionalUtente2.isPresent()) {
                            request.setAttribute("utente", optionalUtente2.get());

                            // AGGIUNGI QUESTO: Carica gli ordini dell'utente
                            OrdineDao<SQLException> ordineDao = new SqlOrdineDao(source);
                            List<Ordine> ordiniUtente = ordineDao.fetchOrdiniByUtente(utenteSession.getId());
                            request.setAttribute("ordiniUtente", ordiniUtente);

                            request.getRequestDispatcher("/WEB-INF/views/site/profilo.jsp").forward(request, response);
                        } else {
                            response.sendRedirect(request.getContextPath() + "/utente/accediutente");
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;

            case "/secret":
                request.getRequestDispatcher("/WEB-INF/views/crm/secret.jsp").forward(request, response);
                break;

            case "/signup":
                request.getRequestDispatcher("/WEB-INF/views/site/singup.jsp").forward(request, response);
                break;

            case "/signin":
                request.getRequestDispatcher("/WEB-INF/views/site/singin.jsp").forward(request, response);
                break;

            case "/logout":
                HttpSession session3 = request.getSession(false);
                if (session3 != null) {
                    UtenteSession utenteSession3 = (UtenteSession) session3.getAttribute("utenteSession");

                    // Determina dove reindirizzare
                    String redirect;
                    if (utenteSession3 != null && utenteSession3.isAdmin()) {
                        redirect = request.getContextPath() + "/utente/secret";
                    } else {
                        redirect = request.getContextPath() + "/"; // Home per utenti normali
                    }

                    // Pulisci TUTTI gli attributi del carrello
                    session3.removeAttribute("utenteSession");
                    session3.removeAttribute("accountSession");
                    session3.removeAttribute("user_id"); // Rimuovi l'ID utente per il carrello
                    session3.removeAttribute("carrello"); // Rimuovi il carrello dalla sessione
                    session3.removeAttribute("carrello_id"); // Rimuovi l'ID carrello
                    session3.invalidate();

                    response.sendRedirect(redirect);
                } else {
                    response.sendRedirect(request.getContextPath() + "/");
                }
                break;

            default:
                notFound();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = (request.getPathInfo() != null) ? request.getPathInfo() : "/";
        switch (path) {

            case "/secret": {
                request.setAttribute("back", "/WEB-INF/views/crm/secret.jsp");

                // 1) leggi parametri ESATTAMENTE come nella form
                final String email = request.getParameter("email");
                final String password = request.getParameter("password");

                // 2) validazione lato server (usa il vostro validator)
                validate(UtenteValidator.validateSignin(request));

                // 3) hash della password con il TUO setPassword (SHA-512)
                final Utente tmp = new Utente();
                tmp.setEmail(email);
                try {
                    tmp.setPassword(password); // -> tmp.getPassword() è l'hash esadecimale (128 char)
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException("SHA-512 non disponibile", e);
                }

                // 4) lookup in DB: email + hash + admin = true
                final Optional<Utente> opt;
                try {
                    opt = utenteDao.findUtente(tmp.getEmail(), tmp.getPassword(), true);
                } catch (SQLException e) {
                    throw new RuntimeException("Errore DB durante il login", e);
                }

                // 5) esito autenticazione
                if (opt.isEmpty()) {
                    // lascia gestire all'handler la view "back" + messaggi
                    request.setAttribute("loginError", "Credenziali non valide");
                    request.getRequestDispatcher("/WEB-INF/views/crm/secret.jsp").forward(request, response);
                    return;
                }

                // 6) crea la sessione e salva GLI STESSI attributi che il progetto si aspetta
                final UtenteSession utenteSession = new UtenteSession(opt.get());
                final HttpSession session = request.getSession(true);
                session.setAttribute("utenteSession", utenteSession);
                session.setAttribute("accountSession", utenteSession); // molte parti controllano questo

                // NUOVO: Collega il carrello all'utente admin (se ne ha uno)
                session.setAttribute("user_id", opt.get().getIdUtente()); // Per il CarrelloService
                try {
                    carrelloService.collegaCarrelloAdUtente(session, opt.get().getIdUtente());
                } catch (Exception e) {
                    // Log dell'errore ma non interrompere il login
                    System.err.println("Errore nel collegamento carrello admin: " + e.getMessage());
                }

                session.setMaxInactiveInterval(30 * 60); // 30 minuti (opzionale ma carino)

                request.getRequestDispatcher("/WEB-INF/views/crm/home.jsp").forward(request, response);
                break;
            }

            case "/signin": // login utente normale
                // 1) leggi parametri dalla form
                final String emailUser = request.getParameter("email");
                final String passwordUser = request.getParameter("password");

                // 2) validazione lato server
                validate(UtenteValidator.validateSignin(request));

                // 3) hash della password
                final Utente tmpUser = new Utente();
                tmpUser.setEmail(emailUser);
                try {
                    tmpUser.setPassword(passwordUser); // hash SHA-512
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException("SHA-512 non disponibile", e);
                }

                // 4) lookup in DB: email + hash + admin = false (utente normale)
                final Optional<Utente> optUser;
                try {
                    optUser = utenteDao.findUtente(tmpUser.getEmail(), tmpUser.getPassword(), false);
                } catch (SQLException e) {
                    throw new RuntimeException("Errore DB durante il login", e);
                }

                // 5) esito autenticazione
                if (optUser.isEmpty()) {
                    // Controlla se l'utente esiste ma è disattivato
                    try {
                        if (utenteDao.isUtenteDisattivato(tmpUser.getEmail(), tmpUser.getPassword(), false)) {
                            request.setAttribute("loginError", "Account disattivato. Contatta l'amministratore.");
                        } else {
                            request.setAttribute("loginError", "Credenziali non valide");
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException("Errore DB durante controllo stato", e);
                    }

                    request.getRequestDispatcher("/WEB-INF/views/site/accediutente.jsp").forward(request, response);
                    return;
                }

                // 6) crea la sessione utente
                final UtenteSession utenteSession = new UtenteSession(optUser.get());
                final HttpSession sessionUser = request.getSession(true);
                sessionUser.setAttribute("utenteSession", utenteSession);
                sessionUser.setAttribute("accountSession", utenteSession); // per compatibilità

                // IMPORTANTE: Imposta user_id PRIMA del collegamento carrello
                final int userId = optUser.get().getIdUtente();
                sessionUser.setAttribute("user_id", userId);
                System.out.println("DEBUG LOGIN: User ID impostato: " + userId);

                // NUOVO: Collega il carrello all'utente
                try {
                    carrelloService.collegaCarrelloAdUtente(sessionUser, userId);
                } catch (Exception e) {
                    // Log dell'errore ma non interrompere il login
                    System.err.println("Errore nel collegamento carrello utente: " + e.getMessage());
                    e.printStackTrace();
                }

                sessionUser.setMaxInactiveInterval(30 * 60); // 30 minuti

                response.sendRedirect(request.getContextPath() + "/utente/profile");
                break;

            case "/profile":
                // Controllo autenticazione (come fai per /secret)
                HttpSession session = request.getSession(false);
                if (session == null || session.getAttribute("utenteSession") == null) {
                    response.sendRedirect(request.getContextPath() + "/utente/accediutente");
                    return;
                }

                UtenteSession utenteSession1 = (UtenteSession) session.getAttribute("utenteSession");

                // Verifica che NON sia admin (gli admin non vanno nel profilo utente)
                if (utenteSession1.isAdmin()) {
                    response.sendRedirect(request.getContextPath() + "/utente/secret");
                    return;
                }

                // Recupera dati completi dal database
                try {
                    Optional<Utente> optionalUtente = utenteDao.fetchUtente(utenteSession1.getId());
                    if (optionalUtente.isPresent()) {
                        request.setAttribute("utente", optionalUtente.get());
                        request.getRequestDispatcher("/WEB-INF/views/site/profile.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/site/accediutente");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "/update":
                // Verifica autenticazione
                HttpSession updateSession = request.getSession(false);
                if (updateSession == null || updateSession.getAttribute("utenteSession") == null) {
                    response.sendRedirect(request.getContextPath() + "/utente/signin");
                    return;
                }

                UtenteSession currentUser = (UtenteSession) updateSession.getAttribute("utenteSession");

                // Leggi parametri
                String nomeUpdate = request.getParameter("nome");
                String cognomeUpdate = request.getParameter("cognome");
                String emailUpdate = request.getParameter("email");
                String telefonoUpdate = request.getParameter("telefono");
                String indirizzoUpdate = request.getParameter("indirizzo");

                // Crea utente aggiornato
                Utente utenteAggiornato = new Utente();
                utenteAggiornato.setIdUtente(currentUser.getId());
                utenteAggiornato.setNome(nomeUpdate);
                utenteAggiornato.setCognome(cognomeUpdate);
                utenteAggiornato.setEmail(emailUpdate);
                utenteAggiornato.setTelefono(telefonoUpdate);
                utenteAggiornato.setIndirizzoSpedizione(indirizzoUpdate);

                try {
                    if (utenteDao.updateUtente(utenteAggiornato)) {
                        // Aggiorna anche la sessione
                        UtenteSession newSession = new UtenteSession(utenteAggiornato);
                        updateSession.setAttribute("utenteSession", newSession);

                        request.setAttribute("successMessage", "Profilo aggiornato con successo!");
                    } else {
                        request.setAttribute("errorMessage", "Errore nell'aggiornamento del profilo");
                    }
                } catch (SQLException e) {
                    request.setAttribute("errorMessage", "Errore del sistema");
                }

                // Ricarica la pagina profilo
                response.sendRedirect(request.getContextPath() + "/utente/profile");
                break;

            // NUOVO CASE: Gestione cambio stato utente
            case "/cambiastato":
                authorize(request.getSession(false)); // Solo admin

                // Leggi parametri
                String idParam = request.getParameter("id");
                String azione = request.getParameter("azione");

                // Validazione
                if (idParam == null || azione == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametri mancanti");
                    return;
                }

                try {
                    int utenteId = Integer.parseInt(idParam);

                    boolean success = false;
                    if ("attiva".equals(azione)) {
                        success = utenteDao.attivaUtente(utenteId);
                    } else if ("disattiva".equals(azione)) {
                        success = utenteDao.disattivaUtente(utenteId);
                    }

                    if (success) {
                        // Redirect con messaggio di successo
                        response.sendRedirect(request.getContextPath() + "/utente/?success=" + azione);
                    } else {
                        // Redirect con messaggio di errore
                        response.sendRedirect(request.getContextPath() + "/utente/?error=cambio_stato");
                    }

                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID utente non valido");
                } catch (SQLException e) {
                    throw new RuntimeException("Errore durante il cambio stato", e);
                }
                break;

            case "/create":
                // Solo admin possono creare altri admin
                authorize(request.getSession(false));

                // Leggi i parametri dalla form
                String nome = request.getParameter("nome");
                String cognome = request.getParameter("cognome");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String telefono = request.getParameter("telefono");
                String indirizzo = request.getParameter("indirizzo");

                try {
                    // Validazione base
                    if (nome == null || nome.trim().isEmpty() ||
                            cognome == null || cognome.trim().isEmpty() ||
                            email == null || email.trim().isEmpty() ||
                            password == null || password.trim().isEmpty()) {

                        request.setAttribute("errorMessage", "Tutti i campi obbligatori devono essere compilati");
                        request.getRequestDispatcher("/WEB-INF/views/crm/utente.jsp").forward(request, response);
                        return;
                    }

                    // Controlla se l'email esiste già
                    if (utenteDao.existsByEmail(email)) {
                        request.setAttribute("errorMessage", "Email già esistente nel sistema");
                        request.getRequestDispatcher("/WEB-INF/views/crm/utente.jsp").forward(request, response);
                        return;
                    }

                    // Crea il nuovo admin
                    Utente nuovoAdmin = new Utente();
                    nuovoAdmin.setNome(nome.trim());
                    nuovoAdmin.setCognome(cognome.trim());
                    nuovoAdmin.setEmail(email.trim().toLowerCase());
                    nuovoAdmin.setPassword(password); // Hash automatico
                    nuovoAdmin.setTelefono(telefono != null ? telefono.trim() : "");
                    nuovoAdmin.setIndirizzoSpedizione(indirizzo != null ? indirizzo.trim() : "");
                    nuovoAdmin.setAdmin(true); // SEMPRE admin
                    nuovoAdmin.setStato("ATTIVO"); // Sempre attivo

                    // Salva nel database
                    if (utenteDao.createUtente(nuovoAdmin)) {
                        response.sendRedirect(request.getContextPath() + "/utente/?success=admin_created");
                    } else {
                        request.setAttribute("errorMessage", "Errore durante la creazione dell'amministratore");
                        request.getRequestDispatcher("/WEB-INF/views/crm/utente.jsp").forward(request, response);
                    }

                } catch (NoSuchAlgorithmException e) {
                    request.setAttribute("errorMessage", "Errore nel sistema di crittografia");
                    request.getRequestDispatcher("/WEB-INF/views/crm/utente.jsp").forward(request, response);
                } catch (SQLException e) {
                    throw new RuntimeException("Errore database durante creazione admin", e);
                }
                break;

            case "/update-contacts":
                authorize(request.getSession(false)); // Solo admin

                // Leggi parametri
                String idParame = request.getParameter("id");
                String telefono1 = request.getParameter("telefono");
                String indirizzo1 = request.getParameter("indirizzo");

                // Validazione ID
                if (idParame == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID utente mancante");
                    return;
                }

                try {
                    int utenteId = Integer.parseInt(idParame);

                    // Recupera l'utente esistente dal database
                    Optional<Utente> optUtente = utenteDao.fetchUtente(utenteId);
                    if (optUtente.isEmpty()) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Utente non trovato");
                        return;
                    }

                    // Aggiorna solo telefono e indirizzo
                    Utente utente = optUtente.get();
                    utente.setTelefono(telefono1 != null ? telefono1.trim() : "");
                    utente.setIndirizzoSpedizione(indirizzo1 != null ? indirizzo1.trim() : "");

                    // Salva nel database
                    if (utenteDao.updateUtente(utente)) {
                        response.sendRedirect(request.getContextPath() + "/utente/show?id=" + utenteId + "&success=contacts_updated");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/utente/show?id=" + utenteId + "&error=update_failed");
                    }

                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID utente non valido");
                } catch (SQLException e) {
                    throw new RuntimeException("Errore durante l'aggiornamento contatti", e);
                }
                break;

            default:
                notFound();
        }
    }
}