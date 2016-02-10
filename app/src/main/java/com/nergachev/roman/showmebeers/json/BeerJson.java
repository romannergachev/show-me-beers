package com.nergachev.roman.showmebeers.json;

import com.nergachev.roman.showmebeers.model.Beer;
import com.nergachev.roman.showmebeers.service.BreweryService;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rone on 06/02/16.
 */
public class BeerJson{
    private String id;
    private String name;
    private String currentPage;
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
            return null;
        }
        return labels.getIcon();
    }

    public String getId() {
        return id;
    }

    public String getStyle() {
        if(style == null){
            return "Unknown Style" ;
        }
        return style.getName();
    }

    public String getCreateDate() {
        if(createDate == null){
            return "Unknown date";
        }
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

    public void setStyle(BeerStyle style) {
        this.style = style;
    }

    public void loadBrewery(BreweryService service, String key) throws IOException {
//        final Call<BreweriesList> call = service.listBreweries(this.id, key);
//                try {
//                    Response<BreweriesList> response = call.execute();
//                    List<Brewery> breweries = response.body().data;
//
//                    if(breweries != null && !breweries.isEmpty()){
//                        brewery = breweries.get(0);
//                    } else {
//                        brewery = new Brewery();
//                        brewery.setName("No Brewery found");
//                        brewery.setId("-1");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
    }
}
