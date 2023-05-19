package com.morax.cashtrack.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.morax.cashtrack.database.entity.Transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Dao
public interface TransactionDao {

    @Insert
    void insert(Transaction transaction);

    @Insert
    void insertAll(Transaction... transactions);

    @Query("SELECT * FROM `Transaction` ORDER BY date DESC")
    List<Transaction> getTransactionDesc();

    @Query("DELETE FROM `Transaction`")
    void deleteAll();

    @Query("SELECT SUM(amount) FROM `Transaction`")
    BigDecimal getSumOfAmount();

    @Query("SELECT SUM(amount) FROM `Transaction` WHERE type = 'Expense'")
    BigDecimal getExpense();

    @Query("SELECT SUM(amount) FROM `Transaction` WHERE type = 'Income'")
    BigDecimal getIncome();

    @Query("SELECT SUM(amount) FROM `Transaction` WHERE date >= :startWeek and date <= :endWeek and type = 'Expense'")
    BigDecimal getExpenseThisWeek(Date startWeek, Date endWeek);

    @Query("SELECT SUM(amount) FROM `Transaction` WHERE date >= :startMonth and date <= :endMonth  and type = 'Expense'")
    BigDecimal getExpenseThisMonth(Date startMonth, Date endMonth);

    @Query("SELECT SUM(amount) FROM `Transaction` WHERE date >= :startYear and date <= :endYear and type = 'Expense'")
    BigDecimal getExpenseThisYear(Date startYear, Date endYear);

    @Query("SELECT SUM(amount) FROM `Transaction` WHERE date >= :startWeek and date <= :endWeek and type = 'Income'")
    BigDecimal getIncomeThisWeek(Date startWeek, Date endWeek);

    @Query("SELECT SUM(amount) FROM `Transaction` WHERE date >= :startMonth and date <= :endMonth  and type = 'Income'")
    BigDecimal getIncomeThisMonth(Date startMonth, Date endMonth);

    @Query("SELECT SUM(amount) FROM `Transaction` WHERE date >= :startYear and date <= :endYear and type = 'Income'")
    BigDecimal getIncomeThisYear(Date startYear, Date endYear);
}
