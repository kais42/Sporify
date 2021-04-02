/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReclamationAvis;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.controlsfx.control.Rating;

/**
 * FXML Controller class
 *
 * @author soltani med ala
 */
public class AvissController implements Initializable {

    @FXML
    private TextArea desc;
    @FXML
    private Button Envoyer;
    @FXML
    private Rating rating;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
