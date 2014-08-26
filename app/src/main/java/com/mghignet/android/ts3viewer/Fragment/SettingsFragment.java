package com.mghignet.android.ts3viewer.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import com.mghignet.android.ts3viewer.R;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

  public static final String PREF_SERVER_ID = "pref_serverId";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Load the preferences from an XML resource
    addPreferencesFromResource(R.xml.preferences);
  }

  @Override
  public void onResume() {
    super.onResume();

    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    updatePreferenceSummary(PREF_SERVER_ID);
  }

  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    updatePreferenceSummary(key);
  }

  private void updatePreferenceSummary(String key) {
    Preference pref = findPreference(key);

    if (pref instanceof EditTextPreference) {
      EditTextPreference textPref = (EditTextPreference) pref;
      pref.setSummary(textPref.getText());
    }
  }
}
