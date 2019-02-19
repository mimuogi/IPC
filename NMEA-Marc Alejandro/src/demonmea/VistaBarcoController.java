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
public class VistaBarcoController implements Initializable {

    private Model model;
    @FXML
    private Label hdgLabel;
    @FXML
    private Label sogLabel;
    @FXML
    private Label pitchLab;
    @FXML
    private Label rollLab;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        model = Model.getInstance();
        
        model.HDGProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                hdgLabel.setText(String.format("%.2f", newValue) + "º");
            });
        });
        
        
        model.PITCHProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                pitchLab.setText(String.format("%.2f", newValue) + "º");
            });
        });
        
        model.ROLLProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                rollLab.setText(String.format("%.2f", newValue) + "º");
            });
        });
        
    }    
    
}
