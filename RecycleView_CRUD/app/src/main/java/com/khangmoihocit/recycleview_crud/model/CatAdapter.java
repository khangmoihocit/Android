package com.khangmoihocit.recycleview_crud.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khangmoihocit.recycleview_crud.R;

import java.util.ArrayList;
import java.util.List;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.CatViewHolder>{
    private Context context;
    private List<Cat> list;
    private CatItemListener catItemListener;

    public CatAdapter(Context context){
        this.context = context;
        list = new ArrayList<>();
    }

    public void setCatItemListener(CatItemListener catItemListener){
        this.catItemListener = catItemListener;
    }

    public void add(Cat cat){
        list.add(cat);
        notifyDataSetChanged(); //làm mới recycle view
    }

    public void update(int position, Cat cat){
        list.set(position, cat);
        notifyDataSetChanged();
    }

    public Cat get (int position){
        return list.get(position);
    }


    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent,false);

        return new CatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Cat cat = list.get(position);
        if(cat == null) return;
        holder.img.setImageResource(cat.getImg());
        holder.tvName.setText(cat.getName());
        holder.tvDescribe.setText(cat.getDescribe());
        holder.tvPrice.setText(cat.getPrice() + "");
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list != null) return list.size();
        return 0;
    }

    public class CatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView img;
        private TextView tvName, tvDescribe, tvPrice;
        private Button btnRemove;
        public CatViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescribe = itemView.findViewById(R.id.tvDescribe);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(catItemListener != null){
                catItemListener.onItemClick(v, getAdapterPosition());
            }
        }
    }// catviewholder

    public interface CatItemListener{
        void onItemClick(View view, int position);
    }

}
