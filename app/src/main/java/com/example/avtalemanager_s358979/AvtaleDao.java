package com.example.avtalemanager_s358979;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AvtaleDao {

    @Query("SELECT * FROM Avtale")
    List<Avtale> getAll();

    @Insert
    void insert(Avtale avtale);

    @Delete
    void delete(Avtale avtale);

    @Update
    void update(Avtale avtale);
}
