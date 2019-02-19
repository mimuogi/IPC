/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demonmea;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Alejandro
 */
public class VistaGPSController implements Initializable {
    private Model model;
    @FXML
    private Label cogLabel;
    @FXML
    private Label sogLabel;
    @FXML
    private Label latLab;
    @FXML
    private Label longLab;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        model = Model.getInstance();
        
        model.GPSroperty().addListener((observable, oldValue, newValue)-> {
            Platform.runLater(() -> {
                longLab.setText(String.format("%.2f", newValue.getLongitude()) + "º " + newValue.getLongitudeHemisphere());
                latLab.setText(String.format("%.2f", newValue.getLatitude()) + "º " + newValue.getLatitudeHemisphere());
            });
        });
        
        model.COGProperty().addListener((observable, oldValue, newValue)-> {
            Platform.runLater(() -> {
                cogLabel.setText(String.format("%.2f", newValue) + "º");
            });
        });
        
        model.SOGProperty().addListener((observable, oldValue, newValue)-> {
            Platform.runLater(() -> {
                sogLabel.setText(String.format("%.2f", newValue) + " Kn");
            });
        });
    }    
    
}
