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
import com.example.asumanga.ui.adapter.EntryAdapter;

public class HomeFragment extends Fragment {

    public HomeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        EntryAdapter adapter = new EntryAdapter(EntryRepository.getAll(),
                position -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("entryIndex", position);
                    NavHostFragment.findNavController(this).navigate(R.id.action_home_to_edit, bundle);
                });
        recyclerView.setAdapter(adapter);
    }
}