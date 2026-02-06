package com.example.asumanga.data;

import android.content.Context;
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

public class EntryRepository {

    private static final String FILE_NAME = "entries.json";
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

    public static List<Entry> getByType(Entry.Type type) {
        List<Entry> filtered = new ArrayList<>();
        for (Entry e : entries) {
            if (e.getType() == type) {
                filtered.add(e);
            }
        }
        return filtered;
    }

    private static void save() {
        if (storageFile == null) return;
        try (FileWriter writer = new FileWriter(storageFile)) {
            new Gson().toJson(entries, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void load() {
        if (storageFile == null || !storageFile.exists()) return;
        try (FileReader reader = new FileReader(storageFile)) {
            Type listType = new TypeToken<ArrayList<Entry>>() {}.getType();
            List<Entry> loadedEntries = new Gson().fromJson(reader, listType);
            if (loadedEntries != null) {
                entries = loadedEntries;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
