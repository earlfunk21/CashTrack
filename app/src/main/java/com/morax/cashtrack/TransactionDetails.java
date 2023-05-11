package com.morax.cashtrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.morax.cashtrack.model.TransactionModel;
import com.morax.cashtrack.utils.CurrencyFormatter;

public class TransactionDetails extends AppCompatActivity {

    private TextView tvCategory, tvAmount, tvDate, tvNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        tvCategory = findViewById(R.id.tv_category_details);
        tvAmount = findViewById(R.id.tv_amount_details);
        tvDate = findViewById(R.id.tv_date_details);
        tvNote = findViewById(R.id.tv_note_details);

        Intent intent = getIntent();
        TransactionModel transactionModel = (TransactionModel) intent.getSerializableExtra("model");

        tvCategory.setText(transactionModel.getCategory());
        tvAmount.setText(CurrencyFormatter.convertFromString(transactionModel.getAmount()));
        tvDate.setText(transactionModel.getDate());
        tvNote.setText(transactionModel.getNote());
    }

    public void goBack(View view) {
        onBackPressed();
        finish();
    }
}