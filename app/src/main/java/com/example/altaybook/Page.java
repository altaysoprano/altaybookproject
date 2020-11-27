package com.example.altaybook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Page extends AppCompatActivity {

    private String bookText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        bookText = findViewById(R.id.book_text).toString();


    }
}
