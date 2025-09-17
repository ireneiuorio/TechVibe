package techvibe.carrello;

import techvibe.prodotto.Prodotto;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

public class CarrelloService {

    // Chiavi per salvare informazioni in sessione
    private static final String CARRELLO_SESSION_KEY = "carrello";
    private static final String CARRELLO_ID_SESSION_KEY = "carrello_id";
    private static final String USER_ID_SESSION_KEY = "user_id"; // Assumendo che l'ID utente sia in sessione

    private final CarrelloDao carrelloDao;

    public CarrelloService(CarrelloDao carrelloDao) {
        this.carrelloDao = carrelloDao;
    }

    /**
     * Recupera il carrello dalla sessione o dal database.
     * Priorità: 1) Sessione 2) Database 3) Nuovo carrello
     */
    public Carrello getCarrello(HttpSession session) {
        if (session == null) {
            return new Carrello(); // Carrello temporaneo se non c'è sessione
        }

        // 1. Prova a recuperare dalla sessione
        Carrello carrelloInSessione = (Carrello) session.getAttribute(CARRELLO_SESSION_KEY);
        if (carrelloInSessione != null) {
            System.out.println("DEBUG getCarrello: Trovato in sessione, " + carrelloInSessione.getNumeroTotaleArticoli() + " prodotti");
            return carrelloInSessione;
        }

        // 2. Recupera dal database
        Integer userId = (Integer) session.getAttribute(USER_ID_SESSION_KEY);
        Carrello carrello = null;

        System.out.println("DEBUG getCarrello: User ID in sessione: " + userId);

        if (userId != null) {
            // Utente loggato - carica il suo carrello dal database
            System.out.println("DEBUG getCarrello: Caricando carrello per utente " + userId);
            Optional<Carrello> carrelloUtente = carrelloDao.getCarrelloByUtente(userId);
            if (carrelloUtente.isPresent()) {
                carrello = carrelloUtente.get();
                System.out.println("DEBUG getCarrello: Caricato dal DB, " + carrello.getNumeroTotaleArticoli() + " prodotti");

                // Salva l'ID carrello in sessione
                Optional<Integer> carrelloId = carrelloDao.getCarrelloIdByUtente(userId);
                if (carrelloId.isPresent()) {
                    session.setAttribute(CARRELLO_ID_SESSION_KEY, carrelloId.get());
                    System.out.println("DEBUG getCarrello: ID carrello salvato: " + carrelloId.get());
                }
            } else {
                System.out.println("DEBUG getCarrello: Nessun carrello trovato per utente, creando nuovo");
                // Crea nuovo carrello per utente
                Optional<Integer> nuovoCarrelloId = carrelloDao.creaCarrelloUtente(userId);
                if (nuovoCarrelloId.isPresent()) {
                    carrello = new Carrello();
                    session.setAttribute(CARRELLO_ID_SESSION_KEY, nuovoCarrelloId.get());
                    System.out.println("DEBUG getCarrello: Nuovo carrello creato con ID: " + nuovoCarrelloId.get());
                }
            }
        } else {
            // Utente anonimo - usa la sessione
            System.out.println("DEBUG getCarrello: Utente anonimo, session ID: " + session.getId());
            String sessionId = session.getId();
            Optional<Carrello> carrelloSessione = carrelloDao.getCarrelloBySessione(sessionId);

            if (carrelloSessione.isPresent()) {
                carrello = carrelloSessione.get();
                System.out.println("DEBUG getCarrello: Carrello sessione caricato, " + carrello.getNumeroTotaleArticoli() + " prodotti");
            } else {
                // Crea nuovo carrello per sessione - MA SOLO SE NON ESISTE GIA' IN SESSIONE
                carrello = new Carrello();
                System.out.println("DEBUG getCarrello: Nuovo carrello vuoto per sessione anonima");
                // Non creare immediatamente nel DB, aspetta il primo prodotto
            }
        }

        if (carrello == null) {
            carrello = new Carrello();
            System.out.println("DEBUG getCarrello: Fallback a carrello vuoto");
        }

        // Salva in sessione per accesso rapido
        session.setAttribute(CARRELLO_SESSION_KEY, carrello);
        System.out.println("DEBUG getCarrello: Salvato in sessione, totale finale: " + carrello.getNumeroTotaleArticoli() + " prodotti");
        return carrello;
    }

