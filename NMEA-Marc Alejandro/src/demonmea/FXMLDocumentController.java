/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demonmea;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import net.sf.marineapi.nmea.event.AbstractSentenceListener;
import net.sf.marineapi.nmea.io.SentenceReader;
import net.sf.marineapi.nmea.sentence.HDGSentence;
import net.sf.marineapi.nmea.sentence.MDASentence;
import net.sf.marineapi.nmea.sentence.MWVSentence;
import net.sf.marineapi.nmea.util.Position;
import sun.plugin.ClassLoaderInfo;

/**
 *
 * @author jsoler
 */

    public class FXMLDocumentController implements Initializable {
    
    private Model model;
  
    private Parent vistaBarco;
    private Parent vistaGPS;
    private Parent vistaViento;
    @FXML
    private VBox vbox;

    @FXML
    private Button barcoVista;
    @FXML
    private Button gpsVista;
    @FXML
    private Button vientoVista;
    @FXML
    private Button cargarFichero;
    
    private boolean dia = true;
    @FXML
    private Button diaNoche;
    @FXML
    private ImageView boatImage;
    @FXML
    private ImageView gpsImage;
    @FXML
    private ImageView atmImage;
    @FXML
    private ImageView modoImage;
    @FXML
    private ImageView ficheroImagen;
    @FXML
    private ImageView prtadaImage;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model=Model.getInstance();
        
         try {
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("VistaBarco.fxml"));
            vistaBarco = loader1.load();

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("VistaGPS.fxml"));
            vistaGPS = loader2.load();
            
            FXMLLoader loader3 = new FXMLLoader(getClass().getResource("VistaV.fxml"));
            vistaViento = loader3.load();
            
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

          LocalTime t = LocalTime.now();
          int hora = t.getHour();
        
         if (hora > 20 || hora < 7) {
                vbox.getStylesheets().add("resources/EstiloNoche.css");
                modoImage.setImage(new Image("resources/sunicon.png"));
                boatImage.setImage(new Image("resources/boaticonb.png"));
                gpsImage.setImage(new Image("resources/gpsiconb.png"));
                atmImage.setImage(new Image("resources/weathericonb.png"));
                ficheroImagen.setImage(new Image("resources/subirficherob.png"));
                prtadaImage.setImage(new Image("resources/portadanoche.png"));
                dia = false;
        } else {
                vbox.getStylesheets().add("resources/EstiloDia.css");
                modoImage.setImage(new Image("resources/moonicon.png"));
                boatImage.setImage(new Image("resources/boaticon.png"));
                gpsImage.setImage(new Image("resources/gpsicon.png"));
                atmImage.setImage(new Image("resources/weathericon.png"));
                ficheroImagen.setImage(new Image("resources/subirfichero.png"));
                prtadaImage.setImage(new Image("resources/portadadia.png"));
           dia = true;
        }
         
    }


    @FXML
    private void cargarFichero(ActionEvent event) throws FileNotFoundException {
        FileChooser ficheroChooser = new FileChooser();
        ficheroChooser.setSelectedExtensionFilter(new ExtensionFilter("Ficheros NMEA", "*.NMEA"));
        ficheroChooser.setTitle("Fichero datos NMEA");
        
        File ficheroNMEA = ficheroChooser.showOpenDialog(cargarFichero.getScene().getWindow());
       
       
        if (ficheroNMEA != null  ) {
            String ext = extension(ficheroNMEA.getPath());
            if (ext.equals("NMEA")){model.addSentenceReader(ficheroNMEA);
            HBox hbox = (HBox) cargarFichero.getParent();
            hbox.getChildren().remove(3);
            }
        }
        
        
    }

    @FXML
    private void barcoVer(ActionEvent event) {
         if (vbox.getChildren().size() > 1) {  // if size == 1 there is only an hbox with 2 buttons, at position 0 
            vbox.getChildren().remove(0);

        }
        vbox.getChildren().add(0, vistaBarco);
    }

    @FXML
    private void gpsVer(ActionEvent event) {
        if (vbox.getChildren().size() > 1) {  // if size == 1 there is only an hbox with 2 buttons, at position 0 
            vbox.getChildren().remove(0);

        }
        vbox.getChildren().add(0, vistaGPS);
    }

    @FXML
    private void vientoVer(ActionEvent event) {
        if (vbox.getChildren().size() > 1) {  // if size == 1 there is only an hbox with 2 buttons, at position 0 
            vbox.getChildren().remove(0);

        }
        vbox.getChildren().add(0, vistaViento);
    }

    @FXML
    private void switchMode(ActionEvent event) {
        if(dia) {
                vbox.getStylesheets().clear();
                vbox.getStylesheets().add("resources/EstiloNoche.css");
                modoImage.setImage(new Image("resources/sunicon.png"));
                boatImage.setImage(new Image("resources/boaticonb.png"));
                gpsImage.setImage(new Image("resources/gpsiconb.png"));
                atmImage.setImage(new Image("resources/weathericonb.png"));
                ficheroImagen.setImage(new Image("resources/subirficherob.png"));
                prtadaImage.setImage(new Image("resources/portadanoche.png"));
                
                dia = false;
            }
            else {
                vbox.getStylesheets().clear();
                vbox.getStylesheets().add("resources/EstiloDia.css");
                modoImage.setImage(new Image("resources/moonicon.png"));
                boatImage.setImage(new Image("resources/boaticon.png"));
                gpsImage.setImage(new Image("resources/gpsicon.png"));
                atmImage.setImage(new Image("resources/weathericon.png"));
                ficheroImagen.setImage(new Image("resources/subirfichero.png"));
                prtadaImage.setImage(new Image("resources/portadadia.png"));
                dia = true;
            }
        }
    
    private String extension(String p){
        String extension = "";

        int i = p.lastIndexOf('.');
        if (i > 0) {
        extension = p.substring(i+1);
        }
       return extension;
    }
    
}