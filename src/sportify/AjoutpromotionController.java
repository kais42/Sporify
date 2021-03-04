/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportify;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author Kais
 */
public class AjoutpromotionController implements Initializable {

    @FXML
    private TextField titre;
    @FXML
    private TextField per;
    @FXML
    private TextArea desc;
    @FXML
    private DatePicker dd;
    @FXML
    private DatePicker df;
    PromotioDAO dao = new PromotioDAO();
    @FXML
    private Button add;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void ajouterp(ActionEvent event) {
        
         java.sql.Date dateD = java.sql.Date.valueOf(dd.getValue()); 
        java.sql.Date dateF= java.sql.Date.valueOf(df.getValue());
        
        Promotion e = new Promotion();
        
        e.setDateDebut(dateD);
        e.setDateFin(dateF);
        e.setTitre(titre.getText());
        e.setDescription(desc.getText()); 
        e.setPourcentage(Integer.valueOf(per.getText())); 
        dao.save(e);
        Stage stage = (Stage) add.getScene().getWindow();
    // do what you have to do
        stage.close();
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("pormolist.fxml"));
            Scene scene = new Scene(root);
        
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String title = "Succée";
        String message = "promotion a été ajouté avec succée";
        
        

        NotificationType notification = NotificationType.SUCCESS;
        TrayNotification tray = new TrayNotification(title, message, notification);
        tray.showAndWait();
    }


    @FXML
    private void validatePer(KeyEvent event) {
        if(!per.getText().matches("[0-9]+")) {
            per.clear();
            Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Alert");
                    alert.setContentText("utilisez seulement des chiffre");
                    alert.showAndWait();
        }
        
        if(Integer.valueOf(per.getText())>100){
            per.clear();
            Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Alert");
                    alert.setContentText("cette valeur ne doit pas depasser 100");
                    alert.showAndWait();
        }
    }

     @FXML
    private void promoRedirect(ActionEvent event) {
        Stage stage = new Stage();
        Parent home;
        try {
            home = FXMLLoader.load(getClass().getResource("pormolist.fxml"));
            Scene homescene = new Scene(home);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(homescene);
            app_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(EventlistController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void eventRedirect(ActionEvent event) {
        Stage stage = new Stage();
        Parent home;
        try {
            home = FXMLLoader.load(getClass().getResource("eventlist.fxml"));
            Scene homescene = new Scene(home);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(homescene);
            app_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(EventlistController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
