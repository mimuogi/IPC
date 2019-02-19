/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestoracademia;

import accesoaBD.AccesoaBD;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import static java.time.LocalDate.now;
import static java.time.LocalDate.parse;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modelo.Alumno;
import modelo.Curso;
import modelo.Dias;
import static modelo.Dias.Domingo;
import static modelo.Dias.Jueves;
import static modelo.Dias.Lunes;
import static modelo.Dias.Martes;
import static modelo.Dias.Miercoles;
import static modelo.Dias.Sabado;
import modelo.LocalTimeAdapter;
import modelo.Matricula;

/**
 * FXML Controller class
 *
 * @author Alejandro
 */
public class NuevoController implements Initializable {

    @FXML
    private TextField nombre;
    @FXML
    private TextField dni;
    @FXML
    private TextField direccion;
    @FXML
    private TextField nombreCurso;
    @FXML
    private TextField docente;
    @FXML
    private TextField plazas;
    @FXML
    private TextField fechaInicio;
    @FXML
    private TextField fechaFin;
    @FXML
    private TextField hora;
    @FXML
    private TextField aula;
    @FXML
    private Button cancelar;
    @FXML
    private Button cancelarC;
    @FXML
    private ImageView foto;
    @FXML
    private TextField edad;
    @FXML
    private Button crearA;
    @FXML
    private Button crearC;
    
    private AccesoaBD baseDatos = new AccesoaBD(); 
    private ArrayList<Alumno> listaAlumnos = (ArrayList<Alumno>) baseDatos.getAlumnos();
    private ArrayList<Curso> listaCursos = (ArrayList<Curso>) baseDatos.getCursos();
    private ArrayList<Matricula> listaMatriculas = (ArrayList<Matricula>) baseDatos.getMatriculas();
    @FXML
    private RadioButton lunes;
    @FXML
    private RadioButton martes;
    @FXML
    private RadioButton miercoles;
    @FXML
    private RadioButton jueves;
    @FXML
    private RadioButton viernes;
    @FXML
    private RadioButton sabado;
    @FXML
    private RadioButton domingo;
    @FXML
    private Button imagenUpload;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //LIMITADOR DE NOMBRE
        nombre.textProperty().addListener((observable, oldText, newText) ->  
        {
            String newInput = (String) newText;
            char lastChar = newInput.charAt(newInput.length() - 1);
            if(!Character.isLetter(lastChar))
            {
                nombre.setText(oldText);
            }
        });
       
        //LIMITADOR DE DNI
        dni.textProperty().addListener((observable, oldText, newText) ->  
        {
            String newInput = (String) newText;
            char lastChar = newInput.charAt(newInput.length() - 1);
            if(!Character.isLetterOrDigit(lastChar))
            {
                dni.setText(oldText);
            }
        });
       
        //LIMITADOR DE EDAD
        edad.textProperty().addListener((observable, oldText, newText) ->  
        {
            String newInput = (String) newText;
            char lastChar = newInput.charAt(newInput.length() - 1);
            if(!Character.isDigit(lastChar))
            {
                edad.setText(oldText);
            }
            if(newText.length() > 3 ) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Casi crack");
                alert.setHeaderText(newText + " años ¿Enserio? ( ͡ಠ ʖ̯ ͡ಠ) ");
                Optional<ButtonType> result = alert.showAndWait();
                edad.setText(oldText);
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
    private void crearAClick(ActionEvent event) {
        if( !nombre.getText().equals("") && !dni.getText().equals("") && !direccion.getText().equals("")
              && !edad.getText().equals("") ) {
            if(baseDatos.getAlumnoByDNI(dni.getText()) == null){
                LocalDate fechaAlta;
                fechaAlta = now();
                Image fotoA = foto.getImage();
                Alumno alumno = new Alumno(dni.getText(), nombre.getText(), parseInt(edad.getText()), direccion.getText(), fechaAlta, fotoA);
                listaAlumnos.add(alumno);
                baseDatos.salvar();
                Stage stage = (Stage) crearA.getScene().getWindow();
                stage.close();
            }
            else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText("Alumno existente");
            alert.setContentText("Este alumno ya está contemplado en la academia");
            alert.showAndWait();
            }
        }   
            else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Ficha de alumno incompleta");
            alert.setContentText("Todos los campos han de ser rellenados obligatoriamente");
            alert.showAndWait();}
    }

    @FXML
    private void crearCClick(ActionEvent event){
        if( !docente.getText().equals("") && !plazas.getText().equals("") && !fechaInicio.getText().equals("")
              && !fechaFin.getText().equals("") && !hora.getText().equals("") && !aula.getText().equals("")) {
            if(baseDatos.getCursoByNombre(nombreCurso.getText()) == null){
                LocalDate fechaIni = null;
                 LocalDate fechaFi = null;
                try{
                    fechaIni = parse(fechaInicio.getText());
                fechaFi = parse(fechaFin.getText());
                }
                catch(DateTimeException e){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Fecha incorrecta");
                alert.setContentText("Las fechas introducidas no son validas");
                alert.showAndWait();
                return;
                }
                LocalTime horaC = null;
                try {
                    LocalTimeAdapter horaCA = new LocalTimeAdapter();
                    horaC = horaCA.unmarshal(hora.getText());
                } catch (Exception ex) {
                    Logger.getLogger(NuevoController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                ArrayList<Dias> diasImparte = new ArrayList<Dias>();
                if(lunes.isArmed()) diasImparte.add(Lunes);
                if(martes.isArmed()) diasImparte.add(Martes);
                if(miercoles.isArmed()) diasImparte.add(Miercoles);
                if(jueves.isArmed()) diasImparte.add(Jueves);
                if(sabado.isArmed()) diasImparte.add(Sabado);
                if(domingo.isArmed()) diasImparte.add(Domingo);
                Curso c = new Curso(nombreCurso.getText(), docente.getText(), parseInt(plazas.getText()), fechaIni, fechaFi, horaC, diasImparte ,aula.getText());
                listaCursos.add(c);
                baseDatos.salvar();
                Stage stage = (Stage) crearA.getScene().getWindow();
                stage.close();
            } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText("Curso existente");
            alert.setContentText("Este alumno ya está contemplado en la academia");
            alert.showAndWait();
            return;
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Ficha de curso incompleta");
            alert.setContentText("Todos los campos han de ser rellenados obligatoriamente");
            alert.showAndWait();
            return;
            }
        }
    

    @FXML
    private void uploadClick(ActionEvent event) {
         String dnitext;
         Image fotoAl;
        if(dni.getText()!= null){
           dnitext = dni.getText();
           try{fotoAl = new Image("/resources/" + dnitext +".png");
           foto.setImage(fotoAl);}
           catch(NullPointerException | IllegalArgumentException e){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Imposible encontrar imagen");
            alert.setContentText("El archivo ha de reconocerse mediante el DNI del alumno y estar contenido en la carpeta 'resources'");
            alert.showAndWait();}
        }
    }

}
