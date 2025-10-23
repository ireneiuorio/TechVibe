package techvibe.Model.prodotto;

import techvibe.Model.categoria.Categoria;
import techvibe.Model.storage.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProdottoExtractor implements ResultSetExtractor<Prodotto> {

    @Override
    public Prodotto extract(ResultSet rs) throws SQLException {
        Prodotto prodotto = new Prodotto();
        prodotto.setIdProdotto(rs.getInt("pro.idprodotto"));
        prodotto.setDimensioneSchermo(rs.getDouble("pro.dimschermo"));
        prodotto.setConnettivita(rs.getString("pro.connettivita"));
        prodotto.setPrezzo(rs.getDouble("pro.prezzo"));
        prodotto.setModello(rs.getString("pro.modello"));
        prodotto.setMarca(rs.getString("pro.marca"));
        prodotto.setSistemaOperativo(rs.getString("pro.sistemaoperativo"));
        prodotto.setQtDisponibile(rs.getInt("pro.qtdisponibile"));
        prodotto.setColore(rs.getString("pro.colore"));
        prodotto.setStorage(rs.getInt("pro.storagedispositivo"));
        prodotto.setRam(rs.getInt("pro.ram"));
        prodotto.setPercentualeSconto(rs.getDouble("pro.percentuale_sconto"));

        // AGGIUNGI LA CATEGORIA
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(rs.getInt("pro.idcategoria")); // Leggi l'ID categoria dal database
        prodotto.setCategoria(categoria);

        prodotto.setImmagine1(rs.getString("pro.immagine1"));
        prodotto.setImmagine2(rs.getString("pro.immagine2"));
        prodotto.setImmagine3(rs.getString("pro.immagine3"));
        prodotto.setImmagine4(rs.getString("pro.immagine4"));


        return prodotto;
    }

}
