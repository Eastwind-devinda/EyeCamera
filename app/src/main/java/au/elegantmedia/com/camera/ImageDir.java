package au.elegantmedia.com.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.GridLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devinda on 10/2/17.
 */

public class ImageDir extends Activity {

    // Array of files in the images subdirectory
    File[] mImageFiles;
    // Array of filenames corresponding to mImageFiles
    String[] mImageFilenames;
    // Initialize the Activity
    Intent intent;
    GridLayout gridLayout;
    List<File> list;
    private Context context;
    private File mPrivateRootDir;
    // The path to the "images" subdirectory
    private File mImagesDir;

    public ImageDir(Context context) {
        this.context = context;
    }

    public List<File> getImage() {

        // Set up an Intent to send back to apps that request a file
        intent = new Intent(Constant.CustDir + ".ACTION_RETURN_PICK");
        // Get the files/ subdirectory of internal storage
        //mPrivateRootDir = getFilesDir();
        mPrivateRootDir = new File(Constant.CustDir);
        // Get the files in the images subdirectory
        mImageFiles = mPrivateRootDir.listFiles();

        list = new ArrayList<>();

        for (int i = 0; i < mImageFiles.length; i++) {

            list.add(mImageFiles[i]);
            Log.wtf("TAG", String.valueOf(mImageFiles[i]));
        }

        // Set the Activity's result to null to begin with
        setResult(Activity.RESULT_CANCELED, null);
        /*
         * Display the file names in the ListView mFileListView.
         * Back the ListView with the array mImageFilenames, which
         * you can create by iterating through mImageFiles and
         * calling File.getAbsolutePath() for each File
         */
        return list;

    }


}
