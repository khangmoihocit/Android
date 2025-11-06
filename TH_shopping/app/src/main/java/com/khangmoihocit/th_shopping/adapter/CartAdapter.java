package com.khangmoihocit.th_shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khangmoihocit.th_shopping.R;
import com.khangmoihocit.th_shopping.model.CartItem;

import java.util.ArrayList;

// Adapter cho danh sách sản phẩm trong giỏ hàng (OrderActivity)
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context context;
    ArrayList<CartItem> cartItems;
    OnCartActionListener listener;

    public interface OnCartActionListener {
        void onIncrease(CartItem item);
        void onDecrease(CartItem item);
        void onDelete(CartItem item);
    }

    public CartAdapter(Context context, ArrayList<CartItem> cartItems, OnCartActionListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.txtName.setText(item.name);
        holder.txtQuantity.setText(String.valueOf(item.quantity));
        holder.txtPrice.setText(String.valueOf(item.price) + "đ");
        holder.txtTotal.setText("Tổng: " + (item.price * item.quantity) + "đ");

        holder.btnPlus.setOnClickListener(v -> listener.onIncrease(item));
        holder.btnMinus.setOnClickListener(v -> listener.onDecrease(item));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(item));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtQuantity, txtTotal;
        ImageButton btnPlus, btnMinus, btnDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtCartProductName);
            txtPrice = itemView.findViewById(R.id.txtCartProductPrice);
            txtQuantity = itemView.findViewById(R.id.txtCartQuantity);
            txtTotal = itemView.findViewById(R.id.txtCartTotal);
            btnPlus = itemView.findViewById(R.id.btnCartIncrease);
            btnMinus = itemView.findViewById(R.id.btnCartDecrease);
            btnDelete = itemView.findViewById(R.id.btnCartDelete);
        }
    }
}
