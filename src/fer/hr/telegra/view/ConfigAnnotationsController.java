package fer.hr.telegra.view;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import fer.hr.telegra.MainApp;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ConfigAnnotationsController {

	private MainApp mainApp;
	private Stage dialogStage;
	@FXML
    private ListView<String>  listOfAnnotations;
	@FXML
	private Label annotationLabel;
	@FXML
	private ColorPicker colorPicker;
	@FXML
	private ColorPicker allColorPicker;
	@FXML
	private ColorPicker allBorderColorPicker;
	@FXML
	private ColorPicker flagsColorPicker;
	@FXML
	private ComboBox<String> flagsColor = new ComboBox<>();
	@FXML
	private Button defaultClassButton;
	@FXML
	private TextField newAnnotation;
	
	
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
	
	@FXML
    private void initialize() {
		colorPicker.setDisable(true);
		listOfAnnotations.getSelectionModel().selectedItemProperty()
		.addListener((observable, oldValue, newValue) -> {
			annotationLabel.setText(newValue);
			colorPicker.setValue(mainApp.getColorOfClasses().get(newValue).get());
			if(!newValue.equals(mainApp.getDefaultClass())) {
				defaultClassButton.setDisable(false);
			}
			else {
				defaultClassButton.setDisable(true);
			}
			colorPicker.setDisable(false);
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
		//mainApp.getColorOfClasses().replaceAll((k, v) -> allColorPicker.getValue());
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
		//mainApp.getColorOfClasses().put(newAnnotation.getText(), mainApp.getDefaultColor());
		mainApp.getColorOfClasses().get(newAnnotation.getText()).set(mainApp.getDefaultColor());
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("config/annotations_colors.txt"));
			mainApp.getColorOfClasses().forEach( (k,v) -> {
				try {
					writer.write(k + " : " + v);
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
}
