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
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Alumno;
import modelo.Curso;
import modelo.Matricula;

/**
 * FXML Controller class
 *
 * @author Alejandro
 */
public class ListaAlumnosController implements Initializable {

    @FXML
    private Button cancelar;
    @FXML
    private Button eliminar;
    @FXML
    private TableView<Alumno> table;
    @FXML
    private TableColumn<Alumno, String> columnaAlumnos;

    private ObservableList<Alumno> alumnosListaObservable = null;
    
    private AccesoaBD baseDatos = new AccesoaBD();
    
    private ArrayList<Matricula> listaMatriculas =(ArrayList<Matricula>)baseDatos.getMatriculas();
    
    private ArrayList<Alumno> listaAlumnos =(ArrayList<Alumno>)baseDatos.getAlumnos();
    @FXML
    private Button ver;
    @FXML
    private TableColumn<Alumno, String> columnaDNI;
    
    private ObservableList<Alumno> dniListaObservable = null;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        alumnosListaObservable = FXCollections.observableList(listaAlumnos);
        columnaAlumnos.setCellValueFactory(alumno -> {    
          return new SimpleStringProperty(alumno.getValue().getNombre());
        });
        
        dniListaObservable = FXCollections.observableList(listaAlumnos);
        columnaDNI.setCellValueFactory(alumno -> {    
          return new SimpleStringProperty(alumno.getValue().getDni());
        });
        
        table.setItems(alumnosListaObservable);
        table.setItems(dniListaObservable);
      
          table.focusedProperty().addListener(new ChangeListener<Boolean>() {
          @Override  
          public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                    Boolean newValue) {
                if (table.isFocused()) {
                    ver.setDisable(false);
                }}}); 
          
          table.focusedProperty().addListener(new ChangeListener<Boolean>() {
          @Override  
          public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                    Boolean newValue) {
                if (table.isFocused()) {
                    ver.setDisable(false);
                }
                if(alumnosListaObservable.isEmpty()){
                    ver.setDisable(true);
                    eliminar.setDisable(true);
                } else {
                ver.setDisable(false);
                eliminar.setDisable(false);}
                }
          }); 
        
    }    

    @FXML
    private void cancelarClick(ActionEvent event) {
        Stage stage;
        stage = (Stage) cancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void eliminarClick(ActionEvent event) {
        Alumno al = (Alumno) table.getSelectionModel().getSelectedItem();
        int nMat = 0;
        for (Matricula m : listaMatriculas){
            if((m.getAlumno().getDni()).equals(al.getDni())) {nMat = nMat+1;}
            System.out.println(nMat);
        }
        if (nMat == 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Aviso de confirmación");
            alert.setHeaderText("Eliminar Alumno");
            alert.setContentText("¿Seguro que desea eliminar este alumno?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                alumnosListaObservable.remove(al);
                dniListaObservable.remove(al);
                listaAlumnos.remove(al);
                baseDatos.salvar();
                reset();
            } else {}    
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No es posible eliminar este alumno");
            alert.setContentText("Existen " + nMat + " matriculas");
            alert.showAndWait();
        }
    }

    @FXML
    private void verClick(ActionEvent event) throws IOException {
        Alumno al = table.getSelectionModel().getSelectedItem();
        
        String nombre = al.getNombre();
        String dni = al.getDni();
        Integer edad = al.getEdad();
        String direccion = al.getDireccion();
        LocalDate fechaAlta = al.getFechadealta();
        
        FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/vistas/VistaAlumno.fxml"));
        Parent root = miLoader.load();
        
        ((VistaAlumnoController)miLoader.getController()).inicio(nombre,dni, edad,
                direccion, fechaAlta);
      
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle(nombre);
        stage.getIcons().add(new Image("/resources/academialogo.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
    
    public void reset(){
        
        alumnosListaObservable = FXCollections.observableList(listaAlumnos);
        columnaAlumnos.setCellValueFactory(alumno -> {    
          return new SimpleStringProperty(alumno.getValue().getNombre());
        });
            dniListaObservable = FXCollections.observableList(listaAlumnos);
            columnaDNI.setCellValueFactory(alumno -> {    
            return new SimpleStringProperty(alumno.getValue().getDni());
            });
        
            table.setItems(alumnosListaObservable);
            table.setItems(dniListaObservable);
      
          table.focusedProperty().addListener(new ChangeListener<Boolean>() {
          @Override  
          public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                    Boolean newValue) {
                if (table.isFocused()) {
                    ver.setDisable(false);
                }}}); 
    }
    
}
