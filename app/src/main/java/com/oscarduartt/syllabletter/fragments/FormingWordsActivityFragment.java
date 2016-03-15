package com.oscarduartt.syllabletter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oscarduartt.syllabletter.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class FormingWordsActivityFragment extends Fragment {

    public FormingWordsActivityFragment newInstance() {
        return new FormingWordsActivityFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forming_words, container, false);
    }
}
