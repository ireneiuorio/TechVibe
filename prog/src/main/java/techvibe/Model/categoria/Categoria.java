package techvibe.Model.categoria;

import techvibe.Model.prodotto.Prodotto;

import java.util.List;

public class Categoria {

    private int IdCategoria;
    private String NomeCategoria;
    private List<Prodotto> Prodotti;

    public Categoria() {
    }

    public int getIdCategoria() {
        return IdCategoria;
    }

    public String getNomeCategoria() {
        return NomeCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        IdCategoria = idCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        NomeCategoria = nomeCategoria;
    }

    public List<Prodotto> getProdotti() {
        return Prodotti;
    }

    public void setProdotti(List<Prodotto> prodotti) {
        Prodotti = prodotti;
    }
}
