package techvibe.prodotto;

import jakarta.servlet.http.Part;
import techvibe.categoria.Categoria;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

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
    private String Immagine1;
    private String Immagine2;
    private String Immagine3;
    private String Immagine4;



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


    public String getImmagine1() {
        return Immagine1;
    }

    public void setImmagine1(String immagine1) {
        Immagine1 = immagine1;
    }

    public String getImmagine2() {
        return Immagine2;
    }

    public String getImmagine3() {
        return Immagine3;
    }

    public String getImmagine4() {
        return Immagine4;
    }

    public void setImmagine2(String immagine2) {
        Immagine2 = immagine2;
    }

    public void setImmagine3(String immagine3) {
        Immagine3 = immagine3;
    }

    public void setImmagine4(String immagine4) {
        Immagine4 = immagine4;
    }
}