    /**
     * Assicura che esista un carrello nel database per la sessione
     */
    private Integer assicuraCarrelloInDB(HttpSession session) {
        Integer carrelloId = (Integer) session.getAttribute(CARRELLO_ID_SESSION_KEY);
        if (carrelloId != null) {
            return carrelloId; // Esiste già
        }

        Integer userId = (Integer) session.getAttribute(USER_ID_SESSION_KEY);

        if (userId != null) {
            // Utente loggato
            Optional<Integer> id = carrelloDao.getCarrelloIdByUtente(userId);
            if (id.isPresent()) {
                carrelloId = id.get();
            } else {
                Optional<Integer> nuovoId = carrelloDao.creaCarrelloUtente(userId);
                carrelloId = nuovoId.orElse(null);
            }
        } else {
            // Utente anonimo
            String sessionId = session.getId();
            Optional<Integer> nuovoId = carrelloDao.creaCarrelloSessione(sessionId);
            carrelloId = nuovoId.orElse(null);
        }

        if (carrelloId != null) {
            session.setAttribute(CARRELLO_ID_SESSION_KEY, carrelloId);
        }

        return carrelloId;
    }

    /**
     * Aggiunge un prodotto al carrello (sessione + database)
     */
    public boolean aggiungiProdotto(HttpSession session, Prodotto prodotto, int quantita) {
        if (session == null || prodotto == null || quantita <= 0) {
            return false;
        }

        // 1. Assicura che esista un carrello nel database
        Integer carrelloId = assicuraCarrelloInDB(session);
        if (carrelloId == null) {
            System.err.println("ERRORE: Impossibile creare carrello nel database");
            return false;
        }

        // 2. Aggiorna il carrello in sessione
        Carrello carrello = getCarrello(session);
        boolean sessionSuccess = carrello.addProdotto(prodotto, quantita);

        if (!sessionSuccess) {
            return false;
        }

        // 3. Aggiorna il database
        boolean dbSuccess = carrelloDao.aggiungiProdotto(carrelloId, prodotto.getIdProdotto(), quantita);

        if (!dbSuccess) {
            System.err.println("ERRORE: Fallito salvataggio prodotto nel database");
            // Rollback sessione se DB fallisce
            carrello.rimuoviProdotto(prodotto.getIdProdotto());
            return false;
        }

        // 4. Aggiorna la sessione
        session.setAttribute(CARRELLO_SESSION_KEY, carrello);

        System.out.println("DEBUG: Aggiunto prodotto " + prodotto.getIdProdotto() +
                " al carrello " + carrelloId + " (qty: " + quantita + ")");

        return true;
    }

    /**
     * Rimuove un prodotto dal carrello (sessione + database)
     */
    public boolean rimuoviProdotto(HttpSession session, int idProdotto) {
        if (session == null) {
            return false;
        }

        // 1. Aggiorna il carrello in sessione
        Carrello carrello = getCarrello(session);
        boolean sessionSuccess = carrello.rimuoviProdotto(idProdotto);

        // 2. Aggiorna il database
        Integer carrelloId = (Integer) session.getAttribute(CARRELLO_ID_SESSION_KEY);
        boolean dbSuccess = true;

        if (carrelloId != null) {
            dbSuccess = carrelloDao.rimuoviProdotto(carrelloId, idProdotto);
        }

        // 3. Aggiorna la sessione
        session.setAttribute(CARRELLO_SESSION_KEY, carrello);

        return sessionSuccess && dbSuccess;
    }

    /**
     * Aggiorna la quantità di un prodotto (sessione + database)
     */
    public boolean aggiornaQuantita(HttpSession session, int idProdotto, int nuovaQuantita) {
        if (session == null) {
            return false;
        }

        // 1. Aggiorna il carrello in sessione
        Carrello carrello = getCarrello(session);
        boolean sessionSuccess = carrello.aggiornaQuantita(idProdotto, nuovaQuantita);

        // 2. Aggiorna il database
        Integer carrelloId = (Integer) session.getAttribute(CARRELLO_ID_SESSION_KEY);
        boolean dbSuccess = true;

        if (carrelloId != null) {
            dbSuccess = carrelloDao.aggiornaQuantita(carrelloId, idProdotto, nuovaQuantita);
        }

        // 3. Aggiorna la sessione
        session.setAttribute(CARRELLO_SESSION_KEY, carrello);

        return sessionSuccess && dbSuccess;
    }

    /**
     * Restituisce il numero totale di articoli nel carrello
     */
    public int getNumeroArticoli(HttpSession session) {
        if (session == null) {
            return 0;
        }

        Carrello carrello = getCarrello(session);
        return carrello.getNumeroTotaleArticoli();
    }

