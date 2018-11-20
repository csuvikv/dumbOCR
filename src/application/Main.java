package application;

import java.io.File;

import controller.OcrController;

public class Main {
	
	public static OcrController controller;
	
    public static void main(String[] args) {
    	new File("temp").mkdirs();
    	controller = new OcrController();
    	
    	controller.startDesktop();
    	

    }
}