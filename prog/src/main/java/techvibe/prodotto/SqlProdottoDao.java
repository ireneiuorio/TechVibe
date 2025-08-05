package techvibe.prodotto;

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
    public List<Prodotto> fetchProdotti() throws SQLException {
        try(Connection conn=source.getConnection())
        {
            QueryBuilder queryBuilder=new QueryBuilder("prodotto","pro");
            queryBuilder.select();
            try(PreparedStatement ps=conn.prepareStatement(queryBuilder.generateQuery()))
            {
                ResultSet set=ps.executeQuery();
                List<Prodotto> prodotti=new ArrayList<>();
                ProdottoExtractor prodottoExtractor=new ProdottoExtractor();

                while(set.next())
                {
                    prodotti.add(prodottoExtractor.extract(set));
                }

                return prodotti;
            }
        }

    }

    @Override
    public boolean createProdotto(Prodotto prodotto) throws SQLException {
        try (Connection conn = source.getConnection()) {
            // --- Costruzione query con QueryBuilder ---
            QueryBuilder queryBuilder = new QueryBuilder("prodotto", "pro");
            queryBuilder.insert(
                    "dimensioneSchermo",
                    "connettivita",
                    "prezzo",
                    "modello",
                    "marca",
                    "sistemaOperativo",
                    "qtDisponibile",
                    "colore",
                    "storage",
                    "ram",
                    "categoria_fk"
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
                ps.setInt(11, prodotto.getCategoria().getIdCategoria());

                int rows = ps.executeUpdate();
                return rows == 1;
            }
        }
    }


    @Override
    public boolean updateProdotto(Prodotto prodotto) throws SQLException {
        try (Connection conn = source.getConnection()) {
            // --- Costruzione query con QueryBuilder ---
            QueryBuilder queryBuilder = new QueryBuilder("prodotto", "pro");
            queryBuilder.update(
                    "dimensioneSchermo",
                    "connettivita",
                    "prezzo",
                    "modello",
                    "marca",
                    "sistemaOperativo",
                    "qtDisponibile",
                    "colore",
                    "storage",
                    "ram",
                    "categoria_fk"
            ).where("pro.id = ?");

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
                ps.setInt(11, prodotto.getCategoria().getIdCategoria());
                ps.setInt(12, prodotto.getIdProdotto());

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
            queryBuilder.select().where("pro.id = ?");
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
            queryBuilder.select().where("pro.categoria_fk = ?");

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

}
