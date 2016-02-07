package com.nergachev.roman.showmebeers.model;

import com.nergachev.roman.showmebeers.httpclient.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rone on 06/02/16.
 */
public class Beer implements Callback<BreweriesList> {
    String id;
    String name;
    BeerImage labels;
    Brewery brewery;
    BeerStyle style;
    String createDate;

    @Override
    public String toString() {
        return super.toString();
    }

    public String getName() {
        return name;
    }

    public String getBrewery() {
        if(brewery == null){
            return "no data";
        }
        return brewery.getName();
    }

    public String getLabel() {
        if(labels == null){
            return "http://pngimg.com/upload/small/beer_PNG2390.png";
        }
        return labels.getIcon();
    }

    public String getStyle() {
        return style.name;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void loadBrewery(RetrofitClient retrofitClient) {
        Call<BreweriesList> call = retrofitClient.listBreweries(this.id);
        //call.enqueue(this);
    }

    @Override
    public void onResponse(Call<BreweriesList> call, Response<BreweriesList> response) {
        List<Brewery> breweries = response.body().data;
        if(breweries != null && !breweries.isEmpty()){
            this.brewery = breweries.get(0);
        } else {
            this.brewery = new Brewery();
            this.brewery.setName("No Brewery found");
            this.brewery.setId("-1");
        }

    }

    @Override
    public void onFailure(Call<BreweriesList> call, Throwable t) {

    }
}
