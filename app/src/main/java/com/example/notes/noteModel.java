package com.example.notes;

import android.widget.CheckBox;

public class noteModel {
    public String noteTitle;
    public String noteTime;
    private String task;


    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public noteModel(String noteTitle, String noteTime, String task) {
        this.noteTitle = noteTitle;
        this.noteTime = noteTime;
        this.task = task;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteTime() {
        return noteTime;
    }

    public void setNoteTime(String noteTime) {
        this.noteTime = noteTime;
    }

    public noteModel(){

    }
}
