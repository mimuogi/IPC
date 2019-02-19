/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demonmea;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import net.sf.marineapi.nmea.event.AbstractSentenceListener;
import net.sf.marineapi.nmea.io.ExceptionListener;
import net.sf.marineapi.nmea.io.SentenceReader;
import net.sf.marineapi.nmea.sentence.HDGSentence;
import net.sf.marineapi.nmea.sentence.MDASentence;
import net.sf.marineapi.nmea.sentence.MWVSentence;
import net.sf.marineapi.nmea.sentence.RMCSentence;
import net.sf.marineapi.nmea.sentence.XDRSentence;
import net.sf.marineapi.nmea.util.Date;
import net.sf.marineapi.nmea.util.Measurement;
import net.sf.marineapi.nmea.util.Position;

/**
 *
 * @author jsoler
 */
public class Model {
    //<editor-fold defaultstate="collapsed" desc="Singleton Model">
    //implementa el patron singleton
    // esto asegura que solamente se va a crear una instancia de la clase model
    // y se podra acceder a ella desde cualquier clase del proyecto
    private static Model model;

    
    private Model() {
    }
    
    public static Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }
//</editor-fold>
    int parametroLista = 0;
    int precisionListas = 120;
    
    List<DatoViento> listaViento = new LinkedList<DatoViento>();
    
    public XYChart.Series listaDireccionViento = new XYChart.Series();
    public XYChart.Series listaFuerzaViento = new XYChart.Series();
    
    private void actualizarListas(double direccionViento, double fuerzaViento) {
        
        parametroLista++;
        
        listaDireccionViento.getData().add(new XYChart.Data("" +  parametroLista, direccionViento));
        //Si has llegado al limite borras el ultimo
        if(listaDireccionViento.getData().size() > precisionListas) listaDireccionViento.getData().remove(0);
        
        listaFuerzaViento.getData().add(new XYChart.Data("" + parametroLista, fuerzaViento));
        //Si has llegado al limite borras el ultimo
        if(listaFuerzaViento.getData().size() > precisionListas) listaFuerzaViento.getData().remove(0);
    }
    
    public void cambiarPrecisionListas(int nuevaPrecision) {
        
        if(nuevaPrecision >= 2 && nuevaPrecision <= 10) {
            
            listaDireccionViento.getData().clear();
            listaFuerzaViento.getData().clear();
            int iteraciones = Math.min(nuevaPrecision * 60 , listaViento.size());
            int maxElementos = listaViento.size();
            //El elemento mas nuevo es el de la ultima posicion
            for (int i = (maxElementos - iteraciones); i < maxElementos; i++) {
                parametroLista++;
                listaDireccionViento.getData().add(new XYChart.Data("" + parametroLista, listaViento.get(i).direccionViento));
                listaFuerzaViento.getData().add(new XYChart.Data("" + parametroLista, listaViento.get(i).fuerzaViento));
            }
            
            precisionListas = nuevaPrecision*60;
        }
    }
   
    
    //===================================================================
    // CUIDADO, el objeto de la clase SentenceReader se ejecuta en un hilo
    // no se pueden modificar las propiedades de los objetos graficos desde
    // un metodo ejecutado en este hilo
    private SentenceReader reader;

    //<editor-fold defaultstate="collapsed" desc="DoubleProperty's">
    //True Wind Dir -- direccion del viento respecto al norte
    private final DoubleProperty TWD = new SimpleDoubleProperty();
    public DoubleProperty TWDProperty() {
        return TWD;
    }
    
    // True Wind Speed -- intensidad de viento
    private final DoubleProperty TWS = new SimpleDoubleProperty();
    public DoubleProperty TWSProperty() {
        return TWS;
    }
    
    //Heading - compas magnetic
    private final DoubleProperty HDG = new SimpleDoubleProperty();
    public DoubleProperty HDGProperty() {
        return HDG;
    }
    
    // Position -- posicion del GPS
    private final ObjectProperty<Position> GPS = new SimpleObjectProperty();
    public ObjectProperty<Position> GPSroperty() {
        return GPS;
    }
    
    // TEMP -- temperatura
    private final DoubleProperty TEMP = new SimpleDoubleProperty();
    public DoubleProperty TEMProperty() {
        return TEMP;
    }
    
    // AWA
    private final DoubleProperty AWA = new SimpleDoubleProperty();
    public DoubleProperty AWAProperty() {
        return AWA;
    }
    
    // AWS
    private final DoubleProperty AWS = new SimpleDoubleProperty();
    public DoubleProperty AWSProperty() {
        return AWS;
    }
    
    
    // COG -- rumbo del GPS
    private final DoubleProperty COG = new SimpleDoubleProperty();
    public DoubleProperty COGProperty() {
        return COG;
    }
    
    // SOG -- velocidad del GPS
    private final DoubleProperty SOG = new SimpleDoubleProperty();
    public DoubleProperty SOGProperty() {
        return SOG;
    }
    
    // PITCH
    private final DoubleProperty PITCH = new SimpleDoubleProperty();
    public DoubleProperty PITCHProperty() {
        return PITCH;
    }
    
    // ROLL
    private final DoubleProperty ROLL = new SimpleDoubleProperty();
    public DoubleProperty ROLLProperty() {
        return ROLL;
    }
    
    
    //==================================================================
    // anade todas las propiedades que necesites, en el hilo principal
    // podras anadir listeners sobre estas propiedades que modifquen la interfaz
    
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Abstract Sentence Listener">
    //====================================================================
    //anadir tantos sentenceListener como tipos de sentence queremos tratar
    class HDGSentenceListener
            extends AbstractSentenceListener<HDGSentence> {
        
        @Override
        public void sentenceRead(HDGSentence sentence) {
            // anadimos el codigo necesario para guardar la información de la sentence
            HDG.set(sentence.getHeading());
        }
    };
    
    class XDRSentenceListener
            extends AbstractSentenceListener<XDRSentence> {
    
        @Override
        public void sentenceRead(XDRSentence sentence) {
            for(Measurement me : sentence.getMeasurements()) {
                if(me.getName().equals("PTCH")) {
                    PITCH.set(me.getValue());
                } 
                
                else if(me.getName().equals("ROLL")) { 
                    ROLL.set(me.getValue());
                }
            }
        }
    }
    
    
    class MDASentenceListener
            extends AbstractSentenceListener<MDASentence> {
        
        @Override
        public void sentenceRead(MDASentence sentence) {
            // anadimos el codigo necesario para guardar la información de la sentence
            
            double direccionViento = sentence.getTrueWindDirection();
            double fuerzaViento = sentence.getWindSpeedKnots();
            double temperatura = sentence.getAirTemperature();
            
            TWD.set(direccionViento);
            TWS.set(fuerzaViento);
            TEMP.set(temperatura);
                    
            listaViento.add(new DatoViento(direccionViento, fuerzaViento));
            
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    actualizarListas(direccionViento, fuerzaViento);
                }
            });
            
        }
    }
    
    class MWVSentenceListener
            extends AbstractSentenceListener<MWVSentence> {
        
        @Override
        public void sentenceRead(MWVSentence sentence) {
            // anadimos el codigo necesario para guardar la información de la sentence
            AWA.set(sentence.getAngle());
            AWS.set(sentence.getSpeed());
        }
    }
    
    class RMCSentenceListener
            extends AbstractSentenceListener<RMCSentence> {
        
        @Override
        public void sentenceRead(RMCSentence sentence) {
            GPS.set(sentence.getPosition());
            COG.set(sentence.getCourse());
            SOG.set(sentence.getSpeed());
        }
    }
    //========================================================================================
    // anade todas las clases de que extiendan AbstractSentenceListener que necesites
