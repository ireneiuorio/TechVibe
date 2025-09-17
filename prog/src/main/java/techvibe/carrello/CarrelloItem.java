package techvibe.carrello;

import techvibe.prodotto.Prodotto;

public class CarrelloItem {

    private final Prodotto prodotto;
    private int quantita; // Rimosso final per permettere modifiche

    public CarrelloItem(Prodotto prodotto, int quantita) {
        this.prodotto = prodotto;
        this.quantita = quantita;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public void aggiungiQuantita(int quantitaDaAggiungere) {
        this.quantita += quantitaDaAggiungere;
    }

    public double total() {
        return prodotto.getPrezzo() * quantita;
    }
}