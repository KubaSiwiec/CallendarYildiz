package com.jakubsiwiec.callendar;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }


    public static class SettingsFragment extends PreferenceFragmentCompat {

        public SwitchPreferenceCompat themeSwitch;
        public boolean swChecked = false;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);


            themeSwitch = (SwitchPreferenceCompat) findPreference("themeSw");

            themeSwitch.setSwitchTextOff("Switch to dark mode");
            themeSwitch.setSwitchTextOn("Switch to light mode");

            themeSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if(themeSwitch.isChecked()){
                        MainActivity.changeToTheme(getActivity(), MainActivity.THEME_DEFAULT);
                    }else{
                        MainActivity.changeToTheme(getActivity(), MainActivity.THEME_DARK);
                    }
                    return false;
                }
            });
        }
    }

}

