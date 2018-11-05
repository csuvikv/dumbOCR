package controller;

import java.io.File;
import java.io.IOException;

import javafx.scene.image.Image;
import view.OcrGUI;

public class OcrController {
	private final static String matlabCommand = "matlab -nodesktop -nosplash -minimize -r";
	
	private final static String output = "temp.jpg";
	private OcrGUI gui;

	public void startDesktop() {
		gui = new OcrGUI();
		gui.startGUI();
	}


	public Image getProcessedPicture() {
		Runtime rt = Runtime.getRuntime();
		String processedFile = OcrGUI.getImageFile().getAbsolutePath();
		File file = null;
		Image result = null;
		
		// matlab -nodesktop -nosplash -minimize -r "func('test1.jpg'); quit"	
		String command = matlabCommand + " \"func('" + processedFile + "'); quit\"";
		
		System.out.println("execute: " + command);
		/*try {
			rt.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		/*while(true) {
			File = new File("../../result.png")
		}*/
		
		
		file = new File("temp/result.png");
		if(file.exists()) {
			System.out.println("File exist");
			// result = new Image("temp/result.png", true);
		} else {
			System.out.println("File not exist");
		}
		
		System.out.println("Now returning");
		return result;
	}
	
	public Image getLoadedImage() {
		return new Image(OcrGUI.getImageFile().toURI().toString());
	}
}