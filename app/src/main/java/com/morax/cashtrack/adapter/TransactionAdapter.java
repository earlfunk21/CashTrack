package com.morax.cashtrack.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.morax.cashtrack.R;
import com.morax.cashtrack.TransactionDetailsActivity;
import com.morax.cashtrack.database.entity.Transaction;
import com.morax.cashtrack.utils.CurrencyFormatter;
import com.morax.cashtrack.utils.Utils;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<Transaction> transactionList;
    private Context context;

    public TransactionAdapter(Context context, List<Transaction> transactionEntityList) {
        this.context = context;
        this.transactionList = transactionEntityList;
    }


    public void setTransactionEntityList(List<Transaction> transactionEntityList) {
        this.transactionList = transactionEntityList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        String amount = CurrencyFormatter.convertFromString(transaction.amount);
        String category = transaction.category;
        int thumbnail = Utils.getCategoryThumbnail(category);
        holder.getIvCategoryThumbnail().setImageResource(thumbnail);
        if (transaction.type == null) transaction.type = "Expense";
        if (transaction.type.equals("Expense")) {
            holder.getTvAmount().setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light));
            amount = "-" + amount;
        }
        holder.getTvAmount().setText(amount);
        holder.getTvDate().setText(Utils.formatDate(transaction.date));
        holder.getTvCategory().setText(category);
        holder.getCvTransactionItem().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TransactionDetailsActivity.class);
                intent.putExtra("model", transaction);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvAmount, tvCategory, tvDate;
        private ImageView ivCategoryThumbnail;

        private CardView cvTransactionItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvDate = itemView.findViewById(R.id.tv_date);
            ivCategoryThumbnail = itemView.findViewById(R.id.iv_category_thumbnail1);
            cvTransactionItem = itemView.findViewById(R.id.cv_transaction_item);
        }

        public TextView getTvAmount() {
            return tvAmount;
        }

        public TextView getTvCategory() {
            return tvCategory;
        }

        public TextView getTvDate() {
            return tvDate;
        }

        public ImageView getIvCategoryThumbnail() {
            return ivCategoryThumbnail;
        }

        public CardView getCvTransactionItem() {
            return cvTransactionItem;
        }
    }
}
