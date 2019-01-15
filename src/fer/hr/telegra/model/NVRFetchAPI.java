package fer.hr.telegra.model;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * Interface that represent API for fetching images from NVR with native code
 * @author dmlinaric
 *
 */
public interface NVRFetchAPI extends Library {
	@SuppressWarnings("deprecation")
	NVRFetchAPI INSTANCE = (NVRFetchAPI) Native.loadLibrary("nvrfetch", NVRFetchAPI.class);
	Pointer get_frame_c(String url);
	void free_string(Pointer str);
	int export_frame(String url, String location);
}
