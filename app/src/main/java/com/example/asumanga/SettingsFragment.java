package com.example.asumanga;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asumanga.helper.EntryFormHelper;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class SettingsFragment extends Fragment {

    private static final String PREFS_NAME = "settings";
    private static final String KEY_THEME_MODE = "theme_mode";
    private static final String KEY_LANG_MODE = "lang_mode";

    public SettingsFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        MaterialAutoCompleteTextView langDropdown = view.findViewById(R.id.dropdown_language);
        MaterialAutoCompleteTextView dropdownTheme = view.findViewById(R.id.dropdown_theme);
        String[] langOptions = getResources().getStringArray(R.array.language_options);
        String[] optionsTheme = getResources().getStringArray(R.array.theme_options);

        EntryFormHelper.setupDropdown(dropdownTheme, R.array.theme_options);
        dropdownTheme.setText(optionsTheme[prefs.getString(KEY_THEME_MODE, "system").equals("light") ? 1 : prefs.getString(KEY_THEME_MODE, "system").equals("dark") ? 2 : 0], false);
        dropdownTheme.setOnItemClickListener((parent, v, position, id) -> {
            prefs.edit().putString(KEY_THEME_MODE, position == 1 ? "light" : position == 2  ? "dark" : "system").apply();
            AppCompatDelegate.setDefaultNightMode(position == 1 ? AppCompatDelegate.MODE_NIGHT_NO : position == 2 ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        });

        EntryFormHelper.setupDropdown(langDropdown, R.array.language_options);
        langDropdown.setText(langOptions[!AppCompatDelegate.getApplicationLocales().isEmpty() && AppCompatDelegate.getApplicationLocales().get(0) != null ? "en".equals(AppCompatDelegate.getApplicationLocales().get(0).getLanguage()) ? 1 : "es".equals(AppCompatDelegate.getApplicationLocales().get(0).getLanguage()) ? 2 : 0 : 0], false);
        langDropdown.setOnItemClickListener((parent, v, position, id) -> {
            prefs.edit().putString(KEY_LANG_MODE, position == 1 ? "english" : position == 2  ? "spanish" : "system").apply();
            AppCompatDelegate.setApplicationLocales(position == 1 ? LocaleListCompat.forLanguageTags("en") : position == 2 ? LocaleListCompat.forLanguageTags("es") : LocaleListCompat.getEmptyLocaleList());
        });
    }
}
