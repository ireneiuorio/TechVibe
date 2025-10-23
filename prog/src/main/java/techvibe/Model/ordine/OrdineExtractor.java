package techvibe.Model.ordine;

import techvibe.Model.storage.ResultSetExtractor;
import techvibe.Model.utente.Utente;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrdineExtractor implements ResultSetExtractor<Ordine> {

    @Override
    public Ordine extract(ResultSet rs) throws SQLException {
        Ordine ordine = new Ordine();

        // Dati dell'ordine
        ordine.setIdOrdine(rs.getInt("ord.idordine"));
        ordine.setStato(rs.getString("ord.stato"));
        ordine.setTotale(rs.getDouble("ord.totale"));
        ordine.setScontoTotale(rs.getDouble("ord.scontototale"));
        ordine.setMetodoDiSpedizione(rs.getString("ord.metodospedizione"));
        ordine.setMetodoDiPagamento(rs.getString("ord.metodopagamento"));

        // AGGIUNGI: Dati dell'utente (se presenti nella query)
        try {
            Utente utente = new Utente();
            utente.setIdUtente(rs.getInt("ute.IdAccount")); // Usa il nome corretto della colonna

            ordine.setUtente(utente);
        } catch (SQLException e) {
            // Se i campi utente non sono presenti nella query, ignora
            ordine.setUtente(null);
        }

        return ordine;
    }
}