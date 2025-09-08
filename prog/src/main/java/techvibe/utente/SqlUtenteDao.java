package techvibe.utente;

import org.apache.taglibs.standard.tag.el.sql.QueryTag;
import techvibe.components.Paginator;
import techvibe.prodotto.ProdottoExtractor;
import techvibe.storage.QueryBuilder;
import techvibe.storage.ResultSetExtractor;
import techvibe.storage.SqlDao;
import techvibe.storage.TableQuery;

import javax.sql.DataSource;
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

    // nuova firma
    public List<Utente> fetchUtenti(Paginator paginator) throws SQLException {
        try (Connection conn = source.getConnection()) {
            // se la tabella è "utente"
            QueryBuilder qb = new QueryBuilder("utente", "ute");

            String sql = qb
                    .select()      // SELECT * FROM utente AS ute
                    .limit(true)   // ... LIMIT ?, ?
                    .generateQuery();

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                // In MySQL/MariaDB: LIMIT offset, row_count
                ps.setInt(1, paginator.getOffset());
                ps.setInt(2, paginator.getLimit());

                try (ResultSet rs = ps.executeQuery()) {
                    UtenteExtractor extractor = new UtenteExtractor();
                    List<Utente> utenti = new ArrayList<>();
                    while (rs.next()) {
                        utenti.add(extractor.extract(rs));
                    }
                    return utenti;
                }
            }
        }
    }


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


    @Override
    public Boolean createUtente(Utente utente) throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("utente", "ute");
            queryBuilder.insert("nome", "cognome", "email", "passwordhash", "telefono", "indirizzospedizione", "isadmin");
            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
                ps.setString(1, utente.getNome());
                ps.setString(2, utente.getCognome());
                ps.setString(3, utente.getEmail());
                ps.setString(4, utente.getPassword());
                ps.setString(5, utente.getTelefono());
                ps.setString(6, utente.getIndirizzoSpedizione());
                ps.setBoolean(7, utente.isAdmin());

                int rows = ps.executeUpdate();
                return rows == 1;
            }
        }
    }

    @Override
    public Boolean deleteAccount(int id) throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("utente", "ute");
            queryBuilder.delete().where("idaccount=?");
            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
                ps.setInt(1, id);
                int rows = ps.executeUpdate();
                return rows == 1;

            }

        }
    }

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

    @Override
    public Optional<Utente> findUtente(String email, String passwordHash, boolean admin) throws SQLException {
        String sql = new QueryBuilder("utente", "ute")
                .select()
                .where("ute.email = ? AND ute.passwordhash = ? AND ute.isadmin = ?")
                .generateQuery();

        try (Connection c = source.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // 2) BIND: usa l'hash già calcolato dalla servlet
            ps.setString(1, email);
            ps.setString(2, passwordHash);
            ps.setBoolean(3, admin); //

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Utente u = new UtenteExtractor().extract(rs); // la password NON si setta nell'extractor
                    return Optional.of(u);
                }
                return Optional.empty();
            }
        }






}
    public int countAll() throws SQLException {
        final String sql = "SELECT COUNT(*) AS total FROM utente ute";
        try (Connection c = source.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt("total") : 0;
        }
    }


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

    public Optional<Utente> findUtenteNormale(String email, String passwordHash) throws SQLException {
        String sql = new QueryBuilder("utente", "ute")
                .select()
                .where("ute.email = ? AND ute.passwordhash = ? AND ute.isadmin = false")
                .generateQuery();

        try (Connection c = source.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, passwordHash);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Utente u = new UtenteExtractor().extract(rs);
                    return Optional.of(u);
                }
                return Optional.empty();
            }
        }
    }




}