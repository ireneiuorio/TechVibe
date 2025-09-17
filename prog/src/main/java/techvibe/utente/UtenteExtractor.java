package techvibe.utente;

import techvibe.storage.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteExtractor implements ResultSetExtractor<Utente> {

    public Utente extract(ResultSet resultSet) throws SQLException
    {
        Utente u = new Utente();
        u.setIdUtente(resultSet.getInt("ute.idaccount"));
        u.setNome(resultSet.getString("ute.nome"));
        u.setCognome(resultSet.getString("ute.cognome"));
        u.setEmail(resultSet.getString("ute.email"));
        u.setTelefono(resultSet.getString("ute.telefono"));
        u.setIndirizzoSpedizione(resultSet.getString("ute.indirizzospedizione"));
        u.setAdmin(resultSet.getBoolean("ute.isadmin"));

        // Gestione del campo stato con fallback per compatibilità
        try {
            String stato = resultSet.getString("ute.stato");
            if (stato != null) {
                u.setStato(stato);
            } else {
                u.setStato("ATTIVO"); // Default se il campo è null
            }
        } catch (SQLException e) {
            // Se la colonna non esiste ancora nel DB, usa il default
            u.setStato("ATTIVO");
        }

        return u;
    }
}