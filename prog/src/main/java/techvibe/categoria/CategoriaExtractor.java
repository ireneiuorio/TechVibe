package techvibe.categoria;

import techvibe.storage.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoriaExtractor implements ResultSetExtractor<Categoria> {

    public Categoria extract (ResultSet resultSet) throws SQLException
    {
        Categoria categoria=new Categoria();
        categoria.setIdCategoria(resultSet.getInt("cat.id"));
        categoria.setNomeCategoria(resultSet.getString("cat.nome"));

        return categoria;

    }
}
