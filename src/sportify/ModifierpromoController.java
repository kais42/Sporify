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
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kais
 */
public class ModifierpromoController implements Initializable {

    @FXML
    private TextArea desc;
    @FXML
    private TextField titre;
    @FXML
    private TextField dd;
    @FXML
    private TextField df;
    @FXML
    private TextField per;
    @FXML
    private Button modif;
    @FXML
    private Button supp;
    
    int id;
    
    PromotioDAO dao = new PromotioDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
     
    void initData(Promotion e) {
    
       
        titre.setText(String.valueOf(e.getTitre()));
        desc.setText(e.getDescription().toString());
        dd.setText(e.getDateDebut().toString());
        df.setText(String.valueOf(e.getDateFin()));
        per.setText(String.valueOf(e.getPourcentage()));
        this.id = e.getId();
  }

    @FXML
    private void modifRedirect(ActionEvent event) {
        
       java.sql.Date dateD = java.sql.Date.valueOf(dd.getText());
        java.sql.Date dateF = java.sql.Date.valueOf(df.getText());
        Promotion e = dao.findOne(String.valueOf(this.id));
        e.setTitre(titre.getText());
        e.setDescription(desc.getText());
        e.setDateDebut(dateD);
        e.setDateFin(dateF);
        e.setPourcentage(Integer.valueOf(per.getText()));
        dao.update(e);
        
        
        Stage stage = (Stage) modif.getScene().getWindow();
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

    }

    @FXML
    private void suppRedirect(ActionEvent event) {
        
        dao.delete(id);
        Stage stage = (Stage) supp.getScene().getWindow();
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
