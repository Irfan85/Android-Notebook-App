package com.mr_exclusive.notebook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Update extends AppCompatActivity {
    EditText ut, ub;
    DatabaseHelper dbh;
    int id;
    TextView textView;
    LinearLayout linearLayout;
    protected String uri;
    private static ImageView imageView;

    public static final int CAPTURE_RQ = 0;
    public static final int CHOOSE_RQ = 1;
    public static final int MEDIA_TYPE_IMAGE = 3;
    protected Uri mediaUri;

    protected DialogInterface.OnClickListener mlistener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0: // take pic
                    Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mediaUri = getOutputImage(MEDIA_TYPE_IMAGE);
                    if (mediaUri == null) {
                        Toast.makeText(getApplicationContext(), "A fatal error occured.", Toast.LENGTH_SHORT).show();
                    } else {
                        takePic.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
                        startActivityForResult(takePic, CAPTURE_RQ);
                    }

                    break;
                case 1: // choose pic
                    Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    choosePhotoIntent.setType("image/*");
                    startActivityForResult(choosePhotoIntent, CHOOSE_RQ);
                    break;
            }
        }

    };

    private Uri getOutputImage(int mediaType) {
        String appname = Update.this.getString(R.string.app_name);
        if (isExternalStorageAvailable()) {
            //create Directory
            File mediaStorageDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appname);
            //create subDirectory
            if (!mediaStorageDirectory.exists()) {
                if (!mediaStorageDirectory.mkdirs()) {
                    Toast.makeText(getApplicationContext(), "Failed to create directory.", Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
            //creating and naming the file
            File mediaFile;
            Date fileDate = new Date();
            String dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(fileDate);
            String path = mediaStorageDirectory.getPath() + File.separator;
            if (mediaType == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(path + "IMG_" + dateFormat + ".jpg");
            } else {
                return null;
            }
            //returning the Uri
            return Uri.fromFile(mediaFile);
        } else {
            return null;
        }
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ut = (EditText) findViewById(R.id.etTitleu);
        ub = (EditText) findViewById(R.id.etMainu);
        textView = (TextView) findViewById(R.id.textView2);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout2);
        imageView = new ImageView(this);
        dbh = new DatabaseHelper(this);
        String t = getIntent().getStringExtra("title");
        String b = getIntent().getStringExtra("body");
        String time = getIntent().getStringExtra("time");
        String uri1 = getIntent().getStringExtra("uri");
        id = getIntent().getIntExtra("idtu", 0);
        ut.setText(t);
        ub.setText(b);
        textView.setText(time);
        if (uri1 != null) {
            Uri mUri = Uri.parse(uri1);
            imageView.setImageURI(mUri);
            linearLayout.addView(imageView);
        } else {
            linearLayout.removeView(imageView);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //save it to the gallery
            if (requestCode == CHOOSE_RQ) {
                if (data == null) {
                    Toast.makeText(getApplicationContext(), "Something unexpected happened.", Toast.LENGTH_SHORT).show();
                } else {
                    mediaUri = data.getData();
                    printPic();
                    uri = data.getData().toString();
                }
            } else {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mediaUri);
                sendBroadcast(mediaScanIntent);
                printPic();
                uri = mediaUri.toString();
            }
        } else if (resultCode != RESULT_CANCELED) {
            //error
            Toast.makeText(getApplicationContext(), "Something unexpected happened.", Toast.LENGTH_SHORT).show();
        }
    }

    private void printPic() {
        if (uri != null) {
            Uri mUri = Uri.parse(uri);
            imageView.setImageURI(mUri);
            linearLayout.addView(imageView);
        } else {
            linearLayout.removeView(imageView);
        }
    }


    public void update(View v) {
        String nut = ut.getText().toString();
        String nub = ub.getText().toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' h:mm:ss a");
        String df = "Updated on: " + dateFormat.format(Calendar.getInstance().getTime());

        if (!nut.equals("") || !nub.equals("")) {

            if (nut.length() < 30) {

                int updated = dbh.updateEmployeeName(id, nut, nub, df);
                if (updated > 0) {
                    Toast.makeText(getApplicationContext(), "Note saved :)", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Note didn't saved :(", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Maximum limit of title has reached.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Please, fill the above fields.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_click) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(R.array.cameraChoices, mlistener);
            AlertDialog dailogue = builder.create();
            dailogue.show();

        }
        return true;

    }

}
