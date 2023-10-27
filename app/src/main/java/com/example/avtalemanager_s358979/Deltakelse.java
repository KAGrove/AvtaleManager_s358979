package com.example.avtalemanager_s358979;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        primaryKeys = {"avtale_id", "kid"},
        indices = {@Index("kid")},  // Legger til denne linjen for indeksen
        foreignKeys = {
                @ForeignKey(entity = Avtale.class,
                        parentColumns = "avtaleId",
                        childColumns = "avtale_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Kontakt.class,
                        parentColumns = "kid",
                        childColumns = "kid",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class Deltakelse {

    @ColumnInfo(name = "avtale_id")
    public int avtaleId;

    @ColumnInfo(name = "kid")
    public int kid;

    public int getAvtaleId() {
        return avtaleId;
    }

    public void setAvtaleId(int avtaleId) {
        this.avtaleId = avtaleId;
    }

    public int getKid() {
        return kid;
    }

    public void setKid(int kid) {
        this.kid = kid;
    }
}

