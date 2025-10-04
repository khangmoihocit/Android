package com.khangmoihocit.recycleview.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.khangmoihocit.recycleview.R;

import java.util.List;

//RecyclerView.Adapter<CatAdapter.CatViewHolder>: có 3 lớp phương thức cần ghi đè
public class CatAdapter extends RecyclerView.Adapter<CatAdapter.CatViewHolder>{
    private Context context;
    private List<Cat> list;
    private CatItemListener catItemListener;

    public void setCatItemListener(CatItemListener catItemListener) {
        this.catItemListener = catItemListener;
    }

    public CatAdapter(Context context, List<Cat> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override //trả về 1 view, tạo khung gồm 1 list các item trống
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //view cho layout nao
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new CatViewHolder(view);
    }

    @Override //gán dữ liệu vào các item trống trong khung theo từng position
    //dùng để gắn (bind) dữ liệu từ một vị trí cụ thể trong danh sách của bạn
    // vào các thành phần giao diện (views) trong một ViewHolder.
    public void onBindViewHolder(@NonNull CatViewHolder holder, int position) {
        Cat cat = list.get(position); //position: vị trí của item trong list
        if(cat == null) return;
        holder.img.setImageResource(cat.getImg());
        holder.tv.setText(cat.getName());

        //gán sự kiện click vào 1 cardview
//        holder.cv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context.getApplicationContext()
//                        , cat.getName()
//                        , Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if(list != null) return list.size();
        return 0;
    }

    //la 1 item (layout - item.xml)
    public class CatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView img;
        private TextView tv;
//        private CardView cv;

        public CatViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tv = itemView.findViewById(R.id.tv);
//            cv = itemView.findViewById(R.id.cvCat);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(catItemListener != null){
                catItemListener.onItemClick(v, getAdapterPosition()); //có position để ánh xạ ở nơi khác
            }
        }
    }

    public interface CatItemListener{
        public void onItemClick(View view, int position);
    }
}
