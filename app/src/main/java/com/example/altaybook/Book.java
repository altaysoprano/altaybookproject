package com.example.altaybook;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "book_table")
public class Book {

    @PrimaryKey(autoGenerate = true)
    protected int id;

    protected byte[] image;

    private String name;

    private String text;

    private boolean isSelected = false;

    public Book(byte[] image, String name, String text) {
        this.image = image;
        this.name = name;
        this.text = text;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public byte[] getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
