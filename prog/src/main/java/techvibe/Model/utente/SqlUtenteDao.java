package techvibe.Model.utente;

import techvibe.Model.components.Paginator;
import techvibe.Model.storage.QueryBuilder;
import techvibe.Model.storage.SqlDao;

import javax.sql.DataSource;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqlUtenteDao extends SqlDao implements UtenteDao<SQLException> {

    public SqlUtenteDao(DataSource source) {
        super(source);
    }

    //RECUPERA LA LISTA PAGINATA DI TUTTI GL UTENTI
    public List<Utente> fetchUtenti(Paginator paginator) throws SQLException {
        try (Connection conn = source.getConnection()) {
            // se la tabella è "utente"
            QueryBuilder qb = new QueryBuilder("utente", "ute");

            String sql = qb
                    .select()      // SELECT * FROM utente AS ute
                    .limit(true)   // ... LIMIT ?, ?
                    .generateQuery();

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, paginator.getOffset()); //Da quale utente partire
                ps.setInt(2, paginator.getLimit());//Quanti utenti prendere

                try (ResultSet rs = ps.executeQuery()) {
                    UtenteExtractor extractor = new UtenteExtractor();
                    List<Utente> utenti = new ArrayList<>();
                    while (rs.next()) {//Vai il prossima riga
                        utenti.add(extractor.extract(rs));
                    }
                    return utenti;
                }
            }
        }
    }


    //Recupera un utente
    @Override
    public Optional<Utente> fetchUtente(int id) throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("utente", "ute");
            String query = queryBuilder.select().where("ute.idaccount=?").generateQuery();
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, id);
                ResultSet set = ps.executeQuery();
                Utente utente = null;
                if (set.next()) {
                    utente = new UtenteExtractor().extract(set);
                }
                return Optional.ofNullable(utente);
            }
        }
    }

    //Crea un utente
    @Override
    public Boolean createUtente(Utente utente) throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("utente", "ute");
            queryBuilder.insert("nome", "cognome", "email", "passwordhash", "telefono", "indirizzospedizione", "isadmin", "stato");
            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
                ps.setString(1, utente.getNome());
                ps.setString(2, utente.getCognome());
                ps.setString(3, utente.getEmail());
                ps.setString(4, utente.getPassword());
                ps.setString(5, utente.getTelefono());
                ps.setString(6, utente.getIndirizzoSpedizione());
                ps.setBoolean(7, utente.isAdmin());
                ps.setString(8, utente.getStato());

                int rows = ps.executeUpdate();
                return rows == 1;
            }
        }
    }


    //Aggiornamento Utente
    @Override
    public Boolean updateUtente(Utente utente) throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("utente", "ute");
            queryBuilder.update("nome", "cognome", "email", "telefono", "indirizzospedizione").where("idaccount=?");
            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
                ps.setString(1, utente.getNome());
                ps.setString(2, utente.getCognome());
                ps.setString(3, utente.getEmail());
                ps.setString(4, utente.getTelefono());
                ps.setString(5, utente.getIndirizzoSpedizione());
                ps.setInt(6, utente.getIdUtente());

                int rows = ps.executeUpdate();
                return rows == 1;
            }
        }
    }

    //Cancella utente con tutte le relazioni
    public boolean deleteAccountWithRelations(int userId) throws SQLException {
        try (Connection c = source.getConnection()) {
            try {
                c.setAutoCommit(false);
                // 1) Cancella carrello_items via join carrelli
                try (PreparedStatement ps = c.prepareStatement(
                        "DELETE ci FROM carrello_items ci " +
                                "JOIN carrelli ca ON ca.id_carrello = ci.id_carrello " +
                                "WHERE ca.id_utente = ?")) {
                    ps.setInt(1, userId);
                    ps.executeUpdate();
                }
                // 2) Cancella carrelli
                try (PreparedStatement ps = c.prepareStatement(
                        "DELETE FROM carrelli WHERE id_utente = ?")) {
                    ps.setInt(1, userId);
                    ps.executeUpdate();
                }
                // 3) (Scelta) ordini: SET NULL o cancellali se le regole lo consentono
                // 4) Cancella utente
                try (PreparedStatement ps = c.prepareStatement(
                        "DELETE FROM utente WHERE idaccount = ?")) {
                    ps.setInt(1, userId);
                    int rows = ps.executeUpdate();
                    c.commit();
                    return rows == 1;
                }
            } catch (SQLException ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }


    // Cambia stato utente
    public Boolean cambiaStatoUtente(int idUtente, String nuovoStato) throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("utente", "ute");
            queryBuilder.update("stato").where("idaccount=?");
            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
                ps.setString(1, nuovoStato);
                ps.setInt(2, idUtente);

                int rows = ps.executeUpdate();
                return rows == 1;
            }
        }
    }

    // Attiva utente
    public Boolean attivaUtente(int idUtente) throws SQLException {
        return cambiaStatoUtente(idUtente, "ATTIVO");
    }

    //Disattiva utente
    public Boolean disattivaUtente(int idUtente) throws SQLException {
        return cambiaStatoUtente(idUtente, "DISATTIVATO");
    }

    //Trova utente
    @Override
    public Optional<Utente> findUtente(String email, String passwordHash, boolean admin) throws SQLException {
        String sql = new QueryBuilder("utente", "ute")
                .select()
                .where("ute.email = ? AND ute.passwordhash = ? AND ute.isadmin = ? AND ute.stato = 'ATTIVO'")
                .generateQuery();

        try (Connection c = source.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // 2) BIND: usa l'hash già calcolato dalla servlet
            ps.setString(1, email);
            ps.setString(2, passwordHash);
            ps.setBoolean(3, admin);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Utente u = new UtenteExtractor().extract(rs); // la password NON si setta nell'extractor
                    return Optional.of(u);
                }
                return Optional.empty();
            }
        }
    }

    //Verifica se l'utente è disattivato
    public boolean isUtenteDisattivato(String email, String passwordHash, boolean admin) throws SQLException {
        String sql = new QueryBuilder("utente", "ute")
                .select()
                .where("ute.email = ? AND ute.passwordhash = ? AND ute.isadmin = ? AND ute.stato = 'DISATTIVATO'")
                .generateQuery();

        try (Connection c = source.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, passwordHash);
            ps.setBoolean(3, admin);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true se esiste un utente disattivato
            }
        }
    }

    //Conta tutti
    public int countAll() throws SQLException {
        final String sql = "SELECT COUNT(*) AS total FROM utente ute";
        try (Connection c = source.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt("total") : 0;
        }
    }



    //Verifica se esiste un utente con una determibara mail
    public boolean existsByEmail(String email) throws SQLException {
        final String sql = "SELECT 1 FROM utente WHERE Email = ? LIMIT 1";
        try (Connection conn = source.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true se esiste almeno una riga
            }
        }
    }

   //Aggiorna la password
    public Boolean updatePassword(int idUtente, String newPassword) throws SQLException, NoSuchAlgorithmException, NoSuchAlgorithmException {
        // Usa il metodo della classe Utente per fare l'hash
        Utente tempUtente = new Utente();
        tempUtente.setPassword(newPassword); // Questo fa automaticamente l'hash
        String hashedPassword = tempUtente.getPassword();

        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("utente", "ute");
            queryBuilder.update("passwordhash").where("idaccount=?");
            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
                ps.setString(1, hashedPassword);
                ps.setInt(2, idUtente);

                int rows = ps.executeUpdate();
                return rows == 1;
            }
        }
    }

   //Aggiorno solo l'email
    public Boolean updateEmail(int idUtente, String newEmail) throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("utente", "ute");
            queryBuilder.update("email").where("idaccount=?");
            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
                ps.setString(1, newEmail);
                ps.setInt(2, idUtente);

                int rows = ps.executeUpdate();
                return rows == 1;
            }
        }
    }
}