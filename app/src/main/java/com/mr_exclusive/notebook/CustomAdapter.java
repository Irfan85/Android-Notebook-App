package com.mr_exclusive.notebook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Note> {
    Activity con;
    ArrayList<Note> noteArrayList;
    DatabaseHelper databaseHelper;
    ArrayList<String> ars = new ArrayList<>();

    public CustomAdapter(Context context, ArrayList<Note> notes) {
        super(context, R.layout.note_view, notes);
        this.con = (Activity) context;
        this.noteArrayList = notes;
        databaseHelper = new DatabaseHelper(getContext());
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = con.getLayoutInflater();
            v = inflater.inflate(R.layout.note_view, null);
        }
        final TextView txtName = (TextView) v.findViewById(R.id.textView);
        CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox);

        Note n = noteArrayList.get(position);
        txtName.setText(n.getTitle());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Note note = noteArrayList.get(position);
                    String s = note.getTitle();
                    ars.add(s);
                }
                if (!isChecked) {
                    Note note = noteArrayList.get(position);
                    String s = note.getTitle();
                    ars.remove(s);
                }
            }
        });

        txtName.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = txtName.getText().toString();
                snd(key);
            }
        });


        return v;
    }

    public void snd(String key) {
        ArrayList<Note> notes = databaseHelper.searchNote(key);

        for (Note note : notes) {
            String title = note.getTitle();
            String nb = note.getNote();
            String t = note.getTime();
            String uri = note.getUri();
            int id = note.getId();

            if (!isTablet(getContext())) {
                Intent i = new Intent(getContext(), Update.class);
                i.putExtra("idtu", id);
                i.putExtra("title", title);
                i.putExtra("body", nb);
                i.putExtra("time", t);
                i.putExtra("uri", uri);
                con.startActivity(i);
            } else {
                MainActivity.sendToMe(title, nb, t, id, uri);
            }


        }
    }

    public void alr(boolean ok) {
        if (ok) {
            if (!ars.isEmpty()) {
                for (int i = 0; i < ars.toArray().length; i++) {
                    String t = ars.get(i);
                    ArrayList<Note> notes = databaseHelper.searchNote(t);
                    for (Note note : notes) {
                        int ide = note.getId();
                        databaseHelper.removeData(ide);
                    }
                }
                ars.clear();
                this.notifyDataSetChanged();
                con.recreate();
            }

        } else {
            Toast.makeText(getContext(), "No items selected to delete.", Toast.LENGTH_SHORT).show();
        }

    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}

