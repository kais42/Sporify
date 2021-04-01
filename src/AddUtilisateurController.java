/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportify;

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
import models.Utilisateur;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author hocin
 */
public class AddUtilisateurController implements Initializable {

    @FXML
    private JFXTextField nameFld;
    @FXML
    private JFXDatePicker birthFld;
    @FXML
    private JFXTextField adressFld;
    @FXML
    private JFXTextField emailFld;

    String query = null;
    Connection connection = null;
    ResultSet resultSet = null;
    PreparedStatement preparedStatement;
    Utilisateur utilisateur = null;
    private boolean update;
    int utilisateurId;

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
        String birth = String.valueOf(birthFld.getValue());
        String adress = adressFld.getText();
        String email = emailFld.getText();

        if (name.isEmpty() || birth.isEmpty() || adress.isEmpty() || email.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All DATA");
            alert.showAndWait();
            String title = "Ajouté! ";
            String message = "L'utilisateur est ajouté avec succés!";
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.seconds(4));

        } else {
            getQuery();
            insert();
            clean();

        }

    }

    @FXML
    private void clean() {
        nameFld.setText(null);
        birthFld.setValue(null);
        adressFld.setText(null);
        emailFld.setText(null);
        
    }

    private void getQuery() {

        if (update == false) {
            
            query = "INSERT INTO `user`( `name`, `birth`, `adress`, `email`) VALUES (?,?,?,?)";

        }else{
            query = "UPDATE `user` SET "
                    + "`name`=?,"
                    + "`birth`=?,"
                    + "`adress`=?,"
                    + "`email`= ? WHERE id = '"+utilisateurId+"'";
        }

    }

    private void insert() {

        try {

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameFld.getText());
            preparedStatement.setString(2, String.valueOf(birthFld.getValue()));
            preparedStatement.setString(3, adressFld.getText());
            preparedStatement.setString(4, emailFld.getText());
            preparedStatement.execute();

        } catch (SQLException ex) {
            Logger.getLogger(AddUtilisateurController.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }

    void setTextField(int id, String name, LocalDate toLocalDate, String adress, String email) {

        utilisateurId = id;
        nameFld.setText(name);
        birthFld.setValue(toLocalDate);
        adressFld.setText(adress);
        emailFld.setText(email);

    }

    void setUpdate(boolean b) {
        this.update = b;

    }

}

