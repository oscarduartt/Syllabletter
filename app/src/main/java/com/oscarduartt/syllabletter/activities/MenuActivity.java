package com.oscarduartt.syllabletter.activities;

import android.os.Bundle;

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
}
