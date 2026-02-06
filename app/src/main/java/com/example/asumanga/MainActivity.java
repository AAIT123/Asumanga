package com.example.asumanga;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.asumanga.data.EntryRepository;
import com.example.asumanga.model.Entry;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    NavHostFragment navHostFragment;
    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applyTheme();
        super.onCreate(savedInstanceState);
        
        // Inicializar el repositorio para cargar datos guardados
        EntryRepository.init(this);
        
        setContentView(R.layout.activity_main);
        
        // Solo agregar ejemplos si es la primera vez que se abre la app
        if (EntryRepository.getAll().isEmpty()) {
            EntryRepository.add(new Entry(
                    "Sousou no Frieren",
                    "Kanehito Yamada",
                    120,
                    45,
                    2,
                    "Fantasy journey after the hero's death",
                    "",
                    Entry.Type.MANGA));
            EntryRepository.add(new Entry(
                    "Mushoku Tensei",
                    "Rifujin na Magonote",
                    26,
                    14,
                    2,
                    "Reincarnation story",
                    "",
                    Entry.Type.NOVEL));
        }
        
        bottomNavigationView = findViewById(R.id.bottom_nav);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment == null) throw new IllegalStateException("NavHostFragment not found");
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.nav_host_fragment), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
    }

    private void applyTheme() {
        SharedPreferences prefs = getSharedPreferences("settings", Context.MODE_PRIVATE);
        switch (prefs.getString("theme_mode", "system")) {
        case "light": AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);  break;
        case "dark": AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);  break;
        default: AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM); }
    }
}
