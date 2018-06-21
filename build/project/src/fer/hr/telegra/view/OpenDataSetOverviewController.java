package fer.hr.telegra.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.security.auth.callback.Callback;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.magicwerk.brownies.collections.BigList;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fer.hr.telegra.MainApp;
import fer.hr.telegra.model.ChangeableHistory;
import fer.hr.telegra.model.DataImage;
import fer.hr.telegra.model.DataSet;
import fer.hr.telegra.model.ImageQuality;
import fer.hr.telegra.model.Operation;
import fer.hr.telegra.model.OperationRectangleWrappers;
import fer.hr.telegra.model.ResizableRectangle;
import fer.hr.telegra.model.ResizableRectangleWrapper;
import fer.hr.telegra.model.TooltippedTableCell;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;

public class OpenDataSetOverviewController {

	// Reference to the main application.
	private MainApp mainApp;

	private DataSet dataSet;

	@FXML
	private SplitPane splitPaneHor;
	@FXML
	private ListView<String> listOfImg;
	@FXML
	private ListView<String> listOfImgWithAnnotations;
	@FXML
	private ListView<String> listOfVerImg;
	@FXML
	private TableView<ResizableRectangleWrapper> annotationsTable;
	@FXML
	private TableColumn<ResizableRectangleWrapper, String> nameColumn;
	@FXML
	private TableColumn<ResizableRectangleWrapper, String> textColumn;
	@FXML
	private TableColumn<ResizableRectangleWrapper, Integer> xMinColumn;
	@FXML
	private TableColumn<ResizableRectangleWrapper, Integer> yMinColumn;
	@FXML
	private TableColumn<ResizableRectangleWrapper, Integer> xMaxColumn;
	@FXML
	private TableColumn<ResizableRectangleWrapper, Integer> yMaxColumn;
	@FXML
	private ImageView imageView;
	//ImageView imageView = new ImageView();
	@FXML
	private AnchorPane anchorPaneOfImg;
	@FXML
	private HBox hbox;
	@FXML
	private BorderPane borderPaneOfImg;
	@FXML
	private ToggleButton tag;
	@FXML
	private Button editButton;
	@FXML
	private Button deleteButton;
	@FXML
	private Button previousButton;
	@FXML
	private Button nextButton;
	@FXML
	private Button zoomInButton;
	@FXML
	private Button zoomOutButton;
	@FXML
	private Button fitZoomButton;
	@FXML
	private Button fitToScreenButton;
	@FXML
	private CheckBox lockZoomLevel;
	@FXML
	private Button saveButton;
	@FXML
	private Button backButton;
	@FXML
	private ToggleButton poly;
	@FXML
	private Group imageGroup;
	//Group imageGroup = new Group();
	@FXML
	private VBox vBoxOfImg;
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private CheckBox aspectRatioLock;
	@FXML
	private TextField xField;
	@FXML
	private TextField yField;
	//@FXML
	//private ScrollPane scrollPaneOfImg = new ScrollPane();
	@FXML
	private Label images;
	@FXML
	private Label ann;
	@FXML
	private Label legendOverlap;
	@FXML
	private Label legendTruncated;
	@FXML
	private Label legendOverlapTruncated;
	@FXML
	private Label legendDifficult;
	@FXML
	private Label numberOfAnnotationsInSetLabel;
	@FXML
	private CheckBox editModeCheckBox;
	private Integer numberOfAnnotationsInSet;
	StackPane imageHolder = new StackPane();

	//ScrollPane scrollPane = new ScrollPane();
	ScrollPane scroller = new ScrollPane();
	
	ChangeableHistory history = ChangeableHistory.getInstance();
	Image image;
	ToggleGroup toggleGroup = new ToggleGroup();;
	ContextMenu contextMenu;
	MenuItem annotateMenuItem;
	MenuItem cropMenuItem;
	final DoubleProperty zoomProperty = new SimpleDoubleProperty(200);
	Bounds viewPortLockZoomBounds;
	boolean lockFlag = true;
	int brojac = 0;

	ObservableList<String> imgs = FXCollections.observableArrayList();
	ObservableList<String> imgsWithAnnotations = FXCollections.observableArrayList();
	ObservableList<String> verImgs = FXCollections.observableArrayList();

	private ObservableList<ResizableRectangleWrapper> annotations = FXCollections.observableArrayList();

	final Selection areaSelection = new Selection();
	int index = 0;
	
	private double aspectRatio = (double)0;
	boolean isAspectSet = false; 

	public OpenDataSetOverviewController() {
	}

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml file has been loaded. sets divider 15%/85% but it is resizable
	 */
	@FXML
	private void initialize() {
		System.gc();
		history.resetHistory();
		splitPaneHor.setDividerPositions(0.15);
		splitPaneHor.lookupAll(".split-pane-divider").stream().forEach(div -> div.setMouseTransparent(true));
		
		//nameColumn.setCellFactory(TooltippedTableCell.forTableColumn());
		textColumn.setCellValueFactory(cellData -> cellData.getValue().additionalTextProperty());
		textColumn.setCellFactory(TooltippedTableCell.forTableColumn());
		xMinColumn.setCellValueFactory(cellData -> cellData.getValue().xMinProperty().asObject());
		yMinColumn.setCellValueFactory(cellData -> cellData.getValue().yMinProperty().asObject());
		xMaxColumn.setCellValueFactory(cellData -> cellData.getValue().xMaxProperty().asObject());
		yMaxColumn.setCellValueFactory(cellData -> cellData.getValue().yMaxProperty().asObject());

		nameColumn.setCellFactory(column -> {
		    return new TableCell<ResizableRectangleWrapper, String>() {
		        @Override
		        protected void updateItem(String item, boolean empty) {
		            super.updateItem(item, empty);
		            if(!empty | item != null) {
		            	this.setText(item);
		            	//this.textFillProperty().bind(mainApp.getColorOfClasses().get(item));
		            	String color = mainApp.getColorOfClasses().get(item).get().toString();
		            	color = color.substring(2);
		            	color = "#" + color;
		            	String style = "-fx-background-color: " + color;
		            	this.setStyle(style);
		            }
		            else {
		            	this.setText(null);
		            	this.setStyle("");
		            }
		        }
		    };
		});
		
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().klassProperty());
		
