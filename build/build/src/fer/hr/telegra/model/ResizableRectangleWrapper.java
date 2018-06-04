package fer.hr.telegra.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Bounds;

public class ResizableRectangleWrapper {

	private ResizableRectangle rectangle;
	private StringProperty klass;
	private StringProperty color;
	private StringProperty additionalText;
	private BooleanProperty isTruncated;
	private BooleanProperty isDifficult;
	private BooleanProperty isOverlap;
	private DoubleProperty aspectRatio;
	private IntegerProperty xMin;
	private IntegerProperty yMin;
	private IntegerProperty xMax;
	private IntegerProperty yMax;
	private IntegerProperty index;
	
	public ResizableRectangleWrapper() {
		
	}
	
	public ResizableRectangleWrapper(ResizableRectangle rectangle, Integer index) {
		this.rectangle = rectangle;
		this.index = new SimpleIntegerProperty(index);
		Bounds bounds = this.rectangle.getBoundsInParent();
		IntegerProperty widthProp = new SimpleIntegerProperty(0);
		IntegerProperty heightProp = new SimpleIntegerProperty(0);
		this.klass = new SimpleStringProperty("");
		this.color = new SimpleStringProperty("");
		this.additionalText = new SimpleStringProperty("");
		this.isTruncated = new SimpleBooleanProperty(false);
		this.isDifficult = new SimpleBooleanProperty(false);
		this.isOverlap = new SimpleBooleanProperty(false);
		this.xMin = new SimpleIntegerProperty((int) bounds.getMinX());
		this.yMin = new SimpleIntegerProperty((int) bounds.getMinY());
		this.xMax = new SimpleIntegerProperty((int) bounds.getMaxX());
		this.yMax = new SimpleIntegerProperty((int) bounds.getMaxY());
		xMin.bind(rectangle.xProperty());
		yMin.bind(rectangle.yProperty());
		widthProp.bind(rectangle.widthProperty());
		heightProp.bind(rectangle.heightProperty());
		xMax.bind(rectangle.xProperty().add(widthProp));
		yMax.bind(rectangle.yProperty().add(heightProp));
		this.aspectRatio = new SimpleDoubleProperty(this.rectangle.getAspectRatio());
		
	}
	
	public ResizableRectangle getRectangle() {
		return rectangle;
	}
	
	public String getKlass() {
		return klass.get();
	}
	
	public void setKlass(String klass) {
		this.klass.set(klass);
	}
	
	public StringProperty klassProperty() {
		return klass;
	}
	
	public String getColor() {
		return color.get();
	}
	
	public void setColor(String color) {
		this.color.set(color);
	}
	
	public StringProperty colorProperty() {
		return color;
	}
	
	public String getAdditionalText() {
		return additionalText.get();
	}
	
	public void setAdditionalText(String additionalText) {
		this.additionalText.set(additionalText);
	}
	
	public StringProperty additionalTextProperty() {
		return additionalText;
	}
	
	public Boolean getTruncated() {
		return isTruncated.get();
	}
	
	public void setTruncated(Boolean isTruncated) {
		this.isTruncated.set(isTruncated);
	}
	
	public BooleanProperty isTruncatedProperty() {
		return isTruncated;
	}
	
	public Boolean getDifficult() {
		return isDifficult.get();
	}
	
	public void setDifficult(Boolean isDifficult) {
		this.isDifficult.set(isDifficult);
	}
	
	public BooleanProperty isDifficultProperty() {
		return isDifficult;
	}
	
	public Boolean getOverlap() {
		return isOverlap.get();
	}
	
	public void setOverlap(Boolean isOverlap) {
		this.isOverlap.set(isOverlap);
	}
	
	public BooleanProperty isOverlapProperty() {
		return isOverlap;
	}
	
	public Integer getXMin() {
		return xMin.get();
	}
	
	public void setXMin(Integer xMin) {
		this.xMin.set(xMin);
	}
	
	public IntegerProperty xMinProperty() {
		return xMin;
	}
	
	public Integer getYMin() {
		return yMin.get();
	}
	
	public void setYMin(Integer yMin) {
		this.yMin.set(yMin);
	}
	
	public IntegerProperty yMinProperty() {
		return yMin;
	}
	
	public Integer getXMax() {
		return xMax.get();
	}
	
	public void setXMax(Integer xMax) {
		this.xMax.set(xMax);
	}
	
	public IntegerProperty xMaxProperty() {
		return xMax;
	}
	
	public Integer getYMax() {
		return yMax.get();
	}
	
	public void setYMax(Integer yMax) {
		this.yMax.set(yMax);
	}
	
	public IntegerProperty yMaxProperty() {
		return yMax;
	}
	
	public Integer getIndex() {
		return index.get();
	}
	
	public void setIndex(Integer index) {
		this.index.set(index);
	}
	
	public IntegerProperty indexProperty() {
		return index;
	}
	
	public Double getAspectRatio() {
		return aspectRatio.get();
	}
	
	public void setAspectRatio(Double aspectRatio) {
		this.aspectRatio.set(aspectRatio);
	}
	
	public DoubleProperty aspectRatioProperty() {
		return aspectRatio;
	}
}
