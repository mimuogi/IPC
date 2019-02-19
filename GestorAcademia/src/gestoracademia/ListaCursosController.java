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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Optional;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import modelo.Curso;
import modelo.Matricula;

/**
 * FXML Controller class
 *
 * @author Alejandro
 */
public class ListaCursosController implements Initializable {

    @FXML
    private Button cancelar;
    @FXML
    private Button eliminar;
    @FXML
    private TableView<Curso> table;
    @FXML
    private TableColumn<Curso, String> columnaCursos;
   
    private ObservableList<Curso> cursosListaObservable = null;
    
     private ObservableList<Matricula> matriculadosListaObservable = null;
    
    private AccesoaBD baseDatos = new AccesoaBD();
    
    private ArrayList<Curso> listaCursos =(ArrayList<Curso>)baseDatos.getCursos();
    @FXML
    private Button ver;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      ver.setDisable(true);
        
      cursosListaObservable = FXCollections.observableList(listaCursos);
      columnaCursos.setCellValueFactory(curso -> {    
          return new SimpleStringProperty(curso.getValue().getTitulodelcurso());
        });
        
      table.setItems(cursosListaObservable);
      
      table.focusedProperty().addListener(new ChangeListener<Boolean>() {
          @Override  
          public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                    Boolean newValue) {
                if (table.isFocused()) {
                    ver.setDisable(false);
                }
                if (cursosListaObservable.isEmpty()) {
                    ver.setDisable(true);
                    eliminar.setDisable(true);
                } else {
                ver.setDisable(false);
                eliminar.setDisable(false);}
          }}); 
    }    

    @FXML
    private void cancelarClick(ActionEvent event) throws IOException {
        Stage stage;
        stage = (Stage) cancelar.getScene().getWindow();
        stage.close();

    }

    @FXML
    private void eliminarClick(ActionEvent event) {
        Curso c = table.getSelectionModel().getSelectedItem();
        if (baseDatos.getMatriculasDeCurso(c).isEmpty()) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Aviso de confirmación");
            alert.setHeaderText("Eliminar Curso");
            alert.setContentText("¿Seguro que desea eliminar este curso?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                cursosListaObservable.remove(c);
                listaCursos.remove(c);
                baseDatos.salvar();
            } else {}    
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No es posible eliminar este curso");
            alert.setContentText("Existen " + baseDatos.getMatriculasDeCurso(c).size() + " alumnos matriculados");
            alert.showAndWait();
        }
    }

    @FXML
    private void verClick(ActionEvent event) throws IOException {
        Curso c = table.getSelectionModel().getSelectedItem();
        String docente = c.getProfesorAsignado();
        String plazas = "" + c.getNumeroMaximodeAlumnos();
        LocalDate fechaInicio = c.getFechainicio();
        LocalDate fechaFin = c.getFechafin();
        LocalTime hora = c.getHora();
        String aula = c.getAula();
        String nombre = c.getTitulodelcurso();
        Stage listaScene = (Stage) ver.getScene().getWindow();
        FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/vistas/VistaCurso.fxml"));
        Parent root = miLoader.load();
        
        ((VistaCursoController)miLoader.getController()).inicio(nombre,docente, plazas,
                fechaInicio, fechaFin, hora, aula);
      
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle(nombre);
        stage.getIcons().add(new Image("/resources/academialogo.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        listaScene.close();
    
    }

    void reload() {
        ver.setDisable(true);
        
      cursosListaObservable = FXCollections.observableList(listaCursos);
      columnaCursos.setCellValueFactory(curso -> {    
          return new SimpleStringProperty(curso.getValue().getTitulodelcurso());
        });
        
      table.setItems(cursosListaObservable);
      
      table.focusedProperty().addListener(new ChangeListener<Boolean>() {
          @Override  
          public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                    Boolean newValue) {
                if (table.isFocused()) {
                    ver.setDisable(false);
                }}}); 
    }
    
}
