package fer.hr.telegra.view;

import fer.hr.telegra.MainApp;
import fer.hr.telegra.model.DataSet;
import fer.hr.telegra.model.ResizableRectangle;
import fer.hr.telegra.model.ResizableRectangleWrapper;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
/**
 * Controller class for annotating the object in image
 * @author dmlinaric
 *
 */
public class AnnotateDialogController {
	/**
	 * Listview with all possible annotations class
	 */
	@FXML
    private ListView<String>  listOfAnnotations;
	/**
	 * Listview with all possible annotations color
	 */
	@FXML
	private ListView<String> listOfCollors;
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
	 * Stage of this dialog windows
	 */
	private Stage dialogStage;
	/**
	 * reference to mainApp
	 */
	private MainApp mainApp;
	/**
	 * Reference to group with image and other annotations
	 */
	private Group imageGroup;
	/**
	 * Reference to bounding box around annotation
	 */
	private ResizableRectangle rectangle;
	/**
	 * Wrapper around rectangle with all information needed to be whole annotation
	 */
	private ResizableRectangleWrapper rectangleWrapper;
	/**
	 * Index of annotation rectangle in group
	 */
	private Integer index;
    private boolean okClicked = false;
    
	public AnnotateDialogController() {
	}
	
	@FXML
	private void initialize() {
//		listOfCollors.setCellFactory(list -> new ListCell<String>() {
//			@Override
//			protected void updateItem(String item, boolean empty) {
//				super.updateItem(item, empty);
//				if(!empty | item != null) {
//	            	this.setText(item);
//	            	//this.textFillProperty().bind(mainApp.getColorOfClasses().get(item));
//	            	String color = mainApp.getCollors().get(item).get().toString();
//	            	color = color.substring(2);
//	            	color = "#" + color;
//	            	
//	            	java.awt.Color oldColor = toSwingColor(mainApp.getCollors().get(item).get());
//	            	String textColor = getContrastColor(oldColor).toString();
//	            	textColor = textColor.substring(2);
//	            	textColor = "#" + textColor;
//	            	
//	            	String style = "-fx-background-color: " + color + ";" + "\n-fx-text-fill: " + textColor + ";";
//	            	this.setStyle(style);
//	            	this.setOnMousePressed(new EventHandler<MouseEvent>() {
//	                    @Override public void handle(MouseEvent mouseEvent) {
//	                      setStyle("-fx-background-color: #0096C9;\n-fx-text-fill: #ffffffff");
//	                      }
//	                    });
//	            }
//	            else {
//	            	this.setText(null);
//	            	this.setStyle("");
//	            }
//			}
//		});
//		
//		listOfCollors.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//			System.out.println(newValue);
//		});
	}
	
	/**
     * Sets the stage of this dialog.
     * Adding keyboard shortcuts to the scene of this stage
     * @param dialogStage is stage to set
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.setResizable(false);
        //If dialog is closed, remove this new potential annotation rectangle from group
        this.dialogStage.setOnCloseRequest(event -> {
        	imageGroup.getChildren().remove(imageGroup.getChildren().size() - 10, imageGroup.getChildren().size());
        });
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
            	if (event.getCode() == KeyCode.ENTER) {
                	handleOK();
                }
            	else if(event.getCode() == KeyCode.R) {
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
                else if (shift1.match(event)) {
                	listOfCollors.getSelectionModel().select(0);
				}
                else if (shift2.match(event)) {
                	listOfCollors.getSelectionModel().select(1);
				}
                else if (shift3.match(event)) {
                	listOfCollors.getSelectionModel().select(2);
				}
                else if (shift4.match(event)) {
                	listOfCollors.getSelectionModel().select(3);
				}
                else if (shift5.match(event)) {
                	listOfCollors.getSelectionModel().select(4);
				}
                else if (shift6.match(event)) {
                	listOfCollors.getSelectionModel().select(5);
				}
                else if (shift7.match(event)) {
                	listOfCollors.getSelectionModel().select(6);
				}
                else if (shift8.match(event)) {
                	listOfCollors.getSelectionModel().select(7);
				}
                else if (shift9.match(event)) {
                	listOfCollors.getSelectionModel().select(8);
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
    
    /**
     * Give access to the mainApp.
     * Populate lists with data from mainApp
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
    	this.mainApp = mainApp;
        listOfAnnotations.setEditable(true);
        listOfAnnotations.setItems(mainApp.getAnnotations());
        listOfCollors.setEditable(true);
        listOfCollors.setItems(mainApp.getColors());
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
    	//remove this new potential annotation rectangle from group
    	imageGroup.getChildren().remove(imageGroup.getChildren().size() - 10, imageGroup.getChildren().size());
    	
    }
    
    /**
     * Add all information to the new annotation
     */
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
    	
    	if(listOfCollors.getSelectionModel().getSelectedItem() != null) {
    		rectangleWrapper.setColor(listOfCollors.getSelectionModel().getSelectedItem());
    	}
    	
    	okClicked = true;
    	dialogStage.close();
    	
    }
    
    /**
     * Helper methods for color manipulation, not used in current version
     */
    public static Color getContrastColor(java.awt.Color color) {
    	  double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
    	  return y >= 128 ? Color.BLACK : Color.WHITE;
    	}
    
    public static java.awt.Color toSwingColor(Color fxColor) {
        final float r = (float) fxColor.getRed();
        final float g = (float) fxColor.getGreen();
        final float b = (float) fxColor.getBlue();
        final float a = (float) fxColor.getOpacity();
        return new java.awt.Color(r, g, b, a);
    }
    
}

