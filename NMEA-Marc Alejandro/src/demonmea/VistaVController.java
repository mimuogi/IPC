/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demonmea;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

/**
 * FXML Controller class
 *
 * @author Alejandro
 */
public class VistaVController implements Initializable {

    private Model model;
    @FXML
    private Label tempLabel;
    @FXML
    private Label awaLabel;
    @FXML
    private Label awsLabel;
    @FXML
    private Label textoSlider;
    @FXML
    private Slider sliderViento;
    @FXML
    private LineChart<?, ?> direccionViento;
    @FXML
    private LineChart<?, ?> velocidadViento;
    @FXML
    private Label twdLabel;
    @FXML
    private Label twsLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        model = Model.getInstance();
        
        sliderViento.setMin(2);
        sliderViento.setMax(10);
        
        model.TEMProperty().addListener((observable, oldValue, newValue) -> {
        Platform.runLater(() -> {
        tempLabel.setText(String.format("%.1f", newValue) + "ºC");
        });
        });
        
        model.AWAProperty().addListener((observable, oldValue, newValue) -> {
        Platform.runLater(() -> {
        awaLabel.setText(String.format("%.3f", newValue) + "º");
        });
        });
        
        model.AWSProperty().addListener((observable, oldValue, newValue) -> {
        Platform.runLater(() -> {
        awsLabel.setText(String.format("%.3f", newValue) + "º");
        });
        });
        
        model.TWSProperty().addListener((observable, oldValue, newValue)-> {
        Platform.runLater(() -> {
        twsLabel.setText("TWS\n" + String.format("%.3f", newValue) + "Kn");
        });
        });
        
        model.TWDProperty().addListener((observable, oldValue, newValue)-> {
        Platform.runLater(() -> {
        twdLabel.setText("TWD\n" + String.format("%.3f", newValue) + "º");
        });
        });
        
        sliderViento.setOnMouseReleased((event) ->
        {
        int valor = (int) Math.floor(sliderViento.getValue());
        if(valor != model.precisionListas) {
        model.cambiarPrecisionListas(valor);
        }
        
        });
        
        direccionViento.setAnimated(false);
        velocidadViento.setAnimated(false);
        direccionViento.setData(FXCollections.observableArrayList(model.listaDireccionViento));
        direccionViento.getXAxis().setTickLabelsVisible(false);
        velocidadViento.setData(FXCollections.observableArrayList(model.listaFuerzaViento));
        velocidadViento.getXAxis().setTickLabelsVisible(false);
    }    
    
}
