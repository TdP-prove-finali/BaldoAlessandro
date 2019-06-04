package tesi.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import tesi.model.AutoElettriche;
import tesi.model.TesiModel;

public class PannelloSceltaAutoController {
	
	private TesiModel model;
	private List<AutoElettriche> auto;
	private Stage primaryStage;
	private Stage secondaryStage;
	private Stage itself;
	private ActionEvent sceltaAuto;
	

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private ImageView btnHome;

    @FXML
    private ImageView btnForward;

    @FXML
    private CheckBox checkCompro;

    @FXML
    private CheckBox checkNoleggio;

    @FXML
    private ComboBox<String> tendinaMarca;

    @FXML
    private ComboBox<String> tendinaModello;

    @FXML
    private ComboBox<String> tendinaSegmento;

    @FXML
    private ComboBox<Integer> tendinaNumPosti;

    @FXML
    private CheckBox checkTrazione;

    @FXML
    private CheckBox checkRicarica;

    @FXML
    private Slider sliderPrezzo;

    @FXML
    private Slider sliderNoleggio;

    @FXML
    private Slider sliderAutonomia;

    @FXML
    private Slider sliderVelocita;

    @FXML
    private Slider sliderPrestazioni;

    @FXML
    private Button btnRisultatiAuto;

    @FXML
    private Button btnResetAuto;

    //@FXML
   // private TextArea txtResult;
    @FXML
    private TableView<AutoElettriche> tableResult;

    @FXML
    private TableColumn<AutoElettriche, String> columnMarca;

    @FXML
    private TableColumn<AutoElettriche, String> columnModello;

    @FXML
    private TableColumn<AutoElettriche, Integer> columnVendita;

    @FXML
    private TableColumn<AutoElettriche, Integer> columnNoleggio;

    @FXML
    private TableColumn<AutoElettriche, Integer> columnAutonomia;

    @FXML
    private TableColumn<AutoElettriche, Integer> columnEfficienza;

    @FXML
    private TableColumn<AutoElettriche, Integer> columnVelRicarica;

    @FXML
    private TableColumn<AutoElettriche, String> columnRicaricaRapida;

    @FXML
    private TableColumn<AutoElettriche, String> columnTrazione;

    @FXML
    private TableColumn<AutoElettriche, String> columnSegmento;

    @FXML
    private TableColumn<AutoElettriche, Integer> columnNumPosti;

    @FXML
    private TableColumn<AutoElettriche, Float> columnPrestazioni;

    @FXML
    private TableColumn<AutoElettriche, Integer> columnVelMax;

   

    @FXML
    void checkSelectedMarca(MouseEvent event) {
		//this.tendinaMarca.getItems().addAll(this.model.getMarche());

    	if(!this.tendinaMarca.getSelectionModel().isEmpty()) {
    		this.tendinaModello.getItems().clear();
    		this.tendinaModello.getItems().addAll(this.model.getModelloByMarca(tendinaMarca.getValue()));
    	}
    }

    @FXML
    void checkSelectedModello(MouseEvent event) {
    	if(!this.tendinaModello.getSelectionModel().isEmpty()) {
    	this.tendinaMarca.setDisable(true);
    	this.tendinaNumPosti.setDisable(true);
    	this.sliderAutonomia.setDisable(true);
    	this.sliderNoleggio.setDisable(true);
    	this.sliderPrezzo.setDisable(true);
    	this.sliderPrestazioni.setDisable(true);
    	this.sliderVelocita.setDisable(true);
    	this.checkRicarica.setDisable(true);
    	this.checkTrazione.setDisable(true);
    	this.tendinaSegmento.setDisable(true);}
	
    }

    @FXML
    void doCompro(ActionEvent event) {
    	if(this.checkCompro.isSelected()) {
    		this.checkNoleggio.setDisable(true);
    		this.sliderNoleggio.setDisable(true);
    	}
    	
    	else {
    		this.checkNoleggio.setDisable(false);
    		this.sliderNoleggio.setDisable(false);
    	}

    }
    
    @FXML
    void doNoleggio(ActionEvent event) {
    	if(this.checkNoleggio.isSelected()) {
    		this.checkCompro.setDisable(true);
    		this.sliderPrezzo.setDisable(true);
    	}
    	else {
    		this.checkCompro.setDisable(false);
    		this.sliderPrezzo.setDisable(false);
    	}

    }

