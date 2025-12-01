package com.khangmoihocit.th_quanlynhansu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NhanVienAdapter extends RecyclerView.Adapter<NhanVienAdapter.NhanVienViewHolder> {
    private List<NhanVien> mList;

    private IClickItemNhanVien iClickItemProduct;

    public interface IClickItemNhanVien{
        void delete(NhanVien nhanVien);
        void clickItem(View view, int position);
    }
    public NhanVienAdapter(IClickItemNhanVien iClickItemProduct){
        mList = new ArrayList<>();
        this.iClickItemProduct = iClickItemProduct;
    }

    public void setData(List<NhanVien> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    public NhanVien get(int position){
        return mList.get(position);
    }

    @NonNull
    @Override
    public NhanVienViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nhanvien, parent, false);
        return new NhanVienViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NhanVienViewHolder holder, int position) {
        NhanVien nhanVien = mList.get(position);
        holder.tvHoTen.setText(nhanVien.getHoTen());
        holder.tvChucVu.setText(nhanVien.getChuVu());
        holder.tvLuongCoBan.setText(nhanVien.getLuongCoBan().toString());
        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemProduct.delete(nhanVien);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(this.mList != null) return this.mList.size();
        return 0;
    }

    public class NhanVienViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvHoTen, tvChucVu, tvLuongCoBan;
        private Button btnXoa;

        public NhanVienViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHoTen = itemView.findViewById(R.id.tvHoTen);
            tvChucVu = itemView.findViewById(R.id.tvChucVu);
            tvLuongCoBan = itemView.findViewById(R.id.tvLuongCoBan);
            btnXoa = itemView.findViewById(R.id.btnXoa);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(iClickItemProduct != null){
                iClickItemProduct.clickItem(v, getAdapterPosition());
            }
        }
    }
}
