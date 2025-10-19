package techvibe.prodotto;

import techvibe.Model.components.Paginator;
import techvibe.search.Condition;

import java.util.List;
import java.util.Optional;

public interface ProdottoDao<E extends Exception> {

    List<Prodotto> fetchProdotti(Paginator paginator) throws E;
    boolean createProdotto(Prodotto prodotto) throws E;
    boolean updateProdotto(Prodotto prodotto) throws E;
    Optional<Prodotto> fetchProdotto(int id) throws E;
    List<Prodotto> fetchProdottiByCategoria(int categoriaId) throws E;
    int countAll()throws E;
    List<Prodotto> search(List<Condition> conditions) throws E;
   boolean deleteProdotto(int id) throws E;
    public List<Prodotto> fetchProdottiInOfferta() throws E;
}
