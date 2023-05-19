package com.morax.cashtrack.database.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;


public class AccountWithTransaction {
    @Embedded
    public Account account;

    @Relation(
            parentColumn = "id",
            entityColumn = "accountId"
    )
    public List<Transaction> transactions;
}
