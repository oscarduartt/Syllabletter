package com.oscarduartt.syllabletter.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.oscarduartt.syllabletter.R;
import com.oscarduartt.syllabletter.fragments.MenuActivityFragment;

public class MenuActivity extends BaseGameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setupWindowAnimations();
        setupToolbarMenu(getString(R.string.app_name));

        getSupportFragmentManager().beginTransaction().replace(R.id.menu_container, new MenuActivityFragment().newInstance()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                // ProjectsActivity is my 'home' activity
                Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
