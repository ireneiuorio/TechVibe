package techvibe.Model.prodotto;

import techvibe.Model.components.Paginator;
import techvibe.Model.search.Condition;
import techvibe.Model.search.Operator;
import techvibe.Model.storage.QueryBuilder;
import techvibe.Model.storage.SqlDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqlProdottoDao extends SqlDao implements ProdottoDao<SQLException> {

    public SqlProdottoDao(DataSource source) {
        super(source);
    }

    //Il dao esegue la query e poi passa il result set all'extractor per renderlo una classe java

    //richiamo nella servelt
    public List<Prodotto> fetchProdotti(Paginator paginator) throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder qb = new QueryBuilder("prodotto", "pro");
            String sql = qb
                    .select()                 // SELECT * FROM prodotto AS pro
                    .limit(true)              // ... LIMIT ?, ?
                    .generateQuery();

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, paginator.getOffset()); // primo ? = offset
                ps.setInt(2, paginator.getLimit());  // secondo ? = limit

                try (ResultSet rs = ps.executeQuery()) {
                    List<Prodotto> prodotti = new ArrayList<>();
                    ProdottoExtractor extractor = new ProdottoExtractor();
                    while (rs.next()) {
                        prodotti.add(extractor.extract(rs));
                    }
                    return prodotti;
                }
            }
        }
    }


    @Override
    public boolean createProdotto(Prodotto prodotto) throws SQLException {
        try (Connection conn = source.getConnection()) {

            QueryBuilder queryBuilder = new QueryBuilder("prodotto", "pro");
            queryBuilder.insert(
                    "Dimschermo",
                    "Connettivita",
                    "Prezzo",
                    "Modello",
                    "Marca",
                    "SistemaOperativo",
                    "QtDisponibile",
                    "Colore",
                    "StorageDispositivo",
                    "Ram",
                    "Immagine1",
                    "Immagine2",
                    "Immagine3",
                    "Immagine4",
                    "IdCategoria",
                    "percentuale_sconto"
            );

            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
                ps.setDouble(1, prodotto.getDimensioneSchermo());
                ps.setString(2, prodotto.getConnettivita());
                ps.setDouble(3, prodotto.getPrezzoOriginale());
                ps.setString(4, prodotto.getModello());
                ps.setString(5, prodotto.getMarca());
                ps.setString(6, prodotto.getSistemaOperativo());
                ps.setInt(7, prodotto.getQtDisponibile());
                ps.setString(8, prodotto.getColore());
                ps.setInt(9, prodotto.getStorage());
                ps.setInt(10, prodotto.getRam());
                ps.setString(11, prodotto.getImmagine1());
                ps.setString(12, prodotto.getImmagine2());
                ps.setString(13, prodotto.getImmagine3());
                ps.setString(14, prodotto.getImmagine4());
                ps.setInt(15, prodotto.getCategoria().getIdCategoria());
                ps.setDouble(16, prodotto.getPercentualeSconto());

                int rows = ps.executeUpdate();
                return rows == 1;
            }
        }
    }


    @Override
    public boolean updateProdotto(Prodotto prodotto) throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("prodotto", "pro");
            queryBuilder.update(
                    "Dimschermo",
                    "Connettivita",
                    "Prezzo",
                    "Modello",
                    "Marca",
                    "SistemaOperativo",
                    "QtDisponibile",
                    "Colore",
                    "StorageDispositivo",
                    "Ram",
                    "IdCategoria",
                    "percentuale_sconto"
            ).where("idProdotto = ?");

            String generatedQuery = queryBuilder.generateQuery();
            System.out.println("=== QUERY GENERATA ===");
            System.out.println(generatedQuery);
            System.out.println("ID Prodotto WHERE: " + prodotto.getIdProdotto());

            try (PreparedStatement ps = conn.prepareStatement(generatedQuery)) {
                ps.setDouble(1, prodotto.getDimensioneSchermo());
                ps.setString(2, prodotto.getConnettivita());
                ps.setDouble(3, prodotto.getPrezzoOriginale());
                ps.setString(4, prodotto.getModello());
                ps.setString(5, prodotto.getMarca());
                ps.setString(6, prodotto.getSistemaOperativo());
                ps.setInt(7, prodotto.getQtDisponibile());
                ps.setString(8, prodotto.getColore());
                ps.setInt(9, prodotto.getStorage());
                ps.setInt(10, prodotto.getRam());
                ps.setInt(11, prodotto.getCategoria().getIdCategoria());
                ps.setDouble(12, prodotto.getPercentualeSconto());
                ps.setInt(13, prodotto.getIdProdotto());

                int rows = ps.executeUpdate();
                System.out.println("Righe modificate: " + rows);
                return rows == 1;
            }
        }
    }

    @Override
    public Optional<Prodotto> fetchProdotto(int id) throws SQLException {
        try(Connection conn=source.getConnection())
        {
            QueryBuilder queryBuilder=new QueryBuilder("prodotto","pro");
            queryBuilder.select().where("pro.idprodotto = ?");
            try(PreparedStatement ps=conn.prepareStatement(queryBuilder.generateQuery()))
            {
                ps.setInt(1,id);
                ResultSet set=ps.executeQuery();
                if(set.next())
                {
                    ProdottoExtractor prodottoExtractor=new ProdottoExtractor();
                    return Optional.of(prodottoExtractor.extract(set));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Prodotto> fetchProdottiByCategoria(int categoriaId) throws SQLException {
        try (Connection conn = source.getConnection()) {
            // --- Costruzione query ---
            QueryBuilder queryBuilder = new QueryBuilder("prodotto", "pro");
            queryBuilder.select().where("pro.idcategoria= ?");

            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
                ps.setInt(1, categoriaId);
                ResultSet set = ps.executeQuery();

                List<Prodotto> prodotti = new ArrayList<>();
                ProdottoExtractor prodottoExtractor = new ProdottoExtractor();

                while (set.next()) {
                    Prodotto prodotto = prodottoExtractor.extract(set);
                    prodotti.add(prodotto);
                }
                return prodotti;
            }
        }
    }

    public int countAll() throws SQLException {
        final String sql = "SELECT COUNT(*) AS total FROM prodotto pro";
        try (Connection c = source.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt("total") : 0;
        }
    }

    @Override
    public List<Prodotto> search(List<Condition> conditions) throws SQLException {
        try(Connection conn = source.getConnection()) {
            QueryBuilder builder = new QueryBuilder("prodotto", "pro");
            builder.select(); // NIENTE JOIN PER ORA

            if(!conditions.isEmpty()) {
                builder.where().search(conditions);
            }

            String query = builder.generateQuery();
            System.out.println("QUERY: " + query); // DEBUG

            try(PreparedStatement ps = conn.prepareStatement(query)) {
                for(int i = 0; i < conditions.size(); i++) {
                    Object value;
                    if(conditions.get(i).getOperator() == Operator.MATCH) {
                        value = "%" + conditions.get(i).getValue() + "%";
                    } else {
                        value = conditions.get(i).getValue();
                    }
                    ps.setObject(i+1, value);
                    System.out.println("Param " + (i+1) + ": " + value); // DEBUG
                }

                ResultSet set = ps.executeQuery();
                List<Prodotto> prodotti = new ArrayList<>();

                while(set.next()) {
                    Prodotto prodotto = new ProdottoExtractor().extract(set);
                    // NON settare categoria per ora
                    prodotti.add(prodotto);
                }

                System.out.println("TROVATI: " + prodotti.size()); // DEBUG
                return prodotti;
            }
        }
    }

    public boolean deleteProdotto(int id) throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("prodotto", "pro");
            queryBuilder.delete().where("idProdotto = ?");

            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
                ps.setInt(1, id);

                int rows = ps.executeUpdate();
                return rows == 1;
            }
        }
    }

    public boolean updateQuantity(int id, int newQuantity) throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("prodotto", "pro");
            queryBuilder.update("QtDisponibile").where("pro.idprodotto = ?");

            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
                ps.setInt(1, newQuantity);
                ps.setInt(2, id);

                int rows = ps.executeUpdate();
                return rows == 1;
            }
        }
    }
    // Metodo per ottenere tutti i prodotti in offerta (sconto > 0)
    public List<Prodotto> fetchProdottiInOfferta() throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder qb = new QueryBuilder("prodotto", "pro");
            String sql = qb
                    .select()
                    .where("pro.percentuale_sconto > ?")
                    .generateQuery();

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setDouble(1, 0.0); // Cerca prodotti con sconto > 0

                try (ResultSet rs = ps.executeQuery()) {
                    List<Prodotto> prodotti = new ArrayList<>();
                    ProdottoExtractor extractor = new ProdottoExtractor();
                    while (rs.next()) {
                        prodotti.add(extractor.extract(rs));
                    }
                    System.out.println("PRODOTTI IN OFFERTA TROVATI: " + prodotti.size());
                    return prodotti;
                }
            }
        }
    }
}