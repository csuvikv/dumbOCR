package controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import engine.Engine;
import javafx.scene.image.Image;
import view.OcrGUI;

public class OcrController {	
	private OcrGUI gui;
	private Engine engine;
	
	public OcrController() {
		engine = new Engine();
	}

	public void startDesktop() {
		gui = new OcrGUI();
		gui.startGUI();
	}

	public Image getProcessedPicture() {
		Image result = null;
		try {
			result = engine.processPicture("", 30);
		} catch (IOException e) {
			System.err.println("IOExeption happened while trying to execute the Matlab script!");
		} catch (InterruptedException e) {
			System.err.println("InterruptedException happened while trying to sleep!");
		}
		/*
		int counter = 0;
		while(true) {
			if(counter > 29) {
				break;
			} else {
				++counter;
			}
			
			file = new File(output);
			if(file.exists()) {
				try {
					String tempURL = file.toURI().toURL().toString();
					result = new Image(tempURL, true);
				} catch (MalformedURLException e) {
					System.err.println("Malformed URL!");
				}
				
				break;
			}
			System.err.println("Image not found. Retrying... (" + counter + ")");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.err.println("Sleep interrupted!");
			}
		}
		
		
		String delc = "del temp/temp,png";
		System.out.println("exec: " + delc);
		if(file.exists()) {
			try {
				rt.exec(delc);
			} catch (IOException e) {
				System.err.println("Exception while trying to delete the temp file!");
			}
		}
		
		
		System.out.println("Now returning");
		*/
		return result;
	}
	
	public Image getLoadedImage() {
		return new Image(OcrGUI.getImageFile().toURI().toString());
	}
}