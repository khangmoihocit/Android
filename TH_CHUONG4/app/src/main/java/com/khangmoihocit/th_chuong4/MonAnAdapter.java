package com.khangmoihocit.th_chuong4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MonAnAdapter extends RecyclerView.Adapter<MonAnAdapter.MonAnViewHolder> {

    private final Context context;
    private final ArrayList<MonAn> danhSachMonAn;

    public MonAnAdapter(Context context, ArrayList<MonAn> danhSachMonAn) {
        this.context = context;
        this.danhSachMonAn = danhSachMonAn;
    }

    @NonNull
    @Override
    public MonAnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_mon_an, parent, false);
        return new MonAnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonAnViewHolder holder, int position) {
        MonAn monAn = danhSachMonAn.get(position);

        holder.tvFoodName.setText(monAn.getTenMon());
        holder.tvFoodPrice.setText(monAn.getGia());
        holder.ivFoodImage.setImageResource(monAn.getHinhAnh());

        holder.itemView.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle(monAn.getTenMon())
                    .setMessage(monAn.getMoTa())
                    .setPositiveButton("Đóng", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return danhSachMonAn.size();
    }

    public static class MonAnViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoodImage;
        TextView tvFoodName;
        TextView tvFoodPrice;

        public MonAnViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoodImage = itemView.findViewById(R.id.ivFoodImage);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvFoodPrice = itemView.findViewById(R.id.tvFoodPrice);
        }
    }
}
