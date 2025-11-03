package techvibe.Model.carrello;

import techvibe.Model.prodotto.Prodotto;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

public class CarrelloService {

    // Chiavi per salvare informazioni in sessione, lo metto con questo nome in sessione
    private static final String CARRELLO_SESSION_KEY = "carrello"; //oggetto carrello
    private static final String CARRELLO_ID_SESSION_KEY = "carrello_id";//id del carrello nel DB
    private static final String USER_ID_SESSION_KEY = "user_id"; //id dell'utente loggato



    private final SqlCarrelloDao sqlCarrelloDao;



    public CarrelloService(SqlCarrelloDao sqlCarrelloDao) {
        this.sqlCarrelloDao = sqlCarrelloDao;
    }



    //Recupera un carrello dal DB o dalla sessione
    public Carrello getCarrello(HttpSession session) {

        if (session == null) {
            return new Carrello();
        }

        //Se c'è un carrello in sessione lo ritorniamo subito
        Carrello carrelloInSessione = (Carrello) session.getAttribute(CARRELLO_SESSION_KEY);
        if (carrelloInSessione != null) {
            return carrelloInSessione;
        }

        //l'utente è loggato?
        Integer userId = (Integer) session.getAttribute(USER_ID_SESSION_KEY);
        Carrello carrello = null;


        //LOGGATO
        if (userId != null) {

            //Prende il carrello dal DB
            Optional<Carrello> carrelloUtente = sqlCarrelloDao.getCarrelloByUtente(userId);
            if (carrelloUtente.isPresent()) {
                carrello = carrelloUtente.get();

                //Lo usa e salva in sessione l'id del carrello
                Optional<Integer> carrelloId = sqlCarrelloDao.getCarrelloIdByUtente(userId);
                if (carrelloId.isPresent()) {
                    session.setAttribute(CARRELLO_ID_SESSION_KEY, carrelloId.get());

                }
            } else { //se non c'è ne crea uno nuovo nel DB, mette un carrello vuoto in memoria e salva l’ID in sessione.

                Optional<Integer> nuovoCarrelloId = sqlCarrelloDao.creaCarrelloUtente(userId);
                if (nuovoCarrelloId.isPresent()) {
                    carrello = new Carrello();
                    session.setAttribute(CARRELLO_ID_SESSION_KEY, nuovoCarrelloId.get());
                }
            }
        } else { //NON LOGGATO

            //Usa l'id della sessione e crea un carrello nel DB
            String sessionId = session.getId();
            Optional<Carrello> carrelloSessione = sqlCarrelloDao.getCarrelloBySessione(sessionId);

            //C'è lo carica e salva l'id della sessione
            if (carrelloSessione.isPresent()) {
                carrello = carrelloSessione.get();



                Optional<Integer> carrelloId = getCarrelloIdBySessione(sessionId);
                if (carrelloId.isPresent()) {
                    session.setAttribute(CARRELLO_ID_SESSION_KEY, carrelloId.get());
                }
            } else {
                //crea un carrello nuovo se non c'è
                carrello = new Carrello();

            }
        }

        //ulteriore controllo null
        if (carrello == null) {
            carrello = new Carrello();

        }

        // Salva in sessione il carrello e lo restituisce
        session.setAttribute(CARRELLO_SESSION_KEY, carrello);
        return carrello;
    }




    //Chiede al DAO l'id del carrello legato alla sessione anonima
    private Optional<Integer> getCarrelloIdBySessione(String sessionId) {
        return sqlCarrelloDao.getCarrelloIdBySessione(sessionId);
    }

