package techvibe.Model.carrello;

import techvibe.Model.prodotto.Prodotto;
import techvibe.Model.prodotto.SqlProdottoDao;
import techvibe.Model.storage.QueryBuilder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqlCarrelloDao implements CarrelloDao {

    private final DataSource dataSource;
    private final SqlProdottoDao prodottoDao;

    public SqlCarrelloDao(DataSource dataSource, SqlProdottoDao prodottoDao) {
        this.dataSource = dataSource;
        this.prodottoDao = prodottoDao;
    }



    @Override
    public Optional<Carrello> getCarrelloByUtente(int idUtente) {
        QueryBuilder qb = new QueryBuilder("carrelli", "c");
        String sql = qb.select("id_carrello")
                .where("c.id_utente = ?")
                .generateQuery();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtente);
            try (ResultSet rs = ps.executeQuery()) {

                //se lo trova chiama caricaCarrello per ottenere l'oggetto Carrello completo
                if (rs.next()) {
                    int idCarrello = rs.getInt("id_carrello");
                    return Optional.of(caricaCarrello(idCarrello));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Carrello> getCarrelloBySessione(String sessionId) {
        QueryBuilder qb = new QueryBuilder("carrelli", "c");
        String sql = qb.select("id_carrello")
                .where("c.session_id = ? AND c.id_utente IS NULL")
                .generateQuery();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idCarrello = rs.getInt("id_carrello");
                    return Optional.of(caricaCarrello(idCarrello));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getCarrelloIdByUtente(int idUtente) {
        QueryBuilder qb = new QueryBuilder("carrelli", "c");
        String sql = qb.select("id_carrello")
                .where("c.id_utente = ?")
                .generateQuery();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtente);
            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(rs.getInt("id_carrello"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getCarrelloIdBySessione(String sessionId) {
        QueryBuilder qb = new QueryBuilder("carrelli", "c");
        String sql = qb.select("id_carrello")
                .where("c.session_id = ? AND c.id_utente IS NULL")
                .generateQuery();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                //se c'è una riga
                if (rs.next()) {
                    return Optional.of(rs.getInt("id_carrello"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    //Prende e inserisce tutti gli item all'interno di un carrello restiruiendo il carrello compelto
    private Carrello caricaCarrello(int idCarrello) {
        QueryBuilder qb = new QueryBuilder("carrello_items", "ci");
        String sql = qb.select("id_prodotto", "quantita")
                .where("ci.id_carrello = ?")
                .generateQuery();

        List<CarrelloItem> items = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCarrello);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idProdotto = rs.getInt("id_prodotto");
                    int quantita = rs.getInt("quantita");

                    Optional<Prodotto> prodotto = prodottoDao.fetchProdotto(idProdotto);
                    prodotto.ifPresent(p -> items.add(new CarrelloItem(p, quantita)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Carrello(items);
    }



    @Override
    public Optional<Integer> creaCarrelloUtente(int idUtente) {
        // Se c'è già un carrello èer quell'utente non fallire aggiorna solo la data
        String insert = new QueryBuilder("carrelli", null)
                .insert("id_utente")
                .generateQuery()
                + "ON DUPLICATE KEY UPDATE data_ultima_modifica = CURRENT_TIMESTAMP";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, idUtente);
            int res = ps.executeUpdate();


            //numero di righe aggiornate e modificate nel Db
            if (res > 0) {
                //prova a leggere la chiave generata
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) return Optional.of(rs.getInt(1));
                }
                // se no non ha generato chiavi, ritorna l'esistente
                return getCarrelloIdByUtente(idUtente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> creaCarrelloSessione(String sessionId) {
        String insert = new QueryBuilder("carrelli", null)
                .insert("session_id")
                .generateQuery();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, sessionId);
            //numero di righe aggiornate e modificate nel Db
            int res = ps.executeUpdate();
            if (res > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) return Optional.of(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    @Override
    public boolean aggiungiProdotto(int idCarrello, int idProdotto, int quantita) {
        // Upsert atomico
        String upsert = new QueryBuilder("carrello_items", null)
                .insert("id_carrello", "id_prodotto", "quantita")
                .generateQuery()
                + "ON DUPLICATE KEY UPDATE quantita = quantita + VALUES(quantita)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(upsert)) {

            ps.setInt(1, idCarrello);
            ps.setInt(2, idProdotto);
            ps.setInt(3, quantita);
            return ps.executeUpdate() > 0; //se il numero di righe modificate è maggiore di zero

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean rimuoviProdotto(int idCarrello, int idProdotto) {
        String sql = new QueryBuilder("carrello_items", null)
                .delete()
                .where("id_carrello = ? AND id_prodotto = ?")
                .generateQuery();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCarrello);
            ps.setInt(2, idProdotto);
            return ps.executeUpdate() > 0; //se il numero di righe modificate è maggiore di 0

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean aggiornaQuantita(int idCarrello, int idProdotto, int nuovaQuantita) {
        if (nuovaQuantita <= 0) {
            return rimuoviProdotto(idCarrello, idProdotto);
        }

        String sql = new QueryBuilder("carrello_items", null)
                .update("quantita")
                .where("id_carrello = ? AND id_prodotto = ?")
                .generateQuery();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, nuovaQuantita);
            ps.setInt(2, idCarrello);
            ps.setInt(3, idProdotto);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean svuotaCarrello(int idCarrello) {
        String sql = new QueryBuilder("carrello_items", null)
                .delete()
                .where("id_carrello = ?")
                .generateQuery();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCarrello);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    //serve a unire il carrello anonimo legato alla sessionId dentro il carrello dell’utente: idUtente quando fa login
    public boolean trasferisciCarrello(String sessionId, int idUtente) {
        try (Connection conn = dataSource.getConnection()) {

            conn.setAutoCommit(false); //Non salvare automaticamente ogni operazione nel DB aspetta che te lo dica con commit

            try {

                int sessionCartId = -1;
                { //trova il carrello anonimo dela sessione
                    String sql = new QueryBuilder("carrelli", "c")
                            .select("id_carrello")
                            .where("c.session_id = ? AND c.id_utente IS NULL")
                            .generateQuery();

                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, sessionId);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) sessionCartId = rs.getInt("id_carrello");
                        }
                    }
                }

                if (sessionCartId == -1) {
                    conn.rollback(); // niente da trasferire
                    return true;
                }

                // Carrello utente: cerca se un utente ha già un carrello
                Integer userCartId = null;
                {
                    String sql = new QueryBuilder("carrelli", "c")
                            .select("id_carrello")
                            .where("c.id_utente = ?")
                            .generateQuery();

                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, idUtente);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next())
                                //se esiste lo prende
                                userCartId = rs.getInt("id_carrello");
                        }
                    }
                }

                //lo crea se non lo tiene
                if (userCartId == null) {
                    String insertUserCart = new QueryBuilder("carrelli", null)
                            .insert("id_utente")
                            .generateQuery();
                    try (PreparedStatement ps = conn.prepareStatement(insertUserCart, Statement.RETURN_GENERATED_KEYS)) {
                        ps.setInt(1, idUtente);
                        ps.executeUpdate();
                        try (ResultSet rs = ps.getGeneratedKeys()) {
                            if (rs.next()) userCartId = rs.getInt(1);
                        }
                    }
                }
                //Se per qualsiasi motivo non riesce ad avere l’ID fallisce
                if (userCartId == null) {
                    conn.rollback();
                    return false;
                }


                //Fa l'insert select: prende tutti gli item del carrello di sessione e li inserisce nel carrello dell'utente
                String transferItemsSql = """
                        INSERT INTO carrello_items (id_carrello, id_prodotto, quantita)
                        SELECT ?, id_prodotto, quantita FROM carrello_items WHERE id_carrello = ?
                        ON DUPLICATE KEY UPDATE quantita = carrello_items.quantita + VALUES(quantita)
                        """;
                try (PreparedStatement ps = conn.prepareStatement(transferItemsSql)) {
                    ps.setInt(1, userCartId);
                    ps.setInt(2, sessionCartId);
                    ps.executeUpdate();
                }

                //Pulisce carrello di sessione
                String delItems = new QueryBuilder("carrello_items", null)
                        .delete().where("id_carrello = ?").generateQuery();
                try (PreparedStatement ps = conn.prepareStatement(delItems)) {
                    ps.setInt(1, sessionCartId);
                    ps.executeUpdate();
                }

                //Cancella anche dalla tabella carrelli
                String delCart = new QueryBuilder("carrelli", null)
                        .delete().where("id_carrello = ?").generateQuery();
                try (PreparedStatement ps = conn.prepareStatement(delCart)) {
                    ps.setInt(1, sessionCartId);
                    ps.executeUpdate();
                }

                //Se tutto è andato bene fa il commit dell'operazione
                conn.commit();
                return true;

            } catch (SQLException e) {
                //se c'è un'eccezione annulla tutto
                conn.rollback();
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
