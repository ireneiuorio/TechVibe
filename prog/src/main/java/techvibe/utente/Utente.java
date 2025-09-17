package techvibe.utente;

import techvibe.ordine.Ordine;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

public class Utente {

    private int IdUtente;
    private String Nome;
    private String Cognome;
    private String Email;
    private String Password;
    private String Telefono;
    private String IndirizzoSpedizione;
    private boolean admin;
    private String stato = "ATTIVO"; // Default: utente attivo
    private List<Ordine> ordini;

    public Utente(){
        super();
    }

    public int getIdUtente() {
        return IdUtente;
    }

    public String getNome() {
        return Nome;
    }

    public String getCognome() {
        return Cognome;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getTelefono() {
        return Telefono;
    }

    public String getIndirizzoSpedizione() {
        return IndirizzoSpedizione;
    }

    public void setIdUtente(int idAccount) {
        IdUtente = idAccount;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public void setCognome(String cognome) {
        Cognome = cognome;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest=MessageDigest.getInstance("SHA-512");
        byte[] hashedPwd =digest.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder builder=new StringBuilder();
        for(byte bit:hashedPwd)
        {
            builder.append(String.format("%02x",bit));
        }
        this.Password=builder.toString();
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public void setIndirizzoSpedizione(String indirizzoSpedizione) {
        IndirizzoSpedizione = indirizzoSpedizione;
    }

    public List<Ordine> getOrdini() {
        return ordini;
    }

    public void setOrdini(List<Ordine> ordini) {
        this.ordini = ordini;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setPasswordHash(String alreadyHashed) {
        this.Password = alreadyHashed; // non ricalcola l'hash
    }

    // Nuovi metodi per la gestione dello stato
    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    // Metodi di utilità per controllare lo stato
    public boolean isAttivo() {
        return "ATTIVO".equals(stato);
    }

    public boolean isDisattivato() {
        return "DISATTIVATO".equals(stato);
    }

    // Metodi per cambiare stato facilmente
    public void attiva() {
        this.stato = "ATTIVO";
    }

    public void disattiva() {
        this.stato = "DISATTIVATO";
    }
}