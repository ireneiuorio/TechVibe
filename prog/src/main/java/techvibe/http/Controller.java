package techvibe.http;


import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import techvibe.Model.carrello.Carrello;
import techvibe.Model.utente.UtenteSession;


import javax.sql.DataSource;

//astratta
public abstract class Controller extends HttpServlet {

    @Resource(name ="jdbc/TechVibe")

    protected DataSource source; //inietta la connessione al database


    protected void validate(RequestValidator validator) throws InvalidRequestException{
        if(validator.hasErrors())
        {
            throw new InvalidRequestException("Validation Error", validator.getErrors(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    //se il parametro di paginazione non c'è restituisce 1
    public static int parsePage(HttpServletRequest request) {
        String raw = request.getParameter("page");
        int page = 1; // default

        if (raw != null && !raw.isBlank()) {
            try {
                page = Integer.parseInt(raw);
            } catch (NumberFormatException ignored) {
                page = 1; // fallback
            }
        }
        return (page < 1) ? 1 : page;
    }


}
