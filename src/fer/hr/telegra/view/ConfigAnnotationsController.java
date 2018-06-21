package fer.hr.telegra.view;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import fer.hr.telegra.MainApp;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
/**
 * Controller class for configuration of annotations options
 * @author dmlinaric
 *
 */
public class ConfigAnnotationsController {
	/**
	 * Reference to mainApp
	 */
	private MainApp mainApp;
	/**
	 * Stage of this dialog window
	 */
	private Stage dialogStage;
	/**
	 * Listview with all possible annotation classes in application
	 */
	@FXML
    private ListView<String>  listOfAnnotations;
	/**
	 * Label with selected annotation class
	 */
	@FXML
	private Label annotationLabel;
	/**
	 * Colorpicker for selected annotation fill color
	 */
	@FXML
	private ColorPicker colorPicker;
	/**
	 * Colorpicker for color of all annotation classes (default)
	 */
	@FXML
	private ColorPicker allColorPicker;
	@FXML
	/**
	 * Colorpicker for color of all annotation border
	 */
	private ColorPicker allBorderColorPicker;
	/**
	 * Colorpicker for color of al flags
	 */
	@FXML
	private ColorPicker flagsColorPicker;
	/**
	 * Combobox with all possible flags
	 */
	@FXML
	private ComboBox<String> flagsColor = new ComboBox<>();
	/**
	 * Button for making selected annotation class default
	 */
	@FXML
	private Button defaultClassButton;
	/**
	 * Textfield for name of new annotation class
	 */
	@FXML
	private TextField newAnnotation;
	
