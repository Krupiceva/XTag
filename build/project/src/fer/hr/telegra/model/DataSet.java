package fer.hr.telegra.model;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.beans.property.SimpleStringProperty;

import java.util.Iterator;

import org.magicwerk.brownies.collections.BigList;

import javafx.beans.property.*;

/**
 * Main model class of dataset with images (frames in video)
 * @author dmlinaric
 *
 */
public class DataSet {
	/**
	 * Attributes of dataset
	 */
	private final StringProperty dataSetName;
	private StringProperty dataSetImagesLocation;
	private StringProperty dataSetAnnotationsLocation;
	private DoubleProperty cameraAngle;
	private DoubleProperty cameraHigh;
	private ObjectProperty<WeatherConditions> weatherCondition;
	private ObjectProperty<ImageQuality> imageQuality;
	private ObjectProperty<TimeOfTheDay> timeOfTheDay;
	
	 /**
     * The data as an observable list of DataImages (without annotations);
     */
    private ObservableList<DataImage> dataSetImages = FXCollections.observableArrayList();
    
    /**
     * The data as an observable list of DataImages (with annotations);
     */
    private ObservableList<DataImage> dataSetImagesWithAnnotations = FXCollections.observableArrayList();
    
    /**
     * The data as an observable list of DataImages (with annotations and verified);
     */
    private ObservableList<DataImage> dataSetVerifiedImages = FXCollections.observableArrayList();
    
    /**
     * The Map with counter of every annotation class in dataset
     */
    private ObservableMap<String, Integer> annotations = FXCollections.observableHashMap();
    
	
	/**
     * Default constructor.
     */
    public DataSet() {
        this(null);
    }
    
    /**
     * Constructor with initial data:
     * Initial cameraAngle and cameraHigh is 0;
     * Initial for weatherCondition, imageQuality and timeOfTheDay is UNKNOWN
     * Initial locations is not selected
     * @param dataSetName is name of this datset
     */
    public DataSet(String dataSetName) {
    	this.dataSetName = new SimpleStringProperty(dataSetName);
    	this.dataSetImagesLocation = new SimpleStringProperty("No Directory selected");
    	this.dataSetAnnotationsLocation = new SimpleStringProperty("No Directory selected");
    	this.cameraAngle = new SimpleDoubleProperty(0);
    	this.cameraHigh = new SimpleDoubleProperty(0);
    	this.weatherCondition = new SimpleObjectProperty<WeatherConditions>(WeatherConditions.Unknown);
    	this.imageQuality = new SimpleObjectProperty<ImageQuality>(ImageQuality.Unknown);
    	this.timeOfTheDay = new SimpleObjectProperty<TimeOfTheDay>(TimeOfTheDay.Unknown);
    }
    
    /**
     * Getters and setters for all attributes of dataset
     * It must be with corresponding name because DataSetListWrapper and loading/saving in xml database 
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
    /**
     * Remove image from list
     * @param imageName is name of image which needs to be removed from list of images without annotations
     */
    public void removeDataSetImage(String imageName) {
    	//Big list is fast collection implementation for bigdata
    	BigList<DataImage> images =  BigList.create(dataSetImages);
    	Iterator<DataImage> itr = images.iterator();
    	while(itr.hasNext()) {
    		DataImage img = itr.next();
    		if (img.getImageName().equals(imageName)) {
    			dataSetImages.remove(dataSetImages.indexOf(img)); 			
    		}
    	}
    }
    
    public ObservableList<DataImage> getDataSetImagesWithAnnotations(){
    	return dataSetImagesWithAnnotations;
    }
    
    public void setDataSetImagesWithAnnotations(ObservableList<DataImage> images) {
    	this.dataSetImagesWithAnnotations = images;
    }
    
    public void addDataSetImageWithAnnotations(DataImage image) {
    	dataSetImagesWithAnnotations.add(image);
    }
    /**
     * Remove image from list
     * @param imageName is name of image which needs to be removed from list of images with annotations
     */
    public void removeDataSetImageWithAnnotations(String imageName) {
    	//Big list is fast collection implementation for bigdata
    	BigList<DataImage> images =  BigList.create(dataSetImagesWithAnnotations);
    	Iterator<DataImage> itr = images.iterator();
    	while(itr.hasNext()) {
    		DataImage img = itr.next();
    		if (img.getImageName().equals(imageName)) {
    			dataSetImagesWithAnnotations.remove(dataSetImagesWithAnnotations.indexOf(img)); 			
    		}
    	}
    }
    
    public ObservableList<DataImage> getDataSetVerifiedImages(){
    	return dataSetVerifiedImages;
    }
    
    public void setDataSetVerifiedImages(ObservableList<DataImage> images) {
    	this.dataSetVerifiedImages = images;
    }
    
    public void addDataSetVerifiedImage(DataImage image) {
    	dataSetVerifiedImages.add(image);
    }
    
    /**
     * Remove image from list
     * @param imageName is name of image which needs to be removed from list of verified images 
     */
    public void removeDataSetVerifiedImage(String imageName) {
    	BigList<DataImage> images =  BigList.create(dataSetVerifiedImages);
    	Iterator<DataImage> itr = images.iterator();
    	while(itr.hasNext()) {
    		DataImage img = itr.next();
    		if (img.getImageName().equals(imageName)) {
    			dataSetVerifiedImages.remove(dataSetVerifiedImages.indexOf(img)); 			
    		}
    	}
    }
    
    public ObservableMap<String, Integer> getAnnotations() {
    	return annotations;
    }
    
    public void setAnnotations(ObservableMap<String, Integer> annotations) {
    	this.annotations = annotations;
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
    
    public TimeOfTheDay getTimeOfTheDay() {
    	return timeOfTheDay.get();
    }
    
    public void setTimeOfTheDay(TimeOfTheDay timeOfTheDay) {
    	this.timeOfTheDay.set(timeOfTheDay);
    }
    
    public ObjectProperty<TimeOfTheDay> timeOfTheDayProperty(){
    	return timeOfTheDay;
    }
}
