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
public class Rendvous {
    int idR;
    String idnut;
    String iduse;
    String dateR;
    public Rendvous(int idR ,String idnut ,String iduse ,String dateR) {
        this.dateR=dateR;
        this.idR=idR;
        this.idnut=idnut;
        this.iduse=iduse;
}

    public Rendvous(String string, String string0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Rendvous(int aInt, String string, String string0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getIdR() {
        return idR;
    }

    public void setIdR(int idR) {
        this.idR = idR;
    }

    public String getIdnut() {
        return idnut;
    }

    public void setIdnut(String idnut) {
        this.idnut = idnut;
    }

    public String getIduse() {
        return iduse;
    }

    public void setIduse(String iduse) {
        this.iduse = iduse;
    }

    public String getDateR() {
        return dateR;
    }

    public void setDateR(String dateR) {
        this.dateR = dateR;
    }
    
    
}
