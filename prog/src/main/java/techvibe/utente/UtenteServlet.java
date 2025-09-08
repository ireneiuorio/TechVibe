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
import techvibe.prodotto.Prodotto;
import techvibe.prodotto.ProdottoDao;
import techvibe.prodotto.SqlProdottoDao;

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
    private UtenteDao<SQLException> utenteDao;

    public void init() throws ServletException {
        super.init();
        utenteDao = new SqlUtenteDao(source);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo() != null ? request.getPathInfo() : "/";

        switch (path) {

            case "/":
                authorize(request.getSession(false));
                int page = parsePage(request);
                Paginator paginator = new Paginator(page, 50);
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

            case "/create":
                authorize(request.getSession(false));
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
                    request.getRequestDispatcher("/WEB-INF/views/crm/utente.jsp").forward(request, response);
                } else {
                    notFound();
                }

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
            case "/signup":
                request.getRequestDispatcher("/WEB-INF/views/site/singup.jsp");
                break;

            case "/signin":
                request.getRequestDispatcher("/WEB-INF/views/site/singin.jsp");
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

                    // Pulisci la sessione
                    session3.removeAttribute("utenteSession");
                    session3.removeAttribute("accountSession");
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
                }

                // 6) crea la sessione e salva GLI STESSI attributi che il progetto si aspetta
                final UtenteSession utenteSession = new UtenteSession(opt.get());
                final HttpSession session = request.getSession(true);
                session.setAttribute("utenteSession", utenteSession);
                session.setAttribute("accountSession", utenteSession); // molte parti controllano questo
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
                    request.setAttribute("loginError", "Credenziali non valide");
                    request.getRequestDispatcher("/WEB-INF/views/site/accediutente.jsp").forward(request, response);
                    return;
                }

                // 6) crea la sessione utente
                final UtenteSession utenteSession = new UtenteSession(optUser.get());
                final HttpSession sessionUser = request.getSession(true);
                sessionUser.setAttribute("utenteSession", utenteSession);
                sessionUser.setAttribute("accountSession", utenteSession); // per compatibilità
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

            default:
                notFound();


        }
    }

}


