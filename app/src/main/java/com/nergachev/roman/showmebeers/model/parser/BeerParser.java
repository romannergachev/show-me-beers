package com.nergachev.roman.showmebeers.model.parser;

import com.nergachev.roman.showmebeers.model.json.BeerJson;
import com.nergachev.roman.showmebeers.model.json.BeersList;
import com.nergachev.roman.showmebeers.model.Beer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rone on 08/02/16.
 */
public class BeerParser {
    public static List<Beer> parseBeerJsonToBeer(BeersList beers) {

        List<Beer> beersList = new ArrayList<>();
        for (BeerJson beerJson : beers.getBeersList()) {
            Beer beer = new Beer(beerJson.getId(), beerJson.getName(), beerJson.getLabels(), beerJson.getBrewery(),
                    beerJson.getStyle(), beerJson.getCreateDate(), beers.getCurrentPage());
            beersList.add(beer);
        }
        return beersList;
    }
}
