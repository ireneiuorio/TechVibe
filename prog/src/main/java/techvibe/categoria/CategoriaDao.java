package techvibe.categoria;

import techvibe.components.Paginator;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CategoriaDao <E extends Exception> {

    List<Categoria> fetchCategorie(Paginator paginator) throws E;

    boolean createCategoria(Categoria categoria) throws E;

    boolean updateCategoria(Categoria categoria) throws E;

    Optional<Categoria> fetchCategoriaEProdotti(int idCategoria, Paginator paginator) throws E;

    int countAll() throws E;
}
