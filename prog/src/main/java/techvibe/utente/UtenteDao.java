package techvibe.utente;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UtenteDao <E extends Exception> {

    //Per ogni operazione della tabella implementiamo un metodo equivalente, in lettura trasformiamo in oggetti account
    //Creazione, aggiornamento ed eliminazione dell'account e ad ognuno lanciamo SQLException

   List<Utente> fetchUtenti(int start, int end) throws E;
    Optional<Utente> fetchUtente(int id) throws E;
    Boolean createUtente(Utente utente)throws E;
    Boolean deleteAccount(int id) throws E;
    Boolean updateUtente (Utente utente)throws E;


}
