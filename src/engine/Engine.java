package engine;

import java.io.File;
import java.io.IOException;

import javafx.scene.image.Image;
import view.OcrGUI;

public class Engine {
	private final static String mCommand = "matlab -nodesktop -nosplash -minimize -r";
	private final static String mFile = "func";
	private final static String mPlace = "cd src/matlab";
	private final static String output = "temp/temp.png";
	private final static String back = "cd ../..";
	
	
	public Image processPicture(String procMode, int numOfMaxTries) throws IOException, InterruptedException {
		Image result = null;
		Runtime rt = Runtime.getRuntime();
		String procFilePar = "'" + OcrGUI.getImageFile().getAbsolutePath() + "'";
		String procModePar = "'" + procMode + "'";
		String outputPar = "'../../" + output + "'";
		String command = mPlace + " && " + mCommand + " \"" + mFile +
				"(" + procFilePar + "," + procModePar + "," + outputPar + "); quit\" & " + back;
		
		System.out.println("exec: " + command);
		rt.exec(command);
		System.out.println("Execution is successful.");
		
		File file = null;
		int tryCounter = 1;
		boolean isFileExist = false;
		while(true) {
			if(tryCounter > numOfMaxTries) {
				break;
			}
			
			file = new File(output);
			if(file.exists()) {
				isFileExist = true;
				break;
			}
			System.out.println("Result file not found... Retrying (" + tryCounter + ")");
			++tryCounter;
			Thread.sleep(1000);
		}
		
		if(isFileExist) {
			String fileURL = file.toURI().toURL().toString();
			result = new Image(fileURL, true);
			command = "del " + output;
			rt.exec(command);
		} else {
			System.err.println("Opening the temp file is failed, returning to caller.");
		}
		
		System.out.println("Image is processed, now returning.");
		return result;
	}
	
}
