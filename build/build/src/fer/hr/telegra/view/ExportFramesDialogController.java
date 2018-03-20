package fer.hr.telegra.view;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

//import org.opencv.core.Mat;
//import org.opencv.videoio.VideoCapture;

import fer.hr.telegra.MainApp;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class ExportFramesDialogController {

    private MainApp mainApp;
	private Stage dialogStage;
	
	@FXML
	private TextField aviLocation;
	@FXML
	private TextField exportLocation;
	@FXML
	private TextField frames;
	@FXML
	private TextArea textArea;
	@FXML
	private TextField maxNumberOfImages;
	@FXML
	private TextField imagePrefix;
    
	public ExportFramesDialogController() {}
	
	@FXML
    private void initialize() {
    	textArea.setEditable(false);
    	textArea.setWrapText(true);
    	
    }
	
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
	
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.setResizable(false);
    }
    
    @FXML
    private void handleBrowseAvi() {
    	FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "AVI files (*.avi)", "*.avi");
        fileChooser.getExtensionFilters().add(extFilter);
        
        // Show open file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if (file != null) {
        	aviLocation.setText(file.getAbsolutePath());
        }
    }
    
    @FXML
    private void handleBrowseFolder() {
    	DirectoryChooser directoryChooser = new DirectoryChooser();
    	File selectedDirectory = directoryChooser.showDialog(dialogStage);
    	if(selectedDirectory != null) {
    		exportLocation.setText(selectedDirectory.getAbsolutePath());
    	}
    }
    
