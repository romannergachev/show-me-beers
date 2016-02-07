package com.nergachev.roman.showmebeers.model;

import com.nergachev.roman.showmebeers.httpclient.RetrofitClient;
import com.nergachev.roman.showmebeers.model.Beer;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rone on 06/02/16.
 */
public class BeersList {
    List<Beer> data;

    public void loadBreweries(RetrofitClient retrofitClient){
        for (Beer item: data) {
            item.loadBrewery(retrofitClient);
        }
    }

    public List<Beer> getBeersList() {
        return data;
    }
}
