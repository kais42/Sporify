/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.obaid.demo;

/**
 *
 * @author User
 */
public class Cours {
    private int id;
    private String nom;
    private String categorie;
    private String niveau;
    private int duree;

    public Cours(int id, String nom, String categorie, String niveau, int duree) {
        this.id = id;
        this.nom = nom;
        this.categorie = categorie;
        this.niveau = niveau;
        this.duree = duree;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getNiveau() {
        return niveau;
    }

    public int getDuree() {
        return duree;
    }
    
}
