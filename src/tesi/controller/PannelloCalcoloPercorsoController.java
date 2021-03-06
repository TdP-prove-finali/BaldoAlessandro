package tesi.controller;

import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import com.javadocmd.simplelatlng.LatLng;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import tesi.model.AutoElettriche;
import tesi.model.City;
import tesi.model.StazioniRicarica;
import tesi.model.TesiModel;
import tesi.model.Vertex;

public class PannelloCalcoloPercorsoController {

	private TesiModel model;
	private Stage primaryStage;
	private Stage itself;
	private List<AutoElettriche> choice=null;
	
	
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private ImageView btnHome;
    
    @FXML
    private TextField txtModelloScelto;

    @FXML
    private TextField txtAutonomiaScelta;

    @FXML
    private ComboBox<String> boxCittaPartenza;

    @FXML
    private TextField txtLatPartenza;

    @FXML
    private TextField txtLongPartenza;

    @FXML
    private ComboBox<String> boxCittaArrivo;

    @FXML
    private TextField txtLatArrivo;

    @FXML
    private TextField txtLongArrivo;
    
    @FXML
    private RadioButton checkColonnineFast;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private Button btnResetPercorso;

    @FXML
    private TextArea txtResult;
    
    @FXML
    private WebView webView;
    
   
    
    
    @FXML
    void doBackHome(MouseEvent event) {

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
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	 WebEngine engine=this.webView.getEngine();

		String partenza=this.boxCittaPartenza.getValue();
		if(partenza==null) {
			txtResult.appendText("Devi inserire un punto di partenza\n");
			return;
		}
		String arrivo=this.boxCittaArrivo.getValue();
		if(arrivo==null) {
			txtResult.appendText("Devi inserire un punto di arrivo\n");
			return;
		}
		
		txtResult.appendText("Distanza tra i due punti: "+this.model.distanzaPunti(new LatLng(Float.parseFloat(this.txtLatPartenza.getText()), Float.parseFloat(this.txtLongPartenza.getText())),new LatLng(Float.parseFloat(this.txtLatArrivo.getText()), Float.parseFloat(this.txtLongArrivo.getText())))+" km\n");
		
		if(this.checkColonnineFast.isDisabled()) //veicoli con solo ricarica lenta
			this.model.creaGrafoSlow(partenza, arrivo, Integer.parseInt(this.txtAutonomiaScelta.getText()));
		
		//Veicoli che supportano anche Ricarica Rapida
		else if(!this.checkColonnineFast.isSelected() && !this.checkColonnineFast.isDisabled())
			this.model.creaGrafo(partenza, arrivo, Integer.parseInt(this.txtAutonomiaScelta.getText()));
		
		else
			this.model.creaGrafoFast(partenza, arrivo, Integer.parseInt(this.txtAutonomiaScelta.getText()));

		
		this.model.verticiRaggiungibili(this.model.getCityByName(partenza));
		List<Vertex> percorso=this.model.spanningTreeFinoA(this.model.getCityByName(arrivo));
		int number=0;
		for(Vertex v:percorso)
			if(v instanceof StazioniRicarica)
				number++;
		if(number!=0) {
			txtResult.appendText(String.format("Possibili stazioni di ricarica presenti nel percorso tra %s e %s\n",partenza,arrivo));
			for(Vertex v:percorso) {
				if(v instanceof StazioniRicarica)
					txtResult.appendText("•"+((StazioniRicarica)v).toString());}
		}
		
		
		
			List<Vertex> percorsoOttimo=this.model.calcolaCamminoMinimo(partenza, arrivo);
			if(percorsoOttimo!=null) {
				txtResult.appendText("Il percorso più veloce per raggiungere "+arrivo+" da "+partenza+" è:\n");
				String s="https://www.google.com/maps/dir/";
				
				for(Vertex v: percorsoOttimo) {
					if(v instanceof City) {
						txtResult.appendText("•"+((City)v).toString());
						String city[]=((City)v).getNome().split(" ");
						for(String sub:city)
							s+=sub+",+";
						s+="California,+Stati+Uniti/";}
					else {
						txtResult.appendText("•"+((StazioniRicarica)v).toString());
						String charge[]=((StazioniRicarica)v).getStationAddress().split(" ");
						for(String sub:charge)
							s+=sub+",+";
						String city[]=((StazioniRicarica)v).getCity().getNome().split(" ");
						for(String sub:city)
							s+=sub+",+";
						s+="California,+Stati+Uniti/";}
					
						
				}
				
				engine.load(s);
				this.webView.setDisable(true);
				//System.out.println(s);
			}
			else
				txtResult.appendText("Per i punti indicati non è stato possibile calcolare alcun percorso");
			
			
		
    }

    @FXML
    void doResetPercorso(ActionEvent event) {
    	this.txtResult.clear();
    	this.boxCittaArrivo.setValue(null);
    	this.boxCittaPartenza.setValue(null);
    	this.txtLatArrivo.clear();
    	this.txtLatPartenza.clear();
    	this.txtLongArrivo.clear();
    	this.txtLongPartenza.clear();
    	WebEngine webEngine=webView.getEngine();
    	this.checkColonnineFast.selectedProperty().set(false);
    	webEngine.load("https://www.google.com/maps/place/California,+Stati+Uniti/@37.1843034,-123.7975979");
		webView.setZoom(0.6);
		this.btnCalcolaPercorso.setDisable(true);
    }

