package fer.hr.telegra.view;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fer.hr.telegra.MainApp;
import fer.hr.telegra.model.DataSet;
import fer.hr.telegra.model.ImageQuality;
import fer.hr.telegra.model.WeatherConditions;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ButtonType;

public class DataSetsOverviewController {
	@FXML
	private TableView<DataSet> dataSetTable;
	@FXML
	private TableColumn<DataSet, String> dataSetNameColumn;
	@FXML
	private TableColumn<DataSet, Integer> numOfImgColumn;
	@FXML
	private TextField filterField;
	@FXML
	private Label dataSetNameLabel;
	@FXML
	private Label dataSetImagesLocationLabel;
	@FXML
	private Label dataSetAnnotationsLocationLabel;
	@FXML
	private Label numOfImgLabel;
	@FXML
	private Label cameraAngleLabel;
	@FXML
	private Label cameraHighLabel;
	@FXML
	private Label weatherConditionLabel;
	@FXML
	private Label imageQualityLabel;
	@FXML
	private SplitPane splitPaneHor;
	@FXML
	private Button openButton;
	//@FXML
	//private Button statButton;
	@FXML
	private Button addDataSet;
	@FXML
	private Button editDataSet;
	@FXML
	private Button deleteDataSet;
	@FXML
	private Label searchLabel;
	@FXML
    private BarChart<String, Integer> barChart;

    @FXML
    private CategoryAxis xAxis;
    
    Map<String, Integer> stat = new HashMap<String, Integer>();
	
	// Reference to the main application.
    private MainApp mainApp;
    
    public DataSetsOverviewController() {
    }
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	splitPaneHor.setDividerPositions(0.15); 
    	dataSetNameColumn.setCellValueFactory(cellData -> cellData.getValue().dataSetNameProperty());
    	numOfImgColumn.setCellValueFactory(cellData -> cellData.getValue().numOfImgInDataSetProperty().asObject());
    	
    	//Add icons to buttons and labels
    	Image imageOpen = new Image(getClass().getResourceAsStream("/Actions-project-open-icon.png"));
    	//Image imageStat = new Image(getClass().getResourceAsStream("/Actions-office-chart-bar-icon.png"));
    	Image imageSearch = new Image(getClass().getResourceAsStream("/Search-icon.png"));
    	Image imageAdd = new Image(getClass().getResourceAsStream("/Actions-document-new-icon.png"));
    	Image imageEdit = new Image(getClass().getResourceAsStream("/Actions-document-edit-icon.png"));
    	Image imageDelete = new Image(getClass().getResourceAsStream("/Actions-document-close-icon.png"));
    	openButton.setGraphic(new ImageView(imageOpen));
    	//statButton.setGraphic(new ImageView(imageStat));
    	searchLabel.setGraphic(new ImageView(imageSearch));
    	addDataSet.setGraphic(new ImageView(imageAdd));
    	editDataSet.setGraphic(new ImageView(imageEdit));
    	deleteDataSet.setGraphic(new ImageView(imageDelete));
    	
    	//clear dataset details
    	showDataSetDetails(null);
    	
