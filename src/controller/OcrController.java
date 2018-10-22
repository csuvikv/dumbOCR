package controller;

import java.io.File;

import javafx.scene.image.Image;
import view.OcrGUI;

	public class OcrController {

	   private OcrGUI gui;
	   
	   private File processedFile;

	    
	    public void startDesktop() {
	    	gui = new OcrGUI();
	    	gui.startGUI();
	    }


		public Image getProcessedPicture() {
			processedFile = OcrGUI.getImageFile();
			Image image = new Image(processedFile.toURI().toString());
			
			//TODO: process the image..
			// return null if not succesfull
			
			return image;
		}
		
		public Image getLoadedImage() {
			return new Image(OcrGUI.getImageFile().toURI().toString());
		}
	}