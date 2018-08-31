package fer.hr.telegra.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fer.hr.telegra.MainApp;
import fer.hr.telegra.model.DataSet;
import fer.hr.telegra.model.ImageQuality;
import fer.hr.telegra.model.NVRStream;
import fer.hr.telegra.model.PathData;
import fer.hr.telegra.model.TimeOfTheDay;
import fer.hr.telegra.model.WeatherConditions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * Controller class for adding new dataset from NVR to the database of application
 * @author dmlinaric
 *
 */
public class DataSetAddFromNVRDialogController {
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
	 * TableView with streams from NVR
	 */
	@FXML
	private TableView<NVRStream> streamTable;
	/**
	 * Column of table with stream address
	 */
	@FXML
	private TableColumn<NVRStream, String> streamAddress;
	/**
	 * Column of table with timestamp start
	 */
	@FXML
	private TableColumn<NVRStream, String> streamStart;
	/**
	 * Column of table with timestamp end
	 */
	@FXML
	private TableColumn<NVRStream, String> streamEnd;
	
	/**
	 * Stage of this dialog window
	 */
	private Stage dialogStage;
	/**
	 * reference to the new added dataset
	 */
	private DataSet dataSet;
	/**
	 * Reference to the mainApp
	 */
    private MainApp mainApp;
    /**
     * Flag that shows if user is clicked ok
     */
	private boolean okClicked = false;
	/**
	 * Instance of singleton class that held information of last used path in application
	 */
	PathData pathData = PathData.getInstance();
	/**
	 * Observable list of streams for new dataset
	 */
	private ObservableList<NVRStream> streams = FXCollections.observableArrayList();
	
	/**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     * In combobox put all enums for weathercondition and imagequality
     * Initialize tableview with streams and enable edit mode
     */
	@FXML
    private void initialize() {
		weatherConditionCombo.getItems().setAll(WeatherConditions.values());
    	imageQualityCombo.getItems().setAll(ImageQuality.values());
    	timeOfTheDayCombo.getItems().setAll(TimeOfTheDay.values());
    	
    	
    	streamTable.setItems(streams);
    	streamTable.setEditable(true);
    	streamAddress.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
    	streamStart.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());
    	streamEnd.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());
    	
    	streamAddress.setCellFactory(TextFieldTableCell.forTableColumn());
    	streamAddress.setOnEditCommit(
    	    new EventHandler<CellEditEvent<NVRStream, String>>() {
    	        @Override
    	        public void handle(CellEditEvent<NVRStream, String> t) {
    	            ((NVRStream) t.getTableView().getItems().get(
    	                t.getTablePosition().getRow())
    	                ).setAddress(t.getNewValue());
    	        }
    	    }
    	);
    	
    	streamStart.setCellFactory(TextFieldTableCell.forTableColumn());
    	streamStart.setOnEditCommit(
    	    new EventHandler<CellEditEvent<NVRStream, String>>() {
    	        @Override
    	        public void handle(CellEditEvent<NVRStream, String> t) {
    	            ((NVRStream) t.getTableView().getItems().get(
    	                t.getTablePosition().getRow())
    	                ).setStartTime(t.getNewValue());
    	        }
    	    }
    	);
    	
    	streamEnd.setCellFactory(TextFieldTableCell.forTableColumn());
    	streamEnd.setOnEditCommit(
    	    new EventHandler<CellEditEvent<NVRStream, String>>() {
    	        @Override
    	        public void handle(CellEditEvent<NVRStream, String> t) {
    	            ((NVRStream) t.getTableView().getItems().get(
    	                t.getTablePosition().getRow())
    	                ).setEndTime(t.getNewValue());
    	        }
    	    }
    	);
	}
	
	/**
     * Sets the stage of this dialog.
     * Add keyboard shortcuts to the scene
     * @param dialogStage
     */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
		this.dialogStage.setResizable(false);
		this.dialogStage.setOnCloseRequest(event -> {
        	okClicked = false;
        });
		
		this.dialogStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
            	if (event.getCode() == KeyCode.DELETE) {
                	if(streamTable.getSelectionModel().getSelectedItem() != null) {
                		streams.remove(streamTable.getSelectionModel().getSelectedItem());
                	}
                }
            }
		});
	}
	
	/**
	 * Sets the dataset to be added in dialog
	 * @param dataSet is new dataset
	 */
	public void setDataSet(DataSet dataSet) {
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
	 * Sets the reference to the mainapp
	 * @param mainApp is reference to the main app
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	 /**
     * Returns true if the user clicked OK, false otherwise.
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }
    
    /**
     * Close the dialog when user clicks cancel
     */
    @FXML
    private void handleCancel() {
    	okClicked = false;
    	dialogStage.close();
    }
    
    /**
     * If input is valid sets the new informations for dataset
     */
    @FXML
    private void handleImport() {
    	if(isInputValid()) {
    		dataSet.setDataSetName(nameField.getText());
			dataSet.setDataSetImagesLocation(imagesLocationField.getText());
			dataSet.setDataSetAnnotationsLocation(annotationsLocationField.getText());
			dataSet.setCameraAngle(Double.parseDouble(cameraAngleField.getText()));
			dataSet.setCameraHigh(Double.parseDouble(cameraHighField.getText()));
			dataSet.setWeatherCondition(weatherConditionCombo.getValue());
			dataSet.setImageQuality(imageQualityCombo.getValue());
			dataSet.setTimeOfTheDay(timeOfTheDayCombo.getValue());
			dataSet.setStreams(streams);
			okClicked = true;
			this.dialogStage.close();
    	}
    }
    
    /**
     * Open new dialog to adding new stream to the dataset
     * If user click add then add new stream to the list of streams
     */
    @FXML
    private void handleAddNewStream() {
    	String stream = mainApp.showAddNewStreamDialog();
    	if(stream != null) {
    		String[] temp = stream.split(";");
    		NVRStream NVRStream = new NVRStream(temp[0], temp[1], temp[2]);
    		streams.add(NVRStream);	
    	}
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
    	
    	if (!(imagesLocationField.getText() == null)) {
    		File dir = new File(imagesLocationField.getText());
    		//If path isnt valid or path isnt directory
    		if(!(dir.exists() && dir.isDirectory())) {
    			errorMessage += "No valid location! Please enter valid path.\n";
    		}
    	}
    	else {
    		errorMessage += "No valid location! Please enter valid path.\n";
    	}
    	
    	if (!(annotationsLocationField.getText() == null)) {
    		File dir = new File(annotationsLocationField.getText());
    		//If path isnt valid or path isnt directory
    		if(!(dir.exists() && dir.isDirectory())) {
    			errorMessage += "No valid location! Please enter valid path.\n";
    		}
    	}
    	else {
    		errorMessage += "No valid location! Please enter valid path.\n";
    	}
    	
    	if(streams.isEmpty()) {
    		errorMessage += "No valid stream! Please enter at least one stream.\n";
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