    	//Listen for change and show selected dataset
    	dataSetTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showDataSetDetails(newValue));
    }
    
    /**
     * Is called by the main application to give a reference back to itself.
     * Observable list of datasets is in mainapp so we wrap observable list in filteredlist
     * and sorted list here 
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
      //Wrap observablelist to filteredlist
    	FilteredList<DataSet> filteredData = new FilteredList<>(mainApp.getDataSets());
    	
    	//Set the filter predicate whenever the filter change
    	filterField.textProperty().addListener((observable, oldValue, newValue) -> {
    		filteredData.setPredicate(dataSet -> {
    			//If filter empty display all datasets
    			if (newValue == null || newValue.isEmpty()) {
    				return true;
    			}
    			//Compare name of datasets with filter
    			String lowerCaseFilter = newValue.toLowerCase();
    			if(dataSet.getDataSetName().toLowerCase().contains(lowerCaseFilter)) {
    				return true;
    			}
    			return false;
    		});
    	});
    	
    	//Wrap filteredlist to sortedlist
    	SortedList<DataSet> sortedData = new SortedList<>(filteredData);
    	
    	//Bind the sortedlist comparator to the tableview comparator
    	sortedData.comparatorProperty().bind(dataSetTable.comparatorProperty());
    	
    	dataSetTable.setItems(sortedData);
    	
    	/**
    	xAxis.setCategories(mainApp.getAnnotations());
     	for(String ant: mainApp.getAnnotations()) {
     		stat.put(ant, 0);
     	}
     	*/

    }
    
    private void showDataSetDetails(DataSet dataSet) {
    	if(dataSet != null) {
    		dataSetNameLabel.setText(dataSet.getDataSetName());
    		dataSetImagesLocationLabel.setText(dataSet.getDataSetImagesLocation());
    		dataSetAnnotationsLocationLabel.setText(dataSet.getDataSetAnnotationsLocation());
    		numOfImgLabel.setText(Integer.toString(dataSet.getNumOfImgInDataSet()));
    		cameraAngleLabel.setText(Double.toString(dataSet.getCameraAngle()));
    		cameraHighLabel.setText(Double.toString(dataSet.getCameraHigh()));
    		weatherConditionLabel.setText(WeatherConditions.printWeatherConditions(dataSet.getWeatherCondition()));
    		imageQualityLabel.setText(ImageQuality.printImageQuality(dataSet.getImageQuality()));
    		
    	}
    	else {
    		dataSetNameLabel.setText("");
    		dataSetImagesLocationLabel.setText("");
    		dataSetAnnotationsLocationLabel.setText("");
    		numOfImgLabel.setText("");
    		cameraAngleLabel.setText("");
    		cameraHighLabel.setText("");
    		weatherConditionLabel.setText("");
    		imageQualityLabel.setText("");
    	}
    }
    
    /**
     * Method which is called when user clicks on the delete button.
     */
    @FXML
    private void handleDeleteDataSet() {
    	DataSet selectedDataSet = dataSetTable.getSelectionModel().getSelectedItem();
    	if (selectedDataSet != null) {
    		Alert alert = new Alert(AlertType.CONFIRMATION);
    		alert.initOwner(mainApp.getPrimaryStage());
    		alert.setTitle("Deleting!");
    		alert.setHeaderText(null);
    		alert.setContentText("You are going to delete selected dataset. Are you sure?");
    		
    		Optional<ButtonType> result = alert.showAndWait();
    		if (result.get() == ButtonType.OK) {
    			
    			mainApp.getDataSets().remove(selectedDataSet);
    		}
    		else {
    			alert.close();
    		}
    		
    		
    	}
    	else {
    		//Table is empty
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.initOwner(mainApp.getPrimaryStage());
    		alert.setTitle("Warning");
    		alert.setHeaderText("No Dataset selected");
    		alert.setContentText("Please select dataset in the table!");
    		
    		alert.showAndWait();
    	}
    	
    }
    
    /**
     * Method which is called when user clicks the new button. Open dialog to edit details for new dataset
     */
    @FXML
    private void handleNewDataSet() {
    	DataSet tempDataSet = new DataSet();
    	boolean okClicked = mainApp.showDataSetAddDialog(tempDataSet);
    	if(okClicked) {
    		mainApp.getDataSets().add(tempDataSet);
    	}
    }
    
    /**
     * Method which is called when user clicks edit button. Open dialog to edit details for selected dataset
     */
    @FXML
    private void handleEditDataSet() {
    	DataSet selectedDataSet = dataSetTable.getSelectionModel().getSelectedItem();
    	if(selectedDataSet != null) {
    		boolean okClicked = mainApp.showDataSetEditDialog(selectedDataSet);
    		if (okClicked) {
    			showDataSetDetails(selectedDataSet);
    		}
    	}
    	else {
    		// Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Warning");
            alert.setHeaderText("No Dataset Selected");
            alert.setContentText("Please select dataset in the table!");

            alert.showAndWait();
    		
    	}
    }
    /**
     * Method which is called when user clicks open button. Inside rootlayout puts opendatasetoverview 
     * give controller selected dataset
     */
    @FXML
    private void handleOpen() {
    	DataSet selectedDataSet = dataSetTable.getSelectionModel().getSelectedItem();
    	if(selectedDataSet != null) {
    		if(!selectedDataSet.getDataSetImagesLocation().equals("No Directory selected") || !selectedDataSet.getDataSetAnnotationsLocation().equals("No Directory selected")) {
    			mainApp.showOpenDataSetOverview(selectedDataSet);
    		}
    		else {
    			//Selected dataset doesn't have location
    			Alert alert = new Alert(AlertType.WARNING);
    			alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("Warning");
                alert.setHeaderText("No Location of Dataset");
                alert.setContentText("Please set the locations to selected dataset!");

                alert.showAndWait();
    		}
    	}
    	else {
    		// Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Warning");
            alert.setHeaderText("No Dataset Selected");
            alert.setContentText("Please select dataset in the table!");

            alert.showAndWait();
    		
    	}
    }
    
    @FXML
    private void handleStatistics() {
    	DataSet selectedDataSet = dataSetTable.getSelectionModel().getSelectedItem();
    	if(selectedDataSet != null) {
    		mainApp.showAnnotationsStatistics(selectedDataSet);
    	}
    	else {
    		// Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Warning");
            alert.setHeaderText("No Dataset Selected");
            alert.setContentText("Please select dataset in the table!");

            alert.showAndWait();
    		
    	}
    	
    }
    
    
    // array of supported extensions (use a List if you prefer)
       static final String[] EXTENSIONS = new String[]{
           "xml" // and other formats you need
       };
       // filter to identify images based on their extensions
       static final FilenameFilter XML_FILTER = new FilenameFilter() {

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
}
