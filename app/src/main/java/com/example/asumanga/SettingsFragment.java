package com.example.asumanga;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asumanga.helper.EntryFormHelper;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class SettingsFragment extends Fragment {

    private static final String PREFS_NAME = "settings";
    private static final String KEY_THEME_MODE = "theme_mode";

    public SettingsFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MaterialAutoCompleteTextView dropdown = view.findViewById(R.id.dropdown_theme);
        String[] options = getResources().getStringArray(R.array.theme_options);
        EntryFormHelper.setupDropdown(dropdown, R.array.theme_options);
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        dropdown.setText(options[prefs.getString(KEY_THEME_MODE, "system").equals("light") ? 1 : prefs.getString(KEY_THEME_MODE, "system").equals("dark") ? 2 : 0], false);
        dropdown.setOnItemClickListener((parent, v, position, id) -> {
            AppCompatDelegate.setDefaultNightMode(position == 1 ? AppCompatDelegate.MODE_NIGHT_NO : position == 2  ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            prefs.edit().putString(KEY_THEME_MODE, position == 1 ? "light" : position == 2  ? "dark" : "system").apply();
        });
    }
}