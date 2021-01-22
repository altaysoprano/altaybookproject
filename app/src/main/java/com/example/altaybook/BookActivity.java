package com.example.altaybook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class BookActivity extends AppCompatActivity {

    public static final String BOOK_TEXT = "com.example.altaybook.BOOK_TEXT";
    private TextView bookText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        bookText = findViewById(R.id.book_text_id);
    }
}
