package com.morax.cashtrack.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("SELECT * FROM `Transaction` WHERE category != 'Transfer' ORDER BY date DESC")
    List<Transaction> getTransactionDesc();

    @Query("DELETE FROM `Transaction`")
    void deleteAll();

    @Query("SELECT * FROM `Transaction` WHERE category != 'Transfer' ORDER BY date DESC LIMIT :limit")
    List<Transaction> getTransactionWithLimit(int limit);

    @Query("SELECT * FROM `transaction` WHERE category = :category AND accountId = :accountId")
    List<Transaction> getTransactionByAccountIdWithCategory(String category, long accountId);

    @Query("SELECT * FROM `transaction` WHERE category = :category")
    List<Transaction> getTransactionByCategory(String category);

    @Query("SELECT * FROM `transaction` WHERE accountId = :accountId")
    List<Transaction> getTransactionByAccountId(long accountId);
    @Update
    void update(Transaction transaction);

    @Query("SELECT * FROM `Transaction` WHERE id = :id")
    Transaction getTransactionById(long id);

    @Delete
    void delete(Transaction transaction);

    @Query("SELECT SUM(amount) FROM `Transaction`")
    BigDecimal getSumOfAmount();

    @Query("SELECT SUM(amount) FROM `Transaction` WHERE type = 'Expense' AND category != 'Transfer'")
    BigDecimal getExpense();

    @Query("SELECT SUM(amount) FROM `Transaction` WHERE type = 'Income' AND category != 'Transfer'")
    BigDecimal getIncome();

    @Query("SELECT SUM(amount) FROM `Transaction` WHERE type = 'Expense' AND accountId = :accountId")
    BigDecimal getExpenseByAccountId(long accountId);

    @Query("SELECT SUM(amount) FROM `Transaction` WHERE type = 'Income' AND accountId = :accountId")
    BigDecimal getIncomeByAccountId(long accountId);

    @Query("SELECT SUM(amount) FROM `Transaction` WHERE date >= :startWeek and date <= :endWeek and type = 'Expense' AND category != 'Transfer'")
    BigDecimal getExpenseThisWeek(Date startWeek, Date endWeek);

    @Query("SELECT SUM(amount) FROM `Transaction` WHERE date >= :startMonth and date <= :endMonth  and type = 'Expense' AND category != 'Transfer'")
    BigDecimal getExpenseThisMonth(Date startMonth, Date endMonth);

    @Query("SELECT SUM(amount) FROM `Transaction` WHERE date >= :startYear and date <= :endYear and type = 'Expense' AND category != 'Transfer'")
    BigDecimal getExpenseThisYear(Date startYear, Date endYear);

    @Query("SELECT SUM(amount) FROM `Transaction` WHERE date >= :startWeek and date <= :endWeek and type = 'Income' AND category != 'Transfer'")
    BigDecimal getIncomeThisWeek(Date startWeek, Date endWeek);

    @Query("SELECT SUM(amount) FROM `Transaction` WHERE date >= :startMonth and date <= :endMonth  and type = 'Income' AND category != 'Transfer'")
    BigDecimal getIncomeThisMonth(Date startMonth, Date endMonth);

    @Query("SELECT SUM(amount) FROM `Transaction` WHERE date >= :startYear and date <= :endYear and type = 'Income' AND category != 'Transfer'")
    BigDecimal getIncomeThisYear(Date startYear, Date endYear);
}
