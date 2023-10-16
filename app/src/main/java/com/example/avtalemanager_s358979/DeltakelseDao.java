package com.example.avtalemanager_s358979;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DeltakelseDao {

    @Query("SELECT * FROM Deltakelse")
    List<Deltakelse> getAll();

    @Insert
    void insert(Deltakelse deltakelse);

    @Delete
    void delete(Deltakelse deltakelse);

    @Update
    void update(Deltakelse deltakelse);
}
