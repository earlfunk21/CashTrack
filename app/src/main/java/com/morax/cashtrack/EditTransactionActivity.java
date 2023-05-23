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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.morax.cashtrack.database.AppDatabase;
import com.morax.cashtrack.database.dao.AccountDao;
import com.morax.cashtrack.database.dao.CategoryDao;
import com.morax.cashtrack.database.dao.TransactionDao;
import com.morax.cashtrack.database.entity.Account;
import com.morax.cashtrack.database.entity.Category;
import com.morax.cashtrack.database.entity.Transaction;
import com.morax.cashtrack.utils.CurrencyFormatter;
import com.morax.cashtrack.utils.Utils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditTransactionActivity extends AppCompatActivity {

    private TextView tvTransDate;
    private EditText etAmount, etNote;
    private Date date;
    private TransactionDao transactionDao;
    private CategoryDao categoryDao;
    private AccountDao accountDao;
    private String transactionType;
    private Transaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);
        transactionDao = AppDatabase.getInstance(this).transactionDao();
        categoryDao = AppDatabase.getInstance(this).categoryDao();
        accountDao = AppDatabase.getInstance(this).accountDao();

        long id = getIntent().getExtras().getLong("id");
        transaction = transactionDao.getTransactionById(id);


        etAmount = findViewById(R.id.et_amount);
        tvTransDate = findViewById(R.id.tv_trans_date_edit);
        etNote = findViewById(R.id.et_note);

        // Init Date
        tvTransDate.setText(Utils.formatDate(transaction.date));
        date = transaction.date;
        etAmount.setText(String.valueOf(transaction.amount));
        etNote.setText(transaction.note);
        RadioGroup radioGroup = findViewById(R.id.rg_type);
        transactionType = transaction.type;
        if (transaction.type.equals("Expense")) {
            radioGroup.check(R.id.rb_expense);
        } else {
            radioGroup.check(R.id.rb_income);
        }

        TextView tvCategory = findViewById(R.id.tv_category_edit);
        TextView tvAccount = findViewById(R.id.tv_account_name_edit);
        tvCategory.setText(transaction.category);

        Account account = accountDao.getAccountById(transaction.accountId);
        tvAccount.setText(account.name);
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

    public void setTypeIncome(View view) {
        transactionType = "Income";
    }

    public void setTypeExpense(View view) {
        transactionType = "Expense";
    }

    public void openTransfer(View view) {
        Intent intent = new Intent(EditTransactionActivity.this, TransferActivity.class);
        startActivity(intent);
    }

    public void updateTransaction(View view) {
        BigDecimal amount = BigDecimal.valueOf(Long.parseLong(etAmount.getText().toString()));
        String note = etNote.getText().toString();
        transaction.date = date;
        transaction.amount = amount;
        transaction.note = note;
        transaction.type = transactionType;
        transactionDao.update(transaction);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}