package techvibe.utente;



import jakarta.servlet.http.HttpServletRequest;
import techvibe.http.RequestValidator;

public class UtenteValidator {

    public static RequestValidator validateSignin(HttpServletRequest request) {


        RequestValidator validator = new RequestValidator(request);
        validator.assertEmail("email", "L'email non è in un formato valido");



        return validator;
    }
}
