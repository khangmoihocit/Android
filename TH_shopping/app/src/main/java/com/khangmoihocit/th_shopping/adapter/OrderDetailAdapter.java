package com.khangmoihocit.th_shopping.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khangmoihocit.th_shopping.R;
import com.khangmoihocit.th_shopping.model.OrderDetail;

import java.util.ArrayList;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.DetailViewHolder> {

    Context context;
    ArrayList<OrderDetail> detailList;

    public OrderDetailAdapter(Context context, ArrayList<OrderDetail> detailList) {
        this.context = context;
        this.detailList = detailList;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_detail, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        OrderDetail item = detailList.get(position);

        holder.txtProductName.setText(item.productName);
        holder.txtPriceAndQty.setText("Đơn giá: " + item.productPrice + "đ x " + item.quantity);
    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtPriceAndQty;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtDetailProductName);
            txtPriceAndQty = itemView.findViewById(R.id.txtDetailPriceAndQty);
        }
    }
}

