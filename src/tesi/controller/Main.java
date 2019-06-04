package tesi.controller;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import tesi.model.TesiModel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//System.out.println(getClass().getResource("PannelloIniziale.fxml"));
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
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
