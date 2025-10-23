package techvibe.Model.ordine;

import techvibe.Model.components.Paginator;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface OrdineDao <E extends Exception > {

    List<Ordine> fetchOrdini(Paginator paginator) throws E;

    List<Ordine> fetchOrdineConProdotto(int utenteId, Paginator paginator) throws E;

    Optional<Ordine> fetchOrdine(int id) throws E;

    boolean createOrdine(Ordine ordine) throws E;

    //da implementare
    List<Ordine> fetchOrdiniByUtente(int utenteId) throws E;

    int countAll() throws E;
    Optional<Ordine> fetchOrdineCompletoById(int ordineId) throws E;

    Optional<Ordine> fetchOrdineConUtente(int ordineId) throws SQLException;
     boolean updateOrdine(Ordine ordine) throws SQLException;

    Optional<Ordine> fetchOrdineCompleto(int ordineId) throws SQLException;
}