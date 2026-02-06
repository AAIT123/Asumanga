package com.example.asumanga.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asumanga.R;
import com.example.asumanga.model.Entry;

import java.util.List;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ViewHolder> {

    private final List<Entry> entries;

    private final OnItemClickListener listener;

    public EntryAdapter(List<Entry> entries, OnItemClickListener listener) {
        this.entries = entries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entry, parent, false), listener); }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) { holder.bind(entries.get(position)); }

    @Override
    public int getItemCount() { return entries.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textType, textProgress, textRating;
        ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_title);
            textType = itemView.findViewById(R.id.text_type);
            textProgress = itemView.findViewById(R.id.text_progress);
            textRating = itemView.findViewById(R.id.text_rating);
            itemView.setOnClickListener(v -> {
                if (getBindingAdapterPosition() != RecyclerView.NO_POSITION)
                    listener.onItemClick(getBindingAdapterPosition()); } );
        }

        void bind(Entry entry) {
            textTitle.setText(entry.getTitle());
            textType.setText(entry.getType().toString());
            textProgress.setText(entry.getCurrentChapter() + " / " + entry.getTotalChapters());
            textRating.setText(entry.getRating() == 2 ? "Rating: Good" : entry.getRating() == 1 ? "Rating: Meh" : "Rating: Bad");
            textRating.setTextColor(entry.getRating() == 2 ? 0xFF2E7D32 : entry.getRating() == 1 ? 0xFFF9A825 : 0xFFC62828);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
