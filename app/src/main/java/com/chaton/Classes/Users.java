package com.chaton.Classes;


public class Users {
    private String email;
    private  String name;
    private String urlPhoto;
    public Users(){
    }

    public Users(String e, String n, String u){
        this.email =e;
        this.name = n;
        this.urlPhoto = u;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }
}
