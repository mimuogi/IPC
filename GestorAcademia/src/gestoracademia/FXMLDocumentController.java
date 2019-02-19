/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestoracademia;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Alejandro
 */
public class FXMLDocumentController implements Initializable {
   
    @FXML
    private Button alumnos;
    @FXML
    private Button cursos;
    @FXML
    private Button matricular;
    @FXML
    private Button nuevo;
    @FXML
    private ImageView portada;
    
    
    @Override
    
    public void initialize(URL url, ResourceBundle rb) {
        
        
    }    

    @FXML
    private void alumnosClick(ActionEvent event) throws IOException {
        FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/vistas/ListaAlumnos.fxml"));
        Parent root = miLoader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setResizable(true);
        stage.setMinHeight(400);
        stage.setMinWidth(600);
        stage.setScene(scene);
        stage.setTitle("Alumnos");
        stage.getIcons().add(new Image("/resources/academialogo.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    private void cursosClick(ActionEvent event) throws IOException {
        FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/vistas/ListaCursos.fxml"));
        Parent root = miLoader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setResizable(true);
        stage.setMinHeight(425);
        stage.setMinWidth(600);
        stage.setScene(scene);
        stage.setTitle("Cursos");
        stage.getIcons().add(new Image("/resources/academialogo.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    private void matricularClick(ActionEvent event) throws IOException {
        FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/vistas/Matricular.fxml"));
        Parent root = miLoader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setResizable(true);
        stage.setMinHeight(400);
        stage.setMinWidth(600);
        stage.setScene(scene);
        stage.setTitle("Matricular");
        stage.getIcons().add(new Image("/resources/academialogo.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    private void nuevoClick(ActionEvent event) throws IOException {
        FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/vistas/Nuevo.fxml"));
        Parent root = miLoader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Nuevo");
        stage.getIcons().add(new Image("/resources/academialogo.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
    
    
    
    
}
