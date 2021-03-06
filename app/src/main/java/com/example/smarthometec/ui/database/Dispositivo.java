package com.example.smarthometec.ui.database;

/**
 * Clase de los Dispositivos que representa un dispositivo propiedad del usuario.
 */
public class Dispositivo {
    String marca, userCorreo, aposento, numSerie, description;
    String consumoElectrico, init_date, final_date;
    int isOn;
    public Dispositivo(){}

    public Dispositivo(String numSerie, String description,String aposento, String userCorreo, String marca, String consumoElectrico){
        this.description = description;
        this.numSerie = numSerie;
        this.aposento = aposento;
        this.userCorreo = userCorreo;
        this.marca = marca;
        this.consumoElectrico = consumoElectrico;
        this.isOn = 0;
        this.init_date = "";
    }

    public Dispositivo(String userCorreo){
        this.userCorreo = userCorreo;
    }

    public Dispositivo(String numSerie, String description,String aposento, String userCorreo, String marca, String consumoElectrico, int ison){
        this.description = description;
        this.numSerie = numSerie;
        this.aposento = aposento;
        this.userCorreo = userCorreo;
        this.marca = marca;
        this.consumoElectrico = consumoElectrico;
        this.isOn = ison;
        this.init_date = "";
    }

    public Dispositivo(String numSerie, String description,String aposento, String userCorreo, String marca, String consumoElectrico, int ison, String init_date){
        this.description = description;
        this.numSerie = numSerie;
        this.aposento = aposento;
        this.userCorreo = userCorreo;
        this.marca = marca;
        this.consumoElectrico = consumoElectrico;
        this.isOn = ison;
        this.init_date = init_date;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getConsumoElectrico() {
        return consumoElectrico;
    }

    public void setConsumoElectrico(String consumoElectrico) {  this.consumoElectrico = consumoElectrico;}
    //0 is not on
    //1 is on
    public int isOn() {
        return isOn;
    }

    public void setOn(int isOn) {  this.isOn = isOn;}

    public String getInit_date() {
        return init_date;
    }

    public void setInit_date(String init_date) {
        this.init_date = init_date;
    }

    public String getFinal_date() {
        return final_date;
    }

    public void setFinal_date(String final_date) {
        this.final_date = final_date;
    }
}
