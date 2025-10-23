package techvibe.http;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import techvibe.Model.utente.UtenteSession;

import java.util.List;

public interface ErrorHandler {

    default void authenticate(HttpSession session) throws InvalidRequestException{
        if(session==null || session.getAttribute("accountSession")==null)
        {
            throw new InvalidRequestException("Errore autenticazione", List.of("Non sei autenticato"), HttpServletResponse.SC_UNAUTHORIZED);

        }
    }

    default void authorize(HttpSession session)throws InvalidRequestException{
        authenticate(session);
        UtenteSession utenteSession=(UtenteSession) session.getAttribute("utenteSession");
        if(!utenteSession.isAdmin())
        {
            throw new InvalidRequestException("Errore autenticazione", List.of("Azione non consentita"), HttpServletResponse.SC_FORBIDDEN);
        }

    }

    default void internalError()throws InvalidRequestException {
        List<String> errors=List.of("Un errore imprevisto è avvento","Riprova più tardi");
        throw new InvalidRequestException("Errore interno",errors,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    default void notFound()throws InvalidRequestException{
        throw new InvalidRequestException("Errore interno",List.of("Risorsa non trovata"),HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

    }


    default void notAllowed() throws InvalidRequestException{
        throw new InvalidRequestException("Operazione non consentita", List.of("Operazione non permessa"),HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }









}
