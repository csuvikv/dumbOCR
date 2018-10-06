package controller;

	import javafx.fxml.FXML;
import javafx.scene.text.Text;
import view.OcrGUI;

	public class OcrController {

	    @FXML
	    private Text output;

	    @FXML
	    private void something() {
	    	
	        output.setText("TADAAA");
	    }

	    
	    public void startDesktop() {
	    	OcrGUI gui = new OcrGUI();
	    	gui.startGUI();
	    }
	}