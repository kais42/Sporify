/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportify;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import jdk.nashorn.internal.objects.annotations.Property;
import models.Utilisateur;
import sportify.AddUtilisateurController;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author hocin
 */
public class TableView1Controller implements Initializable {

    @FXML
    private TableView<Utilisateur> utilisateursTable;
    @FXML
    private TableColumn<Utilisateur, String> idCol;
    @FXML
    private TableColumn<Utilisateur, String> nameCol;
    @FXML
    private TableColumn<Utilisateur, String> birthCol;
    @FXML
    private TableColumn<Utilisateur, String> adressCol;
    @FXML
    private TableColumn<Utilisateur, String> emailCol;
    @FXML
    private TableColumn<Utilisateur, String> editCol;
    
    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    Utilisateur utilisateur = null ;
    
    ObservableList<Utilisateur>  UtilisateurList = FXCollections.observableArrayList();

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
    private void getAddView(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("addUtilisateur.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(TableView1Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void refreshTable() {
        try {
            UtilisateurList.clear();
            
            query = "SELECT * FROM `user`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()){
                UtilisateurList.add(new  Utilisateur(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDate("birth"),
                        resultSet.getString("adress"),
                        resultSet.getString("email")));
                utilisateursTable.setItems(UtilisateurList);
                
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(TableView1Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
            String title = "FRESH! ";
            String message = "La liste des Utilisateurs est actualisée avec succés!";
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.seconds(4));
        
        
        
    }

    @FXML
    private void print(MouseEvent event) {
         try {
            Parent parent = FXMLLoader.load(getClass().getResource("mailing.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(TableView1Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    private void loadDate() {
        
        connection = DbConnect.getConnect();
        refreshTable();
        
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        birthCol.setCellValueFactory(new PropertyValueFactory<>("birth"));
        adressCol.setCellValueFactory(new PropertyValueFactory<>("adress"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        //add cell of button edit 
         Callback<TableColumn<Utilisateur, String>, TableCell<Utilisateur, String>> cellFoctory = (TableColumn<Utilisateur, String> param) -> {
            // make cell containing buttons
            final TableCell<Utilisateur, String> cell = new TableCell<Utilisateur, String>() {
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
                                utilisateur = utilisateursTable.getSelectionModel().getSelectedItem();
                                query = "DELETE FROM `user` WHERE id  ="+utilisateur.getId();
                                connection = DbConnect.getConnect();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.execute();
                                refreshTable();
                                String title = "Supprimé! ";
            String message = "L'utilisateur est supprimé avec succés!";
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setNotificationType(NotificationType.WARNING);
            tray.showAndDismiss(Duration.seconds(4));
                                
                            } catch (SQLException ex) {
                                Logger.getLogger(TableView1Controller.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                           

                          

                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            
                            utilisateur = utilisateursTable.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader ();
                            loader.setLocation(getClass().getResource("/tableView/addUtilisateur.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(TableView1Controller.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            AddUtilisateurController addUtilisateurController = loader.getController();
                            addUtilisateurController.setUpdate(true);
                            addUtilisateurController.setTextField(utilisateur.getId(), utilisateur.getName(), 
                                    utilisateur.getBirth().toLocalDate(),utilisateur.getAdress(), utilisateur.getEmail());
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
         utilisateursTable.setItems(UtilisateurList);
         
         
    }
    @FXML
    private void eventRedirect(MouseEvent event) {
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

    @FXML
    private void redirectevent(MouseEvent event) {
    }


    @FXML
    private void promoRedirect(MouseEvent event) {
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
    private void coursRedirect(MouseEvent event) {
        Stage stage = new Stage();
        Parent home;
        try {
            home = FXMLLoader.load(getClass().getResource("tableView.fxml"));
            Scene homescene = new Scene(home);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(homescene);
            app_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(EventlistController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void userRedirect(MouseEvent event) {
        Stage stage = new Stage();
        Parent home;
        try {
            home = FXMLLoader.load(getClass().getResource("tableView1.fxml"));
            Scene homescene = new Scene(home);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(homescene);
            app_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(EventlistController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void coachRedirect(MouseEvent event) {
                         Stage stage = new Stage();
        Parent home;
        try {
            home = FXMLLoader.load(getClass().getResource("FXMLDocument1.fxml"));
            Scene homescene = new Scene(home);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(homescene);
            app_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(EventlistController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void abonnementRedirect(MouseEvent event) {
        Stage stage = new Stage();
        Parent home;
        try {
            home = FXMLLoader.load(getClass().getResource("abonnementlist.fxml"));
            Scene homescene = new Scene(home);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(homescene);
            app_stage.show();
        } catch (IOException ex) {
            Logger.getLogger(EventlistController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

    
