/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportify;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kais
 */
public class StatController implements Initializable {
    final static String s1 = "Saison 1";
    final static String s2 = "Saison 2";
    final static String s3 = "Saison 3";
    final static String s4 = "Saison 4";
    
    @FXML
    private BarChart<?, ?> chart;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        EventDAO dao = new EventDAO();
        ArrayList statList = dao.getStat();
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        chart.setTitle("Nombre d'evenement par saison");
        xAxis.setLabel("Saison");       
        yAxis.setLabel("Nombre d'evenement");
        
        XYChart.Series series1 = new XYChart.Series();
        //series1.setName("2003");       
        series1.getData().add(new XYChart.Data(s1, statList.get(0)));
        series1.getData().add(new XYChart.Data(s2, statList.get(1)));
        series1.getData().add(new XYChart.Data(s3, statList.get(2)));
        series1.getData().add(new XYChart.Data(s4, statList.get(3)));
        
//        
//        series1.getData().add(new XYChart.Data(s1, 4));
//        series1.getData().add(new XYChart.Data(s2, 3));
//        series1.getData().add(new XYChart.Data(s3, 4));
//        series1.getData().add(new XYChart.Data(s4, 5));
        
        chart.getData().addAll(series1);
        // TODO
    }    

    @FXML
    private void promoRedirect(ActionEvent event) {
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
    private void eventRedirect(ActionEvent event) {
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
    
}
