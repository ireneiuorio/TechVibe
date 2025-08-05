package techvibe.prodotto;

import techvibe.categoria.Categoria;

public class Prodotto {
    private int IdProdotto;
    private double DimensioneSchermo;
    private String Connettivita;
    private double Prezzo;
    private String Modello;
    private String Marca;
    private String SistemaOperativo;
    private int QtDisponibile;
    private String Colore;
    private int Storage;
    private int Ram;
    private Categoria categoria;

    public Prodotto() {
    }

    public int getIdProdotto() {
        return IdProdotto;
    }

    public double getDimensioneSchermo() {
        return DimensioneSchermo;
    }

    public String getConnettivita() {
        return Connettivita;
    }

    public double getPrezzo() {
        return Prezzo;
    }

    public String getModello() {
        return Modello;
    }

    public String getMarca() {
        return Marca;
    }

    public String getSistemaOperativo() {
        return SistemaOperativo;
    }

    public int getQtDisponibile() {
        return QtDisponibile;
    }

    public String getColore() {
        return Colore;
    }

    public int getStorage() {
        return Storage;
    }

    public int getRam() {
        return Ram;
    }

    public void setIdProdotto(int idProdotto) {
        IdProdotto = idProdotto;
    }

    public void setDimensioneSchermo(double dimensioneSchermo) {
        DimensioneSchermo = dimensioneSchermo;
    }

    public void setConnettivita(String connettivita) {
        Connettivita = connettivita;
    }

    public void setPrezzo(double prezzo) {
        Prezzo = prezzo;
    }

    public void setModello(String modello) {
        Modello = modello;
    }

    public void setMarca(String marca) {
        Marca = marca;
    }

    public void setSistemaOperativo(String sistemaOperativo) {
        SistemaOperativo = sistemaOperativo;
    }

    public void setQtDisponibile(int qtDisponibile) {
        QtDisponibile = qtDisponibile;
    }

    public void setColore(String colore) {
        Colore = colore;
    }

    public void setStorage(int storage) {
        Storage = storage;
    }

    public void setRam(int ram) {
        Ram = ram;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
