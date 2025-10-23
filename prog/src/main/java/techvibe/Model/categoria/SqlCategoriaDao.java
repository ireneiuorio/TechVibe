package techvibe.Model.categoria;

import techvibe.Model.components.Paginator;
import techvibe.Model.storage.QueryBuilder;
import techvibe.Model.storage.SqlDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlCategoriaDao extends SqlDao implements CategoriaDao<SQLException> {

    public SqlCategoriaDao(DataSource source) {
        super(source);
    }


    public List<Categoria> fetchCategorie(Paginator paginator) throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder qb = new QueryBuilder("categoria", "cat");
            String sql = qb
                    .select()
                    .limit(true)
                    .generateQuery();

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, paginator.getOffset());
                ps.setInt(2, paginator.getLimit());

                try (ResultSet set = ps.executeQuery()) {
                    CategoriaExtractor categoriaExtractor = new CategoriaExtractor();
                    List<Categoria> categorie = new ArrayList<>();
                    while (set.next()) {
                        categorie.add(categoriaExtractor.extract(set));
                    }
                    return categorie;
                }
            }
        }
    }

    public int countAll() throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder qb = new QueryBuilder("categoria", "cat");

            String sql = qb
                    .select("COUNT(*) AS total")
                    .generateQuery()
                    .replace("cat.COUNT(*)", "COUNT(*)");

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("total");
                }
                return 0;
            }
        }
    }


}




