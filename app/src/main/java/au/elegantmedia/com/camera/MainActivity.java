package au.elegantmedia.com.camera;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_PHOTO = 0;
    public static final int MEDIA_IMAGE = 4;
    private static final String TAG = "test";
    private static final int PERMISSION_CODE = 11;
    Uri mediaUri;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageLoad();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //request permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Call permission")
                                    .setMessage("allow Permission").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, PERMISSION_CODE);
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();

                        } else {
                            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, PERMISSION_CODE);
                        }

                    } else {
                        runCamera();
                    }
                }
                runCamera();
            }
        });
    }

    private void runCamera() {
        mediaUri = getMediaFile(MEDIA_IMAGE);
        if (mediaUri == null) {
            Toast.makeText(MainActivity.this, "Problem in accessing external storage", Toast.LENGTH_SHORT).show();
        } else {
            Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            in.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
            startActivityForResult(in, REQUEST_PHOTO);
        }
    }
    //Check Permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                runCamera();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PHOTO && resultCode == RESULT_OK) {
            Log.i(TAG, "data value " + String.valueOf(data));
            imageLoad();
        } else {
            Log.i(TAG, "Error");
        }
    }
    //Set Image to ImageView
    private void imageLoad() {

        ImageView iv = (ImageView) findViewById(R.id.imge);

        sp = getSharedPreferences("MYp", MODE_PRIVATE);
        String restoredText = sp.getString("path", null);
        if (restoredText != null) {

            String uri = sp.getString("path", "No name defined");
            Uri ii = Uri.parse(uri);
            iv.setImageURI(ii);
        } else {
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
    //set Name to Image set Path of Image
    private Uri getMediaFile(int mediaImage) {

        sp = getSharedPreferences("MYp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (isExtenalStorage()) {
            //get URI
            File mediaStorageDir = getExternalFilesDir("NEW PHOTO");

            String fileName = "";
            String fileType = "";
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            if (mediaImage == MEDIA_IMAGE) {
                fileName = "IMG_" + timeStamp;
                fileType = ".jpg";
            } else {
                return null;
            }

            File mediaFile;
            try {
                mediaFile = File.createTempFile(fileName, fileType, mediaStorageDir);
                Log.i(TAG, "File" + mediaFile.toString());
                //put Path to sharedPreferences
                editor.putString("path", mediaFile.toString());
                editor.commit();

                return Uri.fromFile(mediaFile);
            } catch (IOException e) {
                Log.i(TAG, " error " + mediaStorageDir.getAbsolutePath() + fileName + fileType);
            }
        }
        return null;
    }
    //Check Storage
    private boolean isExtenalStorage() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            imageLoad();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
