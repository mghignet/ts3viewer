package com.mghignet.android.ts3viewer.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import com.mghignet.android.ts3viewer.Fragment.SettingsFragment;

public class SettingsActivity extends PreferenceActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Display the fragment as the main content.
    getFragmentManager().beginTransaction()
        .replace(android.R.id.content, new SettingsFragment())
        .commit();

    getActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        this.finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
