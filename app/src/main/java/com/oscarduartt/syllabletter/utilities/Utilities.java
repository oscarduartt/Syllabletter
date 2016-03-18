package com.oscarduartt.syllabletter.utilities;

import android.os.Build;
import android.speech.tts.TextToSpeech;

import com.oscarduartt.syllabletter.R;
import com.oscarduartt.syllabletter.objects.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by oilopez on 18/03/2016.
 */
public class Utilities {

    public static final String KEY_WORD = "key_word";
    public static final String KEY_POSITION = "key_position";

    public static void play(TextToSpeech textToSpeech, String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public static List<Word> getElements() {
        Word[] objects = new Word[]{new Word("CONEJO", R.mipmap.conejo, "CO,NE,JO"),
                new Word("GALLINA", R.mipmap.gallina, "GA,LLI,NA"),
                new Word("GATO", R.mipmap.gato, "GA,TO"),
                new Word("PERRO", R.mipmap.perro, "PE,RRO"),
                new Word("JIRAFA", R.mipmap.jirafa, "JI,RA,FA"),
                new Word("BURRO", R.mipmap.burro, "BU,RRO"),
                new Word("CEBRA", R.mipmap.cebra, "CE,BRA"),
                new Word("CHANGO", R.mipmap.chango, "CHAN,GO"),
                new Word("ABEJA", R.mipmap.abeja, "A,BE,JA"),
                new Word("MAPACHE", R.mipmap.mapache, "MA,PA,CHE"),
                new Word("TORTUGA", R.mipmap.tortuga, "TOR,TU,GA")};
        return Arrays.asList(objects);
    }

    public static int getRandom(int max_number, ArrayList<Integer> numbers_used) {
        int new_random = new Random().nextInt(max_number);
        if (numbers_used.size() > 0) {
            for (Integer used : numbers_used) {
                if (new_random == used) {
                    new_random = getRandom(max_number, numbers_used);
                }
            }
        }
        return new_random;
    }
}
