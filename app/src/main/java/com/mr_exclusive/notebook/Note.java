package com.mr_exclusive.notebook;

public class Note {
    private int id;
    private String title;
    private String note;
    private String time;
    private String uri;


    public Note(String title, String note, String time) {
        this.title = title;
        this.note = note;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Note(int id, String title, String note, String time) {

        this.id = id;
        this.title = title;
        this.note = note;
        this.time = time;
    }

    public Note(String title, String note, String time, String uri) {
        this.title = title;
        this.note = note;
        this.time = time;
        this.uri = uri;
    }

    public Note(int id, String title, String note) {
        this.id = id;
        this.title = title;
        this.note = note;
    }

    public Note(String title, String note) {
        this.title = title;
        this.note = note;
    }

    public Note(int id, String title, String note, String time, String uri) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.time = time;
        this.uri = uri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
