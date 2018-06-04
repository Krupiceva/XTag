package fer.hr.telegra.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChangeableHistory {
	private static ChangeableHistory singleInstance = null;
	
	public int currentIndex = -1;
	//public Operation operation;
	public ObservableList<Operation> operations = FXCollections.observableArrayList();
	public ObservableList<OperationRectangleWrappers> rectangles = FXCollections.observableArrayList();
	
	
	public static ChangeableHistory getInstance() {
        if (singleInstance == null)
        	singleInstance = new ChangeableHistory();
 
        return singleInstance;
    }
	
	public void resetHistory() {
		singleInstance = null;
	}
}
