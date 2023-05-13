package com.morax.cashtrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.morax.cashtrack.adapter.TransactionAdapter;
import com.morax.cashtrack.database.AppDatabase;
import com.morax.cashtrack.database.dao.TransactionDao;
import com.morax.cashtrack.database.entity.TransactionEntity;
import com.morax.cashtrack.utils.CurrencyFormatter;
import com.morax.cashtrack.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<TransactionEntity> transactionEntityList;

    private TransactionDao transactionDao;
    private TransactionAdapter transactionAdapter;
    private TextView tvExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transactionDao = AppDatabase.getInstance(this).transactionDao();
        initData();
        RecyclerView rvTransaction = findViewById(R.id.rv_transaction);
        transactionAdapter = new TransactionAdapter(this, transactionEntityList);
        rvTransaction.setAdapter(transactionAdapter);
        tvExpense = findViewById(R.id.tv_expense);
        setDateAsYearly(tvExpense);
    }

    public void openNewTransaction(View view) {
        Intent intent = new Intent(MainActivity.this, NewTransactionActivity.class);
        startActivity(intent);
    }

    private void initData() {
        transactionEntityList = new ArrayList<>();
        Intent intent = getIntent();
        TransactionEntity transactionModel = (TransactionEntity) intent.getSerializableExtra("model");
        if (transactionModel != null) transactionDao.insert(transactionModel);

        try {
            transactionEntityList.addAll(transactionDao.getAllTransactions());
        } catch (NullPointerException ignored) {}
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
            double expense = 0;
            if(transactionDao.getSumOfAmountThisWeek(startOfWeek, endOfWeek) != null)
                expense = transactionDao.getSumOfAmountThisWeek(startOfWeek, endOfWeek);
            tvExpense.setText(CurrencyFormatter.convertFromString(expense));
        } catch (NullPointerException ignored){}
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
            double expense = 0;
            if(transactionDao.getSumOfAmountThisWeek(startOfMonth, endOfMonth) != null)
                expense = transactionDao.getSumOfAmountThisWeek(startOfMonth, endOfMonth);
            tvExpense.setText(CurrencyFormatter.convertFromString(expense));
        } catch (NullPointerException ignored) {}
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
            double expense = 0;
            if(transactionDao.getSumOfAmountThisWeek(startOfYear, endOfYear) != null)
                expense = transactionDao.getSumOfAmountThisWeek(startOfYear, endOfYear);
            tvExpense.setText(CurrencyFormatter.convertFromString(expense));
        } catch (NullPointerException ignored) {}
    }

}