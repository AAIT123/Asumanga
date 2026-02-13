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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class EntryFormHelper {

    private final Fragment fragment;

    private final MaterialAutoCompleteTextView title;
    private final EditText author;
    private final EditText description;
    private final EditText total;
    private final EditText current;
    private final MaterialAutoCompleteTextView typeDropdown;
    private final MaterialAutoCompleteTextView ratingDropdown;
    private final ImageView coverPath;

    private Entry entry;
    private String selectedUriString;

    public EntryFormHelper(Fragment fragment, Bundle args, MaterialAutoCompleteTextView title, EditText author, EditText description, EditText total, EditText current, MaterialAutoCompleteTextView typeDropdown, MaterialAutoCompleteTextView ratingDropdown, ImageView coverPath) {
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

        if (args == null || !args.containsKey("entryIndex")) {
            setupTitleAutocomplete();
        } else {
            title.setAdapter(null);
        }

        loadOrCreate(args);
    }

    public static void setupDropdown(MaterialAutoCompleteTextView dropdown, @ArrayRes int optionsRes) {
        String[] options = dropdown.getContext().getResources().getStringArray(optionsRes);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(dropdown.getContext(), android.R.layout.simple_list_item_1, options) {
            @NonNull @Override
            public Filter getFilter() { return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence c) {
                    FilterResults results = new FilterResults();
                    results.values = options;
                    results.count = options.length;
                    return results; }
                @Override
                protected void publishResults(CharSequence c, FilterResults r) { notifyDataSetChanged(); }
            }; }
        };
        dropdown.setAdapter(adapter);
    }

    private void setupTitleAutocomplete() {
        title.setThreshold(1);
        typeDropdown.setOnItemClickListener((parent, view, pos, id) ->
            title.setAdapter(
                parent.getItemAtPosition(pos).toString().equals(fragment.getString(R.string.anime)) ?
                    new ArrayAdapter<>(fragment.requireContext(), android.R.layout.simple_dropdown_item_1line, loadList(R.raw.anime)) :
                parent.getItemAtPosition(pos).toString().equals(fragment.getString(R.string.manga)) ?
                    new ArrayAdapter<>(fragment.requireContext(), android.R.layout.simple_dropdown_item_1line, loadList(R.raw.manga)) :
                parent.getItemAtPosition(pos).toString().equals(fragment.getString(R.string.novel)) ?
                    new ArrayAdapter<>(fragment.requireContext(), android.R.layout.simple_dropdown_item_1line, loadList(R.raw.novelas)) : null)
        );
    }

    private List<String> loadList(int resId) { return new BufferedReader(new InputStreamReader(fragment.getResources().openRawResource(resId))).lines().collect(Collectors.toList()); }

    private void loadOrCreate(Bundle args) {
        if (args != null && args.containsKey("entryIndex")) {
            if (args.getInt("entryIndex") < 0 || args.getInt("entryIndex") >= EntryRepository.getAll().size()) {
                Toast.makeText(fragment.requireContext(), fragment.getString(R.string.entry_not_found), Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(fragment).popBackStack(); return; }
            entry = EntryRepository.getAll().get(args.getInt("entryIndex")); populate(); }
        else {
            entry = new Entry("", "", 0, 0, 0, "", "", Entry.Type.MANGA);
            typeDropdown.setText("", false); ratingDropdown.setText("", false); }
    }

    private void populate() {
        title.setText(entry.getTitle());
        author.setText(entry.getAuthor());
        description.setText(entry.getDescription());
        total.setText(String.valueOf(entry.getTotalChapters()));
        current.setText(String.valueOf(entry.getCurrentChapter()));

        String[] typeOptions = fragment.getResources().getStringArray(R.array.type_options);
        String[] ratingOptions = fragment.getResources().getStringArray(R.array.rating_options);

        typeDropdown.setText(entry.getType() == Entry.Type.ANIME ? typeOptions[0] : entry.getType() == Entry.Type.MANGA ? typeOptions[1] : typeOptions[2], false);
        ratingDropdown.setText(ratingOptions[entry.getRating()], false);

        selectedUriString = entry.getCoverPath();
        if (selectedUriString != null && !selectedUriString.isEmpty()) coverPath.setImageURI(Uri.parse(selectedUriString));
    }

    public void setImageUri(Uri uri) {
        if (uri != null) {
            String localPath = saveToInternalStorage(uri);
            if (localPath != null) {
                selectedUriString = localPath;
                coverPath.setImageURI(Uri.parse(selectedUriString));
            }
        }
    }

    private String saveToInternalStorage(Uri uri) {
        try {
            // Generar un nombre único para evitar conflictos
            String fileName = "cover_" + System.currentTimeMillis() + ".jpg";
            File destFile = new File(fragment.requireContext().getFilesDir(), fileName);

            InputStream is = fragment.requireContext().getContentResolver().openInputStream(uri);
            FileOutputStream os = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }

            os.close();
            is.close();

            return Uri.fromFile(destFile).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean submit() {
        if (typeDropdown.getText().toString().isEmpty()) { typeDropdown.setError(fragment.getString(R.string.required)); return false; }
        if (ratingDropdown.getText().toString().isEmpty()) { ratingDropdown.setError(fragment.getString(R.string.required)); return false; }
        if (title.getText().toString().isEmpty()) { title.setError(fragment.getString(R.string.required)); return false; }
        if (parseInt(total.getText().toString()) < 1) { total.setError(fragment.getString(R.string.at_least_1)); return false; }
        if (parseInt(current.getText().toString()) < 0) { current.setError(fragment.getString(R.string.at_least_0)); return false; }
        if (parseInt(current.getText().toString()) > parseInt(total.getText().toString())) { current.setError(fragment.getString(R.string.invalid_progress)); return false; }

        entry.setTitle(title.getText().toString());
        entry.setAuthor(author.getText().toString().trim());
        entry.setDescription(description.getText().toString().trim());
        entry.setTotalChapters(parseInt(total.getText().toString()));
        entry.setCurrentChapter(parseInt(current.getText().toString()));

        String[] typeOptions = fragment.getResources().getStringArray(R.array.type_options);
        String typeText = typeDropdown.getText().toString();
        if (typeText.equals(typeOptions[0])) entry.setType(Entry.Type.ANIME);
        else if (typeText.equals(typeOptions[1])) entry.setType(Entry.Type.MANGA);
        else entry.setType(Entry.Type.NOVEL);

        String[] ratingOptions = fragment.getResources().getStringArray(R.array.rating_options);
        String ratingText = ratingDropdown.getText().toString();
        if (ratingText.equals(ratingOptions[2])) entry.setRating(2);
        else if (ratingText.equals(ratingOptions[1])) entry.setRating(1);
        else entry.setRating(0);

        entry.setCoverPath(selectedUriString);
        if (!EntryRepository.getAll().contains(entry)) EntryRepository.add(entry);
        else EntryRepository.update(EntryRepository.getAll().indexOf(entry), entry);
        return true;
    }

    private int parseInt(String value) {
        try { return Integer.parseInt(value); }
        catch (Exception e) { return 0; }
    }
}
