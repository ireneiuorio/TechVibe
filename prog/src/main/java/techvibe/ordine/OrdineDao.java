package techvibe.ordine;

import techvibe.search.Paginator;

import java.util.List;
import java.util.Optional;

public interface OrdineDao <E extends Exception > {

    List<Ordine> fetchOrdini(Paginator paginator) throws E;
    List<Ordine> fetchOrdineConProdotto(int utenteId, Paginator paginator) throws E;
    Optional <Ordine> fetchOrdine(int id) throws E;
    boolean createOrdine (Ordine ordine) throws E;
    //da implementare
    List<Ordine> fetchOrdiniByUtente(int utenteId) throws E;



}
