package com.morax.cashtrack.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.morax.cashtrack.R;
import com.morax.cashtrack.database.AppDatabase;
import com.morax.cashtrack.database.dao.CategoryDao;
import com.morax.cashtrack.database.entity.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    public void setCategoryList(List<Category> categoryList){
        this.categoryList = categoryList;
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
        Category account = categoryList.get(position);
        String accountName = account.name;
        holder.tvCategoryName.setText(accountName);
        holder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDelete(account);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    private void confirmDelete(Category category){
        CategoryDao categoryDao = AppDatabase.getInstance(context).categoryDao();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        LayoutInflater inflater =  LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.pop_up_delete, null);
        TextView tvName = dialogView.findViewById(R.id.et_account_name_popup_delete);
        tvName.setText(category.name);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the text from the EditText
                categoryDao.delete(category);
                setCategoryList(categoryDao.getCategories());
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
        public TextView tvCategoryName;
        public ImageButton ibDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tv_account_name);
            ibDelete = itemView.findViewById(R.id.ib_delete);
        }
    }
}
