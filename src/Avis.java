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
public class Avis {
    int id;
    String nom,prenom,reclamation,avis;
    public Avis(String nom,String prenom,String reclamation,String avis){
        this.nom=nom;
        this.prenom=prenom;
        this.reclamation=reclamation;
        this.avis=avis;
    
    
    }

    public Avis(int aInt, String nom,String prenom,String reclamation,String avis) {
       //To change body of generated methods, choose Tools | Templates.
      this.id=aInt;
       this.nom=nom;
        this.prenom=prenom;
        this.reclamation=reclamation;
        this.avis=avis;
    
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getReclamation() {
        return reclamation;
    }

    public void setReclamation(String reclamation) {
        this.reclamation = reclamation;
    }

    public String getAvis() {
        return avis;
    }

    public void setAvis(String avis) {
        this.avis = avis;
    }
   
    
}
