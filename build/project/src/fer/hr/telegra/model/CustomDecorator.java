package fer.hr.telegra.model;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * JavaFX Custom Decorator
 *
 * @author Bhathiya
 */
public class CustomDecorator extends AnchorPane {

    private double xOffset = 0;
    private double yOffset = 0;
    private Stage primaryStage;

    public CustomDecorator(Stage stage, Node node) {
        super();

        primaryStage = stage;
        this.setPadding(new Insets(0, 0, 0, 0));
        // load css : 
        //this.getStylesheets().add("/openpimtests/Catra.css");

        Button btnMax = buildButton("Mx",(e) -> {
            primaryStage.setMaximized(!primaryStage.isMaximized());
        });
        AnchorPane.setRightAnchor(btnMax, 60.0);
        AnchorPane.setTopAnchor(btnMax, 10.0);
       
        Button btnClose = buildButton("X",(e) -> {
            primaryStage.close();
        });
        AnchorPane.setRightAnchor(btnClose, 10.0);
        AnchorPane.setTopAnchor(btnClose, 10.0);
        
        
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);

        this.getChildren().addAll(node,btnMax,btnClose);


        this.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        this.setOnMouseDragged((MouseEvent event) -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
    }

    private Button buildButton(String text, EventHandler<ActionEvent> onAction) {
        Button btn = new Button(text);
        btn.setMinSize(44, 44);
        btn.setMaxSize(44, 44);
        //set a style 
        //btn.getStyleClass().add("white-soft-button");
        btn.setOnAction(onAction);
        return btn;
    }
}