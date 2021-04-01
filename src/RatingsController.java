/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportify;

import helpers.DbConnect;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Coachs;
import org.controlsfx.control.Rating;

/**
 * FXML Controller class
 *
 * @author OMEN
 */
public class RatingsController implements Initializable {

    @FXML
    private Rating rating;
    @FXML
    private Label msg;
    @FXML
    private TableView<Coachs> tableco;
    @FXML
    private TableColumn<Coachs, String> colnom;
    @FXML
    private TableColumn<Coachs, String> colprenom;
    @FXML
    private TableColumn<Coachs, String>  colmail;
    @FXML
    private TextField filteredtext;
    
    ObservableList<Coachs> listM;
    ObservableList<Coachs> dataList;

    int index = -1;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    @FXML
    private Button insert;

    void search_coach() {
        colnom.setCellValueFactory(new PropertyValueFactory<Coachs, String>("nom"));
        colprenom.setCellValueFactory(new PropertyValueFactory<Coachs, String>("prenom"));
        colmail.setCellValueFactory(new PropertyValueFactory<Coachs, String>("mail"));

        dataList = DbConnect.getDataCoachs();
        tableco.setItems(dataList);
        FilteredList<Coachs> filteredData = new FilteredList<>(dataList, b -> true);
        filteredtext.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(coach -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (coach.getNom().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches nom
                } else if (coach.getPrenom().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches prenom
                } else if (String.valueOf(coach.getMail()).indexOf(lowerCaseFilter) != -1) {
                    return true;// Filter matches mail
                } else {
                    return false; // Does not match.
                }
            });
        });
        SortedList<Coachs> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableco.comparatorProperty());
        tableco.setItems(sortedData);
    }
    
    public void UpdateTable() {
        colnom.setCellValueFactory(new PropertyValueFactory<Coachs, String>("nom"));
        colprenom.setCellValueFactory(new PropertyValueFactory<Coachs, String>("prenom"));
        colmail.setCellValueFactory(new PropertyValueFactory<Coachs, String>("mail"));
        
        listM = DbConnect.getDataCoachs();
        tableco.setItems(listM);
        tableco.setItems(dataList);
    }   

    @FXML
    private void handleActionButton(ActionEvent event) throws IOException {
        Parent rating_page_parent = FXMLLoader.load(getClass().getResource("FXMLDocument1.fxml"));
        Scene rating_page_scene = new Scene(rating_page_parent);
        Stage app_stage  = (Stage)  ((Node) event.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene (rating_page_scene);
        app_stage.show(); 
      
    }
    
    public void getSelected(MouseEvent event) {
        index = tableco.getSelectionModel().getSelectedIndex();
        if (index <= -1) {

            return;
        }
    } 
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        rating.ratingProperty().addListener(new ChangeListener<Number> () {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            msg.setText("Rating: "+newValue);
            }
        });
        UpdateTable();
        search_coach();
        
    } 

    @FXML
    private void insert(ActionEvent event) {
        System.out.println("Coach Rating :" + rating.getRating());
    }
}
