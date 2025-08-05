package techvibe.ordine;

import techvibe.storage.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrdineExtractor implements ResultSetExtractor<Ordine> {


    public Ordine extract(ResultSet resultSet) throws SQLException{
        Ordine ordine =new Ordine();
        ordine.setIdOrdine(resultSet.getInt("ord.id"));
        ordine.setStato(resultSet.getString("ord.stato"));
        ordine.setTotale(resultSet.getDouble("ord.totale"));
        ordine.setScontoTotale(resultSet.getDouble("ord.scontototale"));
        ordine.setMetodoDiSpedizione(resultSet.getString("ord.metododispedizione"));
        ordine.setMetodoDiPagamento(resultSet.getString("ord.metododipagamamento"));


        return ordine;
    }
}
