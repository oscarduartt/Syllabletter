package com.oscarduartt.syllabletter.fragments;

import android.content.ClipData;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oscarduartt.syllabletter.R;
import com.oscarduartt.syllabletter.objects.Word;
import com.oscarduartt.syllabletter.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * A placeholder fragment containing a simple view.
 */
public class FormingWordsActivityFragment extends Fragment implements View.OnDragListener {

    private TextToSpeech textToSpeech;
    private int position;
    private List<Word> words;
    private FloatingActionButton fab;
    private LinearLayout linearWord, linearOptions;
    private TextView tvResult;
    private Handler handler = new Handler();
    private char[] letters;
    private ArrayList<LinearLayout> container_answers = new ArrayList<>();
    private ArrayList<Integer> used_view_randoms;
    private ArrayList<View> view_answers = new ArrayList<>();
    private boolean first_time;
    private Word word;
    private CardView cvAnswersOptions, cvResult;

    public FormingWordsActivityFragment newInstance(boolean first_time, int position) {
        FormingWordsActivityFragment fragment = new FormingWordsActivityFragment();
        fragment.words = Utilities.getElements();
        fragment.position = position;
        fragment.first_time = first_time;
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_forming_words, container, false);
        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    Locale locale = new Locale("spa", "ESP");
                    textToSpeech.setLanguage(locale);
                }
            }
        });

        cvAnswersOptions = (CardView) view.findViewById(R.id.cv_answers_options_forming_words);
        cvResult = (CardView) view.findViewById(R.id.cv_result_forming_words);

        ImageView image = (ImageView) view.findViewById(R.id.img_forming_words);
        tvResult = (TextView) view.findViewById(R.id.tv_result_forming_words);

        if (savedInstanceState == null) {
           /* int aux = 0;
            while (position == aux) {
                aux = new Random().nextInt(words.size());
            }
            position = aux;
            word = words.get(position);*/
            if (position < words.size()) {
                word = words.get(position++);
            } else {
                position = 0;
                word = words.get(position);
            }
        } else {
            word = (Word) savedInstanceState.getSerializable(Utilities.KEY_WORD);
            position = savedInstanceState.getInt(Utilities.KEY_POSITION);
        }

        image.setImageResource(word.getImage());

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.play(textToSpeech, word.getName());
            }
        });
        fab = (FloatingActionButton) view.findViewById(R.id.fab_forming_words);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.hide();
                getFragmentManager().beginTransaction().replace(R.id.game_container, new FormingWordsActivityFragment().newInstance(false, position)).commit();
            }
        });

        linearWord = (LinearLayout) view.findViewById(R.id.ly_word_forming_words);
        linearOptions = (LinearLayout) view.findViewById(R.id.ly_options_forming_words);

        letters = word.getName().toCharArray();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (char letter : letters) {

                    final View view_item_container_letter = LayoutInflater.from(getContext()).inflate(R.layout.item_container_letter, null);
                    final LinearLayout linearContainerSyllable = (LinearLayout) view_item_container_letter.findViewById(R.id.ly_container_letter);

                    linearContainerSyllable.setTag("");

                    final View view_item_container_syllable_option = LayoutInflater.from(getContext()).inflate(R.layout.item_container_letter_option, null);
                    final LinearLayout linearContainerSyllableOption = (LinearLayout) view_item_container_syllable_option.findViewById(R.id.ly_container_letter_option);

                    final TextView tvSyllableOption = (TextView) view_item_container_syllable_option.findViewById(R.id.tv_letter_option);
                    tvSyllableOption.setText(Character.toString(letter));
                    tvSyllableOption.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            ClipData data = ClipData.newPlainText("", "");
                            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                            v.startDrag(data, shadowBuilder, v, 0);
                            v.setVisibility(View.INVISIBLE);
                            return true;
                        }
                    });

                    linearContainerSyllable.setOnDragListener(FormingWordsActivityFragment.this);
                    linearContainerSyllableOption.setOnDragListener(FormingWordsActivityFragment.this);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            container_answers.add(linearContainerSyllable);
                            linearWord.addView(view_item_container_letter);
                            view_answers.add(view_item_container_syllable_option);

                            if (view_answers.size() == letters.length) {
                                used_view_randoms = new ArrayList<>();
                                for (int i = 0; i < view_answers.size(); i++) {
                                    int new_random = Utilities.getRandom(view_answers.size(), used_view_randoms);
                                    linearOptions.addView(view_answers.get(new_random));
                                    used_view_randoms.add(new_random);
                                }
                            }
                        }
                    });
                }
            }
        });
        thread.start();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (first_time) {
                first_time = false;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cvAnswersOptions.setVisibility(View.VISIBLE);
                    }
                }, 1000);
            } else {
                cvAnswersOptions.setVisibility(View.VISIBLE);
            }
        } else {
            cvAnswersOptions.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Utilities.KEY_WORD, word);
        outState.putInt(Utilities.KEY_POSITION, position);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        View view = (View) event.getLocalState();
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DROP:

                if (((LinearLayout) v).getChildCount() <= 0) {
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    ((LinearLayout) v).addView(view);
                    v.setTag(((TextView) view).getText());
                }

                break;
            case DragEvent.ACTION_DRAG_ENDED:
                view.setVisibility(View.VISIBLE);

                if (event.getResult()) {

                    if (container_answers.size() == letters.length) {
                        if (checkFullAnswers()) {
                            checkAnswer();
                        } else {
                            fab.hide();
                            tvResult.setVisibility(View.GONE);
                        }
                    }

                }

            default:
                break;
        }
        return true;
    }

    private boolean checkFullAnswers() {
        for (LinearLayout linear : container_answers) {
            if (linear.getChildCount() == 0) {
                return false;
            }
        }
        return true;
    }

    private void checkAnswer() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                for (LinearLayout linear : container_answers) {
                    builder.append(linear.getTag().toString());
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (word.getName().equals(builder.toString())) {
                            Utilities.play(textToSpeech, word.getName());
                            cvResult.setVisibility(View.VISIBLE);
                            tvResult.setText(getString(R.string.correct));
                            tvResult.setTextColor(ContextCompat.getColor(getContext(), R.color.green_dark));
                            fab.show();
                        } else {

                            cvResult.setVisibility(View.VISIBLE);
                            tvResult.setText(getString(R.string.incorrect));
                            tvResult.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                            fab.hide();

                        }
                    }
                });
            }
        });
        thread.start();

        tvResult.setVisibility(View.VISIBLE);
    }
}
