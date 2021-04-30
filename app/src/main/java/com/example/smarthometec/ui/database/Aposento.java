package com.example.smarthometec.ui.database;

public class Aposento {
    String name, userCorreo;


    public Aposento(){}

    public Aposento(String email, String userCorreo){
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
