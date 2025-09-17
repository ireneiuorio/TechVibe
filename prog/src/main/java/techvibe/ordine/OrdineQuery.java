package techvibe.ordine;

import techvibe.storage.QueryBuilder;

public class OrdineQuery {
    // Usa la tabella reale "ordine" con alias "ord"
    private static final String ORDINE_TABLE = "ordine";
    private static final String ORDINE_ALIAS = "ord";

    static String fetchOrdini() {
        QueryBuilder builder = new QueryBuilder(ORDINE_TABLE, ORDINE_ALIAS);
        builder.select()
                .innerJoin("utente", "ute").on("ute.idaccount = ord.idaccount")
                .limit(true);
        return builder.generateQuery();
    }

    static String fetchOrdineConProdotti() {
        QueryBuilder builder = new QueryBuilder(ORDINE_TABLE, ORDINE_ALIAS);
        builder.select()
                .innerJoin("composizioneordine", "com").on("com.idordine = ord.idordine")  // Corretto
                .innerJoin("prodotto", "pro").on("pro.idprodotto = com.idprodotto")              // Corretto
                .outerJoin(true, "categoria", "cat").on("cat.idcategoria = pro.idcategoria")
                .where("ord.idaccount = ?")
                .limit(true);
        return builder.generateQuery();
    }

    static String createOrdine() {
        QueryBuilder builder = new QueryBuilder(ORDINE_TABLE, ORDINE_ALIAS);
        builder.insert("idaccount", "stato", "totale", "scontototale", "metodospedizione", "metodopagamento");  // CAMBIA da utente_fk a idaccount
        return builder.generateQuery();
    }
    static String fetchOrdine() {
        // SELECT ord.* FROM ordine ord WHERE ord.id = ?
        QueryBuilder builder = new QueryBuilder(ORDINE_TABLE, ORDINE_ALIAS);
        builder.select().where("ord.idordine = ?");
        return builder.generateQuery();
    }


    static String insertCart() {
        // INSERT INTO ordine_prodotto (prodotto_fk, ordine_fk, quantita) VALUES (?, ?, ?)
        QueryBuilder builder = new QueryBuilder("composizioneordine", "com");
        builder.insert("idprodotto", "idordine", "quantita");
        return builder.generateQuery();
    }
}
