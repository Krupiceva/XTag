package fer.hr.telegra.view;

import java.io.File;

import fer.hr.telegra.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 *
 */
public class RootLayoutController {

	// Reference to the main application
    private MainApp mainApp;

    @FXML
    private void initialize() {
    	
    }
    
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    /**
     * Creates an empty database of datasets
     */
    @FXML
    private void handleNewDataBase() {
    	mainApp.getDataSets().clear();
    	MainApp.setLastFilePath(null);
    }
    
    /**
     * Opens a filechooser to let user select database to load
     */
    @FXML
    private void handleLoadDataBase() {
    	FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        
        // Show open file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        
        if (file != null) {
            mainApp.loadDatasetsFromFile(file);
        } 
    }
    
    /**
     * Saves file to the currently open database. If there is no open database, the save as dialog is shown
     */
    @FXML
    private void handleSaveDataBase() {
    	File file = MainApp.getLastFilePath();
    	if(file != null) {
    		mainApp.saveDataSetsToFile(file);
    	}
    	else {
    		handleSaveDataBaseAs();
    	}
    }
    
    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveDataBaseAs() {
    	FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        
        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
        
        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.saveDataSetsToFile(file);
        }
    }
    
    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Xtag");
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setHeaderText("About");
        alert.setContentText("Author: Dora Mlinariæ\nVersion: 2.0.1\n");
        Image appIcon = new Image(getClass().getResourceAsStream("/Apps-xorg-icon.png"));
        alert.setGraphic(new ImageView(appIcon));
        

        alert.showAndWait();
    }
    
    /**
     * Opens dialog for export frames from avi video
     */
    @FXML
    private void handleExportFrames() {
    	mainApp.showExportFramesDialog();
    }
    
    /**
     * Opens annotations config dialog
     */
    @FXML
    private void handleConfigAnnotations() {
    	mainApp.showConfigAnnotations();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
    
    
}
