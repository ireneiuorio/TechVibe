package techvibe.prodotto;

import techvibe.storage.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProdottoExtractor implements ResultSetExtractor<Prodotto> {

    @Override
    public Prodotto extract(ResultSet resultSet) throws SQLException {
        Prodotto prodotto = new Prodotto();
        prodotto.setIdProdotto(resultSet.getInt("pro.id"));
        prodotto.setDimensioneSchermo(resultSet.getDouble("pro.dimensioneschermo"));
        prodotto.setConnettivita(resultSet.getString("pro.connettivita"));
        prodotto.setPrezzo(resultSet.getDouble("pro.prezzo"));
        prodotto.setModello(resultSet.getString("pro.modello"));
        prodotto.setMarca(resultSet.getString("pro.marca"));
        prodotto.setSistemaOperativo(resultSet.getString("pro.sistemaoperativo"));
        prodotto.setQtDisponibile(resultSet.getInt("pro.qtdisponibile"));
        prodotto.setColore(resultSet.getString("pro.colore"));
        prodotto.setStorage(resultSet.getInt("pro.storage"));
        prodotto.setRam(resultSet.getInt("pro.ram"));
        return prodotto;
    }
}
