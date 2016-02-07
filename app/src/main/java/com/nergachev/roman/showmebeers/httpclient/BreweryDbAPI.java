package com.nergachev.roman.showmebeers.httpclient;

import com.nergachev.roman.showmebeers.model.BeersList;
import com.nergachev.roman.showmebeers.model.BreweriesList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by rone on 05/02/16.
 */
public interface BreweryDbAPI {
    @GET("/v2/beers")
    Call<BeersList> listBeers(@Query("key") String apiKey, @Query("abv") String abv);

    @GET("/v2/beer/{id}/breweries")
    Call<BreweriesList> listBreweries(@Path("id") String beerId, @Query("key") String apiKey);
}
