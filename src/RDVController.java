/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDV;

import Home.HomeController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import helpers.DbConnect;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author soltani med ala
 */
public class RDVController implements Initializable {

    @FXML
    private FontAwesomeIconView homePage;
    @FXML
    private FontAwesomeIconView goRDV;
    @FXML
    private HBox send;
    @FXML
    private TextField nomId;
    @FXML
    private TextField prenomId;
    @FXML
    private TextArea DescId;
    @FXML
    private TextField emailId;
    @FXML
    private DatePicker dateId;
    @FXML
    private Button sendId;
    @FXML
    private FontAwesomeIconView closeid;
    String nom;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
      @FXML
    private void goHome(MouseEvent event) {
           try {
            Parent parent = FXMLLoader.load(getClass().getResource("/Adherent/Homeadhe.fxml"));
               Stage mainStage = new Stage();
                Scene scene = new Scene(parent);
                mainStage.setScene(scene);
                mainStage.show();
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
     @FXML
    private void goRDV(MouseEvent event) {
           try {
            Parent parent = FXMLLoader.load(getClass().getResource("/RDV/DemandeRDV.fxml"));
               Stage mainStage = new Stage();
                Scene scene = new Scene(parent);
                mainStage.setScene(scene);
                mainStage.show();
//           
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    void setTextField(String emailCol,String nom){
        this.emailId.setText(emailCol);
        this.nom=nom;
    }

    @FXML
    private void sendMail(ActionEvent event) {
        String name=nomId.getText();
        String prenom =prenomId.getText();
        String Des =DescId.getText();
//       String date=dateId.getValue().toString();

        if (name.isEmpty()  || prenom.isEmpty()||Des.isEmpty()/*||date.isEmpty()*/) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tout les champs");
            alert.showAndWait();
            notificationalert();
        } else{
            ajout();
        sendEmail(); 
        notification();
        clean();
        }
    }
    private void clean(){
        nomId.setText(null);
        prenomId.setText(null);
        DescId.setText(null);
       
}

    private void sendEmail() {
       	String Msg;
    
        final String username = "nsporify@gmail.com";
        final String password = "esprit123*";

        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("nsporify@gmail.com"));//ur email
            message.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(/*"nessrineselmi215@gmail.com"*/emailId.getText()));//u will send to
            message.setSubject("DEMANDE RENDEZ-VOUS");
            String msge="nom adherent : "+nomId.getText()+"\n"+
                  "prenom adherent :"+  prenomId.getText()+"\n"+
                  "Date RDV Souhaité :"+  dateId.getValue().toString()+"\n"+
                   "Description :"+   DescId.getText();
            message.setText(msge);
            Transport.send(message);
      
      
            Msg="true";
    	   
        } catch (Exception e) {
          //  return e.toString();
        }
    
    }
    
    void  ajout ()
    {
          Statement stmt = null;
           String query = null;
            Connection conn = DbConnect.getConnect();
           
          query = "INSERT INTO `rdv`(`id_user`,`id_nut`,`date_rdv`) VALUES ('"+nomId.getText()+"','"+this.nom+"','"+dateId.getValue()+"')";  
        try {
             stmt = conn.createStatement();
            stmt.executeUpdate(query);
            
        } catch (SQLException ex) {
            Logger.getLogger(RDVController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    
    
    
    }
    
    
    public void notificationalert(){
     String title = "EMAIL NON ENVOYER! ";
            String messagee = "mail ne pas été envoiyer !";
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(messagee);
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.seconds(4));
    }
    public void notification(){
     String title = "EMAIL ENVOYER! ";
            String messagee = "mail a été envoiyer avec succés!";
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(messagee);
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.seconds(4));
    }
    public void notificationvariable(){
     String title = "REMPLIR TOUT LES CHAMPS! ";
            String messagee = "Vous devez remplir tout les chapms!";
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(messagee);
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.seconds(4));
    }
    @FXML
    private void close(MouseEvent event) {
         Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
