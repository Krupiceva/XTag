package fer.hr.telegra.model;
/**
 * Singleton class with last path used in application
 * @author dmlinaric
 *
 */
public class PathData {
    private static PathData singleInstance = null;
 
    // variable of type String
    public String path;
 
    private PathData()
    {
        path = "";
    }
 
    // static method to create instance of PathData class
    public static PathData getInstance() {
        if (singleInstance == null)
        	singleInstance = new PathData();
 
        return singleInstance;
    }
}
