package fer.hr.telegra.view;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Optional;
import fer.hr.telegra.MainApp;
import fer.hr.telegra.model.DataSet;
import fer.hr.telegra.model.ImageQuality;
import fer.hr.telegra.model.TimeOfTheDay;
import fer.hr.telegra.model.WeatherConditions;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.control.ButtonType;
/**
 * Controller class that shows overview of selected dataset and table of all datasets in database
 * @author dmlinaric
 *
 */
public class DataSetsOverviewController {
	/**
	 * Table with all dataset in database of application
	 */
	@FXML
	private TableView<DataSet> dataSetTable;
	/**
	 * Column of table with name of dataset
	 */
	@FXML
	private TableColumn<DataSet, String> dataSetNameColumn;
	/**
	 * Column of table with number of images in dataset
	 */
	@FXML
	private TableColumn<DataSet, Integer> numOfImgColumn;
	/**
	 * Textfield for searching the dataset in table
	 */
	@FXML
	private TextField filterField;
	/**
	 * Label with name of dataset
	 */
	@FXML
	private Label dataSetNameLabel;
	/**
	 * Label with path to the folder with images in dataset
	 */
	@FXML
	private Label dataSetImagesLocationLabel;
	/**
	 * Label with path to the folder with annotations in dataset
	 */
	@FXML
	private Label dataSetAnnotationsLocationLabel;
	/**
	 * Label with number of images in dataset
	 */
	@FXML
	private Label numOfImgLabel;
	/**
	 * Label with camera angle of dataset
	 */
	@FXML
	private Label cameraAngleLabel;
	/**
	 * Label with camera high of dataset
	 */
	@FXML
	private Label cameraHighLabel;
	/**
	 * Label with weather condition on images in dataset
	 */
	@FXML
	private Label weatherConditionLabel;
	/**
	 * Label with quality of images in dataset
	 */
	@FXML
	private Label imageQualityLabel;
	/**
	 * Label with time of the ay on images in dataset
	 */
	@FXML
	private Label timeOfTheDayLabel;
	@FXML
	private SplitPane splitPaneHor;
	/**
	 * Button for open selected dataset
	 */
	@FXML
	private Button openButton;
	/**
	 * Button for adding new dataset
	 */
	@FXML
	private Button addDataSet;
	/**
	 * Button for edit selected dataset
	 */
	@FXML
	private Button editDataSet;
	/**
	 * Button for delete selected dataset
	 */
	@FXML
	private Button deleteDataSet;
	/**
	 * Label that contains text "search"
	 */
	@FXML
	private Label searchLabel;
	/**
	 * Label that shows how many annotations are in the dataset
	 */
	@FXML
	private Label numberOfAnnotationsInSetLabel;
	/**
	 * Variable with number of annotations in dataset which is display on numberOfAnnotationsInSetLabel
	 */
	private Integer numberOfAnnotationsInSet;
	/**
	 * Bar chart for statistics of how many annotations classes are in dataset
	 */
	@FXML
    private BarChart<String, Integer> barChart;
    @FXML
    private CategoryAxis xAxis;
    /**
     * List of all possible annotations classes for bar chart
     */
    private ObservableList<String> annotationsName = FXCollections.observableArrayList();
	/**
	 * Reference to the mainApp
	 */
    private MainApp mainApp;
    
