package com.khangmoihocit.musicapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khangmoihocit.musicapp.model.MusicFile;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
    private final List<MusicFile> list;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MusicFile musicFile);
    }

    public MusicAdapter(List<MusicFile> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    static class MusicViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvArtist;
        public MusicViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvArtist = view.findViewById(R.id.tvArtist);
        }

        public void bind(final MusicFile file, final OnItemClickListener listener) {
            tvTitle.setText(file.title);
            tvArtist.setText(file.artist);
            itemView.setOnClickListener(v -> listener.onItemClick(file));
        }
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music,
                parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        holder.bind(list.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}