    @FXML
    void generaCoordinateArrivo(ActionEvent event) {
    	if(!this.boxCittaArrivo.getSelectionModel().isEmpty()) {
    	String arrivo=this.boxCittaArrivo.getValue();
    	String latArrivo=this.model.getLatitudine(arrivo);
    	this.txtLatArrivo.setText(latArrivo);
    	String longArrivo=this.model.getLongitudine(arrivo);
    	this.txtLongArrivo.setText(longArrivo);
    	
    	if(!this.txtLatPartenza.getText().equals("") && !this.txtLongPartenza.getText().equals(""))
    		this.btnCalcolaPercorso.setDisable(false);
    	
    	}
    	
    }

    @FXML
    void generaCoordinatePartenza(ActionEvent event) {
    	if(!this.boxCittaPartenza.getSelectionModel().isEmpty()) {
    	String partenza=this.boxCittaPartenza.getValue();
    	this.txtLatPartenza.setText(this.model.getLatitudine(partenza));
    	this.txtLongPartenza.setText(this.model.getLongitudine(partenza));
    	
    	if(!this.txtLatArrivo.getText().equals("") && !this.txtLongArrivo.getText().equals(""))
    		this.btnCalcolaPercorso.setDisable(false);
    	
    	}
    	
    }

    @FXML
    void initialize() {
    	assert btnHome != null : "fx:id=\"btnHome\" was not injected: check your FXML file 'PannelloCalcoloPercorso.fxml'.";
    	assert txtModelloScelto != null : "fx:id=\"txtModelloScelto\" was not injected: check your FXML file 'PannelloCalcoloPercorso.fxml'.";
    	assert txtAutonomiaScelta != null : "fx:id=\"txtAutonomiaScelta\" was not injected: check your FXML file 'PannelloCalcoloPercorso.fxml'.";
        assert boxCittaPartenza != null : "fx:id=\"boxCittaPartenza\" was not injected: check your FXML file 'PannelloCalcoloPercorso.fxml'.";
        assert txtLatPartenza != null : "fx:id=\"txtLatPartenza\" was not injected: check your FXML file 'PannelloCalcoloPercorso.fxml'.";
        assert txtLongPartenza != null : "fx:id=\"txtLongPartenza\" was not injected: check your FXML file 'PannelloCalcoloPercorso.fxml'.";
        assert boxCittaArrivo != null : "fx:id=\"boxCittaArrivo\" was not injected: check your FXML file 'PannelloCalcoloPercorso.fxml'.";
        assert txtLatArrivo != null : "fx:id=\"txtLatArrivo\" was not injected: check your FXML file 'PannelloCalcoloPercorso.fxml'.";
        assert txtLongArrivo != null : "fx:id=\"txtLongArrivo\" was not injected: check your FXML file 'PannelloCalcoloPercorso.fxml'.";
        assert checkColonnineFast != null : "fx:id=\"checkColonnineFast\" was not injected: check your FXML file 'PannelloCalcoloPercorso.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'PannelloCalcoloPercorso.fxml'.";
        assert btnResetPercorso != null : "fx:id=\"btnResetPercorso\" was not injected: check your FXML file 'PannelloCalcoloPercorso.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'PannelloCalcoloPercorso.fxml'.";
        assert webView != null : "fx:id=\"webView\" was not injected: check your FXML file 'PannelloCalcoloPercorso.fxml'.";
    }

	public void setModel(TesiModel model) {
		this.model = model;
		this.boxCittaPartenza.getItems().addAll(this.model.getCitta());
		this.boxCittaArrivo.getItems().addAll(this.model.getCitta());
		WebEngine webEngine=webView.getEngine();
		webEngine.load("https://www.google.com/maps/place/California,+Stati+Uniti/@37.1843034,-123.7975979");
		webView.setZoom(0.6);
	}

	public void setItself(Stage itself) {
		this.itself = itself;
	}

	

	public void setModelloAuto(List<AutoElettriche> choice) {
		double rapporto=0.0;
		int autonomia=0;
		String modello="";
		this.choice=choice;
		System.out.println(this.choice);
		if(choice.size()!=0) {
    		for(AutoElettriche a:choice) {
    			//System.out.println((double)(a.getAutonomia())/a.getPrezzoVendita());
    			if(((double)a.getAutonomia()/a.getPrezzoVendita())>rapporto) {
    				rapporto=((double)a.getAutonomia())/a.getPrezzoVendita();
    				System.out.println(rapporto);
    				autonomia=a.getAutonomia();
    				modello=a.getModello();
    				}
    		}
		}
		
    			
		this.txtAutonomiaScelta.setText(""+autonomia);
		this.txtModelloScelto.setText(modello);
		if(!this.model.getAutoByModello(modello).isRicaricaRapida())
			this.checkColonnineFast.setDisable(true);
	}
	
	public void setModelloAuto() {
		int autonomia=0;
		String modello="";
		Random rand=new Random();
		int i=rand.nextInt(model.getAllAuto().size());
		autonomia=model.getAllAuto().get(i).getAutonomia();
		modello=model.getAllAuto().get(i).getModello();
		this.txtAutonomiaScelta.setText(""+autonomia);
		this.txtModelloScelto.setText(modello);
		if(!this.model.getAutoByModello(modello).isRicaricaRapida())
			this.checkColonnineFast.setDisable(true);
	}
	
	
    
	
}
