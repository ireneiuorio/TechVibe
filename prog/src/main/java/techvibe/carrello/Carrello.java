package techvibe.carrello;

import techvibe.prodotto.Prodotto;

import java.util.List;

public class Carrello {

    private List<CarrelloItem> items;

    public Carrello(List<CarrelloItem> items) {
        this.items = items;
    }

    public double total() {
        double total=0;
        for(CarrelloItem c:items)
        {
            total+=c.total();

        }
        return total;
    }

    public boolean addProdotto(Prodotto prodotto,int quantita)
    {
        return items.add(new CarrelloItem(prodotto,quantita));
    }

    public List<CarrelloItem> getItems() {
        return items;
    }




}
