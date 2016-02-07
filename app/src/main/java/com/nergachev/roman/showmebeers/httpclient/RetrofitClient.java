package com.nergachev.roman.showmebeers.httpclient;

import com.nergachev.roman.showmebeers.model.BeersList;
import com.nergachev.roman.showmebeers.model.BreweriesList;

import retrofit2.Call;
import retrofit2.MoshiConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by rone on 06/02/16.
 */
public class RetrofitClient {

    private String apiKey;
    private String abv;
    private String baseUrl;
    private Retrofit retrofit;
    private BreweryDbAPI breweryDbAPI;

    private RetrofitClient() {

    }

    private void configureRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getAbv() {
        return abv;
    }

    public Call<BeersList> listBeers(){
        return breweryDbAPI.listBeers(apiKey, abv);
    }

    public Call<BreweriesList> listBreweries(String beerId){
        return breweryDbAPI.listBreweries(beerId, apiKey);
    }

    public static Builder newBuilder() {
        return new RetrofitClient().new Builder();
    }

    public class Builder {

        private Builder() {
            //setApiKey("920c6d2232172e28c4854416d53fc530");
            setApiKey("b472a4a7277a9944ae2af0732ebaf395");
            setAbv("-10");
            setBaseUrl("http://api.brewerydb.com");
        }

        public Builder setApiKey(String apiKey) {
            RetrofitClient.this.apiKey = apiKey;
            return this;
        }

        public Builder setAbv(String abv) {
            RetrofitClient.this.abv = abv;
            return this;
        }

        public Builder setBaseUrl(String baseUrl){
            RetrofitClient.this.baseUrl = baseUrl;
            return this;
        }

        public RetrofitClient build() {
            RetrofitClient.this.configureRetrofit();
            RetrofitClient.this.breweryDbAPI = RetrofitClient.this.
                    retrofit.create(BreweryDbAPI.class);
            return RetrofitClient.this;
        }

    }

}