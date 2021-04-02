/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDV;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import helpers.DbConnect;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import models.Nutritionniste;
import tableView.AddNutController;
import tableView.TableViewController;

/**
 * FXML Controller class
 *
 * @author soltani med ala
 */
public class DemandeRDVController implements Initializable {

    @FXML
    private TableView<Nutritionniste> nutTable;
    @FXML
    private TableColumn<Nutritionniste, String> nomCol;
    @FXML
    private TableColumn<Nutritionniste, String> prenomCol;
    @FXML
    private TableColumn<Nutritionniste, String> adressCol;
    @FXML
    private TableColumn<Nutritionniste, String> emailCol;
    @FXML
    private TableColumn<Nutritionniste, String> telCol;
    @FXML
    private TableColumn<Nutritionniste, String> editCol;
    @FXML
    private FontAwesomeIconView homeId;
     @FXML
    private FontAwesomeIconView searchId;
    @FXML
    private TextField rechId;
    
    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    Nutritionniste nutritionniste = null ;
    
    ObservableList<Nutritionniste>  nutritionnisteList = FXCollections.observableArrayList();
    @FXML
    private FontAwesomeIconView refrech;
    @FXML
    private FontAwesomeIconView add;
    @FXML
    private Label nut;
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadDate();
    }    

    @FXML
    private void close(MouseEvent event) {
         Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
 @FXML
    private void goHome(MouseEvent event) {
         try {
            Parent parent = FXMLLoader.load(getClass().getResource("/Adherent/Homeadhe.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
   

    
    
    @FXML
     private void refreshTable() {
        try {
            nutritionnisteList.clear();
            
            query = "SELECT * FROM `nutritionniste`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()){
                nutritionnisteList.add(new  Nutritionniste(
                        resultSet.getInt("id_nutrit"),
                        resultSet.getString("nom_nut"),
                        resultSet.getString("prenom_nut"),
                        resultSet.getString("addresse_nut"),
                        resultSet.getString("mail_nut"),
                        resultSet.getString("num_tel_nut")));
                         nutTable.setItems(nutritionnisteList);
                
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
     private void loadDate() {
        
        connection = DbConnect.getConnect();
        refreshTable();
        
       // idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        adressCol.setCellValueFactory(new PropertyValueFactory<>("adress"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        telCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        
        //add cell of button edit 
         Callback<TableColumn<Nutritionniste, String>, TableCell<Nutritionniste, String>> cellFoctory = (TableColumn<Nutritionniste, String> param) -> {
            // make cell containing buttons
            final TableCell<Nutritionniste, String> cell = new TableCell<Nutritionniste, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.SEND);

                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:green;"
                        );
            
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            nutritionniste = nutTable.getSelectionModel().getSelectedItem();
             FXMLLoader loader = new FXMLLoader ();
                        loader.setLocation(getClass().getResource("/RDV/RDV.fxml"));
                   try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                            }
            RDVController RDVController = loader.getController();
                  RDVController.setTextField(nutritionniste.getEmail(),nutritionniste.getNom());
                    Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UTILITY);
                            stage.show();
             
                        });
                            


                        HBox managebtn = new HBox(deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));


                        setGraphic(managebtn);

                        setText(null);

                    }
                }

            };

            return cell;
        };
         editCol.setCellFactory(cellFoctory);
         nutTable.setItems(nutritionnisteList);
         
         
    }
        private void recherche(String nom) {
        try {
            nutritionnisteList.clear();
            
            query = "SELECT * FROM `nutritionniste` where `nom_nut` like '%"+nom+"%' or `prenom_nut` like '%"+nom+"%' or `addresse_nut` like '%"+nom+"%' or `num_tel_nut` like '%"+nom+"%' ";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()){
                nutritionnisteList.add(new  Nutritionniste(
                        resultSet.getInt("id_nutrit"),
                        resultSet.getString("nom_nut"),
                        resultSet.getString("prenom_nut"),
                        resultSet.getString("addresse_nut"),
                        resultSet.getString("mail_nut"),
                        resultSet.getString("num_tel_nut")));
                nutTable.setItems(nutritionnisteList);
                
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        }

    @FXML
    private void searchName(MouseEvent event) {
         recherche(rechId.getText());
    }

    @FXML
    private void getAddView(MouseEvent event) {
        
            try {
            Parent parent = FXMLLoader.load(getClass().getResource("/RDV/Homeadhe.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
    }

    @FXML
    private void eventRedirect(MouseEvent event) {
    }

    @FXML
    private void redirectevent(MouseEvent event) {
    }

    @FXML
    private void nut_login(MouseEvent event) {
    }

    @FXML
    private void coursRedirect(MouseEvent event) {
    }

    @FXML
    private void promoRedirect(MouseEvent event) {
    }

    @FXML
    private void coachRedirect(MouseEvent event) {
    }

    @FXML
    private void abonnementRedirect(MouseEvent event) {
    }

    @FXML
    private void userRedirect(MouseEvent event) {
    }
     

    
}
