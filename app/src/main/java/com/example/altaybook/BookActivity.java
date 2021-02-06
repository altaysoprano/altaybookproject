package com.example.altaybook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Section;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity {

    public static final String BOOK_TEXT = "com.example.altaybook.BOOK_TEXT";
    BookViewModel bookViewModel;
    private WebView bookWebView;
    private String bookName;
    private Pagination mPagination;
    private String bookText;
    ProgressBar bookTextProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        bookWebView = findViewById(R.id.book_text_id);
        bookTextProgressBar = findViewById(R.id.bookTextProgressBar);
        Intent intent = getIntent();
        final int bookPosition = intent.getIntExtra(BOOK_TEXT, -1);

        bookViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(BookViewModel.class);
        bookViewModel.getAllBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                bookName = books.get(bookPosition).getName();
                setTitle(bookName);
                bookTextProgressBar.setVisibility(View.VISIBLE);
                bookText = books.get(bookPosition).getText();
                bookWebView.loadData(bookText, "text/html", "UTF-8");
                bookTextProgressBar.setVisibility(View.GONE);
            }
        });
    }
}
