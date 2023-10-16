package com.example.avtalemanager_s358979;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Kontakt.class, Avtale.class, Deltakelse.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract KontaktDao kontaktDao();
    public abstract AvtaleDao avtaleDao();
    public abstract DeltakelseDao deltakelseDao();
}
