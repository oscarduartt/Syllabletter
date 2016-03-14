package com.oscarduartt.syllabletter.objects;

import java.io.Serializable;

/**
 * Created by oilopez on 14/03/2016.
 */
public class Game implements Serializable {
    private String title;
    private int color;

    public Game(String title, int color) {
        this.title = title;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public int getColor() {
        return color;
    }
}
