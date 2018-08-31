package fer.hr.telegra.view;

import java.sql.Timestamp;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Controller class for adding new stream to the new dataset
 * @author dmlinaric
 *
 */
public class AddNewStreamDialogController {

	/**
	 * Stage of this dialog window
	 */
	private Stage dialogStage;
	/**
	 * Textfiled with address od new stream
	 */
	@FXML
	private TextField streamAddress;
	/**
	 * Textfield with start of timestamp range for new stream
	 */
	@FXML
	private TextField startTime;
	/**
	 * Textfield with end of timestamp range for new stream
	 */
	@FXML
	private TextField endTime;
	/**
	 * Flag that indicates if user clicked add
	 */
	private boolean isOk = false;
	/**
	 * Return value that contains information about new stream
	 */
	private String streamInfo;
	
	/**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
	 */
	@FXML
    private void initialize() {
	}
	
	/**
	 * Sets the stage of this dialog and disable resize
	 * @param dialogStage is stage of this dialog
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
		this.dialogStage.setResizable(false);
	}
	
	/**
	 * Method which is called when user click Add
	 * To the one string add all information about stream
	 */
	@FXML
	private void handleAdd() {
		if (isInputValid()) {
			streamInfo = streamAddress.getText() + ";" + startTime.getText() + ";" + endTime.getText();
			isOk = true;
			this.dialogStage.close();
		}
	}
	
	/**
	 * Method which is called when user click cancel
	 * Close this dialog
	 */
	@FXML
	private void handleCancel() {
		isOk = false;
		this.dialogStage.close();
	}
	
	/**
	 * @return true if user is clicked add, false otherwise 
	 */
	public String isOk() {
		if(isOk) {
			return streamInfo;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Valid input fields
	 * @return true if all fields are correct, false otherwise and pop up a alert dialog
	 */
	private boolean isInputValid() {
		String errorMessage = "";
    	
    	if (streamAddress.getText() == null || streamAddress.getText().length() == 0) {
    		errorMessage += "No valid address!\n";
    	}
    	if (startTime.getText() == null || startTime.getText().length() == 0) {
    		errorMessage += "No valid start time!\n";
    	}
    	if (endTime.getText() == null || endTime.getText().length() == 0) {
    		errorMessage += "No valid end time!\n";
    	}
    	
    	String time = startTime.getText();
    	try {
			String year = time.substring(0, 4);
			String month = time.substring(4, 6);
			String day = time.substring(6, 8);
			String hours = time.substring(9, 11);
			String minutes = time.substring(11, 13);
			String seconds = time.substring(13);
			String newStartTime = year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
			
			time = endTime.getText();
			year = time.substring(0, 4);
			month = time.substring(4, 6);
			day = time.substring(6, 8);
			hours = time.substring(9, 11);
			minutes = time.substring(11, 13);
			seconds = time.substring(13);
			String newEndTime = year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
			
			long start = Timestamp.valueOf(newStartTime).getTime();
			long end = Timestamp.valueOf(newEndTime).getTime();
			if(end-start < 1) {
				errorMessage += "Incorrect time. End time is less then start time!\n";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			errorMessage += "Incorrect time format. (yyyyMMddTHHmmss)!\n";
		}
		
		
    	
    	//If input is ok
    	if (errorMessage.length() == 0) {
    		return true;
    	}
    	else {
    		//Show error alert
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.initOwner(dialogStage);
    		alert.setTitle("Invalid Fields!");
    		alert.setHeaderText("Please correct invalid fields.");
    		alert.setContentText(errorMessage);
    		
    		alert.showAndWait();
    		
    		return false;
    	}
	}
}
