package techvibe.Model.carrello;

import techvibe.prodotto.Prodotto;

public class CarrelloItem {

//Gli item contenuti all'interno del carrello
    private final Prodotto prodotto;
    private int quantita;

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