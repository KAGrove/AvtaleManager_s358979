package com.example.avtalemanager_s358979;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface KontaktDao {

    @Query("SELECT * FROM kontakt")
    List<Kontakt> getAll();

    @Insert
    void insert(Kontakt kontakt);

    @Delete
    void delete(Kontakt kontakt);

    @Update
    void update(Kontakt kontakt);
}