    // Quando l'utente fa LOGIN
    public boolean onUserLogin(HttpSession session, int idUtente) {
        if (session == null) return false;

        String sessionId = session.getId();

        try {
            // Recupera carrello anonimo (sessione)
            Optional<Carrello> carrelloAnonimoOpt = sqlCarrelloDao.getCarrelloBySessione(sessionId);
            Carrello carrelloAnonimo = carrelloAnonimoOpt.orElse(new Carrello());

            //Recupera carrello dell'utente dal DB se esiste
            Optional<Carrello> carrelloUtenteOpt = sqlCarrelloDao.getCarrelloByUtente(idUtente);
            Carrello carrelloUtente = carrelloUtenteOpt.orElse(new Carrello());

            //Merge: somma quantità per stesso prodotto se è in entrambi
            for (CarrelloItem itemAnonimo : carrelloAnonimo.getItems()) {
                boolean trovato = false;
                for (CarrelloItem itemUtente : carrelloUtente.getItems()) {
                    if (itemUtente.getProdotto().getIdProdotto() == itemAnonimo.getProdotto().getIdProdotto()) {
                        itemUtente.setQuantita(itemUtente.getQuantita() + itemAnonimo.getQuantita());
                        trovato = true;
                        break;
                    }
                }
                if (!trovato) {
                    carrelloUtente.addProdotto(itemAnonimo.getProdotto(), itemAnonimo.getQuantita());
                }
            }

            //Svuota il carrello utente nel DB (lo riscriveremo da zero)
            Optional<Integer> carrelloUtenteIdOpt = sqlCarrelloDao.getCarrelloIdByUtente(idUtente);
            int carrelloUtenteId;

            if (carrelloUtenteIdOpt.isPresent()) {
                carrelloUtenteId = carrelloUtenteIdOpt.get();
                sqlCarrelloDao.svuotaCarrello(carrelloUtenteId);
            } else {
                // se non esiste, lo crea
                carrelloUtenteId = sqlCarrelloDao.creaCarrelloUtente(idUtente).orElseThrow();
            }

            //Riscrivi i prodotti unificati nel carrello utente
            for (CarrelloItem item : carrelloUtente.getItems()) {
                sqlCarrelloDao.aggiungiProdotto(carrelloUtenteId,
                        item.getProdotto().getIdProdotto(),
                        item.getQuantita());
            }

            // Svuota anche il carrello anonimo (se aveva un id)
            Optional<Integer> carrelloAnonimoIdOpt = sqlCarrelloDao.getCarrelloIdBySessione(sessionId);
            if (carrelloAnonimoIdOpt.isPresent()) {
                sqlCarrelloDao.svuotaCarrello(carrelloAnonimoIdOpt.get());
            }

            //Aggiorna la sessione con i dati loggati e il carrello finale
            session.setAttribute(USER_ID_SESSION_KEY, idUtente);
            session.setAttribute(CARRELLO_SESSION_KEY, carrelloUtente);
            session.setAttribute(CARRELLO_ID_SESSION_KEY, carrelloUtenteId);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    //Logout
    public void onUserLogout(HttpSession session) {
        if (session == null) return;


        Integer userId = (Integer) session.getAttribute(USER_ID_SESSION_KEY);

        //Sincronizza il carrello finale nel database prima del logout
        if (userId != null) {
            sincronizzaCarrello(session);
            System.out.println("Carrello sincronizzato prima del logout");
        }

        // 2. Pulisci tutti i dati utente dalla sessione
        session.removeAttribute(USER_ID_SESSION_KEY);
        session.removeAttribute(CARRELLO_SESSION_KEY);
        session.removeAttribute(CARRELLO_ID_SESSION_KEY);

    }

    //Si assicura che esista un carrello associato a un utente nel DB e mi da il suo id
    private Integer assicuraCarrelloInDB(HttpSession session) {
        Integer carrelloId = (Integer) session.getAttribute(CARRELLO_ID_SESSION_KEY);

        if (carrelloId != null) {
            return carrelloId;
        }

        //Controlliamo se l'utente è loggato
        Integer userId = (Integer) session.getAttribute(USER_ID_SESSION_KEY);
        if (userId != null) {
            //chiede al DB se esiste un carrello associato a quell'utente
            Optional<Integer> id = sqlCarrelloDao.getCarrelloIdByUtente(userId);
            if (id.isPresent()) {
                carrelloId = id.get();
            } else {
                Optional<Integer> nuovoId = sqlCarrelloDao.creaCarrelloUtente(userId);
                carrelloId = nuovoId.orElse(null);
            }
        } else {
            //Utente anonimo, si usa l'id della sessione per creare un carrello nel database
            String sessionId = session.getId();
            Optional<Integer> nuovoId = sqlCarrelloDao.creaCarrelloSessione(sessionId);
            carrelloId = nuovoId.orElse(null);
        }

        //salva l'id del carrello nella sessione
        if (carrelloId != null) {
            session.setAttribute(CARRELLO_ID_SESSION_KEY, carrelloId);
        }

        return carrelloId;
    }


    public boolean aggiungiProdotto(HttpSession session, Prodotto prodotto, int quantita) {
        if (session == null || prodotto == null || quantita <= 0) {
            return false;
        }

        //Assicura che esista un carrello nel database
        Integer carrelloId = assicuraCarrelloInDB(session);
        if (carrelloId == null) {
            System.err.println("ERRORE: Impossibile creare carrello nel database");
            return false;
        }

        //Aggiorna il carrello in sessione
        Carrello carrello = getCarrello(session);
        boolean sessionSuccess = carrello.addProdotto(prodotto, quantita);

        if (!sessionSuccess) {
            return false;
        }

        // Aggiorna il database
        boolean dbSuccess = sqlCarrelloDao.aggiungiProdotto(carrelloId, prodotto.getIdProdotto(), quantita);

        if (!dbSuccess) {
            System.err.println("ERRORE: Fallito salvataggio prodotto nel database");

            // Rollback sessione se DB fallisce
            carrello.rimuoviProdotto(prodotto.getIdProdotto());
            return false;
        }

        // Aggiorna la sessione
        session.setAttribute(CARRELLO_SESSION_KEY, carrello);

        return true;
    }


    public boolean rimuoviProdotto(HttpSession session, int idProdotto) {
        if (session == null) {
            return false;
        }

        // Aggiorna il carrello in sessione
        Carrello carrello = getCarrello(session);
        boolean sessionSuccess = carrello.rimuoviProdotto(idProdotto);

        // Aggiorna il database
        Integer carrelloId = (Integer) session.getAttribute(CARRELLO_ID_SESSION_KEY);
        boolean dbSuccess = true;

        if (carrelloId != null) {
            dbSuccess = sqlCarrelloDao.rimuoviProdotto(carrelloId, idProdotto);
        }

        //Aggiorna la sessione
        session.setAttribute(CARRELLO_SESSION_KEY, carrello);

        return sessionSuccess && dbSuccess;
    }



    public boolean aggiornaQuantita(HttpSession session, int idProdotto, int nuovaQuantita) {
        if (session == null) {
            return false;
        }

        // Aggiorna il carrello in sessione
        Carrello carrello = getCarrello(session);
        boolean sessionSuccess = carrello.aggiornaQuantita(idProdotto, nuovaQuantita);

        // Aggiorna il database
        Integer carrelloId = (Integer) session.getAttribute(CARRELLO_ID_SESSION_KEY);
        boolean dbSuccess = true;

        if (carrelloId != null) {
            dbSuccess = sqlCarrelloDao.aggiornaQuantita(carrelloId, idProdotto, nuovaQuantita);
        }

        // Aggiorna la sessione
        session.setAttribute(CARRELLO_SESSION_KEY, carrello);

        return sessionSuccess && dbSuccess;
    }


    public int getNumeroArticoli(HttpSession session) {
        if (session == null) {
            return 0;
        }

        Carrello carrello = getCarrello(session);
        return carrello.getNumeroTotaleArticoli();
    }


    public double getTotaleCarrello(HttpSession session) {
        if (session == null) {
            return 0.0;
        }
        Carrello carrello = getCarrello(session);
        return carrello.total();


    }


    public boolean isCarrelloVuoto(HttpSession session) {
        if (session == null) {
            return true;
        }

        Carrello carrello = getCarrello(session);
        return carrello.isEmpty();
    }


    public void svuotaCarrello(HttpSession session) {
        if (session != null) {
            // Svuota in sessione
            Carrello carrello = getCarrello(session);
            carrello.svuota();

            // Svuota nel database
            Integer carrelloId = (Integer) session.getAttribute(CARRELLO_ID_SESSION_KEY);
            if (carrelloId != null) {
                sqlCarrelloDao.svuotaCarrello(carrelloId);
            }

            // Aggiorna la sessione
            session.setAttribute(CARRELLO_SESSION_KEY, carrello);
        }
    }



    //Allinea sessione e DB, prende il carrello in sessione e lo riscrive completamnete nel Database
    public void sincronizzaCarrello(HttpSession session) {
        if (session == null) {
            return;
        }

        Carrello carrello = (Carrello) session.getAttribute(CARRELLO_SESSION_KEY);
        Integer carrelloId = (Integer) session.getAttribute(CARRELLO_ID_SESSION_KEY);

        if (carrello != null && carrelloId != null) {
            System.out.println("Sincronizzando carrello ID: " + carrelloId);

            // Svuota il carrello nel DB e risalva tutto
            sqlCarrelloDao.svuotaCarrello(carrelloId);

            for (CarrelloItem item : carrello.getItems()) {
                sqlCarrelloDao.aggiungiProdotto(carrelloId,
                        item.getProdotto().getIdProdotto(),
                        item.getQuantita());
            }

            System.out.println("Sincronizzazione completata");
        }
    }

}