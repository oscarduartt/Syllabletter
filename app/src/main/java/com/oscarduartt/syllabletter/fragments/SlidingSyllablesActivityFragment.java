package com.oscarduartt.syllabletter.fragments;

import android.content.ClipData;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oscarduartt.syllabletter.R;
import com.oscarduartt.syllabletter.objects.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * A placeholder fragment containing a simple view.
 */
public class SlidingSyllablesActivityFragment extends Fragment implements View.OnDragListener {

    private int position;
    private List<Word> words;
    private FloatingActionButton fab;
    private LinearLayout linearWord, linearOptions;
    private TextView tvAnswer;
    private Handler handler = new Handler();
    private String[] syllables;
    private ArrayList<LinearLayout> container_answers = new ArrayList<>();
    private ArrayList<Integer> used_view_randoms;
    private ArrayList<View> view_answers = new ArrayList<>();

    public SlidingSyllablesActivityFragment newInstance(int position) {
        SlidingSyllablesActivityFragment fragment = new SlidingSyllablesActivityFragment();
        fragment.position = position;
        Word[] objects = new Word[]{new Word("CONEJO", R.mipmap.conejo, "CO,NE,JO"),
                new Word("GALLINA", R.mipmap.gallina, "GA,LLI,NA"),
                new Word("GATO", R.mipmap.gato, "GA,TO"),
                new Word("GUSANO", R.mipmap.gusano, "GU,SA,NO"),
                new Word("PERRO", R.mipmap.perro, "PE,RRO"),
                new Word("TORTUGA", R.mipmap.tortuga, "TOR,TU,GA")};
        fragment.words = Arrays.asList(objects);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sliding_syllables, container, false);
        ImageView image = (ImageView) view.findViewById(R.id.img_sliding_syllables);

        int aux = 0;
        while (position == aux) {
            aux = new Random().nextInt(words.size());
        }
        position = aux;

        image.setImageResource(words.get(position).getImage());

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.show();
            }
        });
        fab = (FloatingActionButton) view.findViewById(R.id.fab_sliding_syllables);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.hide();
                getFragmentManager().beginTransaction().replace(R.id.game_container, new SlidingSyllablesActivityFragment().newInstance(position)).commit();
            }
        });

        linearWord = (LinearLayout) view.findViewById(R.id.ly_word_sliding_syllables);
        linearOptions = (LinearLayout) view.findViewById(R.id.ly_options_sliding_syllables);

        syllables = words.get(position).getSyllables().split(",");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (String syllable : syllables) {

                    final View view_item_container_syllable = LayoutInflater.from(getContext()).inflate(R.layout.item_container_syllable, null);
                    final LinearLayout linearContainerSyllable = (LinearLayout) view_item_container_syllable.findViewById(R.id.ly_container_syllable);

                    linearContainerSyllable.setTag("");

                    final View view_item_container_syllable_option = LayoutInflater.from(getContext()).inflate(R.layout.item_container_syllable_option, null);
                    final LinearLayout linearContainerSyllableOption = (LinearLayout) view_item_container_syllable_option.findViewById(R.id.ly_container_syllable_option);

                    final TextView tvSyllableOption = (TextView) view_item_container_syllable_option.findViewById(R.id.tv_syllable_option);
                    tvSyllableOption.setText(syllable);
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

                    linearContainerSyllable.setOnDragListener(SlidingSyllablesActivityFragment.this);
                    linearContainerSyllableOption.setOnDragListener(SlidingSyllablesActivityFragment.this);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            container_answers.add(linearContainerSyllable);
                            linearWord.addView(view_item_container_syllable);
                            view_answers.add(view_item_container_syllable_option);

                            if (view_answers.size() == syllables.length) {
                                used_view_randoms = new ArrayList<>();
                                for (int i = 0; i < view_answers.size(); i++) {
                                    int new_random = getRandom(view_answers.size(), used_view_randoms);
                                    linearOptions.addView(view_answers.get(new_random));
                                    used_view_randoms.add(new_random);
                                }
                            }

                            //linearOptions.addView(view_item_container_syllable_option);
                        }
                    });
                }
            }
        });
        thread.start();

        tvAnswer = (TextView) view.findViewById(R.id.tv_answer_sliding_syllables);

        return view;
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
                    if (container_answers.size() == syllables.length) {
                        if (checkFullAnswers()) {
                            checkAnswer();
                        } else {
                            fab.hide();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tvAnswer.setVisibility(View.GONE);
                                }
                            });
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
        StringBuilder builder = new StringBuilder();
        for (LinearLayout linear : container_answers) {
            builder.append(linear.getTag().toString());
        }

        if (words.get(position).getName().equals(builder.toString())) {
            tvAnswer.setText(getString(R.string.correct));
            fab.show();
        } else {
            tvAnswer.setText(getString(R.string.incorrect));
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                tvAnswer.setVisibility(View.VISIBLE);
            }
        });
    }


    public int getRandom(int max_number, ArrayList<Integer> usadas) {
        int new_random = new Random().nextInt(max_number);
        if (usadas.size() > 0) {
            for (Integer usado : usadas) {
                if (new_random == usado) {
                    new_random = getRandom(max_number, usadas);
                }
            }
        }
        return new_random;
    }
}
