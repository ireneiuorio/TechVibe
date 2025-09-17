package techvibe.ordine;

import techvibe.carrello.Carrello;
import techvibe.carrello.CarrelloItem;
import techvibe.categoria.Categoria;
import techvibe.categoria.CategoriaExtractor;
import techvibe.prodotto.Prodotto;
import techvibe.prodotto.ProdottoExtractor;
import techvibe.components.Paginator;
import techvibe.storage.QueryBuilder;
import techvibe.storage.SqlDao;
import techvibe.utente.Utente;
import techvibe.utente.UtenteExtractor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SqlOrdineDao extends SqlDao implements OrdineDao<SQLException> {

    public SqlOrdineDao(DataSource source) {
        super(source);
    }

    @Override
    public List<Ordine> fetchOrdineConProdotto(int IdUtente, Paginator paginator) throws SQLException {
        try (Connection conn = source.getConnection()) {

            String query = OrdineQuery.fetchOrdineConProdotti();
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                System.out.println(ps.toString());
                ps.setInt(1, IdUtente);
                ps.setInt(2, paginator.getOffset());
                ps.setInt(3, paginator.getLimit());
                ResultSet set = ps.executeQuery();

                Map<Integer, Ordine> ordineMap = new LinkedHashMap<>();
                OrdineExtractor ordineExtractor = new OrdineExtractor();
                ProdottoExtractor prodottoExtractor = new ProdottoExtractor();
                CategoriaExtractor categoriaExtractor = new CategoriaExtractor();

                while (set.next()) {
                    // CORRETTO: usa idordine invece di id
                    int ordineId = set.getInt("ord.idordine");
                    if (!ordineMap.containsKey(ordineId)) {
                        Ordine ordine = ordineExtractor.extract(set);
                        ordine.setCarrello(new Carrello(new ArrayList<>()));
                        ordineMap.put(ordineId, ordine);
                    }

                    Prodotto prodotto = prodottoExtractor.extract(set);
                    Categoria categoria = categoriaExtractor.extract(set);
                    prodotto.setCategoria(categoria);
                    // CORRETTO: usa com.quantita invece di op.quantita
                    ordineMap.get(ordineId).getCarrello().addProdotto(prodotto, set.getInt("com.quantita"));
                }

                return new ArrayList<>(ordineMap.values());
            }
        }
    }

    @Override
    public List<Ordine> fetchOrdini(Paginator paginator) throws SQLException {
        try (Connection conn = source.getConnection()) {
            String query = OrdineQuery.fetchOrdini();
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, paginator.getOffset());
                ps.setInt(2, paginator.getLimit());
                ResultSet set = ps.executeQuery();
                OrdineExtractor ordineExtractor = new OrdineExtractor();
                UtenteExtractor utenteExtractor = new UtenteExtractor(); // AGGIUNGI QUESTO
                List<Ordine> o = new ArrayList<>();
                while (set.next()) {
                    Ordine ordine = ordineExtractor.extract(set);
                    Utente utente = utenteExtractor.extract(set); // AGGIUNGI QUESTO
                    ordine.setUtente(utente); // AGGIUNGI QUESTO
                    o.add(ordine);
                }
                return o;
            }
        }
    }
    public boolean createOrdine(Ordine ordine) throws SQLException {
        try (Connection conn = source.getConnection()) {
            conn.setAutoCommit(false);
            String query = OrdineQuery.createOrdine();
            String query2 = OrdineQuery.insertCart();
            String checkQuantityQuery = "SELECT QtDisponibile FROM prodotto WHERE IdProdotto = ?";
            String updateQuantityQuery = "UPDATE prodotto SET QtDisponibile = QtDisponibile - ? WHERE IdProdotto = ?";

            try (PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
                 PreparedStatement psAssoc = conn.prepareStatement(query2);
                 PreparedStatement psCheck = conn.prepareStatement(checkQuantityQuery);
                 PreparedStatement psUpdate = conn.prepareStatement(updateQuantityQuery)) {

                // Verifica che tutti i prodotti abbiano quantità sufficiente
                for (CarrelloItem item : ordine.getCarrello().getItems()) {
                    psCheck.setInt(1, item.getProdotto().getIdProdotto());
                    ResultSet rs = psCheck.executeQuery();

                    if (rs.next()) {
                        int quantitaDisponibile = rs.getInt("QtDisponibile");
                        if (quantitaDisponibile < item.getQuantita()) {
                            conn.rollback();
                            conn.setAutoCommit(true);
                            return false;
                        }
                    } else {
                        conn.rollback();
                        conn.setAutoCommit(true);
                        return false;
                    }
                }

                // Inserisci l'ordine
                ps.setInt(1, ordine.getUtente().getIdUtente());
                ps.setString(2, ordine.getStato());
                ps.setDouble(3, ordine.getTotale());
                ps.setDouble(4, ordine.getScontoTotale());
                ps.setString(5, ordine.getMetodoDiSpedizione());
                ps.setString(6, ordine.getMetodoDiPagamento());

                int rows = ps.executeUpdate();

                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idOrdineGenerato = generatedKeys.getInt(1);
                    ordine.setIdOrdine(idOrdineGenerato);

                    int total = rows;

                    // Inserisci nella composizioneordine e decrementa quantità
                    for (CarrelloItem item : ordine.getCarrello().getItems()) {
                        // Inserisci nella tabella composizioneordine
                        psAssoc.setInt(1, item.getProdotto().getIdProdotto());
                        psAssoc.setInt(2, idOrdineGenerato);
                        psAssoc.setInt(3, item.getQuantita());
                        total += psAssoc.executeUpdate();

                        // Decrementa la quantità disponibile
                        psUpdate.setInt(1, item.getQuantita());
                        psUpdate.setInt(2, item.getProdotto().getIdProdotto());
                        int updatedRows = psUpdate.executeUpdate();

                        if (updatedRows == 0) {
                            conn.rollback();
                            conn.setAutoCommit(true);
                            return false;
                        }
                    }

                    if (total == (rows + ordine.getCarrello().getItems().size())) {
                        conn.commit();
                        conn.setAutoCommit(true);
                        return true;
                    } else {
                        conn.rollback();
                        conn.setAutoCommit(true);
                        return false;
                    }
                } else {
                    conn.rollback();
                    conn.setAutoCommit(true);
                    return false;
                }
            }
        }
    }

    @Override
    public Optional<Ordine> fetchOrdine(int id) throws SQLException {
        try (Connection conn = source.getConnection()) {
            String query = OrdineQuery.fetchOrdine();

            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, id);
                ResultSet set = ps.executeQuery();

                if (set.next()) {
                    OrdineExtractor ordineExtractor = new OrdineExtractor();
                    return Optional.of(ordineExtractor.extract(set));
                }
                return Optional.empty();
            }
        }
    }


    @Override
    public List<Ordine> fetchOrdiniByUtente(int utenteId) throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("ordine", "ord");
            queryBuilder.select().where("ord.idaccount = ?");
            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
                ps.setInt(1, utenteId);
                ResultSet set = ps.executeQuery();

                List<Ordine> ordini = new ArrayList<>();
                OrdineExtractor ordineExtractor = new OrdineExtractor();
                while (set.next()) {
                    ordini.add(ordineExtractor.extract(set));
                }
                return ordini;
            }
        }
    }









    public Optional<Ordine> fetchOrdineConProdottiById(int ordineId, int utenteId) throws SQLException {
        try (Connection conn = source.getConnection()) {
            String query = """
            SELECT ord.*, pro.*, cat.*, com.quantita
            FROM ordine ord
            INNER JOIN composizioneordine com ON com.idordine = ord.idordine
            INNER JOIN prodotto pro ON pro.idprodotto = com.idprodotto
            LEFT JOIN categoria cat ON cat.idcategoria = pro.idcategoria
            WHERE ord.idordine = ? AND ord.idaccount = ?
            """;

            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, ordineId);
                ps.setInt(2, utenteId);
                ResultSet set = ps.executeQuery();

                Ordine ordine = null;
                OrdineExtractor ordineExtractor = new OrdineExtractor();
                ProdottoExtractor prodottoExtractor = new ProdottoExtractor();
                CategoriaExtractor categoriaExtractor = new CategoriaExtractor();

                while (set.next()) {
                    if (ordine == null) {
                        ordine = ordineExtractor.extract(set);
                        ordine.setCarrello(new Carrello(new ArrayList<>()));
                    }

                    Prodotto prodotto = prodottoExtractor.extract(set);
                    Categoria categoria = categoriaExtractor.extract(set);
                    prodotto.setCategoria(categoria);
                    ordine.getCarrello().addProdotto(prodotto, set.getInt("quantita"));
                }

                return Optional.ofNullable(ordine);
            }
        }
    }

    public Optional<Ordine> fetchOrdineCompletoById(int ordineId) throws SQLException {
        try (Connection conn = source.getConnection()) {
            String query = """
        SELECT ord.*, ute.*, pro.*, cat.*, com.quantita
        FROM ordine ord
        INNER JOIN utente ute ON ute.idaccount = ord.idaccount
        LEFT JOIN composizioneordine com ON com.idordine = ord.idordine
        LEFT JOIN prodotto pro ON pro.idprodotto = com.idprodotto
        LEFT JOIN categoria cat ON cat.idcategoria = pro.idcategoria
        WHERE ord.idordine = ?
        """;

            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, ordineId);
                ResultSet set = ps.executeQuery();

                Ordine ordine = null;
                OrdineExtractor ordineExtractor = new OrdineExtractor();
                UtenteExtractor utenteExtractor = new UtenteExtractor();
                ProdottoExtractor prodottoExtractor = new ProdottoExtractor();
                CategoriaExtractor categoriaExtractor = new CategoriaExtractor();

                while (set.next()) {
                    if (ordine == null) {
                        ordine = ordineExtractor.extract(set);
                        Utente utente = utenteExtractor.extract(set);
                        ordine.setUtente(utente);
                        ordine.setCarrello(new Carrello(new ArrayList<>()));
                    }

                    // Estrai prodotto se presente (LEFT JOIN può restituire NULL)
                    if (set.getObject("pro.idprodotto") != null) {
                        Prodotto prodotto = prodottoExtractor.extract(set);
                        Categoria categoria = categoriaExtractor.extract(set);
                        prodotto.setCategoria(categoria);
                        ordine.getCarrello().addProdotto(prodotto, set.getInt("com.quantita"));
                    }
                }

                return Optional.ofNullable(ordine);
            }
        }
    }

    @Override
    public int countAll() throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder qb = new QueryBuilder("ordine", "ord");

            String sql = qb
                    .select("COUNT(*) AS total")
                    .generateQuery()
                    .replace("ord.COUNT(*)", "COUNT(*)");

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("total");
                }
                return 0;
            }
        }
    }

    // Aggiungi questi due metodi nel SqlOrdineDao

    /**
     * Recupera un ordine con le informazioni utente (senza prodotti)
     */
    public Optional<Ordine> fetchOrdineConUtente(int ordineId) throws SQLException {
        try (Connection conn = source.getConnection()) {
            String query = """
        SELECT ord.*, ute.*
        FROM ordine ord
        INNER JOIN utente ute ON ute.idaccount = ord.idaccount
        WHERE ord.idordine = ?
        """;

            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, ordineId);
                ResultSet set = ps.executeQuery();

                if (set.next()) {
                    OrdineExtractor ordineExtractor = new OrdineExtractor();
                    UtenteExtractor utenteExtractor = new UtenteExtractor();

                    Ordine ordine = ordineExtractor.extract(set);
                    Utente utente = utenteExtractor.extract(set);
                    ordine.setUtente(utente);

                    return Optional.of(ordine);
                }

                return Optional.empty();
            }
        }
    }

    /**
     * Aggiorna un ordine esistente
     */
    // Aggiungi questo metodo nel SqlOrdineDao

    /**
     * Recupera un ordine completo con utente e prodotti (carrello)
     */
    public Optional<Ordine> fetchOrdineCompleto(int ordineId) throws SQLException {
        try (Connection conn = source.getConnection()) {
            String query = """
        SELECT ord.*, ute.*, pro.*, cat.*, com.quantita
        FROM ordine ord
        INNER JOIN utente ute ON ute.idaccount = ord.idaccount
        LEFT JOIN composizioneordine com ON com.idordine = ord.idordine
        LEFT JOIN prodotto pro ON pro.idprodotto = com.idprodotto
        LEFT JOIN categoria cat ON cat.idcategoria = pro.idcategoria
        WHERE ord.idordine = ?
        """;

            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, ordineId);
                ResultSet set = ps.executeQuery();

                Ordine ordine = null;
                OrdineExtractor ordineExtractor = new OrdineExtractor();
                UtenteExtractor utenteExtractor = new UtenteExtractor();
                ProdottoExtractor prodottoExtractor = new ProdottoExtractor();
                CategoriaExtractor categoriaExtractor = new CategoriaExtractor();

                while (set.next()) {
                    if (ordine == null) {
                        // Estrai ordine e utente solo la prima volta
                        ordine = ordineExtractor.extract(set);
                        Utente utente = utenteExtractor.extract(set);
                        ordine.setUtente(utente);
                        ordine.setCarrello(new Carrello(new ArrayList<>()));
                    }

                    // Estrai prodotto se presente (LEFT JOIN può restituire NULL)
                    if (set.getObject("pro.idprodotto") != null) {
                        Prodotto prodotto = prodottoExtractor.extract(set);
                        Categoria categoria = categoriaExtractor.extract(set);
                        prodotto.setCategoria(categoria);
                        ordine.getCarrello().addProdotto(prodotto, set.getInt("com.quantita"));
                    }
                }

                return Optional.ofNullable(ordine);
            }
        }
    }

    /**
     * Aggiorna un ordine esistente (solo stato e sconto)
     */
    public boolean updateOrdine(Ordine ordine) throws SQLException {
        try (Connection conn = source.getConnection()) {
            String query = """
        UPDATE ordine 
        SET stato = ?, scontototale = ?
        WHERE idordine = ?
        """;

            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, ordine.getStato());
                ps.setDouble(2, ordine.getScontoTotale());
                ps.setInt(3, ordine.getIdOrdine());

                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
            }
        }
    }

// E aggiungi anche questi metodi nell'interfaccia OrdineDao:
// Optional<Ordine> fetchOrdineCompleto(int ordineId) throws SQLException;
// boolean updateOrdine(Ordine ordine) throws SQLException;

// E aggiungi anche questi metodi nell'interfaccia OrdineDao:
// Optional<Ordine> fetchOrdineConUtente(int ordineId) throws SQLException;
// boolean updateOrdine(Ordine ordine) throws SQLException;
}