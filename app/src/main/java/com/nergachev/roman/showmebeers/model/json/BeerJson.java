package com.nergachev.roman.showmebeers.model.json;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by rone on 06/02/16.
 */
public class BeerJson {
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String UNKNOWN_STYLE = "Unknown Style";
    private static final String UNKNOWN_DATE = "Unknown date";
    private String id;
    private String name;
    private String currentPage;
    private BeerImage labels;
    private String brewery;
    private BeerStyle style;
    private String createDate;

    public String getName() {
        return name;
    }

    public String getBrewery() {
        return brewery;
    }

    public String getLabels() {
        if (labels == null) {
            return null;
        }
        return labels.getIcon();
    }

    public String getId() {
        return id;
    }

    public String getStyle() {
        if (style == null) {
            return UNKNOWN_STYLE;
        }
        return style.getName();
    }

    public String getCreateDate() {
        if (createDate == null) {
            return UNKNOWN_DATE;
        }
        DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_PATTERN);
        DateTime dt = formatter.parseDateTime(createDate);
        return "" + dt.getDayOfMonth() + " " + dt.monthOfYear().getAsText() + " " + dt.year().getAsText();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStyle(BeerStyle style) {
        this.style = style;
    }

    public void setBrewery(String brewery) {
        this.brewery = brewery;
    }
}
