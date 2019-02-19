/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestoracademia;

import accesoaBD.AccesoaBD;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
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
public class VistaCursoController implements Initializable {

    @FXML
    private Text docente;
    @FXML
    private Text plazas;
    @FXML
    private Text fechaInicio;
    @FXML
    private Text fechaFin;
    @FXML
    private Text hora;
    @FXML
    private Text aula;
    @FXML
    private Button eliminar;
    @FXML
    private Button desmatricular;
    @FXML
    private Button matricular;
    @FXML
    private Button cancelar;
    private ListView<Alumno> listaAlumnos;
   
    private ObservableList<Alumno> alumnosListaObservable = null;
    
    private AccesoaBD baseDatos = new AccesoaBD(); 
    
    private ArrayList<Alumno> listaAlumnosMat = (ArrayList<Alumno>) baseDatos.getAlumnos();
    
    private ArrayList<Curso> listaCursos = (ArrayList<Curso>) baseDatos.getCursos();
    
    private ArrayList<Matricula> listaMatriculas = (ArrayList<Matricula>) baseDatos.getMatriculas();
    
    private ArrayList<Matricula> listaMatriculasDCurso;
    
    private Curso esteCurso;
    
    ArrayList<Alumno> listaAl;
   
    @FXML
    private Text nombreCurso;
    @FXML
    private TableView<Alumno> table;
    @FXML
    private TableColumn<Alumno, String> columnaAlumnos;
    @FXML
    private Text matriculados;

    public VistaCursoController() {
     
 
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    eliminar.setDisable(true);
    desmatricular.setDisable(true);
    
    table.focusedProperty().addListener(new ChangeListener<Boolean>() {
          @Override  
          public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                    Boolean newValue) {
                if (table.isFocused()) {
                    desmatricular.setDisable(false);
                }}}); 
    
        matriculados.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if(matriculados.getText().equals("0")) {eliminar.setDisable(false);}
            if(matriculados.getText().equals(plazas.getText())) {matricular.setDisable(true);}
            else {matricular.setDisable(false);}
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
    private void eliminarClick(ActionEvent event) throws IOException {
        Curso c = baseDatos.getCursoByNombre(nombreCurso.getText());
        if (baseDatos.getMatriculasDeCurso(c).isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Aviso de confirmación");
            alert.setHeaderText("Eliminar Curso");
            alert.setContentText("¿Seguro que desea eliminar este curso?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                listaCursos.remove(c);
                baseDatos.salvar();
                Stage cursoStage = (Stage) cancelar.getScene().getWindow();
                FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/vistas/ListaCursos.fxml"));
                Parent root = miLoader.load();
                Scene scene = new Scene(root);
                ((ListaCursosController) miLoader.getController()).reload();
                
                Stage stage = new Stage();
                stage.setResizable(false);
                stage.setScene(scene);
                stage.setTitle("Cursos");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
                cursoStage.close();
            } else {}    
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No es posible eliminar este curso");
            alert.setContentText("Existen " + baseDatos.getMatriculasDeCurso(c).size() + " alumnos matriculados");
            alert.showAndWait();
        }
    }

    @FXML
    private void desmatriculaClick(ActionEvent event) {
        Alumno al = table.getSelectionModel().getSelectedItem();
        int nmatriculados = parseInt(matriculados.getText());
        for (Matricula m: listaMatriculasDCurso){
           if(m.getAlumno().equals(al)){ 
               listaMatriculas.remove(m);
               break;}
        }
        alumnosListaObservable.remove(al);
        nmatriculados--;
        matriculados.setText("" + nmatriculados);
        baseDatos.salvar();
    }

    @FXML
    private void matricularClick(ActionEvent event) throws IOException {
        String c = nombreCurso.getText();
        Stage cursoScene = (Stage) matricular.getScene().getWindow();
        FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/vistas/Matricular.fxml"));
        Parent root = miLoader.load();
        ((MatricularController) miLoader.getController()).autofill(c);
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
        cursoScene.close();
        
        
    }
    
    public void inicio(String nombre, String docente, String plazas, LocalDate fechaInicio, 
        LocalDate fechaFin,LocalTime hora, String aula){
        this.nombreCurso.setText(nombre);
        esteCurso = baseDatos.getCursoByNombre(nombreCurso.getText());
        this.docente.setText(docente);
        this.plazas.setText(plazas);
        this.fechaInicio.setText("" + fechaInicio);
        this.fechaFin.setText("" + fechaFin);
        this.hora.setText("" + hora);
        this.aula.setText(aula);
        listaAl = (ArrayList<Alumno>) baseDatos.getAlumnosDeCurso(esteCurso);
       if(listaAl == null) this.matriculados.setText("0");
       else {
        this.matriculados.setText("" + listaAl.size());
        
        alumnosListaObservable = FXCollections.observableList(listaAl);
        
        columnaAlumnos.setCellValueFactory(alumno -> {    
        return new SimpleStringProperty(alumno.getValue().getNombre());});
        table.setItems(alumnosListaObservable);
       }
       
      listaMatriculasDCurso = (ArrayList<Matricula>) baseDatos.getMatriculasDeCurso(esteCurso);
        
    }

}
