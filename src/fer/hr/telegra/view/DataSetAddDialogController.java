package fer.hr.telegra.view;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.magicwerk.brownies.collections.BigList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import fer.hr.telegra.model.*;
/**
 * Controller class for adding new dataset to the database of application
 * @author dmlinaric
 *
 */
public class DataSetAddDialogController {
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
	 * Progress bar that shows percent of importing images and annotations to database
	 */
	@FXML
	private ProgressBar progressBar;
	/**
	 * Progress indicator that shows percent of importing images and annotations to database
	 */
	@FXML
	private ProgressIndicator progressIndicator;
	/**
	 * Label that shows which image is current importing
	 */
	@FXML
	private Label statusLabel;
	/**
	 * Button for importing new dataset
	 */
	@FXML
	private Button importBtn;
	/**
	 * Button for canceling import
	 */
	@FXML
	private Button cancel;
	/**
	 * Button when import is finished
	 */
	@FXML
	private Button ok;
	/**
	 * Stage of this dialog window
	 */
	private Stage dialogStage;
	/**
	 * reference to the new added dataset
	 */
	private DataSet dataSet;
	private boolean okClicked = false;
	/**
	 * Importtask for importing images that will be executed in separate theard
	 */
	ImportTask importTask;
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
    	ok.setDisable(true);
    	cancel.setDisable(true);
    }

    /**
     * Sets the stage of this dialog.
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.setResizable(false);
        this.dialogStage.setOnCloseRequest(event -> {
        	okClicked = false;
        });
    }
    
    
    /**
     * Sets the dataset to be added in the dialog
     * @param dataSet is new dataset
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
     * Method which is called when user clicks import.
     * Initialize components
     * Bind progress bar with import task progress
     * Put import task in separate thread
     */
    @FXML
    private void handleImport() {
    	if (isInputValid()) {
    		importBtn.setDisable(true);
    		ok.setDisable(true);
    		cancel.setDisable(false);
    		progressBar.setProgress(0);
            progressIndicator.setProgress(0);
            cancel.setDisable(false);
			importTask = new ImportTask();
			progressBar.progressProperty().unbind();
			progressBar.progressProperty().bind(importTask.progressProperty());
			progressIndicator.progressProperty().unbind();
			progressIndicator.progressProperty().bind(importTask.progressProperty());
			statusLabel.textProperty().unbind();
			statusLabel.textProperty().bind(importTask.messageProperty());
			
			importTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                    new EventHandler<WorkerStateEvent>() {

                        @Override
                        public void handle(WorkerStateEvent t) {
                            statusLabel.textProperty().unbind();
                            statusLabel.setText("Imported!");
                            ok.setDisable(false);
                            okClicked = true;
                        }
                    });
			 new Thread(importTask).start();
    	}
    }
    
    /**
     * Method which is called when user clicks cancel.
     * Cancel import task in thread
     * Unbind progress bar with import task progress
     */
    @FXML
    private void handleCancel() {
    	okClicked = false;
    	importBtn.setDisable(false);
    	ok.setDisable(true);
    	cancel.setDisable(true);
    	importTask.cancel(true);
    	progressBar.progressProperty().unbind();
        progressIndicator.progressProperty().unbind();
        statusLabel.textProperty().unbind();
        progressBar.setProgress(0);
        progressIndicator.setProgress(0);
    }
    
    /**
     * Method which is called when user clicks ok.
     * It close this dialog window
     */
    @FXML
    private void handleOK() {
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
    
    // array of supported extensions (use a List if you prefer)
    static final String[] EXTENSIONS = new String[]{
        "gif", "png", "bmp", "jpg", "jpeg" // and other formats you need
    };
    // filter to identify images based on their extensions
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };
    
    /**
     * Inner class that represent task of importing new dataset in database
     * @author dmlinaric
     *
     */
	private class ImportTask extends Task<DataSet> {
		@Override
		protected DataSet call() throws Exception {
			DataSet data = new DataSet();
			dataSet.setDataSetName(nameField.getText());
			dataSet.setDataSetImagesLocation(imagesLocationField.getText());
			dataSet.setDataSetAnnotationsLocation(annotationsLocationField.getText());
			dataSet.setCameraAngle(Double.parseDouble(cameraAngleField.getText()));
			dataSet.setCameraHigh(Double.parseDouble(cameraHighField.getText()));
			dataSet.setWeatherCondition(weatherConditionCombo.getValue());
			dataSet.setImageQuality(imageQualityCombo.getValue());
			dataSet.setTimeOfTheDay(timeOfTheDayCombo.getValue());

			File dir = new File(imagesLocationField.getText());
			// File[] files = dir.listFiles();
			/**
			 * Iterate trough all images in folder and check if exist xml file for that image
			 * If exist parse xml and add all annotation for that image and image to dataset
			 * Otherwise add only image
			 */
			BigList<File> images = BigList.create(dir.listFiles(IMAGE_FILTER));
			Iterator<File> itr = images.iterator();
			int i = 0;
			while (itr.hasNext()) {
				File img = itr.next();
				this.importImg(img);
				String path = dataSet.getDataSetAnnotationsLocation() + "\\" + img.getName();
				if (path.contains(".")) {
					path = path.substring(0, path.lastIndexOf('.'));
				}
				path = path + ".xml";
				File file = new File(path);
				DataImage dataImg = new DataImage(img.getName());
				if (file.exists()) {
					try {
						DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
						Document doc = dBuilder.parse(file);
						doc.getDocumentElement().normalize();
						String ver = doc.getDocumentElement().getAttribute("verified");
						if (ver.equals("yes")) {
							dataSet.addDataSetVerifiedImage(dataImg);
						} else {
							dataSet.addDataSetImageWithAnnotations(dataImg);
						}
						
						NodeList nList = doc.getElementsByTagName("object");
						for (int j = 0; j < nList.getLength(); j++) {
							Node nNode = nList.item(j);
							if (nNode.getNodeType() == Node.ELEMENT_NODE) {
								Element eElement = (Element) nNode;
								String name = eElement.getElementsByTagName("name").item(0).getTextContent().trim();
								if(dataSet.getAnnotations().containsKey(name)) {
									int num = dataSet.getAnnotations().get(name);
									num = num + 1;
									dataSet.getAnnotations().put(name, num);
								}
								else {
									dataSet.getAnnotations().put(name, 1);
								}
							}
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					dataSet.addDataSetImage(dataImg);
				}
				i++;
				this.updateProgress(i, images.size());
			}
			return data;

		}

		private void importImg(File file) throws Exception {
			this.updateMessage("Importing: " + file.getName());
		}

	}
	
}
