/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IMC;

import Home.HomeController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tableView.TableViewController;

/**
 * FXML Controller class
 *
 * @author soltani med ala
 */
public class CalculeIMCController implements Initializable {

    @FXML
    private TextField tailleId;
    @FXML
    private TextField poidsId;
    @FXML
    private Button calculeBTN;
    @FXML
    private Label reponse;
    @FXML
    private Button consultRegime;
    @FXML
    private Label resultIMC;
    @FXML
    private FontAwesomeIconView homePage;
    @FXML
    private FontAwesomeIconView closeid;
    @FXML
    private Label nut;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.resultIMC.setVisible(false);
        this.reponse.setVisible(false);
        this.consultRegime.setVisible(false);
    }    

    @FXML
    private void CalculeIMC(MouseEvent event) {
         double poids = Double.parseDouble(this.poidsId.getText());
       double taille =Double.parseDouble(this.tailleId.getText());
       
       System.out.print(poids/taille);
 double imc=poids/(taille*taille);
    
    this.reponse.setVisible(true);
     this.resultIMC.setVisible(true);
     resultIMC.setText(""+imc);

 if (imc < 18.5)
{
 reponse.setText("votre indicateur est inferieur au IMC normal Voulez vous suivre un regime ? ");
 consultRegime.setVisible(true);
}
else if (imc < 25.0)
{
 reponse.setText("Felicitation votre IMC est dans l'ideal");
}
else if (imc > 30.0)
{
 reponse.setText("votre indicateur est superieur au IMC normal Voulez vous suivre un regime ?");
 consultRegime.setVisible(true);
}
    }

    @FXML
    private void goHome(MouseEvent event) {
           try {
            Parent parent = FXMLLoader.load(getClass().getResource("/Adherent/Homeadhe.fxml"));
               Stage mainStage = new Stage();
                Scene scene = new Scene(parent);
                mainStage.setScene(scene);
                mainStage.show();
//          
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void goProgrameMinceur(ActionEvent event) {
                   try {
            Parent parent = FXMLLoader.load(getClass().getResource("/Regime/regime.fxml"));
               Stage mainStage = new Stage();
                Scene scene = new Scene(parent);
                mainStage.setScene(scene);
                mainStage.show();
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void close(MouseEvent event) {
         Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
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