		// Add listener for which image need to be display in imageview
		listOfImg.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				listOfImgWithAnnotations.getSelectionModel().select(-1);
				listOfVerImg.getSelectionModel().select(-1);
				showImage(newValue);
			}
			else {
				if(imgs.isEmpty()) {
					listOfImgWithAnnotations.getSelectionModel().select(imgsWithAnnotations.size()-1);
					listOfImgWithAnnotations.getFocusModel()
					.focus(listOfImgWithAnnotations.getSelectionModel().getSelectedIndex());
			listOfImgWithAnnotations.scrollTo(listOfImgWithAnnotations.getSelectionModel().getSelectedIndex());
				}
			}
			
		});
		listOfImgWithAnnotations.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {
					if (newValue != null) {
						listOfImg.getSelectionModel().select(-1);
						listOfVerImg.getSelectionModel().select(-1);
					}
					showImage(newValue);
				});
		listOfVerImg.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				listOfImg.getSelectionModel().select(-1);
				listOfImgWithAnnotations.getSelectionModel().select(-1);
			}
			showImage(newValue);
		});
		// Add listener for which rectangle to select
		annotationsTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) ->{ 
					showRectangle(newValue, oldValue);
				});
		annotationsTable.setPlaceholder(new Label("No annotations"));
		


		// Create icons for buttons and labels
		Image imageTag = new Image(getClass().getResourceAsStream("/Actions-insert-text-icon.png"));
		Image imageEdit = new Image(getClass().getResourceAsStream("/Actions-select-rectangular-icon.png"));
		Image imageDelete = new Image(getClass().getResourceAsStream("/Actions-edit-delete-icon.png"));
		Image imagePrevious = new Image(getClass().getResourceAsStream("/Actions-go-previous-icon.png"));
		Image imageNext = new Image(getClass().getResourceAsStream("/Actions-go-next-icon.png"));
		Image imageZoomIn = new Image(getClass().getResourceAsStream("/Actions-zoom-in-icon.png"));
		Image imageZoomOut = new Image(getClass().getResourceAsStream("/Actions-zoom-out-icon.png"));
		Image imageFitZoom = new Image(getClass().getResourceAsStream("/Actions-zoom-original-icon.png"));
		Image imageSave = new Image(getClass().getResourceAsStream("/Actions-document-save-icon.png"));
		Image imageBack = new Image(getClass().getResourceAsStream("/Actions-document-revert-icon.png"));
		Image imageAnn = new Image(getClass().getResourceAsStream("/Actions-edit-select-all-icon.png"));
		Image imageImages = new Image(getClass().getResourceAsStream("/Mimetypes-image-x-generic-icon.png"));
		Image imageFitToScreenZoom = new Image(getClass().getResourceAsStream("/Actions-page-zoom-icon.png"));

		tag.setGraphic(new ImageView(imageTag));
		tag.setDisable(true);
		editButton.setGraphic(new ImageView(imageEdit));
		editButton.setDisable(true);
		deleteButton.setGraphic(new ImageView(imageDelete));
		deleteButton.setDisable(true);
		previousButton.setGraphic(new ImageView(imagePrevious));
		nextButton.setGraphic(new ImageView(imageNext));
		nextButton.setMnemonicParsing(true);
		zoomInButton.setGraphic(new ImageView(imageZoomIn));
		zoomOutButton.setGraphic(new ImageView(imageZoomOut));
		fitZoomButton.setGraphic(new ImageView(imageFitZoom));
		fitToScreenButton.setGraphic(new ImageView(imageFitToScreenZoom));
		//saveButton.setGraphic(new ImageView(imageSave));
		//saveButton.setDisable(true);
		backButton.setGraphic(new ImageView(imageBack));
		//images.setGraphic(new ImageView(imageImages));
		//ann.setGraphic(new ImageView(imageAnn)); 
		
		/**
		 * zoomProperty.addListener(new InvalidationListener() {
		 * 
		 * @Override public void invalidated(Observable arg0) {
		 *           imageView.setFitWidth(zoomProperty.get() * 3);
		 *           //imageView.setFitHeight(zoomProperty.get() * 3); } });
		 * 
		 *           scrollPaneOfImg.addEventFilter(ScrollEvent.ANY, new
		 *           EventHandler<ScrollEvent>() {
		 * @Override public void handle(ScrollEvent event) { if (event.getDeltaY() > 0)
		 *           { zoomProperty.set(zoomProperty.get() * 1.1); } else if
		 *           (event.getDeltaY() < 0) { zoomProperty.set(zoomProperty.get() /
		 *           1.1); } } });
		 */

		
		
		tag.setToggleGroup(toggleGroup);
		// Add listener for toggle button
		// If is pressed enable drawing a recatngle, if is not disable
		tag.selectedProperty().addListener(((observable, oldValue, newValue) -> {
			if (newValue) {
				areaSelection.selectArea(imageGroup);
			} else {
				areaSelection.deselectArea();
			}
		}));

