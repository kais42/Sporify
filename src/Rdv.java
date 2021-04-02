/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OperationRDV;

import Home.HomeController;
import RDV.RDVController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import helpers.DbConnect;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.util.Duration;
import models.Nutritionniste;
import models.Rendvous;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author soltani med ala
 */
public class Rdv implements Initializable {

    @FXML
    private TableColumn<Rendvous, String> editCol;
    @FXML
    private FontAwesomeIconView homeId;
    @FXML
    private TableColumn<Rendvous, Integer> id_rdv;
    @FXML
    private TableColumn<Rendvous, String > iduser;
    @FXML
    private TableColumn<Rendvous, String> idnut;
    @FXML
    private TableColumn<Rendvous, String> date;
    @FXML
    private TableView<Rendvous> RdvTable;
     
    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
     Rendvous Rendvous = null ;
    ObservableList<Rendvous>  RendvousList = FXCollections.observableArrayList();
    @FXML
    private FontAwesomeIconView add;
    @FXML
    private FontAwesomeIconView refresh;
   
    @FXML
    private FontAwesomeIconView searchId;
    /**
     * Initializes the controller class.
     */
    private int totalToApprove;
    @FXML
    private TextField rechId;
    @FXML
    private Label nut;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       rdvnonapprouver();
        loadDate();
    }    
