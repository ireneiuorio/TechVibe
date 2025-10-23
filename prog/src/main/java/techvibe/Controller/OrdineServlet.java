package techvibe.Controller;

import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import techvibe.Model.components.Paginator;
import techvibe.http.CommonValidator;
import techvibe.http.Controller;
import techvibe.http.ErrorHandler;
import techvibe.Model.ordine.Ordine;
import techvibe.Model.ordine.OrdineDao;
import techvibe.Model.ordine.SqlOrdineDao;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "OrdineServlet", value = "/ordini/*")
public class OrdineServlet extends Controller implements ErrorHandler {

    @Resource(name = "jdbc/TechVibe")
    protected DataSource source;
    private OrdineDao<SQLException> ordineDao;

    public void init() throws ServletException {
        super.init();
        ordineDao = new SqlOrdineDao(source);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String path = request.getPathInfo() != null ? request.getPathInfo() : "/";
            switch (path) {
                case "/":
                    authorize(request.getSession(false));

                    // Rendi la validazione del page opzionale
                    String pageParam = request.getParameter("page");
                    if (pageParam != null && !pageParam.isEmpty()) {
                        validate(CommonValidator.validatePage(request));
                    }

                    int intPage = parsePage(request);
                    int size = ordineDao.countAll();
                    Paginator paginator = new Paginator(intPage, 10);
                    List<Ordine> ordini = ordineDao.fetchOrdini(paginator);
                    request.setAttribute("ordini", ordini);
                    request.setAttribute("pages", paginator.getPages(size));
                    request.getRequestDispatcher("/WEB-INF/views/crm/ordini.jsp").forward(request, response);
                    break;

                case "/show":
                    authorize(request.getSession(false));
                    showOrdine(request, response);
                    break;

                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (SQLException e) {
            notFound();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String path = request.getPathInfo() != null ? request.getPathInfo() : "/";

            switch (path) {
                case "/update":
                    authorize(request.getSession(false));
                    updateOrdine(request, response);
                    break;

                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            notFound();
        }
    }

    private void showOrdine(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID ordine mancante");
            return;
        }

        try {
            int ordineId = Integer.parseInt(idParam);

            // Usa il nuovo metodo che recupera tutto in una volta
            Optional<Ordine> optionalOrdine = ordineDao.fetchOrdineCompleto(ordineId);

            if (optionalOrdine.isPresent()) {
                Ordine ordine = optionalOrdine.get();
                request.setAttribute("ordine", ordine);
                request.getRequestDispatcher("/WEB-INF/views/crm/dettagli-ordini.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Ordine non trovato");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID ordine non valido");
        }
    }

    private void updateOrdine(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID ordine mancante");
            return;
        }

        try {
            int ordineId = Integer.parseInt(idParam);

            // Recupera l'ordine esistente
            Optional<Ordine> optionalOrdine = ordineDao.fetchOrdine(ordineId);
            if (!optionalOrdine.isPresent()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Ordine non trovato");
                return;
            }

            Ordine ordine = optionalOrdine.get();

            // Aggiorna solo i campi che possono essere modificati: stato e sconto
            String stato = request.getParameter("stato");
            if (stato != null && !stato.isEmpty()) {
                ordine.setStato(stato);
            }

            String scontoTotaleParam = request.getParameter("scontoTotale");
            if (scontoTotaleParam != null && !scontoTotaleParam.isEmpty()) {
                try {
                    double scontoTotale = Double.parseDouble(scontoTotaleParam);
                    ordine.setScontoTotale(scontoTotale);
                } catch (NumberFormatException e) {
                    // Ignora se il valore non è valido
                }
            }

            // I metodi di pagamento e spedizione NON vengono più aggiornati
            // sono campi read-only una volta creato l'ordine

            // Salva le modifiche
            boolean success = ordineDao.updateOrdine(ordine);

            if (success) {
                request.getSession().setAttribute("success", "Ordine aggiornato con successo");
            } else {
                request.getSession().setAttribute("error", "Errore durante l'aggiornamento dell'ordine");
            }

            response.sendRedirect(request.getContextPath() + "/ordini/show?id=" + ordineId);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID ordine non valido");
        }
    }
}