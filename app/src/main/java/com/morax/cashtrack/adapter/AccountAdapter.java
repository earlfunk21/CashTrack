package com.morax.cashtrack.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.morax.cashtrack.R;
import com.morax.cashtrack.database.AppDatabase;
import com.morax.cashtrack.database.dao.AccountDao;
import com.morax.cashtrack.database.entity.Account;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    private Context context;
    private List<Account> accountList;

    public AccountAdapter(Context context, List<Account> accountList) {
        this.context = context;
        this.accountList = accountList;
    }

    public void setAccountList(List<Account> accountList){
        this.accountList = accountList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Account account = accountList.get(position);
        String accountName = account.name;
        holder.tvAccountName.setText(accountName);
        holder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDelete(account);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    private void confirmDelete(Account account){
        AccountDao accountDao = AppDatabase.getInstance(context).accountDao();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        LayoutInflater inflater =  LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.pop_up_delete, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the text from the EditText
                accountDao.delete(account);
                setAccountList(accountDao.getAccounts());
            }
        });
        dialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAccountName;
        public ImageButton ibDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAccountName = itemView.findViewById(R.id.tv_account_name);
            ibDelete = itemView.findViewById(R.id.ib_delete);
        }
    }
}
