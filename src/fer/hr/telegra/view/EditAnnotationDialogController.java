package fer.hr.telegra.view;

import fer.hr.telegra.MainApp;
import fer.hr.telegra.model.ResizableRectangleWrapper;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class EditAnnotationDialogController {

	@FXML
    private ListView<String>  listOfAnnotations;
	@FXML
	private TextField additionalText;
	@FXML
	private CheckBox difficult;
	@FXML
	private CheckBox truncated;
	@FXML
	private CheckBox overlap;
	private Stage dialogStage;
	private ResizableRectangleWrapper rectangleWrapper;
	private boolean okClicked = false;
    
    public EditAnnotationDialogController() {}
	
    @FXML
    private void initialize() {
    	
    }
    
    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.setResizable(false);
        //this.dialogStage.initStyle(StageStyle.UNDECORATED);
        this.dialogStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.Q) {
                	difficult.setSelected(!difficult.isSelected());
                }
                else if(event.getCode() == KeyCode.R) {
            		handleOK();
                }
                else if (event.getCode() == KeyCode.W) {
                	overlap.setSelected(!overlap.isSelected());
                }
                else if (event.getCode() == KeyCode.E) {
                	truncated.setSelected(!truncated.isSelected());
                }
                else if (event.getCode() == KeyCode.NUMPAD1 || event.getCode() == KeyCode.DIGIT1) {
                	listOfAnnotations.getSelectionModel().select(0);
                }
                else if (event.getCode() == KeyCode.NUMPAD2 || event.getCode() == KeyCode.DIGIT2) {
                	listOfAnnotations.getSelectionModel().select(1);
                }
                else if (event.getCode() == KeyCode.NUMPAD3 || event.getCode() == KeyCode.DIGIT3) {
                	listOfAnnotations.getSelectionModel().select(2);
                }
                else if (event.getCode() == KeyCode.NUMPAD4 || event.getCode() == KeyCode.DIGIT4) {
                	listOfAnnotations.getSelectionModel().select(3);
                }
                else if (event.getCode() == KeyCode.NUMPAD5 || event.getCode() == KeyCode.DIGIT5) {
                	listOfAnnotations.getSelectionModel().select(4);
                }
                else if (event.getCode() == KeyCode.NUMPAD6 || event.getCode() == KeyCode.DIGIT6) {
                	listOfAnnotations.getSelectionModel().select(5);
                }
                else if (event.getCode() == KeyCode.NUMPAD7 || event.getCode() == KeyCode.DIGIT7) {
                	listOfAnnotations.getSelectionModel().select(6);
                }
                else if (event.getCode() == KeyCode.NUMPAD8 || event.getCode() == KeyCode.DIGIT8) {
                	listOfAnnotations.getSelectionModel().select(7);
                }
                else if (event.getCode() == KeyCode.NUMPAD9 || event.getCode() == KeyCode.DIGIT9) {
                	listOfAnnotations.getSelectionModel().select(8);
                }
                else if (event.getCode() == KeyCode.ENTER) {
                	handleOK();
                }
                
            }
        });
    }
    
    
    public void setMainApp(MainApp mainApp) {
        listOfAnnotations.setEditable(true);
        listOfAnnotations.setItems(mainApp.getAnnotations());
        //listOfAnnotations.setCellFactory(param -> new RadioListCell());
    }
    
    
    public void setRectangle(ResizableRectangleWrapper rectangle) {
    	this.rectangleWrapper = rectangle;
    	additionalText.setText(rectangleWrapper.getAdditionalText());
    	difficult.setSelected(rectangleWrapper.getDifficult());
    	truncated.setSelected(rectangleWrapper.getTruncated());
    	overlap.setSelected(rectangleWrapper.getOverlap());
    	listOfAnnotations.getSelectionModel().select(rectangleWrapper.getKlass());
    }
    
    public void setIndex(Integer index) {
    }
    
    public void setAnnotation(ObservableList<ResizableRectangleWrapper> annotations) {
    }
    
    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }
    
    
    /**
     * Method which is called when user clicks cancel.
     */
    @FXML
    private void handleCancel() {
    	dialogStage.close();
    }
    
    @FXML
    private void handleOK() {
    	rectangleWrapper.setKlass(listOfAnnotations.getSelectionModel().getSelectedItem());
    	rectangleWrapper.setAdditionalText(additionalText.getText());
    	rectangleWrapper.setDifficult(difficult.isSelected());
    	rectangleWrapper.setTruncated(truncated.isSelected());
    	rectangleWrapper.setOverlap(overlap.isSelected());
    	okClicked = true;
    	dialogStage.close();
    }
}
