/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.obaid.demo;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author User
 */
public class MainController implements Initializable {
    
    
    @FXML
    private TextField tfid;
    @FXML
    private TextField tfnom;
    @FXML
    private TextField tfcategorie;
    @FXML
    private TextField tfniveau;
    @FXML
    private TextField tfduree;
    @FXML
    private TableView<Cours> tvCours;
    @FXML
    private TableColumn<Cours, Integer> colId;
    @FXML
    private TableColumn<Cours, String> colNom;
    @FXML
    private TableColumn<Cours, String> colCategorie;
    @FXML
    private TableColumn<Cours, String> colNiveau;
    @FXML
    private TableColumn<Cours, Integer> colDuree;
    @FXML
    private Button btnInsert;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {        
        
          if(event.getSource() == btnInsert){
            insertRecord();
        }else if (event.getSource() == btnUpdate){
            updateRecord();
        }else if(event.getSource() == btnDelete){
            deleteButton();
        }
            
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        showCours();
    }  
    
    
    public Connection getConnection(){
        Connection conn;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sporify?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root","");
            return conn;
        }catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
    }
    
    public ObservableList<Cours> getCoursList(){
        ObservableList<Cours> coursList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM cours";
        Statement st;
        ResultSet rs;
        
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Cours cours;
            while(rs.next()){
                cours = new Cours(rs.getInt("id"), rs.getString("nom"), rs.getString("categorie"), rs.getString("niveau"),rs.getInt("duree"));
                coursList.add(cours);
            }
                
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return coursList;
    }
    
    public void showCours(){
        ObservableList<Cours> list = getCoursList();
        colId.setCellValueFactory(new PropertyValueFactory<Cours, Integer>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<Cours, String>("nom"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<Cours, String>("categorie"));
        colNiveau.setCellValueFactory(new PropertyValueFactory<Cours, String>("niveau"));
        colDuree.setCellValueFactory(new PropertyValueFactory<Cours, Integer>("duree"));
        tvCours.setItems(list);
}
    public void insertRecord(){
        String query = "INSERT INTO cours VALUES (" + tfid.getText() + ",'" + tfnom.getText() + "','" + tfcategorie.getText() + "'," + tfniveau.getText() + "," 
                + tfduree.getText() + ",)";
        executeQuery(query);
        showCours();
    }
    private void updateRecord(){
        String query = "UPDATE cours SET nom  = '" + tfnom.getText() + "', categorie = '" + tfcategorie.getText() + "', niveau = " + tfniveau.getText() + ", duree = " + tfduree.getText() + " WHERE id = " + tfid.getText() + "";
        executeQuery(query);
        showCours();
    }
    private void deleteButton(){
        String query = "DELETE FROM cours WHERE id =" + tfid.getText() + "";
        executeQuery(query);
        showCours();
    }

    private void executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;
        try{
            st = conn.createStatement();
            st.executeUpdate(query);
        }catch(SQLException ex){
        }
    }
    }