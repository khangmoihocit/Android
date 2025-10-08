package com.khangmoihocit.noteapps.ui;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khangmoihocit.noteapps.R;
import com.khangmoihocit.noteapps.data.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.VH> {

    public interface OnNoteClick {
        void onClick(Note n, View anchor);
    }

    private List<Note> data = new ArrayList<>();
    private final OnNoteClick callback;
    private final SimpleDateFormat fmt = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());

    public NoteAdapter(OnNoteClick cb) {
        this.callback = cb;
    }

    public void submit(List<Note> list) {
        data = list;
        notifyDataSetChanged(); // For simplicity, we use notifyDataSetChanged
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(data.get(position), callback, fmt);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvMeta;

        VH(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvContent = v.findViewById(R.id.tvContent);
            tvMeta = v.findViewById(R.id.tvMeta);
        }

        void bind(Note n, OnNoteClick listener, SimpleDateFormat fmt) {
            tvTitle.setText(n.title == null || n.title.isEmpty() ? "(Không tiêu đề)" : n.title);
            tvContent.setText(n.content == null ? "" : n.content);

            String meta = "Sửa: " + fmt.format(new Date(n.updatedAt));
            if (n.remindAt != null) {
                meta += "  •  Nhắc: " + fmt.format(new Date(n.remindAt));
            }
            tvMeta.setText(meta);

            itemView.setOnClickListener(v -> listener.onClick(n, v));
        }
    }
}