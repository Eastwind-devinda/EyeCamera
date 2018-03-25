package com.eastwind.devinda.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devinda on 10/2/17.
 */

public class ImageDir extends Activity {

    File[] mImageFiles;
    Intent intent;
    List<File> list;
    private Context context;
    private File mPrivateRootDir;
    private File mImagesDir;

    public ImageDir(Context context, File imageFile) {
        this.context = context;
        this.mImagesDir = imageFile;
    }

    public List<File> getImage() {


        intent = new Intent(mImagesDir + ".ACTION_RETURN_PICK");

        mPrivateRootDir = mImagesDir;
        // Get the files in the images subdirectory
        mImageFiles = mPrivateRootDir.listFiles();

        if (mImageFiles == null) {
            return null;
        }

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
