package com.oscarduartt.syllabletter.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.oscarduartt.syllabletter.R;
import com.oscarduartt.syllabletter.objects.Game;
import com.oscarduartt.syllabletter.utilities.TransitionHelper;

public class MenuActivityFragment extends Fragment {
    private Game game;

    public MenuActivityFragment newInstance() {
        return new MenuActivityFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_activity, container, false);

        AppCompatButton acbListenToLearn = (AppCompatButton) v.findViewById(R.id.btn_listen_to_learn);
        AppCompatButton acbMissingVowels = (AppCompatButton) v.findViewById(R.id.btn_missing_vowels);
        AppCompatButton acbSlidingSyllables = (AppCompatButton) v.findViewById(R.id.btn_sliding_syllables);
        AppCompatButton acbFormingWords = (AppCompatButton) v.findViewById(R.id.btn_forming_words);

        final ImageView ivListenToLearn = (ImageView) v.findViewById(R.id.shared_target_listen_to_learn);
        ivListenToLearn.setColorFilter(ContextCompat.getColor(getContext(), R.color.blue), PorterDuff.Mode.SRC_IN);
        final ImageView ivMissingVowels = (ImageView) v.findViewById(R.id.shared_target_missing_vowels);
        ivMissingVowels.setColorFilter(ContextCompat.getColor(getContext(), R.color.green), PorterDuff.Mode.SRC_IN);
        final ImageView ivSlidingSyllables = (ImageView) v.findViewById(R.id.shared_target_sliding_syllables);
        ivSlidingSyllables.setColorFilter(ContextCompat.getColor(getContext(), R.color.red), PorterDuff.Mode.SRC_IN);
        final ImageView ivFormingWords = (ImageView) v.findViewById(R.id.shared_target_forming_words);
        ivFormingWords.setColorFilter(ContextCompat.getColor(getContext(), R.color.yellow), PorterDuff.Mode.SRC_IN);

        /*View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_listen_to_learn:
                        game = new Game(getString(R.string.listen_to_learn), R.color.blue);
                        transitionToActivity(ListenToLearnActivity.class, ivListenToLearn, game, R.string.transition_game);
                        break;
                    case R.id.btn_missing_vowels:
                        game = new Game(getString(R.string.missing_vowels), R.color.green);
                        transitionToActivity(MissingVowelsActivity.class, ivMissingVowels, game, R.string.transition_game);
                        break;
                    case R.id.btn_sliding_syllables:
                        game = new Game(getString(R.string.sliding_syllables), R.color.red);
                        transitionToActivity(SlidingSyllablesActivity.class, ivSlidingSyllables, game, R.string.transition_game);
                        break;
                    case R.id.btn_forming_words:
                        game = new Game(getString(R.string.forming_words), R.color.yellow);
                        transitionToActivity(FormingWordsActivity.class, ivFormingWords, game, R.string.transition_game);
                        break;
                }
            }
        };

        acbListenToLearn.setOnClickListener(onClickListener);
        acbMissingVowels.setOnClickListener(onClickListener);
        acbSlidingSyllables.setOnClickListener(onClickListener);
        acbFormingWords.setOnClickListener(onClickListener);*/

        return v;
    }

    private void transitionToActivity(Class target, ImageView shared_image, Game game, int transitionName) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(getActivity(), false,
                new Pair<>(shared_image, getString(transitionName)));
        startActivity(target, pairs, game);
    }

    private void startActivity(Class target, Pair<View, String>[] pairs, Game game) {
        Intent i = new Intent(getActivity(), target);
        i.putExtra("game", game);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pairs);
            startActivity(i, transitionActivityOptions.toBundle());
        } else {
            startActivity(i);
        }
    }
}
