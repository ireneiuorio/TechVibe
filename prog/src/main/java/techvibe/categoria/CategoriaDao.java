package techvibe.categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaDao <E extends Exception> {

    List<Categoria> fetchCategorie()throws E;
    boolean createCategoria(Categoria categoria) throws E;
    boolean  updateCategoria(Categoria categoria) throws  E;
    Optional<Categoria> fetchCategoriaEProdotti (int idCategoria)throws E;

}
