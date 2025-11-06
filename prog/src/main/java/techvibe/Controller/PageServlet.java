package techvibe.Controller;

import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import techvibe.Model.components.Paginator;
import techvibe.Model.prodotto.Prodotto;
import techvibe.Model.prodotto.ProdottoDao;
import techvibe.Model.prodotto.SqlProdottoDao;
import techvibe.Model.utente.SqlUtenteDao;
import techvibe.Model.utente.Utente;
import techvibe.Model.utente.UtenteDao;
import techvibe.http.*;

import javax.sql.DataSource;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

                // Nel doGet della homepage
                case "/":
                    // Carica i primi 3 prodotti in offerta per la vetrina
                    List<Prodotto> prodottiVetrina = new ArrayList<>();
                    try {
                        // Usa il metodo che hai già creato per le offerte
                        List<Prodotto> tutteLeOfferte = prodottoDao.fetchProdottiInOfferta();
                        // Prendi solo i primi 3 per la vetrina
                        prodottiVetrina = tutteLeOfferte.stream().limit(4).collect(Collectors.toList());
                    } catch (SQLException e) {
                        // Se errore, lista vuota
                        prodottiVetrina = new ArrayList<>();
                    }
                    request.setAttribute("prodottiVetrina", prodottiVetrina);
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
                    break;
                case "/smartphone":
                    //Id categoria
                    try {
                        int idSmartphone = 1;
                        List<Prodotto> prodotti = prodottoDao.fetchProdottiByCategoria(idSmartphone);

                        request.setAttribute("prodotti", prodotti);
                        request.getRequestDispatcher("/WEB-INF/views/site/smartphone.jsp")
                                .forward(request, response);
                    } catch (SQLException e) {
                        throw new ServletException(e);
                    }
                    break;

                case "/tablet":
                    try {
                        int idTablet = 2; //
                        List<Prodotto> prodotti = prodottoDao.fetchProdottiByCategoria(idTablet);
                        request.setAttribute("prodotti", prodotti);
                        request.getRequestDispatcher("/WEB-INF/views/site/tablet.jsp")
                                .forward(request, response);
                    } catch (SQLException e) {
                        throw new ServletException(e);
                    }
                    break;

                //Registrazione utente
                case "/create":
                    request.getRequestDispatcher("/WEB-INF/views/site/create.jsp").forward(request,response);
                    break;

                //Dettagli di un prodotto
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


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String path = request.getPathInfo() != null ? request.getPathInfo() : "/";

            switch (path) {
                case "/create": {
                    UtenteDao<SQLException> utenteDao = new SqlUtenteDao(source);

                    // 1) Validazione con RequestValidator
                    RequestValidator v = new RequestValidator(request);
                    v.assertEmail("email", "Inserisci un indirizzo email valido");
                    v.assertMatch("password", Pattern.compile("^.{8,}$"), "La password deve avere almeno 8 caratteri");
                    v.assertMatch("confirm",  Pattern.compile("^.{8,}$"), "La conferma password deve avere almeno 8 caratteri");
                    // Campi obbligatori base (nome/cognome)
                    v.assertMatch("nome",    Pattern.compile("^\\S.{0,}$"), "Il nome è obbligatorio");
                    v.assertMatch("cognome", Pattern.compile("^\\S.{0,}$"), "Il cognome è obbligatorio");
                    // Telefono (opzionale): se presente, deve rispettare il pattern
                    String telefono = request.getParameter("telefono");
                    if (telefono != null && !telefono.isBlank()) {
                        v.assertMatch("telefono", Pattern.compile("^[+]?\\d[\\d\\s()-]{5,}$"), "Telefono non valido");
                    }

                    // Password = Confirm
                    String password = request.getParameter("password");
                    String confirm  = request.getParameter("confirm");
                    if (password == null || !password.equals(confirm)) {
                        v.getErrors().add("Le password non coincidono");
                    }

                    //Se ci sono errori li raccoglie tutti man mano e li invia all'utente
                    if (v.hasErrors()) {
                        request.setAttribute("errors", v.getErrors());
                        // ripopolo i campi "sicuri" per non farli riscrivere all'utente
                        request.setAttribute("nome",     safe(request.getParameter("nome")));
                        request.setAttribute("cognome",  safe(request.getParameter("cognome")));
                        request.setAttribute("email",    safe(request.getParameter("email")));
                        request.setAttribute("telefono", safe(telefono));
                        request.setAttribute("indirizzospedizione", safe(request.getParameter("indirizzospedizione")));
                        request.getRequestDispatcher("/WEB-INF/views/site/create.jsp").forward(request, response);
                        return;
                    }

                    // 2) Costruzione utente
                    Utente utente = new Utente();
                    utente.setNome(safe(request.getParameter("nome")));
                    utente.setCognome(safe(request.getParameter("cognome")));
                    utente.setEmail(safe(request.getParameter("email")));
                    try {
                        utente.setPassword(password); // tua logica di hash interna
                    } catch (NoSuchAlgorithmException ex) {
                        throw new RuntimeException(ex);
                    }
                    utente.setTelefono(safe(telefono));
                    utente.setIndirizzoSpedizione(safe(request.getParameter("indirizzospedizione")));

                    // 3) Unicità email
                    try {
                        if (utenteDao.existsByEmail(utente.getEmail())) {
                            request.setAttribute("errors", List.of("Email già registrata, usa un altro indirizzo"));
                            request.setAttribute("nome",     utente.getNome());
                            request.setAttribute("cognome",  utente.getCognome());
                            request.setAttribute("email",    utente.getEmail());
                            request.setAttribute("telefono", utente.getTelefono());
                            request.setAttribute("indirizzospedizione", utente.getIndirizzoSpedizione());
                            request.getRequestDispatcher("/WEB-INF/views/site/create.jsp").forward(request, response);
                            return;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    // 4) Persistenza: prova a creare l'utente nel DB
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
                }

                default:
                    notFound();
            }
        } catch (RuntimeException | IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }


    //Trasforma null in"" e toglie spazi all'estremità;
    private String safe(String s) {
        return s == null ? "" : s.trim();
    }



}

