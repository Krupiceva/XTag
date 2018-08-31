package fer.hr.telegra.view;

import java.io.File;

import fer.hr.telegra.MainApp;
import fer.hr.telegra.model.DataSet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller class for small dialog in which user can choose to import new dataset from disk or NVR
 * @author dmlinaric
 *
 */
public class ChooseDataSetAddDialogController {
	/**
	 * Button for adding dataset from disk
	 */
	@FXML
	private Button addFromDisk;
	/**
	 * Button for adding dataset from NVR
	 */
	@FXML
	private Button addFromNVR;
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
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
	@FXML
    private void initialize() {}
	
	/**
	 * Sets the reference to mainapp
	 * @param mainApp is reference to the mainapp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	/**
	 * Sets the stage of this dialog and disable resize
	 * @param dialogStage is stage of this dialog
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
		this.dialogStage.setResizable(false);
	}
	
	/**
	 * Sets the dataset to be added 
	 * @param dataSet is new dataset
	 */
	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}
	
	/**
	 * Method which is called  when user choose to import new dataset from disk
	 * It shows dialog for adding from disk
	 */
	@FXML
	private void handleAddFromDisk() {
    	boolean okClicked = mainApp.showDataSetAddDialog(dataSet);
    	if(okClicked) {
    		mainApp.getDataSets().add(dataSet);
    		File file = MainApp.getLastFilePath();
            mainApp.saveDataSetsToFile(file);
    	}
    	dialogStage.close();
	}
	
	/**
	 * Method which is called when user choose to import new dataset from NVR
	 * It shows dialog for adding from NVR
	 */
	@FXML
	private void handleAddFromNVR() {
		boolean okClicked = mainApp.showDataSetAddFromNVRDialog(dataSet);
    	if(okClicked) {
    		mainApp.getDataSets().add(dataSet);
    		File file = MainApp.getLastFilePath();
            mainApp.saveDataSetsToFile(file);
    	}
    	dialogStage.close();
	}
}
