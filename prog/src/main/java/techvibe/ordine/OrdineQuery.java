package techvibe.ordine;

import techvibe.storage.QueryBuilder;

public class OrdineQuery {
    private static final String ORDINE_QUERY = "ordine";
    private static final String ORDINE_ALIAS = "ord";

    static String fetchOrdini() {
        QueryBuilder builder = new QueryBuilder(ORDINE_QUERY, ORDINE_ALIAS);
        builder.select().limit(true);
        return builder.generateQuery();
    }

    static String fetchOrdineConProdotti() {
        QueryBuilder builder = new QueryBuilder(ORDINE_QUERY, ORDINE_ALIAS);
        builder.select().innerJoin("ordine_prodotto", "op").on("op.idordine = ord.idordine");
        builder.innerJoin("prodotto", "pro").on("op.idprodotto = pro.idprodotto");
        builder.outerJoin(true, "categoria", "cat").on("cat.idcategoria = pro.idcategoria");
        builder.where("ord.idaccount = ?");
        builder.limit(true);
        return builder.generateQuery();
    }

    static String fetchOrdine() {
        QueryBuilder builder = new QueryBuilder(ORDINE_QUERY, ORDINE_ALIAS);
        builder.select().where("ord.idordine = ?");
        return builder.generateQuery();
    }

    static String createOrdine() {
        QueryBuilder builder = new QueryBuilder(ORDINE_QUERY, ORDINE_ALIAS);
        builder.insert("idaccount", "stato", "totale", "scontototale", "metodospedizione", "metodopagamento");
        return builder.generateQuery();
    }

    static String insertCart() {
        QueryBuilder builder = new QueryBuilder("ordine_prodotto", "op");
        builder.insert("idprodotto", "idordine", "quantita");
        return builder.generateQuery();
    }
}