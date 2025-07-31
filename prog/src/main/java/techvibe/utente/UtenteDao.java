package techvibe.utente;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UtenteDao {

    //Per ogni operazione della tabella implementiamo un metodo equivalente, in lettura trasformiamo in oggetti account
    //Creazione, aggiornamento ed eliminazione dell'account e ad ognuno lanciamo SQLException

   List<Utente> fetchUtente(int start, int end) throws SQLException;
    Optional<Utente> fetchUtente(String email) throws SQLException;
    Integer createUtente(Utente utente)throws SQLException;
    Integer deleteAccount(String email) throws SQLException;
    Integer updateUtente (Utente utente)throws SQLException;


}
