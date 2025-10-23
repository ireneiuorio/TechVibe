package techvibe.Model.ordine;

import techvibe.Model.carrello.Carrello;
import techvibe.Model.categoria.Categoria;
import techvibe.Model.utente.Utente;

public class Ordine {
    private int IdOrdine;
    private String stato;
    private double Totale;
    private double ScontoTotale;
    private String MetodoDiSpedizione;
    private String MetodoDiPagamento;
    private Utente utente;
    private Categoria categoria;
    private Carrello carrello;


    public Ordine() {
    }

    public int getIdOrdine() {
        return IdOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        IdOrdine = idOrdine;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public double getTotale() {
        return Totale;
    }

    public void setTotale(double totale) {
        Totale = totale;
    }

    public double getScontoTotale() {
        return ScontoTotale;
    }

    public void setScontoTotale(double scontoTotale) {
        ScontoTotale = scontoTotale;
    }

    public String getMetodoDiSpedizione() {
        return MetodoDiSpedizione;
    }

    public void setMetodoDiSpedizione(String metodoDiSpedizione) {
        MetodoDiSpedizione = metodoDiSpedizione;
    }

    public String getMetodoDiPagamento() {
        return MetodoDiPagamento;
    }

    public void setMetodoDiPagamento(String metodoDiPagamento) {
        MetodoDiPagamento = metodoDiPagamento;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Carrello getCarrello() {
        return carrello;
    }

    public void setCarrello(Carrello carrello) {
        this.carrello = carrello;
    }


}
