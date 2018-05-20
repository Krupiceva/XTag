package fer.hr.telegra.view;

import fer.hr.telegra.MainApp;
import fer.hr.telegra.model.DataSet;
import fer.hr.telegra.model.ResizableRectangle;
import fer.hr.telegra.model.ResizableRectangleWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class AnnotateDialogController {
	@FXML
    private ListView<String>  listOfAnnotations;
	@FXML
	private Slider overlapSlider;
	@FXML
	private Slider truncationSlider;
	@FXML
	private TextField overlapField;
	@FXML
	private TextField truncationField;
	@FXML
	private TextField additionalText;
	@FXML
	private CheckBox difficult;
	@FXML
	private CheckBox truncated;
	@FXML
	private CheckBox overlap;
	private Stage dialogStage;
	private MainApp mainApp;
	private Group imageGroup;
	private ResizableRectangle rectangle;
	private ResizableRectangleWrapper rectangleWrapper;
	private Integer index;
    private boolean okClicked = false;
    public static final ObservableList<RadioButton> namesRadioButtons = FXCollections.observableArrayList();
    
	public AnnotateDialogController() {
	}
	
	@FXML
    private void initialize() {
		/**
		StringConverter<Number> converter = new NumberStringConverter();
		overlapSlider.setValue(INIT_VALUE);
		overlapField.setText(new Double(INIT_VALUE).toString());
		Bindings.bindBidirectional(overlapField.textProperty(), overlapSlider.valueProperty(), converter);
		truncationSlider.setValue(INIT_VALUE);
		truncationField.setText(new Double(INIT_VALUE).toString());
		Bindings.bindBidirectional(truncationField.textProperty(), truncationSlider.valueProperty(), converter);
		*/
		//Bug u javafx-u, valueProperty ne zna šta bi sa null vrijednosti ukoliko se izbriše iz text fielda vrijednost
		//pa onda baca warning exception
		//truncationField.textProperty().bindBidirectional(truncationSlider.valueProperty(), NumberFormat.getNumberInstance());
		//overlapField.textProperty().bindBidirectional(overlapSlider.valueProperty(), NumberFormat.getNumberInstance());
		
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
        this.dialogStage.setOnCloseRequest(event -> {
        	imageGroup.getChildren().remove(imageGroup.getChildren().size() - 10, imageGroup.getChildren().size());
        });
        this.dialogStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
            	if (event.getCode() == KeyCode.ENTER) {
                	System.out.println("stisno enter");
                	handleOK();
                }
            	else if(event.getCode() == KeyCode.Q) {
                	difficult.setSelected(!difficult.isSelected());
                }
                else if(event.getCode() == KeyCode.W) {
                	overlap.setSelected(!overlap.isSelected());
                }
                else if(event.getCode() == KeyCode.E) {
                	truncated.setSelected(!truncated.isSelected());
                }
                else if(event.getCode() == KeyCode.NUMPAD1 || event.getCode() == KeyCode.DIGIT1) {
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
                
                
            }
        });
    }
    
    public void setDataSet (DataSet dataSet) {
    }
    
    public void setMainApp(MainApp mainApp) {
    	this.mainApp = mainApp;
        listOfAnnotations.setEditable(true);
        listOfAnnotations.setItems(mainApp.getAnnotations());
        //listOfAnnotations.setCellFactory(param -> new RadioListCell());
    }
    
    public void setImageGroup(Group imageGroup) {
    	this.imageGroup = imageGroup;
    }
    
    public void setRectangle(ResizableRectangle rectangle) {
    	this.rectangle = rectangle;
    }
    
    public void setIndex(Integer index) {
    	this.index = index;
    }
    /**
     * Returns rectangleWrapper if ok is clicked, null otherwise
     * 
     * @return
     */
    public ResizableRectangleWrapper isOkClicked() {
        if(okClicked) {
        	return rectangleWrapper;
        }
        else
        	return null;
    }
    
    
    /**
     * Method which is called when user clicks cancel.
     */
    @FXML
    private void handleCancel() {
    	dialogStage.close();
    	imageGroup.getChildren().remove(imageGroup.getChildren().size() - 10, imageGroup.getChildren().size());
    	
    }
    
    @FXML
    private void handleOK() {
    	rectangleWrapper = new ResizableRectangleWrapper(rectangle, index);
    	String className = listOfAnnotations.getSelectionModel().getSelectedItem();
    	if( className == null || className.length() == 0) {
    		className = mainApp.getDefaultClass();
    	}
    	rectangleWrapper.setKlass(className);
    	rectangleWrapper.setAdditionalText(additionalText.getText());
    	rectangleWrapper.setDifficult(difficult.isSelected());
    	rectangleWrapper.setTruncated(truncated.isSelected());
    	rectangleWrapper.setOverlap(overlap.isSelected());
    	okClicked = true;
    	dialogStage.close();
    	
    }
    
    
}

