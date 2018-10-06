package view;

import controller.OcrController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OcrGUI extends Application {

	private OcrController controller;

	@Override
    public void start(Stage primaryStage) throws Exception {
    	
        Parent root = FXMLLoader.load(getClass().getResource("ui.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    
    public void startGUI() {
    	launch();
    }
    
    public OcrController getController() {
		return controller;
	}

}