package techvibe.utente;

import techvibe.storage.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteExtractor implements ResultSetExtractor<Utente> {

    public Utente extract(ResultSet resultSet) throws SQLException
    {
        Utente u=new Utente();
        u.setIdUtente(resultSet.getInt("ute.id"));
        u.setNome(resultSet.getString("ute.nome"));
        u.setCognome(resultSet.getString("ute.cognome"));
        u.setEmail(resultSet.getString("ute.email"));
        u.setPassword(resultSet.getString("ute.password"));
        u.setTelefono(resultSet.getString("ute.telefono"));
        u.setIndirizzoSpedizione(resultSet.getString("ute.indirizzospedizione"));
        u.setAdmin(resultSet.getBoolean("ute.admin"));

        return u;


    }
}
