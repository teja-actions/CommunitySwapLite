package com.example.communityhub.legacyui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import com.example.communityhub.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    private Switch swDarkMode;
    private Button btnClearData;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dbHelper = new DBHelper(this);
        swDarkMode = findViewById(R.id.swDark);
        btnClearData = findViewById(R.id.btnClearData);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        swDarkMode.setChecked(prefs.getBoolean("pref_dark_mode", false));

        // Handle dark mode toggle
        swDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("pref_dark_mode", isChecked).apply();
            Toast.makeText(this, "Restart app to apply theme", Toast.LENGTH_SHORT).show();
        });

        // Clear data button
        btnClearData.setOnClickListener(v -> {
            dbHelper.clearAll();
            Toast.makeText(this, "All items cleared", Toast.LENGTH_SHORT).show();
        });
    }
}
