package com.morax.cashtrack.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity(tableName = "transaction")
public class TransactionEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "amount")
    public double amount;

    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "date")
    public Date date;

    @ColumnInfo(name = "note")
    public String note;

    public TransactionEntity(double amount, String category, Date date, String note) {
        this.amount = amount;
        this.category = category;
        this.date = date != null ? date : new Date();
        this.note = note;
    }

}
