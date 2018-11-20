package view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import controller.OcrController;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class OcrGUI extends Application {

	@FXML
	private ColorPicker characterColor;
	
	@FXML
	private ColorPicker lineColor;
	
	@FXML
	private ColorPicker paragraphColor;
	
	@FXML
	private Button loadFile;
	
	@FXML
	private Button fullScreen;
	
	@FXML
	private ImageView imageView;
	
	@FXML
	private ImageView originalImage;
	
	@FXML
	private Slider treshold;
	
	@FXML
	private CheckBox rotateCheckBox;
	
	private Label label;
	private FileChooser fileChooser;
	private final OcrController controller = application.Main.controller;
	private static File imageFile;
	private static Image processedImage;
	private static Stage stage;
	private String mode = "letters";
	private Color color;
	private Desktop desktop = Desktop.getDesktop();
	
	
	@Override
    public void start(Stage primaryStage) {
		primaryStage.setTitle("DUMB OCR");
		stage = primaryStage;
        Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("ui.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(root != null) {
			primaryStage.setScene(new Scene(root));
	        
	        fileChooser = new FileChooser();
	        
	        characterColor = (ColorPicker) primaryStage.getScene().lookup("#characterColor");
	        characterColor.setValue(Color.GREEN);
	        loadFile = (Button) primaryStage.getScene().lookup("#loadFile");
	        
	        loadFile.setOnAction(
				new EventHandler<ActionEvent>() {
				    @Override
				    public void handle(final ActionEvent e) {
				    	configureFileChooser(fileChooser);
				        imageFile = fileChooser.showOpenDialog(primaryStage);
				        label = (Label) primaryStage.getScene().lookup("#fileLabel");
				        originalImage = (ImageView) primaryStage.getScene().lookup("#originalImage");
				        
				        if (imageFile != null) {
				        	label.setText(imageFile.getName());
				        	originalImage.setImage(new Image(imageFile.toURI().toString()));
				        } else {
				        	label.setText("Sikertelen kép betöltés!");
				        }
				    }
				});
	        
	        treshold = (Slider) primaryStage.getScene().lookup("#treshold");
	        treshold.setValue(50);
	        
	        primaryStage.show();
	        
		} else {
			System.out.println("Failed to load ui.fxml!");
		}
    }
    
    public void startGUI() {
    	launch();
    }
    
    
    private static void configureFileChooser(FileChooser fileChooser){                           
    	fileChooser.setTitle("Kép betöltése");
        fileChooser.setInitialDirectory(
            new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("JPG", "*.jpg"),
            new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }
    
    public void run() {
    	if (imageFile == null) {
    		constructAlert("futtatáshoz");
    	} else {
    		
    		if (color == null) {
    			color = characterColor.getValue();
    		}
    		
    		if (!lineColor.isDisabled()) {
    			color = lineColor.getValue();
    		} else if (!characterColor.isDisabled()) {
    			color = characterColor.getValue();
    		} else {
    			color = paragraphColor.getValue();
    		}
    		
    		processedImage = controller.getProcessedPicture(mode, color.toString(), treshold.getValue(), rotateCheckBox.isSelected());
    		
    		if (processedImage == null) {
    			constructWarning("futtatás");
    		} else {
	        	fullScreen.setOpacity(1);
	        	fullScreen.setDisable(false);
	        	fullScreen.setGraphic(new ImageView(new Image(new File("src/view/img/full-image.png").toURI().toString())));
	        	
	        	imageView.setImage(processedImage);
    		}
    		
    	}
    }
    
    @SuppressWarnings("deprecation")
	public void loadFullScreen() {
    	try {
    		if (desktop != null && processedImage != null) {
    			desktop.open(new File(processedImage.impl_getUrl().substring(5)));
    		} else {
    			throw new IOException("");
    		}
        } catch (IOException ex) {
        	System.out.println("Failed to load image into desktop!");
        }
    }
    
    
    public void saveMenuAction() {
    	Image processedImage = imageView.getImage();
    	
    	if (processedImage == null) {
    		constructAlert("mentéshez");
    	} else {
    		FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Feldolgozott kép mentése...");
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                try {
                	ImageIO.write(SwingFXUtils.fromFXImage(processedImage, null), "jpg", file);         
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
    	}
    }
    
    public void quitMenuAction() {
        System.exit(0);
    }
    
    public void aboutMenuAction() {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Információ");
    	alert.setHeaderText("Az alkalmazást készítették: Barta Bence, Csuvik Viktor, Mészáros Jenő");
    	alert.setContentText("Képfeldolgozás haladóknak");
    	alert.showAndWait();
    }
    
    public void enableCharacter() {
    	characterColor.setValue(Color.GREEN);
    	characterColor.setDisable(false);
    	lineColor.setDisable(true);
    	paragraphColor.setDisable(true);
    	mode = "letters";
    	color = characterColor.getValue();
    }
    
    public void enableLine() {
    	lineColor.setValue(Color.RED);
    	lineColor.setDisable(false);
    	paragraphColor.setDisable(true);
    	characterColor.setDisable(true);
    	mode = "words";
    	color = lineColor.getValue();
    }
    
    public void enableParagraph() {
    	paragraphColor.setValue(Color.BLUE);
    	paragraphColor.setDisable(false);
    	lineColor.setDisable(true);
    	characterColor.setDisable(true);
    	mode = "lines";
    	color = paragraphColor.getValue();
    }
    
    public void enableTresholdiing() {
    	treshold.setDisable(!treshold.isDisable());
    }

    public void showProperties() {
    	Properties dialog = new Properties();
    	dialog.start(stage);
    }
    
    public void constructAlert(String errorRoot) {
    	Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Hiba");
		alert.setHeaderText("A " + errorRoot + " be kell tölteni egy képet!");
		alert.setContentText("A \"Kép betöltése\" gombra kattintva és egy képet kiválasztva lehet képet betölteni.");
		alert.showAndWait();
    }
    
   public void constructWarning(String errorRoot) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Hiba");
		alert.setHeaderText("Nem sikerült a " + errorRoot + ".");
		alert.setContentText("Próbálja újra.");
		alert.showAndWait();
   }
    
	public ColorPicker getCharacterColor() {
		return characterColor;
	}

	public void setCharacterColor(ColorPicker characterColor) {
		this.characterColor = characterColor;
	}

	public ColorPicker getLineColor() {
		return lineColor;
	}

	public void setLineColor(ColorPicker lineColor) {
		this.lineColor = lineColor;
	}

	public ColorPicker getParagraphColor() {
		return paragraphColor;
	}

	public void setParagraphColor(ColorPicker paragraphColor) {
		this.paragraphColor = paragraphColor;
	}

	public OcrController getController() {
		return controller;
	}

	public static File getImageFile() {
		return imageFile;
	}

	public static void setImageFile(File imageFile) {
		OcrGUI.imageFile = imageFile;
	}
    
	public String getPath() {
		return Properties.getPath();
	}
}