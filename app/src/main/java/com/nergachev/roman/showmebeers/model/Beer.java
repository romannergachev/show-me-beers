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

    private String id;
    private String label;
    private String brewery;
    private String style;
    private String createDate;
    private int page;

    public Beer(){

    }

    public Beer(String id, String name, String label, String brewery, String style
            , String createDate, int page){
        this.id = id;
        this.name = name;
        this.label = label;
        this.brewery = brewery;
        this.style = style;
        this.createDate = createDate;
        this.page = page;
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

    public int getPage() {
        return page;
    }

    public String getId() {
        return id;
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

    public void setPage(int page) {
        this.page = page;
    }

    public void setId(String id) {
        this.id = id;
    }
}
