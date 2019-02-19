/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestoracademia;

import accesoaBD.AccesoaBD;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Alumno;

/**
 * FXML Controller class
 *
 * @author Alejandro
 */
public class VistaAlumnoController implements Initializable {

    @FXML
    private Button cambiarImagen;
    @FXML
    private Text nombre;
    @FXML
    private Text dni;
    @FXML
    private Text direccion;
    @FXML
    private Text fechaAlta;
    @FXML
    private Button cancelar;
    
    private Alumno esteAlumno;
    
    private AccesoaBD baseDatos = new AccesoaBD();
    @FXML
    private ImageView imagen;
    @FXML
    private Text edad;
    @FXML
    private Button matricular;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void imagenClick(ActionEvent event) {
         String dnitext;
         Image fotoAl;
        if(dni.getText()!= null){
           dnitext = dni.getText();
           try{
               fotoAl = new Image("/resources/" + dnitext +".png");
               imagen.setImage(fotoAl);}
           catch(NullPointerException | IllegalArgumentException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Imposible encontrar imagen");
            alert.setContentText("El archivo ha de reconocerse mediante el DNI del alumno y estar contenido en la carpeta 'resources'");
            alert.showAndWait();}
        }
    }

    @FXML
    private void cancelarClick(ActionEvent event) {
    Stage stage;
    stage = (Stage) cancelar.getScene().getWindow();
    stage.close();
    }

    public void inicio(String nombre, String dni, Integer edad, String direccion, LocalDate fechaAlta) {
       this.nombre.setText(nombre);
       this.dni.setText(dni);
       this.edad.setText("" + edad);
       this.nombre.setText(nombre);
       this.direccion.setText(direccion);
       this.fechaAlta.setText("" + fechaAlta);
       
       esteAlumno = baseDatos.getAlumnoByDNI(dni);
       imagen.setImage(esteAlumno.getFoto());
       
    }

    @FXML
    private void matricularClick(ActionEvent event) throws IOException {
        String dniAl = dni.getText();
       
        FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/vistas/Matricular.fxml"));
        Parent root = miLoader.load();
        ((MatricularController) miLoader.getController()).autofillA(dniAl);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setResizable(true);
        stage.setMinHeight(400);
        stage.setMinWidth(600);
        stage.setScene(scene);
        stage.getIcons().add(new Image("/resources/academialogo.png"));
        stage.setTitle("Matricular");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
    
   
        
}
