package techvibe.categoria;

import techvibe.prodotto.Prodotto;
import techvibe.prodotto.ProdottoExtractor;
import techvibe.storage.QueryBuilder;
import techvibe.storage.SqlDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SqlCategoriaDao extends SqlDao implements CategoriaDao <SQLException> {

    public SqlCategoriaDao(DataSource source) {
        super(source);
    }


    public List<Categoria> fetchCategorie() throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("categoria", "cat");
            queryBuilder.select();

            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
                ResultSet set = ps.executeQuery();
                CategoriaExtractor categoriaExtractor = new CategoriaExtractor();
                List<Categoria> categorie = new ArrayList<>();

                while (set.next()) {
                    categorie.add(categoriaExtractor.extract(set));

                }
                return categorie;
            }
        }
    }

    public boolean createCategoria(Categoria categoria) throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("categoria", "cat");
            queryBuilder.insert("label");
            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
                ps.setString(1, categoria.getNomeCategoria());
                int rows = ps.executeUpdate();
                return rows == 1;
            }
        }
    }

    public boolean updateCategoria(Categoria categoria) throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("categoria", "cat");
            queryBuilder.update("label").where("id=?");

            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
                ps.setString(1, categoria.getNomeCategoria());
                ps.setInt(2, categoria.getIdCategoria());
                int rows = ps.executeUpdate();
                return rows == 1;

            }
        }
    }

    @Override
    public Optional<Categoria> fetchCategoriaEProdotti(int idCategoria) throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("categoria", "cat");
            queryBuilder.select().innerJoin("prodotto", "pro").on("cat.id = pro.categoria_fk").where("cat.id=?");
            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
                ps.setInt(1, idCategoria);
                ResultSet set = ps.executeQuery();
                CategoriaExtractor categoriaExtractor = new CategoriaExtractor();
                Categoria categoria = null;

                if (set.next()) {
                    categoria = categoriaExtractor.extract(set);
                    categoria.setProdotti(new ArrayList<>());
                    ProdottoExtractor prodottoExtractor = new ProdottoExtractor();
                    categoria.getProdotti().add(prodottoExtractor.extract(set));
                    while (set.next()) {
                        categoria.getProdotti().add(prodottoExtractor.extract(set));
                    }

                }
                return Optional.ofNullable(categoria);


            }
        }
    }


}




