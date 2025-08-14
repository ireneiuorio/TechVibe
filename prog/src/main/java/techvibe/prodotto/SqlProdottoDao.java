package techvibe.prodotto;

import techvibe.components.Paginator;
import techvibe.ordine.OrdineExtractor;
import techvibe.storage.QueryBuilder;
import techvibe.storage.SqlDao;

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


    @Override
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
                    "IdCategoria"
            );

            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
                ps.setDouble(1, prodotto.getDimensioneSchermo());
                ps.setString(2, prodotto.getConnettivita());
                ps.setDouble(3, prodotto.getPrezzo());
                ps.setString(4, prodotto.getModello());
                ps.setString(5, prodotto.getMarca());
                ps.setString(6, prodotto.getSistemaOperativo());
                ps.setInt(7, prodotto.getQtDisponibile());
                ps.setString(8, prodotto.getColore());
                ps.setInt(9, prodotto.getStorage());
                ps.setInt(10, prodotto.getRam());
                ps.setString(11,prodotto.getCover());
                ps.setString(12, prodotto.getImmagine1());
                ps.setString(13, prodotto.getImmagine2());
                ps.setString(14, prodotto.getImmagine3());
                ps.setInt(15, prodotto.getCategoria().getIdCategoria());

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
                    "DimSchermo",
                    "Connettivita",
                    "Prezzo",
                    "Modello",
                    "Marca",
                    "SistemaOperativo",
                    "QtDisponibile",
                    "Colore",
                    "Storage",
                    "Ram",
                    "Categoria_fk"
            ).where("pro.idProdotto = ?");

            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
                ps.setDouble(1, prodotto.getDimensioneSchermo());
                ps.setString(2, prodotto.getConnettivita());
                ps.setDouble(3, prodotto.getPrezzo());
                ps.setString(4, prodotto.getModello());
                ps.setString(5, prodotto.getMarca());
                ps.setString(6, prodotto.getSistemaOperativo());
                ps.setInt(7, prodotto.getQtDisponibile());
                ps.setString(8, prodotto.getColore());
                ps.setInt(9, prodotto.getStorage());
                ps.setInt(10, prodotto.getRam());
                ps.setString(11, prodotto.getCover());
                ps.setInt(12, prodotto.getCategoria().getIdCategoria());
                ps.setInt(13, prodotto.getIdProdotto());

                int rows = ps.executeUpdate();
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


}
