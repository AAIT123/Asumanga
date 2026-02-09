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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asumanga.helper.EntryFormHelper;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class AddFragment extends Fragment {
    private EntryFormHelper form;
    private final ActivityResultLauncher<String> imagePicker = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> { if (uri != null && form != null) { form.setImageUri(uri); } }
    );

    public AddFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MaterialAutoCompleteTextView title = view.findViewById(R.id.input_title);
        EditText   author  = view.findViewById(R.id.input_author);
        EditText   desc    = view.findViewById(R.id.input_description);
        EditText   total   = view.findViewById(R.id.input_total_chapters);
        EditText   current = view.findViewById(R.id.input_current_chapter);
        MaterialAutoCompleteTextView type = view.findViewById(R.id.dropdown_type);
        MaterialAutoCompleteTextView rating = view.findViewById(R.id.dropdown_rating);
        ImageView  cover   = view.findViewById(R.id.cover_image);
        Button     pick    = view.findViewById(R.id.button_select_image);
        Button     save    = view.findViewById(R.id.button_save);
        form = new EntryFormHelper(this, null, title, author, desc, total, current, type, rating, cover);
        pick.setOnClickListener(v -> imagePicker.launch("image/*"));
        save.setOnClickListener(v -> { if (form.submit()) {
                Toast.makeText(requireContext(), R.string.saved, Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).popBackStack(); } });
        cancel.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        // --- Cargar listas desde archivos raw ---
        List<String> animeList  = loadListFromRaw(R.raw.anime);
        List<String> mangaList  = loadListFromRaw(R.raw.manga);
        List<String> novelasList = loadListFromRaw(R.raw.novelas);

        // --- Crear adapters ---
        ArrayAdapter<String> animeAdapter  = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, animeList);
        ArrayAdapter<String> mangaAdapter  = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, mangaList);
        ArrayAdapter<String> novelasAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, novelasList);

        title.setThreshold(1); // Autocompletado desde la primera letra

        // --- Cambiar autocompletado según tipo seleccionado ---
        type.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedType = ((String) parent.getItemAtPosition(position)).toLowerCase();

            switch (selectedType) {
                case "anime":
                    title.setAdapter(animeAdapter);
                    break;
                case "manga":
                    title.setAdapter(mangaAdapter);
                    break;
                case "novela":
                case "novel":
                    title.setAdapter(novelasAdapter);
                    break;
                default:
                    title.setAdapter(null);
                    break;
            }
        });
    }

    // --- Función genérica para cargar listas desde archivos raw ---
    private List<String> loadListFromRaw(int rawId) {
        List<String> list = new ArrayList<>();
        try {
            InputStream is = getResources().openRawResource(rawId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
