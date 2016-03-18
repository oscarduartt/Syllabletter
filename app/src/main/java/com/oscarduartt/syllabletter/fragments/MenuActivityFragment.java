package com.oscarduartt.syllabletter.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.oscarduartt.syllabletter.R;
import com.oscarduartt.syllabletter.activities.FormingWordsActivity;
import com.oscarduartt.syllabletter.activities.ListenToLearnActivity;
import com.oscarduartt.syllabletter.activities.MissingVowelsActivity;
import com.oscarduartt.syllabletter.activities.SlidingSyllablesActivity;
import com.oscarduartt.syllabletter.objects.Game;
import com.oscarduartt.syllabletter.utilities.TransitionHelper;

import java.util.Locale;

public class MenuActivityFragment extends Fragment {

    private TextToSpeech textToSpeech;
    private Game game;

    public MenuActivityFragment newInstance() {
        return new MenuActivityFragment();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_activity, container, false);

        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    Locale locale = new Locale("spa", "ESP");
                    textToSpeech.setLanguage(locale);
                }
            }
        });

        AppCompatButton acbListenToLearn = (AppCompatButton) v.findViewById(R.id.btn_listen_to_learn);
        AppCompatButton acbMissingVowels = (AppCompatButton) v.findViewById(R.id.btn_missing_vowels);
        AppCompatButton acbSlidingSyllables = (AppCompatButton) v.findViewById(R.id.btn_sliding_syllables);
        AppCompatButton acbFormingWords = (AppCompatButton) v.findViewById(R.id.btn_forming_words);

        ImageView imgHiddenLevel = (ImageView) v.findViewById(R.id.img_hidden_level);
        final ImageView ivListenToLearn = (ImageView) v.findViewById(R.id.shared_target_listen_to_learn);
        ivListenToLearn.setColorFilter(ContextCompat.getColor(getContext(), R.color.blue), PorterDuff.Mode.SRC_IN);
        final ImageView ivMissingVowels = (ImageView) v.findViewById(R.id.shared_target_missing_vowels);
        ivMissingVowels.setColorFilter(ContextCompat.getColor(getContext(), R.color.green), PorterDuff.Mode.SRC_IN);
        final ImageView ivSlidingSyllables = (ImageView) v.findViewById(R.id.shared_target_sliding_syllables);
        ivSlidingSyllables.setColorFilter(ContextCompat.getColor(getContext(), R.color.red), PorterDuff.Mode.SRC_IN);
        final ImageView ivFormingWords = (ImageView) v.findViewById(R.id.shared_target_forming_words);
        ivFormingWords.setColorFilter(ContextCompat.getColor(getContext(), R.color.yellow), PorterDuff.Mode.SRC_IN);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.img_hidden_level:
                        createAlertHiddenLevel();
                        break;
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

        imgHiddenLevel.setOnClickListener(onClickListener);
        acbListenToLearn.setOnClickListener(onClickListener);
        acbMissingVowels.setOnClickListener(onClickListener);
        acbSlidingSyllables.setOnClickListener(onClickListener);
        acbFormingWords.setOnClickListener(onClickListener);

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

    private void createAlertHiddenLevel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppTheme_AlertDialog);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_hidden_level, null);
        final AppCompatEditText acetHiddenLevel = (AppCompatEditText)view.findViewById(R.id.acetHiddenLevel);

        builder.setView(view)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.play), null);

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        play(acetHiddenLevel.getText().toString());
                    }
                });
            }
        });

        alert.show();
    }

    private void play(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
