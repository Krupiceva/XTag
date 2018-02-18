package fer.hr.telegra.model;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import javafx.beans.property.*;

/**
 * Model class for DataSet of images/frames(video)
 *
 */
public class DataSet {
	private final StringProperty dataSetName;
	private StringProperty dataSetImagesLocation;
	private StringProperty dataSetAnnotationsLocation;
	private IntegerProperty numOfImgInDataSet;
	private DoubleProperty cameraAngle;
	private DoubleProperty cameraHigh;
	private ObjectProperty<WeatherConditions> weatherCondition;
	private ObjectProperty<ImageQuality> imageQuality;
	
	 /**
     * The data as an observable list of DataImages.
     */
    private ObservableList<DataImage> dataSetImages = FXCollections.observableArrayList();
    
	
	/**
     * Default constructor.
     */
    public DataSet() {
        this(null);
    }
    
    /**
     * Constructor with initial data. Initial for numOfImg, cameraAngle and cameraHigh is 0;
     * Initial for weatherCondition and imageQuality is UNKNOWN
     * Initial location is not selected
     * 
     * @param dataSetName
     */
    public DataSet(String dataSetName) {
    	this.dataSetName = new SimpleStringProperty(dataSetName);
    	this.numOfImgInDataSet = new SimpleIntegerProperty(0);
    	this.dataSetImagesLocation = new SimpleStringProperty("No Directory selected");
    	this.dataSetAnnotationsLocation = new SimpleStringProperty("No Directory selected");
    	this.cameraAngle = new SimpleDoubleProperty(0);
    	this.cameraHigh = new SimpleDoubleProperty(0);
    	this.weatherCondition = new SimpleObjectProperty<WeatherConditions>(WeatherConditions.Unknown);
    	this.imageQuality = new SimpleObjectProperty<ImageQuality>(ImageQuality.Unknown);
    }
    
    //UNAPRIJEDITI SRUSI SE ZA VELIK BROJ SLIKA
    /**
     * Method which check if this dataset contains specified image
     * @param path is string of absolute path to image
     * @return true if contains, false otherwise
     *
    public boolean containsImage(String path) {
    	for(DataImage img: dataSetImages) {
    		if(img.getImagePath().equals(path)) {
    			return true;
    		}
    	}
    	return false;
    }
    */
    
    public String getDataSetName() {
    	return dataSetName.get();
    }
    
    public void setDataSetName(String dataSetName) {
    	this.dataSetName.set(dataSetName);
    }
    
    public StringProperty dataSetNameProperty() {
    	return dataSetName;
    }
    
    public String getDataSetImagesLocation() {
    	return dataSetImagesLocation.get();
    }
    
    public void setDataSetImagesLocation(String dataSetLocation) {
    	this.dataSetImagesLocation.set(dataSetLocation);
    }
    
    public StringProperty dataSetImagesLocationProperty() {
    	return dataSetImagesLocation;
    }
    
    public String getDataSetAnnotationsLocation() {
    	return dataSetAnnotationsLocation.get();
    }
    
    public void setDataSetAnnotationsLocation(String dataSetLocation) {
    	this.dataSetAnnotationsLocation.set(dataSetLocation);
    }
    
    public StringProperty dataSetAnnotationsLocationProperty() {
    	return dataSetAnnotationsLocation;
    }
    
    public ObservableList<DataImage> getDataSetImages() {
    	return dataSetImages;
    }
    
    public void setDataSetImages(ObservableList<DataImage> images) {
    	this.dataSetImages = images;
    }
    
    public void addDataSetImage(DataImage image) {
    	dataSetImages.add(image);
    }
    
    
    public Integer getNumOfImgInDataSet() {
    	return numOfImgInDataSet.get();
    }
    
    public void setNumOfImgInDataSet(Integer numOfImgInDataSet) {
    	this.numOfImgInDataSet.set(numOfImgInDataSet);
    	//this.numOfImgInDataSet.set(dataSetImages.size());

    }
    
    public IntegerProperty numOfImgInDataSetProperty() {
    	return numOfImgInDataSet;
    }
    
    public Double getCameraAngle() {
    	return cameraAngle.get();
    }
    
    public void setCameraAngle(Double cameraAngle) {
    	this.cameraAngle.set(cameraAngle);
    }
    
    public DoubleProperty cameraAngleProperty() {
    	return cameraAngle;
    }
    
    public Double getCameraHigh() {
    	return cameraHigh.get();
    }
    
    public void setCameraHigh(Double cameraHigh) {
    	this.cameraHigh.set(cameraHigh);
    }
    
    public DoubleProperty cameraHighProperty() {
    	return cameraHigh;
    }
    
    public WeatherConditions getWeatherCondition() {
    	return weatherCondition.get();
    }
    
    public void setWeatherCondition(WeatherConditions weatherCondition) {
    	this.weatherCondition.set(weatherCondition);
    }
    
    public ObjectProperty<WeatherConditions> weatherConditionProperty(){
    	return weatherCondition;
    }
    
    public ImageQuality getImageQuality() {
    	return imageQuality.get();
    }
    
    public void setImageQuality(ImageQuality imageQuality) {
    	this.imageQuality.set(imageQuality);
    }
    
    public ObjectProperty<ImageQuality> imageQualityProperty(){
    	return imageQuality;
    }
    
}
