package com.khangmoihocit.a4_searchview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> implements Filterable {
    private List<User> mListUser;
    private List<User> mListUserOld;

    public UserAdapter(List<User> mListUser) {
        this.mListUser = mListUser;
        this.mListUserOld = mListUser;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = mListUser.get(position);
        if (user == null) return;
        holder.img.setImageResource(user.getImage());
        holder.tvName.setText(user.getName());
        holder.tvAddress.setText(user.getAddress());
    }

    @Override
    public int getItemCount() {
        if(mListUser != null) return mListUser.size();
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String keyword = constraint.toString();
                if(keyword.isEmpty()){
                    mListUser = mListUserOld;
                }else{
                    List<User> users = new ArrayList<>();
                    for(User user : mListUserOld){
                        if(user.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                                user.getAddress().toLowerCase().contains(keyword.toLowerCase())){
                            users.add(user);
                        }
                    }
                    mListUser = users;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mListUser;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mListUser = (List<User>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView tvName;
        private TextView tvAddress;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_user);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
        }
    }
}
