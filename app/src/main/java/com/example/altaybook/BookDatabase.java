package com.example.altaybook;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Book.class}, version = 1, exportSchema = false)
public abstract class BookDatabase extends RoomDatabase {

    private static BookDatabase instance;

    public abstract BookDao bookDao();

    public static synchronized BookDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BookDatabase.class, "book_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

        }
    };

    private static class populateAsyncTask extends AsyncTask<Void, Void, Void> {

        private BookDao bookDao;

        private populateAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
