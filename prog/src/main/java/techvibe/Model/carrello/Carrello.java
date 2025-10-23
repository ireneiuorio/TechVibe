package techvibe.Model.carrello;

import techvibe.Model.prodotto.Prodotto;

import java.util.ArrayList;
import java.util.List;

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

    //Rimuove un prodotto dal carrello
    public boolean rimuoviProdotto(int idProdotto) {
        return items.removeIf(item -> item.getProdotto().getIdProdotto() == idProdotto);
    }

    //Aggiorna la quantità di uno specifico prodotto
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

    //Calcola il totale del carrello
    public double total() {
        return items.stream()
                .mapToDouble(CarrelloItem::total)
                .sum();
    }

    //Conta il numero totale di articoli nel carrello
    public int getNumeroTotaleArticoli() {
        return items.stream()
                .mapToInt(CarrelloItem::getQuantita)
                .sum();
    }

    //Verifica se il carrello è vuoto
    public boolean isEmpty() {
        return items.isEmpty();
    }

    //Svuota il carrello
    public void svuota() {
        items.clear();
    }

    //Restituisce gli items
    public List<CarrelloItem> getItems() {
        return items;
    }
}