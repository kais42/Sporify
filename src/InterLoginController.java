/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tableView.TableViewController;

/**
 * FXML Controller class
 *
 * @author soltani med ala
 */
public class InterLoginController implements Initializable {

    @FXML
    private TextField txtlogin;
    @FXML
    private TextField txtpwd;
    @FXML
    private Button cnx;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void cnxaction(ActionEvent event) {
       
        if(txtlogin.getText().toString().equals("ala")&&txtpwd.getText().equals("ala")){
            
            try {
            Parent parent = FXMLLoader.load(getClass().getResource("/Home/Home.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
            Stage current = (Stage)((Node) event.getSource()).getScene().getWindow();
           current.close();
        } catch (IOException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        }
        
         else if(txtlogin.getText().toString().equals("soltani")&&txtpwd.getText().equals("soltani")){
        
              try {
            Parent parent = FXMLLoader.load(getClass().getResource("/Adherent/Homeadhe.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
            Stage current = (Stage)((Node) event.getSource()).getScene().getWindow();
           current.close();
        } catch (IOException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }
    
}
