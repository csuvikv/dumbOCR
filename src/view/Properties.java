package view;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Properties extends Application {

	@FXML
	private TextField matlabPath;
	
	private static String path = "";
	private static Stage stage;
	
	@Override
    public void start(Stage primaryStage) {
		stage = new Stage();
		stage.setTitle("Properties");
        Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("properties.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (root != null) {
			stage.setScene(new Scene(root));
			
			if (! "".equals(path)) {
				matlabPath = (TextField) stage.getScene().lookup("#matlabPath");
				matlabPath.setText(path);
				matlabPath.setDisable(false);
				
				CheckBox matlabPathCheckBox = (CheckBox) stage.getScene().lookup("#matlabPathCheckBox");
				matlabPathCheckBox.setSelected(true);
			}
			
			stage.show();
	        
		} else {
			System.out.println("Failed to load properties.fxml!");
		}
    }
    
	
	public void enableMatlabPath() {
		matlabPath.setDisable(!matlabPath.isDisable());
	}
	
	public void savePath() {
		if (!matlabPath.isDisable()) {
			path = matlabPath.getText();
		} else {
			path = "";
		}
		stage.close();
	}
	
	public void cancelPath() {
		path = "";
		stage.close();
	}


	public static String getPath() {
		return path;
	}


	public static void setPath(String path) {
		Properties.path = path;
	}
}