package com.oscarduartt.syllabletter.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.oscarduartt.syllabletter.R;
import com.oscarduartt.syllabletter.objects.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class MissingVowelsActivityFragment extends Fragment {

    private static final String KEY_WORD = "key_word";
    private static final String KEY_POSITION = "key_position";
    private TextToSpeech textToSpeech;
    private int position;
    private List<Word> words = new ArrayList<>();
    private FloatingActionButton fab;
    private LinearLayout linearWord;
    private Handler handler = new Handler();
    private ArrayList<TextView> views_word = new ArrayList<>();
    private TextView tvAnswer;
    private ArrayList<TextView> options_word = new ArrayList<>();
    private boolean first_time;
    private Word word;

    public MissingVowelsActivityFragment newInstance(boolean first_time, int position) {
        MissingVowelsActivityFragment fragment = new MissingVowelsActivityFragment();
        Word[] objects = new Word[]{new Word("CONEJO", R.mipmap.conejo),
                new Word("GALLINA", R.mipmap.gallina),
                new Word("GATO", R.mipmap.gato),
                new Word("GUSANO", R.mipmap.gusano),
                new Word("PERRO", R.mipmap.perro),
                new Word("TORTUGA", R.mipmap.tortuga)};
        fragment.words = Arrays.asList(objects);
        fragment.first_time = first_time;
        fragment.position = position;
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
        View view = inflater.inflate(R.layout.fragment_missing_vowels, container, false);

        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    Locale locale = new Locale("spa", "ESP");
                    textToSpeech.setLanguage(locale);
                }
            }
        });

        ImageView image = (ImageView) view.findViewById(R.id.img_missing_vowels);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
        linearWord = (LinearLayout) view.findViewById(R.id.ly_word_missing_vowels);
        tvAnswer = (TextView) view.findViewById(R.id.tv_answer_missing_vowels);
        fab = (FloatingActionButton) view.findViewById(R.id.fab_missing_vowels);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.hide();
                getFragmentManager().beginTransaction().replace(R.id.game_container, new MissingVowelsActivityFragment().newInstance(false, position)).commit();
            }
        });

        if (savedInstanceState == null) {
            int aux = 0;
            while (position == aux) {
                aux = new Random().nextInt(words.size());
            }
            position = aux;
            word = words.get(position);
        } else {
            word = (Word) savedInstanceState.getSerializable(KEY_WORD);
            position = savedInstanceState.getInt(KEY_POSITION);
        }

        assert word != null;
        image.setImageResource(word.getImage());

        final char[] palabra = word.getName().toCharArray();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i <= palabra.length - 1; i++) {
                    if (!Character.toString(palabra[i]).equals(getString(R.string.vowel_a)) &&
                            !Character.toString(palabra[i]).equals(getString(R.string.vowel_e)) &&
                            !Character.toString(palabra[i]).equals(getString(R.string.vowel_i)) &&
                            !Character.toString(palabra[i]).equals(getString(R.string.vowel_o)) &&
                            !Character.toString(palabra[i]).equals(getString(R.string.vowel_u))) {
                        final View item = LayoutInflater.from(getContext()).inflate(R.layout.item_letter, null);
                        final TextView letter = (TextView) item.findViewById(R.id.tv_item_letter);
                        letter.setText(Character.toString(palabra[i]));
                        letter.setTag(Character.toString(palabra[i]));
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                linearWord.addView(item);
                            }
                        });

                        views_word.add(letter);

                    } else {
                        final View item_container = LayoutInflater.from(getContext()).inflate(R.layout.item_letter_option, null);
                        final TextView tvLetterOption = (TextView) item_container.findViewById(R.id.tv_item_letter_option);
                        tvLetterOption.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                createAlertOption(v);
                            }
                        });

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                linearWord.addView(item_container);
                            }
                        });
                        options_word.add(tvLetterOption);
                        views_word.add(tvLetterOption);
                    }
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
                        linearWord.setVisibility(View.VISIBLE);
                    }
                }, 1000);
            } else {
                linearWord.setVisibility(View.VISIBLE);
            }
        } else {
            linearWord.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(KEY_WORD, word);
        outState.putInt(KEY_POSITION, position);
        super.onSaveInstanceState(outState);
    }

    private boolean checkFullAnswer() {
        for (TextView option : options_word) {
            if (option.getText().equals("")) {
                return false;
            }
        }
        return true;
    }

    private void checkOptions() {
        StringBuilder builder = new StringBuilder();
        for (TextView v : views_word) {
            builder.append(v.getText());
        }

        Log.i("word", word.getName());
        Log.i("answer", builder.toString());
        if (word.getName().equals(builder.toString())) {
            tvAnswer.setText(getString(R.string.correct));
            tvAnswer.setTextColor(ContextCompat.getColor(getContext(), R.color.green_dark));
            fab.show();
            play();
        } else {
            tvAnswer.setText(getString(R.string.incorrect));
            tvAnswer.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            fab.hide();
        }
        tvAnswer.setVisibility(View.VISIBLE);
    }

    private void play() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(word.getName(), TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            textToSpeech.speak(word.getName(), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void createAlertOption(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.vowels))
                .setCancelable(true);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.vowels));
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((TextView)view).setText(adapter.getItem(which));
                if(checkFullAnswer()){
                    checkOptions();
                }
            }
        }).create().show();
    }

}
