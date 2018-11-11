package controller;

import java.io.IOException;

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

	public Image getProcessedPicture(String ProcessMode, String color) {
		Image result = null;
		try {
			result = engine.processPicture(ProcessMode, color, 60);
		} catch (IOException e) {
			System.err.println("IOExeption happened while trying to execute the Matlab script!\n"
					+ e.getMessage());
		} catch (InterruptedException e) {
			System.err.println("InterruptedException happened while trying to sleep!");
		}
		
		return result;
	}
	
	public Image getLoadedImage() {
		return new Image(OcrGUI.getImageFile().toURI().toString());
	}

	public Image getTresholdedPicture(double value) {
		// TODO: treshold the image; return null if not succesfull
		return new Image(OcrGUI.getImageFile().toURI().toString());
	}
}