package com.morax.cashtrack.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Transaction implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String type;
    public long accountId;
    public String category;
    public Date date;
    public BigDecimal amount;
    public String note;

    public Transaction(long accountId, String category, Date date, BigDecimal amount, String note) {
        this.accountId = accountId;
        this.category = category;
        this.date = date;
        this.amount = amount;
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setTypeExpense() {
        this.type = "Expense";
    }

    public void setTypeIncome() {
        this.type = "Income";
    }
}
