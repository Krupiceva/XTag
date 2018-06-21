package fer.hr.telegra.model;

/**
 * Class which is wrapper for wrapper around resizable rectangle that represent annotation
 * it has two wrapper around resizable rectangle, old and new
 * new - wrapper around annotation when adding new annotation or after edit existing annotation
 * old - wrapper around annotation when deleting that annotation or before edit existing annotation
 * @author dmlinaric
 *
 */
public class OperationRectangleWrappers {

	private ResizableRectangleWrapper rectangleWrapperNew;
	private ResizableRectangleWrapper rectangleWrapperOld;

	public OperationRectangleWrappers(ResizableRectangleWrapper newRectangle, ResizableRectangleWrapper oldRectangle) {
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
