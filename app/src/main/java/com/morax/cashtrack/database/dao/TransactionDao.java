package com.morax.cashtrack.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.morax.cashtrack.database.entity.TransactionEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Dao
public interface TransactionDao {

    @Insert
    void insert(TransactionEntity transactionEntity);

    @Insert
    void insertAll(TransactionEntity... transactionEntities);

    @Query("SELECT * FROM `transaction` ORDER BY date DESC")
    List<TransactionEntity> getAllTransactions();

    @Query("DELETE FROM `transaction`")
    void deleteAll();

    @Query("SELECT SUM(amount) FROM `transaction`")
    Double getSumOfAmount();

    @Query("SELECT SUM(amount) FROM `transaction` WHERE date >= :startWeek and date <= :endWeek")
    Double getSumOfAmountThisWeek(Date startWeek, Date endWeek);
    @Query("SELECT SUM(amount) FROM `transaction` WHERE date >= :startMonth and date <= :endMonth")
    Double getSumOfAmountThisMonth(Date startMonth, Date endMonth);

    @Query("SELECT SUM(amount) FROM `transaction` WHERE date >= :startYear and date <= :endYear")
    Double getSumOfAmountThisYear(Date startYear, Date endYear);
}
