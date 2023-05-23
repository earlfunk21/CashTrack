package com.morax.cashtrack.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.morax.cashtrack.database.entity.Account;
import com.morax.cashtrack.database.entity.AccountWithTransaction;

import java.math.BigDecimal;
import java.util.List;

@Dao
public interface AccountDao {
    @Insert
    void insert(Account account);

    @Query("DELETE FROM Account")
    void deleteAll();
    @Delete
    void delete(Account account);

    @Query("SELECT * FROM Account")
    List<Account> getAccounts();

    @Query("SELECT name FROM Account")
    List<String> getAccountNames();

    @Query("SELECT * FROM Account WHERE id = :id")
    Account getAccountById(long id);

    @Query("SELECT * FROM Account WHERE name = :name")
    List<Account> getAccountByName(long name);

    @Transaction
    @Query("SELECT * FROM Account, `Transaction` WHERE Account.id = :accountId AND type = 'Income'")
    AccountWithTransaction getAccountWithIncomesById(long accountId);

    @Transaction
    @Query("SELECT * FROM Account, `Transaction` WHERE Account.id = :accountId AND type = 'Expense'")
    AccountWithTransaction getAccountWithExpenseById(long accountId);

    @Query("SELECT SUM(amount) FROM Account, `Transaction` WHERE Account.id = :accountId")
    BigDecimal getSumOfTransactionsById(long accountId);

}
