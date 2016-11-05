package com.example.android.movieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import static android.R.attr.value;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        bindPreferenceSummaryToValue(findPreference("sort_list"));
        //setContentView(R.layout.activity_settings);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        /*SharedPreferences.Editor editor = preference.getEditor();
        editor.clear();
        Log.d("The value is ", newValue.toString() + "");
        ListPreference list = (ListPreference) preference;
        Log.d("The prefernce key is", preference.getKey());
        switch (newValue.toString()) {
            case "top_rated":
                Log.d("Top", " Rated");
                if (newValue.equals(true)) {
                    editor.putBoolean("top_rated", true);
                    editor.putBoolean("most_pop", false);
                }
                break;
            case "most_pop":
                Log.d("Most", " Popular");
                if (newValue.equals(true)) {
                    editor.putBoolean("top_rated", false);
                    editor.putBoolean("most_pop", true);
                }
                break;
        }
        editor.commit();
        return false;*/
        String stringValue = newValue.toString();
        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);
        //preference.setOnPreferenceClickListener(this);
        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

}
