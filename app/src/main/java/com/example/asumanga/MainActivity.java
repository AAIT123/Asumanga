package com.example.asumanga;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.asumanga.data.EntryRepository;
import com.example.asumanga.model.Entry;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        if (EntryRepository.getAll().isEmpty()) {
            EntryRepository.add(new Entry(
                    "Sousou no Frieren",
                    "Kanehito Yamada",
                    120,
                    45,
                    2,
                    "Fantasy journey after the hero's death",
                    Entry.Type.MANGA));
            EntryRepository.add(new Entry(
                    "Mushoku Tensei",
                    "Rifujin na Magonote",
                    26,
                    14,
                    2,
                    "Reincarnation story",
                    Entry.Type.NOVEL));}
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment =
                item.getItemId() == R.id.nav_home  ? new HomeFragment()  :
                item.getItemId() == R.id.nav_manga ? new MangaFragment() :
                item.getItemId() == R.id.nav_novel ? new NovelFragment() :
                item.getItemId() == R.id.nav_add   ? new AddFragment()   :
                null;
        if (fragment == null) return false;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        return true;
    }
}