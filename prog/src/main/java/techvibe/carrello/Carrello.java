package techvibe.carrello;

import techvibe.prodotto.Prodotto;
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

    /**
     * Aggiunge un prodotto al carrello.
     * Se il prodotto esiste già, aumenta la quantità.
     * Se non esiste, crea un nuovo item.
     */
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

    /**
     * Rimuove un prodotto completamente dal carrello
     */
    public boolean rimuoviProdotto(int idProdotto) {
        return items.removeIf(item -> item.getProdotto().getIdProdotto() == idProdotto);
    }

    /**
     * Aggiorna la quantità di un prodotto specifico
     */
    public boolean aggiornaQuantita(int idProdotto, int nuovaQuantita) {
        System.out.println("=== DEBUG AGGIORNA QUANTITA ===");
        System.out.println("ID Prodotto: " + idProdotto);
        System.out.println("Nuova quantità: " + nuovaQuantita);

        if (nuovaQuantita <= 0) {
            return rimuoviProdotto(idProdotto);
        }

        for (CarrelloItem item : items) {
            if (item.getProdotto().getIdProdotto() == idProdotto) {
                System.out.println("Quantità prima: " + item.getQuantita());
                item.setQuantita(nuovaQuantita);
                System.out.println("Quantità dopo: " + item.getQuantita());
                return true;
            }
        }
        return false;
    }

    /**
     * Calcola il totale del carrello
     */
    public double total() {
        return items.stream()
                .mapToDouble(CarrelloItem::total)
                .sum();
    }

    /**
     * Conta il numero totale di articoli nel carrello
     */
    public int getNumeroTotaleArticoli() {
        return items.stream()
                .mapToInt(CarrelloItem::getQuantita)
                .sum();
    }

    /**
     * Verifica se il carrello è vuoto
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Svuota completamente il carrello
     */
    public void svuota() {
        items.clear();
    }

    public List<CarrelloItem> getItems() {
        return items;
    }
}