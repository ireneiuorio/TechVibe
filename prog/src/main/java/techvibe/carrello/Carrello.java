package techvibe.carrello;

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





}
