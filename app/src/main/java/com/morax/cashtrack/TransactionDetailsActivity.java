package com.morax.cashtrack;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.morax.cashtrack.database.AppDatabase;
import com.morax.cashtrack.database.dao.TransactionDao;
import com.morax.cashtrack.database.entity.Transaction;
import com.morax.cashtrack.utils.CurrencyFormatter;
import com.morax.cashtrack.utils.Utils;


public class TransactionDetailsActivity extends AppCompatActivity {

    private TextView tvCategory, tvAmount, tvDate, tvNote;
    private Transaction transaction;
    private TransactionDao transactionDao;
    private long transaction_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        tvCategory = findViewById(R.id.tv_category_details);
        tvAmount = findViewById(R.id.tv_amount_details);
        tvDate = findViewById(R.id.tv_date_details);
        tvNote = findViewById(R.id.tv_note_details);

        Intent intent = getIntent();
        transaction = (Transaction) intent.getSerializableExtra("model");
        transaction_id = transaction.id;

        tvCategory.setText(transaction.category);
        tvAmount.setText(CurrencyFormatter.convertFromString(transaction.amount));
        tvDate.setText(Utils.formatDate(transaction.date));
        tvNote.setText(transaction.note);

        transactionDao = AppDatabase.getInstance(this).transactionDao();
    }

    public void goBack(View view) {
        onBackPressed();
        finish();
    }

    public void deleteTransaction(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setCancelable(true);
        builder.setTitle("Are you sure?");
        builder.setMessage("You want to delete this transaction?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        transactionDao.delete(transaction);
                        Intent intent = new Intent(TransactionDetailsActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
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

    public void editTransaction(View view) {
        Intent intent = new Intent(TransactionDetailsActivity.this, EditTransactionActivity.class);
        intent.putExtra("id", transaction_id);
        startActivity(intent);
    }
}