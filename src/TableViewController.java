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
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import models.Cours;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author User
 */
public class TableViewController implements Initializable {

    @FXML
    private TableView<Cours> coursTable;
    @FXML
    private TableColumn<Cours, Integer> idCol;
    @FXML
    private TableColumn<Cours, String> nomCol;
    @FXML
    private TableColumn<Cours, String> categCol;
    @FXML
    private TableColumn<Cours, String> niveauCol;
    @FXML
    private TableColumn<Cours, Integer> dureeCol;
    @FXML
    private TableColumn<Cours, String> editCol;
    @FXML
    private TextField filterField;
    
    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    Cours cours = null ;
    ObservableList<Cours> CoursList = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadDate();
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Cours> filteredData = new FilteredList<>(CoursList, b -> true);
		
		// 2. Set the filter Predicate whenever the filter changes.
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(cours -> {
				// If filter text is empty, display all persons.
								
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				// Compare first name and last name of every person with filter text.
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (cours.getNom().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
					return true; // Filter matches first name.
				} else if (cours.getCategorie().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches last name.
				}
				else if (String.valueOf(cours.getDuree()).indexOf(lowerCaseFilter)!=-1)
				     return true;
				     else  
				    	 return false; // Does not match.
			});
		});
		
		// 3. Wrap the FilteredList in a SortedList. 
		SortedList<Cours> sortedData = new SortedList<>(filteredData);
		
		// 4. Bind the SortedList comparator to the TableView comparator.
		// 	  Otherwise, sorting the TableView would have no effect.
		sortedData.comparatorProperty().bind(coursTable.comparatorProperty());
		
		// 5. Add sorted (and filtered) data to the table.
		coursTable.setItems(sortedData);
    }   

    @FXML
    private void getAddView(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("AddCours.fxml"));
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
            CoursList.clear();
            query = "SELECT * FROM `cours`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
               CoursList.add(new Cours(
                       resultSet.getInt("id"),
                       resultSet.getString("nom"),
                       resultSet.getString("categorie"),
                       resultSet.getString("niveau"),
                       resultSet.getInt("duree")));
               coursTable.setItems(CoursList);
               String title = "Succes! ";
        String message = "La liste est actualisée avec succés";

        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setNotificationType(NotificationType.SUCCESS);
        tray.showAndDismiss(Duration.seconds(4));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void print(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("mailing.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
        @FXML
    private void playit(MouseEvent event) {
    try {
        Parent parent = FXMLLoader.load(getClass().getResource("mediaplayer.fxml"));
        
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        //scene.getStyleSheets().addPath("style.css");
        stage.setTitle("VideoPlayer");
        stage.setFullScreenExitHint("Press ESC or Double click to exit the full Screen");
        
        scene.setOnMouseClicked(new EventHandler <MouseEvent>(){
            @Override
            public void handle(MouseEvent clicked) {
                if(clicked.getClickCount() == 2){
                    if(!stage.isFullScreen()){
                        stage.setFullScreen(true);
                    }else{
                        stage.setFullScreen(false);
                    }
                    
                }
            }
            
        });
        
        stage.setScene(scene);
        stage.show();
   
                } catch (IOException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
           

    @FXML
    private void close(MouseEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
               String title = "Restez en forme !";
        String message = "Au revoir et à la prochaine!";

        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setNotificationType(NotificationType.INFORMATION);
        tray.showAndDismiss(Duration.seconds(4));
    }

    private void loadDate() {
        connection = DbConnect.getConnect();
        refreshTable();
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        categCol.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        niveauCol.setCellValueFactory(new PropertyValueFactory<>("niveau"));
        dureeCol.setCellValueFactory(new PropertyValueFactory<>("duree"));
        Callback<TableColumn<Cours, String>, TableCell<Cours, String>> cellFoctory = (TableColumn<Cours, String> param) -> {
            // make cell containing buttons
          final TableCell<Cours, String> cell = new TableCell<Cours, String>() {
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
                                cours = coursTable.getSelectionModel().getSelectedItem();
                                query = "DELETE FROM `cours` WHERE id  ="+cours.getId();
                                connection = DbConnect.getConnect();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.execute();
                                refreshTable();
                            
                                String title = " Suppression Réussie! ";
                                String message = "Le cours est supprimé avec succés!";
                                TrayNotification tray = new TrayNotification();
                                 tray.setTitle(title);
                                 tray.setMessage(message);
                                 tray.setNotificationType(NotificationType.SUCCESS);
                                 tray.showAndDismiss(Duration.seconds(4));
                                 
                            } catch (SQLException ex) {
                                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            
                            cours = coursTable.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader ();
                            loader.setLocation(getClass().getResource("/tableView/AddCours.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            AddCoursController addCoursController = loader.getController();
                            addCoursController.setUpdate(true);
                            addCoursController.setTextField(cours.getId(), cours.getNom(), 
                                    cours.getCategorie(),cours.getNiveau(), cours.getDuree());
                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UTILITY);
                            stage.show();
                            
                            String title = " Suppression Réussie! ";
                            String message = "Le cours est mis à jour avec succés!";
                            TrayNotification tray = new TrayNotification();
                            tray.setTitle(title);
                            tray.setMessage(message);
                            tray.setNotificationType(NotificationType.SUCCESS);
                            tray.showAndDismiss(Duration.seconds(4));
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
        coursTable.setItems(CoursList);
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
