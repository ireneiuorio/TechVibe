package techvibe.Model.categoria;

import techvibe.Model.components.Paginator;

import java.util.List;

public interface CategoriaDao<E extends Exception> {

    List<Categoria> fetchCategorie(Paginator paginator) throws E;
    int countAll() throws E;
}
