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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.morax.cashtrack.model.TransactionModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewTransactionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView tvTransDate;
    private EditText etAmount, etNote;
    private ImageView ivCategoryThumbnail;
    private String strCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);

        etAmount = findViewById(R.id.et_amount);
        tvTransDate = findViewById(R.id.tv_trans_date);
        etNote = findViewById(R.id.et_note);
        ivCategoryThumbnail = findViewById(R.id.iv_category_thumbnail);

        Spinner spinnerCategory = findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> spinnerCategoryAdapter = ArrayAdapter.createFromResource(this, R.array.categories, R.layout.category_spinner_layout);
        spinnerCategoryAdapter.setDropDownViewResource(R.layout.category_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerCategoryAdapter);
        spinnerCategory.setOnItemSelectedListener(this);
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
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault());
                String formattedDate = sdf.format(calendar.getTime());
                tvTransDate.setText(formattedDate);
            }
        }, initialYear, initialMonth, initialDay);
        datePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        strCategory = adapterView.getItemAtPosition(i).toString();
        int thumbnail = R.drawable.sneakers;
        switch (strCategory){
            case "Sneakers":
                thumbnail = R.drawable.sneakers;
                break;
            case "Fitness":
                thumbnail = R.drawable.fitness;
                break;
            case "Education":
                thumbnail = R.drawable.education;
                break;
        }
        ivCategoryThumbnail.setImageResource(thumbnail);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void addNewTransaction(View view) {
        double amount = Double.parseDouble(etAmount.getText().toString());
        String date = tvTransDate.getText().toString();
        String note = etNote.getText().toString();
        TransactionModel transactionModel = new TransactionModel(amount, strCategory, date, note);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("model", transactionModel);
        startActivity(intent);
    }
}