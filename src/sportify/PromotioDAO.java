/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportify;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Kais
 */
public class PromotioDAO {
     Connection connection = DBConnection.getInstance().getConnexion();
      
       public ArrayList<Promotion> findAll(){
        ArrayList<Promotion> promos = new ArrayList(); 
        String query = "SELECT * FROM promotion ORDER BY date_debut";
        PreparedStatement ps;
        try{
            ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while ( rs.next() ){
                Promotion promo = new Promotion();
                promo.setId(rs.getInt("id"));
                promo.setDescription(rs.getString("description"));
                promo.setTitre(rs.getString("titre"));
                promo.setDateDebut(rs.getDate("date_debut"));
                promo.setDateFin(rs.getDate("date_fin"));
                promo.setPourcentage(rs.getInt("Pourcentage"));

                        
                
                promos.add(promo);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
           System.err.println(promos);
        return promos;
    }
       public Promotion findOne(String id){
        String query = "SELECT * FROM promotion WHERE id = ?";
        PreparedStatement preparedStmt;
        Promotion p = new Promotion();
        try{
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, id);
            ResultSet rs = preparedStmt.executeQuery();
            while ( rs.next() ){
                p.setId(rs.getInt("id"));
                p.setTitre(rs.getString("titre"));
                p.setDateDebut(rs.getDate("date_debut"));
                p.setDateFin(rs.getDate("date_fin"));
                p.setDescription(rs.getString("description"));
                p.setPourcentage(rs.getInt("pourcentage"));
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return p;
    }
        public void save(Promotion p){
        String  query  = "INSERT INTO promotion (titre, description, date_debut, date_fin, pourcentage) VALUES (?,?,?,?,?)";
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement(query);
            ps.setString (1, p.getTitre());
            ps.setString(2, p.getDescription());
            ps.setDate (3, p.getDateDebut());
            ps.setDate (4, p.getDateFin());
             ps.setInt(5, p.getPourcentage());
            ps.execute();
            
            System.out.println(" Pormoition a  été ajouté avec succée");
        } catch (SQLException ex) {
            System.out.println("Erreur lors l'ajout");
            System.err.println(ex.getMessage());
        }
    }
    public void delete(int id){
        String query = "DELETE FROM promotion WHERE id=?";
        PreparedStatement preparedStmt;
        try{
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, id);
            preparedStmt.execute();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
     public void update(Promotion p){
        String  query  = "UPDATE promotion SET titre = ?,description = ?, date_debut = ?,date_fin = ? , pourcentage = ? Where id = ?";
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement(query);
            ps.setString (1, p.getTitre());
            ps.setString (2, p.getDescription());
            ps.setDate   (3, p.getDateDebut());
            ps.setDate   (4, p.getDateFin());
            ps.setInt   (5, p.getPourcentage());
            ps.setInt    (6, p.getId());
            ps.execute();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }   
    }
}
