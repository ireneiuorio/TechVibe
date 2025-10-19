package techvibe.Model.categoria;

import techvibe.storage.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoriaExtractor implements ResultSetExtractor<Categoria> {

    public Categoria extract(ResultSet resultSet) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(resultSet.getInt("cat.idcategoria"));
        categoria.setNomeCategoria(resultSet.getString("cat.nomecategoria"));

        return categoria;

    }
}
