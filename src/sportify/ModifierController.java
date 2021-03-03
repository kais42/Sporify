/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportify;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Kais
 */
public class ModifierController implements Initializable {
    
    @FXML
    TextField dd;
    
    @FXML
    TextField df;
    
    @FXML
    private TextField titre;
    
    @FXML
    private TextArea desc;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
    void initData(Event e) {
    
       
        titre.setText(String.valueOf(e.getId()));
        desc.setText(e.getDescription().toString());
        dd.setText(e.getDateDebut().toString());
        df.setText(String.valueOf(e.getDateFin()));
  }

}
