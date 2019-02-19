/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestoracademia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Alejandro
 */
public class GestorAcademia extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/vistas/FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        stage.setTitle("Gestor de academia");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.getIcons().add(new Image("/resources/academialogo.png"));
        stage.setMinHeight(400);
        stage.setMinWidth(600);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