//</editor-fold>
    
    
    
    //Vinculamos todos los sentence listeners que hemos creado al hilo
    //que leera los datos para ir actualizando valores
    public void addSentenceReader(File file) throws FileNotFoundException {
        InputStream stream = new FileInputStream(file);
        if (reader != null) {  // esto ocurre si ya estamos leyendo un fichero
            reader.stop();
        }
        reader = new SentenceReader(stream);
        
        
 
        //==================================================================
        //============= Registra todos los sentenceListener que necesites
        HDGSentenceListener hdg = new HDGSentenceListener();
        reader.addSentenceListener(hdg);

        MDASentenceListener mda = new MDASentenceListener();
        reader.addSentenceListener(mda);

        RMCSentenceListener rmd = new RMCSentenceListener();
        reader.addSentenceListener(rmd);
        
        XDRSentenceListener xdr = new XDRSentenceListener();
        reader.addSentenceListener(xdr);
        
        MWVSentenceListener mwv = new MWVSentenceListener();
        reader.addSentenceListener(mwv);
        
         //===============================================================

         //===============================================================
         //== Anadimos un exceptionListener para que capture las tramas que 
         // == no tienen parser, ya que no las usamos
         reader.setExceptionListener(e->{System.out.println(e.getMessage());});
         
         //================================================================
         //======== arrancamos el SentenceReader para que empieze a escucha           
                 
        reader.start();
    }
    
    class DatoViento {
        double direccionViento;
        double fuerzaViento;
        
        DatoViento(double direccionV, double fuerzaV) 
        {
            direccionViento = direccionV; 
            fuerzaViento = fuerzaV;
        }
    }
}