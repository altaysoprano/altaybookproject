package com.example.altaybook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Section;

import org.w3c.dom.Text;

import java.util.List;

public class BookActivity extends AppCompatActivity {

    public static final String BOOK_TEXT = "com.example.altaybook.BOOK_TEXT";
    BookViewModel bookViewModel;
    private TextView bookTextView;
    private String bookName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        bookTextView = findViewById(R.id.book_text_id);
        Intent intent = getIntent();
        final int bookPosition = intent.getIntExtra(BOOK_TEXT, -1);

        bookViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(BookViewModel.class);

        bookViewModel.getAllBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                setTitle(books.get(bookPosition).getName());
                bookTextView.setText(books.get(bookPosition).getText());
            }
        });

    }
}
