package techvibe.utente;

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

    //senza le relazioni, solo dati di questa tabella
    @Override
    public List<Utente> fetchUtenti(int start, int end) throws SQLException {
        try (Connection conn = source.getConnection()) { //source si occupa di darmi la connessione in maniera safe
            QueryBuilder queryBuilder = new QueryBuilder("account", "acc");
            String query = queryBuilder.select().limit(true).generateQuery();
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, start);
                ps.setInt(2, end);

                ResultSet set = ps.executeQuery();
                UtenteExtractor utenteExtractor = new UtenteExtractor();
                List<Utente> utenti = new ArrayList<>();
                while (set.next()) {
                    utenti.add(utenteExtractor.extract(set));
                }

                return utenti;
            }
        }
    }

    @Override
    public Optional<Utente> fetchUtente(int id) throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("utente", "ute");
            String query = queryBuilder.select().where("ute.id=?").generateQuery();
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
            queryBuilder.insert("nome", "cognome", "email", "password", "telefono", "indirizzospedizione", "admin");
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
        try(Connection conn=source.getConnection()){
            QueryBuilder queryBuilder=new QueryBuilder("utente","ute");
            queryBuilder.delete().where("id=?");
            try(PreparedStatement ps=conn.prepareStatement(queryBuilder.generateQuery()))
            {
                ps.setInt(1,id);
                int rows=ps.executeUpdate();
                return rows==1;

            }

        }
    }

    @Override
    public Boolean updateUtente(Utente utente) throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("utente", "ute");
            queryBuilder.update("nome", "cognome").where("id=?");
            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
                ps.setString(1, utente.getNome());
                ps.setString(2, utente.getCognome());
                ps.setInt(3, utente.getIdUtente());

                int rows = ps.executeUpdate();
                return rows == 1;

            }
        }
    }
}