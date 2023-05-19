package com.morax.cashtrack;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.morax.cashtrack.adapter.AccountAdapter;
import com.morax.cashtrack.adapter.CategoryAdapter;
import com.morax.cashtrack.database.AppDatabase;
import com.morax.cashtrack.database.dao.AccountDao;
import com.morax.cashtrack.database.dao.CategoryDao;
import com.morax.cashtrack.database.entity.Account;
import com.morax.cashtrack.database.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private AccountDao accountDao;
    private CategoryDao categoryDao;
    private AccountAdapter accountAdapter;
    private CategoryAdapter categoryAdapter;
    private List<Account> accountList;
    private List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        accountDao = AppDatabase.getInstance(this).accountDao();
        categoryDao = AppDatabase.getInstance(this).categoryDao();

        accountList = new ArrayList<>(accountDao.getAccounts());
        RecyclerView rvAccount = findViewById(R.id.rv_account);
        accountAdapter = new AccountAdapter(this, accountList);
        rvAccount.setAdapter(accountAdapter);

        categoryList = new ArrayList<>(categoryDao.getCategories());
        RecyclerView rvCategory = findViewById(R.id.rv_category);
        categoryAdapter = new CategoryAdapter(this, categoryList);
        rvCategory.setAdapter(categoryAdapter);
    }

    public void goBack(View view) {
        onBackPressed();
        finish();
    }

    public void addAccount(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_account_layout, null);
        dialogBuilder.setView(dialogView);
        EditText editText = dialogView.findViewById(R.id.et_account_name_popup);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the text from the EditText
                String enteredText = editText.getText().toString();
                accountDao.insert(new Account(enteredText));
                accountAdapter.setAccountList(accountDao.getAccounts());
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

    }

    public void addCategory(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_category_layout, null);
        dialogBuilder.setView(dialogView);
        EditText editText = dialogView.findViewById(R.id.et_account_name_popup);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the text from the EditText
                String enteredText = editText.getText().toString();
                categoryDao.insert(new Category(enteredText));
                categoryAdapter.setCategoryList(categoryDao.getCategories());
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

    }
}