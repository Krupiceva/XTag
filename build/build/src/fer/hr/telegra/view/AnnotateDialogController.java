package fer.hr.telegra.view;

import java.io.IOException;

import fer.hr.telegra.MainApp;
import fer.hr.telegra.model.DataSet;
import fer.hr.telegra.model.ResizableRectangle;
import fer.hr.telegra.model.ResizableRectangleWrapper;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AnnotateDialogController {
	@FXML
    private ListView<String>  listOfAnnotations;
	@FXML
	private ListView<String> listOfCollors;
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
    	
    	if(listOfCollors.getSelectionModel().getSelectedItem() != null) {
    		rectangleWrapper.setColor(listOfCollors.getSelectionModel().getSelectedItem());
    	}
    	
    	okClicked = true;
    	dialogStage.close();
    	
    }
    
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

