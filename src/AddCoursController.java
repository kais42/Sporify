/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportify;

import helpers.DbConnect;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import models.Cours;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author User
 */
public class AddCoursController implements Initializable {

    @FXML
    private TextField nomFid;
    @FXML
    private TextField categorieFid;
    @FXML
    private TextField niveauFid;
    @FXML
    private TextField dureeFid;
    
    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    Cours cours = null ;
    private boolean update;
    int coursId;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void save(MouseEvent event) {
        connection =DbConnect.getConnect();
        String nom = nomFid.getText();
        String categorie = categorieFid.getText();
        String niveau = niveauFid.getText();
        String duree = dureeFid.getText();
        if (nom.isEmpty() || categorie.isEmpty() || niveau.isEmpty() || duree.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All DATA");
            String title = "PLEASE FILL ALL DATA! ";
            String message = "CHAMP VIDE!";
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setNotificationType(NotificationType.WARNING);
            tray.showAndDismiss(Duration.seconds(4));
            alert.showAndWait();

        } else {
            getQuery();
            insert();
            clean();
            String title = "Ajouté! ";
            String message = "Le cours est ajouté avec succés!";
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.seconds(4));

        }
   
    }

    @FXML
    private void clean() {
        nomFid.setText(null);
        categorieFid.setText(null);
        niveauFid.setText(null);
        dureeFid.setText(null);
    }

    private void getQuery() {
        if (update == false) {
              query = "INSERT INTO `cours`(`nom`, `categorie`, `niveau`, `duree`) VALUES (?,?,?,?)";

        }else{
            query = "UPDATE `cours` SET "
                    + "`nom`=?,"
                    + "`categorie`=?,"
                    + "`niveau`=?,"
                    + "`duree`= ? WHERE id = ' "+coursId+" ' ";
        }
        
    }

    private void insert() {
        try {

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nomFid.getText());
            preparedStatement.setString(2, categorieFid.getText());
            preparedStatement.setString(3, niveauFid.getText());
            preparedStatement.setString(4, dureeFid.getText());
            preparedStatement.execute();

        } catch (SQLException ex) {
            Logger.getLogger(AddCoursController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void setUpdate(boolean b) {
        this.update = b;
    }

    void setTextField(int id, String nom, String categorie, String niveau, int duree) {
        coursId = id;
        nomFid.setText(nom);
        categorieFid.setText(categorie);
        niveauFid.setText(niveau);
        dureeFid.setText(Integer.toString(duree));
    }
    
}
