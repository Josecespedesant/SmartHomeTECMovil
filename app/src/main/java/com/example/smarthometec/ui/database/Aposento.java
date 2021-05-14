package com.example.smarthometec.ui.database;

/**
 * Clase de los Aposentos que representa un lugar en la casa del usuario.
 */
public class Aposento {
    String name, userCorreo;


    public Aposento(){}

    public Aposento(String name, String userCorreo){
        this.name = name;
        this.userCorreo = userCorreo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserCorreo() {
        return userCorreo;
    }

    public void setUserCorreo(String userCorreo) {
        this.userCorreo = userCorreo;
    }

}
