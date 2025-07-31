package techvibe.utente;

import techvibe.prodotto.Prodotto;
import techvibe.storage.Manager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UtenteManager extends Manager implements UtenteDao {

    private static final UtentiQuery QUERY=new UtentiQuery("utente");
    public UtenteManager(DataSource source) {
        super(source);
    }

    @Override
    public List<Utente> fetchUtente(int start, int end) throws SQLException {
      try(Connection conn= source.getConnection()){ //source si occupa di darmi la connessione in maniera safe
          try(PreparedStatement ps=conn.prepareStatement(QUERY.selectUtente()))
          {
              ps.setInt(1,start);  //primo offset secondo limit
              ps.setInt(2,end);
              ResultSet set=ps.executeQuery();
              List<Utente> utenti= new ArrayList<>();
              //mapping
              while (set.next())//per iterare sulle singole righe
              {
                 Utente utente =new Utente();
                 utente.setEmail(set.getString("Email"));
                 utente.setPassword(set.getString("Password"));
                 utente.setCognome(set.getString("Cognome"));
                 utente.setNome(set.getString("Nome"));
                 utente.setTelefono(set.getString("Telefono"));
                 utente.setAdmin(set.getBoolean("Admin"));
                 utente.setIndirizzoSpedizione("IndirizzoSpedizione");
                 utenti.add(utente);
              }
              set.close();
              return utenti;
          }

      }
    }

    @Override
    public Optional<Utente> fetchUtente(String email) throws SQLException {
        return Optional.empty();
    }

    @Override
    public Integer createUtente(Utente utente) throws SQLException {
        return 0;
    }

    @Override
    public Integer deleteAccount(String email) throws SQLException {
        return 0;
    }

    @Override
    public Integer updateUtente(Utente utente) throws SQLException {
        return 0;
    }
}
