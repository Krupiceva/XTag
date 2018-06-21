package fer.hr.telegra.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fer.hr.telegra.model.*;
/**
 * Controller class for editing selected dataset
 * @author dmlinaric
 *
 */
public class DataSetEditDialogController {
	/**
	 * Textfiled with name of new dataset
	 */
	@FXML
	private TextField nameField;
	/**
	 * Textfiled with camera angle of new dataset
	 */
	@FXML
	private TextField cameraAngleField;
	/**
	 * Combobox with all possbile enums of weather conditions in new dataset
	 */
	@FXML
	private ComboBox<WeatherConditions> weatherConditionCombo = new ComboBox<>();
	/**
	 * Textfiled with camera high of new datset
	 */
	@FXML
	private TextField cameraHighField;
	/**
	 * Combobox with all possible image quality in new dataset
	 */
	@FXML
	private ComboBox<ImageQuality> imageQualityCombo = new ComboBox<>();
	/**
	 * Combobox with all possible times of the day in images in new dataset
	 */
	@FXML
	private ComboBox<TimeOfTheDay> timeOfTheDayCombo = new ComboBox<>();
	/**
	 * Textfiled with path to folder with images in dataset
	 */
	@FXML
	private TextField imagesLocationField;
	/**
	 * Textfield with path to folder with annotations in dataset
	 */
	@FXML
	private TextField annotationsLocationField;
	/**
	 * Stage of this dialog window
	 */
	private Stage dialogStage;
	/**
	 * Dataset that is selected to be edit
	 */
	private DataSet dataSet;
	private boolean okClicked = false;
	/**
	 * Instance of singleton class that held information of last used path in application
	 */
	PathData pathData = PathData.getInstance();
	
	/**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     * In combobox put all enums for weathercondition and imagequality
     */
    @FXML
    private void initialize() {
    	weatherConditionCombo.getItems().setAll(WeatherConditions.values());
    	imageQualityCombo.getItems().setAll(ImageQuality.values());
    	timeOfTheDayCombo.getItems().setAll(TimeOfTheDay.values());
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
     * Populate information with dataset data
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
    	timeOfTheDayCombo.setValue(dataSet.getTimeOfTheDay());
    }
    
    /**
     * Returns true if the user clicked OK, false otherwise.
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }
    
    /**
     * Method which is called when user clicks ok.
     * If input is valid put new information to dataset
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
    		dataSet.setTimeOfTheDay(timeOfTheDayCombo.getValue());	
    		okClicked = true;
    		dialogStage.close();
    	}
    }
    
    /**
     * Method which is called when user clicks cancel.
     * Close dialog window
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
    	File file = new File(pathData.path);
    	if(file.isDirectory()) {
    		directoryChooser.setInitialDirectory(file);
    	}
    	File selectedDirectory = 
                directoryChooser.showDialog(dialogStage);
    	//LastPath.setLastFilePath(selectedDirectory);
         
        if(selectedDirectory == null){
            //locationField.setText("No Directory selected");
        }else{
            imagesLocationField.setText(selectedDirectory.getAbsolutePath());
            pathData.path = selectedDirectory.getAbsolutePath();
            try {
    			BufferedWriter writer = new BufferedWriter(new FileWriter("config/path_data.txt"));
    			writer.write(pathData.path);
    		    writer.close();
    		} catch(IOException e) {
    			e.printStackTrace();
    		}
        }
    }
    
    /**
     * Method which is called when user clicks browse for annotations location.
     */
    @FXML
    private void handleBrowse1() {
    	DirectoryChooser directoryChooser = new DirectoryChooser();
    	File file = new File(pathData.path);
    	if(file.isDirectory()) {
    		directoryChooser.setInitialDirectory(file);
    	}
    	File selectedDirectory = 
                directoryChooser.showDialog(dialogStage);
    	//LastPath.setLastFilePath(selectedDirectory);
         
        if(selectedDirectory == null){
            //locationField.setText("No Directory selected");
        }else{
            annotationsLocationField.setText(selectedDirectory.getAbsolutePath());
            pathData.path = selectedDirectory.getAbsolutePath();
            try {
    			BufferedWriter writer = new BufferedWriter(new FileWriter("config/path_data.txt"));
    			writer.write(pathData.path);
    		    writer.close();
    		} catch(IOException e) {
    			e.printStackTrace();
    		}
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
