/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tableView;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import helpers.DbConnect;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Observable;
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
import javafx.util.Duration;
import jdk.nashorn.internal.objects.annotations.Property;
import models.Nutritionniste;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author hocin
 */
public class TableViewController implements Initializable {

    @FXML
    private TableView<Nutritionniste> nutTable;
    @FXML
    private TableColumn<Nutritionniste, String> idCol;
     @FXML
    private TableColumn<Nutritionniste, String> nomCol;
    @FXML
    private TableColumn<Nutritionniste, String> prenomCol;
    @FXML
    private TableColumn<Nutritionniste, String> adressCol;
    @FXML
    private TableColumn<Nutritionniste, String> emailCol;
    @FXML
    private TableColumn<Nutritionniste, String> editCol;
    @FXML
    private TableColumn<Nutritionniste, String> telCol;
    
    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    Nutritionniste nutritionniste = null ;
    
    ObservableList<Nutritionniste>  nutritionnisteList = FXCollections.observableArrayList();
    @FXML
    private TextField rechId;
    @FXML
   private FontAwesomeIconView homeId;
    @FXML
    private FontAwesomeIconView searchId;

    
   

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
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
    private void getAddView(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/tableView/addNut.fxml"));
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

//recherhce nutristionniste par nom
    
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
    
    
    
    private void loadDate() {
        
        connection = DbConnect.getConnect();
        refreshTable();
        
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
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

                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#ff1744;"
                        );
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#00E676;"
                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            
                            try {
                                nutritionniste = nutTable.getSelectionModel().getSelectedItem();
                                query = "DELETE FROM `nutritionniste` WHERE id_nutrit  ="+nutritionniste.getIdnut();
                                connection = DbConnect.getConnect();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.execute();
                                refreshTable();
                                notificationsup();
                                
                            } catch (SQLException ex) {
                                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                           

                          

                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            
                            nutritionniste = nutTable.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader ();
                            loader.setLocation(getClass().getResource("/tableView/addNut.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                             AddNutController addNutController = loader.getController();
                            addNutController.setUpdate(true);
                            addNutController.setTextField(nutritionniste.getIdnut(), nutritionniste.getNom(), 
                                    nutritionniste.getPrenom(),nutritionniste.getAdress(), nutritionniste.getEmail(),nutritionniste.getTelephone());
                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UTILITY);
                            stage.show();
                            

                           

                        });

                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

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

    @FXML
    private void goHome(MouseEvent event) {
         try {
            Parent parent = FXMLLoader.load(getClass().getResource("/Home/Home.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

     public void notificationsup(){
     String title = "NUTRITIONNISTE SUPRIMER! ";
            String messagee = "nutritionniste a été supprimer!";
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(messagee);
            tray.setNotificationType(NotificationType.SUCCESS);
         }

    @FXML
    private void searchName(MouseEvent event) {
        //System.out.print("vvvvvvvvvvvvvv");
        recherche(rechId.getText());
            
    }
}
