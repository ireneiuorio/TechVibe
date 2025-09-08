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
        List<Condition> conditions = new ArrayList<>();
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String param = parameterNames.nextElement();
            String value = request.getParameter(param);

            if (value != null && !value.trim().isEmpty()) {
                switch (param) {
                    case "Marca":
                        conditions.add(new Condition("marca", Operator.MATCH, value.trim()));
                        break;
                    case "Modello":
                        conditions.add(new Condition("modello", Operator.MATCH, value.trim()));
                        break;
                    case "SistemaOperativo":
                        conditions.add(new Condition("sistemaoperativo", Operator.MATCH, value.trim()));
                        break;
                    case "minPrice":
                        try {
                            double price = Double.parseDouble(value.trim());
                            conditions.add(new Condition("prezzo", Operator.GE, String.valueOf(price)));
                        } catch (Exception e) {
                            // Ignora valori non numerici
                        }
                        break;
                    case "maxPrice":
                        try {
                            double price = Double.parseDouble(value.trim());
                            conditions.add(new Condition("prezzo", Operator.LE, String.valueOf(price)));
                        } catch (Exception e) {
                            // Ignora valori non numerici
                        }
                        break;
                    case "Ram":
                        try {
                            int ram = Integer.parseInt(value.trim());
                            conditions.add(new Condition("ram", Operator.GE, String.valueOf(ram)));
                        } catch (Exception e) {
                            // Ignora valori non numerici
                        }
                        break;
                    case "StorageDispositivo":
                        try {
                            int storage = Integer.parseInt(value.trim());
                            conditions.add(new Condition("storagedispositivo", Operator.GE, String.valueOf(storage)));
                        } catch (Exception e) {
                            // Ignora valori non numerici
                        }
                        break;
                    case "Colore":
                        conditions.add(new Condition("colore", Operator.MATCH, value.trim()));
                        break;
                    case "IdCategoria":
                        conditions.add(new Condition("idcategoria", Operator.EQ, value.trim()));
                        break;
                }
            }
        }

        return conditions;
    }
}