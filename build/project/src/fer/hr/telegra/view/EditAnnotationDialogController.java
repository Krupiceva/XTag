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
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
/**
 * Controller class for editing selected annotation 
 * @author dmlinaric
 *
 */
public class EditAnnotationDialogController {
	/**
	 * Listview with all possible annotations class
	 */
	@FXML
    private ListView<String>  listOfAnnotations;
	/**
	 * Listview with all possible annotations color
	 */
	@FXML
	private ListView<String> listOfColors;
	/**
	 * Textfield for possible additionalText, not mandatory
	 */
	@FXML
	private TextField additionalText;
	/**
	 * Check box for is it or not this annotation difficult to see on image
	 */
	@FXML
	private CheckBox difficult;
	/**
	 * Check box for is it or not this annotation truncated (when the object is not completely visible in the image) 
	 */
	@FXML
	private CheckBox truncated;
	/**
	 * Check box for is it or not this annotation overlap with some other annotation
	 */
	@FXML
	private CheckBox overlap;
	/**
	 * Stage of this dialog window
	 */
	private Stage dialogStage;
	/**
	 * Annotation to be edited
	 */
	private ResizableRectangleWrapper rectangleWrapper;
	private boolean okClicked = false;
   
    public EditAnnotationDialogController() {}
	
    /**
     * Initialize method is mandatory for JavaFX FXML layout 
     */
    @FXML
    private void initialize() {
    }
    
    /**
     * Sets the stage of this dialog.
     * Adding keyboard shortcuts to the scene of this stage
     * @param dialogStage is stage to set
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.setResizable(false);
        this.dialogStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
            	KeyCombination shift1 = new KeyCodeCombination(KeyCode.DIGIT1, KeyCodeCombination.SHIFT_DOWN);
            	KeyCombination shift2 = new KeyCodeCombination(KeyCode.DIGIT2, KeyCodeCombination.SHIFT_DOWN);
            	KeyCombination shift3 = new KeyCodeCombination(KeyCode.DIGIT3, KeyCodeCombination.SHIFT_DOWN);
            	KeyCombination shift4 = new KeyCodeCombination(KeyCode.DIGIT4, KeyCodeCombination.SHIFT_DOWN);
            	KeyCombination shift5 = new KeyCodeCombination(KeyCode.DIGIT5, KeyCodeCombination.SHIFT_DOWN);
            	KeyCombination shift6 = new KeyCodeCombination(KeyCode.DIGIT6, KeyCodeCombination.SHIFT_DOWN);
            	KeyCombination shift7 = new KeyCodeCombination(KeyCode.DIGIT7, KeyCodeCombination.SHIFT_DOWN);
            	KeyCombination shift8 = new KeyCodeCombination(KeyCode.DIGIT8, KeyCodeCombination.SHIFT_DOWN);
            	KeyCombination shift9 = new KeyCodeCombination(KeyCode.DIGIT9, KeyCodeCombination.SHIFT_DOWN);
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
                else if (shift1.match(event)) {
                	listOfColors.getSelectionModel().select(0);
				}
                else if (shift2.match(event)) {
                	listOfColors.getSelectionModel().select(1);
				}
                else if (shift3.match(event)) {
                	listOfColors.getSelectionModel().select(2);
				}
                else if (shift4.match(event)) {
                	listOfColors.getSelectionModel().select(3);
				}
                else if (shift5.match(event)) {
                	listOfColors.getSelectionModel().select(4);
				}
                else if (shift6.match(event)) {
                	listOfColors.getSelectionModel().select(5);
				}
                else if (shift7.match(event)) {
                	listOfColors.getSelectionModel().select(6);
				}
                else if (shift8.match(event)) {
                	listOfColors.getSelectionModel().select(7);
				}
                else if (shift9.match(event)) {
                	listOfColors.getSelectionModel().select(8);
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
    
    /**
     * Give access to the mainApp.
     * Populate lists with data from mainApp
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        listOfAnnotations.setEditable(true);
        listOfAnnotations.setItems(mainApp.getAnnotations());
        listOfColors.setEditable(true);
        listOfColors.setItems(mainApp.getColors());
    }
    
    /**
     * Put information of annotation on the screen
     * @param rectangle is annotation that is selected to edit
     */
    public void setRectangle(ResizableRectangleWrapper rectangle) {
    	this.rectangleWrapper = rectangle;
    	additionalText.setText(rectangleWrapper.getAdditionalText());
    	difficult.setSelected(rectangleWrapper.getDifficult());
    	truncated.setSelected(rectangleWrapper.getTruncated());
    	overlap.setSelected(rectangleWrapper.getOverlap());
    	listOfAnnotations.getSelectionModel().select(rectangleWrapper.getKlass());
    	listOfColors.getSelectionModel().select(rectangleWrapper.getColor());
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
    
    /**
     * Add all new information to the selected annotation
     */
    @FXML
    private void handleOK() {
    	rectangleWrapper.setKlass(listOfAnnotations.getSelectionModel().getSelectedItem());
    	rectangleWrapper.setAdditionalText(additionalText.getText());
    	rectangleWrapper.setDifficult(difficult.isSelected());
    	rectangleWrapper.setTruncated(truncated.isSelected());
    	rectangleWrapper.setOverlap(overlap.isSelected());
    	rectangleWrapper.setColor(listOfColors.getSelectionModel().getSelectedItem());
    	okClicked = true;
    	dialogStage.close();
    }
}
