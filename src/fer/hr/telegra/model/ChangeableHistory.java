package fer.hr.telegra.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Singleton class which represent history for implementing undo/redo functionality
 * @author dmlinaric
 *
 */
public class ChangeableHistory {
	private static ChangeableHistory singleInstance = null;
	
	public int currentIndex = -1;
	public ObservableList<Operation> operations = FXCollections.observableArrayList();
	public ObservableList<OperationRectangleWrappers> rectangles = FXCollections.observableArrayList();
	
	/**
	 * Method which return instance of class
	 * @return existing instance if exist, new otherwise
	 */
	public static ChangeableHistory getInstance() {
        if (singleInstance == null)
        	singleInstance = new ChangeableHistory();
 
        return singleInstance;
    }
	
	public void resetHistory() {
		singleInstance = null;
	}
}
