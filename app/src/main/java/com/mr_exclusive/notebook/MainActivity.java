package com.mr_exclusive.notebook;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    CustomAdapter adapter;
    ListView lv;
    static LinearLayout linearLayout;
    static ImageView imageView;
    static TextView tv, tv2, tv3, tv4;
    static String titlem;
    static String bodym;
    static String timem;
    static int idm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.listView);

        if (isTablet(getApplicationContext())) {
            tv = (TextView) findViewById(R.id.textView3);
            tv2 = (TextView) findViewById(R.id.textView4);
            tv3 = (TextView) findViewById(R.id.textView5);//dt
            tv4 = (TextView) findViewById(R.id.textView6);
            linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
            imageView = new ImageView(this);
        }

        databaseHelper = new DatabaseHelper(this);


        ArrayList<Note> notes = databaseHelper.getAllNotes();

        if (databaseHelper.getAllNotes().isEmpty()) {
            String[] e = {"No notes available"};
            ArrayAdapter<String> ads = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, e);
            lv.setAdapter(ads);
        } else {
            if (notes.size() > 0) {
                adapter = new CustomAdapter(this, notes);
                lv.setAdapter(adapter);
            }
        }

        if (isTablet(getApplicationContext())) {
            if (tv2.getText().toString().equals("") && tv3.getText().toString().equals("") && tv4.getText().toString().equals("")) {
                tv.setText("No notes are selected.");
            }
        }


    }

    public static void sendToMe(String title, String body, String time, int id, String uri) {
        titlem = title;
        bodym = body;
        timem = time;
        idm = id;

        tv.setText("");
        tv2.setText("Title: " + title);
        tv3.setText(time);
        tv4.setText(body);

        if (uri != null) {
            Uri mUri = Uri.parse(uri);
            imageView.setImageURI(mUri);
            linearLayout.addView(imageView);
        } else {
            linearLayout.removeView(imageView);
        }
    }

    public void update(View v) {
        Intent intent = new Intent(this, Update.class);
        intent.putExtra("idtu", idm);
        intent.putExtra("title", titlem);
        intent.putExtra("body", bodym);
        intent.putExtra("time", timem);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Note> notes = databaseHelper.getAllNotes();

        if (databaseHelper.getAllNotes().isEmpty()) {
            String[] e = {"No notes available"};
            ArrayAdapter<String> ads = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, e);
            lv.setAdapter(ads);
        } else {
            if (notes.size() > 0) {
                adapter = new CustomAdapter(this, notes);
                lv.setAdapter(adapter);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void add(View view) {
        Intent intent = new Intent(this, AddActiviy.class);
        startActivity(intent);
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_delete) {
            if (!databaseHelper.getAllNotes().isEmpty()) {
                adapter.alr(true);
            } else {
                Toast.makeText(getApplicationContext(), "No notes available to delete", Toast.LENGTH_SHORT).show();
            }

        }


        return super.onOptionsItemSelected(item);
    }

}
