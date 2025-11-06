package com.khangmoihocit.th_shopping.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khangmoihocit.th_shopping.R;
import com.khangmoihocit.th_shopping.model.Order;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    Context context;
    ArrayList<Order> orderList;
    OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrderAdapter(Context context, ArrayList<Order> orderList, OnOrderClickListener listener) {
        this.context = context;
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.txtOrderId.setText("Đơn hàng #" + order.id);
        holder.txtOrderTotal.setText("Tổng tiền: " + order.totalAmount + "đ");

        // Format ngày tháng
        Date date = new Date(order.orderDate);
        String formattedDate = DateFormat.getDateTimeInstance().format(date);
        holder.txtOrderDate.setText("Ngày: " + formattedDate);

        holder.itemView.setOnClickListener(v -> listener.onOrderClick(order));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtOrderDate, txtOrderTotal;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtOrderTotal = itemView.findViewById(R.id.txtOrderTotal);
        }
    }
}
