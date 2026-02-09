package com.example.asumanga.ui.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asumanga.R;
import com.example.asumanga.model.Entry;

import java.util.ArrayList;
import java.util.List;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ViewHolder> {

    private final List<Entry> entries;
    private final OnItemClickListener listener;

    public EntryAdapter(List<Entry> entries, OnItemClickListener listener) {
        this.entries = new ArrayList<>(entries);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entry, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(entries.get(position));
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageCover;
        final TextView textTitle;
        final TextView textType;
        final TextView textProgress;
        final TextView textRating;

        ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            imageCover = itemView.findViewById(R.id.image_cover);
            textTitle = itemView.findViewById(R.id.text_title);
            textType = itemView.findViewById(R.id.text_type);
            textProgress = itemView.findViewById(R.id.text_progress);
            textRating = itemView.findViewById(R.id.text_rating);
            itemView.setOnClickListener(v -> {
                if (getBindingAdapterPosition() != RecyclerView.NO_POSITION && listener != null)
                    listener.onItemClick(getBindingAdapterPosition());
            });
        }

        void bind(Entry entry) {
            textTitle.setText(entry.getTitle());
            String typeStr = "";
            switch (entry.getType()) {
                case MANGA: typeStr = itemView.getContext().getString(R.string.manga); break;
                case NOVEL: typeStr = itemView.getContext().getString(R.string.novel); break;
                case ANIME: typeStr = itemView.getContext().getString(R.string.anime); break;
            }
            textType.setText(typeStr);
            textProgress.setText(itemView.getContext().getString(R.string.progress_format, entry.getCurrentChapter(), entry.getTotalChapters()));
            textRating.setText(itemView.getContext().getString(R.string.rating_with_label,
                    entry.getRating() == 2 ? itemView.getContext().getString(R.string.good) :
                            entry.getRating() == 1 ? itemView.getContext().getString(R.string.meh) :
                                    itemView.getContext().getString(R.string.bad)));
            textRating.setTextColor(entry.getRating() == 2 ? 0xFF2E7D32 : entry.getRating() == 1 ? 0xFFF9A825 : 0xFFC62828);
            if (entry.getCoverPath() != null && !entry.getCoverPath().isEmpty()) imageCover.setImageURI(Uri.parse(entry.getCoverPath()));
            else imageCover.setImageResource(android.R.color.darker_gray);
        }
    }

    public interface OnItemClickListener { void onItemClick(int position); }
}
