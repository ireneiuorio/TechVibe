package techvibe.Model.carrello;

import techvibe.Model.prodotto.Prodotto;

import java.util.ArrayList;
import java.util.List;


//Carrello è una relazione uno a molti quindi utilizzo due tabelle nel DB: carrelli: contiene le info generali del carrello
//Carrello items contiene i prodotti dentro ogni carrello
public class Carrello {
    private List<CarrelloItem> items;

    public Carrello() {
        this.items = new ArrayList<>();
    }

    public Carrello(List<CarrelloItem> items) {
        this.items = items != null ? items : new ArrayList<>();
    }


    //Aggiunge il prodotto al carrello
    public boolean addProdotto(Prodotto prodotto, int quantita) {
        if (prodotto == null || quantita <= 0) {
            return false;
        }

        // Cerca se il prodotto è già nel carrello
        for (CarrelloItem item : items) {
            if (item.getProdotto().getIdProdotto() == prodotto.getIdProdotto()) {
                // Prodotto già presente, aggiorna la quantità
                item.aggiungiQuantita(quantita);
                return true;
            }
        }

        // Prodotto non presente, aggiungi nuovo item
        return items.add(new CarrelloItem(prodotto, quantita));
    }

    public boolean rimuoviProdotto(int idProdotto) {
        return items.removeIf(item -> item.getProdotto().getIdProdotto() == idProdotto);
    }


    public boolean aggiornaQuantita(int idProdotto, int nuovaQuantita) {
        if (nuovaQuantita <= 0) {
            return rimuoviProdotto(idProdotto);
        }

        for (CarrelloItem item : items) {
            if (item.getProdotto().getIdProdotto() == idProdotto) {
                item.setQuantita(nuovaQuantita);
                return true;
            }
        }
        return false;
    }

    public int getNumeroTotaleArticoli() {
        int totaleArticoli = 0;

        for (CarrelloItem item : items) {
            totaleArticoli += item.getQuantita(); // aggiunge la quantità di ogni prodotto
        }

        return totaleArticoli;
    }

    public double total() {
        double totale = 0.0;

        for (CarrelloItem item : items) {
            totale += item.total(); // somma il totale di ogni prodotto (prezzo * quantità)
        }

        return totale;
    }

    public double totalescontato() {
        double totale = 0.0;
        double sconto=0.0;

        for (CarrelloItem item : items) {
            totale += item.total(); // somma il totale di ogni prodotto (prezzo * quantità)
        }

        if(this.getNumeroTotaleArticoli()>10)
        {
            sconto=(totale*10)/100;
            totale-=sconto;
        }

        return totale;
    }







    public boolean isEmpty() {
        return items.isEmpty();
    }


    public void svuota() {
        items.clear();
    }


    public List<CarrelloItem> getItems() {
        return items;
    }
}