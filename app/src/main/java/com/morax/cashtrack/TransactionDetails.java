package com.morax.cashtrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.morax.cashtrack.database.entity.TransactionEntity;
import com.morax.cashtrack.utils.CurrencyFormatter;
import com.morax.cashtrack.utils.Utils;


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
        TransactionEntity transactionEntity = (TransactionEntity) intent.getSerializableExtra("model");

        tvCategory.setText(transactionEntity.category);
        tvAmount.setText(CurrencyFormatter.convertFromString(transactionEntity.amount));
        tvDate.setText(Utils.formatDate(transactionEntity.date));
        tvNote.setText(transactionEntity.note);
    }

    public void goBack(View view) {
        onBackPressed();
        finish();
    }
}