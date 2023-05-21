package com.morax.cashtrack;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.morax.cashtrack.database.AppDatabase;
import com.morax.cashtrack.database.dao.AccountDao;
import com.morax.cashtrack.database.dao.CategoryDao;
import com.morax.cashtrack.database.dao.TransactionDao;
import com.morax.cashtrack.database.entity.Account;
import com.morax.cashtrack.database.entity.Transaction;
import com.morax.cashtrack.utils.Utils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransferActivity extends AppCompatActivity {

    private TextView tvTransDate;
    private EditText etAmount, etNote;
    private long accountIdFrom, accountIdTo;
    private Date date;
    private TransactionDao transactionDao;
    private AccountDao accountDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        transactionDao = AppDatabase.getInstance(this).transactionDao();
        accountDao = AppDatabase.getInstance(this).accountDao();

        etAmount = findViewById(R.id.et_amount_transfer);
        tvTransDate = findViewById(R.id.tv_trans_date_transfer);
        etNote = findViewById(R.id.et_note_transfer);

        // Init Date
        date = Calendar.getInstance().getTime();
        tvTransDate.setText(Utils.formatDate(date));

        List<Account> accounts = accountDao.getAccounts();
        ArrayAdapter<Account> accountArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_layout, accounts);

        Spinner spinnerFrom = findViewById(R.id.spinner_account_transfer_from);
        accountArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerFrom.setAdapter(accountArrayAdapter);
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Account selectedAccount = (Account) parent.getItemAtPosition(position);
                accountIdFrom = selectedAccount.id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when no item is selected
            }
        });

        Spinner spinnerTo = findViewById(R.id.spinner_account_transfer_to);
        accountArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerTo.setAdapter(accountArrayAdapter);
        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Account selectedAccount = (Account) parent.getItemAtPosition(position);
                accountIdTo = selectedAccount.id;
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

    public void openDateDialog(View view) {
        Calendar calendar = Calendar.getInstance();
        int initialYear = calendar.get(Calendar.YEAR);
        int initialMonth = calendar.get(Calendar.MONTH);
        int initialDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.MyDatePickerStyle, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                date = calendar.getTime();
                tvTransDate.setText(Utils.formatDate(date));
            }
        }, initialYear, initialMonth, initialDay);
        datePickerDialog.show();
    }

    public void addNewTransaction(View view) {
        BigDecimal amount = BigDecimal.valueOf(Long.parseLong(etAmount.getText().toString()));
        String note = etNote.getText().toString();
        Transaction transaction = new Transaction(accountIdFrom, "Transfer", date, amount, note);
        transaction.type = "Expense";
        transactionDao.insert(transaction);

        transaction = new Transaction(accountIdTo, "Transfer", date, amount, note);
        transaction.type = "Income";
        transactionDao.insert(transaction);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}