    /**
     * Restituisce il totale del carrello
     */
    public double getTotaleCarrello(HttpSession session) {
        if (session == null) {
            return 0.0;
        }

        Carrello carrello = getCarrello(session);
        return carrello.total();
    }

    /**
     * Verifica se il carrello è vuoto
     */
    public boolean isCarrelloVuoto(HttpSession session) {
        if (session == null) {
            return true;
        }

        Carrello carrello = getCarrello(session);
        return carrello.isEmpty();
    }

    /**
     * Svuota completamente il carrello (sessione + database)
     */
    public void svuotaCarrello(HttpSession session) {
        if (session != null) {
            // 1. Svuota in sessione
            Carrello carrello = getCarrello(session);
            carrello.svuota();

            // 2. Svuota nel database
            Integer carrelloId = (Integer) session.getAttribute(CARRELLO_ID_SESSION_KEY);
            if (carrelloId != null) {
                carrelloDao.svuotaCarrello(carrelloId);
            }

            // 3. Aggiorna la sessione
            session.setAttribute(CARRELLO_SESSION_KEY, carrello);
        }
    }

    /**
     * Trasferisce il carrello da sessione anonima a utente loggato
     * VERSIONE SEMPLIFICATA
     */
    public void collegaCarrelloAdUtente(HttpSession session, int idUtente) {
        if (session == null) return;

        System.out.println("=== COLLEGAMENTO CARRELLO ===");
        System.out.println("User ID: " + idUtente);

        // 1. Recupera il carrello attuale dalla sessione
        Carrello carrelloCorrente = (Carrello) session.getAttribute(CARRELLO_SESSION_KEY);

        if (carrelloCorrente == null || carrelloCorrente.isEmpty()) {
            System.out.println("Nessun carrello da trasferire");
            // Imposta comunque l'user_id per future operazioni
            session.setAttribute(USER_ID_SESSION_KEY, idUtente);
            return;
        }

        System.out.println("Carrello da trasferire: " + carrelloCorrente.getNumeroTotaleArticoli() + " prodotti");

        // 2. Salva l'ID utente in sessione
        session.setAttribute(USER_ID_SESSION_KEY, idUtente);

        // 3. Assicura che l'utente abbia un carrello nel database
        Optional<Integer> userCartId = carrelloDao.getCarrelloIdByUtente(idUtente);
        if (userCartId.isEmpty()) {
            userCartId = carrelloDao.creaCarrelloUtente(idUtente);
        }

        if (userCartId.isEmpty()) {
            System.err.println("ERRORE: Impossibile creare carrello utente");
            return;
        }

        // 4. Trasferisci tutti i prodotti del carrello sessione al carrello utente nel DB
        for (var item : carrelloCorrente.getItems()) {
            boolean success = carrelloDao.aggiungiProdotto(
                    userCartId.get(),
                    item.getProdotto().getIdProdotto(),
                    item.getQuantita()
            );
            System.out.println("Trasferito prodotto " + item.getProdotto().getIdProdotto() +
                    " qty " + item.getQuantita() + ": " + success);
        }

        // 5. Pulisci la sessione e ricarica dal database utente
        session.removeAttribute(CARRELLO_SESSION_KEY);
        session.setAttribute(CARRELLO_ID_SESSION_KEY, userCartId.get());

        // 6. Ricarica il carrello dal database
        Carrello nuovoCarrello = getCarrello(session);
        System.out.println("Nuovo carrello caricato: " + nuovoCarrello.getNumeroTotaleArticoli() + " prodotti");
        System.out.println("=== FINE COLLEGAMENTO ===");
    }

    /**
     * Sincronizza il carrello dalla sessione al database
     * Utile per chiamate periodiche o su eventi specifici
     */
    public void sincronizzaCarrello(HttpSession session) {
        if (session == null) {
            return;
        }

        Carrello carrello = (Carrello) session.getAttribute(CARRELLO_SESSION_KEY);
        Integer carrelloId = (Integer) session.getAttribute(CARRELLO_ID_SESSION_KEY);

        if (carrello != null && carrelloId != null) {
            // Svuota il carrello nel DB e risalva tutto
            carrelloDao.svuotaCarrello(carrelloId);

            for (CarrelloItem item : carrello.getItems()) {
                carrelloDao.aggiungiProdotto(carrelloId,
                        item.getProdotto().getIdProdotto(),
                        item.getQuantita());
            }
        }
    }
}