package com.morax.cashtrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.morax.cashtrack.adapter.TransactionAdapter;
import com.morax.cashtrack.model.TransactionModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RadioGroup rg;
    private RadioButton weeklyRG, monthlyRG, months3;

    private List<TransactionModel> transactionModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        RecyclerView rvTransaction = (RecyclerView) findViewById(R.id.rv_transaction);
        TransactionAdapter transactionAdapter = new TransactionAdapter(this, transactionModelList);
        rvTransaction.setAdapter(transactionAdapter);

        rg = findViewById(R.id.report_rg);
        rg.clearCheck();

        weeklyRG = findViewById(R.id.weekly_rb);
        monthlyRG = findViewById(R.id.monthly_rb);
        monthlyRG = findViewById(R.id.months3_rb);

        weeklyRG.setChecked(true);


    }

    public void openNewTransaction(View view) {
        Intent intent = new Intent(MainActivity.this, NewTransactionActivity.class);
        startActivity(intent);
    }

    private void initData() {
        transactionModelList = new ArrayList<>();
        Intent intent = getIntent();
        TransactionModel transactionModel = (TransactionModel) intent.getSerializableExtra("model");
        if (transactionModel != null)
            transactionModelList.add(transactionModel);

        transactionModelList.add(new TransactionModel(320000, "Sneakers", "01/01/2003", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
        transactionModelList.add(new TransactionModel(592929, "Fitness", "02/05/2003", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
        transactionModelList.add(new TransactionModel(312399, "Education", "07/25/2003", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
        transactionModelList.add(new TransactionModel(592929, "Fitness", "02/05/2003", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
        transactionModelList.add(new TransactionModel(320000, "Sneakers", "01/01/2003", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));


    }
}