    @FXML
    void doResetAuto(ActionEvent event) {
    	//txtResult.clear();
    	for ( int i = 0; i<tableResult.getItems().size(); i++) {
    	    tableResult.getItems().clear();
    	}
		this.tableResult.getItems().add(null);

    	this.checkCompro.selectedProperty().set(false);
    	this.checkCompro.setDisable(false);
    	this.checkNoleggio.selectedProperty().set(false);
    	this.checkNoleggio.setDisable(false);
    	this.tendinaMarca.setValue(null);
		this.tendinaMarca.setDisable(false);
		this.tendinaModello.getItems().clear();
		this.tendinaModello.getItems().addAll(model.getModelli());
    	this.tendinaModello.setValue(null);
    	this.tendinaModello.setDisable(false);
		this.tendinaNumPosti.setValue(null);
		this.tendinaNumPosti.setDisable(false);
		this.tendinaSegmento.setValue(null);
		this.tendinaSegmento.setDisable(false);
		this.sliderPrezzo.setValue(0);
		this.sliderPrezzo.setDisable(false);
		this.sliderAutonomia.setValue(0);
		this.sliderAutonomia.setDisable(false);
		this.sliderNoleggio.setValue(0);
		this.sliderNoleggio.setDisable(false);
		this.sliderVelocita.setValue(0);
		this.sliderVelocita.setDisable(false);
		this.sliderPrestazioni.setValue(0);
		this.sliderPrestazioni.setDisable(false);
		this.checkRicarica.selectedProperty().set(false);
		this.checkRicarica.setDisable(false);
		this.checkTrazione.selectedProperty().set(false);
		this.checkTrazione.setDisable(false);

    	

    
    }

   
    
    
    @FXML
    List<AutoElettriche> doTrovaRisultati(ActionEvent event) {
    	this.sceltaAuto=event;
    	for ( int i = 0; i<tableResult.getItems().size(); i++) {
    	    tableResult.getItems().clear();
    	}

    	
    	
    	if(this.tendinaModello.getValue()!=null) {
    		//this.txtResult.appendText("Auto scelta: "+this.model.getAutoByModello(tendinaModello.getValue()).toString()+"\n");
    		List<AutoElettriche> scelta=new ArrayList<>();

    		
    		scelta.add(this.model.getAutoByModello(tendinaModello.getValue()));
    		this.tableResult.getItems().addAll(this.model.getAutoByModello(tendinaModello.getValue()));

    		this.columnMarca.setCellValueFactory(new PropertyValueFactory<AutoElettriche, String>("marca"));
        	this.columnModello.setCellValueFactory(new PropertyValueFactory<AutoElettriche, String>("modello"));
        	this.columnVendita.setCellValueFactory(new PropertyValueFactory<AutoElettriche, Integer>("prezzoVendita"));
        	this.columnNoleggio.setCellValueFactory(new PropertyValueFactory<AutoElettriche, Integer>("prezzoNoleggio"));
        	this.columnAutonomia.setCellValueFactory(new PropertyValueFactory<AutoElettriche, Integer>("autonomia"));
        	this.columnEfficienza.setCellValueFactory(new PropertyValueFactory<AutoElettriche, Integer>("efficienza"));
        	this.columnVelRicarica.setCellValueFactory(new PropertyValueFactory<AutoElettriche, Integer>("velocitaRicarica"));
        	this.columnRicaricaRapida.setCellValueFactory(new PropertyValueFactory<AutoElettriche, String>("ricaricaRapida"));
        	this.columnTrazione.setCellValueFactory(new PropertyValueFactory<AutoElettriche, String>("trazioneIntegrale"));
        	this.columnSegmento.setCellValueFactory(new PropertyValueFactory<AutoElettriche, String>("segmento"));
        	this.columnNumPosti.setCellValueFactory(new PropertyValueFactory<AutoElettriche, Integer>("numeroPosti"));
        	this.columnPrestazioni.setCellValueFactory(new PropertyValueFactory<AutoElettriche, Float>("prestazioni"));
        	this.columnVelMax.setCellValueFactory(new PropertyValueFactory<AutoElettriche, Integer>("velocitaMax"));

    		return scelta;
    	}
    	
    	List<AutoElettriche> desiderate=new ArrayList<>();
    	
    	for(AutoElettriche a:this.auto) {
    		int trovato=0;
        	int parametri=0;
    		if(!this.tendinaMarca.getSelectionModel().isEmpty()) {
    			parametri++;
    			//System.out.println(this.tendinaMarca.getValue());
    			if(a.getMarca().equals(this.tendinaMarca.getValue()))
    				trovato++;
    			else
    				trovato=0;
    		}
    		if(!this.tendinaSegmento.getSelectionModel().isEmpty()) {
    			parametri++;
    			//System.out.println(this.tendinaSegmento.getValue());
    			if(a.getSegmento().equals(this.tendinaSegmento.getValue()))
    				trovato++;
    			else
    				trovato=0;
    		}
    		if((int)this.sliderPrezzo.getValue()!=sliderPrezzo.getMin() && !this.sliderPrezzo.isDisable()) {
    			parametri++;
    			//System.out.println(this.sliderPrezzo.getValue());
    			if(a.getPrezzoVendita()<=this.sliderPrezzo.getValue())
    				trovato++;
    			else 
    				trovato=0;	
    		}
    		
    		if(this.sliderNoleggio.getValue()!=sliderNoleggio.getMin() && !this.sliderNoleggio.isDisable()) {
    			parametri++;
    			if(a.getPrezzoNoleggio()<=this.sliderNoleggio.getValue())
    				trovato++;
    			else
    				trovato=0;
    		}
    		
    		if(this.sliderAutonomia.getValue()!=sliderAutonomia.getMin()) {
    			parametri++;
    			if(a.getAutonomia()<=this.sliderAutonomia.getValue())
    				trovato++;
    			else
    				trovato=0;
    		}
    		
    		if(this.sliderPrestazioni.getValue()!=sliderPrestazioni.getMin()) {
    			parametri++;
    			if(a.getPrestazioni()<=this.sliderPrestazioni.getValue())
    				trovato++;
    			else
    				trovato=0;
    		}
    		
    		if(this.sliderVelocita.getValue()!=this.sliderVelocita.getMin()) {
    			parametri++;
    			if(a.getVelocitaMax()<=this.sliderVelocita.getValue())
    				trovato++;
    			else
    				trovato=0;
    		}
    		
    		if(!this.tendinaNumPosti.getSelectionModel().isEmpty()) {
    			parametri++;
    			if(a.getNumeroPosti()==this.tendinaNumPosti.getValue())
    				trovato++;
    			else
    				trovato=0;
    		}
    		
    		if(this.checkTrazione.isSelected()) {
    			parametri++;
    			if(a.isTrazioneIntegrale()==true)
    				trovato++;
    			else
    				trovato=0;
    		}
    		
    		if(this.checkRicarica.isSelected()) {
    			parametri++;
    			if(a.isRicaricaRapida()==true)
    				trovato++;
    			else
    				trovato=0;
    		}
    		
    		
    		//Controllo che trovato abbia lo stesso valore di parametri
    		if(trovato==parametri)
    			desiderate.add(a);
    		
    			
    	}
    	
    	
    	
    	if(desiderate.size()!=0)
    		/*for(AutoElettriche a:desiderate)
    			txtResult.appendText(a.toString()+"\n");*/
    		this.tableResult.getItems().addAll(desiderate);
    	
    	this.columnMarca.setCellValueFactory(new PropertyValueFactory<AutoElettriche, String>("marca"));
    	this.columnModello.setCellValueFactory(new PropertyValueFactory<AutoElettriche, String>("modello"));
    	this.columnVendita.setCellValueFactory(new PropertyValueFactory<AutoElettriche, Integer>("prezzoVendita"));
    	this.columnNoleggio.setCellValueFactory(new PropertyValueFactory<AutoElettriche, Integer>("prezzoNoleggio"));
    	this.columnAutonomia.setCellValueFactory(new PropertyValueFactory<AutoElettriche, Integer>("autonomia"));
    	this.columnEfficienza.setCellValueFactory(new PropertyValueFactory<AutoElettriche, Integer>("efficienza"));
    	this.columnVelRicarica.setCellValueFactory(new PropertyValueFactory<AutoElettriche, Integer>("velocitaRicarica"));
    	this.columnRicaricaRapida.setCellValueFactory(new PropertyValueFactory<AutoElettriche, String>("ricaricaRapida"));
    	this.columnTrazione.setCellValueFactory(new PropertyValueFactory<AutoElettriche, String>("trazioneIntegrale"));
    	this.columnSegmento.setCellValueFactory(new PropertyValueFactory<AutoElettriche, String>("segmento"));
    	this.columnNumPosti.setCellValueFactory(new PropertyValueFactory<AutoElettriche, Integer>("numeroPosti"));
    	this.columnPrestazioni.setCellValueFactory(new PropertyValueFactory<AutoElettriche, Float>("prestazioni"));
    	this.columnVelMax.setCellValueFactory(new PropertyValueFactory<AutoElettriche, Integer>("velocitaMax"));

    	
    	return desiderate;
    }
    
