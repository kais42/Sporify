/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportify;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kais
 */
public class PormolistController implements Initializable {

    @FXML
    private TableView<Promotion> table;
    @FXML
    private TableColumn<Promotion, String> titre;
    @FXML
    private TableColumn<Promotion, String> desc;
    @FXML
    private TableColumn<Promotion, Date> dd;
    @FXML
    private TableColumn<Promotion, Date> df;
    @FXML
    private TableColumn<Promotion, Integer> per;
    
    ObservableList<Promotion> promos;
    
    private TableColumn<Promotion, String> actionCol = new TableColumn("Action");
    
    PromotioDAO dao = new PromotioDAO();
    @FXML
    private Button add;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ArrayList<Promotion> promotions = dao.findAll();
        
        table.getColumns().add(actionCol);
        titre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        desc.setCellValueFactory(new PropertyValueFactory<>("description"));
        dd.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        df.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        per.setCellValueFactory(new PropertyValueFactory<>("pourcentage"));
        actionCol.setCellValueFactory(new PropertyValueFactory<Promotion, String>("action"));
        
        for(Promotion ev: promotions){
            ev.getAction().setOnAction(e -> {
                
                Stage stage = new Stage();
               FXMLLoader loader = new FXMLLoader(getClass().getResource("modifierpromo.fxml"));
               Parent parent;
                    try {
                        parent = (Parent) loader.load();
                        Scene scene = new Scene(parent);
                        stage.setScene(scene);
                        stage.show();
                        ModifierpromoController controller = loader.getController();
                        controller.initData(ev);
                        Stage parentStage = (Stage) ev.getAction().getScene().getWindow();
                        parentStage.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
            });
        
        }
        
        ObservableList<Promotion> listp = FXCollections.observableArrayList(promotions);
       table.setItems(listp);
        // TODO
    }    

//    @FXML
//    private void promoRedirect(ActionEvent event) {
//        Stage stage = new Stage();
//        Parent home;
//        try {
//            home = FXMLLoader.load(getClass().getResource("pormolist.fxml"));
//            Scene homescene = new Scene(home);
//            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            app_stage.setScene(homescene);
//            app_stage.show();
//        } catch (IOException ex) {
//            Logger.getLogger(EventlistController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    @FXML
    private void addRedirect(ActionEvent event) {
        Stage stage = new Stage();
        Parent home;
        try {
            home = FXMLLoader.load(getClass().getResource("Ajoutpromotion.fxml"));
            Scene homescene = new Scene(home);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(homescene);
            app_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(EventlistController.class.getName()).log(Level.SEVERE, null, ex);
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
