package com.morax.cashtrack.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.morax.cashtrack.R;
import com.morax.cashtrack.model.TransactionModel;
import com.morax.cashtrack.utils.CurrencyFormatter;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<TransactionModel> transactionModelList;
    private Context context;

    public TransactionAdapter(Context context, List<TransactionModel> transactionModelList) {
        this.context = context;
        this.transactionModelList = transactionModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.transaction_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionModel transactionModel = transactionModelList.get(position);
        String amount = CurrencyFormatter.convertFromString(transactionModel.getAmount());
        String category = transactionModel.getCategory();
        String date = transactionModel.getDate();

        int thumbnail = R.drawable.sneakers;
        switch (category){
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
        holder.getIvCategoryThumbnail().setImageResource(thumbnail);
        holder.getTvAmount().setText(amount);
        holder.getTvDate().setText(date);
        holder.getTvCategory().setText(category);
    }

    @Override
    public int getItemCount() {
        return transactionModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvAmount, tvCategory, tvDate;
        private ImageView ivCategoryThumbnail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvDate = itemView.findViewById(R.id.tv_date);
            ivCategoryThumbnail = itemView.findViewById(R.id.iv_category_thumbnail1);
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
    }
}
