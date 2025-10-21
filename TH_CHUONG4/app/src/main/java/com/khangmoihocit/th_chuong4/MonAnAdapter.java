package com.khangmoihocit.th_chuong4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MonAnAdapter extends RecyclerView.Adapter<MonAnAdapter.MonAnViewHolder> {
    private final Context context;
    private List<MonAn> danhSachMonAn;
    private final IClickItemFood iClickItemFood;
    public interface IClickItemFood{
        void showActivityFoodDetail(MonAn monAn);
    }

    public MonAnAdapter(Context context, IClickItemFood iClickItemFood) {
        this.context = context;
        this.iClickItemFood = iClickItemFood;
    }

    public void setData(List<MonAn> list){
        this.danhSachMonAn = list;
        notifyDataSetChanged();
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemFood.showActivityFoodDetail(monAn);
            }
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
