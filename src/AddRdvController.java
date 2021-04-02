/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OperationRDV;

import com.jfoenix.controls.JFXTextField;
import helpers.DbConnect;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import models.Nutritionniste;
import models.Rendvous;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author soltani med ala
 */
public class AddRdvController implements Initializable {

    @FXML
    private JFXTextField Nid;
    @FXML
    private JFXTextField Userid;
    
    
    
     String query = null;
    Connection connection = null;
    ResultSet resultSet = null;
    PreparedStatement preparedStatement;
     Rendvous Rendvous = null ;
int id_rdv;
     
    private boolean update;

    @FXML
    private DatePicker dateId;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void save(MouseEvent event) {
        connection = DbConnect.getConnect();
         String Iduser = Userid.getText();
        String Nutid = Nid.getText();
        

        String dateRDV ;
    
        dateRDV = dateId.getValue().toString();
        

        if (Iduser.isEmpty()  || Nutid.isEmpty()|| dateRDV.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tout les champs");
            alert.showAndWait();
             if(update==false)
             {
             notificationalert();
             }
             else if (update==true){
                 notifmodalert();
             
             }
        } else if (validation_ajout()==1){
            getQuery();
             insert();
             if(update==false)
             {
              notification();
             }
             else if (update==true){
             notificationmod();
             }
             clean();
            }else{
             if(update==false)
             {
             notificationalert();
             }
             else if (update==true){
                 notifmodalert();
             
             }
                     }
    }

    private void getQuery() {
       
        if (update == false) {
        query = "INSERT INTO `rdv`(`id_user`,`id_nut`,`date_rdv`) VALUES (?,?,?)";   
      
        }else if(update==true) {
            query = "UPDATE `rdv` SET "
                    + "`id_user`=?,"
                    + "`id_nut`=?,"
                    + "`date_rdv`=?"
                    +" WHERE id_rdv = '"+id_rdv+"'";
        
           

        }
    }

    private void insert() {
           try {
preparedStatement = connection.prepareStatement(query);
 
preparedStatement.setString(1,Userid.getText());
 preparedStatement.setString(2, Nid.getText());
 preparedStatement.setString(3,  dateId.getValue().toString());
               System.out.println(""+preparedStatement.toString());
      preparedStatement.execute();
        }
            catch (SQLException ex) {
            
        }
    }

    @FXML
    private void clean() {
        
        Userid.setText(null);
        Nid.setText(null);
        dateId.setValue(null); 
   
    }
     void setUpdate(boolean b) {
        this.update = b;

    }
        void setTextField(int id, String user,String nut, String date) {

       id_rdv = id;
        Userid.setText(user);
        Nid.setText(nut);
     String pattern = "yyyy-MM-dd";
     DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
     LocalDate.parse(date, dateFormatter);
       dateId.setValue(LocalDate.parse(date, dateFormatter));
       

    } private int validation_ajout() {
        int a=1;
         if(!Userid.getText().matches("[a-z A-Z]+") ) {
            Userid.clear();
            Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Attention");
                    alert.setContentText("veuillez saisir NOM utulisateur Valide");
                   alert.showAndWait();
                   a=0;

    }
         else if(!Nid.getText().matches("[a-z A-Z]+") ) {
            Nid.clear();
            Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Attention");
                    alert.setContentText("veuillez saisir NOM Nutritionniste Valide");
                   alert.showAndWait();
                   a=0;

             
    }
return a;
}
        public void notificationmod(){
     String title = "RENDEZ-VOUS  MODIFIER! ";
            String messagee = "RENDEZ-VOUS  a été modifier avec succés!";
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(messagee);
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.seconds(4));
            
    }
   
          
      public void notifmodalert(){
     String title = "RENDEZ-VOUS  NON MODIFIER! ";
            String messagee = "RENDEZ-VOUS  ne pas été MODIFIER !";
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(messagee);
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.seconds(4));
      }
             public void notification(){
     String title = "RENDEZ-VOUS ajouter! ";
            String messagee = "Rendez-vous a été ajouter !";
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(messagee);
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.seconds(4));
}   
               public void notificationalert(){
     String title = "RNEDER-VOUZ NON AJOUTER! ";
            String messagee = "Rendez-vous n'est pas été ajouter!";
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(messagee);
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.seconds(4));
            }
}