//    @FXML
//    private void handleExport() {
//		// Make new thread for extracting frames, without that textarea in gui wont
//		// refresh!
//    	int frameNum = Integer.valueOf(frames.getText());
//		Thread thread = new Thread(() -> {
//			if (isInputValid()) {
//				VideoCapture video = new VideoCapture(aviLocation.getText());
//				// File file = new File(exportLocation.getText());
//				if (!video.isOpened()) {
//					writeLog("Error! Camera can't be opened!");
//					return;
//				}
//				Mat frame = new Mat();
//				boolean hasNext = true;
//				int i = 0;
//				int j = 0;
//				writeLog("Exporting started...");
//				
//				while (hasNext) {
//					hasNext = video.read(frame);
//					if (i % frameNum == 0) {
//
//						System.out.println("Frame Obtained");
//						writeLog("Frame Obtained");
//						System.out.println("Captured Frame Width " + frame.width() + " Height " + frame.height());
//						writeLog("Captured Frame Width " + frame.width() + " Height " + frame.height());
//						//Imgcodecs.imwrite("camera.jpg", frame);
//						System.out.println("OK");
//						writeLog("OK");
//						BufferedImage bImage = matToBufferedImage(frame);
//						try {
//							String path = exportLocation.getText() + "\\img" + j + ".jpg";
//							File file = new File(path);
//							ImageIO.write(bImage, "jpg", file);
//							System.out.println("Image saved to " + file.getAbsolutePath());
//							writeLog("Image saved to " + file.getAbsolutePath());
//						} catch (IOException e) {
//							throw new RuntimeException(e);
//						}
//						j++;
//					}
//					i++;
//				}
//				writeLog("Exporting Completed!");
//				video.release();
//				video = null;
//				frame.release();
//				frame = null;
//
//			}
//			System.gc();
//		});
//		thread.start();
//    }
    
	@FXML
	private void handleExport1() {
		if (isInputValid()) {
			int frameNum = Integer.valueOf(frames.getText());
			// Make new thread for extracting frames, without that textarea in gui wont
			Thread thread = new Thread(() -> {
				FFmpegFrameGrabber g = new FFmpegFrameGrabber(aviLocation.getText());
				try {
					g.start();
					Frame frame = null;
					int j = 0;
					int i = 0;
					int limit = Integer.valueOf(maxNumberOfImages.getText());
					writeLog("Exporting started...\nPlease wait...");
					System.out.println(g.getLengthInFrames());
					if (limit > g.getLengthInFrames() / frameNum) {
						limit = g.getLengthInFrames() / frameNum;
					}
					//TODO napravit za avije bez duljine :D
					while (j < limit) {
						System.out.print(j);
						frame = g.grab();
						if (i % frameNum == 0) {
							Java2DFrameConverter cnvrt = new Java2DFrameConverter();
							BufferedImage bImage = cnvrt.convert(frame);
							if (bImage == null) {
								System.out.println("null je");
								break;
							}
							String path = exportLocation.getText() + "\\" + imagePrefix.getText() + j + ".jpg";
							File file = new File(path);
							ImageIO.write(bImage, "jpg", file);
							writeLog("Image saved to " + file.getAbsolutePath());
							j++;
						}
						i++;
					}
					writeLog("Exporting Completed!");
					writeLog("Number of images exported:");
					writeLog(String.valueOf(j));
					writeLog("Number of frames in video:");
					writeLog(String.valueOf(g.getLengthInFrames()));
					g.close();
					frame = null;

				} catch (Exception e) {
					e.printStackTrace();
				}
				g = null;
				System.gc();

			});
			thread.start();
		}
	}
    
    private boolean isInputValid() {
    	String errorMessage = "";
    	if(aviLocation.getText() == null || aviLocation.getText().length() == 0) {
    		errorMessage += "No valid avi location!\n";
    	}
    	if(exportLocation.getText() == null || exportLocation.getText().length() == 0) {
    		errorMessage += "No valid export location!\n";
    	}
    	if(frames.getText() == null || frames.getText().length() == 0) {
    		errorMessage += "No valid each frames number!\n";
    	}
    	else{
    		try{
    			  @SuppressWarnings("unused")
				int num = Integer.parseInt(frames.getText());
    		} catch (NumberFormatException e) {
    			  errorMessage += "No valid each frames number!\n";
    		}
    	}
    	if(imagePrefix.getText() == null || imagePrefix.getText().length() == 0) {
    		errorMessage += "No valid image prefix!\n";
    	}
    	if(maxNumberOfImages.getText() == null || maxNumberOfImages.getText().length() == 0) {
    		errorMessage += "No valid max number of images!\n";
    	}
    	else {
    		try{
  			  @SuppressWarnings("unused")
			int num = Integer.parseInt(maxNumberOfImages.getText());
  		} catch (NumberFormatException e) {
  			  errorMessage += "No valid max number of images!\n";
  		}
    	}
    	//If input is ok
    	if (errorMessage.length() == 0) {
    		return true;
    	}
    	else {
    		//Show error alert
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.initOwner(dialogStage);
    		alert.setTitle("Invalid Fields!");
    		alert.setHeaderText("Please correct invalid fields.");
    		alert.setContentText(errorMessage);
    		
    		alert.showAndWait();
    		
    		return false;
    	}
    }
    
//    public static Image mat2Image(Mat frame)
//	{
//		try
//		{
//			return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
//		}
//		catch (Exception e)
//		{
//			System.err.println("Cannot convert the Mat obejct: " + e);
//			return null;
//		}
//	}
//    
//    private static BufferedImage matToBufferedImage(Mat original)
//	{
//		// init
//		BufferedImage image = null;
//		int width = original.width(), height = original.height(), channels = original.channels();
//		byte[] sourcePixels = new byte[width * height * channels];
//		original.get(0, 0, sourcePixels);
//		
//		if (original.channels() > 1)
//		{
//			image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
//		}
//		else
//		{
//			image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
//		}
//		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
//		System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
//		
//		return image;
//	}
    
    private void writeLog(String message) {
    	System.gc();
        if (Platform.isFxApplicationThread()) {
            textArea.appendText(message + "\n");
        } else {
            Platform.runLater(() -> textArea.appendText(message + "\n"));
        }
    }
}
