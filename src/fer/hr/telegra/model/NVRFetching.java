package fer.hr.telegra.model;

import java.io.File;
import java.sql.Timestamp;
import java.util.Random;
import javafx.collections.ObservableList;


/**
 * Class with static methods for fetching images(frames) from NVR
 * @author dmlinaric
 *
 */

public class NVRFetching {
	
	/**
	 * Instance of JNA api for fetching frames from NVR
	 */
	public static NVRFetchAPI nvr = NVRFetchAPI.INSTANCE;
	
	/**
	 * Method which fetch frame from NVR every second from given start timestamp of stream and save this image
	 * to the dataset image directory
	 * @param stream is NVR stream from which frame need to be fetched
	 * @param location is location of images on disk in dataset
	 * @param lastTimestamp last timestamp that is fetched
	 * @return
	 */
	public static String generateNVRImage(NVRStream stream, String location, String lastTimestamp) {
		Timestamp newTimestamp;
		String start = parseTime(stream.getStartTime());
		String end = parseTime(stream.getEndTime());
		String last = parseTime(lastTimestamp);
		
		long startTime = Timestamp.valueOf(start).getTime();
		long endTime = Timestamp.valueOf(end).getTime();
		long lastTime = Timestamp.valueOf(last).getTime();
		if(lastTime > endTime) {
			return null;
		}
		else {
			long newTime = lastTime + 1000L;
			newTimestamp = new Timestamp(newTime);
			String path = getFullPath(newTimestamp, location);
			String url = getFullUrl(newTimestamp, stream.getAddress());
			boolean sucess = getFrameFromNVR(url, path);
	        String imageName = parseTimestamp(newTimestamp) + ".jpg";
			return imageName;
		}

	}
	
	/**
	 * Method which fetch new random frame from NVR and save this new image to the dataset image direcotry
	 * @return string that represent name of new image (e.g. 20170827T211325.387.png)
	 */
	public static String generateRandomNVRimage(ObservableList<NVRStream> streams, String location) {
		//Choose random one of streams
		NVRStream randomStream = streams.get(new Random().nextInt(streams.size()));
		Timestamp randomTimestamp;
		while (timestampExistsOnDisk((randomTimestamp = generateRandomTimeStamp(randomStream.getStartTime(), randomStream.getEndTime())), location));
		
		String path = getFullPath(randomTimestamp, location);
		String url = getFullUrl(randomTimestamp, randomStream.getAddress());
		boolean sucess = getFrameFromNVR(url, path);
		
		String imageName = parseTimestamp(randomTimestamp) + ".jpg";
		
		return imageName;
	}
	
	/**
	 * Method which get full path of new image
	 * @param name is timestamp
	 * @param location is location where image need to be saved on disk
	 * @return full path location+timestamp+jpg
	 */
	public static String getFullPath(Timestamp name, String location) {
		String imageName;
		imageName = parseTimestamp(name) + ".jpg";
		String path = location + "\\" + imageName;
		return path;

	}
	
	/**
	 * Method which get full url for NVR
	 * Example: txvn://172.17.101.88:9801/600281@20170322T040000:86400000
	 * @param timestamp is time in yyyy-mm-dd hh:mm:ss.fff format 
	 * @param location is stream adress
	 * @return full url in format location+@+timestamp+:86400000
	 */
	public static String getFullUrl(Timestamp timestamp, String location) {
		String url = location + "@";
		String time = parseTimestamp(timestamp) + ":86400000";
		url = url + time; 
		return url;
		
	}

	/**
	 * Method that generate new frame from NVR
	 * this method call native code in DLL with JNA
	 * @param url is full stream url, example:  txvn://172.17.101.88:9801/600281@20170322T040000:86400000
	 * @param location is full path to new image
	 * @return true if images is successfully generate
	 */
	public static boolean getFrameFromNVR(String url, String location) {
		
//		Pointer result = nvr.get_frame_c("txvn://172.17.101.88:9801/600281@20170322T040000:86400000");
//		System.out.println("----JAVA code start:----");
//		
//		
//		byte[] bytes = result.getByteArray(0, 4);
//		
//		System.out.println(Arrays.toString(bytes));
//		
//		int width = (int) bytes[0] & 0xFF;
//		width *= 256;
//		int temp = (int) bytes[1] & 0xFF;
//		width += temp;
//		
//		int height = (int) bytes[2] & 0xFF;
//		height *= 256;
//		temp = (int) bytes[3] & 0xFF;
//		height += temp;
//		
//		System.out.println(width);
//		System.out.println(height);
//		
//		//bytes = result.getByteArray(4, width*height*3);
//		
//		for(int i = 0; i< height; i++) {
//			System.out.println(i);
//			bytes = result.getByteArray(4+i*width, width*3);
//		}
//		String dirName="D:\\work_in_progress\\TaggingAppTest";
//		
//		
//		System.out.println(bytes.length);
//		
//		nvr.free_string(result);
//		Mat frame = imread("D:\\work_in_progress\\TaggingAppTest\\_fakeimages\\slika.bmp");
		
		if(nvr.export_frame(url, location) == 1) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Method which generate random time stamp in range
	 * @param startTime is start of the time range (format: yyyyMMddTHHmmss)
	 * @param endTime is end of the time range (format: yyyyMMddTHHmmss)
	 * @return random time stamp (format: yyyy-mm-dd hh:mm:ss.fff)
	 */
	public static Timestamp generateRandomTimeStamp(String startTime, String endTime) {
		String newStart = parseTime(startTime);
		String newEnd = parseTime(endTime);
		
		long offset = Timestamp.valueOf(newStart).getTime();
		long end = Timestamp.valueOf(newEnd).getTime();
		long diff = end - offset + 1;
		Timestamp random = new Timestamp(offset + (long)(Math.random() * diff));
		
		return random;
	}
	
	/**
	 * Method which check if new timestamp exist in dataset image folder
	 * @param timestamp is timestamp of new image
	 * @param location is location of dataset images
	 * @return true if exists, false otherwise
	 */
	public static boolean timestampExistsOnDisk(Timestamp timestamp, String location) {
		String imageName = parseTimestamp(timestamp) + ".jpg";
		String path = location + "\\" + imageName;
	    File outputfile = new File(path);
	    return outputfile.exists();
	}
	
	/**
	 * Convert time in yyyyMMddTHHmmss format to yyyy-mm-dd hh:mm:ss
	 * @param time is time in yyyyMMddTHHmmss format
	 * @return time in yyyy-mm-dd hh:mm:ss format
	 */
	public static String parseTime(String time) {
		String year = time.substring(0, 4);
		String month = time.substring(4, 6);
		String day = time.substring(6, 8);
		String hours = time.substring(9, 11);
		String minutes = time.substring(11, 13);
		String seconds = time.substring(13);
		String newTime = year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
		
		return newTime;
	}
	
	/**
	 * Convert time stamp to yyyyMMddTHHmmss.fff format
	 * @param timestamp is time in yyyy-mm-dd hh:mm:ss.fff format
	 * @return time in yyyyMMddTHHmmss.fff format
	 */
	public static String parseTimestamp(Timestamp timestamp) {
		String time ="";
		String tempTime = timestamp.toString();
		String year = tempTime.substring(0, 4);
		String month = tempTime.substring(5, 7);
		String day = tempTime.substring(8, 10);
		String hours = tempTime.substring(11, 13);
		String minutes = tempTime.substring(14, 16);
		String seconds = tempTime.substring(17, 19);
		String frame = tempTime.substring(20);
		
		time = year + month + day + "T" + hours + minutes + seconds + "." + frame;
		
		return time;
	}
	
}
