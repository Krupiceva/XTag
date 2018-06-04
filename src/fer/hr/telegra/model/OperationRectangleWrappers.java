package fer.hr.telegra.model;

public class OperationRectangleWrappers {

	private ResizableRectangleWrapper rectangleWrapperNew;
	private ResizableRectangleWrapper rectangleWrapperOld;
	
	public OperationRectangleWrappers (ResizableRectangleWrapper newRectangle,ResizableRectangleWrapper oldRectangle) {
		this.rectangleWrapperNew = newRectangle;
		this.rectangleWrapperOld = oldRectangle;
	}
	
	public ResizableRectangleWrapper getRectangleWrapperNew() {
		return rectangleWrapperNew;
	}
	
	public void setRectangleWrapperNew(ResizableRectangleWrapper newRectangle) {
		this.rectangleWrapperNew = newRectangle;
	}
	
	public ResizableRectangleWrapper getRectangleWrapperOld() {
		return rectangleWrapperOld;
	}
	
	public void setRectangleWrapperOld(ResizableRectangleWrapper oldRectangle) {
		this.rectangleWrapperOld = oldRectangle;
	} 
}