	/**
	 * Give controller access to mainApp
	 * Populate data with data from mainApp
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		listOfAnnotations.setEditable(true);
		listOfAnnotations.setItems(mainApp.getAnnotations());
		defaultClassButton.setDisable(true);
		allColorPicker.setValue(mainApp.getDefaultColor());
		allBorderColorPicker.setValue(mainApp.getDefaultBorderColor());
    }
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
		this.dialogStage.setResizable(false);
	}
	
	/**
	 * initialize controller class
	 */
	@FXML
    private void initialize() {
		colorPicker.setDisable(true);
		listOfAnnotations.setCellFactory(param -> new DragNDropCell());
		listOfAnnotations.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				annotationLabel.setText(newValue);
				colorPicker.setValue(mainApp.getColorOfClasses().get(newValue).get());
				if (!newValue.equals(mainApp.getDefaultClass())) {
					defaultClassButton.setDisable(false);
				} else {
					defaultClassButton.setDisable(true);
				}
				colorPicker.setDisable(false);
			}
			else {
				annotationLabel.setText("");
				defaultClassButton.setDisable(true);
				colorPicker.setDisable(true);
			}
		});
		flagsColor.getItems().add("Overlap");
		flagsColor.getItems().add("Truncated");
		flagsColor.getItems().add("Overlap&Truncated");
		flagsColor.getItems().add("Difficult");
	}
	
	@FXML
	private void handleDefaultClassButton() {
		mainApp.setDefaultClass(annotationLabel.getText());
		defaultClassButton.setDisable(true);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("config/default.txt"));
			writer.write("class: " + mainApp.getDefaultClass());
			writer.newLine();
			writer.write("color: " + mainApp.getDefaultColor());
			writer.newLine();
			writer.write("border: " + mainApp.getDefaultBorderColor());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleColorSelect() {
		Color color = colorPicker.getValue();
		mainApp.getColorOfClasses().get(annotationLabel.getText()).set(color);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("config/annotations_colors.txt"));
			mainApp.getColorOfClasses().forEach( (k,v) ->  {
				try {
					writer.write(k + " : " + v.get() );
					writer.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		     
		    writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	    try {
			mainApp.openController.refreshImage();
		} catch (NullPointerException e) {
		}
	}
	
	@FXML
	private void handleAllColorSelect() {
		mainApp.getColorOfClasses().forEach( (k,v) ->  {
			v.set(allColorPicker.getValue());
		});
		mainApp.setDefaultColor(allColorPicker.getValue());
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("config/annotations_colors.txt"));
			mainApp.getColorOfClasses().forEach( (k,v) ->  {
				try {
					writer.write(k + " : " + v.get() );
					writer.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		    writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("config/default.txt"));
			writer.write("class: " + mainApp.getDefaultClass());
			writer.newLine();
			writer.write("color: " + mainApp.getDefaultColor());
			writer.newLine();
			writer.write("border: " + mainApp.getDefaultBorderColor());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    mainApp.openController.refreshImage();
	}
	
	@FXML
	private void handleAllBorderColorSelect() {
		mainApp.setDefaultBorderColor(allBorderColorPicker.getValue());
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("config/default.txt"));
			writer.write("class: " + mainApp.getDefaultClass());
			writer.newLine();
			writer.write("color: " + mainApp.getDefaultColor());
			writer.newLine();
			writer.write("border: " + mainApp.getDefaultBorderColor());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    mainApp.openController.refreshImage();
	}
	
	@FXML
	private void handleAddNewAnnotation() {
		mainApp.getAnnotations().add(newAnnotation.getText());
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("config/annotations_config.txt"));
			for (String annotation: mainApp.getAnnotations()) {
				writer.write(annotation);
				writer.newLine();
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mainApp.getColorOfClasses().put(newAnnotation.getText(), new SimpleObjectProperty<Color>(mainApp.getDefaultColor()));
		//mainApp.getColorOfClasses().get(newAnnotation.getText()).set(mainApp.getDefaultColor());
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("config/annotations_colors.txt"));
			mainApp.getColorOfClasses().forEach( (k,v) -> {
				try {
					writer.write(k + " : " + v.get());
					writer.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void flagsSelect() {
		flagsColorPicker.setValue(mainApp.getColorsOfFlags().get(flagsColor.getValue()).get());
	}
	
	@FXML
	private void handleFlagsColorSelect() {
		Color color = flagsColorPicker.getValue();
		mainApp.getColorsOfFlags().get(flagsColor.getValue()).set(color);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("config/flags_color.txt"));
			mainApp.getColorsOfFlags().forEach( (k,v) ->  {
				try {
					writer.write(k + " : " + v.get() );
					writer.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		    writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		mainApp.openController.refreshImage();
	}
	
	@FXML
	private void handleClose() {
		dialogStage.close();
	}
	
	/**
	 * Inner class that represent new list cell with drag and drop implemented
	 * @author dmlinaric
	 *
	 */
	private class DragNDropCell extends ListCell<String> {
		public DragNDropCell() {
			setOnDragDetected(event -> {
				if(getItem() == null) {
					return;
				}
				
				Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(getItem());
                dragboard.setContent(content);
                event.consume();
			});
			setOnDragOver(event -> {
                if (event.getGestureSource() != this &&
                       event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }

                event.consume();
            });
            setOnDragEntered(event -> {
                if (event.getGestureSource() != this &&
                        event.getDragboard().hasString()) {
                    setOpacity(0.3);
                }
            });
            setOnDragExited(event -> {
                if (event.getGestureSource() != this &&
                        event.getDragboard().hasString()) {
                    setOpacity(1);
                }
            });
            setOnDragDropped(event -> {
            	if(getItem() == null) {
            		return;
            	}
            	Dragboard db = event.getDragboard();
                boolean success = false;
                
                if (db.hasString()) {
                    ObservableList<String> items = getListView().getItems();
                    int draggedIdx = items.indexOf(db.getString());
                    int thisIdx = items.indexOf(getItem());

                    items.set(draggedIdx, getItem());
                    items.set(thisIdx, db.getString());

                    ObservableList<String> itemscopy = FXCollections.observableArrayList(getListView().getItems());
                    getListView().getItems().setAll(itemscopy);
                    mainApp.getAnnotations().setAll(itemscopy);
                    
                    /**
                     * When drag is droped then save new order to the config file
                     */
                    try {
            			BufferedWriter writer = new BufferedWriter(new FileWriter("config/annotations_config.txt"));
            			for(String annotation: mainApp.getAnnotations()) {
            				writer.write(annotation);
        					writer.newLine();
            			}
            		    writer.close();
            		} catch(IOException e) {
            			e.printStackTrace();
            		}
                    
                    success = true;
                }
                
                event.setDropCompleted(success);

                event.consume();
            });
            setOnDragDone(DragEvent::consume);
		}
		@Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty | item != null) {
            	this.setText(item);
            }
            else {
            	this.setText(null);
            }
        }
	}
}
