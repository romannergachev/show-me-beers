package com.nergachev.roman.showmebeers.json;

import android.util.Log;

import com.nergachev.roman.showmebeers.service.BreweryService;

import java.io.IOException;
import java.util.List;

/**
 * Created by rone on 06/02/16.
 */
public class BeersList{
    private int currentPage;
    List<BeerJson> data;

    public void loadBreweries(BreweryService service, String key){
        for (BeerJson item: data) {
            try {
                item.loadBrewery(service, key);
            } catch (IOException e) {
                Log.d("LOADING_BREWERIES", "loading failed!");
            }
        }
    }

    public List<BeerJson> getBeersList() {
        return data;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
