package controller;

import java.io.StringWriter;
import java.util.concurrent.ExecutionException;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import view.OcrGUI;

	public class OcrController {

		private MatlabEngine engine;
		
	    @FXML
	    private Text output;

	    @FXML
	    private void something() {
	    	
	    	StringWriter matlabOut = null;
	    	try {
				engine.eval("syms x;", null, null);
				engine.eval("f = sin(x)/x;", null, null);
				matlabOut = new StringWriter();
				engine.eval("limit(f, x, 0)", matlabOut, null);
				
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
				output.setText("Matlab error");
			}
	    	
	        output.setText(matlabOut.toString());
	    }

	    
	    public void startDesktop() {
	    	try {
				engine = MatlabEngine.startMatlab();
			} catch (EngineException | IllegalArgumentException | IllegalStateException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	OcrGUI gui = new OcrGUI();
	    	gui.startGUI();
	    }
	}