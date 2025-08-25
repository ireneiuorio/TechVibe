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


            case "/secret":
                request.getRequestDispatcher("/WEB-INF/views/crm/secret.jsp").forward(request, response);
            case "/signup":
                request.getRequestDispatcher("/WEB-INF/views/site/singup.jsp");
                break;

            case "/signin":
                request.getRequestDispatcher("/WEB-INF/views/site/singin.jsp");
                break;

            case "/profile":
                break;

            case "/logout":
                HttpSession session = request.getSession(false);
                authenticate(session);
                UtenteSession utenteSession = (UtenteSession) session.getAttribute("utenteSession");
                String redirect = utenteSession.isAdmin() ? "/prog_war/utente/secret" : "/prog_war/utente/signin";
                session.removeAttribute("utenteSession");
                session.invalidate();
                response.sendRedirect(redirect);
                break;


            default:
                notFound();
        }


    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = (request.getPathInfo() != null) ? request.getPathInfo() : "/";
        switch (path) {

            case "/signin": //login cliente ricerca nel db
                break;
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

            default:
                notFound();


        }
    }

}


