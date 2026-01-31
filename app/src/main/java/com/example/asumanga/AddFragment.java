package com.example.asumanga;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.asumanga.data.EntryRepository;
import com.example.asumanga.model.Entry;

public class AddFragment extends Fragment {

    public AddFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText editTitle = view.findViewById(R.id.input_title);
        EditText editAuthor = view.findViewById(R.id.input_author);
        EditText editTotal = view.findViewById(R.id.input_total_chapters);
        EditText editCurrent = view.findViewById(R.id.input_current_chapter);
        RadioGroup groupType = view.findViewById(R.id.type_group);
        RadioGroup groupRating = view.findViewById(R.id.rating_group);
        Button buttonSave = view.findViewById(R.id.button_save);
        buttonSave.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String author = editAuthor.getText().toString().trim();
            if (title.isEmpty()) { Toast.makeText(requireContext(), "Title required", Toast.LENGTH_SHORT).show(); return; }
            int total = parseInt(editTotal.getText().toString());
            int current = parseInt(editCurrent.getText().toString());
            Entry.Type type = groupType.getCheckedRadioButtonId() == R.id.type_manga ? Entry.Type.MANGA : Entry.Type.NOVEL;
            int rating = groupRating.getCheckedRadioButtonId() == R.id.rating_good ? 2 : groupRating.getCheckedRadioButtonId() == R.id.rating_meh ? 1 : 0;
            Entry entry = new Entry(title, author, total, current, rating, "", type);
            EntryRepository.add(entry);
            Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).popBackStack();
        });
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }
}