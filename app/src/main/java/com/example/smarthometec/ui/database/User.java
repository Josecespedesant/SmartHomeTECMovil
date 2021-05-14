package com.example.smarthometec.ui.database;

/**
 * Clase de los Usuarios que representa a cada uno de los usuarios en el dispositivo.
 */
public class User {
    String name, lastName, address, email, pass, continent, country;

    public User(){}

    public User(String email, String name, String lastName, String address, String pass
    , String continent, String country){
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.pass = pass;
        this.continent = continent;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
