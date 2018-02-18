package fer.hr.telegra.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

import fer.hr.telegra.MainApp;
import fer.hr.telegra.model.*;

public class DataSetEditDialogController {
	
	@FXML
	private TextField nameField;
	@FXML
	private TextField cameraAngleField;
	@FXML
	private ComboBox<WeatherConditions> weatherConditionCombo = new ComboBox<>();
	@FXML
	private TextField cameraHighField;
	@FXML
	private ComboBox<ImageQuality> imageQualityCombo = new ComboBox<>();
	@FXML
	private TextField imagesLocationField;
	@FXML
	private TextField annotationsLocationField;
	
	private Stage dialogStage;
	private DataSet dataSet;
	private boolean okClicked = false;
	
	/**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     * In combobox put all enums for weathercondition and imagequality
     */
    @FXML
    private void initialize() {
    	weatherConditionCombo.getItems().setAll(WeatherConditions.values());
    	imageQualityCombo.getItems().setAll(ImageQuality.values());
    }

    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.setResizable(false);
    }
    
    
    /**
     * Sets the dataset to be edited in the dialog.
     * 
     * @param dataSet
     */
    public void setDataSet (DataSet dataSet) {
    	this.dataSet = dataSet;
    	
    	nameField.setText(dataSet.getDataSetName());
    	imagesLocationField.setText(dataSet.getDataSetImagesLocation());
    	annotationsLocationField.setText(dataSet.getDataSetAnnotationsLocation());
    	cameraAngleField.setText(Double.toString(dataSet.getCameraAngle()));
    	weatherConditionCombo.setValue(dataSet.getWeatherCondition());
    	imageQualityCombo.setValue(dataSet.getImageQuality());
    	cameraHighField.setText(Double.toString(dataSet.getCameraHigh()));
    	
    }
    
    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }
    
    /**
     * Method which is called when user clicks ok.
     */
    @FXML
    private void handleOK() {
    	if (isInputValid()) {
    		dataSet.setDataSetName(nameField.getText());
    		dataSet.setDataSetImagesLocation(imagesLocationField.getText());
    		dataSet.setDataSetAnnotationsLocation(annotationsLocationField.getText());
    		dataSet.setCameraAngle(Double.parseDouble(cameraAngleField.getText()));
    		dataSet.setCameraHigh(Double.parseDouble(cameraHighField.getText()));
    		dataSet.setWeatherCondition(weatherConditionCombo.getValue());
    		dataSet.setImageQuality(imageQualityCombo.getValue());
    		/**
    		//Add images from dataset location to dataset
    		if(!locationField.getText().equals("No Directory selected")) {
	    		File dir = new File(locationField.getText());
	    		File[] files = dir.listFiles();
				for (File file: files) {
					if(file.isFile()) {
						String stringFile = file.toString();
						String ext ="";
						//var p is here because of filename with multiple "." 
						//it takes last "."
						int i = stringFile.lastIndexOf('.');
						int p = Math.max(stringFile.lastIndexOf('/'), stringFile.lastIndexOf('\\'));
						if (i>p) {
							ext = stringFile.substring(i+1);
							ext = ext.toLowerCase();
						}
						if(ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png") || ext.equals("bmp")) {
							DataImage image = new DataImage(file.getName(), file.getAbsolutePath());
							//UNAPRIJEDITI SRUSI SE ZA VELIK BROJ SLIKA
							if(!dataSet.containsImage(image.getImagePath())) {
								dataSet.addDataSetImage(image);
							}
						}
					}
				}
    		}
			dataSet.setNumOfImgInDataSet();
			*/
    		
    		okClicked = true;
    		dialogStage.close();
    	}
    }
    
    /**
     * Method which is called when user clicks cancel.
     */
    @FXML
    private void handleCancel() {
    	dialogStage.close();
    }
    
    /**
     * Method which is called when user clicks browse for images location.
     */
    @FXML
    private void handleBrowse() {
    	DirectoryChooser directoryChooser = new DirectoryChooser();
    	File selectedDirectory = 
                directoryChooser.showDialog(dialogStage);
    	//LastPath.setLastFilePath(selectedDirectory);
         
        if(selectedDirectory == null){
            //locationField.setText("No Directory selected");
        }else{
            imagesLocationField.setText(selectedDirectory.getAbsolutePath());
        }
    }
    
    /**
     * Method which is called when user clicks browse for annotations location.
     */
    @FXML
    private void handleBrowse1() {
    	DirectoryChooser directoryChooser = new DirectoryChooser();
    	File selectedDirectory = 
                directoryChooser.showDialog(dialogStage);
    	//LastPath.setLastFilePath(selectedDirectory);
         
        if(selectedDirectory == null){
            //locationField.setText("No Directory selected");
        }else{
            annotationsLocationField.setText(selectedDirectory.getAbsolutePath());
        }
    }
    
    /**
     * Validates user input of text fields
     */
    private boolean isInputValid() {
    	String errorMessage = "";
    	
    	if (nameField.getText() == null || nameField.getText().length() == 0) {
    		errorMessage += "No valid Name!\n";
    	}
    	if (cameraAngleField.getText() == null || cameraAngleField.getText().length() == 0) {
    		errorMessage += "No valid Camera Angle!\n";
    	}
    	else {
    		//Try parse camera angle to double
    		try {
    			Double.parseDouble(cameraAngleField.getText());
    		} catch (NumberFormatException e) {
    			errorMessage += "No valid Camera Angle!\n";
    		}
    	}
    	if (cameraHighField.getText() == null || cameraHighField.getText().length() == 0) {
    		errorMessage += "No valid Camera High!\n";
    	}
    	else {
    		//Try parse camre high to double
    		try {
    			Double.parseDouble(cameraHighField.getText());
    		} catch (NumberFormatException e) {
    			errorMessage += "No valid Camera High!\n";
    		}
    	}
    	
    	if (!imagesLocationField.getText().equals("No Directory selected")) {
    		File dir = new File(imagesLocationField.getText());
    		//If path isnt valid or path isnt directory
    		if(!(dir.exists() && dir.isDirectory())) {
    			errorMessage += "No valid location! Please enter valid path.\n";
    		}
    	}
    	
    	if (!annotationsLocationField.getText().equals("No Directory selected")) {
    		File dir = new File(annotationsLocationField.getText());
    		//If path isnt valid or path isnt directory
    		if(!(dir.exists() && dir.isDirectory())) {
    			errorMessage += "No valid location! Please enter valid path.\n";
    		}
    	}
    	
    	//If input is ok
    	if (errorMessage.length() == 0) {
    		return true;
    	}
    	else {
    		//Show error alert
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.initOwner(dialogStage);
    		alert.setTitle("Invalid Fields!");
    		alert.setHeaderText("Please correct invalid fields.");
    		alert.setContentText(errorMessage);
    		
    		alert.showAndWait();
    		
    		return false;
    	}
    }
}
