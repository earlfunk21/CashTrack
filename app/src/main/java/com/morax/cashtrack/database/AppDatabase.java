package com.morax.cashtrack.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.morax.cashtrack.database.converter.BigDecimalConverter;
import com.morax.cashtrack.database.converter.DateConverter;
import com.morax.cashtrack.database.dao.AccountDao;
import com.morax.cashtrack.database.dao.CategoryDao;
import com.morax.cashtrack.database.dao.TransactionDao;
import com.morax.cashtrack.database.entity.Account;
import com.morax.cashtrack.database.entity.Category;
import com.morax.cashtrack.database.entity.Transaction;

@Database(entities = {Account.class, Transaction.class, Category.class}, version = 1)
@TypeConverters({DateConverter.class, BigDecimalConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract TransactionDao transactionDao();
    public abstract AccountDao accountDao();

    public abstract CategoryDao categoryDao();

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                    "test_db1").allowMainThreadQueries().build();
        }
        return instance;
    }
}
