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
    private String strCategory;
    private long accountId;
    private Date date;
    private TransactionDao transactionDao;
    private CategoryDao categoryDao;
    private AccountDao accountDao;
    private String transactionType = "Expense";
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
        etAmount.setText(CurrencyFormatter.convertFromString(transaction.amount));
        etNote.setText(transaction.note);
        RadioGroup radioGroup = findViewById(R.id.rg_type);

        if (transaction.type.equals("Expense")) {
            radioGroup.check(R.id.rb_expense);
        } else {
            radioGroup.check(R.id.rb_income);
        }

        // Spinner Category
        Spinner spinnerCategory = findViewById(R.id.spinner_category);
        List<Category> categories = categoryDao.getCategories();
        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_layout, categories);
        categoryArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryArrayAdapter);
        Category category = categoryDao.getCategoryByName(transaction.category);
        int selectedCategoryPosition = categories.indexOf(category);
        spinnerCategory.setSelection(selectedCategoryPosition);
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
        Spinner spinner = findViewById(R.id.spinner_account); // Replace `R.id.spinner` with the actual ID of your spinner
        List<Account> accounts = accountDao.getAccounts();
        ArrayAdapter<Account> accountArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_layout, accounts);
        accountArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(accountArrayAdapter);
        Account account = accountDao.getAccountById(transaction.accountId);
        spinner.setSelection(accountArrayAdapter.getPosition(account));
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
        Transaction transaction = new Transaction(accountId, strCategory, date, amount, note);
        transaction.type = transactionType;
        transactionDao.insert(transaction);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
}