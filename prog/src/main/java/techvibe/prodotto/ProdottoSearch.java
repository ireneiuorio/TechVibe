package techvibe.prodotto;

import jakarta.servlet.http.HttpServletRequest;
import techvibe.http.SearchBuilder;
import techvibe.search.Condition;
import techvibe.search.Operator;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ProdottoSearch implements SearchBuilder {



    @Override
    public List<Condition> buildSearch(HttpServletRequest request) {
        List<Condition> conditions =new ArrayList<>();
        Enumeration<String> parameterNames=request.getParameterNames();
        while(parameterNames.hasMoreElements())
        {
            String param=parameterNames.nextElement();
            String value=request.getParameter(param);
            if(value!=null && !value.isBlank()) //anche se non passi un parametro potrebbe passare per questo si scartano (opzionali non compilati)
            {
                switch(param)
                {
                    case "Modello":
                        conditions.add(new Condition("Modello", Operator.MATCH,value));
                        break;
                    case "Marca":
                        conditions.add(new Condition("Marca", Operator.MATCH,value));
                        break;
                    case "IdCategoria":
                        conditions.add(new Condition("IdCategoria", Operator.EQ,value));
                        break;
                    case "minPrice":
                        conditions.add(new Condition("Prezzo", Operator.GT,value));
                        break;
                    case "maxPrice":
                        conditions.add(new Condition("Prezzo", Operator.LT,value));
                        break;
                    case "SistemaOperativo":
                        conditions.add(new Condition("SistemaOperativo", Operator.MATCH,value));
                        break;
                    case "Ram":
                        conditions.add(new Condition("Ram", Operator.GT,value));
                        break;
                    case "StorageDispositivo":
                        conditions.add(new Condition("StorageDispositivo", Operator.GT,value));
                        break;
                    case "Colore":
                        conditions.add(new Condition("Colore", Operator.MATCH,value));
                        break;


                }
            }
        }
        return conditions;
    }

}
