package com.khangmoihocit.th_quanlybanhang;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> mList;
    private IClickItemProduct iClickItemProduct;
    public interface IClickItemProduct{
        void delete(Product product);
        void clickItem(View view, int position);
    }
    public ProductAdapter(IClickItemProduct iClickItemProduct) {
        this.iClickItemProduct = iClickItemProduct;
        this.mList = new ArrayList<>();
    }

    public void setData(List<Product> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    public Product get(int position){
        return mList.get(position);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = mList.get(position);
        holder.tvTitle.setText(product.getTitle());
        holder.tvPrice.setText(product.getPrice().toString());
        holder.tvDescription.setText(product.getDescription());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemProduct.delete(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(this.mList != null) return this.mList.size();
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle, tvPrice, tvDescription;
        private Button btnDelete;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvDescription = itemView.findViewById(R.id.tv_description);
            btnDelete = itemView.findViewById(R.id.btn_delete);
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
