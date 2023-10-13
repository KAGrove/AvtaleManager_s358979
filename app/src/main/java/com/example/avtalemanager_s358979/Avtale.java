package com.example.avtalemanager_s358979;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Avtale {
    @PrimaryKey(autoGenerate = true)
    public int avtaleId;

    @ColumnInfo(name = "dato")
    public String dato;

    @ColumnInfo(name = "klokkeslett")
    public String klokkeslett;

    @ColumnInfo(name = "treffsted")
    public String treffsted;

    public int getAvtaleId() {
        return avtaleId;
    }

    public void setAvtaleId(int avtaleId) {
        this.avtaleId = avtaleId;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public String getKlokkeslett() {
        return klokkeslett;
    }

    public void setKlokkeslett(String klokkeslett) {
        this.klokkeslett = klokkeslett;
    }

    public String getTreffsted() {
        return treffsted;
    }

    public void setTreffsted(String treffsted) {
        this.treffsted = treffsted;
    }
}