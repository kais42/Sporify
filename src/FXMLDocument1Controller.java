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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import javafx.util.Duration;
import models.Coachs;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author OMEN
 */
public class FXMLDocument1Controller implements Initializable {

    @FXML
    private TextField tfid;
    @FXML
    private TextField tfnom;
    @FXML
    private TextField tfprenom;
    @FXML
    private TextField tfprix;
    @FXML
    private TextField tftel;
    @FXML
    private TextField tfmail;
    @FXML
    private TableColumn<Coachs, Integer> colid;
    @FXML
    private TableColumn<Coachs, String> colnom;
    @FXML
    private TableColumn<Coachs, String> colprenom;
    @FXML
    private TableColumn<Coachs, Integer> colprix;
    @FXML
    private TableColumn<Coachs, Integer> coltel;
    @FXML
    private TableColumn<Coachs, String> colmail;
    @FXML
    private TableView<Coachs> tableco;
    @FXML
    private Button AddC;
    @FXML
    private TextField filteredtext;

    ObservableList<Coachs> listM;
    ObservableList<Coachs> dataList;
    ObservableList<Coachs> listn = FXCollections.observableArrayList();

    int index = -1;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    @FXML
    private Button Update;
    @FXML
    private Button Delete;
    

    

    @FXML
    private void AddC(ActionEvent event) {
        conn = DbConnect.getConnect();
        String sql = "insert into coachs (id,nom,prenom,prix,tel,mail)values (?,?,?,?,?,?)";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, tfid.getText());
            pst.setString(2, tfnom.getText());
            pst.setString(3, tfprenom.getText());
            pst.setString(4, tfprix.getText());
            pst.setString(5, tftel.getText());
            pst.setString(6, tfmail.getText());
            pst.execute();
            UpdateTable();
            String title = "Added! ";
            String message = "L'ajout est effectué avec succés";

            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.seconds(4));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void AddC() {
        conn = DbConnect.getConnect();
        String sql = "insert into coachs (id,nom,prenom,prix,tel,mail)values (?,?,?,?,?,?)";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, tfid.getText());
            pst.setString(2, tfnom.getText());
            pst.setString(3, tfprenom.getText());
            pst.setString(4, tfprix.getText());
            pst.setString(5, tftel.getText());
            pst.setString(6, tfmail.getText());
            pst.execute();
            
            UpdateTable();
            String title = "Added! ";
            String message = "L'ajout est effectué avec succés";

            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.seconds(4));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    public void getSelected(MouseEvent event) {
        index = tableco.getSelectionModel().getSelectedIndex();
        if (index <= -1) {

            return;
        }

        tfid.setText(colid.getCellData(index).toString());
        tfnom.setText(colnom.getCellData(index).toString());
        tfprenom.setText(colprenom.getCellData(index).toString());
        tfprix.setText(colprix.getCellData(index).toString());
        tftel.setText(coltel.getCellData(index).toString());
        tfmail.setText(colmail.getCellData(index).toString());
    }

    @FXML
    private void Update(ActionEvent event) {
        try {
            conn = DbConnect.getConnect();
            String value1 = tfid.getText();
            String value2 = tfnom.getText();
            String value3 = tfprenom.getText();
            String value4 = tfprix.getText();
            String value5 = tftel.getText();
            String value6 = tfmail.getText();
            String sql = "update coachs set id =" + value1 + ",nom= '" + value2 + "', prenom= '" + value3 + "', "
                    + "prix= '" + value4 + "', tel= '" + value5 + "', mail= '" + value6 + "' where id= '" + value1 +"'";
            pst = conn.prepareStatement(sql);
            pst.execute();
            
            UpdateTable();
            String title = "Updated! ";
            String message = "L'update est effectué avec succés";

            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.seconds(10));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void Update() {

        try {
            conn = DbConnect.getConnect();
            String value1 = tfid.getText();
            String value2 = tfnom.getText();
            String value3 = tfprenom.getText();
            String value4 = tfprix.getText();
            String value5 = tftel.getText();
            String value6 = tfmail.getText();
            String sql = "update coachs set id =" + value1 + ",nom= '" + value2 + "', prenom= '" + value3 + "', "
                    + "prix= '" + value4 + "', tel= '" + value5 + "', mail= '" + value6 + "' where id= '" + value1 +"'";
            pst = conn.prepareStatement(sql);
            pst.execute();
            
            UpdateTable();
            String title = "Updated! ";
            String message = "L'update est effectué avec succés";

            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.seconds(10));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    private void Delete(ActionEvent event) {
        conn = DbConnect.getConnect();
        String sql = "delete from coachs where id=?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, tfid.getText());
            pst.execute();
            String title = "Deleted! ";
            String message = "La suppression est effectué avec succés";

            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.seconds(4));
            UpdateTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void Delete() {
        conn = DbConnect.getConnect();
        String sql = "delete from coachs where id=?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, tfid.getText());
            pst.execute();
            String title = "Deleted! ";
            String message = "La suppression est effectué avec succés";

            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.seconds(4));
            UpdateTable();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    public void UpdateTable() {
        colid.setCellValueFactory(new PropertyValueFactory<Coachs, Integer>("id"));
        colnom.setCellValueFactory(new PropertyValueFactory<Coachs, String>("nom"));
        colprenom.setCellValueFactory(new PropertyValueFactory<Coachs, String>("prenom"));
        colprix.setCellValueFactory(new PropertyValueFactory<Coachs, Integer>("prix"));
        coltel.setCellValueFactory(new PropertyValueFactory<Coachs, Integer>("tel"));
        colmail.setCellValueFactory(new PropertyValueFactory<Coachs, String>("mail"));
        listM = DbConnect.getDataCoachs();
        tableco.setItems(listn);
        tableco.setItems(dataList);
        tableco.setItems(listM);
    }

    void search_coach() {
        colid.setCellValueFactory(new PropertyValueFactory<Coachs, Integer>("id"));
        colnom.setCellValueFactory(new PropertyValueFactory<Coachs, String>("nom"));
        colprenom.setCellValueFactory(new PropertyValueFactory<Coachs, String>("prenom"));
        colprix.setCellValueFactory(new PropertyValueFactory<Coachs, Integer>("prix"));
        coltel.setCellValueFactory(new PropertyValueFactory<Coachs, Integer>("tel"));
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
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        
        colid.setCellValueFactory(new PropertyValueFactory<Coachs, Integer>("id"));
        colnom.setCellValueFactory(new PropertyValueFactory<Coachs, String>("nom"));
        colprenom.setCellValueFactory(new PropertyValueFactory<Coachs, String>("prenom"));
        colprix.setCellValueFactory(new PropertyValueFactory<Coachs, Integer>("prix"));
        coltel.setCellValueFactory(new PropertyValueFactory<Coachs, Integer>("tel"));
        colmail.setCellValueFactory(new PropertyValueFactory<Coachs, String>("mail"));
        tableco.setItems(listn);
        tableco.setItems(dataList);
        tableco.setItems(listM);
        UpdateTable();
        search_coach();
    }

    
    @FXML
    private void Redirect(MouseEvent event) throws IOException {
      try {
            Parent parent = FXMLLoader.load(getClass().getResource("ratings.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void changeScreenButtonPushed(ActionEvent event) throws IOException {
       try {
            Parent parent = FXMLLoader.load(getClass().getResource("ratings.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void redirectmail(MouseEvent event) throws IOException {
     /*   Parent rating_page_parent = FXMLLoader.load(getClass().getResource("mailing.fxml"));
        Scene rating_page_scene = new Scene(rating_page_parent);
        Stage app_stage  = (Stage)  ((Node) event.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene (rating_page_scene);
        app_stage.show(); */
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
    private void changescreenmail(ActionEvent event) throws IOException {
      /*  Parent rating_page_parent = FXMLLoader.load(getClass().getResource("mailing.fxml"));
        Scene rating_page_scene = new Scene(rating_page_parent);
        Stage app_stage  = (Stage)  ((Node) event.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene (rating_page_scene);
        app_stage.show(); */
    }
     @FXML
    private void redirectevent(MouseEvent event) {
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
    



    
    

