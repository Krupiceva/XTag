package fer.hr.telegra.model;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Model class for image in dataset
 * @author dmlinaric
 *
 */
public class DataImage {
	private final StringProperty imageName;
	
	/**
     * Default constructor.
     */
    public DataImage() {
        this(null);
    }
	
    
    public DataImage(String imageName) {
    	this.imageName = new SimpleStringProperty(imageName);

    }
    
    public String getImageName() {
    	return imageName.get();
    }
    
    public void setImageName(String imageName) {
    	this.imageName.set(imageName);
    }
    
    public StringProperty imageNameProperty() {
    	return imageName;
    }
}
