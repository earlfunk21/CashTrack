package com.morax.cashtrack.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.morax.cashtrack.database.converter.DateTypeConverter;
import com.morax.cashtrack.database.dao.TransactionDao;
import com.morax.cashtrack.database.entity.TransactionEntity;

@Database(entities = {TransactionEntity.class}, version = 1)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract TransactionDao transactionDao();

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                    "my_db").allowMainThreadQueries().build();
        }
        return instance;
    }
}
