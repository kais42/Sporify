/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReclamationAvis;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import models.Nutritionniste;
import models.Avis;
import tableView.AddNutController;
import tableView.TableViewController;

/**
 * FXML Controller class
 *
 * @author soltani med ala
 */
public class AvisController implements Initializable {

   
    @FXML
    private TableColumn<Avis, Integer> idCol;
    @FXML
    private TableColumn<Avis, String> nomCol;
    @FXML
    private TableColumn<Avis, String> prenomCol;
    
    @FXML
    private TableColumn<Avis, String> reclcol;
    private TableColumn<Avis, String> editCol;
    @FXML
    private FontAwesomeIconView homeId;
      String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    Avis avis = null ;
    
    ObservableList<Avis>  avislist = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Avis, String> aviscol;
    @FXML
    private TableView<Avis> avisTable;
    @FXML
    private Label nut;
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDate();
        // TODO
    }    
  @FXML
    private void refreshTable(MouseEvent event) {
        
    }
private void refresh(){

    try {
            avislist.clear();
                       
            query = "SELECT * FROM `avis`";
                      

            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                                       

                avislist.add(new  Avis(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("avis"),
                        resultSet.getString("reclamation")));
                        
                         
                avisTable.setItems(avislist);
                
            }
            
           
        } catch (SQLException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
                }
   
    @FXML
    private void close(MouseEvent event) {
         Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
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
    private void loadDate() {
       
        connection = DbConnect.getConnect();
        refresh();
      
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        reclcol.setCellValueFactory(new PropertyValueFactory<>("reclamation"));
       aviscol.setCellValueFactory(new PropertyValueFactory<>("avis"));
        
        //add cell of button edit 
         Callback<TableColumn<Avis, String>, TableCell<Avis, String>> cellFoctory = (TableColumn<Avis, String> param) -> {
            // make cell containing buttons
            final TableCell<Avis, String> cell = new TableCell<Avis, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
            

                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                       // FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#ff1744;"
                        );
                        
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            
                            try {
                                avis = avisTable.getSelectionModel().getSelectedItem();
                                query = "DELETE FROM `avis` WHERE `id`  ="+avis.getId();
                                connection = DbConnect.getConnect();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.execute();
                                refresh();
                            
                                
                            } catch (SQLException ex) {
                                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                                                                                
                       });
                        HBox managebtn = new HBox( deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                       

                        setGraphic(managebtn);

                        setText(null);

                    }
                }

            };

            return cell;
        };
//         editCol.setCellFactory(cellFoctory);
         avisTable.setItems(avislist);
    
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
