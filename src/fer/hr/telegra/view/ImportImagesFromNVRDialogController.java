package fer.hr.telegra.view;

import fer.hr.telegra.MainApp;
import fer.hr.telegra.model.DataImage;
import fer.hr.telegra.model.DataSet;
import fer.hr.telegra.model.NVRFetching;
import fer.hr.telegra.model.NVRStream;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

public class ImportImagesFromNVRDialogController {
	/**
	 * Progress bar that shows percent of importing images(frames) from NVR
	 */
	@FXML
	private ProgressBar progressBar;
	/**
	 * Progress indicator that shows percent of importing images(frames) from NVR
	 */
	@FXML
	private ProgressIndicator progressIndicator;
	/**
	 * Label that shows which image is current importing
	 */
	@FXML
	private Label statusLabel;
	/**
	 * Ok button that user press after images are imported
	 */
	@FXML
	private Button okButton;
	/**
	 * Flag that indicates if images are imported
	 */
	private boolean isOk = false;
	/**
	 * Reference to the mainApp
	 */
    @SuppressWarnings("unused")
	private MainApp mainApp;
    /**
     * Reference to the new data set
     */
    private DataSet dataSet;
    /**
	 * Stage of this dialog window
	 */
	private Stage dialogStage;
	/**
	 * Importtask for importing images that will be executed in separate theard
	 */
	ImportImagesTask importImagesTask;
	/**
	 * Number of images that need to be imported
	 */
	private Integer numberOfImages;
	private Thread thread;
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
	@FXML
    private void initialize() {
		okButton.setDisable(true);
	}
	
	/**
	 * Sets the reference to mainapp
	 * @param mainApp is reference to the mainapp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	/**
	 * Sets the dataset to be added in dialog
	 * @param dataSet is new dataset
	 */
	public void setDataSet(DataSet dataSet, Integer numberOfImages) {
		this.dataSet = dataSet;
		this.numberOfImages = numberOfImages;
		importImages();
	}
	/**
	 * Sets the stage of this dialog and disable resize.
	 * Set on close request: kill thread for importing
	 * @param dialogStage is stage of this dialog
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
		this.dialogStage.setResizable(false);
		this.dialogStage.setOnCloseRequest(event -> {
        	importImagesTask.shutdown();
        	isOk = false;
        });
	}
	
	/**
	 * @return true if images are imported, false otherwise 
	 */
	public boolean isOk() {
		return isOk;
	}
	
	/**
	 * Method which handle when user press ok
	 */
	@FXML
	private void handleOK() {
		isOk = true;
		this.dialogStage.close();
	}
	
	/**
	 * Main method for importing. Initialize parameters and start new thread with import task 
	 */
	private void importImages() {
		progressBar.setProgress(0);
        progressIndicator.setProgress(0);
        importImagesTask = new ImportImagesTask();
		progressBar.progressProperty().unbind();
		progressBar.progressProperty().bind(importImagesTask.progressProperty());
		progressIndicator.progressProperty().unbind();
		progressIndicator.progressProperty().bind(importImagesTask.progressProperty());
		statusLabel.textProperty().unbind();
		statusLabel.textProperty().bind(importImagesTask.messageProperty());
		importImagesTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {

                    @Override
                    public void handle(WorkerStateEvent t) {
                        statusLabel.textProperty().unbind();
                        statusLabel.setText("Imported!");
                        okButton.setDisable(false);
                        isOk = true;
                    }
                });
		 thread = new Thread(importImagesTask);
		 thread.start();
	}
	
	/**
	 * Custom task for image importing from NVR
	 * @author dmlinaric
	 *
	 */
	private class ImportImagesTask extends Task<Boolean>{
		private volatile boolean done = false;
		String location = dataSet.getDataSetImagesLocation();
		NVRStream stream = dataSet.getStreams().get(0);
		String lastTimestamp = stream.getStartTime();
		@Override
		protected Boolean call() throws Exception {
			int i = 0;
			long startTime = System.currentTimeMillis();
			while(i < numberOfImages && !done) {
				importImg(String.valueOf(i));
				lastTimestamp = NVRFetching.generateNVRImage(stream, location, lastTimestamp);
				if(lastTimestamp != null) {
					dataSet.addDataSetImage(new DataImage(lastTimestamp));
				}
				else {
					shutdown();
					this.updateMessage("Imported fewer images of a desired!");
					okButton.setDisable(false);
					continue;
				}
				lastTimestamp = lastTimestamp.substring(0, 15);
				i++;
				this.updateProgress(i, numberOfImages);
			}
			long stopTime = System.currentTimeMillis();
		    long elapsedTime = stopTime - startTime;
		    System.out.println(elapsedTime);
			
			return true;
		}
		
		private void importImg(String msg) throws Exception {
			this.updateMessage("Importing: " + msg);
		}

		public void shutdown() {
		    done = true;
		}
	}
}
