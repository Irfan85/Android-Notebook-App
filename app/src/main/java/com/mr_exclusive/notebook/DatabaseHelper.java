package com.mr_exclusive.notebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DAT_NAME = "noteBox";
    public static final int DAT_VERSION = 1;

    public static final String NOTE_TABLE = "notes";
    public static final String ID_FIELD = "_id";
    public static final String TITLE_FIELD = "title";
    public static final String NOTE_FIELD = "note";
    public static final String TIME_FIELD = "time";
    public static final String URI_FIELD = "uri";

    public static final String NOTE_TABLE_SQL = "CREATE TABLE " + NOTE_TABLE + " (" + ID_FIELD + " INTEGER PRIMARY KEY, "
            + TITLE_FIELD + " TEXT, " + NOTE_FIELD + " TEXT, " + TIME_FIELD + " TEXT, "+ URI_FIELD + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DAT_NAME, null, DAT_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NOTE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TITLE_FIELD, note.getTitle());
        values.put(NOTE_FIELD, note.getNote());
        values.put(TIME_FIELD, note.getTime());
        values.put(URI_FIELD, note.getUri());

        long inserted = db.insert(NOTE_TABLE, null, values);

        db.close();

        return inserted;
    }

    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> allNotes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(NOTE_TABLE, null, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                int id = cursor.getInt(cursor.getColumnIndex(ID_FIELD));
                String title = cursor.getString(cursor.getColumnIndex(TITLE_FIELD));
                String body = cursor.getString(cursor.getColumnIndex(NOTE_FIELD));
                String time = cursor.getString(cursor.getColumnIndex(TIME_FIELD));
                String uri = cursor.getString(cursor.getColumnIndex(URI_FIELD));

                Note note = new Note(id, title, body, time, uri);

                allNotes.add(note);

                cursor.moveToNext();
            }
        }

        assert cursor != null;
        cursor.close();
        db.close();
        return allNotes;

    }

    public ArrayList<Note> searchNote(String keyword) {
        ArrayList<Note> notes = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(NOTE_TABLE, null, TITLE_FIELD + " LIKE '" + keyword + "'", null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                int id = cursor.getInt(cursor.getColumnIndex(ID_FIELD));
                String title = cursor.getString(cursor.getColumnIndex(TITLE_FIELD));
                String note = cursor.getString(cursor.getColumnIndex(NOTE_FIELD));
                String time = cursor.getString(cursor.getColumnIndex(TIME_FIELD));
                String uri = cursor.getString(cursor.getColumnIndex(URI_FIELD));

                Note note1 = new Note(id, title, note, time, uri);

                notes.add(note1);

                cursor.moveToNext();
            }
        }
        db.close();
        return notes;
    }

    public int updateEmployeeName(int id, String nTitle, String nNote, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TITLE_FIELD, nTitle);
        values.put(NOTE_FIELD, nNote);
        values.put(TIME_FIELD, time);

        int updated = db.update(NOTE_TABLE, values, ID_FIELD + "=?", new String[]{"" + id});

        db.close();

        return updated;
    }

    public void removeData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String s = String.valueOf(id);
        db.execSQL("DELETE FROM " + NOTE_TABLE + " WHERE " + ID_FIELD + " = '" + s + "'");

    }

}
