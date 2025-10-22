package com.khangmoihocit.a1_sqllite_room;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{
    private List<User> mListUser;

    private final IClickItemUser iClickItemUser;
    public interface IClickItemUser{ //handle event in item_adapter
        void updateUser(User user);
        void deleteUser(User user);
    }
    public UserAdapter(IClickItemUser iClickItemUser){
        this.iClickItemUser = iClickItemUser;
    }

    public void setData(List<User> list){
        this.mListUser = list;
        notifyDataSetChanged();
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
        if(user == null) return;

        holder.tvUsername.setText(user.getUsername());
        holder.tvAddress.setText(user.getAddress());
        holder.tvYear.setText(user.getYear().toString());

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemUser.updateUser(user);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemUser.deleteUser(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(this.mListUser != null){
            return this.mListUser.size();
        }
        return 0;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvUsername;
        private final TextView tvAddress;
        private final TextView tvYear;
        private final Button btnUpdate;
        private final Button btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tv_username);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvYear = itemView.findViewById(R.id.tv_year);
            btnUpdate = itemView.findViewById(R.id.btn_update);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
