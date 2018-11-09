package engine;

import java.io.File;
import java.io.IOException;

import javafx.scene.image.Image;
import view.OcrGUI;

public class Engine {
	private final static String mPlacePar = "'src/matlab'";
	private final static String mCommand = "matlab -nodesktop -nosplash -minimize -r";
	private final static String mFile = "func";
	private final static String output = "temp/temp.png";

	public Image processPicture(String procMode, int numOfMaxTries) throws IOException, InterruptedException {
		Image result = null;
		Runtime rt = Runtime.getRuntime();
		String procFilePar = "'" + OcrGUI.getImageFile().getAbsolutePath() + "'";
		String procModePar = "'" + procMode + "'";
		String outputPar = "'" + output + "'";
		String command = mCommand + " \"addpath(" + mPlacePar + "); " + mFile +
				"(" + procFilePar + "," + procModePar + "," + outputPar + "); quit\"";
		
		System.out.println("exec: " + command);
		rt.exec(command);
		System.out.println("Matlab script execution is successful.");
		
		File tempFile = new File(output);;
		if(tempFile.exists()) {
			System.out.print("Clearing temp folder...");
			tempFile.delete();
			System.out.println(" Cleared.");
		} else {
			System.out.println("'temp' folder is empty. Nothing to do.");
		}
		
		int tryCounter = 1;
		boolean isFileExist = false;
		while(true) {
			if(tryCounter > numOfMaxTries) {
				break;
			}
			
			tempFile = new File(output);
			if(tempFile.exists()) {
				isFileExist = true;
				break;
			}
			System.out.println("Result file not found. Retrying... (" + tryCounter + ")");
			++tryCounter;
			Thread.sleep(1000);
		}
		
		if(isFileExist) {
			String tempFileURL = tempFile.toURI().toURL().toString();
			result = new Image(tempFileURL, true);
			tryCounter = 1;
			while(true) {
				if(tempFile.delete()) {
					System.out.println("Temp file sucessfully deleted.");
					break;
				} else if(tryCounter >= 10) {
					System.out.println("Temp file cannot be deleted.");
					break;
				}
				
				Thread.sleep(1000);
			}
			
		} else {
			System.err.println("Opening the temp file is failed, returning to caller.");
		}
		
		System.out.println("Image is processed, now returning.");
		return result;
	}
	
}
