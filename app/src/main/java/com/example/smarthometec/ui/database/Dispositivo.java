package com.example.smarthometec.ui.database;

public class Dispositivo {
    String marca, userCorreo, aposento, numSerie;
    int consumoElectrico;

    public Dispositivo(){}

    public Dispositivo(String numSerie, String aposento, String userCorreo, String marca, int consumoElectrico){
        this.numSerie = numSerie;
        this.aposento = aposento;
        this.userCorreo = userCorreo;
        this.marca = marca;
        this.consumoElectrico = consumoElectrico;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getUserCorreo() {
        return userCorreo;
    }

    public void setUserCorreo(String userCorreo) {
        this.userCorreo = userCorreo;
    }

    public String getAposento() {
        return aposento;
    }

    public void setAposento(String aposento) {
        this.aposento = aposento;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public int getConsumoElectrico() {
        return consumoElectrico;
    }

    public void setConsumoElectrico(int consumoElectrico) {  this.consumoElectrico = consumoElectrico;}
}
