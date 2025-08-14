package techvibe.ordine;

import techvibe.carrello.Carrello;
import techvibe.carrello.CarrelloItem;
import techvibe.categoria.Categoria;
import techvibe.categoria.CategoriaExtractor;
import techvibe.prodotto.Prodotto;
import techvibe.prodotto.ProdottoExtractor;
import techvibe.components.Paginator;
import techvibe.storage.QueryBuilder;
import techvibe.storage.SqlDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SqlOrdineDao extends SqlDao implements OrdineDao <SQLException> {

    public SqlOrdineDao(DataSource source) {
        super(source);
    }


    @Override
    public List<Ordine> fetchOrdineConProdotto(int IdUtente, Paginator paginator) throws SQLException {
        try (Connection conn = source.getConnection()) {

            String query=OrdineQuery.fetchOrdineConProdotti();
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                System.out.println(ps.toString());
                ps.setInt(1, IdUtente);
                ps.setInt(2, paginator.getOffset());
                ps.setInt(3, paginator.getLimit());
                ResultSet set = ps.executeQuery();

                Map<Integer, Ordine> ordineMap = new LinkedHashMap<>();
                OrdineExtractor ordineExtractor = new OrdineExtractor();
                ProdottoExtractor prodottoExtractor = new ProdottoExtractor();
                CategoriaExtractor categoriaExtractor = new CategoriaExtractor();

                while (set.next()) {
                    int ordineId = set.getInt("ord.id");
                    if (!ordineMap.containsKey(ordineId)) {
                        Ordine ordine = ordineExtractor.extract(set);
                        ordine.setCarrello(new Carrello(new ArrayList<>()));
                        ordineMap.put(ordineId, ordine);
                    }

                    Prodotto prodotto = prodottoExtractor.extract(set);
                    Categoria categoria = categoriaExtractor.extract(set);
                    prodotto.setCategoria(categoria);
                    ordineMap.get(ordineId).getCarrello().addProdotto(prodotto, set.getInt("op.quantita"));


                }

                return new ArrayList<>(ordineMap.values());

            }


        }
    }


    @Override
    public List<Ordine> fetchOrdini(Paginator paginator) throws SQLException {
        try (Connection conn = source.getConnection()) {
            String query=OrdineQuery.fetchOrdini();
            try(PreparedStatement ps=conn.prepareStatement(query))
            {
                ps.setInt(1,paginator.getOffset());
                ps.setInt(2,paginator.getLimit());
                ResultSet set=ps.executeQuery();
                OrdineExtractor ordineExtractor=new OrdineExtractor();
                List<Ordine>o=new ArrayList<>();
                while(set.next())
                {
                    o.add(ordineExtractor.extract(set));
                }
                return o;
            }

            }



    }



    @Override
    public Optional<Ordine> fetchOrdine(int id) throws SQLException {
        try (Connection conn = source.getConnection()) {
            String query=OrdineQuery.fetchOrdine();

            try (PreparedStatement ps = conn.prepareStatement(query)){
                ps.setInt(1, id);
                ResultSet set = ps.executeQuery();

                if (set.next()) {
                    OrdineExtractor ordineExtractor = new OrdineExtractor();
                    return Optional.of(ordineExtractor.extract(set));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Ordine> fetchOrdiniByUtente(int utenteId) throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder("ordine", "ord");
            queryBuilder.select().where("ord.utente_fk = ?");
            try (PreparedStatement ps = conn.prepareStatement(queryBuilder.generateQuery())) {
                ps.setInt(1, utenteId);
                ResultSet set = ps.executeQuery();

                List<Ordine> ordini = new ArrayList<>();
                OrdineExtractor ordineExtractor = new OrdineExtractor();
                while (set.next()) {
                    ordini.add(ordineExtractor.extract(set));
                }
                return ordini;
            }
        }
    }


    @Override
    public boolean createOrdine(Ordine ordine) throws SQLException {

        try(Connection conn=source.getConnection())
        {
            conn.setAutoCommit(false);
            String query=OrdineQuery.createOrdine();
            String query2=OrdineQuery.insertCart();
            try(
                PreparedStatement ps=conn.prepareStatement(query);
                PreparedStatement psAssoc=conn.prepareStatement(query2);
            ){
                int rows=ps.executeUpdate();
                int total=rows;
                for(CarrelloItem item: ordine.getCarrello().getItems()){
                    psAssoc.setInt(1,item.getProdotto().getIdProdotto());
                    psAssoc.setInt(2,ordine.getIdOrdine());
                    psAssoc.setInt(3,item.getQuantita());
                    total+=psAssoc.executeUpdate();
                }
                if(total==(rows+ordine.getTotale())) {
                    conn.commit();
                    conn.setAutoCommit(true);
                    return true;
                }else {
                    conn.rollback(); // se qualcosa va storto annulla tutte le azioni
                    conn.setAutoCommit(true);
                    return false;
                }
            }


        }



    }
    public int countAll() throws SQLException {
        try (Connection conn = source.getConnection()) {
            QueryBuilder qb = new QueryBuilder("ordine", "ord");

            // Costruiamo manualmente la SELECT evitando il prefisso alias
            String sql = qb
                    .select("COUNT(*) AS total") // verrà messo come "ord.COUNT(*) AS total" se non modifichi il builder
                    .generateQuery()
                    .replace("ord.COUNT(*)", "COUNT(*)"); // fix rapido per rimuovere il prefisso

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("total");
                }
                return 0;
            }
        }
    }


}
