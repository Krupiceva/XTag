package fer.hr.telegra.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fer.hr.telegra.MainApp;
import fer.hr.telegra.model.DataImage;
import fer.hr.telegra.model.DataSet;
import fer.hr.telegra.model.ResizableRectangle;
import fer.hr.telegra.model.ResizableRectangleWrapper;
import fer.hr.telegra.model.TooltippedTableCell;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Screen;

public class OpenDataSetOverviewController {

	// Reference to the main application.
    private MainApp mainApp;
    
    private DataSet dataSet;
    
    @FXML
    private SplitPane splitPaneHor;
    @FXML
    private ListView<String> listOfImg;
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
    private Button saveButton;
    @FXML
    private Button backButton;
    @FXML
    private ToggleButton poly;
    @FXML
    private Group imageGroup;
    @FXML
    private VBox vBoxOfImg;
    @FXML
    private ScrollPane scrollPaneOfImg;
    @FXML
    private Label images;
    @FXML
    private Label ann;
    StackPane imageHolder = new StackPane();
    Image image;
    ToggleGroup toggleGroup = new ToggleGroup();;
	ContextMenu contextMenu;
	MenuItem annotateMenuItem;
	MenuItem cropMenuItem;
	final DoubleProperty zoomProperty = new SimpleDoubleProperty(200);
    
    ObservableList<String> imgs = FXCollections.observableArrayList();
    
    private ObservableList<ResizableRectangleWrapper> annotations = FXCollections.observableArrayList();
    
    private boolean isAreaSelected = false;
    final Selection areaSelection = new Selection();
    int index = 0;
    
    public OpenDataSetOverviewController() {
    }
    
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     * sets divider 15%/85% but it is resizable
     */
    @FXML
    private void initialize() {
    	System.gc();
    	splitPaneHor.setDividerPositions(0.15);
    	splitPaneHor.lookupAll(".split-pane-divider").stream().forEach(div ->  div.setMouseTransparent(true) );
    	
    	nameColumn.setCellValueFactory(cellData -> cellData.getValue().klassProperty());
    	nameColumn.setCellFactory(TooltippedTableCell.forTableColumn());
    	textColumn.setCellValueFactory(cellData -> cellData.getValue().additionalTextProperty());
    	textColumn.setCellFactory(TooltippedTableCell.forTableColumn());
    	xMinColumn.setCellValueFactory(cellData -> cellData.getValue().xMinProperty().asObject());
    	yMinColumn.setCellValueFactory(cellData -> cellData.getValue().yMinProperty().asObject());
    	xMaxColumn.setCellValueFactory(cellData -> cellData.getValue().xMaxProperty().asObject());
    	yMaxColumn.setCellValueFactory(cellData -> cellData.getValue().yMaxProperty().asObject());
    	
    	//Add listener for which image need to be display in imageview
    	listOfImg.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showImage(newValue));
    	
