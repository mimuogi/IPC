/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestoracademia;
 
import accesoaBD.AccesoaBD;
import java.net.URL;
import java.time.LocalDate;
import static java.time.LocalDate.now;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.Alumno;
import modelo.Curso;
import modelo.Dias;
import modelo.Matricula;
 
/**
 * FXML Controller class
 *
 * @author Alejandro
 */
public class MatricularController implements Initializable {
 
    @FXML
    private Button cancelar;
    @FXML
    private Button matricular;
    @FXML
    private TextField curso;
   
    private AccesoaBD baseDatos = new AccesoaBD();
    private ArrayList<Alumno> listaAlumnos = (ArrayList<Alumno>) baseDatos.getAlumnos();
    private ArrayList<Curso> listaCursos = (ArrayList<Curso>) baseDatos.getCursos();
    private ArrayList<Matricula> listaMatriculas = (ArrayList<Matricula>) baseDatos.getMatriculas();
    @FXML
    private TextField dni;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    
 
    @FXML
    private void cancelarClick(ActionEvent event) {
        Stage stage;
        stage = (Stage) cancelar.getScene().getWindow();
        stage.close();
    }
 
    @FXML
    private void matricularClick(ActionEvent event) {
            Alumno al;
            Curso c;
            try{
                al = baseDatos.getAlumnoByDNI(dni.getText());
                c = baseDatos.getCursoByNombre(curso.getText()); 
                    
                //Comprueba si el alumno ya esta matriculado en el curso
                List<Alumno> alumnosCurso = baseDatos.getAlumnosDeCurso(c);
                for(Alumno alum : alumnosCurso) {
                    if(alum.getDni().equals(al.getDni())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Alumno ya matriculado");
                        alert.showAndWait();
                        return;
                    }
                }
                //Si el curso esta lleno
                if(c.getNumeroMaximodeAlumnos() <= baseDatos.getMatriculasDeCurso(c).size()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Curso completo");
                    alert.showAndWait();
                    return;
                }
                
                //Obtienes dias y hora del curso
                ArrayList<Dias> diasEsteCurso = c.getDiasimparte();
                LocalTime horaEsteCurso = c.getHora();

                //Recorres todas las matriculas de la escuela
                for(Matricula m : listaMatriculas) {
                    //Si el alumno da el curso
                    //en un horario solapado, no dejas
                    if(al.getDni().equals(m.getAlumno().getDni()) 
                        && horaEsteCurso.equals(m.getCurso().getHora())
                        && diasEsteCurso.equals(m.getCurso().getDiasimparte())){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Horarios solapados");
                            alert.showAndWait();
                            return;
                    } 
                }
                //Si no has encontrado problemas en todas
                //las matriculas, matriculas al alumno
                try{
                    LocalDate fechaM;
                    fechaM = now();
                    Matricula toDo = new Matricula(fechaM, c, al);
                    listaMatriculas.add(toDo);
                    baseDatos.salvar();
                    Stage stage = (Stage) cancelar.getScene().getWindow();
                    stage.close();}
                    catch(ConcurrentModificationException e) {}
            } 
            catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Información errónea");
                alert.setContentText("El alumno o el curso no existen");
                alert.showAndWait();
                return;
            }
    }
 
    public void autofill(String c) {
        curso.setText(c);
        curso.editableProperty().setValue(Boolean.FALSE);
    }

    void autofillA(String dniAl) {
        dni.setText(dniAl);
        dni.editableProperty().setValue(Boolean.FALSE);
    }
   
}