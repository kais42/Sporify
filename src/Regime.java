/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author soltani med ala
 */
public class Regime {
    int id;
    String nom_plat;
    String description;
    String categorie;
    String contenu;
    public Regime(int id,String nom_plat,String description,String categorie ,String contenu){
    this.categorie=categorie;
    this.contenu=contenu;
    this.description=description;
    this.nom_plat=nom_plat;
    this.id=id;
    
    
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_plat() {
        return nom_plat;
    }

    public void setNom_plat(String nom_plat) {
        this.nom_plat = nom_plat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
    
}
