package techvibe.http;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import techvibe.Model.utente.UtenteSession;

import java.util.List;


//Interfaccia che centralizza la gestione degli errori e dei controlli di autenticazione
public interface ErrorHandler {


    //Controlla se l'utente è autenticato
    default void authenticate(HttpSession session) throws InvalidRequestException{
        if(session==null || session.getAttribute("accountSession")==null)
        {
            throw new InvalidRequestException("Errore autenticazione", List.of("Non sei autenticato"), HttpServletResponse.SC_UNAUTHORIZED);

        }
    }

    //Controlla se l'utente è un admin autenticato
    default void authorize(HttpSession session)throws InvalidRequestException{
        authenticate(session);
        UtenteSession utenteSession=(UtenteSession) session.getAttribute("utenteSession");
        if(!utenteSession.isAdmin())
        {
            throw new InvalidRequestException("Errore autenticazione", List.of("Azione non consentita"), HttpServletResponse.SC_FORBIDDEN);
        }

    }

    //Serve per gestire errori imprevisti o eccezioni interne.
    default void internalError()throws InvalidRequestException {
        List<String> errors=List.of("Un errore imprevisto è avvento","Riprova più tardi");
        throw new InvalidRequestException("Errore interno",errors,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }


    //Gestisce una risorsa non trovata
    default void notFound()throws InvalidRequestException{
        throw new InvalidRequestException("Errore interno",List.of("Risorsa non trovata"),HttpServletResponse.SC_NOT_FOUND);

    }


    //Gestisce i casi di operazione non permessa
    default void notAllowed() throws InvalidRequestException{
        throw new InvalidRequestException("Operazione non consentita", List.of("Operazione non permessa"),HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }









}
