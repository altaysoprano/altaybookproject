package com.example.altaybook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.drm.DrmStore;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import junit.extensions.ActiveTestSuite;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int STORAGE_PERMISSION_CODE = 1;
    public static final int ADD_BOOK_REQUEST = 1;

    private BookViewModel bookViewModel;
    public ActionMode mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FloatingActionButton buttonAddBook = findViewById(R.id.button_add_book);
        buttonAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
                    startActivityForResult(intent, ADD_BOOK_REQUEST);
                }
                else{
                    requestStoragePermission();
                }
            }
        });

        RecyclerView rvBook = findViewById(R.id.recyclerview_id);
        rvBook.setLayoutManager(new GridLayoutManager(this, 3));
        rvBook.setHasFixedSize(true);

        final RecyclerViewAdapter bookAdapter = new RecyclerViewAdapter();
        rvBook.setAdapter(bookAdapter);

        bookViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(BookViewModel.class);
        bookViewModel.getAllBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                bookAdapter.setBookList(books);
            }
        });

        bookAdapter.setOnLongClickListener(new RecyclerViewAdapter.OnLongClickListener() {

            @Override
            public void onLongClick() {

                if(mActionMode != null) {
                    if(bookAdapter.getSelectedBooks().size() == 0) {
                        mActionMode.finish();
                    }
                    return;
                }

                ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        mode.getMenuInflater().inflate(R.menu.delete_book_menu, menu);
                        buttonAddBook.setVisibility(View.INVISIBLE);
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete_book :
                                for(Book book : bookAdapter.getSelectedBooks()) {
                                    bookViewModel.delete(book);
                                }
                                mode.finish();
                                return true;
                        default:
                            return false;
                        }
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        mActionMode = null;
                        for(Book book : bookAdapter.getBookList()) {
                            book.setSelected(false);
                            bookAdapter.notifyDataSetChanged();
                        }
                        buttonAddBook.setVisibility(View.VISIBLE);
                    }
                };
                mActionMode = startSupportActionMode(mActionModeCallback);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_BOOK_REQUEST && resultCode == RESULT_OK) {
            Toast.makeText(this, "Kitap eklendi", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Kitap eklenemedi.", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("İzin Gerekli")
                    .setMessage("Kitap ekleyebilmeniz için dosyalara erişim izni gerekli")
                    .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "İzin verildi.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "İzin reddedildi.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
