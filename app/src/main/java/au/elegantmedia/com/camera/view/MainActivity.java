package au.elegantmedia.com.camera.view;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import au.elegantmedia.com.camera.Constant;
import au.elegantmedia.com.camera.FileUtil;
import au.elegantmedia.com.camera.ImageAdapter;
import au.elegantmedia.com.camera.ImageDir;
import au.elegantmedia.com.camera.R;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_PHOTO = 0;
    private static final String TAG = "test";
    private static final int PERMISSION_CODE = 11;
    ShareActionProvider mShareActionProvider;
    Uri mediaUri;
    List<File> list;
    RecyclerView mRecyclerView;
    ImageAdapter imageAdapter;
    private FloatingActionButton fab;
    private File imageFile;
    private ImageDir imageDir;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (FileUtil.isExtenalStorage()) {
            File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            imageFile = new File(mediaStorageDir, Constant.APP_DIR);
            if (!imageFile.exists()) {
                imageFile.mkdirs();
            }
        }

        list = new ArrayList<>();
        iv = (ImageView) findViewById(R.id.imge);

        initial();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (isPermission()) {
                        runCamera();
                    }
                } else {
                    runCamera();
                }
            }
        });
    }

    private void initial() {

        imageDir = new ImageDir(this, imageFile);
        if (imageDir.getImage() != null)
            list.addAll(imageDir.getImage());

        mRecyclerView = (RecyclerView)findViewById(R.id.rv_image);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false));
        imageAdapter = new ImageAdapter(this, list, new ImageAdapter.OnClickImage() {
            @Override
            public void onClickImageCallback(File file) {
                Picasso.with(MainActivity.this).load(file).fit().centerCrop().into(iv);
            }
        });
        mRecyclerView.setAdapter(imageAdapter);

        imageLoad();
    }

    private boolean isPermission() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this
                    , new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , PERMISSION_CODE);
            return false;
        } else {
            return true;
        }
    }

    private void runCamera() {
        mediaUri = FileUtil.getMediaFile(Constant.MEDIA_IMAGE, imageFile);
        Constant.imagePath = mediaUri;
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
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        Toast.makeText(this, permission + " Denied", Toast.LENGTH_SHORT).show();
                    } else {
                        Snackbar snackbar = Snackbar.make(fab, permission + "Denied", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Setting", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getApplication().getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        });
                        snackbar.show();
                    }
                } else {
                    Toast.makeText(this, permission + " Granted", Toast.LENGTH_SHORT).show();
                    runCamera();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PHOTO) {
                imageLoad();
                imageAdapter.addAll(imageDir.getImage());
            } else {
                Log.i(TAG, "Error");
            }
        }
    }


    private void imageLoad() {
        if (Constant.imagePath != null)
            Picasso.with(this).load(Constant.imagePath).fit().centerCrop().into(iv);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.shareMenu);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

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
        } else if (id == R.id.shareMenu) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareBodyText = "Check it out. Your message goes here";
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject here");
            intent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(intent, "Shearing Option"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}
