package com.oscarduartt.syllabletter.activities;

import android.os.Build;
import android.os.Bundle;

import com.oscarduartt.syllabletter.R;
import com.oscarduartt.syllabletter.fragments.SlidingSyllablesActivityFragment;
import com.oscarduartt.syllabletter.objects.Game;

public class SlidingSyllablesActivity extends BaseGameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_container);
        Game game = (Game) getIntent().getExtras().getSerializable("game");

        if (game != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setupToolbar();
                setupEnterAnimations();
                bindData(game);
            } else {
                setupToolbar(game.getTitle());
            }
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.game_container, new SlidingSyllablesActivityFragment().newInstance(true, 0)).commit();
        }
    }

}
