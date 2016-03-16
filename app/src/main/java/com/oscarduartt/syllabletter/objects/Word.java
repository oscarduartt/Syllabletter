package com.oscarduartt.syllabletter.objects;

import java.io.Serializable;

/**
 * Created by oilopez on 14/03/2016.
 */
public class Word implements Serializable {
    private String name;
    private int image;
    private String syllables;

    public Word(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public Word(String name, int image, String syllables) {
        this.name = name;
        this.image = image;
        this.syllables = syllables;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public String getSyllables() {
        return syllables;
    }
}
