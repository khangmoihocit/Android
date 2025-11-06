package com.khangmoihocit.th_shopping.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khangmoihocit.th_shopping.R;
import com.khangmoihocit.th_shopping.model.Product;

import java.util.ArrayList;

// Hiển thị danh sách sản phẩm trong MainActivity
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    ArrayList<Product> products;
    ArrayList<Product> originalList; // Danh sách gốc để filter
    OnProductClickListener listener;

    public interface OnProductClickListener {
        void onAddToCart(Product product);
    }

    public ProductAdapter(Context context, ArrayList<Product> products, OnProductClickListener listener) {
        this.context = context;
        this.products = products;
        this.originalList = new ArrayList<>(products);
        this.listener = listener;
    }

    // Cập nhật danh sách gốc khi dữ liệu thay đổi
    public void setOriginalList(ArrayList<Product> list) {
        this.originalList = new ArrayList<>(list);
    }

    // Lọc danh sách
    public void filter(String text) {
        products.clear();
        if (text.isEmpty()) {
            products.addAll(originalList);
        } else {
            text = text.toLowerCase();
            for (Product p : originalList) {
                if (p.name.toLowerCase().contains(text)) {
                    products.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = products.get(position);
        holder.txtName.setText(p.name);
        holder.txtPrice.setText(String.valueOf(p.price) + "đ"); // Thêm đơn vị
        holder.btnAdd.setOnClickListener(v -> listener.onAddToCart(p));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;
        Button btnAdd;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
            btnAdd = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
