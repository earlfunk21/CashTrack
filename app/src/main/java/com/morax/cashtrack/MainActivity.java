package com.morax.cashtrack;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.morax.cashtrack.adapter.AccountAdapter;
import com.morax.cashtrack.adapter.CategoryAdapter;
import com.morax.cashtrack.adapter.TransactionAdapter;
import com.morax.cashtrack.database.AppDatabase;
import com.morax.cashtrack.database.dao.AccountDao;
import com.morax.cashtrack.database.dao.CategoryDao;
import com.morax.cashtrack.database.dao.TransactionDao;
import com.morax.cashtrack.database.entity.Account;
import com.morax.cashtrack.database.entity.Category;
import com.morax.cashtrack.database.entity.Transaction;
import com.morax.cashtrack.utils.CurrencyFormatter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Transaction> transactionList;

    private TransactionDao transactionDao;
    private AccountDao accountDao;
    private TransactionAdapter transactionAdapter;
    private TextView tvExpense;
    private TextView tvIncome;

    private DrawerLayout drawerLayout;

    private ArrayAdapter<Account> accountArrayAdapter;
    private CategoryDao categoryDao;
    private AccountAdapter accountAdapter;
    private CategoryAdapter categoryAdapter;
    private List<Account> accountList;
    private List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transactionDao = AppDatabase.getInstance(this).transactionDao();
        accountDao = AppDatabase.getInstance(this).accountDao();

        initData();
        RecyclerView rvTransaction = findViewById(R.id.rv_transaction);
        transactionAdapter = new TransactionAdapter(this, transactionList);
        rvTransaction.setAdapter(transactionAdapter);

        tvExpense = findViewById(R.id.tv_expense);
        tvIncome = findViewById(R.id.tv_income);
        setDateAsYearly(tvExpense);
        setSavings();
        drawerLayout = findViewById(R.id.drawerLayout);

        Spinner spinner = findViewById(R.id.spinner_savings); // Replace `R.id.spinner` with the actual ID of your spinner
        List<Account> accounts = accountDao.getAccounts();
        Account mainSavings = new Account("Main Savings");
        accounts.add(mainSavings);
        accountArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, accounts);
        accountArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(accountArrayAdapter);
        spinner.setSelection(accounts.size() -  1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Account selectedAccount = (Account) parent.getItemAtPosition(position);
                if (selectedAccount.name.equals("Main Savings")) {
                    setSavings();
                } else {
                    setSavingsById(selectedAccount.id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when no item is selected
                accountArrayAdapter.clear();
                List<Account> accounts = accountDao.getAccounts();
                accountArrayAdapter.addAll(accounts);
                accountArrayAdapter.notifyDataSetChanged();
            }
        });

        accountDao = AppDatabase.getInstance(this).accountDao();
        categoryDao = AppDatabase.getInstance(this).categoryDao();

        accountList = new ArrayList<>(accountDao.getAccounts());
        RecyclerView rvAccount = findViewById(R.id.rv_account);
        accountAdapter = new AccountAdapter(this, accountList);
        rvAccount.setAdapter(accountAdapter);

        categoryList = new ArrayList<>(categoryDao.getCategories());
        RecyclerView rvCategory = findViewById(R.id.rv_category);
        categoryAdapter = new CategoryAdapter(this, categoryList);
        rvCategory.setAdapter(categoryAdapter);
    }

    public void openNewTransaction(View view) {
        Intent intent = new Intent(MainActivity.this, NewTransactionActivity.class);
        startActivity(intent);
    }

    private void initData() {
        transactionList = new ArrayList<>();
        Intent intent = getIntent();
        Transaction transactionModel = (Transaction) intent.getSerializableExtra("model");
        if (transactionModel != null) transactionDao.insert(transactionModel);

        try {
            transactionList.addAll(transactionDao.getTransactionWithLimit(5));
        } catch (NullPointerException ignored) {
        }
    }

    public void setDateAsWeekly(View view) {
        // Current Date

        Calendar calendar = Calendar.getInstance();

        // Calculate the start of the current week (Sunday)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date startOfWeek = calendar.getTime();

        // Calculate the end of the current week (Saturday)
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        Date endOfWeek = calendar.getTime();
        try {
            BigDecimal expense = transactionDao.getExpenseThisWeek(startOfWeek, endOfWeek);
            BigDecimal income = transactionDao.getIncomeThisWeek(startOfWeek, endOfWeek);
            if (expense == null)
                expense = BigDecimal.valueOf(0);
            if (income == null)
                income = BigDecimal.valueOf(0);
            tvExpense.setText(CurrencyFormatter.convertFromString(expense));
            tvIncome.setText(CurrencyFormatter.convertFromString(income));
        } catch (NullPointerException ignored) {
        }
    }

    public void setDateAsMonthly(View view) {
        // Current Date
        Calendar calendar = Calendar.getInstance();

        // Set the day of the month to the first day
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startOfMonth = calendar.getTime();

        // Get the maximum number of days in the current month
        int maxDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Set the day of the month to the last day
        calendar.set(Calendar.DAY_OF_MONTH, maxDaysInMonth);
        Date endOfMonth = calendar.getTime();

        try {
            BigDecimal expense = transactionDao.getExpenseThisMonth(startOfMonth, endOfMonth);
            BigDecimal income = transactionDao.getIncomeThisMonth(startOfMonth, endOfMonth);
            if (expense == null)
                expense = BigDecimal.valueOf(0);
            if (income == null)
                income = BigDecimal.valueOf(0);
            tvExpense.setText(CurrencyFormatter.convertFromString(expense));
            tvIncome.setText(CurrencyFormatter.convertFromString(income));
        } catch (NullPointerException ignored) {
        }
    }

    public void setDateAsYearly(View view) {
        // Current Date
        Calendar calendar = Calendar.getInstance();

        // Set the day of the month to the first day of January
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startOfYear = calendar.getTime();

        // Set the month to December
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);

        // Set the day of the month to the last day of December
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        Date endOfYear = calendar.getTime();

        try {
            BigDecimal expense = transactionDao.getExpenseThisYear(startOfYear, endOfYear);
            BigDecimal income = transactionDao.getIncomeThisYear(startOfYear, endOfYear);
            if (expense == null)
                expense = BigDecimal.valueOf(0);
            if (income == null)
                income = BigDecimal.valueOf(0);
            tvExpense.setText(CurrencyFormatter.convertFromString(expense));
            tvIncome.setText(CurrencyFormatter.convertFromString(income));
        } catch (NullPointerException ignored) {
        }
    }

    private void setSavings() {
        TextView tvSavings = findViewById(R.id.tv_savings);

        BigDecimal sumExpense = transactionDao.getExpense();
        BigDecimal sumIncome = transactionDao.getIncome();
        if (sumExpense == null) {
            sumExpense = BigDecimal.valueOf(0);
        }
        if (sumIncome == null) {
            sumIncome = BigDecimal.valueOf(0);
        }
        String savings = String.valueOf(sumIncome.subtract(sumExpense));

        tvSavings.setText(savings);
    }

    private void setSavingsById(long accountId) {
        TextView tvSavings = findViewById(R.id.tv_savings);

        BigDecimal sumExpense = transactionDao.getExpenseByAccountId(accountId);
        BigDecimal sumIncome = transactionDao.getIncomeByAccountId(accountId);
        if (sumExpense == null) {
            sumExpense = BigDecimal.valueOf(0);
        }
        if (sumIncome == null) {
            sumIncome = BigDecimal.valueOf(0);
        }
        String savings = String.valueOf(sumIncome.subtract(sumExpense));

        tvSavings.setText(savings);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void openSidebar(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }


    public void addAccount(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_account_layout, null);
        dialogBuilder.setView(dialogView);
        EditText editText = dialogView.findViewById(R.id.et_account_name_popup);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the text from the EditText
                String enteredText = editText.getText().toString();
                accountDao.insert(new Account(enteredText));
                accountAdapter.setAccountList(accountDao.getAccounts());
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

    }


    public void addCategory(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_category_layout, null);
        dialogBuilder.setView(dialogView);
        EditText editText = dialogView.findViewById(R.id.et_account_name_popup);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the text from the EditText
                String enteredText = editText.getText().toString();
                categoryDao.insert(new Category(enteredText));
                categoryAdapter.setCategoryList(categoryDao.getCategories());
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

    }

    public void resetData(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setCancelable(true);
        builder.setTitle("Are you sure?");
        builder.setMessage("You want to delete all data?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        transactionDao.deleteAll();
                        accountDao.deleteAll();
                        categoryDao.deleteAll();
                        Intent intent = getIntent();
                        overridePendingTransition(0, 0);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void openListTransactions(View view) {
        Intent intent = new Intent(MainActivity.this, TransactionsActivity.class);
        startActivity(intent);
    }
}