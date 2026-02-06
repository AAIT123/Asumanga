package com.example.asumanga.helper;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.asumanga.R;
import com.example.asumanga.data.EntryRepository;
import com.example.asumanga.model.Entry;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class EntryFormHelper {

    private final Fragment fragment;

    private final EditText title;
    private final EditText author;
    private final EditText description;
    private final EditText total;
    private final EditText current;
    private final MaterialAutoCompleteTextView typeDropdown;
    private final MaterialAutoCompleteTextView ratingDropdown;
    private final ImageView coverPath;

    private Entry entry;
    private String selectedUriString;

    public EntryFormHelper(Fragment fragment, Bundle args, EditText title, EditText author, EditText description, EditText total, EditText current, MaterialAutoCompleteTextView typeDropdown, MaterialAutoCompleteTextView ratingDropdown, ImageView coverPath) {
        this.fragment = fragment;
        this.title = title;
        this.author = author;
        this.description = description;
        this.total = total;
        this.current = current;
        this.typeDropdown = typeDropdown;
        this.ratingDropdown = ratingDropdown;
        this.coverPath = coverPath;
        setupDropdown(typeDropdown, R.array.type_options);
        setupDropdown(ratingDropdown, R.array.rating_options);
        loadOrCreate(args);
    }

    public static void setupDropdown(MaterialAutoCompleteTextView dropdown, @ArrayRes int optionsRes) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(dropdown.getContext(), android.R.layout.simple_list_item_1, dropdown.getContext().getResources().getStringArray(optionsRes)) { @NonNull @Override
            public Filter getFilter() { return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence c) {
                    FilterResults results = new FilterResults();
                    results.values = dropdown.getContext().getResources().getStringArray(optionsRes);
                    results.count = dropdown.getContext().getResources().getStringArray(optionsRes).length;
                    return results;
                }

                @Override
                protected void publishResults(CharSequence c, FilterResults r) { notifyDataSetChanged(); }
            }; }
        };
        dropdown.setAdapter(adapter);
    }

    private void loadOrCreate(Bundle args) {
        if (args != null && args.containsKey("entryIndex")) {
            if (args.getInt("entryIndex") < 0 || args.getInt("entryIndex") >= EntryRepository.getAll().size()) {
                Toast.makeText(fragment.requireContext(), "Entry not found", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(fragment).popBackStack(); return; }
            entry = EntryRepository.getAll().get(args.getInt("entryIndex")); populate(); }
        else {
            entry = new Entry("", "", 0, 0, 0, "", "", Entry.Type.MANGA);
            typeDropdown.setText(fragment.getResources().getStringArray(R.array.type_options)[0], false);
            ratingDropdown.setText(fragment.getResources().getStringArray(R.array.rating_options)[0], false); }
    }

    private void populate() {
        title.setText(entry.getTitle());
        author.setText(entry.getAuthor());
        description.setText(entry.getDescription());
        total.setText(String.valueOf(entry.getTotalChapters()));
        current.setText(String.valueOf(entry.getCurrentChapter()));
        
        String[] typeOptions = fragment.getResources().getStringArray(R.array.type_options);
        typeDropdown.setText(entry.getType() == Entry.Type.MANGA ? typeOptions[0] : typeOptions[1], false);
        
        String[] ratingOptions = fragment.getResources().getStringArray(R.array.rating_options);
        ratingDropdown.setText(ratingOptions[entry.getRating()], false);

        selectedUriString = entry.getCoverPath();
        if (selectedUriString != null && !selectedUriString.isEmpty()) coverPath.setImageURI(Uri.parse(selectedUriString));
    }

    public void setImageUri(Uri uri) {
        if (uri != null) {
            selectedUriString = uri.toString();
            coverPath.setImageURI(uri); }
    }

    public boolean submit() {
        if (title.getText().toString().isEmpty()) { title.setError("Required"); return false; }
        if (parseInt(total.getText().toString()) < 1) { total.setError("Must be at least 1"); return false; }
        if (parseInt(current.getText().toString()) < 0) { current.setError("Must be at least 0"); return false; }
        if (parseInt(current.getText().toString()) > parseInt(total.getText().toString())) { current.setError("Invalid progress"); return false; }
        
        entry.setTitle(title.getText().toString());
        entry.setAuthor(author.getText().toString().trim());
        entry.setDescription(description.getText().toString().trim());
        entry.setTotalChapters(parseInt(total.getText().toString()));
        entry.setCurrentChapter(parseInt(current.getText().toString()));
        
        String typeText = typeDropdown.getText().toString();
        String[] typeOptions = fragment.getResources().getStringArray(R.array.type_options);
        entry.setType(typeText.equals(typeOptions[0]) ? Entry.Type.MANGA : Entry.Type.NOVEL);
        
        String ratingText = ratingDropdown.getText().toString();
        String[] ratingOptions = fragment.getResources().getStringArray(R.array.rating_options);
        entry.setRating(ratingText.equals(ratingOptions[2]) ? 2 : ratingText.equals(ratingOptions[1]) ? 1 : 0);

        entry.setCoverPath(selectedUriString);
        if (!EntryRepository.getAll().contains(entry)) EntryRepository.add(entry);
        return true;
    }

    private int parseInt(String value) {
        try { return Integer.parseInt(value); }
        catch (Exception e) { return 0; }
    }
}
