package com.example.asumanga.data;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.example.asumanga.model.Entry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntryRepository {

    private static final String FILE_NAME = "entries.json";
    private static final String TAG = "EntryRepository";
    private static List<Entry> entries = new ArrayList<>();
    private static File storageFile;

    public static void init(Context context) {
        storageFile = new File(context.getFilesDir(), FILE_NAME);
        load();
    }

    public static List<Entry> getAll() {
        return entries;
    }

    public static void add(Entry entry) {
        entries.add(entry);
        save();
    }

    public static void update(int index, Entry entry) {
        if (index >= 0 && index < entries.size()) {
            entries.set(index, entry);
            save();
        }
    }

    public static void delete(int index) {
        if (index >= 0 && index < entries.size()) {
            Entry entry = entries.get(index);
            // Eliminar la imagen si existe
            if (entry.getCoverPath() != null) {
                try {
                    Uri uri = Uri.parse(entry.getCoverPath());
                    if ("file".equals(uri.getScheme())) {
                        File file = new File(uri.getPath());
                        if (file.exists()) {
                            if (file.delete()) {
                                Log.d(TAG, " " + file.getAbsolutePath());
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error al eliminar la imagen", e);
                }
            }
            entries.remove(index);
            save();
        }
    }

    public static List<Entry> getByType(Entry.Type type) {
        return entries.stream().filter(e -> e.getType() == type).collect(Collectors.toList());
    }

    private static void save() {
        if (storageFile == null) return;
        try (FileWriter writer = new FileWriter(storageFile)) {
            new Gson().toJson(entries, writer);
        } catch (IOException e) {
            Log.e(TAG, "Error saving entries", e);
        }
    }

    private static void load() {
        if (storageFile == null || !storageFile.exists()) return;
        try (FileReader reader = new FileReader(storageFile)) {
            Type listType = new TypeToken<ArrayList<Entry>>() {}.getType();
            List<Entry> loadedEntries = new Gson().fromJson(reader, listType);
            if (loadedEntries != null) entries = loadedEntries;
        } catch (IOException e) {
            Log.e(TAG, "Error loading entries", e);
        }
    }
}
