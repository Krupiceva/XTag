package fer.hr.telegra.model;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Model class for image/frame.
 *
 */
public class DataImage {
	private final StringProperty imageName;
	//private ObservableList<ResizableRectangleWrapper> annotations = FXCollections.observableArrayList();
	
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
    //Ne koristim jos, koristit cu kad cu imat bazu
    /**
    public ObservableList<ResizableRectangleWrapper> getAnnotations(){
    	return annotations;
    }
    
    public void setAnnotations(ObservableList<ResizableRectangleWrapper> annotations) {
    	this.annotations = annotations;
    }
    
    public void addAnnotation(ResizableRectangleWrapper annotation) {
    	this.annotations.add(annotation);
    }
    */

}
