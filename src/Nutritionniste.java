/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Date;

/**
 *
 * @author hocin
 */
public class Nutritionniste {
    
    
    int idnut  ;
    String nom ;
    String prenom;
    String adress ,email,telephone ;

    public Nutritionniste(int id, String name, String prenom, String adress, String email,String telephone) {
        this.idnut = id;
        this.nom = name;
        this.prenom=prenom;
        this.adress = adress;
        this.email = email;
        this.telephone = telephone;
    }

    public Nutritionniste(String string, String string0, String string1, String string2, String string3) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getIdnut() {
        return idnut;
    }

    public void setIdnut(int idnut) {
        this.idnut = idnut;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }



    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
    
    
}
