package techvibe.ordine;

import java.util.List;
import java.util.Optional;

public interface OrdineDao <E extends Exception > {

    List<Ordine> fetchOrdini() throws E;
    List<Ordine> fetchOrdineConProdotto(int utenteId) throws E;
    Optional <Ordine> fetchOrdine(int id) throws E;
    boolean createOrdine (Ordine ordine) throws E;
    //da implementare
    List<Ordine> fetchOrdiniByUtente(int utenteId) throws E;



}
