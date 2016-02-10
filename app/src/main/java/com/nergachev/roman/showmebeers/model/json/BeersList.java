package com.nergachev.roman.showmebeers.model.json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rone on 06/02/16.
 */
public class BeersList {
    private int currentPage;
    List<BeerJson> data;
    private List<String> ids;

    public List<String> getIds() {
        if (ids == null) {
            ids = new ArrayList<>();
            for (BeerJson item : data) {
                ids.add(item.getId());
            }
        }
        return ids;
    }

    public List<BeerJson> getBeersList() {
        return data;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
