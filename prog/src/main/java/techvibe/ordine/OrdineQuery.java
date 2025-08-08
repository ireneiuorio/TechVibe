package techvibe.ordine;

import techvibe.storage.QueryBuilder;

public class OrdineQuery {
    private static final String ORDINE_QUERY="purchase";
    private static final String ORDINE_ALIAS="pur";

    static String fetchOrdini(){
        QueryBuilder builder=new QueryBuilder(ORDINE_QUERY,ORDINE_ALIAS);
        builder.select().limit(true);
        return builder.generateQuery();

    }

    static String fetchOrdineConProdotti(){
        QueryBuilder builder=new QueryBuilder(ORDINE_QUERY,ORDINE_ALIAS);
        builder.select().innerJoin("ordine","ord").on("op.ordine_fk=ord.id");
        builder.innerJoin("prodotto","pro").on("op.prodotto_fk=pro.id");
        builder.outerJoin(true,"categoria","cat").on("cat.id= pro.categoria_fk");
        builder.where("ord.utente_fk=?");
        builder.limit(true);
        return builder.generateQuery();

    }

    static String fetchOrdine(){
        QueryBuilder builder =new QueryBuilder(ORDINE_QUERY,ORDINE_ALIAS);
        builder.select().where("pur.id=?");
        return builder.generateQuery();
    }

    static String createOrdine(){
        QueryBuilder builder=new QueryBuilder(ORDINE_QUERY,ORDINE_ALIAS);
        builder.insert("total","created");
        return builder.generateQuery();
    }

    static String insertCart(){
        QueryBuilder builder=new QueryBuilder("ordine_prodotto","op");
        builder.insert("prodotto_fk","ordine_fk","quantita");
        return builder.generateQuery();
    }

}

