package com.example.asumanga;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asumanga.data.EntryRepository;
import com.example.asumanga.model.Entry;
import com.example.asumanga.ui.adapter.EntryAdapter;

public class AnimeFragment extends Fragment {

    public AnimeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_anime, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_anime);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        EntryAdapter adapter = new EntryAdapter(EntryRepository.getByType(Entry.Type.ANIME),
                position -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("entryIndex", position);
                    NavHostFragment.findNavController(this).navigate(R.id.action_anime_to_edit, bundle);
                });
        recyclerView.setAdapter(adapter);
    }
}
