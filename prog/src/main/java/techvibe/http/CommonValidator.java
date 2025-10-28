package techvibe.http;

import jakarta.servlet.http.HttpServletRequest;

public class CommonValidator {

    //Controlla che il parametro page sia un intero
    public static RequestValidator validatePage(HttpServletRequest request)
    {
        RequestValidator validator=new RequestValidator(request);
        validator.assertInt("page","Il numero di pagina deve essere in un formato valido");
        return validator;

    }

    //Controlla che il parametro id sia un intero

    public static RequestValidator validateId(HttpServletRequest request)
    {
        RequestValidator validator=new RequestValidator(request);
        validator.assertInt("id","Id deve essere in un formato valido");
        return validator;
    }
}
