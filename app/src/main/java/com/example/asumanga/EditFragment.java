package com.example.asumanga;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asumanga.helper.EntryFormHelper;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class EditFragment extends Fragment {
    private EntryFormHelper form;
    private final ActivityResultLauncher<String> imagePicker = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> { if (uri != null && form != null) { form.setImageUri(uri); } }
    );

    public EditFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText   title   = view.findViewById(R.id.input_title);
        EditText   author  = view.findViewById(R.id.input_author);
        EditText   desc    = view.findViewById(R.id.input_description);
        EditText   total   = view.findViewById(R.id.input_total_chapters);
        EditText   current = view.findViewById(R.id.input_current_chapter);
        MaterialAutoCompleteTextView type = view.findViewById(R.id.dropdown_type);
        MaterialAutoCompleteTextView rating = view.findViewById(R.id.dropdown_rating);
        ImageView  cover   = view.findViewById(R.id.cover_image);
        Button     pick    = view.findViewById(R.id.button_select_image);
        Button     save    = view.findViewById(R.id.button_save);
        Button     cancel  = view.findViewById(R.id.button_cancel);
        form = new EntryFormHelper(this, getArguments(), title, author, desc, total, current, type, rating, cover);
        pick.setOnClickListener(v -> imagePicker.launch("image/*"));
        save.setOnClickListener(v -> { if (form.submit()) {
                Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).popBackStack(); } });
        cancel.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
    }
}
