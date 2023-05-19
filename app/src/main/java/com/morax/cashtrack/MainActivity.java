package com.morax.cashtrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import com.morax.cashtrack.adapter.TransactionAdapter;
import com.morax.cashtrack.database.AppDatabase;
import com.morax.cashtrack.database.dao.TransactionDao;
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
    private TransactionAdapter transactionAdapter;
    private TextView tvExpense;
    private TextView tvIncome;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transactionDao = AppDatabase.getInstance(this).transactionDao();
        initData();
        RecyclerView rvTransaction = findViewById(R.id.rv_transaction);
        transactionAdapter = new TransactionAdapter(this, transactionList);
        rvTransaction.setAdapter(transactionAdapter);
        tvExpense = findViewById(R.id.tv_expense);
        tvIncome = findViewById(R.id.tv_income);
        setDateAsYearly(tvExpense);
        setSavings();
        drawerLayout = findViewById(R.id.drawerLayout);
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
            transactionList.addAll(transactionDao.getTransactionDesc());
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

    public void openProfile(View view) {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
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

    @Override
    protected void onPause() {
        super.onPause();
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void openSidebar(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
}