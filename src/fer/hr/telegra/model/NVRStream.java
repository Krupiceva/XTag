package fer.hr.telegra.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 * Model class that represent one NVR stream
 * @author dmlinaric
 *
 */
public class NVRStream {
	
	/**
	 * Stream addrees
	 * e.g. txvn://172.17.101.88:9801/600305
	 */
	private StringProperty address;
	/**
	 * start timestamp
	 * e.g. 20180601T003317
	 */
	private StringProperty startTime;
	/**
	 * end timestamp
	 * e.g. 20180601T003317
	 */
	private StringProperty endTime;
	
	/**
	 * default constructor
	 */
	public NVRStream() {
		this(null, null, null);
	}
	
	/**
	 * Constructor with initial data
	 * @param address is stream address
	 * @param startTime is start of available timestamp
	 * @param endTime is end of available timestamp
	 */
	public NVRStream(String address, String startTime, String endTime) {
		this.address = new SimpleStringProperty(address);
		this.startTime = new SimpleStringProperty(startTime);
		this.endTime = new SimpleStringProperty(endTime);
	}
	
	/**
     * Getters and setters for all attributes of NVRStream
     * It must be with corresponding name because DataSetListWrapper and loading/saving in xml database 
     */
	public String getAddress() {
		return this.address.get();
	}
	
	public void setAddress(String address) {
		this.address.set(address);
	}
	
	public StringProperty addressProperty() {
		return this.address;
	}
	
	public String getStartTime() {
		return this.startTime.get();
	}
	
	public void setStartTime(String startTime) {
		this.startTime.set(startTime);
	}
	
	public StringProperty startTimeProperty() {
		return startTime;
	}
	
	public String getEndTime() {
		return this.endTime.get();
	}
	
	public void setEndTime(String endTime) {
		this.endTime.set(endTime);
	}
	
	public StringProperty endTimeProperty() {
		return endTime;
	}

}
