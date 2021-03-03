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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kais
 */
public class EventlistController implements Initializable {
    
    @FXML
    private TableView<Event> table;
    @FXML
    private TableColumn<Event, Integer> id;
    
    @FXML
    private TableColumn<Event, Date> dd;
    
    @FXML
    private TableColumn<Event,Date> df;
    
    @FXML
    private TableColumn<Event, String> titre;
    
  
    @FXML
    private TableColumn<Event, String> desc;
    
    ObservableList<Event> events;
    
    private TableColumn<Event, String> actionCol = new TableColumn("Action");
    private TableColumn<Event, String> modifCol = new TableColumn("Modifier");
    
    EventDAO dao = new EventDAO();
    @FXML
    private Button ajouter;
    @FXML
    private TextField searchInput;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        table.getColumns().add(actionCol);
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        titre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        desc.setCellValueFactory(new PropertyValueFactory<>("description"));
        dd.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        df.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        actionCol.setCellValueFactory(new PropertyValueFactory<Event, String>("action"));
        ArrayList<Event> events = dao.findAll();
        
       
     
        for(Event ev: events){
            ev.getAction().setOnAction(e -> {
                
                Stage stage = new Stage();
               FXMLLoader loader = new FXMLLoader(getClass().getResource("event.fxml"));
               Parent parent;
                    try {
                        parent = (Parent) loader.load();
                        Scene scene = new Scene(parent);
                        stage.setScene(scene);
                        stage.show();
                        EventController controller = loader.getController();
                        controller.initData(ev);
                        Stage parentStage = (Stage) ev.getAction().getScene().getWindow();
                        parentStage.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
            });
        
        }
        
        
        
        ObservableList<Event> listevent = FXCollections.observableArrayList(events);
        table.setItems(listevent);
        
        
        // TODO
    }  
    
    
    @FXML
    private void ajouterRedirect(ActionEvent event) {
        Stage stage = new Stage();
        Parent home;
        try {
            home = FXMLLoader.load(getClass().getResource("ajouter.fxml"));
            Scene homescene = new Scene(home);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(homescene);
            app_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(EventlistController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void searchAction(KeyEvent event) {
        
        ArrayList<Event> events = dao.findByTitre(searchInput.getText());
        
       
     
        for(Event ev: events){
            ev.getAction().setOnAction(e -> {
                
                Stage stage = new Stage();
               FXMLLoader loader = new FXMLLoader(getClass().getResource("event.fxml"));
               Parent parent;
                    try {
                        parent = (Parent) loader.load();
                        Scene scene = new Scene(parent);
                        stage.setScene(scene);
                        stage.show();
                        EventController controller = loader.getController();
                        controller.initData(ev);
                        Stage parentStage = (Stage) ev.getAction().getScene().getWindow();
                        parentStage.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
            });
        
        }
        
        
        
        ObservableList<Event> listevent = FXCollections.observableArrayList(events);
        table.setItems(listevent);
    }

    @FXML
    private void redirectStat(ActionEvent event) {
        Stage stage = new Stage();
        Parent home;
        try {
            home = FXMLLoader.load(getClass().getResource("stat.fxml"));
            Scene homescene = new Scene(home);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(homescene);
            app_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(EventlistController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
