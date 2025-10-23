package techvibe.Model.utente;

import techvibe.Model.components.Paginator;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UtenteDao <E extends Exception> {
   List<Utente> fetchUtenti(Paginator paginator) throws E;
 Optional<Utente> fetchUtente(int id) throws E;
    Boolean createUtente(Utente utente)throws E;
    Boolean updateUtente (Utente utente)throws E;
    Optional<Utente> findUtente(String email, String password, boolean b) throws E;
    int countAll() throws E;
    boolean deleteAccountWithRelations(int userId) throws SQLException;
    boolean existsByEmail(String email) throws E;


}
