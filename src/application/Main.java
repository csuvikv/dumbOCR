package application;

import controller.OcrController;

public class Main {
	
	public static OcrController controller;
	
    public static void main(String[] args) {
    	
    	controller = new OcrController();
    	
    	controller.startDesktop();
    	

    }
}