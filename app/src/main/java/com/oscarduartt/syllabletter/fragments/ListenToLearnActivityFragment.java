package com.oscarduartt.syllabletter.fragments;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.oscarduartt.syllabletter.R;
import com.oscarduartt.syllabletter.utilities.Utilities;

import java.util.Arrays;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListenToLearnActivityFragment extends Fragment {

    private TextToSpeech textToSpeech;
    private CardView cvExplanation;
    private CoordinatorLayout clA, clO, clU;
    private TextView tvA, tvE, tvI, tvO, tvU;

    public ListenToLearnActivityFragment newInstance() {
        return new ListenToLearnActivityFragment();
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
        View view = inflater.inflate(R.layout.fragment_listen_to_learn, container, false);

        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    Locale locale = new Locale("spa", "ESP");
                    textToSpeech.setLanguage(locale);
                }
            }
        });

        Spinner spnOptions = (Spinner) view.findViewById(R.id.spn_options_listen_to_learn);
        cvExplanation = (CardView) view.findViewById(R.id.cv_explanation_listen_to_learn);
        clA = (CoordinatorLayout) view.findViewById(R.id.cl_a_listen_to_learn);
        clO = (CoordinatorLayout) view.findViewById(R.id.cl_o_listen_to_learn);
        clU = (CoordinatorLayout) view.findViewById(R.id.cl_u_listen_to_learn);

        tvA = (TextView) view.findViewById(R.id.tv_consonant_vowel_a_listen_to_learn);
        tvE = (TextView) view.findViewById(R.id.tv_consonant_vowel_e_listen_to_learn);
        tvI = (TextView) view.findViewById(R.id.tv_consonant_vowel_i_listen_to_learn);
        tvO = (TextView) view.findViewById(R.id.tv_consonant_vowel_o_listen_to_learn);
        tvU = (TextView) view.findViewById(R.id.tv_consonant_vowel_u_listen_to_learn);

        FloatingActionButton fabA = (FloatingActionButton) view.findViewById(R.id.fab_audio_vowel_a_listen_to_learn);
        FloatingActionButton fabE = (FloatingActionButton) view.findViewById(R.id.fab_audio_vowel_e_listen_to_learn);
        FloatingActionButton fabI = (FloatingActionButton) view.findViewById(R.id.fab_audio_vowel_i_listen_to_learn);
        FloatingActionButton fabO = (FloatingActionButton) view.findViewById(R.id.fab_audio_vowel_o_listen_to_learn);
        FloatingActionButton fabU = (FloatingActionButton) view.findViewById(R.id.fab_audio_vowel_u_listen_to_learn);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNameFile(v);
            }
        };

        fabA.setOnClickListener(onClickListener);
        fabE.setOnClickListener(onClickListener);
        fabI.setOnClickListener(onClickListener);
        fabO.setOnClickListener(onClickListener);
        fabU.setOnClickListener(onClickListener);

        ArrayAdapter<String> adapter_spinner = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Arrays.asList(getResources().getStringArray(R.array.letters)));
        spnOptions.setAdapter(adapter_spinner);
        spnOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cvExplanation.setVisibility(View.GONE);
                clA.setVisibility(View.VISIBLE);
                clO.setVisibility(View.VISIBLE);
                clU.setVisibility(View.VISIBLE);
                if (position == 0) {
                    tvA.setText(getString(R.string.vowel_a));
                    tvE.setText(getString(R.string.vowel_e));
                    tvI.setText(getString(R.string.vowel_i));
                    tvO.setText(getString(R.string.vowel_o));
                    tvU.setText(getString(R.string.vowel_u));
                } else if (parent.getSelectedItem().equals(getString(R.string.consonant_q))) {
                    tvE.setText(getString(R.string.consontant_q_vowel, parent.getSelectedItem(), getString(R.string.vowel_u), getString(R.string.vowel_e)));
                    tvI.setText(getString(R.string.consontant_q_vowel, parent.getSelectedItem(), getString(R.string.vowel_u), getString(R.string.vowel_i)));
                    cvExplanation.setVisibility(View.VISIBLE);
                    clA.setVisibility(View.GONE);
                    clO.setVisibility(View.GONE);
                    clU.setVisibility(View.GONE);
                } else {
                    tvA.setText(getString(R.string.consontant_vowel, parent.getSelectedItem(), getString(R.string.vowel_a)));
                    tvE.setText(getString(R.string.consontant_vowel, parent.getSelectedItem(), getString(R.string.vowel_e)));
                    tvI.setText(getString(R.string.consontant_vowel, parent.getSelectedItem(), getString(R.string.vowel_i)));
                    tvO.setText(getString(R.string.consontant_vowel, parent.getSelectedItem(), getString(R.string.vowel_o)));
                    tvU.setText(getString(R.string.consontant_vowel, parent.getSelectedItem(), getString(R.string.vowel_u)));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void getNameFile(View v) {

        String fileName = "a";

        switch (v.getId()) {
            case R.id.fab_audio_vowel_a_listen_to_learn:
                fileName = tvA.getText().toString();
                break;
            case R.id.fab_audio_vowel_e_listen_to_learn:
                fileName = tvE.getText().toString();
                break;
            case R.id.fab_audio_vowel_i_listen_to_learn:
                fileName = tvI.getText().toString();
                break;
            case R.id.fab_audio_vowel_o_listen_to_learn:
                fileName = tvO.getText().toString();
                break;
            case R.id.fab_audio_vowel_u_listen_to_learn:
                fileName = tvU.getText().toString();
                break;
        }

        Utilities.play(textToSpeech, fileName.toLowerCase());
    }
}
