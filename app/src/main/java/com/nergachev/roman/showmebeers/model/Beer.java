package com.nergachev.roman.showmebeers.model;

import com.nergachev.roman.showmebeers.json.BeerImage;
import com.nergachev.roman.showmebeers.json.BeerStyle;
import com.nergachev.roman.showmebeers.json.Brewery;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rone on 06/02/16.
 */
public class Beer extends RealmObject {
    @PrimaryKey
    private String name;

    private String label;
    private String brewery;
    private String style;
    private String createDate;

    public Beer(){

    }

    public Beer(String name, String label, String brewery, String style, String createDate){
        this.name = name;
        this.label = label;
        this.brewery = brewery;
        this.style = style;
        this.createDate = createDate;
    }

    public String getName() {
        return name;
    }

    public String getBrewery() {
        return brewery;
    }

    public String getLabel() {
        return label;
    }

    public String getStyle() {
        return style;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setBrewery(String brewery) {
        this.brewery = brewery;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
