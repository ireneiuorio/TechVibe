package techvibe.Controller;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import techvibe.Model.components.Paginator;
import techvibe.http.Controller;
import techvibe.http.ErrorHandler;
import techvibe.http.CommonValidator;
import techvibe.Model.ordine.Ordine;
import techvibe.Model.ordine.OrdineDao;
import techvibe.Model.ordine.SqlOrdineDao;
import techvibe.Model.prodotto.SqlProdottoDao;
// Imports per il carrello
import techvibe.Model.carrello.CarrelloService;
import techvibe.Model.carrello.SqlCarrelloDao;
import techvibe.Model.utente.SqlUtenteDao;
import techvibe.Model.utente.Utente;
import techvibe.Model.utente.UtenteSession;
import techvibe.Model.utente.UtenteValidator;

import javax.sql.DataSource;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/utente/*")
public class UtenteServlet extends Controller implements ErrorHandler {

    @Resource(name = "jdbc/TechVibe")
    protected DataSource source;
    private SqlUtenteDao utenteDao;
    private CarrelloService carrelloService;

    public void init() throws ServletException {
        super.init(); //chiama l'inizializzazione di base della servlet
        utenteDao = new SqlUtenteDao(source); //crea i Dao
        SqlProdottoDao prodottoDao = new SqlProdottoDao(source);
        SqlCarrelloDao sqlCarrelloDao = new SqlCarrelloDao(source, prodottoDao);
        carrelloService = new CarrelloService(sqlCarrelloDao); //crea i Service
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo() != null ? request.getPathInfo() : "/";

        switch (path) {

            case "/": //CRM
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

            case "/ordine": //UTENTE
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

            case "/create": //CRM
                authorize(request.getSession(false));
                request.getRequestDispatcher("/WEB-INF/views/crm/utente.jsp").forward(request, response);
                break;

            case "/show": //CRM
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

            case "/profile"://UTENTE
                HttpSession session = request.getSession(false);
                if (session == null || session.getAttribute("utenteSession") == null) {
                    response.sendRedirect(request.getContextPath() + "/utente/signin");
                    return;
                }

                UtenteSession utenteSession = (UtenteSession) session.getAttribute("utenteSession");

                if (utenteSession.isAdmin()) {
                    response.sendRedirect(request.getContextPath() + "/crm/home");
                    return;
                } else {
                    try {
                        Optional<Utente> optionalUtente2 = utenteDao.fetchUtente(utenteSession.getId());
                        if (optionalUtente2.isPresent()) {
                            request.setAttribute("utente", optionalUtente2.get());

                            OrdineDao<SQLException> ordineDao = new SqlOrdineDao(source);
                            List<Ordine> ordiniUtente = ordineDao.fetchOrdiniByUtente(utenteSession.getId());
                            request.setAttribute("ordiniUtente", ordiniUtente);

                            request.getRequestDispatcher("/WEB-INF/views/site/profilo.jsp").forward(request, response);
                        } else {
                            // FIX 2: redirect corretto
                            response.sendRedirect(request.getContextPath() + "/utente/signin");
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;

            case "/secret"://CRM
                request.getRequestDispatcher("/WEB-INF/views/crm/secret.jsp").forward(request, response);
                break;

            case "/signup"://UTENTE
                request.getRequestDispatcher("/WEB-INF/views/site/create.jsp").forward(request, response);
                break;

            case "/signin"://UTENTE
                request.getRequestDispatcher("/WEB-INF/views/site/accediutente.jsp").forward(request, response);
                break;

            case "/logout"://UTENTE/CRM
                HttpSession session3 = request.getSession(false);
                if (session3 != null) {
                    UtenteSession utenteSession3 = (UtenteSession) session3.getAttribute("utenteSession");

                    try {
                        carrelloService.onUserLogout(session3);
                        System.out.println("Carrello salvato correttamente durante logout");
                    } catch (Exception e) {
                        System.err.println("Errore durante salvataggio carrello in logout: " + e.getMessage());
                        e.printStackTrace();
                    }

                    String redirect;
                    if (utenteSession3 != null && utenteSession3.isAdmin()) {
                        redirect = request.getContextPath() + "/utente/secret";
                    } else {
                        redirect = request.getContextPath() + "/";
                    }

                    session3.invalidate();

                    response.sendRedirect(redirect);
                } else {
                    response.sendRedirect(request.getContextPath() + "/");
                }
                break;

            case "/admin-profile": // CRM
                HttpSession adminSession = request.getSession(false);
                if (adminSession == null || adminSession.getAttribute("utenteSession") == null) {
                    response.sendRedirect(request.getContextPath() + "/utente/secret");
                    return;
                }

                UtenteSession adminUserSession = (UtenteSession) adminSession.getAttribute("utenteSession");

                if (!adminUserSession.isAdmin()) {
                    response.sendRedirect(request.getContextPath() + "/utente/signin");
                    return;
                }

                try {
                    Optional<Utente> optionalAdmin = utenteDao.fetchUtente(adminUserSession.getId());
                    if (optionalAdmin.isPresent()) {
                        request.setAttribute("admin", optionalAdmin.get());
                        request.getRequestDispatcher("/WEB-INF/views/crm/profilo.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/utente/secret");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
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

                final String email = request.getParameter("email");
                final String password = request.getParameter("password");

                validate(UtenteValidator.validateSignin(request));

                final Utente tmp = new Utente();
                tmp.setEmail(email);
                try {
                    tmp.setPassword(password);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException("SHA-512 non disponibile", e);
                }

                final Optional<Utente> opt;
                try {
                    opt = utenteDao.findUtente(tmp.getEmail(), tmp.getPassword(), true);
                } catch (SQLException e) {
                    throw new RuntimeException("Errore DB durante il login", e);
                }

                if (opt.isEmpty()) {
                    request.setAttribute("loginError", "Credenziali non valide");
                    request.getRequestDispatcher("/WEB-INF/views/crm/secret.jsp").forward(request, response);
                    return;
                }

                final UtenteSession utenteSession = new UtenteSession(opt.get());
                final HttpSession session = request.getSession(true);
                session.setAttribute("utenteSession", utenteSession);
                session.setAttribute("accountSession", utenteSession);

                final int userId = opt.get().getIdUtente();
                try {
                    boolean transferSuccess = carrelloService.onUserLogin(session, userId);
                    if (transferSuccess) {
                        System.out.println("Login admin: carrello collegato correttamente per user " + userId);
                    } else {
                        System.err.println("Login admin: errore nel collegamento carrello per user " + userId);
                    }
                } catch (Exception e) {
                    System.err.println("Errore nel collegamento carrello admin: " + e.getMessage());
                    e.printStackTrace();
                }

                session.setMaxInactiveInterval(30 * 60);
                response.sendRedirect(request.getContextPath() + "/crm/home");
                break;
            }

            case "/signin": {
                final String emailUser = request.getParameter("email");
                final String passwordUser = request.getParameter("password");

                validate(UtenteValidator.validateSignin(request));

                final Utente tmpUser = new Utente();
                tmpUser.setEmail(emailUser);
                try {
                    tmpUser.setPassword(passwordUser);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException("SHA-512 non disponibile", e);
                }

                final Optional<Utente> optUser;
                try {
                    optUser = utenteDao.findUtente(tmpUser.getEmail(), tmpUser.getPassword(), false);
                } catch (SQLException e) {
                    throw new RuntimeException("Errore DB durante il login", e);
                }

                if (optUser.isEmpty()) {
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

                final UtenteSession utenteSession = new UtenteSession(optUser.get());
                final HttpSession sessionUser = request.getSession(true);
                sessionUser.setAttribute("utenteSession", utenteSession);
                sessionUser.setAttribute("accountSession", utenteSession);

                final int userId = optUser.get().getIdUtente();
                try {
                    boolean transferSuccess = carrelloService.onUserLogin(sessionUser, userId);
                    if (transferSuccess) {
                        System.out.println("Login utente: carrello collegato correttamente per user " + userId);
                    } else {
                        System.err.println("Login utente: errore nel collegamento carrello per user " + userId);
                    }
                } catch (Exception e) {
                    System.err.println("Errore nel collegamento carrello utente: " + e.getMessage());
                    e.printStackTrace();
                }

                sessionUser.setMaxInactiveInterval(30 * 60);

                response.sendRedirect(request.getContextPath() + "/carrello/view");
                break;
            }

            case "/profile":
                HttpSession session = request.getSession(false);
                if (session == null || session.getAttribute("utenteSession") == null) {
                    response.sendRedirect(request.getContextPath() + "/utente/signin");
                    return;
                }

                UtenteSession utenteSession1 = (UtenteSession) session.getAttribute("utenteSession");

                if (utenteSession1.isAdmin()) {
                    response.sendRedirect(request.getContextPath() + "/utente/secret");
                    return;
                }

                try {
                    Optional<Utente> optionalUtente = utenteDao.fetchUtente(utenteSession1.getId());
                    if (optionalUtente.isPresent()) {
                        request.setAttribute("utente", optionalUtente.get());
                        request.getRequestDispatcher("/WEB-INF/views/site/profilo.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/utente/signin");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "/update":
                HttpSession updateSession = request.getSession(false);
                if (updateSession == null || updateSession.getAttribute("utenteSession") == null) {
                    response.sendRedirect(request.getContextPath() + "/utente/signin");
                    return;
                }

                UtenteSession currentUser = (UtenteSession) updateSession.getAttribute("utenteSession");

                String nomeUpdate = request.getParameter("nome");
                String cognomeUpdate = request.getParameter("cognome");
                String emailUpdate = request.getParameter("email");
                String telefonoUpdate = request.getParameter("telefono");
                String indirizzoUpdate = request.getParameter("indirizzo");

                Utente utenteAggiornato = new Utente();
                utenteAggiornato.setIdUtente(currentUser.getId());
                utenteAggiornato.setNome(nomeUpdate);
                utenteAggiornato.setCognome(cognomeUpdate);
                utenteAggiornato.setEmail(emailUpdate);
                utenteAggiornato.setTelefono(telefonoUpdate);
                utenteAggiornato.setIndirizzoSpedizione(indirizzoUpdate);

                try {
                    if (utenteDao.updateUtente(utenteAggiornato)) {
                        UtenteSession newSession = new UtenteSession(utenteAggiornato);
                        updateSession.setAttribute("utenteSession", newSession);
                        request.setAttribute("successMessage", "Profilo aggiornato con successo!");
                    } else {
                        request.setAttribute("errorMessage", "Errore nell'aggiornamento del profilo");
                    }
                } catch (SQLException e) {
                    request.setAttribute("errorMessage", "Errore del sistema");
                }

                response.sendRedirect(request.getContextPath() + "/utente/profile");
                break;

            case "/cambiastato":
                authorize(request.getSession(false));

                String idParam = request.getParameter("id");
                String azione = request.getParameter("azione");

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
                        response.sendRedirect(request.getContextPath() + "/utente/?success=" + azione);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/utente/?error=cambio_stato");
                    }

                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID utente non valido");
                } catch (SQLException e) {
                    throw new RuntimeException("Errore durante il cambio stato", e);
                }
                break;

            case "/create":
                authorize(request.getSession(false));

                String nome = request.getParameter("nome");
                String cognome = request.getParameter("cognome");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String telefono = request.getParameter("telefono");
                String indirizzo = request.getParameter("indirizzo");

                try {
                    if (nome == null || nome.trim().isEmpty() ||
                            cognome == null || cognome.trim().isEmpty() ||
                            email == null || email.trim().isEmpty() ||
                            password == null || password.trim().isEmpty()) {

                        request.setAttribute("errorMessage", "Tutti i campi obbligatori devono essere compilati");
                        request.getRequestDispatcher("/WEB-INF/views/crm/utente.jsp").forward(request, response);
                        return;
                    }

                    if (utenteDao.existsByEmail(email)) {
                        request.setAttribute("errorMessage", "Email già esistente nel sistema");
                        request.getRequestDispatcher("/WEB-INF/views/crm/utente.jsp").forward(request, response);
                        return;
                    }

                    Utente nuovoAdmin = new Utente();
                    nuovoAdmin.setNome(nome.trim());
                    nuovoAdmin.setCognome(cognome.trim());
                    nuovoAdmin.setEmail(email.trim().toLowerCase());
                    nuovoAdmin.setPassword(password);
                    nuovoAdmin.setTelefono(telefono != null ? telefono.trim() : "");
                    nuovoAdmin.setIndirizzoSpedizione(indirizzo != null ? indirizzo.trim() : "");
                    nuovoAdmin.setAdmin(true);
                    nuovoAdmin.setStato("ATTIVO");

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
                authorize(request.getSession(false));

                String idParame = request.getParameter("id");
                String telefono1 = request.getParameter("telefono");
                String indirizzo1 = request.getParameter("indirizzo");

                if (idParame == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID utente mancante");
                    return;
                }

                try {
                    int utenteId = Integer.parseInt(idParame);

                    Optional<Utente> optUtente = utenteDao.fetchUtente(utenteId);
                    if (optUtente.isEmpty()) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Utente non trovato");
                        return;
                    }

                    Utente utente = optUtente.get();
                    utente.setTelefono(telefono1 != null ? telefono1.trim() : "");
                    utente.setIndirizzoSpedizione(indirizzo1 != null ? indirizzo1.trim() : "");

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

            case "/change-password":
                HttpSession passwordSession = request.getSession(false);
                if (passwordSession == null || passwordSession.getAttribute("utenteSession") == null) {
                    response.sendRedirect(request.getContextPath() + "/utente/signin");
                    return;
                }

                UtenteSession currentUserPassword = (UtenteSession) passwordSession.getAttribute("utenteSession");

                String currentPassword = request.getParameter("currentPassword");
                String newPassword = request.getParameter("newPassword");
                String confirmPassword = request.getParameter("confirmPassword");

                // Validazione dei parametri
                if (currentPassword == null || currentPassword.trim().isEmpty() ||
                        newPassword == null || newPassword.trim().isEmpty() ||
                        confirmPassword == null || confirmPassword.trim().isEmpty()) {

                    response.sendRedirect(request.getContextPath() + "/utente/profile?tab=password&error=missing_fields");
                    return;
                }

                // Controlla che le nuove password coincidano
                if (!newPassword.equals(confirmPassword)) {
                    response.sendRedirect(request.getContextPath() + "/utente/profile?tab=password&error=password_mismatch");
                    return;
                }

                // Validazione lunghezza password
                if (newPassword.length() < 6) {
                    response.sendRedirect(request.getContextPath() + "/utente/profile?tab=password&error=password_too_short");
                    return;
                }

                try {
                    // Prima recupera l'utente completo per ottenere l'email
                    Optional<Utente> currentUserData = utenteDao.fetchUtente(currentUserPassword.getId());
                    if (currentUserData.isEmpty()) {
                        response.sendRedirect(request.getContextPath() + "/utente/profile?tab=password&error=user_not_found");
                        return;
                    }

                    // Verifica la password attuale
                    Utente tempUser = new Utente();
                    tempUser.setEmail(currentUserData.get().getEmail());
                    tempUser.setPassword(currentPassword);

                    Optional<Utente> userCheck = utenteDao.findUtente(tempUser.getEmail(), tempUser.getPassword(), false);

                    if (userCheck.isEmpty()) {
                        response.sendRedirect(request.getContextPath() + "/utente/profile?tab=password&error=wrong_current_password");
                        return;
                    }

                    // Aggiorna la password
                    if (utenteDao.updatePassword(currentUserPassword.getId(), newPassword)) {
                        response.sendRedirect(request.getContextPath() + "/utente/profile?tab=password&success=password_changed");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/utente/profile?tab=password&error=update_failed");
                    }

                } catch (SQLException e) {
                    throw new RuntimeException("Errore durante il cambio password", e);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException("Errore nella crittografia", e);
                }
                break;

            case "/admin-change-password":
                HttpSession adminPasswordSession = request.getSession(false);
                if (adminPasswordSession == null || adminPasswordSession.getAttribute("utenteSession") == null) {
                    response.sendRedirect(request.getContextPath() + "/utente/secret");
                    return;
                }

                UtenteSession currentAdminPassword = (UtenteSession) adminPasswordSession.getAttribute("utenteSession");

                if (!currentAdminPassword.isAdmin()) {
                    response.sendRedirect(request.getContextPath() + "/utente/signin");
                    return;
                }

                String currentPasswordAdmin = request.getParameter("currentPassword");
                String newPasswordAdmin = request.getParameter("newPassword");
                String confirmPasswordAdmin = request.getParameter("confirmPassword");

                // Validazione parametri
                if (currentPasswordAdmin == null || currentPasswordAdmin.trim().isEmpty() ||
                        newPasswordAdmin == null || newPasswordAdmin.trim().isEmpty() ||
                        confirmPasswordAdmin == null || confirmPasswordAdmin.trim().isEmpty()) {

                    response.sendRedirect(request.getContextPath() + "/utente/admin-profile?tab=password&error=missing_fields");
                    return;
                }

                // Controlla che le nuove password coincidano
                if (!newPasswordAdmin.equals(confirmPasswordAdmin)) {
                    response.sendRedirect(request.getContextPath() + "/utente/admin-profile?tab=password&error=password_mismatch");
                    return;
                }

                // Validazione lunghezza password
                if (newPasswordAdmin.length() < 6) {
                    response.sendRedirect(request.getContextPath() + "/utente/admin-profile?tab=password&error=password_too_short");
                    return;
                }

                try {
                    // Recupera l'utente completo per ottenere l'email
                    Optional<Utente> currentAdminData = utenteDao.fetchUtente(currentAdminPassword.getId());
                    if (currentAdminData.isEmpty()) {
                        response.sendRedirect(request.getContextPath() + "/utente/admin-profile?tab=password&error=user_not_found");
                        return;
                    }

                    // Verifica la password attuale
                    Utente tempAdmin = new Utente();
                    tempAdmin.setEmail(currentAdminData.get().getEmail());
                    tempAdmin.setPassword(currentPasswordAdmin);

                    Optional<Utente> adminCheck = utenteDao.findUtente(tempAdmin.getEmail(), tempAdmin.getPassword(), true);

                    if (adminCheck.isEmpty()) {
                        response.sendRedirect(request.getContextPath() + "/utente/admin-profile?tab=password&error=wrong_current_password");
                        return;
                    }

                    // Aggiorna la password
                    if (utenteDao.updatePassword(currentAdminPassword.getId(), newPasswordAdmin)) {
                        response.sendRedirect(request.getContextPath() + "/utente/admin-profile?tab=password&success=password_changed");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/utente/admin-profile?tab=password&error=update_failed");
                    }

                } catch (SQLException e) {
                    throw new RuntimeException("Errore durante il cambio password admin", e);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException("Errore nella crittografia", e);
                }
                break;

            case "/admin-change-email":
                HttpSession adminEmailSession = request.getSession(false);
                if (adminEmailSession == null || adminEmailSession.getAttribute("utenteSession") == null) {
                    response.sendRedirect(request.getContextPath() + "/utente/secret");
                    return;
                }

                UtenteSession currentAdminEmail = (UtenteSession) adminEmailSession.getAttribute("utenteSession");

                if (!currentAdminEmail.isAdmin()) {
                    response.sendRedirect(request.getContextPath() + "/utente/signin");
                    return;
                }

                String currentPasswordForEmail = request.getParameter("currentPassword");
                String newEmailAdmin = request.getParameter("newEmail");

                // Validazione parametri
                if (currentPasswordForEmail == null || currentPasswordForEmail.trim().isEmpty() ||
                        newEmailAdmin == null || newEmailAdmin.trim().isEmpty()) {

                    response.sendRedirect(request.getContextPath() + "/utente/admin-profile?tab=email&error=missing_fields");
                    return;
                }

                // Validazione formato email
                if (!newEmailAdmin.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    response.sendRedirect(request.getContextPath() + "/utente/admin-profile?tab=email&error=invalid_email_format");
                    return;
                }

                try {
                    // Recupera l'utente completo
                    Optional<Utente> currentAdminDataEmail = utenteDao.fetchUtente(currentAdminEmail.getId());
                    if (currentAdminDataEmail.isEmpty()) {
                        response.sendRedirect(request.getContextPath() + "/utente/admin-profile?tab=email&error=user_not_found");
                        return;
                    }

                    // Controlla se la nuova email è uguale a quella attuale
                    if (newEmailAdmin.equals(currentAdminDataEmail.get().getEmail())) {
                        response.sendRedirect(request.getContextPath() + "/utente/admin-profile?tab=email&error=same_email");
                        return;
                    }

                    // Controlla se l'email esiste già
                    if (utenteDao.existsByEmail(newEmailAdmin)) {
                        response.sendRedirect(request.getContextPath() + "/utente/admin-profile?tab=email&error=email_exists");
                        return;
                    }

                    // Verifica la password attuale
                    Utente tempAdminEmail = new Utente();
                    tempAdminEmail.setEmail(currentAdminDataEmail.get().getEmail());
                    tempAdminEmail.setPassword(currentPasswordForEmail);

                    Optional<Utente> adminEmailCheck = utenteDao.findUtente(tempAdminEmail.getEmail(), tempAdminEmail.getPassword(), true);

                    if (adminEmailCheck.isEmpty()) {
                        response.sendRedirect(request.getContextPath() + "/utente/admin-profile?tab=email&error=wrong_current_password");
                        return;
                    }

                    // Aggiorna l'email
                    if (utenteDao.updateEmail(currentAdminEmail.getId(), newEmailAdmin)) {
                        response.sendRedirect(request.getContextPath() + "/utente/admin-profile?tab=email&success=email_changed");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/utente/admin-profile?tab=email&error=update_failed");
                    }

                } catch (SQLException e) {
                    throw new RuntimeException("Errore durante il cambio email admin", e);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException("Errore nella crittografia", e);
                }
                break;

            case "/delete": {
                HttpSession s = request.getSession(false);
                UtenteSession us = (s != null) ? (UtenteSession) s.getAttribute("utenteSession") : null;
                if (us == null || !us.isAdmin()) { response.sendError(HttpServletResponse.SC_FORBIDDEN); return; }

                int userId = Integer.parseInt(request.getParameter("id"));
                // opzionale ma consigliato: non permettere l'auto-eliminazione
                if (userId == us.getId()) {
                    response.sendRedirect(request.getContextPath() + "/utente/?error=self_delete");
                    return;
                }

                boolean ok = false;
                try {
                    ok = utenteDao.deleteAccountWithRelations(userId);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                response.sendRedirect(request.getContextPath() + "/utente/?"
                        + (ok ? "success=deleted" : "error=delete_failed"));
                break;
            }

            default:
                notFound();
        }
    }
}
