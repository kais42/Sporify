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
public class Event {
    private int id ;
    private String titre ;
    private String description ;
    private Date dateDebut ;
    private Date dateFin ;
    
    private Button action;
    private Button actionModif;

    public Button getAction() {
        return action;
    }

    public void setAction(Button action) {
        this.action = action;
    }

    public Button getActionModif() {
        return actionModif;
    }

    public void setActionModif(Button actionModif) {
        this.actionModif = actionModif;
    }
    
    
    
    

    public Event() {
        this.action = new Button("Voir");
        this.actionModif = new Button("Modifier");
    }
    
    

    public Event(String titre, String description, Date dateDebut, Date dateFin) {
        this.titre = titre;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.action = new Button("Voir");
        this.actionModif = new Button("Modifier");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Event{" + "id=" + id + ", titre=" + titre + ", description=" + description + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin + '}';
    }
    
    
    
    
    
}
