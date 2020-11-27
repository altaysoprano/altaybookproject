package com.example.altaybook;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BookRepository {

    private BookDao bookDao;
    private LiveData<List<Book>> allNotes;

    public BookRepository(Application application) {
        BookDatabase bookDatabase = BookDatabase.getInstance(application);
        bookDao = bookDatabase.bookDao();
        allNotes = bookDao.getAllBooks();
    }

    public void insert(Book book) {
        new InsertBookAsyncTask(bookDao).execute(book);
    }

    public void update(Book book) {
        new UpdateBookAsyncTask(bookDao).execute(book);
    }

    public void delete(Book book) {
        new DeleteBookAsyncTask(bookDao).execute(book);
    }

    public LiveData<List<Book>> getAllBooks() {
        return allNotes;
    }

    private static class InsertBookAsyncTask extends AsyncTask<Book, Void, Void> {

        private BookDao bookDao;

        private InsertBookAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }
        @Override
        protected Void doInBackground(Book... books) {
            bookDao.insert(books[0]);
            return null;
        }
    }

    private static class UpdateBookAsyncTask extends AsyncTask<Book, Void, Void> {

        private BookDao bookDao;

        private UpdateBookAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }
        @Override
        protected Void doInBackground(Book... books) {
            bookDao.update(books[0]);
            return null;
        }
    }

    private static class DeleteBookAsyncTask extends AsyncTask<Book, Void, Void> {

        private BookDao bookDao;

        private DeleteBookAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }
        @Override
        protected Void doInBackground(Book... books) {
            bookDao.delete(books[0]);
            return null;
        }
    }

}
