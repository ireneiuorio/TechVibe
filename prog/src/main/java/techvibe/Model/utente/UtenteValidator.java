package techvibe.Model.utente;



import jakarta.servlet.http.HttpServletRequest;
import techvibe.http.RequestValidator;

public class UtenteValidator {

    //Prende la richiesta dell'utente, crea una validazione, controlla che l'email sia scritta in modo corretto e retsituisce il risultato della validazione
    public static RequestValidator validateSignin(HttpServletRequest request) {
        RequestValidator validator = new RequestValidator(request);
        validator.assertEmail("email", "L'email non è in un formato valido");
        return validator;
    }
}
