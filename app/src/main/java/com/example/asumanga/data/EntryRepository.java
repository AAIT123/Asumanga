package com.example.asumanga.data;

import com.example.asumanga.model.Entry;

import java.util.ArrayList;
import java.util.List;

public class EntryRepository {

    private static final List<Entry> entries = new ArrayList<>();

    public static List<Entry> getAll() {
        return entries;
    }

    public static void add(Entry entry) {
        entries.add(entry);
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
}
