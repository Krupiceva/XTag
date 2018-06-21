package fer.hr.telegra.model;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import fer.hr.telegra.MainApp;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
/**
 * Model class for bounding box around annotation
 * It can be resized and moved in scene
 * aspect ratio can be set and fixed
 * @author dmlinaric
 *
 */
public class ResizableRectangle extends Rectangle {

	private double rectangleStartX;
    private double rectangleStartY;
    private double mouseClickPozX;
    private double mouseClickPozY;
    private double aspectRatio;
    private static final double RESIZER_SQUARE_SIDE = 4;
    private Paint resizerSquareColor = Color.WHITE;
    private ImageView imageView;
    private MainApp mainApp;

    public ResizableRectangle(double x, double y, double width, double height, double aspectRatio, Group group, ObservableList<ResizableRectangleWrapper> annotations, TableView<ResizableRectangleWrapper> annotationsTable, ImageView imageView, MainApp mainApp) {
    	super(x,y,width,height);
        this.imageView = imageView;
        this.aspectRatio = aspectRatio;
        this.mainApp = mainApp;
        group.getChildren().add(this);
        super.setStroke(mainApp.getDefaultBorderColor());
        super.setStrokeWidth(1);
        super.setStrokeType(StrokeType.INSIDE);
        super.setFill(mainApp.getDefaultColor());

        /**
         * Invisible rectangle that is the same like corresponding rectangle (this)
         * It is bind with this rectangle and it is used for event handling
         */
        Rectangle moveRect = new Rectangle(0,0,0,0);
        moveRect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0));
        moveRect.xProperty().bind(super.xProperty());
        moveRect.yProperty().bind(super.yProperty());
        moveRect.widthProperty().bind(super.widthProperty());
        moveRect.heightProperty().bind(super.heightProperty());

        group.getChildren().add(moveRect);

        moveRect.addEventHandler(MouseEvent.MOUSE_ENTERED, event ->{
            moveRect.getParent().setCursor(Cursor.HAND);
        });

        moveRect.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
        	if(event.getButton() != MouseButton.PRIMARY) {
        		return;
        	}
            moveRect.getParent().setCursor(Cursor.MOVE);
            mouseClickPozX = event.getX();
            mouseClickPozY = event.getY();
            
            /**
             * select rectangle on which is mouse clicked (change the color and select corresponding rectangle in table with all annotations)
             */
            for(ResizableRectangleWrapper rectWrap: annotations) {
    			int xmin = rectWrap.getXMin();
    			int xmax = rectWrap.getXMax();
    			int ymin = rectWrap.getYMin();
    			int ymax = rectWrap.getYMax();
    			int curx = (int) event.getX();
    			int cury = (int) event.getY();
    			rectWrap.getRectangle().setFill(mainApp.getColorOfClasses().get(rectWrap.getKlass()).get());
    			if(curx > xmin && curx < xmax && cury > ymin && cury < ymax) {
    				rectWrap.getRectangle().setFill(Color.WHITE.deriveColor(0, 1.2, 1, 0.2));;
    				annotationsTable.getSelectionModel().select(rectWrap);
    			}
    		}
            /**
             * On double click open edit dialog for this annotation rectangle
             */
            if(event.getClickCount() == 2) {
            	ResizableRectangleWrapper rect = annotationsTable.getSelectionModel().getSelectedItem();
            	//First reduce number of this annotation class
            	if(mainApp.openController.getDataSet().getAnnotations().containsKey(rect.getKlass())) {
    				int num = mainApp.openController.getDataSet().getAnnotations().get(rect.getKlass());
    				num = num - 1;
    				if(num == 0) {
    					mainApp.openController.getDataSet().getAnnotations().remove(rect.getKlass());
    				}
    				else {
    					mainApp.openController.getDataSet().getAnnotations().put(rect.getKlass(), num);
    				}
    			}
            	
            	mainApp.showEditAnnotationDialog(rect, rect.getIndex(), annotations);
            	
            	//Refresh border color of this annotation
        		boolean overlap = rect.getOverlap();
        		boolean truncated = rect.getTruncated();
        		boolean difficult = rect.getDifficult();
        		if(overlap && truncated) {
        			rect.getRectangle().setStroke(mainApp.getColorsOfFlags().get("Overlap&Truncated").get());
        		}
        		else if(overlap) {
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
        		
        		//In the end increase number of this annotation class
        		if(mainApp.openController.getDataSet().getAnnotations().containsKey(rect.getKlass())) {
    				int num = mainApp.openController.getDataSet().getAnnotations().get(rect.getKlass());
    				num = num + 1;
    				mainApp.openController.getDataSet().getAnnotations().put(rect.getKlass(), num);
    			}
    			else {
    				mainApp.openController.getDataSet().getAnnotations().put(rect.getKlass(), 1);
    			}
        		
        		mainApp.openController.handleSaveButton();
            }
        });

        moveRect.addEventHandler(MouseEvent.MOUSE_RELEASED, event ->{
                moveRect.getParent().setCursor(Cursor.HAND);
                mainApp.openController.handleSaveButton();
        });
        moveRect.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
                moveRect.getParent().setCursor(Cursor.DEFAULT);
        });

        moveRect.addEventHandler(MouseEvent.MOUSE_DRAGGED,event -> {
        	if(event.getButton() != MouseButton.PRIMARY) {
        		return;
        	}

            double offsetX = event.getX() - mouseClickPozX;
            double offsetY = event.getY() - mouseClickPozY;
            double newX = super.getX() + offsetX ;
            double newY = super.getY() + offsetY ;
            if (newX >= 0 && newX + super.getWidth() <= imageView.getFitWidth() ) {
                super.setX(newX);
            }

            if (newY >= 0 && newY + super.getHeight() <= imageView.getFitHeight()) {
                super.setY(newY);
            }
            mouseClickPozX = event.getX();
            mouseClickPozY = event.getY();
            
        });


        makeNWResizerSquare(group);
        makeCWResizerSquare(group);
        makeSWResizerSquare(group);
        makeSCResizerSquare(group);
        makeSEResizerSquare(group);
        makeCEResizerSquare(group);
        makeNEResizerSquare(group);
        makeNCResizerSquare(group);


    }
    /**
     * Methods for making 8 small rectangles for resizing the main rectangle
     * @param group
     */
    private void makeNWResizerSquare(Group group) {
        Rectangle squareNW = new Rectangle(RESIZER_SQUARE_SIDE,RESIZER_SQUARE_SIDE);

        squareNW.xProperty().bind(super.xProperty().subtract(squareNW.widthProperty().divide(2.0)));
        squareNW.yProperty().bind(super.yProperty().subtract(squareNW.heightProperty().divide(2.0)));
        group.getChildren().add(squareNW);

        squareNW.addEventHandler(MouseEvent.MOUSE_ENTERED,event ->
                squareNW.getParent().setCursor(Cursor.NW_RESIZE));

        prepareResizerSquare(squareNW);

        squareNW.addEventHandler(MouseEvent.MOUSE_DRAGGED,event -> {
            rectangleStartX = super.getX();
            rectangleStartY = super.getY();
            double offsetX = event.getX() - rectangleStartX;
            double offsetY = event.getY() - rectangleStartY;
            double newX = super.getX() + offsetX ;
            double newY = super.getY() + offsetY ;

            if(aspectRatio != 0) {
            	if (newX >= 0 && newX <= super.getX() + super.getWidth() ) {
            		offsetY = offsetX / aspectRatio;
            		if (super.getY() + offsetY > 0) {
		                super.setX(newX);
		                super.setWidth(super.getWidth() - offsetX);
		                
		                newY = super.getY() + offsetY ;
		                super.setY(newY);
		                super.setHeight(super.getWidth()/aspectRatio);
            		}
	            }
            }
            else {
	            if (newX >= 0 && newX <= super.getX() + super.getWidth() ) {
	                super.setX(newX);
	                super.setWidth(super.getWidth() - offsetX);
	            }
	
	            if (newY >= 0 && newY <= super.getY() + super.getHeight() ) {
	                super.setY(newY);
	                super.setHeight(super.getHeight() - offsetY);
	            }
            }

        });
    }

    private void makeCWResizerSquare(Group group) {
        Rectangle squareCW = new Rectangle(RESIZER_SQUARE_SIDE,RESIZER_SQUARE_SIDE);
        squareCW.xProperty().bind(super.xProperty().subtract(squareCW.widthProperty().divide(2.0)));
        squareCW.yProperty().bind(super.yProperty().add(super.heightProperty().divide(2.0).subtract(
                squareCW.heightProperty().divide(2.0))));
        group.getChildren().add(squareCW);

        squareCW.addEventHandler(MouseEvent.MOUSE_ENTERED,event ->
                squareCW.getParent().setCursor(Cursor.W_RESIZE));

        prepareResizerSquare(squareCW);

        squareCW.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            rectangleStartX = super.getX();
            double offsetX = event.getX() - rectangleStartX;
            double newX = super.getX() + offsetX;

            if(aspectRatio != 0) {
            	 if (newX >= 0 && newX <= super.getX() + super.getWidth() - 5) {
            		 if(super.getY() + (super.getWidth() - offsetX)/aspectRatio <= imageView.getFitHeight()) {
	 	                super.setX(newX);
	 	                super.setWidth(super.getWidth() - offsetX);
	 	                super.setHeight(super.getWidth()/aspectRatio);
            		 }
 	            }
            }
            else {
	            if (newX >= 0 && newX <= super.getX() + super.getWidth() - 5) {
	                super.setX(newX);
	                super.setWidth(super.getWidth() - offsetX);
	            }
            }

        });

    }

    private void makeSWResizerSquare(Group group) {
        Rectangle squareSW = new Rectangle(RESIZER_SQUARE_SIDE,RESIZER_SQUARE_SIDE);
        squareSW.xProperty().bind(super.xProperty().subtract(squareSW.widthProperty().divide(2.0)));
        squareSW.yProperty().bind(super.yProperty().add(super.heightProperty().subtract(
                squareSW.heightProperty().divide(2.0))));
        group.getChildren().add(squareSW);

        squareSW.addEventHandler(MouseEvent.MOUSE_ENTERED,event ->
                squareSW.getParent().setCursor(Cursor.SW_RESIZE));

        prepareResizerSquare(squareSW);

        squareSW.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            rectangleStartX = super.getX();
            rectangleStartY = super.getY();
            double offsetX = event.getX() - rectangleStartX;
            double offsetY = event.getY() - rectangleStartY;
            double newX = super.getX() + offsetX;

            
            if(aspectRatio != 0) {
            	if (newX >= 0 && newX <= super.getX() + super.getWidth() - 5) {
            		if(super.getY() + (super.getWidth() - offsetX)/aspectRatio <= imageView.getFitHeight()) {
	                    super.setX(newX);
	                    super.setWidth(super.getWidth() - offsetX);
	                    super.setHeight(super.getWidth()/aspectRatio);
            		}
                }
            }
            else {
            	if (newX >= 0 && newX <= super.getX() + super.getWidth() - 5) {
                    super.setX(newX);
                    super.setWidth(super.getWidth() - offsetX);
                }
            	if (offsetY >= 0 && offsetY + super.getY() <= imageView.getFitHeight() ) {
            		super.setHeight(offsetY);
            	}
            }
        });
    }

    private void makeSCResizerSquare(Group group) {
        Rectangle squareSC = new Rectangle(RESIZER_SQUARE_SIDE,RESIZER_SQUARE_SIDE);

        squareSC.xProperty().bind(super.xProperty().add(super.widthProperty().divide(2.0).subtract(
                squareSC.widthProperty().divide(2.0))));
        squareSC.yProperty().bind(super.yProperty().add(super.heightProperty().subtract(
                squareSC.heightProperty().divide(2.0))));
        group.getChildren().add(squareSC);

        squareSC.addEventHandler(MouseEvent.MOUSE_ENTERED,event ->
                squareSC.getParent().setCursor(Cursor.S_RESIZE));

        prepareResizerSquare(squareSC);

        squareSC.addEventHandler(MouseEvent.MOUSE_DRAGGED,event -> {
            rectangleStartX = super.getX();
            rectangleStartY = super.getY();
            
            double offsetY = event.getY() - rectangleStartY;

            if(aspectRatio != 0) {
            	if (offsetY >= 0 && offsetY + super.getY() <= imageView.getFitHeight()) {
            		if(super.getX() + offsetY*aspectRatio <= imageView.getFitWidth()) {
		                super.setHeight(offsetY);
		                super.setWidth(super.getHeight()*aspectRatio);
            		}
	            }
            }
            else {
	            if (offsetY >= 0 && offsetY + super.getY() <= imageView.getFitHeight()) {
	                super.setHeight(offsetY);
	            }
            }
        });
    }

    private void makeSEResizerSquare(Group group) {
        Rectangle squareSE = new Rectangle(RESIZER_SQUARE_SIDE,RESIZER_SQUARE_SIDE);
        squareSE.xProperty().bind(super.xProperty().add(super.widthProperty()).subtract(
                squareSE.widthProperty().divide(2.0)));
        squareSE.yProperty().bind(super.yProperty().add(super.heightProperty().subtract(
                squareSE.heightProperty().divide(2.0))));
        group.getChildren().add(squareSE);

        squareSE.addEventHandler(MouseEvent.MOUSE_ENTERED,event ->
                squareSE.getParent().setCursor(Cursor.SE_RESIZE));

        prepareResizerSquare(squareSE);

        squareSE.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            rectangleStartX = super.getX();
            rectangleStartY = super.getY();
            double offsetX = event.getX() - rectangleStartX;
            double offsetY = event.getY() - rectangleStartY;

            if(aspectRatio != 0) {
            	if (offsetX >= 0 && offsetX + super.getX() <= imageView.getFitWidth()) {
            		if(super.getY() + offsetX/aspectRatio <= imageView.getFitHeight()) {
		                super.setWidth(offsetX);
		                super.setHeight(super.getWidth()/aspectRatio);
            		}
	            }
            }
            else {
	            if (offsetX >= 0 && offsetX + super.getX() <= imageView.getFitWidth()) {
	                super.setWidth(offsetX);
	            }
	
	            if (offsetY >= 0 && offsetY + super.getY() <= imageView.getFitHeight()) {
	                super.setHeight(offsetY);
	            }
            }
        });
    }

    private void makeCEResizerSquare(Group group) {
        Rectangle squareCE = new Rectangle(RESIZER_SQUARE_SIDE,RESIZER_SQUARE_SIDE);
        squareCE.xProperty().bind(super.xProperty().add(super.widthProperty()).subtract(
                squareCE.widthProperty().divide(2.0)));
        squareCE.yProperty().bind(super.yProperty().add(super.heightProperty().divide(2.0).subtract(
                squareCE.heightProperty().divide(2.0))));
        group.getChildren().add(squareCE);

        squareCE.addEventHandler(MouseEvent.MOUSE_ENTERED,event ->
                squareCE.getParent().setCursor(Cursor.E_RESIZE));

        prepareResizerSquare(squareCE);

        squareCE.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            rectangleStartX = super.getX();
            double offsetX = event.getX() - rectangleStartX;
            if(aspectRatio != 0) {
            	if (offsetX >= 0 && offsetX + super.getX() <= imageView.getFitWidth()) {
            		if(super.getY() + offsetX/aspectRatio <= imageView.getFitHeight()) {
		                super.setWidth(offsetX);
		                super.setHeight(super.getWidth()/aspectRatio);
            		}
	            }
            }
            else {
	            if (offsetX >= 0 && offsetX + super.getX() <= imageView.getFitWidth()) {
	                super.setWidth(offsetX);
	            }
            }

        });
    }

    private void makeNEResizerSquare(Group group){
        Rectangle squareNE = new Rectangle(RESIZER_SQUARE_SIDE,RESIZER_SQUARE_SIDE);

        squareNE.xProperty().bind(super.xProperty().add(super.widthProperty()).subtract(
                squareNE.widthProperty().divide(2.0)));
        squareNE.yProperty().bind(super.yProperty().subtract(squareNE.heightProperty().divide(2.0)));
        group.getChildren().add(squareNE);

        squareNE.addEventHandler(MouseEvent.MOUSE_ENTERED,event ->
                squareNE.getParent().setCursor(Cursor.NE_RESIZE));

        prepareResizerSquare(squareNE);

        squareNE.addEventHandler(MouseEvent.MOUSE_DRAGGED,event -> {
            rectangleStartX = super.getX();
            rectangleStartY = super.getY();
            double offsetX = event.getX() - rectangleStartX;
            double offsetY = event.getY() - rectangleStartY;
            double newY = super.getY() + offsetY ;

            if(aspectRatio != 0) {
            	if (newY >= 0 && newY <= super.getY() + super.getHeight() - 5) {
            		if(super.getX() + (super.getHeight() - offsetY)*aspectRatio <= imageView.getFitWidth()) {
		                super.setY(newY);
		                super.setHeight(super.getHeight() - offsetY);
		                super.setWidth(super.getHeight()*aspectRatio);
            		}
	            }
            }
            else {
	            if (offsetX >= 0 && offsetX + super.getX() <= imageView.getFitWidth()) {
	                super.setWidth(offsetX);
	            }
	
	            if (newY >= 0 && newY <= super.getY() + super.getHeight() - 5) {
	                super.setY(newY);
	                super.setHeight(super.getHeight() - offsetY);
	            }
            }

        });
    }

    private void makeNCResizerSquare(Group group){
        Rectangle squareNC = new Rectangle(RESIZER_SQUARE_SIDE,RESIZER_SQUARE_SIDE);

        squareNC.xProperty().bind(super.xProperty().add(super.widthProperty().divide(2.0).subtract(
                squareNC.widthProperty().divide(2.0))));
        squareNC.yProperty().bind(super.yProperty().subtract(
                squareNC.heightProperty().divide(2.0)));
        group.getChildren().add(squareNC);

        squareNC.addEventHandler(MouseEvent.MOUSE_ENTERED,event ->
                squareNC.getParent().setCursor(Cursor.N_RESIZE));

        prepareResizerSquare(squareNC);

        squareNC.addEventHandler(MouseEvent.MOUSE_DRAGGED,event -> {
            rectangleStartY = super.getY();
            double offsetY = event.getY() - rectangleStartY;
            double newY = super.getY() + offsetY ;

            if(aspectRatio != 0) {
            	if (newY >= 0 && newY <= super.getY() + super.getHeight()) {
            		if(super.getX() + (super.getHeight() - offsetY)*aspectRatio <= imageView.getFitWidth()) {
		                super.setY(newY);
		                super.setHeight(super.getHeight() - offsetY);
		                super.setWidth(super.getHeight()*aspectRatio);
            		}
	            }
            }
            else {
	            if (newY >= 0 && newY <= super.getY() + super.getHeight()) {
	                super.setY(newY);
	                super.setHeight(super.getHeight() - offsetY);
	            }
            }

        });
    }

    private void prepareResizerSquare(Rectangle rect) {
        rect.setFill(resizerSquareColor);

        rect.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
                rect.getParent().setCursor(Cursor.DEFAULT);
                mainApp.openController.handleSaveButton();
        });
    }

    public double getAspectRatio() {
    	return aspectRatio;
    }
	
}