    @FXML
    void goBackHome(MouseEvent event) {
    	try {
        	primaryStage=new Stage();
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("PannelloIniziale.fxml"));
    		BorderPane root = (BorderPane) loader.load();
    		
    		Scene scene=new Scene(root);
    		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    		
    		PannelloInizialeController controller=loader.getController();
    		TesiModel model=new TesiModel();
    		controller.setModel(model);
    		
    		primaryStage.setScene(scene);
    		primaryStage.show();
    		primaryStage.setResizable(false);
    		controller.setItself(primaryStage);
    		itself.close();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}


    }

    @FXML
    void goForward(MouseEvent event) {

    	try {
        	secondaryStage=new Stage();
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("PannelloCalcoloPercorso.fxml"));
    		BorderPane root = (BorderPane) loader.load();
    		
    		Scene scene=new Scene(root);
    		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    		
    		PannelloCalcoloPercorsoController controller=loader.getController();
    		TesiModel model=new TesiModel();
    		controller.setModel(model);
    		
    		secondaryStage.setScene(scene);
    		secondaryStage.show();
    		secondaryStage.setResizable(false);
    		controller.setItself(secondaryStage);
    		controller.setModelloAuto(this.doTrovaRisultati(sceltaAuto));
    		itself.close();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}

    }


    @FXML
    void initialize() {
    	assert btnHome != null : "fx:id=\"btnHome\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert btnForward != null : "fx:id=\"btnForward\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert checkCompro != null : "fx:id=\"checkCompro\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert checkNoleggio != null : "fx:id=\"checkNoleggio\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert tendinaMarca != null : "fx:id=\"tendinaMarca\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert tendinaModello != null : "fx:id=\"tendinaModello\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert tendinaSegmento != null : "fx:id=\"tendinaSegmento\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert tendinaNumPosti != null : "fx:id=\"tendinaNumPosti\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert checkTrazione != null : "fx:id=\"checkTrazione\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert checkRicarica != null : "fx:id=\"checkRicarica\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert sliderPrezzo != null : "fx:id=\"sliderPrezzo\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert sliderNoleggio != null : "fx:id=\"sliderNoleggio\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert sliderAutonomia != null : "fx:id=\"sliderAutonomia\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert sliderVelocita != null : "fx:id=\"sliderVelocita\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert sliderPrestazioni != null : "fx:id=\"sliderPrestazioni\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert btnRisultatiAuto != null : "fx:id=\"btnRisultatiAuto\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert btnResetAuto != null : "fx:id=\"btnResetAuto\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        //assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert tableResult != null : "fx:id=\"tableResult\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert columnMarca != null : "fx:id=\"columnMarca\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert columnModello != null : "fx:id=\"columnModello\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert columnVendita != null : "fx:id=\"columnVendita\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert columnNoleggio != null : "fx:id=\"columnNoleggio\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert columnAutonomia != null : "fx:id=\"columnAutonomia\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert columnEfficienza != null : "fx:id=\"columnEfficienza\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert columnVelRicarica != null : "fx:id=\"columnVelRicarica\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert columnRicaricaRapida != null : "fx:id=\"columnRicaricaRapida\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert columnTrazione != null : "fx:id=\"columnTrazione\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert columnSegmento != null : "fx:id=\"columnSegmento\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert columnNumPosti != null : "fx:id=\"columnNumPosti\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert columnPrestazioni != null : "fx:id=\"columnPrestazioni\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";
        assert columnVelMax != null : "fx:id=\"columnVelMax\" was not injected: check your FXML file 'PannelloSceltaAuto.fxml'.";

    }

	public void setModel(TesiModel model) {
		this.model = model;
		this.tendinaMarca.getItems().addAll(this.model.getMarche());
		this.tendinaModello.getItems().addAll(this.model.getModelli());
		this.tendinaNumPosti.getItems().addAll(this.model.getNumPosti());
		

		this.tendinaSegmento.getItems().addAll(this.model.getSegmenti());
		this.auto=this.model.getAllAuto();
		this.tableResult.getItems().add(null);
		
	}

	public void setItself(Stage itself) {
		this.itself = itself;
	}
	
    
}
