package techvibe.Model.carrello;

import java.util.Optional;

public interface CarrelloDao {
    Optional<Carrello> getCarrelloByUtente(int idUtente);//Recupera carrello per utente loggato
    Optional<Carrello> getCarrelloBySessione(String sessionId);//Recupera carrello per utente anonimo
    Optional<Integer> creaCarrelloUtente(int idUtente);//Crea nuovo carrello per utente loggato
    Optional<Integer> creaCarrelloSessione(String sessionId);//Crea nuovo carrello per utente anonimo
    Optional<Integer> getCarrelloIdByUtente(int idUtente);//Restituisce carrello di un utente
    boolean aggiungiProdotto(int idCarrello, int idProdotto, int quantita);//Aggiunge o aggiorna un prodotto in un carrello
    boolean rimuoviProdotto(int idCarrello, int idProdotto);//Rimuove un prodotto dal carrello
    boolean aggiornaQuantita(int idCarrello, int idProdotto, int nuovaQuantita); //Aggiorna la quantità di un prodotto
    boolean svuotaCarrello(int idCarrello);//Svuota completamente il carrello
    boolean trasferisciCarrello(String sessionId, int idUtente);//Trasferisce il carrello da sessione a utente loggato
    Optional<Integer> getCarrelloIdBySessione(String sessionId);//Restituisce solo l'id del carrello per utente non loggato

}
