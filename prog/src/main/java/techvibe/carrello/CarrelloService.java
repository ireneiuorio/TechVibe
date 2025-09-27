package techvibe.carrello;

import techvibe.prodotto.Prodotto;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

public class CarrelloService {

    // Chiavi per salvare informazioni in sessione
    private static final String CARRELLO_SESSION_KEY = "carrello";
    private static final String CARRELLO_ID_SESSION_KEY = "carrello_id";
    private static final String USER_ID_SESSION_KEY = "user_id";

    private final CarrelloDao carrelloDao;

    public CarrelloService(CarrelloDao carrelloDao) {
        this.carrelloDao = carrelloDao;
    }

    //Recupera il carrello dalla sessione o dal database.

    public Carrello getCarrello(HttpSession session) {
        if (session == null) {
            return new Carrello(); // Carrello temporaneo se non c'è sessione
        }

        // 1. Prova a recuperare dalla sessione
        Carrello carrelloInSessione = (Carrello) session.getAttribute(CARRELLO_SESSION_KEY);
        if (carrelloInSessione != null) {
            return carrelloInSessione;
        }

        // 2. Recupera dal database
        Integer userId = (Integer) session.getAttribute(USER_ID_SESSION_KEY);
        Carrello carrello = null;


        if (userId != null) {
            // Utente loggato - carica il suo carrello dal database
            Optional<Carrello> carrelloUtente = carrelloDao.getCarrelloByUtente(userId);
            if (carrelloUtente.isPresent()) {
                carrello = carrelloUtente.get();

                // Salva l'ID carrello in sessione
                Optional<Integer> carrelloId = carrelloDao.getCarrelloIdByUtente(userId);
                if (carrelloId.isPresent()) {
                    session.setAttribute(CARRELLO_ID_SESSION_KEY, carrelloId.get());

                }
            } else {
                // Crea nuovo carrello per utente
                Optional<Integer> nuovoCarrelloId = carrelloDao.creaCarrelloUtente(userId);
                if (nuovoCarrelloId.isPresent()) {
                    carrello = new Carrello();
                    session.setAttribute(CARRELLO_ID_SESSION_KEY, nuovoCarrelloId.get());
                }
            }
        } else {
            // Utente anonimo - usa la sessione
            String sessionId = session.getId();
            Optional<Carrello> carrelloSessione = carrelloDao.getCarrelloBySessione(sessionId);

            if (carrelloSessione.isPresent()) {
                carrello = carrelloSessione.get();


                // Salva l'ID carrello per le operazioni future
                Optional<Integer> carrelloId = getCarrelloIdBySessione(sessionId);
                if (carrelloId.isPresent()) {
                    session.setAttribute(CARRELLO_ID_SESSION_KEY, carrelloId.get());
                }
            } else {
                // Crea nuovo carrello per sessione
                carrello = new Carrello();

            }
        }

        if (carrello == null) {
            carrello = new Carrello();

        }

        // Salva in sessione per accesso rapido
        session.setAttribute(CARRELLO_SESSION_KEY, carrello);
        return carrello;
    }

    /**
     * Helper method per ottenere l'ID carrello per sessione
     */
    private Optional<Integer> getCarrelloIdBySessione(String sessionId) {
        return Optional.empty(); // Per ora placeholder
    }

     //Metodo chiamato quando un utente fa LOGIN
     //Trasferisce il carrello anonimo al carrello utente
    public boolean onUserLogin(HttpSession session, int idUtente) {
        if (session == null) return false;

        // 1. Verifica se c'è un carrello anonimo da trasferire
        String sessionId = session.getId();
        Optional<Carrello> carrelloAnonimo = carrelloDao.getCarrelloBySessione(sessionId);

        // 2. Usa il metodo del DAO per trasferire (più robusto)
        boolean transferSuccess = carrelloDao.trasferisciCarrello(sessionId, idUtente);

        if (transferSuccess) {
            System.out.println("Trasferimento completato con successo");

            // 3. Pulisci la sessione e imposta l'utente
            session.removeAttribute(CARRELLO_SESSION_KEY);
            session.removeAttribute(CARRELLO_ID_SESSION_KEY);
            session.setAttribute(USER_ID_SESSION_KEY, idUtente);

            // 4. Ricarica il carrello dal database dell'utente
            Carrello nuovoCarrello = getCarrello(session);
            System.out.println("Carrello post-login: " + nuovoCarrello.getNumeroTotaleArticoli() + " prodotti");

            return true;
        } else {
            System.out.println("Errore nel trasferimento, fallback a collegamento semplice");
            // Fallback al metodo precedente
            session.setAttribute(USER_ID_SESSION_KEY, idUtente);
            return true;
        }
    }

    /**
     * Metodo chiamato quando un utente fa LOGOUT
     * Pulisce la sessione ma mantiene il carrello nel database
     */
    public void onUserLogout(HttpSession session) {
        if (session == null) return;

        System.out.println("=== LOGOUT UTENTE ===");
        Integer userId = (Integer) session.getAttribute(USER_ID_SESSION_KEY);
        System.out.println("Logout user ID: " + userId);

        if (userId != null) {
            // 1. Sincronizza il carrello finale nel database prima del logout
            sincronizzaCarrello(session);
            System.out.println("Carrello sincronizzato prima del logout");
        }

        // 2. Pulisci tutti i dati utente dalla sessione
        session.removeAttribute(USER_ID_SESSION_KEY);
        session.removeAttribute(CARRELLO_SESSION_KEY);
        session.removeAttribute(CARRELLO_ID_SESSION_KEY);

        System.out.println("Sessione pulita dopo logout");
        System.out.println("=== FINE LOGOUT ===");
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
            System.out.println("Sincronizzando carrello ID: " + carrelloId);

            // Svuota il carrello nel DB e risalva tutto
            carrelloDao.svuotaCarrello(carrelloId);

            for (CarrelloItem item : carrello.getItems()) {
                carrelloDao.aggiungiProdotto(carrelloId,
                        item.getProdotto().getIdProdotto(),
                        item.getQuantita());
            }

            System.out.println("Sincronizzazione completata");
        }
    }

    /**
     * METODO DEPRECATO - usa onUserLogin() invece
     */
    @Deprecated
    public void collegaCarrelloAdUtente(HttpSession session, int idUtente) {
        onUserLogin(session, idUtente);
    }
}