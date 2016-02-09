package com.nergachev.roman.showmebeers.json;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rone on 06/02/16.
 */
public class BeerJson implements Callback<BreweriesList> {
    private String id;
    private String name;
    private BeerImage labels;
    private Brewery brewery;
    private BeerStyle style;
    private String createDate;

    public String getName() {
        return name;
    }

    public String getBrewery() {
        if(brewery == null){
            return "none";
        }
        return brewery.getName();
    }

    public String getLabels() {
        if(labels == null){
            return "http://pngimg.com/upload/small/beer_PNG2390.png";
        }
        return labels.getIcon();
    }

    public String getId() {
        return id;
    }

    public BeerStyle getStyle() {
        return style;
    }

    public String getCreateDate() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dt = formatter.parseDateTime(createDate);
        return "" + dt.getDayOfMonth() + " " + dt.monthOfYear().getAsText() + " " + dt.year().getAsText();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLabels(BeerImage labels) {
        this.labels = labels;
    }

    public void setBrewery(Brewery brewery) {
        this.brewery = brewery;
    }

    public void setStyle(BeerStyle style) {
        this.style = style;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

//        public void loadBrewery(RetrofitClient retrofitClient) {
//        Call<BreweriesList> call = retrofitClient.listBreweries(this.id);
//        //call.enqueue(this);
//    }

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