//		borderPaneOfImg.widthProperty().addListener((observable, oldValue, newValue) -> {
//			System.out.println(newValue);
//			
//		});
		
	}
	
	public DataSet getDataSet() {
		return dataSet;
	}
	
	public ResizableRectangleWrapper getSelectedAnnotation() {
		return annotationsTable.getSelectionModel().getSelectedItem();
	}
	
	public void refreshImage() {
		if (listOfImg.getSelectionModel().getSelectedItem() != null) {
			int index = listOfImg.getSelectionModel().getSelectedIndex();
			listOfImg.getSelectionModel().select(-1);
			listOfImg.getSelectionModel().select(index);
		} else if (listOfImgWithAnnotations.getSelectionModel().getSelectedItem() != null) {
			int index = listOfImgWithAnnotations.getSelectionModel().getSelectedIndex();
			listOfImgWithAnnotations.getSelectionModel().select(-1);
			listOfImgWithAnnotations.getSelectionModel().select(index);
		} else {
			int index = listOfVerImg.getSelectionModel().getSelectedIndex();
			listOfVerImg.getSelectionModel().select(-1);
			listOfVerImg.getSelectionModel().select(index);
		}
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		KeyCombination cntrlS = new KeyCodeCombination(KeyCode.S, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlZ = new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.CONTROL_DOWN);
		KeyCombination cntrlY = new KeyCodeCombination(KeyCode.Y, KeyCodeCombination.CONTROL_DOWN);
		this.mainApp.getPrimaryStage().getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(cntrlZ.match(event)) {
					//handleUndo();
				} else if (cntrlY.match(event)) {
					//handleRedo();
				} else if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) {
					handlePrevious();
				} else if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) {
					handleNext();
				} else if (event.getCode() == KeyCode.V) {
					handleVerified();
				} else if (event.getCode() == KeyCode.T) {
					testPrint();
				} else if (cntrlS.match(event)) {
					handleSaveButton();
				} else if (event.getCode() == KeyCode.DELETE) {
					if (annotationsTable.getSelectionModel().getSelectedItem() != null
							&& !deleteButton.disabledProperty().get()) {
						handleDeleteButton();
					}
				}
			}
		});
		
		legendOverlap.textFillProperty().bind(mainApp.getColorsOfFlags().get("Overlap"));
		legendTruncated.textFillProperty().bind(mainApp.getColorsOfFlags().get("Truncated"));
		legendDifficult.textFillProperty().bind(mainApp.getColorsOfFlags().get("Difficult"));
		legendOverlapTruncated.textFillProperty().bind(mainApp.getColorsOfFlags().get("Overlap&Truncated"));
	}

	/**
	 * Set the dataset and set dataset images in list Set custom cell factory for
	 * lists (adding context menu for deleting)
	 * 
	 * @param dataSet
	 *            is the current open dataset
	 */
	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
		updateAnnotationsNumber();
		BigList<DataImage> images = BigList.create(dataSet.getDataSetImages());
		Iterator<DataImage> itr = images.iterator();
		while (itr.hasNext()) {
			DataImage img = itr.next();
			imgs.add(img.getImageName());
		}
		images = null;
		images = BigList.create(dataSet.getDataSetImagesWithAnnotations());
		itr = images.iterator();
		while (itr.hasNext()) {
			DataImage img = itr.next();
			imgsWithAnnotations.add(img.getImageName());
		}
		images = null;
		images = BigList.create(dataSet.getDataSetVerifiedImages());
		itr = images.iterator();
		while (itr.hasNext()) {
			DataImage img = itr.next();
			verImgs.add(img.getImageName());
		}
		if (!imgs.isEmpty()) {
			listOfImg.setItems(imgs);
		}
		if (!imgsWithAnnotations.isEmpty()) {
			listOfImgWithAnnotations.setItems(imgsWithAnnotations);
		}
		if (!verImgs.isEmpty()) {
			listOfVerImg.setItems(verImgs);
		}

		// Custom cell factory for list of images

		listOfImg.setCellFactory(lv -> {
			ListCell<String> cell = new ListCell<>();
			ContextMenu contextMenu = new ContextMenu();

			MenuItem deleteImg = new MenuItem();
			deleteImg.textProperty().bind(Bindings.format("Delete \"%s\"", cell.itemProperty()));
			deleteImg.setOnAction(event -> {
				String item = cell.getItem();
				dataSet.removeDataSetImage(item);
				String path = dataSet.getDataSetImagesLocation() + "\\" + item;
				File file = new File(path);
				if (file.delete()) {

				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(mainApp.getPrimaryStage());
					alert.setTitle("Error!");
					alert.setHeaderText("Error deleting image file");
					alert.setContentText("Delete operation is failed!");

					alert.showAndWait();
				}
				path = dataSet.getDataSetAnnotationsLocation() + "\\" + item;
				if (path.contains(".")) {
					path = path.substring(0, path.lastIndexOf('.'));
				}
				path = path + ".xml";
				file = new File(path);
				if (file.exists()) {
					if (file.delete()) {

					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.initOwner(mainApp.getPrimaryStage());
						alert.setTitle("Error!");
						alert.setHeaderText("Error deleting xml file");
						alert.setContentText("Delete operation is failed!");

						alert.showAndWait();
					}
				}
				listOfImg.getItems().remove(item);
				file = MainApp.getLastFilePath();
				mainApp.saveDataSetsToFile(file);

			});
			MenuItem openImage = new MenuItem();
			openImage.setText("Open image location");
			openImage.setOnAction(event -> {
				String item = cell.getItem();
				String path = dataSet.getDataSetImagesLocation() + "\\" + item;
				try {
					Runtime.getRuntime().exec("explorer.exe /select," + path);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			
			contextMenu.getItems().addAll(deleteImg, openImage);

			cell.textProperty().bind(cell.itemProperty());

			cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
				if (isNowEmpty) {
					cell.setContextMenu(null);
				} else {
					cell.setContextMenu(contextMenu);
				}
			});
			return cell;
		});

		listOfImgWithAnnotations.setCellFactory(lv -> {
			ListCell<String> cell = new ListCell<>();

			ContextMenu contextMenu = new ContextMenu();

			MenuItem deleteImg = new MenuItem();
			deleteImg.textProperty().bind(Bindings.format("Delete \"%s\"", cell.itemProperty()));
			deleteImg.setOnAction(event -> {
				String item = cell.getItem();
				dataSet.removeDataSetImageWithAnnotations(item);
				String path = dataSet.getDataSetImagesLocation() + "\\" + item;
				File file = new File(path);
				if (file.delete()) {

				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(mainApp.getPrimaryStage());
					alert.setTitle("Error!");
					alert.setHeaderText("Error deleting image file");
					alert.setContentText("Delete operation is failed!");

					alert.showAndWait();
				}
				path = dataSet.getDataSetAnnotationsLocation() + "\\" + item;
				if (path.contains(".")) {
					path = path.substring(0, path.lastIndexOf('.'));
				}
				path = path + ".xml";
				file = new File(path);
				if (file.exists()) {
					if (file.delete()) {

					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.initOwner(mainApp.getPrimaryStage());
						alert.setTitle("Error!");
						alert.setHeaderText("Error deleting xml file");
						alert.setContentText("Delete operation is failed!");

						alert.showAndWait();
					}
				}
				listOfImgWithAnnotations.getItems().remove(item);
				file = MainApp.getLastFilePath();
				mainApp.saveDataSetsToFile(file);

			});

			MenuItem openImage = new MenuItem();
			openImage.setText("Open image location");
			openImage.setOnAction(event -> {
				String item = cell.getItem();
				String path = dataSet.getDataSetImagesLocation() + "\\" + item;
				try {
					Runtime.getRuntime().exec("explorer.exe /select," + path);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			
			MenuItem openAnnotation = new MenuItem();
			openAnnotation.setText("Open xml location");
			openAnnotation.setOnAction(event -> {
				String item = cell.getItem();
				String path = dataSet.getDataSetAnnotationsLocation() + "\\" + item;
				if (path.contains(".")) {
					path = path.substring(0, path.lastIndexOf('.'));
				}
				path = path + ".xml";
				try {
					Runtime.getRuntime().exec("explorer.exe /select," + path);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

			contextMenu.getItems().addAll(deleteImg, openImage, openAnnotation);

			cell.textProperty().bind(cell.itemProperty());

			cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
				if (isNowEmpty) {
					cell.setContextMenu(null);
				} else {
					cell.setContextMenu(contextMenu);
				}
			});
			return cell;
		});

		listOfVerImg.setCellFactory(lv -> {
			ListCell<String> cell = new ListCell<>();

			ContextMenu contextMenu = new ContextMenu();

			MenuItem deleteImg = new MenuItem();
			deleteImg.textProperty().bind(Bindings.format("Delete \"%s\"", cell.itemProperty()));
			deleteImg.setOnAction(event -> {
				String item = cell.getItem();
				dataSet.removeDataSetVerifiedImage(item);
				String path = dataSet.getDataSetImagesLocation() + "\\" + item;
				File file = new File(path);
				if (file.delete()) {

				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(mainApp.getPrimaryStage());
					alert.setTitle("Error!");
					alert.setHeaderText("Error deleting image file");
					alert.setContentText("Delete operation is failed!");

					alert.showAndWait();
				}
				path = dataSet.getDataSetAnnotationsLocation() + "\\" + item;
				if (path.contains(".")) {
					path = path.substring(0, path.lastIndexOf('.'));
				}
				path = path + ".xml";
				file = new File(path);
				if (file.exists()) {
					if (file.delete()) {

					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.initOwner(mainApp.getPrimaryStage());
						alert.setTitle("Error!");
						alert.setHeaderText("Error deleting xml file");
						alert.setContentText("Delete operation is failed!");

						alert.showAndWait();
					}
				}
				listOfVerImg.getItems().remove(item);
				file = MainApp.getLastFilePath();
				mainApp.saveDataSetsToFile(file);

			});
			MenuItem openImage = new MenuItem();
			openImage.setText("Open image location");
			openImage.setOnAction(event -> {
				String item = cell.getItem();
				String path = dataSet.getDataSetImagesLocation() + "\\" + item;
				try {
					Runtime.getRuntime().exec("explorer.exe /select," + path);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			MenuItem openAnnotation = new MenuItem();
			openAnnotation.setText("Open xml location");
			openAnnotation.setOnAction(event -> {
				String item = cell.getItem();
				String path = dataSet.getDataSetAnnotationsLocation() + "\\" + item;
				if (path.contains(".")) {
					path = path.substring(0, path.lastIndexOf('.'));
				}
				path = path + ".xml";
				try {
					Runtime.getRuntime().exec("explorer.exe /select," + path);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			
			contextMenu.getItems().addAll(deleteImg, openImage, openAnnotation);

			cell.textProperty().bind(cell.itemProperty());

			cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
				if (isNowEmpty) {
					cell.setContextMenu(null);
				} else {
					cell.setContextMenu(contextMenu);
				}
			});
			return cell;
		});
	}

	/**
	 * Show selected image in imageview It put imageview in group because of
	 * rectangles (taggs) that will be in the same group It put it always on first
	 * place so the rectangle is visible Imagegroup puts in stackpane because only
	 * stackpane by default is in center And stackpane puts in scrollerpane so you
	 * can zoom image it join dataset location and image name to get absolut path
	 * 
	 * @param imageName
	 *            is name of selected image
	 */
	private void showImage(String imageName) {
		if (imageName == null) {
			//clearSelection(imageGroup);
			imageView.setImage(null);
			return;
		}
		//handleFitZoomButton();
		String path = dataSet.getDataSetImagesLocation() + "\\" + imageName;
		clearSelection(imageGroup);
		editButton.setDisable(true);
		deleteButton.setDisable(true);
		annotations.clear();
		tag.setDisable(false);
		//saveButton.setDisable(false);
		try {
			if (imageGroup != null) {
				imageGroup.getChildren().remove(imageView);
				// imageHolder.getChildren().remove(imageGroup);
			}
			FileInputStream stream = new FileInputStream(path);
			image = new Image(stream);
			// imageView = new ImageView(image);
			imageView.setImage(image);
			imageView.fitWidthProperty().bind(image.widthProperty());
			imageView.fitHeightProperty().bind(image.heightProperty());
			imageView.setPreserveRatio(true);
			imageView.setPickOnBounds(true);
			imageGroup.getChildren().add(0, imageView);
			Parent zoomPane = createZoomPane(imageGroup);
			scroller.prefWidthProperty().bind(borderPaneOfImg.widthProperty());
			scroller.prefHeightProperty().bind(borderPaneOfImg.heightProperty());
			scroller.setHvalue(scroller.getHmin() + (scroller.getHmax() - scroller.getHmin()) / 2);
			scroller.setVvalue(scroller.getVmin() + (scroller.getVmax() - scroller.getVmin()) / 2);
			borderPaneOfImg.setCenter(zoomPane);
			//imageHolder.getChildren().add(imageGroup);
			//scrollPaneOfImg.setContent(imageHolder);
			
			//imageView.fitWidthProperty().bind(borderPaneOfImg.widthProperty());
			showAnnotations(imageName);
			if(!lockZoomLevel.isSelected()) {
				handleFitZoomToScreenButton();
			}		
			stream.close();
			System.gc();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Parent createZoomPane(final Group group) {
		final StackPane zoomPane = new StackPane();
		
		zoomPane.getChildren().add(group);
		zoomPane.addEventHandler(MouseEvent.ANY, event -> {
		    if(event.getButton() != MouseButton.MIDDLE) event.consume();
		});
		
		zoomPane.setOnScroll(event -> {
			if(event.isControlDown()) {
				event.consume();
				double factor = event.getDeltaY();
				
				if(factor > 0) {
					handleZoomInButton();
				}
				else if (factor < 0) {
					handleZoomOutButton();
				}
			}
		});

		final Group scrollContent = new Group(zoomPane);
		
		scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		
		scroller.setPannable(true);
		scroller.setContent(scrollContent);

		//System.out.println("MinWidthProperty before: " + zoomPane.minWidthProperty().get());
		zoomPane.minWidthProperty().bind(Bindings.createDoubleBinding(() -> scroller.getViewportBounds().getWidth(), scroller.viewportBoundsProperty()));

		zoomPane.minHeightProperty().bind(Bindings.createDoubleBinding(() -> scroller.getViewportBounds().getHeight(), scroller.viewportBoundsProperty()));
		//System.out.println("MinWidthProperty after: " + zoomPane.minWidthProperty().get());
//		scroller.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
//			@Override
//			public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
//				System.out.println("******************************");
//				System.out.println(oldValue.getWidth());
//				System.out.println(oldValue.getHeight());
//				zoomPane.setMinSize(newValue.getWidth(), newValue.getHeight());
//				System.out.println(newValue.getWidth());
//				System.out.println(newValue.getHeight());
//				System.out.println("******************************");
//			}
//		});

		return scroller;
	}
	
	

	private void showRectangle(ResizableRectangleWrapper rectangleWrapperNew,
			ResizableRectangleWrapper rectangleWrapperOld) {
		if (rectangleWrapperNew != null) {
			rectangleWrapperNew.getRectangle().setFill(Color.WHITE.deriveColor(0, 1.2, 1, 0.2));
		}
		if (rectangleWrapperOld != null) {
			rectangleWrapperOld.getRectangle()
					.setFill(mainApp.getColorOfClasses().get(rectangleWrapperOld.getKlass()).get());

		}
		editButton.setDisable(false);
		deleteButton.setDisable(false);
		if (editModeCheckBox.isSelected()) {
			if (rectangleWrapperNew != null) {
				int index = rectangleWrapperNew.getIndex();
				int start = index + (index - 1) * 9;
				int end = start + 10;
				for (int i = 1; i < imageGroup.getChildren().size(); i++) {
					if (i >= start && i < end) {
						imageGroup.getChildren().get(i).setVisible(true);
					} else {
						imageGroup.getChildren().get(i).setVisible(false);
					}
				}
				handleEditButton();
			}
		}
		else {
			for(javafx.scene.Node node: imageGroup.getChildren()) {
				node.setVisible(true);
			}
		}
	}

	private void showAnnotations(String imageName) {
		String name;
		String color = "";
		String text = "";
		String truncated = "0";
		String difficult = "0";
		String overlap = "0";
		Double aspect_ratio;
		String xmin;
		String ymin;
		String xmax;
		String ymax;
		ResizableRectangle rect;
		ResizableRectangleWrapper rectWrap;
		Integer index = 0;
		String annotationPath = dataSet.getDataSetAnnotationsLocation() + "\\" + imageName;
		if (annotationPath.contains(".")) {
			annotationPath = annotationPath.substring(0, annotationPath.lastIndexOf('.'));
		}
		annotationPath = annotationPath + ".xml";

		try {
			File inputFile = new File(annotationPath);
			if (inputFile.isFile()) {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(inputFile);
				doc.getDocumentElement().normalize();

				NodeList nList = doc.getElementsByTagName("object");
				index = 0;
				for (int i = 0; i < nList.getLength(); i++) {
					index = index + 1;
					Node nNode = nList.item(i);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						name = eElement.getElementsByTagName("name").item(0).getTextContent().trim();
						NodeList temp = eElement.getElementsByTagName("text");
						if (temp.getLength() > 0) {
							text = eElement.getElementsByTagName("text").item(0).getTextContent().trim();
						}
						temp = eElement.getElementsByTagName("color");
						if (temp.getLength() > 0) {
							color = eElement.getElementsByTagName("color").item(0).getTextContent().trim();
						}
						temp = eElement.getElementsByTagName("truncated");
						if (temp.getLength() > 0) {
							truncated = eElement.getElementsByTagName("truncated").item(0).getTextContent().trim();
						}
						temp = eElement.getElementsByTagName("difficult");
						if (temp.getLength() > 0) {
							difficult = eElement.getElementsByTagName("difficult").item(0).getTextContent().trim();
						}
						temp = eElement.getElementsByTagName("overlap");
						if (temp.getLength() > 0) {
							overlap = eElement.getElementsByTagName("overlap").item(0).getTextContent().trim();
						}
						temp = eElement.getElementsByTagName("aspect_ratio");
						if(temp.getLength() > 0) {
							aspect_ratio = Double.parseDouble(eElement.getElementsByTagName("aspect_ratio").item(0).getTextContent().trim());
						}
						else {
							aspect_ratio = (double) 0;
						}
						
						// System.out.println(name + " " + text + " " + truncated + " " + difficult );
						xmin = eElement.getElementsByTagName("xmin").item(0).getTextContent().trim();
						ymin = eElement.getElementsByTagName("ymin").item(0).getTextContent().trim();
						xmax = eElement.getElementsByTagName("xmax").item(0).getTextContent().trim();
						ymax = eElement.getElementsByTagName("ymax").item(0).getTextContent().trim();
						// System.out.println(xmin + " " + ymin + " " + xmax + " " + ymax);
						Double width = Double.parseDouble(xmax) - Double.parseDouble(xmin);
						Double height = Double.parseDouble(ymax) - Double.parseDouble(ymin);
						rect = new ResizableRectangle(Double.parseDouble(xmin), Double.parseDouble(ymin), width, height, aspect_ratio,
								imageGroup, annotations, annotationsTable, imageView, mainApp);
						rectWrap = new ResizableRectangleWrapper(rect, index);
						rectWrap.setKlass(name);
						rectWrap.setColor(color);
						rectWrap.setAdditionalText(text);
						if (truncated.equals("0")) {
							rectWrap.setTruncated(false);
						} else if (truncated.equals("1")) {
							rectWrap.setTruncated(true);
						}
						if (difficult.equals("0")) {
							rectWrap.setDifficult(false);
						} else if (difficult.equals("1")) {
							rectWrap.setDifficult(true);
						}
						if (overlap.equals("0")) {
							rectWrap.setOverlap(false);
						} else if (overlap.equals("1")) {
							rectWrap.setOverlap(true);
						}
						if (annotations.isEmpty()) {
							annotations.add(rectWrap);
							annotationsTable.setItems(annotations);
						} else {
							annotations.add(rectWrap);
						}
						rectWrap.getRectangle().setFill(mainApp.getColorOfClasses().get(rectWrap.getKlass()).get());
						boolean overlapFlag = rectWrap.getOverlap();
						boolean truncatedFlag = rectWrap.getTruncated();
						boolean difficultFlag = rectWrap.getDifficult();
						if(overlapFlag && truncatedFlag) {
							rectWrap.getRectangle().setStroke(mainApp.getColorsOfFlags().get("Overlap&Truncated").get());
						}
						else if(overlapFlag) {
							rectWrap.getRectangle().setStroke(mainApp.getColorsOfFlags().get("Overlap").get());
						}
						else if(truncatedFlag) {
							rectWrap.getRectangle().setStroke(mainApp.getColorsOfFlags().get("Truncated").get());
						}
						else if(difficultFlag) {
							rectWrap.getRectangle().setStroke(mainApp.getColorsOfFlags().get("Difficult").get());
						}
						else if(overlapFlag == false && truncatedFlag == false && difficultFlag == false) {
							rectWrap.getRectangle().setStroke(mainApp.getDefaultBorderColor());
						}

					}
					this.index = index;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method which is called when user clicks on back to datasets button. Inside
	 * rootlayout puts datasetsoverview
	 */
	@FXML
	private void handleBack() {
		mainApp.showDataSetsOverview();
	}


	@FXML
	private void handleDeleteButton() {

		history.currentIndex++;
		history.operations.add(Operation.delete);
		history.rectangles.add(new OperationRectangleWrappers(null, annotationsTable.getSelectionModel().getSelectedItem()));
		updateAnnotations("Minus");
		int i = annotationsTable.getSelectionModel().getSelectedItem().getIndex();
		int j = i + (i - 1) * 9;
		imageGroup.getChildren().remove(j, j + 10);
		annotations.remove(annotationsTable.getSelectionModel().getSelectedItem());
		annotationsTable.getSelectionModel().clearSelection();
		deleteButton.setDisable(true);
		editButton.setDisable(true);
		for (ResizableRectangleWrapper rect : annotations) {
			if (rect.getIndex() > i) {
				rect.setIndex(rect.getIndex() - 1);
			}
		}

		index = index - 1;
		
		updateAnnotationsNumber();
		handleSaveButton();
	}

	@FXML
	private void handleEditButton() {
		ResizableRectangleWrapper rect = annotationsTable.getSelectionModel().getSelectedItem();

		updateAnnotations("Minus");
		mainApp.showEditAnnotationDialog(rect, rect.getIndex(), annotations);
		
		boolean overlap = rect.getOverlap();
		boolean truncated = rect.getTruncated();
		boolean difficult = rect.getDifficult();
		if(overlap) {
			rect.getRectangle().setStroke(mainApp.getColorsOfFlags().get("Overlap").get());
		}
		else if(truncated) {
			rect.getRectangle().setStroke(mainApp.getColorsOfFlags().get("Truncated").get());
		}
		else if(difficult) {
			rect.getRectangle().setStroke(mainApp.getColorsOfFlags().get("Difficult").get());
		}
		else if(overlap == false && truncated == false && difficult == false) {
			rect.getRectangle().setStroke(mainApp.getDefaultBorderColor());
		}
		
		updateAnnotations("Plus");
		
		handleSaveButton();

	}
	
	public void updateAnnotations(String name) {
		ResizableRectangleWrapper rect = annotationsTable.getSelectionModel().getSelectedItem();
		if (name.equals("Minus")) {
			if(dataSet.getAnnotations().containsKey(rect.getKlass())) {
				int num = dataSet.getAnnotations().get(rect.getKlass());
				num = num - 1;
				if(num == 0) {
					dataSet.getAnnotations().remove(rect.getKlass());
				}
				else {
					dataSet.getAnnotations().put(rect.getKlass(), num);
				}
			}
		}
		else if(name.equals("Plus")) {
			if(dataSet.getAnnotations().containsKey(rect.getKlass())) {
				int num = dataSet.getAnnotations().get(rect.getKlass());
				num = num + 1;
				dataSet.getAnnotations().put(rect.getKlass(), num);
			}
			else {
				dataSet.getAnnotations().put(rect.getKlass(), 1);
			}
		}
	}
	
	public void updateAnnotationsNumber() {
		numberOfAnnotationsInSet = 0;
		dataSet.getAnnotations().forEach((k,v) -> {
			numberOfAnnotationsInSet = numberOfAnnotationsInSet + v;
		});
		numberOfAnnotationsInSetLabel.setText("Total annotations in set: " + numberOfAnnotationsInSet.toString());
	}

	@FXML
	private void handleZoomInButton() {
		imageGroup.setScaleX(imageGroup.getScaleX() * 1.1);
		imageGroup.setScaleY(imageGroup.getScaleY() * 1.1);
	}

	@FXML
	private void handleZoomOutButton() {
		imageGroup.setScaleX(imageGroup.getScaleX() * (1 / 1.1));
		imageGroup.setScaleY(imageGroup.getScaleY() * (1 / 1.1));
	}

	@FXML
	private void handleFitZoomButton() {
		imageGroup.setScaleX(1);
		imageGroup.setScaleY(1);
	}
	
	@FXML
	private void handleFitZoomToScreenButton() {
		double num = borderPaneOfImg.getWidth()/imageView.getFitWidth();
		if(num < 2) {
			//num = num - 0.02;
		}
		else {
			//num = num - 0.04;
		}
		imageGroup.setScaleX(num);
		imageGroup.setScaleY(num);
		if(imageView.getFitHeight()*imageGroup.getScaleY() > borderPaneOfImg.getHeight()) {
			num = borderPaneOfImg.getHeight()/imageView.getFitHeight();
			//num = num - 0.02;
			imageGroup.setScaleX(num);
			imageGroup.setScaleY(num);
		}
		scroller.setHvalue(scroller.getHmin() + (scroller.getHmax() - scroller.getHmin()) / 2);
		scroller.setVvalue(scroller.getVmin() + (scroller.getVmax() - scroller.getVmin()) / 2);
		//scroller.layout();
	}

	@FXML
	private void handleWindowZoomButton() {
		Number i = anchorPaneOfImg.getWidth() / imageView.getFitWidth();
		Number j = anchorPaneOfImg.getHeight() / imageView.getFitHeight();
		// double round = Math.round(i * 10.0) / 10.0;
		DecimalFormat df = new DecimalFormat("#.#");
		df.setRoundingMode(RoundingMode.DOWN);
		String roundi = df.format(i.doubleValue());
		roundi = roundi.replaceAll(",", ".");
		Double rdi = Double.valueOf(roundi);
		String roundj = df.format(j.doubleValue());
		roundj = roundj.replaceAll(",", ".");
		//Double rdj = Double.valueOf(roundj);

		imageGroup.setScaleX(rdi);
		imageGroup.setScaleY(rdi);
		// System.out.println(anchorPaneOfImg.getHeight());
	}

	
	private void handleVerified() {
		if (listOfImgWithAnnotations.getSelectionModel().getSelectedItem() != null) {
			if (verImgs.isEmpty()) {
				verImgs.add(listOfImgWithAnnotations.getSelectionModel().getSelectedItem());
				listOfVerImg.setItems(verImgs);
			} else {
				verImgs.add(listOfImgWithAnnotations.getSelectionModel().getSelectedItem());
			}
			String fileName = listOfImgWithAnnotations.getSelectionModel().getSelectedItem();
			imgsWithAnnotations.remove(fileName);
			DataImage img = new DataImage(fileName);
			dataSet.removeDataSetImageWithAnnotations(fileName);
			dataSet.addDataSetVerifiedImage(img);
			String path = dataSet.getDataSetAnnotationsLocation() + "\\" + fileName;
			if (path.contains(".")) {
				path = path.substring(0, path.lastIndexOf('.'));
			}
			path = path + ".xml";
			File file = new File(path);
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(file);
				doc.getDocumentElement().normalize();
				Node value = doc.getFirstChild();
				NamedNodeMap attr = value.getAttributes();
				Node nodeAttr = attr.getNamedItem("verified");
				if(nodeAttr == null) {
					Attr attr1 = doc.createAttribute("verified");
					attr1.setValue("yes");
					Element eElement = (Element) value;
					eElement.setAttributeNode(attr1);
				}
				else {
					nodeAttr.setTextContent("yes");
				}
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
				transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(file);
				transformer.transform(source, result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			file = MainApp.getLastFilePath();
			mainApp.saveDataSetsToFile(file);
		}
	}
	
	@FXML
	public void handleSaveButton() {

		try {
			boolean isVerified = false;
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();

			String imageName;
			int index;
			Element filename = doc.createElement("filename");
			if (listOfImg.getSelectionModel().getSelectedItem() != null) {
				imageName = listOfImg.getSelectionModel().getSelectedItem();
			} else if (listOfImgWithAnnotations.getSelectionModel().getSelectedItem() != null) {
				imageName = listOfImgWithAnnotations.getSelectionModel().getSelectedItem();
				index = listOfImgWithAnnotations.getSelectionModel().getSelectedIndex();
			} else {
				imageName = listOfVerImg.getSelectionModel().getSelectedItem();
				isVerified = true;
			}
			
			Element rootElement = doc.createElement("annotation");
			doc.appendChild(rootElement);
			Attr attr = doc.createAttribute("verified");
			attr.setValue("no");
			rootElement.setAttributeNode(attr);

			Element folder = doc.createElement("folder");
			Path dataSetImagesLocation = Paths.get(dataSet.getDataSetImagesLocation());
			String fold = dataSetImagesLocation.getFileName().toString();
			folder.appendChild(doc.createTextNode(fold));
			rootElement.appendChild(folder);

			filename.appendChild(doc.createTextNode(imageName));
			rootElement.appendChild(filename);

			Element path = doc.createElement("path");
			path.appendChild(doc.createTextNode(dataSetImagesLocation + "\\" + imageName));
			rootElement.appendChild(path);

			Element sourceNode = doc.createElement("source");
			Element database = doc.createElement("database");
			database.appendChild(doc.createTextNode("Unknown"));
			sourceNode.appendChild(database);
			rootElement.appendChild(sourceNode);

			Element size = doc.createElement("size");
			Element width = doc.createElement("width");
			width.appendChild(doc.createTextNode(String.valueOf((int) image.getWidth())));
			Element height = doc.createElement("height");
			height.appendChild(doc.createTextNode(String.valueOf((int) image.getHeight())));
			Element depth = doc.createElement("depth");
			// TODO kako dobit depth od slike, vecinom je 3
			depth.appendChild(doc.createTextNode(String.valueOf(3)));
			size.appendChild(width);
			size.appendChild(height);
			size.appendChild(depth);
			rootElement.appendChild(size);

			Element segmented = doc.createElement("segmented");
			segmented.appendChild(doc.createTextNode(String.valueOf(0)));
			rootElement.appendChild(segmented);

			for (ResizableRectangleWrapper rectWrapp : annotations) {
				Element object = doc.createElement("object");
				Element name = doc.createElement("name");
				name.appendChild(doc.createTextNode(rectWrapp.getKlass()));
				Element color = doc.createElement("color");
				color.appendChild(doc.createTextNode(rectWrapp.getColor()));
				Element text = doc.createElement("text");
				text.appendChild(doc.createTextNode(rectWrapp.getAdditionalText()));
				Element pose = doc.createElement("pose");
				pose.appendChild(doc.createTextNode("Unspecified"));
				Element truncated = doc.createElement("truncated");
				boolean n = rectWrapp.getTruncated();
				if (n) {
					truncated.appendChild(doc.createTextNode(String.valueOf(1)));
				} else {
					truncated.appendChild(doc.createTextNode(String.valueOf(0)));
				}
				Element difficult = doc.createElement("difficult");
				n = rectWrapp.getDifficult();
				if (n) {
					difficult.appendChild(doc.createTextNode(String.valueOf(1)));
				} else {
					difficult.appendChild(doc.createTextNode(String.valueOf(0)));
				}
				Element overlap = doc.createElement("overlap");
				n = rectWrapp.getOverlap();
				if (n) {
					overlap.appendChild(doc.createTextNode(String.valueOf(1)));
				} else {
					overlap.appendChild(doc.createTextNode(String.valueOf(0)));
				}
				Element aspect_ratio = doc.createElement("aspect_ratio");
				aspect_ratio.appendChild(doc.createTextNode(String.valueOf(rectWrapp.getAspectRatio())));
				
				object.appendChild(name);
				object.appendChild(color);
				object.appendChild(text);
				object.appendChild(pose);
				object.appendChild(truncated);
				object.appendChild(difficult);
				object.appendChild(overlap);
				object.appendChild(aspect_ratio);
				Element bndbox = doc.createElement("bndbox");
				Element xmin = doc.createElement("xmin");
				xmin.appendChild(doc.createTextNode(String.valueOf(rectWrapp.getXMin())));
				Element ymin = doc.createElement("ymin");
				ymin.appendChild(doc.createTextNode(String.valueOf(rectWrapp.getYMin())));
				Element xmax = doc.createElement("xmax");
				xmax.appendChild(doc.createTextNode(String.valueOf(rectWrapp.getXMax())));
				Element ymax = doc.createElement("ymax");
				ymax.appendChild(doc.createTextNode(String.valueOf(rectWrapp.getYMax())));
				bndbox.appendChild(xmin);
				bndbox.appendChild(ymin);
				bndbox.appendChild(xmax);
				bndbox.appendChild(ymax);
				object.appendChild(bndbox);
				rootElement.appendChild(object);
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			String xmlPathString = dataSet.getDataSetAnnotationsLocation() + "\\" + imageName;
			if (xmlPathString.contains(".")) {
				xmlPathString = xmlPathString.substring(0, xmlPathString.lastIndexOf('.'));
			}
			xmlPathString = xmlPathString + ".xml";
			StreamResult result = new StreamResult(new File(xmlPathString));
			transformer.transform(source, result);

			if (!imgsWithAnnotations.contains(imageName)) {
				if (imgsWithAnnotations.isEmpty()) {
					imgsWithAnnotations.add(imageName);
					listOfImgWithAnnotations.setItems(imgsWithAnnotations);
				} else {
					imgsWithAnnotations.add(imageName);
				}
				DataImage img = new DataImage(imageName);
				dataSet.addDataSetImageWithAnnotations(img);
				if(isVerified) {
					verImgs.remove(imageName);
					dataSet.removeDataSetVerifiedImage(imageName);
				}
				imgs.remove(imageName);
				dataSet.removeDataSetImage(imageName);
			}
			else {
				if(annotations.size() == 0 | annotations.isEmpty()) {
					imgs.add(imageName);
					DataImage img = new DataImage(imageName);
					dataSet.addDataSetImage(img);
					dataSet.removeDataSetImageWithAnnotations(imageName);
					imgsWithAnnotations.remove(imageName);
				}
			}
			
			File file = MainApp.getLastFilePath();
			mainApp.saveDataSetsToFile(file);

			if(listOfImg.getSelectionModel().getSelectedItem() != null || imgs.size()==0) {
				if(listOfImg.getSelectionModel().getSelectedIndex() == 0) {
					listOfImgWithAnnotations.getSelectionModel().select(imgsWithAnnotations.size()-1);
					listOfImgWithAnnotations.getFocusModel()
					.focus(listOfImgWithAnnotations.getSelectionModel().getSelectedIndex());
			listOfImgWithAnnotations.scrollTo(listOfImgWithAnnotations.getSelectionModel().getSelectedIndex());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method which is called when user clicks next(>) button. It shows next image
	 * from currently selected list
	 */
	@FXML
	private void handleNext() {
		if (listOfImg.getSelectionModel().getSelectedItem() != null) {
			listOfImg.getSelectionModel().selectNext();
			listOfImg.getFocusModel().focus(listOfImg.getSelectionModel().getSelectedIndex());
			listOfImg.scrollTo(listOfImg.getSelectionModel().getSelectedIndex());
		} else if (listOfImgWithAnnotations.getSelectionModel().getSelectedItem() != null) {
			if(listOfImgWithAnnotations.getSelectionModel().getSelectedIndex() == (imgsWithAnnotations.size()-1)) {
				listOfImg.getSelectionModel().select(0);
				listOfImg.getFocusModel().focus(listOfImg.getSelectionModel().getSelectedIndex());
				listOfImg.scrollTo(listOfImg.getSelectionModel().getSelectedIndex());
				return;
			}
			listOfImgWithAnnotations.getSelectionModel().selectNext();
			listOfImgWithAnnotations.getFocusModel()
					.focus(listOfImgWithAnnotations.getSelectionModel().getSelectedIndex());
			listOfImgWithAnnotations.scrollTo(listOfImgWithAnnotations.getSelectionModel().getSelectedIndex());
		} else {
			listOfVerImg.getSelectionModel().selectNext();
			listOfVerImg.getFocusModel().focus(listOfVerImg.getSelectionModel().getSelectedIndex());
			listOfVerImg.scrollTo(listOfVerImg.getSelectionModel().getSelectedIndex());
		}
	}

	/**
	 * Method which is called when user clicks previous(<) button. It shows previous
	 * image from currently selected list
	 */
	@FXML
	private void handlePrevious() {
		if (listOfImg.getSelectionModel().getSelectedItem() != null) {
			if(listOfImg.getSelectionModel().getSelectedIndex() == 0) {
				listOfImgWithAnnotations.getSelectionModel().select(imgsWithAnnotations.size()-1);
				listOfImgWithAnnotations.getFocusModel()
				.focus(listOfImgWithAnnotations.getSelectionModel().getSelectedIndex());
		listOfImgWithAnnotations.scrollTo(listOfImgWithAnnotations.getSelectionModel().getSelectedIndex());
				return;
			}
			listOfImg.getSelectionModel().selectPrevious();
			listOfImg.getFocusModel().focus(listOfImg.getSelectionModel().getSelectedIndex());
			listOfImg.scrollTo(listOfImg.getSelectionModel().getSelectedIndex());
		} else if (listOfImgWithAnnotations.getSelectionModel().getSelectedItem() != null) {
			listOfImgWithAnnotations.getSelectionModel().selectPrevious();
			listOfImgWithAnnotations.getFocusModel()
					.focus(listOfImgWithAnnotations.getSelectionModel().getSelectedIndex());
			listOfImgWithAnnotations.scrollTo(listOfImgWithAnnotations.getSelectionModel().getSelectedIndex());
		} else {
			listOfVerImg.getSelectionModel().selectPrevious();
			listOfVerImg.getFocusModel().focus(listOfVerImg.getSelectionModel().getSelectedIndex());
			listOfVerImg.scrollTo(listOfVerImg.getSelectionModel().getSelectedIndex());
		}
	}

	EventHandler<ActionEvent> onActionCrop = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			String selectedImage = listOfImg.getSelectionModel().getSelectedItem();
			if (selectedImage != null) {
				Bounds selectionBounds = areaSelection.selectArea(imageGroup).getBoundsInParent();

				// crop the image
				crop(selectionBounds);
			} else {
				// Nothing selected.
				Alert alert = new Alert(AlertType.WARNING);
				alert.initOwner(mainApp.getPrimaryStage());
				alert.setTitle("Warning");
				alert.setHeaderText("No Image Selected");
				alert.setContentText("Please select the image first!");

				alert.showAndWait();
			}
		}
	};

	private void crop(Bounds bounds) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Image");

		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
		if (file == null) {
			System.out.println("ZAAAAASTO");
			return;
		}

		int width = (int) bounds.getWidth();
		int height = (int) bounds.getHeight();

		SnapshotParameters parameters = new SnapshotParameters();
		parameters.setFill(Color.TRANSPARENT);
		parameters.setViewport(new Rectangle2D(bounds.getMinX(), bounds.getMinY(), width, height));

		WritableImage wi = new WritableImage(width, height);
		imageView.snapshot(parameters, wi);

		// save image
		// !!! has bug because of transparency (use approach below) !!!
		// --------------------------------
		// try {
		// ImageIO.write(SwingFXUtils.fromFXImage( wi, null), "jpg", file);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		// save image (without alpha)
		// --------------------------------
		BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
		BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(),
				BufferedImage.OPAQUE);

		Graphics2D graphics = bufImageRGB.createGraphics();
		graphics.drawImage(bufImageARGB, 0, 0, null);

		try {

			ImageIO.write(bufImageRGB, "jpg", file);

			System.out.println("Image saved to " + file.getAbsolutePath());

		} catch (IOException e) {
			e.printStackTrace();
		}

		graphics.dispose();

	}

	public void clearSelection(Group group) {
		if (group.getChildren().size() > 1) {
			// deletes everything except for base container layer
			System.out.println(group.getChildren().size());
			group.getChildren().remove(1, group.getChildren().size());
			index = 0;
		}

	}

	private class Selection {
		private Group group;
		private ResizableRectangle selectionRectangle = null;
		private double rectangleStartX;
		private double rectangleStartY;
		private boolean isSecondary = false;

		private ResizableRectangle selectArea(Group group) {
			this.group = group;

			if (imageView != null && image != null) {
				this.group.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
				this.group.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
				this.group.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
			}

			return selectionRectangle;
		}

		private void deselectArea() {
			this.group.getChildren().get(0).removeEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
			this.group.getChildren().get(0).removeEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
			this.group.getChildren().get(0).removeEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
		}

		EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
			if(event.isMiddleButtonDown()) {
				System.out.println("srednji stisnut");
				return;
			}
			if (event.isSecondaryButtonDown()) {
				System.out.println("desni");
				isSecondary = true;
				for(javafx.scene.Node node: imageGroup.getChildren()) {
					node.setVisible(true);
				}
				return;
			}
			for (ResizableRectangleWrapper rectWrap : annotations) {
				rectWrap.getRectangle().setFill(mainApp.getColorOfClasses().get(rectWrap.getKlass()).get());
			}
			for(javafx.scene.Node node: imageGroup.getChildren()) {
				node.setVisible(true);
			}
			annotationsTable.getSelectionModel().clearSelection();
			isSecondary = false;
			editButton.setDisable(true);
			deleteButton.setDisable(true);
			rectangleStartX = event.getX();
			rectangleStartY = event.getY();

			// clearSelection(group);
			if (aspectRatioLock.isSelected()) {
				if((xField.getText().length() == 0 || xField.getText() == null) && (yField.getText().length() == 0 || yField.getText() == null)) {
					aspectRatio = (double) 1/1;
				}
				else {
					try {
					aspectRatio = (double) Integer.valueOf(xField.getText()) / Integer.valueOf(yField.getText());
					} catch (Exception e) {
						aspectRatio = (double) 1/1;
					}
				}
			}
			else {
				aspectRatio = 0;
			}
			
			selectionRectangle = new ResizableRectangle(rectangleStartX, rectangleStartY, 0, 0, aspectRatio, group, annotations,
					annotationsTable, imageView, mainApp);

		};

		EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
			if(event.isMiddleButtonDown()) return;
			if (event.isSecondaryButtonDown()) {
				return;
			}
			if (aspectRatioLock.isSelected()) {
				if((xField.getText().length() == 0 || xField.getText() == null) && (yField.getText().length() == 0 || yField.getText() == null)) {
					aspectRatio = (double) 1/1;
				}
				else {
					try {
					aspectRatio = (double) Integer.valueOf(xField.getText()) / Integer.valueOf(yField.getText());
					} catch (Exception e) {
						aspectRatio = (double) 1/1;
					}
				}
				DoubleProperty offsetX = new SimpleDoubleProperty(event.getX() - rectangleStartX);
				DoubleProperty offsetY = new SimpleDoubleProperty(event.getY() - rectangleStartY);
				if(offsetX.get() > 0 && offsetY.get() > 0) {
					if (event.getX() < imageView.getFitWidth() && event.getY() < imageView.getFitHeight()) {
						if(selectionRectangle.getY() + offsetX.get()/aspectRatio < imageView.getFitHeight()) {
							selectionRectangle.setWidth(offsetX.get());
							selectionRectangle.setHeight(offsetX.get()/aspectRatio);
						}
					}
				}
//				else if(offsetX.get() < 0 && offsetY.get() < 0) {
//					if(event.getX() < 0 && event.getY() < 0) {
//						selectionRectangle.setX(0);
//						selectionRectangle.setY(0);
//					}
//					else if (event.getX() > 0 && event.getY() > 0) {
//						selectionRectangle.setX(event.getX());
//						selectionRectangle.setY(event.getY()*aspectRatio);
//						selectionRectangle.setWidth(rectangleStartX - selectionRectangle.getX());
//						selectionRectangle.setHeight(selectionRectangle.getWidth()/aspectRatio);
//					}
////					if(event.getY() < 0) {
////						selectionRectangle.setY(0);
////					}
////					else {
////						selectionRectangle.setY(event.getY());
////						selectionRectangle.setHeight(selectionRectangle.getWidth()/aspectRatio);
////					}
//				}
//				else if(offsetX.get() < 0 && offsetY.get() > 0) {
//					
//				}
//				else if(offsetX.get() > 0 && offsetY.get() < 0) {
//					System.out.println(2);
//				}
				
				
			} else {
				aspectRatio = 0;
				DoubleProperty offsetX = new SimpleDoubleProperty(event.getX() - rectangleStartX);
				DoubleProperty offsetY = new SimpleDoubleProperty(event.getY() - rectangleStartY);
				// offsetX.bindBidirectional(offsetY);

				if (offsetX.get() > 0) {
					if (event.getX() > imageView.getFitWidth()) {
						selectionRectangle.setWidth(imageView.getFitWidth() - rectangleStartX);
					} else
						selectionRectangle.setWidth(offsetX.get());
				} else {
					if (event.getX() < 0)
						selectionRectangle.setX(0);
					else
						selectionRectangle.setX(event.getX());
					selectionRectangle.setWidth(rectangleStartX - selectionRectangle.getX());
				}

				if (offsetY.get() > 0) {
					if (event.getY() > imageView.getFitHeight())
						selectionRectangle.setHeight(imageView.getFitHeight() - rectangleStartY);
					else
						selectionRectangle.setHeight(offsetY.get());
				} else {
					if (event.getY() < 0)
						selectionRectangle.setY(0);
					else
						selectionRectangle.setY(event.getY());
					selectionRectangle.setHeight((rectangleStartY - selectionRectangle.getY()));
				}
			}

		};

		EventHandler<MouseEvent> onMouseReleasedEventHandler = event -> {
			if(event.getButton() != MouseButton.PRIMARY) {
				System.out.println("srednji otpušten");
				return;
			}
			if (selectionRectangle != null && !isSecondary) {

				String selectedImage;
				if (listOfImg.getSelectionModel().getSelectedItem() != null) {
					selectedImage = listOfImg.getSelectionModel().getSelectedItem();
				} else if (listOfImgWithAnnotations.getSelectionModel().getSelectedItem() != null) {
					selectedImage = listOfImgWithAnnotations.getSelectionModel().getSelectedItem();
				}
				else {
					selectedImage = listOfVerImg.getSelectionModel().getSelectedItem();
				}
				if (selectedImage != null) {
					index = index + 1;
					ResizableRectangleWrapper annotation = mainApp.showAnnotateDialog(imageGroup, selectionRectangle,
							index);
					if (annotation != null) {
						if (annotations.isEmpty()) {
							annotations.add(annotation);
							annotationsTable.setItems(annotations);
						} else {
							annotations.add(annotation);
						}
						annotation.getRectangle().setFill(mainApp.getColorOfClasses().get(annotation.getKlass()).get());
						boolean overlap = annotation.getOverlap();
						boolean truncated = annotation.getTruncated();
						boolean difficult = annotation.getDifficult();
						if(overlap && truncated) {
							annotation.getRectangle().setStroke(mainApp.getColorsOfFlags().get("Overlap&Truncated").get());
						}
						else if(overlap) {
							annotation.getRectangle().setStroke(mainApp.getColorsOfFlags().get("Overlap").get());
						}
						else if(truncated) {
							annotation.getRectangle().setStroke(mainApp.getColorsOfFlags().get("Truncated").get());
						}
						else if(difficult) {
							annotation.getRectangle().setStroke(mainApp.getColorsOfFlags().get("Difficult").get());
						}
						else if(overlap == false && truncated == false && difficult == false) {
							annotation.getRectangle().setStroke(mainApp.getDefaultBorderColor());
						}
						
						if(dataSet.getAnnotations().containsKey(annotation.getKlass())) {
							int num = dataSet.getAnnotations().get(annotation.getKlass());
							num = num + 1;
							dataSet.getAnnotations().put(annotation.getKlass(), num);
						}
						else {
							dataSet.getAnnotations().put(annotation.getKlass(), 1);
						}
						
						updateAnnotationsNumber();
						
						handleSaveButton();
						
						history.currentIndex++;
						history.operations.add(Operation.add);
						history.rectangles.add(new OperationRectangleWrappers(annotation, null));
						
					} else {
						index = index - 1;
					}
				}
			}

		};
	}

	
	public void handleUndo() {
		if (history.currentIndex > -1) {
			if (history.operations.get(history.currentIndex) == Operation.add) {
				ResizableRectangleWrapper rect = history.rectangles.get(history.currentIndex).getRectangleWrapperNew();
				if (dataSet.getAnnotations().containsKey(rect.getKlass())) {
					int num = dataSet.getAnnotations().get(rect.getKlass());
					num = num - 1;
					if (num == 0) {
						dataSet.getAnnotations().remove(rect.getKlass());
					} else {
						dataSet.getAnnotations().put(rect.getKlass(), num);
					}
				}
				int i = history.rectangles.get(history.currentIndex).getRectangleWrapperNew().getIndex();
				int j = i + (i - 1) * 9;
				imageGroup.getChildren().remove(j, j + 10);
				annotations.remove(history.rectangles.get(history.currentIndex).getRectangleWrapperNew());
				annotationsTable.getSelectionModel().clearSelection();
				deleteButton.setDisable(true);
				editButton.setDisable(true);
				for (ResizableRectangleWrapper r : annotations) {
					if (r.getIndex() > i) {
						r.setIndex(r.getIndex() - 1);
					}
				}

				updateAnnotationsNumber();
				handleSaveButton();

				history.currentIndex--;
				index--;
			} else if (history.operations.get(history.currentIndex) == Operation.delete) {

			} 
		}
	}
	
	public void handleRedo() {
		if (history.currentIndex < history.operations.size()-1 ) {
			history.currentIndex++;
			if (history.operations.get(history.currentIndex) == Operation.add) {
				ResizableRectangleWrapper annotation = history.rectangles.get(history.currentIndex)
						.getRectangleWrapperNew();
				index = index + 1;
				if (annotation != null) {
					annotation.setRectangle(new ResizableRectangle(annotation.getRectangle().getX(),
							annotation.getRectangle().getY(), annotation.getRectangle().getWidth(),
							annotation.getRectangle().getHeight(), annotation.getAspectRatio(), imageGroup, annotations,
							annotationsTable, imageView, mainApp));
					if (annotations.isEmpty()) {
						annotations.add(annotation);
						annotationsTable.setItems(annotations);
					} else {
						annotations.add(annotation);
					}
					annotation.getRectangle().setFill(mainApp.getColorOfClasses().get(annotation.getKlass()).get());
					boolean overlap = annotation.getOverlap();
					boolean truncated = annotation.getTruncated();
					boolean difficult = annotation.getDifficult();
					if (overlap && truncated) {
						annotation.getRectangle().setStroke(mainApp.getColorsOfFlags().get("Overlap&Truncated").get());
					} else if (overlap) {
						annotation.getRectangle().setStroke(mainApp.getColorsOfFlags().get("Overlap").get());
					} else if (truncated) {
						annotation.getRectangle().setStroke(mainApp.getColorsOfFlags().get("Truncated").get());
					} else if (difficult) {
						annotation.getRectangle().setStroke(mainApp.getColorsOfFlags().get("Difficult").get());
					} else if (overlap == false && truncated == false && difficult == false) {
						annotation.getRectangle().setStroke(mainApp.getDefaultBorderColor());
					}

					if (dataSet.getAnnotations().containsKey(annotation.getKlass())) {
						int num = dataSet.getAnnotations().get(annotation.getKlass());
						num = num + 1;
						dataSet.getAnnotations().put(annotation.getKlass(), num);
					} else {
						dataSet.getAnnotations().put(annotation.getKlass(), 1);
					}

					updateAnnotationsNumber();

					handleSaveButton();

				}
			} 
		}
	}
	
	private void testPrint() {
		System.out.println(history.currentIndex);
		for(OperationRectangleWrappers rect: history.rectangles) {
			System.out.println("*************");
			ResizableRectangleWrapper newRect = rect.getRectangleWrapperNew();
			ResizableRectangleWrapper oldRect = rect.getRectangleWrapperOld();
			if(newRect != null) {
				System.out.println(newRect.getRectangle());
			}
			else {
				System.out.println("null");
			}
			if(oldRect != null) {
				System.out.println(oldRect.getRectangle());
			}
			else {
				System.out.println("null");
			}
			System.out.println("*************");
		}
		for(Operation op: history.operations) {
			System.out.println("--------------");
			System.out.println(op);
			System.out.println("--------------");
		}
	}
}