void rdvnonapprouver()
        {
            Statement stmt = null;
           String query = null;
            Connection conn = DbConnect.getConnect();
      query = "SELECT COUNT(*)as total FROM `rdv` WHERE approuver=0";
          
        try {
             
           ResultSet rs = conn.createStatement().executeQuery(query);
          //  System.out.println("vvvvvvv"+rs.getInt(1));
           
   while (rs.next()) {
       totalToApprove=rs.getInt("total");
      // System.out.println("aaaaaaaaaa"+rs.getInt("total"));
      if (totalToApprove>0){
       notifeprouve();
      }else{
      
          notifnonprouve();
      }
 }
        } catch (SQLException ex) {
            Logger.getLogger(Rdv.class.getName()).log(Level.SEVERE, null, ex);
        }


}
    
    
    @FXML
    private void close(MouseEvent event) {
         Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void getAddView(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/OperationRDV/AddRdv.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Rdv.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void refreshTable() {
         try {
            RendvousList.clear();
            
            query = "SELECT * FROM `rdv`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()){
                RendvousList.add(new  Rendvous(
                        
                        resultSet.getInt("id_rdv"),
                        resultSet.getString("id_user"),
                        resultSet.getString("id_nut"),
                        resultSet.getString("date_rdv")
                        
                       ));
              
                RdvTable.setItems(RendvousList);
          
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Rdv.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
     private void loadDate() {
        
        connection = DbConnect.getConnect();
         refreshTable();
        
        id_rdv.setCellValueFactory(new PropertyValueFactory<>("idR"));
        iduser.setCellValueFactory(new PropertyValueFactory<>("iduse"));
        idnut.setCellValueFactory(new PropertyValueFactory<>("idnut"));
        date.setCellValueFactory(new PropertyValueFactory<>("dateR"));
       
        
        //add cell of button edit 
         Callback<TableColumn<Rendvous , String>, TableCell<Rendvous , String>> cellFoctory = (TableColumn<Rendvous , String> param) -> {
            // make cell containing buttons
            final TableCell<Rendvous , String> cell = new TableCell<Rendvous , String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        FontAwesomeIconView acceptIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK); 
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

                          


                        acceptIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:green"
                        ); 
                         
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
                                Rendvous= RdvTable.getSelectionModel().getSelectedItem();
                                query = "DELETE FROM `rdv` WHERE id_rdv ="+Rendvous.getIdR();
                                connection = DbConnect.getConnect();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.execute();
                                refreshTable();
                                notification();
                                
                            } catch (SQLException ex) {
                                Logger.getLogger(Rdv.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                           

                          

                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            
                            Rendvous = RdvTable.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader ();
                            loader.setLocation(getClass().getResource("/OperationRDV/AddRdv.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(Rdv.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                             AddRdvController AddRdvController = loader.getController();
                            AddRdvController.setUpdate(true);
                            
                            AddRdvController.setTextField(Integer.valueOf(Rendvous.getIdR()), Rendvous.getIduse(), 
                                    Rendvous.getIdnut(),Rendvous.getDateR());
                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UTILITY);
                            stage.show();
                            

                           

                        });
                         acceptIcon.setOnMouseClicked((MouseEvent event) -> {
                                Rendvous = RdvTable.getSelectionModel().getSelectedItem();
                             
                            query = "UPDATE `rdv` SET "
                                        
                    + "`approuver`=1"
                    +" WHERE id_rdv = '"+Rendvous.getIdR()+"'";
                             System.out.println(""+query);
                      try {
preparedStatement = connection.prepareStatement(query);
 
      preparedStatement.execute();
        }
            catch (SQLException ex) {
            
        }

        
                             
                         });

                        HBox managebtn = new HBox(acceptIcon,editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));
                        HBox.setMargin(acceptIcon, new Insets(2, 4, 0, 1));

                        setGraphic(managebtn);

                        setText(null);

                    }
                }

            };

            return cell;
        };
         editCol.setCellFactory(cellFoctory);
         RdvTable.setItems(RendvousList);
         
         
    }

    @FXML
    private void goHome(MouseEvent event) {
         try {
            Parent parent = FXMLLoader.load(getClass().getResource("/Home/Home.fxml"));
               Stage mainStage = new Stage();
                Scene scene = new Scene(parent);
                mainStage.setScene(scene);
                mainStage.show();
//          
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
     public void notification(){
     String title = "REGIME SUPRIMER! ";
            String messagee = "Regime a été supprimer!";
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(messagee);
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.seconds(4));
            
    }
      public void notifeprouve(){
     String title = "RENDEZ-VOUS NON ACCEPTER! ";
            String messagee = "Vous-avez  "+ totalToApprove +"  rendz-vous en attente!";
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(messagee);
            tray.setNotificationType(NotificationType.INFORMATION);
            tray.showAndDismiss(Duration.seconds(6));
      }
      public void notifnonprouve(){
     String title = "aucun rendez-vous en attente! ";
           // String messagee = "Vous-avez  "+ totalToApprove +"  rendz-vous en attente!";
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            //tray.setMessage(messagee);
            tray.setNotificationType(NotificationType.INFORMATION);
            tray.showAndDismiss(Duration.seconds(6));
      }
private void recherche(String nom) {
//        try {
//            RendvousList.clear();
//            
//            query = "SELECT * FROM `rdv` where `id_user` like '%"+nom+"%' or `id_nut` like '%"+nom+"%'or `id_rdv` like '%"+nom+"%'";
//            preparedStatement = connection.prepareStatement(query);
//            resultSet = preparedStatement.executeQuery();
//            
//            while (resultSet.next()){
//                RendvousList.add(new Rendvous(
//                        resultSet.getInt("id_rdv"),
//                        resultSet.getString("iduser"),
//                        resultSet.getString("idnut")));
//                RdvTable.setItems(RendvousList);
//                
//            }
//            
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(RDVController.class.getName()).log(Level.SEVERE, null, ex);
//        }
                 
     try {
            RendvousList.clear();
            
           query = "SELECT * FROM `rdv` where `id_user` like '%"+nom+"%' or `id_nut` like '%"+nom+"%'or `id_rdv` like '%"+nom+"%'";
           System.out.println("recherche"+query); 
           preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()){
                RendvousList.add(new  Rendvous(
                        
                        resultSet.getInt("id_rdv"),
                        resultSet.getString("id_user"),
                        resultSet.getString("id_nut"),
                        resultSet.getString("date_rdv")
                        
                       ));
              
                RdvTable.setItems(RendvousList);
          
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Rdv.class.getName()).log(Level.SEVERE, null, ex);
        }
         }

    @FXML
    private void searchName(MouseEvent event) {
        
        recherche(rechId.getText().toString());
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
