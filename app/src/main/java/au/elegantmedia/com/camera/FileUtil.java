package au.elegantmedia.com.camera;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by Janithanoma on 3/24/18.
 */

public class FileUtil {

    public static Uri getMediaFile(int mediaImage, File imageFile) {

            String fileName = "";
            String fileType = "";
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            if (mediaImage == Constant.MEDIA_IMAGE) {
                fileName = "IMG_" + timeStamp;
                fileType = ".jpg";
            } else {
                return null;
            }

            File mediaFile;
            try {
                mediaFile = File.createTempFile(fileName, fileType, imageFile);
                Log.i(TAG, "File" + mediaFile.toString());
                //put Path to sharedPreferences

                return Uri.fromFile(mediaFile);

            } catch (IOException e) {
                Log.i(TAG, " error " + imageFile.getAbsolutePath() + fileName + fileType);
            }
        return null;
    }

    //Check Storage
    public static boolean isExtenalStorage() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else {
            return false;
        }
    }
}
