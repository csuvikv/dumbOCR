package controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

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
		
		while(true) {
			file = new File("temp/result.png");
			if(file.exists()) {
				
				try {
					String tempURL = file.toURI().toURL().toString();
					result = new Image(tempURL, true);
				} catch (MalformedURLException e) {
					System.err.println("Malformed URL!");
				}
				
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.err.println("Sleep interrupted!");
			}
		}
		
		System.out.println("Now returning");
		return result;
	}
	
	public Image getLoadedImage() {
		return new Image(OcrGUI.getImageFile().toURI().toString());
	}
}