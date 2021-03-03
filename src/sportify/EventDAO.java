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
public class EventDAO {
      Connection connection = DBConnection.getInstance().getConnexion();
      
       public ArrayList<Event> findAll(){
        ArrayList<Event> events = new ArrayList(); 
        String query = "SELECT * FROM evenement ORDER BY date_debut";
        PreparedStatement ps;
        try{
            ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while ( rs.next() ){
                Event event = new Event();
                event.setId(rs.getInt("id"));
                event.setDescription(rs.getString("description"));
                event.setTitre(rs.getString("titre"));
                event.setDateDebut(rs.getDate("date_debut"));
                event.setDateFin(rs.getDate("date_fin"));
                        
                
                events.add(event);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return events;
    }
       
       
     public ArrayList<Event> findByTitre(String titre){
        ArrayList<Event> events = new ArrayList(); 
        String query = "SELECT * FROM evenement WHERE titre LIKE '%"+titre+"%' ORDER BY date_debut";
        PreparedStatement ps;
        try{
            ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while ( rs.next() ){
                Event event = new Event();
                event.setId(rs.getInt("id"));
                event.setDescription(rs.getString("description"));
                event.setTitre(rs.getString("titre"));
                event.setDateDebut(rs.getDate("date_debut"));
                event.setDateFin(rs.getDate("date_fin"));
                        
                
                events.add(event);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return events;
    }
       
       
    public void save(Event e){
        String  query  = "INSERT INTO evenement (titre, description, date_debut, date_fin) VALUES (?,?,?,?)";
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement(query);
            ps.setString (1, e.getTitre());
            ps.setString(2, e.getDescription());
            ps.setDate (3, e.getDateDebut());
            ps.setDate (4, e.getDateFin());
            ps.execute();
            
            System.out.println("event & été ajouté avec succée");
        } catch (SQLException ex) {
            System.out.println("Erreur lors l'ajout");
            System.err.println(ex.getMessage());
        }
    }
    public void delete(int id){
        String query = "DELETE FROM evenement WHERE id=?";
        PreparedStatement preparedStmt;
        try{
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, id);
            preparedStmt.execute();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
     public void update(Event e){
        String  query  = "UPDATE evenement SET titre = ?,description = ?, date_debut = ?,date_fin = ?  Where id = ?";
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement(query);
            ps.setString (1, e.getTitre());
            ps.setString (2, e.getDescription());
            ps.setDate   (3, e.getDateDebut());
            ps.setDate   (4, e.getDateFin());
            ps.setInt    (5, e.getId());
            ps.execute();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }   
    }
     
     
    public Event findOne(String id){
        String query = "SELECT * FROM evenement WHERE id = ?";
        PreparedStatement preparedStmt;
        Event e = new Event();
        try{
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, id);
            ResultSet rs = preparedStmt.executeQuery();
            while ( rs.next() ){
                e.setId(rs.getInt("id"));
                e.setTitre(rs.getString("titre"));
                e.setDateDebut(rs.getDate("date_debut"));
                e.setDateFin(rs.getDate("date_fin"));
                e.setDescription(rs.getString("description"));
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return e;
    }
    
}
