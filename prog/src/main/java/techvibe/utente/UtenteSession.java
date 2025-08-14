package techvibe.utente;

public class UtenteSession {
    private final String nome;
    private final String cognome;
    private final int id;
    private final boolean isAdmin;

    public UtenteSession(Utente utente) {
        this.nome = utente.getNome();
        this.cognome = utente.getCognome();
        this.id = utente.getIdUtente();
        this.isAdmin =utente.isAdmin();
    }



    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public int getId() {
        return id;
    }

    public boolean isAdmin() {
        return isAdmin;
    }


}
