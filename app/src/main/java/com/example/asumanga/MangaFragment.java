package com.example.asumanga;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asumanga.data.EntryRepository;
import com.example.asumanga.model.Entry;
import com.example.asumanga.ui.adapter.EntryAdapter;

public class MangaFragment extends Fragment {

    public MangaFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manga, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_manga);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        EntryAdapter adapter = new EntryAdapter(EntryRepository.getByType(Entry.Type.MANGA));
        recyclerView.setAdapter(adapter);
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}