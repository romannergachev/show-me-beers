package com.nergachev.roman.showmebeers;

/**
 * Created by rone on 06/02/16.
 */
public class Beer {
    String name;
    String brewery;
    String style;
    String date ;

    @Override
    public String toString() {
        return super.toString();
    }

    public String getName() {
        return name;
    }

    public String getBrewery() {
        return brewery;
    }

    public String getStyle() {
        return style;
    }

    public String getDate() {
        return date;
    }
}
