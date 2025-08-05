package techvibe.ordine;

import techvibe.carrello.Carrello;
import techvibe.carrello.CarrelloItem;
import techvibe.categoria.Categoria;
import techvibe.categoria.CategoriaDao;
import techvibe.categoria.CategoriaExtractor;
import techvibe.prodotto.Prodotto;
import techvibe.prodotto.ProdottoExtractor;
import techvibe.storage.QueryBuilder;
import techvibe.storage.SqlDao;
import techvibe.utente.Utente;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SqlOrdineDao extends SqlDao implements OrdineDao <SQLException> {

    public SqlOrdineDao(DataSource source) {
        super(source);
    }


    @Override
    public List<Ordine> fetchOrdineConProdotto(int IdUtente) throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("ordine_prodotto", "op");
            StringBuilder builder = new StringBuilder();
            queryBuilder.select().innerJoin("ordine", "ord").on("op.ordine_fk=ord.id");
            queryBuilder.innerJoin("prodotto", "pro").on("pp.prodotto_fk =ord.id");
            queryBuilder.outerJoin(true, "categoria", "cat").on("cat.id=pro.categoria_fk");

            queryBuilder.where("ord.utente_fk=?");


            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
                System.out.println(ps.toString());
                ps.setInt(1, IdUtente);
                ResultSet set = ps.executeQuery();

                Map<Integer, Ordine> ordineMap = new LinkedHashMap<>();
                OrdineExtractor ordineExtractor = new OrdineExtractor();
                ProdottoExtractor prodottoExtractor = new ProdottoExtractor();
                CategoriaExtractor categoriaExtractor = new CategoriaExtractor();

                while (set.next()) {
                    int ordineId = set.getInt("ord.id");
                    if (!ordineMap.containsKey(ordineId)) {
                        Ordine ordine = ordineExtractor.extract(set);
                        ordine.setCarrello(new Carrello(new ArrayList<>()));
                        ordineMap.put(ordineId, ordine);
                    }

                    Prodotto prodotto = prodottoExtractor.extract(set);
                    Categoria categoria = categoriaExtractor.extract(set);
                    prodotto.setCategoria(categoria);
                    ordineMap.get(ordineId).getCarrello().addProdotto(prodotto, set.getInt("op.quantita"));


                }

                return new ArrayList<>(ordineMap.values());

            }


        }
    }


    @Override
    public List<Ordine> fetchOrdini() throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("ordine", "ord");
            queryBuilder.select(); // SELECT * FROM ordine AS ord
            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
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

    @Override
    public Optional<Ordine> fetchOrdine(int id) throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("ordine", "ord");
            queryBuilder.select().where("ord.id = ?");
            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
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
            queryBuilder.select().where("ord.utente_fk = ?");
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


    @Override
    public boolean createOrdine(Ordine ordine) throws SQLException {
        try (Connection conn = source.getConnection()) {
            conn.setAutoCommit(false); // Inizio transazione
            try {
                // --- Inserimento ordine ---
                QueryBuilder qbOrdine = new QueryBuilder("ordine", "ord");
                qbOrdine.insert("stato", "totale", "scontototale", "metododispedizione", "metododipagamento", "utente_fk");

                try (PreparedStatement psOrd = conn.prepareStatement(qbOrdine.generateQuery(), PreparedStatement.RETURN_GENERATED_KEYS)) {
                    psOrd.setString(1, ordine.getStato());
                    psOrd.setDouble(2, ordine.getTotale());
                    psOrd.setDouble(3, ordine.getScontoTotale());
                    psOrd.setString(4, ordine.getMetodoDiSpedizione());
                    psOrd.setString(5, ordine.getMetodoDiPagamento());
                    psOrd.setInt(6, ordine.getUtente().getIdUtente());
                    int rowsOrd = psOrd.executeUpdate();

                    if (rowsOrd != 1) {
                        conn.rollback();
                        return false;
                    }

                    int ordineId;
                    try (ResultSet generatedKeys = psOrd.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            ordineId = generatedKeys.getInt(1);
                            ordine.setIdOrdine(ordineId);
                        } else {
                            throw new SQLException("Creazione ordine fallita, nessun ID generato.");
                        }
                    }

                    // --- Inserimento prodotti del carrello ---
                    if (ordine.getCarrello() != null && ordine.getCarrello().getItems() != null) {
                        QueryBuilder qbProdotti = new QueryBuilder("ordine_prodotto", "op");
                        qbProdotti.insert("ordine_fk", "prodotto_fk", "quantita");

                        try (PreparedStatement psProd = conn.prepareStatement(qbProdotti.generateQuery())) {
                            for (CarrelloItem item : ordine.getCarrello().getItems()) {
                                psProd.setInt(1, ordineId);
                                psProd.setInt(2, item.getProdotto().getIdProdotto());
                                psProd.setInt(3, item.getQuantita());
                                psProd.addBatch();
                            }
                            psProd.executeBatch();
                        }
                    }
                }

                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
