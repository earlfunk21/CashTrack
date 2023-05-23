package com.morax.cashtrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.morax.cashtrack.adapter.TransactionAdapter;
import com.morax.cashtrack.database.AppDatabase;
import com.morax.cashtrack.database.dao.AccountDao;
import com.morax.cashtrack.database.dao.CategoryDao;
import com.morax.cashtrack.database.dao.TransactionDao;
import com.morax.cashtrack.database.entity.Account;
import com.morax.cashtrack.database.entity.Category;
import com.morax.cashtrack.database.entity.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionsActivity extends AppCompatActivity {

    private TransactionAdapter transactionAdapter;
    private List<Transaction> transactionList;

    private TransactionDao transactionDao;
    private CategoryDao categoryDao;
    private AccountDao accountDao;

    private String strCategory;
    private long accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        transactionDao = AppDatabase.getInstance(this).transactionDao();
        categoryDao = AppDatabase.getInstance(this).categoryDao();
        accountDao = AppDatabase.getInstance(this).accountDao();


        initData();
        RecyclerView rvTransaction = findViewById(R.id.rv_transaction_list);
        transactionAdapter = new TransactionAdapter(this, transactionList);
        rvTransaction.setAdapter(transactionAdapter);

        // Spinner Category
        Spinner spinnerCategory = findViewById(R.id.spinner_category_list);
        List<Category> categories = categoryDao.getCategories();
        Category category = new Category("All");
        categories.add(category);
        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_layout, categories);
        categoryArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryArrayAdapter);
        spinnerCategory.setSelection(categories.size() - 1);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strCategory = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Spinner Account
        Spinner spinner = findViewById(R.id.spinner_account_list); // Replace `R.id.spinner` with the actual ID of your spinner
        List<Account> accounts = accountDao.getAccounts();
        Account account = new Account("All");
        account.id = -1;
        accounts.add(account);
        ArrayAdapter<Account> accountArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_layout, accounts);
        accountArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(accountArrayAdapter);
        spinner.setSelection(accounts.size() - 1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Account selectedAccount = (Account) parent.getItemAtPosition(position);
                accountId = selectedAccount.id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when no item is selected
            }
        });
    }

    public void goBack(View view) {
        onBackPressed();
        finish();
    }

    private void initData() {
        transactionList = new ArrayList<>();
        try {
            transactionList.addAll(transactionDao.getTransactionDesc());
        } catch (NullPointerException ignored) {
        }
    }

    public void setFilter(View view) {
        List<Transaction> filteredList = new ArrayList<>();
        if (accountId != -1)
            transactionList = transactionDao.getTransactionByAccountId(accountId);
        else {
            transactionList = transactionDao.getTransactionDesc();
        }

        if (!strCategory.equals("All")) {
            for (Transaction transaction : transactionList) {
                if (transaction.category.equals(strCategory))
                    filteredList.add(transaction);
            }
        } else {
            filteredList.addAll(transactionList);
        }

        try {
            transactionAdapter.setTransactionEntityList(filteredList);
        } catch (NullPointerException ignored) {
        }
    }
}