    public DataSetsOverviewController() {
    }
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     * Put images to the buttons
     * Define event handlers
     */
    @FXML
    private void initialize() {
    	splitPaneHor.setDividerPositions(0.15); 
    	dataSetNameColumn.setCellValueFactory(cellData -> cellData.getValue().dataSetNameProperty());
    	numOfImgColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDataSetImages().size() + cellData.getValue().getDataSetImagesWithAnnotations().size() + cellData.getValue().getDataSetVerifiedImages().size()).asObject());
    	
    	//Add icons to buttons and labels
    	Image imageOpen = new Image(getClass().getResourceAsStream("/Actions-project-open-icon.png"));
    	Image imageSearch = new Image(getClass().getResourceAsStream("/Search-icon.png"));
    	Image imageAdd = new Image(getClass().getResourceAsStream("/Actions-document-new-icon.png"));
    	Image imageEdit = new Image(getClass().getResourceAsStream("/Actions-document-edit-icon.png"));
    	Image imageDelete = new Image(getClass().getResourceAsStream("/Actions-document-close-icon.png"));
    	openButton.setGraphic(new ImageView(imageOpen));
    	searchLabel.setGraphic(new ImageView(imageSearch));
    	addDataSet.setGraphic(new ImageView(imageAdd));
    	editDataSet.setGraphic(new ImageView(imageEdit));
    	deleteDataSet.setGraphic(new ImageView(imageDelete));
    	
    	//clear dataset details
    	showDataSetDetails(null);
    	
    	//Open selected dataset on double click
    	dataSetTable.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
    		if(event.getClickCount() == 2) {
     		   handleOpen();
     	   }
    	});
    	
    	
    	//Listen for change and show selected dataset
    	dataSetTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showDataSetDetails(newValue));
    	
    	barChart.setAnimated(false);
    	barChart.setLegendVisible(false);
    	
    }
    
    /**
     * Is called by the main application to give a reference back to itself.
     * Observable list of datasets is in mainapp so we wrap observable list in filteredlist
     * and sorted list here 
     * @param mainApp reference ot the mainApp
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
    }
    
    /**
     * Show information and bar chart for selected dataset
     * @param dataSet is selected dataset
     */
    private void showDataSetDetails(DataSet dataSet) {
    	if(dataSet != null) {
    		dataSetNameLabel.setText(dataSet.getDataSetName());
    		dataSetImagesLocationLabel.setText(dataSet.getDataSetImagesLocation());
    		dataSetAnnotationsLocationLabel.setText(dataSet.getDataSetAnnotationsLocation());
    		numOfImgLabel.setText(Integer.toString(dataSet.getDataSetImages().size() + dataSet.getDataSetImagesWithAnnotations().size() + dataSet.getDataSetVerifiedImages().size()));
    		cameraAngleLabel.setText(Double.toString(dataSet.getCameraAngle()));
    		cameraHighLabel.setText(Double.toString(dataSet.getCameraHigh()));
    		weatherConditionLabel.setText(WeatherConditions.printWeatherConditions(dataSet.getWeatherCondition()));
    		imageQualityLabel.setText(ImageQuality.printImageQuality(dataSet.getImageQuality()));
    		timeOfTheDayLabel.setText(TimeOfTheDay.printTimeOfTheDay(dataSet.getTimeOfTheDay()));
    		
    		barChart.getData().clear();
    		annotationsName.clear();
    		XYChart.Series<String, Integer> series = new XYChart.Series<>();
    		numberOfAnnotationsInSet = 0;
    		dataSet.getAnnotations().forEach((k,v) -> {
    			annotationsName.add(k);
    			numberOfAnnotationsInSet = numberOfAnnotationsInSet + v;
    			final XYChart.Data<String, Integer> data = new Data<String, Integer>(k, v);
//    			 data.nodeProperty().addListener(new ChangeListener<Node>() {
//    			        @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
//    			          if (node != null) {
//    			            //setNodeStyle(data);
//    			            displayLabelForData(data);
//    			          } 
//    			        }
//    			      });
    			 series.getData().add(data);
    		});
    		
    		xAxis.setCategories(annotationsName);
    		barChart.getData().add(series);

    		/**
    		 * Iterate trough all annotation classes on xAxis and for every color every node with color of class that represent
    		 */
			for (int i = 0; i < annotationsName.size(); i++) {
				String lookup = ".data" + i + ".chart-bar";
				for (Node n : barChart.lookupAll(lookup)) {
					String color ="";
					if (mainApp.getAnnotations().contains(annotationsName.get(i))) {
						color = mainApp.getColorOfClasses().get(annotationsName.get(i)).get().toString();
					}
					else {
						color = mainApp.getDefaultColor().toString();
					}
					color = color.substring(2);
					color = "#" + color;
					//color = color.substring(0, color.length() - 2);
					String style = "-fx-bar-fill: " + color + ";";
					n.setStyle(style);
				}
			}
    		numberOfAnnotationsInSetLabel.setText(numberOfAnnotationsInSet.toString());
    		
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
    		timeOfTheDayLabel.setText("");
    		
    		numberOfAnnotationsInSet = 0;
    		barChart.getData().clear();
    		
    		numberOfAnnotationsInSetLabel.setText("");
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
    			File file = MainApp.getLastFilePath();
    	        mainApp.saveDataSetsToFile(file);
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
//    	boolean okClicked = mainApp.showDataSetAddDialog(tempDataSet);
//    	if(okClicked) {
//    		mainApp.getDataSets().add(tempDataSet);
//    		File file = MainApp.getLastFilePath();
//            mainApp.saveDataSetsToFile(file);
//    	}
    	mainApp.showChooseDataSetAddDialog(tempDataSet);
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
    			File file = MainApp.getLastFilePath();
    	        mainApp.saveDataSetsToFile(file);
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
       
       
       @SuppressWarnings("unused")
	private void displayLabelForData(XYChart.Data<String, Integer> data) {
    	   final Node node = data.getNode();
    	   final Text dataText = new Text(data.getYValue() + "");
    	   node.parentProperty().addListener(new ChangeListener<Parent>() {
    	     @Override public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
    	       Group parentGroup = (Group) parent;
    	       parentGroup.getChildren().add(dataText);
    	     }
    	   });

    	   node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
    	     @Override public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
    	       dataText.setLayoutX(
    	         Math.round(
    	           bounds.getMinX() + bounds.getWidth() / 2 - dataText.prefWidth(-1) / 2
    	         )
    	       );
    	       dataText.setLayoutY(
    	         Math.round(
    	           bounds.getMinY() - dataText.prefHeight(-1) * 0.5
    	         )
    	       );
    	     }
    	   });
    	 }
       
       
}
