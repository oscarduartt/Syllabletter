package com.oscarduartt.syllabletter.objects;

import java.io.Serializable;

/**
 * Created by oilopez on 14/03/2016.
 */
public class Word implements Serializable {
    private String name;
    private int image;

    public Word(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }
}
