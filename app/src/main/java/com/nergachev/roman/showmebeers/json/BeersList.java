package com.nergachev.roman.showmebeers.json;

import java.util.List;

/**
 * Created by rone on 06/02/16.
 */
public class BeersList{
    List<BeerJson> data;

//    public void loadBreweries(RetrofitClient retrofitClient){
////        for (Beer item: data) {
////            item.loadBrewery(retrofitClient);
////        }
//    }

    public List<BeerJson> getBeersList() {
        return data;
    }
}