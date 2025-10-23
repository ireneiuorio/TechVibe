package techvibe.Model.prodotto;

import jakarta.servlet.http.HttpServletRequest;
import techvibe.http.RequestValidator;

public class ProdottoValidator {

    static RequestValidator validateForm(HttpServletRequest request)
    {
        RequestValidator validator=new RequestValidator(request);
        validator.assertDouble("Prezzo", "Prezzo deve essere un numero con la virgola");
        validator.assertInt("QtDisponibile ", "QtDisponibile  deve essere un intero");
        validator.assertInt("StorageDispositivo", "  StorageDispositivo deve essere un intero");
        validator.assertInt("Ram", "Ram deve essere un intero");
        validator.assertInt("IdCategoria", "IdCategoria deve essere un intero");

        return  validator;




    }
}
