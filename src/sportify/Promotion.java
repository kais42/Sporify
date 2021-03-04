/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportify;

import java.sql.Date;
import javafx.scene.control.Button;

/**
 *
 * @author Kais
 */
public class Promotion {
    private int id ;
    private int pourcentage ;
    private Date dateDebut ;
    private Date dateFin;
    private String titre ;
    private String description ;
    private Button action;

    public Button getAction() {
        return action;
    }

    public void setAction(Button action) {
        //this.action = action;
    }
    
    

    public Promotion(int pourcentage, Date dateDebut, Date dateFin, String titre, String description) {
        this.pourcentage = pourcentage;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.titre = titre;
        this.description = description;
        this.action = new Button("Voir");
    }

    public Promotion() {
        this.action = new Button("Voir");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(int pourcentage) {
        this.pourcentage = pourcentage;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Promotion{" + "id=" + id + ", pourcentage=" + pourcentage + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin + ", titre=" + titre + ", description=" + description + '}';
    }
    
    
    
}