    	//Add listener for which rectangle to select
    	annotationsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showRectangle(newValue, oldValue));
    	annotationsTable.setPlaceholder(new Label("No annotations"));
    	
    	
    	//Create icons for buttons and labels
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
    	saveButton.setGraphic(new ImageView(imageSave));
    	saveButton.setDisable(true);
    	backButton.setGraphic(new ImageView(imageBack));
    	images.setGraphic(new ImageView(imageImages));
    	ann.setGraphic(new ImageView(imageAnn));
    	
    	/**
    	zoomProperty.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable arg0) {
                imageView.setFitWidth(zoomProperty.get() * 3);
                //imageView.setFitHeight(zoomProperty.get() * 3);
            }
        });
    	
    	scrollPaneOfImg.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() > 0) {
                    zoomProperty.set(zoomProperty.get() * 1.1);
                } else if (event.getDeltaY() < 0) {
                    zoomProperty.set(zoomProperty.get() / 1.1);
                }
            }
        });
    	*/
    	  	
    	tag.setToggleGroup(toggleGroup);
    	//Add listener for toggle button 
    	//If is pressed enable drawing a recatngle, if is not disable
    	tag.selectedProperty().addListener(((observable, oldValue, newValue) -> {
    	    if (newValue) {
    	    	areaSelection.selectArea(imageGroup);
    	    }
    	    else {
    			areaSelection.deselectArea();
    	    }
    	}));
     	
    	
    }
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        KeyCombination cntrlS = new KeyCodeCombination(KeyCode.S, KeyCodeCombination.CONTROL_DOWN);
        this.mainApp.getPrimaryStage().getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) {
                	handlePrevious();
                }
                else if(event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) {
                	handleNext();
                }
                else if(cntrlS.match(event)) {
                	handleSaveButton();
                }
                else if(event.getCode() == KeyCode.DELETE) {
                	if(annotationsTable.getSelectionModel().getSelectedItem() != null && !deleteButton.disabledProperty().get()) {
                		System.out.println("delete button pressed");
                		handleDeleteButton();
                	}
                }
            }
        });
    }
    
    /**
     * Set the dataset and set dataset images in list
     * @param dataSet is the current open dataset
     */
    public void setDataSet(DataSet dataSet) {
    	this.dataSet = dataSet;
    	BigList<DataImage> images =  BigList.create(dataSet.getDataSetImages());
		Iterator<DataImage> itr = images.iterator();
		while(itr.hasNext()) {
			imgs.add(itr.next().getImageName());
		}
	    if(!imgs.isEmpty()) {
	    	listOfImg.setItems(imgs);
	    }
	    
	    
    }
    
    /**
     * Show selected image in imageview
     * It put imageview in group because of rectangles (taggs) that will be in the same group
     * It put it always on first place so the rectangle is visible
     * Imagegroup puts in stackpane because only stackpane by default is in center
     * And stackpane puts in scrollerpane so you can zoom image
     * it join dataset location and image name to get absolut path
     * @param imageName is name of selected image
     */
    private void showImage(String imageName) {
    	handleFitZoomButton();
    	String path = dataSet.getDataSetImagesLocation() + "\\" + imageName;
    	clearSelection(imageGroup);
    	editButton.setDisable(true);
    	deleteButton.setDisable(true);
    	annotations.clear();
    	tag.setDisable(false);
    	saveButton.setDisable(false);
    	try {
    		if(imageGroup != null) {
    			imageGroup.getChildren().remove(imageView);
    			//imageHolder.getChildren().remove(imageGroup);
    		}
    		image = new Image(new FileInputStream(path));
	    	//imageView = new ImageView(image);
    		imageView.setImage(image);
    		imageView.fitWidthProperty().bind(image.widthProperty());
    		imageView.fitHeightProperty().bind(image.heightProperty());
    		imageView.setPreserveRatio(true);
	    	//imageView.preserveRatioProperty().set(true);
    		/**
    		System.out.println(imageView.getFitWidth() + " " + imageView.getFitHeight() + " " + imageView.preserveRatioProperty());
    		System.out.println(image.getWidth() + " " + image.getHeight());;
    		System.out.println(borderPaneOfImg.getWidth() + borderPaneOfImg.getHeight());
    		System.out.println(anchorPaneOfImg.getWidth());
    		*/
	    	imageView.setPickOnBounds(true);
	    	imageGroup.getChildren().add(0, imageView);
	    	Parent zoomPane = createZoomPane(imageGroup);
	    	borderPaneOfImg.setCenter(zoomPane);
	    	//imageHolder.getChildren().add(imageGroup);
	    	//scrollPaneOfImg.setContent(imageHolder);
	    	showAnnotations(imageName);
	    	System.gc();
	    	
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	}
    }
    
    private Parent createZoomPane(final Group group) {
        final StackPane zoomPane = new StackPane();

        zoomPane.getChildren().add(group);

        final ScrollPane scroller = new ScrollPane();
        final Group scrollContent = new Group(zoomPane);
        scroller.setContent(scrollContent);

        scroller.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
          @Override
          public void changed(ObservableValue<? extends Bounds> observable,
              Bounds oldValue, Bounds newValue) {
            zoomPane.setMinSize(newValue.getWidth(), newValue.getHeight());
          }
        });
        
        return scroller;
    }
    
    private void showRectangle(ResizableRectangleWrapper rectangleWrapperNew, ResizableRectangleWrapper rectangleWrapperOld) {
    	if(rectangleWrapperNew != null) {
    		rectangleWrapperNew.getRectangle().setFill(Color.LIGHTPINK.deriveColor(0, 1.2, 1, 0.5));
    	}
    	if(rectangleWrapperOld != null) {
    		rectangleWrapperOld.getRectangle().setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.5));
    	}
    	editButton.setDisable(false);
    	deleteButton.setDisable(false);
    }
    
    private void showAnnotations(String imageName) {
    	String name;
    	String text = "";
    	String truncated = "0";
    	String difficult = "0";
    	String overlap = "0";
    	String xmin;
    	String ymin;
    	String xmax;
    	String ymax;
    	ResizableRectangle rect;
    	ResizableRectangleWrapper rectWrap;
    	Integer index = 0;
    	String annotationPath = dataSet.getDataSetAnnotationsLocation() + "\\" + imageName;
    	 if(annotationPath.contains(".")) {
    		 annotationPath = annotationPath.substring(0, annotationPath.lastIndexOf('.'));
         }
    	 annotationPath = annotationPath + ".xml";
    	 
    	 try {
    		 File inputFile = new File(annotationPath);
    		 if(inputFile.isFile()) {
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
	            		 if(temp.getLength() > 0) {
	            			 text = eElement.getElementsByTagName("text").item(0).getTextContent().trim();
	            		 }
	            		 temp = eElement.getElementsByTagName("truncated");
	            		 if(temp.getLength() > 0) {
	            			 truncated = eElement.getElementsByTagName("truncated").item(0).getTextContent().trim();
	            		 }
	            		 temp = eElement.getElementsByTagName("difficult");
	            		 if(temp.getLength() > 0) {
	            			 difficult = eElement.getElementsByTagName("difficult").item(0).getTextContent().trim();
	            		 }
	            		 temp = eElement.getElementsByTagName("overlap");
	            		 if(temp.getLength() > 0) {
	            			 overlap = eElement.getElementsByTagName("overlap").item(0).getTextContent().trim();
	            		 }
	            		 
	            		 //System.out.println(name + " " + text + " " + truncated + " " + difficult );
	            		 xmin = eElement.getElementsByTagName("xmin").item(0).getTextContent().trim();
	            		 ymin = eElement.getElementsByTagName("ymin").item(0).getTextContent().trim();
	            		 xmax = eElement.getElementsByTagName("xmax").item(0).getTextContent().trim();
	            		 ymax = eElement.getElementsByTagName("ymax").item(0).getTextContent().trim();
	            		 //System.out.println(xmin + " " + ymin + " " + xmax + " " + ymax);
	            		 Double width = Double.parseDouble(xmax) - Double.parseDouble(xmin);
	            		 Double height = Double.parseDouble(ymax) - Double.parseDouble(ymin);
	            		 rect = new ResizableRectangle(Double.parseDouble(xmin), Double.parseDouble(ymin), width, height, imageGroup, annotations, annotationsTable, imageView, mainApp);
	            		 rectWrap = new ResizableRectangleWrapper(rect, index);
	            		 rectWrap.setKlass(name);
	            		 rectWrap.setAdditionalText(text);
	            		 if (truncated.equals("0")) {
	            			 rectWrap.setTruncated(false);
	            		 }
	            		 else if(truncated.equals("1")) {
	            			 rectWrap.setTruncated(true);
	            		 }
	            		 if(difficult.equals("0")) {
	            			 rectWrap.setDifficult(false);
	            		 }
	            		 else if(difficult.equals("1")) {
	            			 rectWrap.setDifficult(true);
	            		 }
	            		 if(overlap.equals("0")) {
	            			 rectWrap.setOverlap(false);
	            		 }
	            		 else if(overlap.equals("1")) {
	            			 rectWrap.setOverlap(true);
	            		 }
	            		 if(annotations.isEmpty()) {
	         				annotations.add(rectWrap);
	         				annotationsTable.setItems(annotations);
	         			}
	         			else {
	         				annotations.add(rectWrap);
	         			}
	            		 
	            	 }
	            	 this.index = index;
	             }
    		 }
    	 } catch(Exception e) {
    		 e.printStackTrace();
    	 }
    	
    }
    
    
    /**
     * Method which is called when user clicks on back to datasets button. Inside rootlayout puts datasetsoverview
     */
    @FXML
    private void handleBack() {
    	mainApp.showDataSetsOverview();
    }
    
    /**
     * Method which is called when user clicks edit button. Open dialog to edit details for open dataset
     */
    @FXML
    private void handleEdit() {
    	boolean okClicked = mainApp.showDataSetEditDialog(dataSet);
    }
    
    @FXML
    private void handleDeleteButton() {
    	
    	int i = annotationsTable.getSelectionModel().getSelectedItem().getIndex();
    	System.out.println(i);
    	int j = i + (i-1) * 9;
    	imageGroup.getChildren().remove(j, j+10);
    	annotations.remove(annotationsTable.getSelectionModel().getSelectedItem());
    	annotationsTable.getSelectionModel().clearSelection();
    	deleteButton.setDisable(true);
    	editButton.setDisable(true);
    	for (ResizableRectangleWrapper rect : annotations) {
    		if(rect.getIndex() > i ) {
    			rect.setIndex(rect.getIndex() - 1);
    		}
    	}
    	
    	index = index - 1;
    }
    
    @FXML
    private void handleEditButton() {
    	ResizableRectangleWrapper rect = annotationsTable.getSelectionModel().getSelectedItem();
    	
    	mainApp.showEditAnnotationDialog(rect, rect.getIndex(), annotations);
    	
    }
    
    @FXML
    private void handleZoomInButton() {
    	imageGroup.setScaleX(imageGroup.getScaleX() * 1.1);
    	imageGroup.setScaleY(imageGroup.getScaleY() * 1.1);
    }
    
    @FXML
    private void handleZoomOutButton() {
    	imageGroup.setScaleX(imageGroup.getScaleX() * (1/1.1));
    	imageGroup.setScaleY(imageGroup.getScaleY() * (1/1.1));
    }
    
    @FXML
    private void handleFitZoomButton() {
    	imageGroup.setScaleX(1);
    	imageGroup.setScaleY(1);
    }
    
    @FXML
    private void handleWindowZoomButton() {
    	Number i = anchorPaneOfImg.getWidth()/imageView.getFitWidth();
    	Number j = anchorPaneOfImg.getHeight()/imageView.getFitHeight();
    	//double round = Math.round(i * 10.0) / 10.0;
    	DecimalFormat df = new DecimalFormat("#.#");
    	df.setRoundingMode(RoundingMode.DOWN);
    	String roundi = df.format(i.doubleValue());
    	roundi = roundi.replaceAll(",", ".");
    	Double rdi = Double.valueOf(roundi);
    	String roundj = df.format(j.doubleValue());
    	roundj = roundj.replaceAll(",", ".");
    	Double rdj = Double.valueOf(roundj);
    	
    	imageGroup.setScaleX(rdi);
    	imageGroup.setScaleY(rdi);
    	//System.out.println(anchorPaneOfImg.getHeight());
    }
    
    @FXML
    private void handleSaveButton() {
    	
    	// Ne koristim za sad dok aplikacija nece imat bazu
    	/**
    	for(DataImage img: dataSet.getDataSetImages()) {
    		if(img.getImageName().equals(listOfImg.getSelectionModel().getSelectedItem())){
    			img.setAnnotations(annotations);
    		}
    	}
		*/
    	
    	try {
    	DocumentBuilderFactory dbFactory =
    	DocumentBuilderFactory.newInstance();
    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    	Document doc = dBuilder.newDocument();
    	
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
    	
    	Element filename = doc.createElement("filename");
    	filename.appendChild(doc.createTextNode(listOfImg.getSelectionModel().getSelectedItem()));
    	rootElement.appendChild(filename);
    	
    	Element path = doc.createElement("path");
    	path.appendChild(doc.createTextNode(dataSetImagesLocation + "\\" + listOfImg.getSelectionModel().getSelectedItem()));
    	rootElement.appendChild(path);
    	
    	Element sourceNode = doc.createElement("source");
    	Element database = doc.createElement("database");
    	database.appendChild(doc.createTextNode("Unknown"));
    	sourceNode.appendChild(database);
    	rootElement.appendChild(sourceNode);
    	
    	Element size = doc.createElement("size");
    	Element width = doc.createElement("width");
    	width.appendChild(doc.createTextNode(String.valueOf((int)image.getWidth())));
    	Element height = doc.createElement("height");
    	height.appendChild(doc.createTextNode(String.valueOf((int)image.getHeight())));
    	Element depth = doc.createElement("depth");
    	//TODO kako dobit depth od slike, vecinom je 3
    	depth.appendChild(doc.createTextNode(String.valueOf(3)));
    	size.appendChild(width);
    	size.appendChild(height);
    	size.appendChild(depth);
    	rootElement.appendChild(size);
    	
    	Element segmented = doc.createElement("segmented");
    	segmented.appendChild(doc.createTextNode(String.valueOf(0)));
    	rootElement.appendChild(segmented);
    	
    	for(ResizableRectangleWrapper rectWrapp: annotations) {
    		Element object = doc.createElement("object");
    		Element name = doc.createElement("name");
    		name.appendChild(doc.createTextNode(rectWrapp.getKlass()));
    		Element text = doc.createElement("text");
    		text.appendChild(doc.createTextNode(rectWrapp.getAdditionalText()));
    		Element pose = doc.createElement("pose");
    		pose.appendChild(doc.createTextNode("Unspecified"));
    		Element truncated = doc.createElement("truncated");
    		boolean n = rectWrapp.getTruncated();
    		if(n) {
    			truncated.appendChild(doc.createTextNode(String.valueOf(1)));
    		}
    		else {
    			truncated.appendChild(doc.createTextNode(String.valueOf(0)));
    		}
    		Element difficult = doc.createElement("difficult");
    		n = rectWrapp.getDifficult();
    		if(n) {
    			difficult.appendChild(doc.createTextNode(String.valueOf(1)));
    		}
    		else {
    			difficult.appendChild(doc.createTextNode(String.valueOf(0)));
    		}
    		Element overlap = doc.createElement("overlap");
    		n = rectWrapp.getOverlap();
    		if(n) {
    			overlap.appendChild(doc.createTextNode(String.valueOf(1)));
    		}
    		else {
    			overlap.appendChild(doc.createTextNode(String.valueOf(0)));
    		}
    		object.appendChild(name);
    		object.appendChild(text);
    		object.appendChild(pose);
    		object.appendChild(truncated);
    		object.appendChild(difficult);
    		object.appendChild(overlap);
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
        transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount","2");
        DOMSource source = new DOMSource(doc);
        String xmlPathString = dataSet.getDataSetAnnotationsLocation() + "\\" + listOfImg.getSelectionModel().getSelectedItem();
        if(xmlPathString.contains(".")) {
        	xmlPathString = xmlPathString.substring(0, xmlPathString.lastIndexOf('.'));
        }
        xmlPathString = xmlPathString + ".xml";
        StreamResult result = new StreamResult(new File(xmlPathString));
        transformer.transform(source, result);
        
        // Output to console for testing
        StreamResult consoleResult = new StreamResult(System.out);
        transformer.transform(source, consoleResult);
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * Method which is called when user clicks next(>) button.
     * If it has no next element in list, it go from start
     */
    @FXML
    private void handleNext() {
    	int i = listOfImg.getSelectionModel().getSelectedIndex();
    	//if index + 1 is greater then list size it means that we came to end of list and need to go from start again
    	if((i+1) >= imgs.size()){
    		i = -1;
    	}
    	String nextImg = imgs.get(i+1);
    	listOfImg.getSelectionModel().select(i+1);
    	listOfImg.getFocusModel().focus(i+1);
    	listOfImg.scrollTo(i+1);
    	showImage(nextImg);
    }
    
    /**
     * Method which is called when user clicks previous(<) button.
     * If it has no previous element in list, it go from end
     */
    @FXML
    private void handlePrevious() {
    	int i = listOfImg.getSelectionModel().getSelectedIndex();
    	if(i <= 0) {
    		i = imgs.size();
    	}
    	String nextImg = imgs.get(i-1);
    	listOfImg.getSelectionModel().select(i-1);
    	listOfImg.getFocusModel().focus(i-1);
    	listOfImg.scrollTo(i-1);
    	showImage(nextImg);
    }
    
   
    
    EventHandler<ActionEvent> onActionCrop = new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent event) {
        	String selectedImage = listOfImg.getSelectionModel().getSelectedItem();
        	if(selectedImage != null) {
        		Bounds selectionBounds = areaSelection.selectArea(imageGroup).getBoundsInParent();
        		
        		// crop the image
                crop( selectionBounds);
        	}
        	else {
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
    
    private void crop( Bounds bounds) {
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
        parameters.setViewport(new Rectangle2D( bounds.getMinX(), bounds.getMinY(), width, height));

        WritableImage wi = new WritableImage( width, height);
        imageView.snapshot(parameters, wi);

        // save image 
        // !!! has bug because of transparency (use approach below) !!!
        // --------------------------------
//        try {
//          ImageIO.write(SwingFXUtils.fromFXImage( wi, null), "jpg", file);
//      } catch (IOException e) {
//          e.printStackTrace();
//      }


        // save image (without alpha)
        // --------------------------------
        BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
        BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);

        Graphics2D graphics = bufImageRGB.createGraphics();
        graphics.drawImage(bufImageARGB, 0, 0, null);

        try {

            ImageIO.write(bufImageRGB, "jpg", file); 

            System.out.println( "Image saved to " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }

        graphics.dispose();

    }
    
    public void clearSelection(Group group) {
        //deletes everything except for base container layer
        isAreaSelected = false;
        group.getChildren().remove(1,group.getChildren().size());
        index = 0;

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
    		if(event.isSecondaryButtonDown()) {
        		System.out.println("desni");
        		isSecondary = true;
        		return;
        	}
    		for(ResizableRectangleWrapper rectWrap: annotations) {
    			rectWrap.getRectangle().setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.5));
    		}
    		annotationsTable.getSelectionModel().clearSelection();
    		isSecondary = false;
            editButton.setDisable(true);
        	deleteButton.setDisable(true);
            rectangleStartX = event.getX();
            rectangleStartY = event.getY();

            //clearSelection(group);

            selectionRectangle = new ResizableRectangle(rectangleStartX, rectangleStartY, 0, 0, group, annotations, annotationsTable, imageView, mainApp);

        };
        
        EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
            if (event.isSecondaryButtonDown())
                return;

            double offsetX = event.getX() - rectangleStartX;
            double offsetY = event.getY() - rectangleStartY;

            if (offsetX > 0) {
                if (event.getX() > imageView.getFitWidth()) {
                    selectionRectangle.setWidth(imageView.getFitWidth() - rectangleStartX);
                    System.out.println(imageView.getFitWidth());
                }
                else
                    selectionRectangle.setWidth(offsetX);
            } else {
                if (event.getX() < 0)
                    selectionRectangle.setX(0);
                else
                    selectionRectangle.setX(event.getX());
                selectionRectangle.setWidth(rectangleStartX - selectionRectangle.getX());
            }

            if (offsetY > 0) {
                if (event.getY() > imageView.getFitHeight())
                    selectionRectangle.setHeight(imageView.getFitHeight() - rectangleStartY);
                else
                    selectionRectangle.setHeight(offsetY);
            } else {
                if (event.getY() < 0)
                    selectionRectangle.setY(0);
                else
                    selectionRectangle.setY(event.getY());
                selectionRectangle.setHeight(rectangleStartY - selectionRectangle.getY());
            }

        };
        
        EventHandler<MouseEvent> onMouseReleasedEventHandler = event -> {
            if (selectionRectangle != null && !isSecondary) {
            	isAreaSelected = true;

            	String selectedImage = listOfImg.getSelectionModel().getSelectedItem();
            	if(selectedImage != null) {
            		index = index +1;
            		ResizableRectangleWrapper annotation = mainApp.showAnnotateDialog(imageGroup, selectionRectangle, index);
            		if(annotation != null) {
            			if(annotations.isEmpty()) {
            				annotations.add(annotation);
            				annotationsTable.setItems(annotations);
            			}
            			else {
            				annotations.add(annotation);
            			}
            		}
            		else {
            			index = index - 1;
            		}
            	}
            }
            
        };
    }
    
    
	
}
