package com.nergachev.roman.showmebeers.service;

import com.nergachev.roman.showmebeers.model.json.BeersList;
import com.nergachev.roman.showmebeers.model.json.BreweriesList;


import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by rone on 05/02/16.
 */
public interface BreweryService {
    String SERVICE_ENDPOINT = "http://api.brewerydb.com";

    @GET("/v2/beers")
    Observable<BeersList> listBeers(@Query("key") String apiKey, @Query("abv") String abv, @Query("p") int page);

    @GET("/v2/beer/{id}/breweries")
    Observable<BreweriesList> listBreweries(@Path("id") String beerId, @Query("key") String apiKey);
}
