package com.nergachev.roman.showmebeers.model.parser;

import com.nergachev.roman.showmebeers.json.BeerJson;
import com.nergachev.roman.showmebeers.model.Beer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rone on 08/02/16.
 */
public class BeerParser {
    public static List<Beer> parseBeerJsonToBeer(List<BeerJson> beers){
        List<Beer> beersList = new ArrayList<>();
        for (BeerJson beerJson: beers) {
            Beer beer = new Beer(beerJson.getName(), beerJson.getLabels(), beerJson.getBrewery(),
                    beerJson.getStyle().getName(), beerJson.getCreateDate());
            beersList.add(beer);
        }
        return beersList;
    }
}
