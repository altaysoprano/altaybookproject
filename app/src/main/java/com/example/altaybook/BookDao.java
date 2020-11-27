package com.example.altaybook;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookDao {

    @Insert
    void insert(Book book);

    @Update
    void update(Book book);

    @Delete
    void delete(Book book);

    @Query("SELECT * FROM book_table")
    LiveData<List<Book>> getAllBooks(); // bu metot 1 kez çalıştığı anda artık database ne zaman
     // değişse geri döndürdüğü değer değişmişse o da değişecek. Livedata observable (gözlemlenebilir)
    // veri demektir.
}
