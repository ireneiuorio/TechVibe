package techvibe.carrello;

import techvibe.prodotto.Prodotto;
import techvibe.prodotto.SqlProdottoDao;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarrelloDao {

    private final DataSource dataSource;
    private final SqlProdottoDao prodottoDao;

    public CarrelloDao(DataSource dataSource, SqlProdottoDao prodottoDao) {
        this.dataSource = dataSource;
        this.prodottoDao = prodottoDao;
    }

    /**
     * Recupera il carrello per utente loggato
     */
    public Optional<Carrello> getCarrelloByUtente(int idUtente) {
        String sql = "SELECT id_carrello FROM carrelli WHERE id_utente = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtente);
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

    /**
     * Recupera il carrello per sessione anonima
     */
    public Optional<Carrello> getCarrelloBySessione(String sessionId) {
        String sql = "SELECT id_carrello FROM carrelli WHERE session_id = ? AND id_utente IS NULL";

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

    /**
     * Crea un nuovo carrello per utente loggato
     */
    public Optional<Integer> creaCarrelloUtente(int idUtente) {
        String sql = "INSERT INTO carrelli (id_utente) VALUES (?) ON DUPLICATE KEY UPDATE data_ultima_modifica = CURRENT_TIMESTAMP";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, idUtente);
            int affected = ps.executeUpdate();

            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return Optional.of(rs.getInt(1));
                    }
                }
                // Se non ha generato keys, cerca l'ID esistente
                return getCarrelloIdByUtente(idUtente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Crea un nuovo carrello per sessione anonima
     */
    public Optional<Integer> creaCarrelloSessione(String sessionId) {
        String sql = "INSERT INTO carrelli (session_id) VALUES (?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, sessionId);
            int affected = ps.executeUpdate();

            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return Optional.of(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Integer> getCarrelloIdByUtente(int idUtente) {
        String sql = "SELECT id_carrello FROM carrelli WHERE id_utente = ?";
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

    /**
     * Carica tutti gli items di un carrello
     */
    private Carrello caricaCarrello(int idCarrello) {
        String sql = "SELECT id_prodotto, quantita FROM carrello_items WHERE id_carrello = ?";
        List<CarrelloItem> items = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCarrello);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idProdotto = rs.getInt("id_prodotto");
                    int quantita = rs.getInt("quantita");

                    Optional<Prodotto> prodotto = prodottoDao.fetchProdotto(idProdotto);
                    if (prodotto.isPresent()) {
                        items.add(new CarrelloItem(prodotto.get(), quantita));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Carrello(items);
    }

    /**
     * Aggiunge o aggiorna un prodotto nel carrello
     */
    public boolean aggiungiProdotto(int idCarrello, int idProdotto, int quantita) {
        String checkSql = "SELECT quantita FROM carrello_items WHERE id_carrello = ? AND id_prodotto = ?";
        String insertSql = "INSERT INTO carrello_items (id_carrello, id_prodotto, quantita) VALUES (?, ?, ?)";
        String updateSql = "UPDATE carrello_items SET quantita = quantita + ? WHERE id_carrello = ? AND id_prodotto = ?";

        try (Connection conn = dataSource.getConnection()) {
            // Verifica se il prodotto è già nel carrello
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setInt(1, idCarrello);
                checkPs.setInt(2, idProdotto);

                try (ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next()) {
                        // Prodotto già presente, aggiorna quantità
                        try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                            updatePs.setInt(1, quantita);
                            updatePs.setInt(2, idCarrello);
                            updatePs.setInt(3, idProdotto);
                            return updatePs.executeUpdate() > 0;
                        }
                    } else {
                        // Prodotto non presente, inserisci nuovo
                        try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                            insertPs.setInt(1, idCarrello);
                            insertPs.setInt(2, idProdotto);
                            insertPs.setInt(3, quantita);
                            return insertPs.executeUpdate() > 0;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Rimuove un prodotto dal carrello
     */
    public boolean rimuoviProdotto(int idCarrello, int idProdotto) {
        String sql = "DELETE FROM carrello_items WHERE id_carrello = ? AND id_prodotto = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCarrello);
            ps.setInt(2, idProdotto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Aggiorna la quantità di un prodotto
     */
    public boolean aggiornaQuantita(int idCarrello, int idProdotto, int nuovaQuantita) {
        if (nuovaQuantita <= 0) {
            return rimuoviProdotto(idCarrello, idProdotto);
        }

        String sql = "UPDATE carrello_items SET quantita = ? WHERE id_carrello = ? AND id_prodotto = ?";

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

    /**
     * Svuota completamente un carrello
     */
    public boolean svuotaCarrello(int idCarrello) {
        String sql = "DELETE FROM carrello_items WHERE id_carrello = ?";

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

    /**
     * Trasferisce il carrello da sessione a utente loggato
     */
    public boolean trasferisciCarrello(String sessionId, int idUtente) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            try {
                System.out.println("=== DEBUG TRASFERIMENTO DB ===");
                System.out.println("Session ID: " + sessionId);
                System.out.println("User ID: " + idUtente);

                // 1. Trova il carrello della sessione
                String findSessionCartSql = "SELECT id_carrello FROM carrelli WHERE session_id = ? AND id_utente IS NULL";
                int sessionCartId = -1;

                try (PreparedStatement ps1 = conn.prepareStatement(findSessionCartSql)) {
                    ps1.setString(1, sessionId);
                    try (ResultSet rs = ps1.executeQuery()) {
                        if (rs.next()) {
                            sessionCartId = rs.getInt("id_carrello");
                            System.out.println("Trovato carrello sessione ID: " + sessionCartId);
                        } else {
                            System.out.println("Nessun carrello trovato per session: " + sessionId);
                        }
                    }
                }

                if (sessionCartId == -1) {
                    System.out.println("Nessun carrello sessione da trasferire");
                    conn.rollback();
                    return true; // Non è un errore, semplicemente non c'è nulla da trasferire
                }

                // 1.5 Conta gli items nel carrello sessione
                String countItemsSql = "SELECT COUNT(*) FROM carrello_items WHERE id_carrello = ?";
                int itemsCount = 0;
                try (PreparedStatement ps = conn.prepareStatement(countItemsSql)) {
                    ps.setInt(1, sessionCartId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            itemsCount = rs.getInt(1);
                            System.out.println("Items nel carrello sessione: " + itemsCount);
                        }
                    }
                }

                // 2. Trova o crea carrello utente
                Integer userCartId = null;

                // Prima prova a trovare un carrello esistente
                String findUserCartSql = "SELECT id_carrello FROM carrelli WHERE id_utente = ?";
                try (PreparedStatement ps = conn.prepareStatement(findUserCartSql)) {
                    ps.setInt(1, idUtente);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            userCartId = rs.getInt("id_carrello");
                            System.out.println("Trovato carrello utente esistente ID: " + userCartId);
                        }
                    }
                }

                // Se non esiste, crealo
                if (userCartId == null) {
                    String createUserCartSql = "INSERT INTO carrelli (id_utente) VALUES (?)";
                    try (PreparedStatement ps = conn.prepareStatement(createUserCartSql, Statement.RETURN_GENERATED_KEYS)) {
                        ps.setInt(1, idUtente);
                        ps.executeUpdate();
                        try (ResultSet rs = ps.getGeneratedKeys()) {
                            if (rs.next()) {
                                userCartId = rs.getInt(1);
                                System.out.println("Creato nuovo carrello utente ID: " + userCartId);
                            }
                        }
                    }
                }

                if (userCartId == null) {
                    System.out.println("ERRORE: Impossibile creare/trovare carrello utente");
                    conn.rollback();
                    return false;
                }

                // 3. Trasferisci gli items dal carrello sessione a quello utente
                String transferItemsSql = """
                    INSERT INTO carrello_items (id_carrello, id_prodotto, quantita) 
                    SELECT ?, id_prodotto, quantita FROM carrello_items WHERE id_carrello = ? 
                    ON DUPLICATE KEY UPDATE quantita = carrello_items.quantita + VALUES(quantita)
                    """;

                int itemsTransferred = 0;
                try (PreparedStatement ps3 = conn.prepareStatement(transferItemsSql)) {
                    ps3.setInt(1, userCartId);
                    ps3.setInt(2, sessionCartId);
                    itemsTransferred = ps3.executeUpdate();
                    System.out.println("Items trasferiti: " + itemsTransferred);
                }

                // 4. Elimina il carrello della sessione e i suoi items
                String deleteSessionItemsSql = "DELETE FROM carrello_items WHERE id_carrello = ?";
                try (PreparedStatement ps4 = conn.prepareStatement(deleteSessionItemsSql)) {
                    ps4.setInt(1, sessionCartId);
                    int deletedItems = ps4.executeUpdate();
                    System.out.println("Items sessione eliminati: " + deletedItems);
                }

                String deleteSessionCartSql = "DELETE FROM carrelli WHERE id_carrello = ?";
                try (PreparedStatement ps5 = conn.prepareStatement(deleteSessionCartSql)) {
                    ps5.setInt(1, sessionCartId);
                    int deletedCarts = ps5.executeUpdate();
                    System.out.println("Carrello sessione eliminato: " + deletedCarts);
                }

                // 5. Verifica finale
                String verifyUserItemsSql = "SELECT COUNT(*) FROM carrello_items WHERE id_carrello = ?";
                try (PreparedStatement ps = conn.prepareStatement(verifyUserItemsSql)) {
                    ps.setInt(1, userCartId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            int finalCount = rs.getInt(1);
                            System.out.println("Items finali nel carrello utente: " + finalCount);
                        }
                    }
                }

                conn.commit();
                System.out.println("Trasferimento completato con successo");
                System.out.println("=== FINE DEBUG TRASFERIMENTO ===");
                return true;

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("ERRORE durante trasferimento: " + e.getMessage());
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("ERRORE connessione DB: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Aggiorna il timestamp di ultima modifica
     */
    private void aggiornaUltimaModifica(int idCarrello) {
        String sql = "UPDATE carrelli SET data_ultima_modifica = CURRENT_TIMESTAMP WHERE id_carrello = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCarrello);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}