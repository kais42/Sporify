/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tableView;

import com.jfoenix.controls.JFXTextField;
import helpers.DbConnect;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import helpers.DbConnect;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import models.Nutritionniste;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author soltani med ala
 */
public class AddNutController implements Initializable {

    @FXML
    private JFXTextField nameFld;
    @FXML
    private JFXTextField prenomFld;
    @FXML
    private JFXTextField adressFld;
    @FXML
    private JFXTextField emailFld;
    @FXML
    private JFXTextField telFld;
    
    
    String query = null;
    Connection connection = null;
    ResultSet resultSet = null;
    PreparedStatement preparedStatement;
    Nutritionniste nutritionniste = null;
    private boolean update;
    int nutId;
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
        String name = nameFld.getText();
        String adress = adressFld.getText();
        String email = emailFld.getText();
        String tel = telFld.getText();
        String prenom = prenomFld.getText();

        if (name.isEmpty()  || tel.isEmpty()|| prenom.isEmpty()|| adress.isEmpty() || email.isEmpty()) {
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

    @FXML
    private void clean() {
        nameFld.setText(null);
        adressFld.setText(null);
        emailFld.setText(null);
        telFld.setText(null);
        prenomFld.setText(null);
    }
     private void insert() {

        try {

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameFld.getText());
            preparedStatement.setString(3, telFld.getText());
            preparedStatement.setString(4, emailFld.getText());
            preparedStatement.setString(2, prenomFld.getText());
           preparedStatement.setString(5, adressFld.getText());
            preparedStatement.execute();
           
            
        } catch (SQLException ex) {
            
        }
     
    }
       private void getQuery() {

        if (update == false) {
            
            query = "INSERT INTO `nutritionniste`( `nom_nut`, `prenom_nut`, `num_tel_nut`, `mail_nut`,`addresse_nut`) VALUES (?,?,?,?,?)";
            
        }else if(update==true) {
            query = "UPDATE `nutritionniste` SET "
                    + "`nom_nut`=?,"
                    + "`prenom_nut`=?,"
                    + "`num_tel_nut`=?,"
                    + "`mail_nut`= ? ,"
                    + "`addresse_nut` =? WHERE id_nutrit  = '"+nutId+"'";
        }
      

    


    }
       
        private int validation_ajout() {
int a=1;
        
           if(!nameFld.getText().matches("[a-z A-Z]+")){
            nameFld.clear();
            Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Attention");
                    alert.setContentText("veuillez saisir un Nom Valide");
                    alert.showAndWait();
                    
                    a=0;
    }
           
                else if(!prenomFld.getText().matches("[a-z A-Z]+")){
            emailFld.clear();
            Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Attention");
                    alert.setContentText("veuillez saisir un Prenom Valide");
                   alert.showAndWait();
                   a=0;
    }
           
                else if(!emailFld.getText().matches("[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
            emailFld.clear();
            Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Attention");
                    alert.setContentText("veuillez saisir un e-mail Valide");
                   alert.showAndWait();
                   a=0;
    }
                 
                else if(!telFld.getText().matches("\\d{8}| +") ) {
            telFld.clear();
            Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Attention");
                    alert.setContentText("veuillez saisir Numero Telephone Valide");
                   alert.showAndWait();
                   a=0;
    }
                else if(!adressFld.getText().matches("[a-z A-Z 0-9 . ,]+")) {
            adressFld.clear();
            Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Attention");
                    alert.setContentText("veuillez saisir Adresee Valide");
                   alert.showAndWait();
                   a=0;
    }
                 
    
    
    
                 
    return a;
    }
        void setTextField(int id, String name, String prenom,String adress, String email,String tel) {

        nutId = id;
        nameFld.setText(name);
        adressFld.setText(adress);
        emailFld.setText(email);
        telFld.setText(tel);
        prenomFld.setText(prenom);

    }
         void setUpdate(boolean b) {
        this.update = b;

    }
          public void notificationalert(){
     String title = "nuyritionniste NON ajouter! ";
            String messagee = "nutritionniste ne pas été ajouter !";
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(messagee);
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.seconds(4));
    }
    public void notification(){
     String title = "NUTRITIONNISTE AJOUTER! ";
            String messagee = "nutritionniste a été ajouter avec succés!";
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(messagee);
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.seconds(4));
            
    }
           public void notifmodalert(){
     String title = "nuyritionniste NON MODIFIER! ";
            String messagee = "nutritionniste ne pas été MODIFIER !";
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(messagee);
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.seconds(4));
    }
    public void notificationmod(){
     String title = "NUTRITIONNISTE MODIFIER! ";
            String messagee = "nutritionniste a été modifier avec succés!";
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(messagee);
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.seconds(4));
            
    }
   
}
