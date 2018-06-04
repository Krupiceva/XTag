package fer.hr.telegra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


import fer.hr.telegra.model.DataSet;
import fer.hr.telegra.model.DataSetListWrapper;
import fer.hr.telegra.model.ResizableRectangle;
import fer.hr.telegra.model.ResizableRectangleWrapper;
import fer.hr.telegra.view.AnnotateDialogController;
import fer.hr.telegra.view.ConfigAnnotationsController;
import fer.hr.telegra.view.DataSetAddDialogController;
import fer.hr.telegra.view.DataSetEditDialogController;
import fer.hr.telegra.view.DataSetsOverviewController;
import fer.hr.telegra.view.EditAnnotationDialogController;
import fer.hr.telegra.view.ExportFramesDialogController;
import fer.hr.telegra.view.OpenDataSetOverviewController;
import fer.hr.telegra.view.RootLayoutController;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	private final String appVersion = "XTag v2.0.0 ";
	
	//Lists of datasets
	private ObservableList<DataSet> dataSets = FXCollections.observableArrayList();
	
	//List of annotations 
	private ObservableList<String> annotations = FXCollections.observableArrayList();
	
	//List of collors 
	HashMap<String, ObjectProperty<Color>> collors = new HashMap<String, ObjectProperty<Color>>();
	
	private ObservableList<String> colors = FXCollections.observableArrayList();
	
	//Map of colors of annotations
	HashMap<String, ObjectProperty<Color>> colorsOfClasses = new HashMap<String, ObjectProperty<Color>>();
	HashMap<String, ObjectProperty<Color>> colorsOfFlags = new HashMap<String, ObjectProperty<Color>>();
	public StringProperty defaultClass = new SimpleStringProperty("unknown");
	public Color defaultColor = Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.2);
	public Color defaultBorderColor = Color.WHITE;
	
	public OpenDataSetOverviewController openController;
	
	public MainApp() { 
		colorsOfFlags.put("Overlap", new SimpleObjectProperty<Color>(Color.RED));
		colorsOfFlags.put("Truncated", new SimpleObjectProperty<Color>(Color.RED));
		colorsOfFlags.put("Difficult", new SimpleObjectProperty<Color>(Color.RED));
		colorsOfFlags.put("Overlap&Truncated", new SimpleObjectProperty<Color>(Color.RED));
	}
	
	public ObservableList<DataSet> getDataSets() {
        return dataSets;
    }
	
	public ObservableList<String> getAnnotations(){
		return annotations;
	}
	
	public ObservableList<String> getColors(){
		return colors;
	}
	
	public HashMap<String, ObjectProperty<Color>> getCollors(){
		return collors;
	}

	public HashMap<String, ObjectProperty<Color>> getColorOfClasses(){
		return colorsOfClasses;
	}
	
	public void setDefaultClass(String name) {
		defaultClass.set(name);
	}
	
	public String getDefaultClass() {
		return defaultClass.get();
	}
	
	public StringProperty defaultClassProperty() {
		return defaultClass;
	}
	
	public void setDefaultColor(Color color) {
		defaultColor = color;
	}
	
	public Color getDefaultColor() {
		return defaultColor;
	}
	
	public HashMap<String, ObjectProperty<Color>> getColorsOfFlags(){
		return colorsOfFlags;
	}
	
	public Color getDefaultBorderColor() {
		return defaultBorderColor;
	}
	
	public void setDefaultBorderColor(Color color) {
		defaultBorderColor = color;
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(appVersion);
		Image appIcon = new Image(getClass().getResourceAsStream("/Apps-xorg-icon.png"));
		this.primaryStage.getIcons().add(appIcon);
		//this.primaryStage.initStyle(StageStyle.UNDECORATED);
		
		initAnnotations();
		initRootLayout();
		showDataSetsOverview();
		
	}
	
	/**
     * Initializes the root layout.
     */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			
			 // Show the scene containing the root layout.
			//TODO prepravit custom decorator i da bi implementirao svoj izgled prozora i funkcionalnosti
			//primaryStage.initStyle(StageStyle.UNDECORATED);
			//Scene scene = new Scene(new CustomDecorator(primaryStage, rootLayout));
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.setMaximized(true);
			primaryStage.setMinWidth(1280);
			primaryStage.setMinHeight(800);
			//primaryStage.setFullScreen(true);
			
			
			// Give the controller access to the main app.
	        RootLayoutController controller = loader.getController();
	        controller.setMainApp(this);
			
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	    File file = new File("data/database.xml");
	    file.exists();
	    if (file != null) {
	        loadDatasetsFromFile(file);
	    }
	    
	}
	
	/**
	 * Shows the dataset overview inside the root layout.
	 */
	
	public void showDataSetsOverview() {
		try {
			//Load datasetoverview from xml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/DataSetsOverview.fxml"));
			AnchorPane dataSetsOverview = (AnchorPane) loader.load();
			
			//Set dataseroverview into center of root layout.
			rootLayout.setCenter(dataSetsOverview);
			
			// Give the controller access to the main app.
	        DataSetsOverviewController controller = loader.getController();
	        controller.setMainApp(this);
	        
	        this.primaryStage.setTitle(appVersion);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showOpenDataSetOverview(DataSet dataSet) {
		try {
			//Load opendatasetoverview from xml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/OpenDataSetOverview.fxml"));
			AnchorPane openDataSetOverview = (AnchorPane) loader.load();
			
			//Set overview into center of root lyout
			rootLayout.setCenter(openDataSetOverview);
			
			//Give the controller acces to the main app
			openController = loader.getController();
			openController.setMainApp(this);
			openController.setDataSet(dataSet);
			
			this.primaryStage.setTitle(appVersion + dataSet.getDataSetName());
			
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Opens dialog to edit details of dataset. If user clicks ok, the new changes are saved into provided dataset
	 * 
	 * @param dataSet is the dataset to be edited
	 * @return true if user clicks ok, false otherwise
	 */
	public boolean showDataSetEditDialog (DataSet dataSet) {
		try {
			//Load fxml file
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/DataSetEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			
			//Create new dialog stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Dataset");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Image appIcon = new Image(getClass().getResourceAsStream("/Apps-xorg-icon.png"));
	        dialogStage.getIcons().add(appIcon);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        //Set the dataset into controller
	        DataSetEditDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setDataSet(dataSet);
	        
	        //Show dialog and wait for user
	        dialogStage.showAndWait();
	        
	        return controller.isOkClicked();	        
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean showDataSetAddDialog (DataSet dataSet) {
		try {
			//Load fxml file
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/DataSetAddDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			
			//Create new dialog stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add Dataset");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Image appIcon = new Image(getClass().getResourceAsStream("/Apps-xorg-icon.png"));
	        dialogStage.getIcons().add(appIcon);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        //Set the dataset into controller
	        DataSetAddDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setDataSet(dataSet);
	        
	        //Show dialog and wait for user
	        dialogStage.showAndWait();
	        
	        return controller.isOkClicked();	        
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public ResizableRectangleWrapper showAnnotateDialog (Group imageGroup, ResizableRectangle rectangle, Integer index) {
		try{
			//Load fxml file
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/AnnotateDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			
			//Create new dialog stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Annotate");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Image appIcon = new Image(getClass().getResourceAsStream("/Apps-xorg-icon.png"));
	        dialogStage.getIcons().add(appIcon);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        
	        AnnotateDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setMainApp(this);
	        controller.setImageGroup(imageGroup);
	        controller.setRectangle(rectangle);
	        controller.setIndex(index);
	        
	        dialogStage.showAndWait();
	        return controller.isOkClicked();
			
		}catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean showEditAnnotationDialog (ResizableRectangleWrapper rectangle, Integer index, ObservableList<ResizableRectangleWrapper> annotations) {
		try{
			//Load fxml file
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/EditAnnotationDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			
			//Create new dialog stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Annotation");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Image appIcon = new Image(getClass().getResourceAsStream("/Apps-xorg-icon.png"));
	        dialogStage.getIcons().add(appIcon);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        
	        EditAnnotationDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setMainApp(this);
	        controller.setRectangle(rectangle);
	        controller.setIndex(index);
	        controller.setAnnotation(annotations);
	        
	        //dialogStage.setX(rectangle.getXMax());
	        //dialogStage.setY(rectangle.getYMax());
	        
	        dialogStage.showAndWait();
	        return controller.isOkClicked();
			
		}catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public void showExportFramesDialog () {
		try {
			
			// Load the fxml file and create a new stage for the popup.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("view/ExportFramesDialog.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Export frames from video");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Image appIcon = new Image(getClass().getResourceAsStream("/Apps-xorg-icon.png"));
	        dialogStage.getIcons().add(appIcon);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        ExportFramesDialogController controller = loader.getController();
	        controller.setMainApp(this);
	        controller.setDialogStage(dialogStage);
	        
	        dialogStage.showAndWait();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showConfigAnnotations () {
		try {
			
			// Load the fxml file and create a new stage for the popup.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("view/ConfigAnnotations.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Annotations Configuration");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Image appIcon = new Image(getClass().getResourceAsStream("/Apps-xorg-icon.png"));
	        dialogStage.getIcons().add(appIcon);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        ConfigAnnotationsController controller = loader.getController();
	        controller.setMainApp(this);
	        controller.setDialogStage(dialogStage);
	        
	        dialogStage.showAndWait();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method which read annotations from config file and add it to list of available annotations
	 */
	public void initAnnotations() {
		try {
			File file = new File("config/annotations_config.txt");
			InputStream res = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(res));
			String line = null;
			while ((line = reader.readLine()) != null) {
		        annotations.add(line);
		    }
			reader.close();
//			for (String annotation: annotations) {
//				colorsOfClasses.put(annotation, Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.2));
//			}
			
			file = new File("config/annotations_colors.txt");
			res = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(res));
			line = null;
			while ((line = reader.readLine()) != null) {
				String[] temp = line.split(" : ");
		        colorsOfClasses.put(temp[0], new SimpleObjectProperty<Color>(Color.web(temp[1])) );
		    }
			reader.close();
			
			file = new File("config/default.txt");
			res = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(res));
			line = null;
			while ((line = reader.readLine()) != null) {
				String[] temp = line.split(": ");
		        switch(temp[0]) {
		        	case "class" :
		        		defaultClass.set(temp[1]);
		        		break;
		        	case "color" :
		        		defaultColor = Color.web(temp[1]);
		        		break;
		        	case "border" :
		        		defaultBorderColor = Color.web(temp[1]);
		        }
		    }
			reader.close();
			
			file = new File("config/flags_color.txt");
			res = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(res));
			line = null;
			while ((line = reader.readLine()) != null) {
				String[] temp = line.split(" : ");
		        colorsOfFlags.put(temp[0], new SimpleObjectProperty<Color>(Color.web(temp[1])) );
		    }
			reader.close();
			
			file = new File("config/collors.txt");
			res = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(res));
			line = null;
			while ((line = reader.readLine()) != null) {
				colors.add(line);
		    }
			reader.close();
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

	public static void main(String[] args) {
		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		launch(args);
	}
	
	/**
	 * Loads database of datasets from the specified file. The current datasets will be replaced
	 * @param file represents simple database of datasets
	 */
	
	public void loadDatasetsFromFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(DataSetListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();
			
			// Reading XML from the file and unmarshalling.
			DataSetListWrapper wrapper = (DataSetListWrapper) um.unmarshal(file);
			
			dataSets.clear();
			dataSets.addAll(wrapper.getDataSets());
			
			// Save the file path to the registry.
			setLastFilePath(file);
			
			
		} catch (Exception e) {
			 Alert alert = new Alert(AlertType.ERROR);
		     alert.setTitle("Error");
		     alert.setHeaderText("Could not load data");
		     alert.setContentText("Could not load data from file:\n" + file.getPath());

		     alert.showAndWait();
		}
	}
	
	
	/**
	 * Saves the current datasets to the specified file
	 * @param file represents simple database of datasets
	 */
	
	public void saveDataSetsToFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(DataSetListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			//Wrapping our datasets
			DataSetListWrapper wrapper = new DataSetListWrapper();
			wrapper.setDataSets(dataSets);
			
			// Marshalling and saving XML to the file.
	        m.marshal(wrapper, file);
	        
	        // Save the file path to the registry.
	     	setLastFilePath(file);
			
		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Could not save data");
	        alert.setContentText("Could not save data to file:\n" + file.getPath());

	        alert.showAndWait();
		}
	}
	

	/**
	 * Returns file preferences.
	 * The preferences is read from OS specific registry. 
	 * @return file with last used preference, if no such preference, null is returned
	 */
	public static File getLastFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("filePath", null);
	    if (filePath != null) {
	        return new File(filePath);
	    } else {
	        return null;
	    }
	}
	
	/**
	 * Sets the file path of the currently loaded file. The path is persisted in
	 * the OS specific registry.
	 * @param file is last used file or null to remove path
	 */
	public static void setLastFilePath(File file) {
		 Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		    if (file != null) {
		        prefs.put("filePath", file.getPath());
		    } else {
		        prefs.remove("filePath");
		    }
	}
}
