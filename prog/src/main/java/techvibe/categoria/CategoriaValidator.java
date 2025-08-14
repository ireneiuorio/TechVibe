package techvibe.categoria;

import jakarta.servlet.http.HttpServletRequest;
import techvibe.http.RequestValidator;

import java.util.regex.Pattern;

public class CategoriaValidator {

    static RequestValidator validatorForm(HttpServletRequest request,boolean update){
        RequestValidator validator=new RequestValidator(request);
        validator.assertMatch("label", Pattern.compile("^\\w{4,20}$"),"Il nome deve avere lunghezza tra 4 e 20 caratteri");
        if(update)
        {
            validator.assertInt("id","Id deve essere un numero intero");

        }
        return validator;
    }
}
