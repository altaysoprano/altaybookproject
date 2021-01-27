package com.example.altaybook;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;

public class BookViewModel extends AndroidViewModel {

    private BookRepository bookRepository;
    private LiveData<List<Book>> allBooks;
    private List<Book> allBooksB;

    public BookViewModel(@NonNull Application application) {
        super(application);
        bookRepository = new BookRepository(application);
        allBooks = bookRepository.getAllBooks();
    }

    public void insert(Book book) {
        bookRepository.insert(book);
    }

    public void delete(Book book) {
        bookRepository.delete(book);
    }

    public LiveData<List<Book>> getAllBooks() {
        return allBooks;
    }

}
