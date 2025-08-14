package techvibe.http;


import com.sun.jdi.request.InvalidRequestStateException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import techvibe.carrello.Carrello;
import techvibe.utente.UtenteSession;


import javax.naming.directory.InvalidAttributeIdentifierException;
import javax.print.DocFlavor;
import javax.sql.DataSource;
import java.io.File;
import java.util.Map;

public abstract class Controller extends HttpServlet {

    @Resource(name ="jdbc/TechVibe")

    protected DataSource source;


    protected void validate(RequestValidator validator) throws InvalidRequestException{
        if(validator.hasErrors())
        {
            throw new InvalidRequestException("Validation Error", validator.getErrors(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
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


   protected UtenteSession getUtenteSession(HttpSession session)
   {
       return (UtenteSession) session.getAttribute("utenteSession");
   }

   protected Carrello  getSessionCarrello(HttpSession session)
   {
       return(Carrello) session.getAttribute("utenteCarrello");
   }
}
