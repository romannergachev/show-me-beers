package com.nergachev.roman.showmebeers;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nergachev.roman.showmebeers.httpclient.BreweryDbAPI;
import com.nergachev.roman.showmebeers.httpclient.RetrofitClient;
import com.nergachev.roman.showmebeers.model.Beer;
import com.nergachev.roman.showmebeers.model.BeersList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.MoshiConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends Activity implements Callback<BeersList> {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RetrofitClient retrofitClient;
    private List<Beer> beersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.beer_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        int i = 0;
        String[] myDataset = new String[150];
        for (int j = 0;j<myDataset.length;j++){
            myDataset[j] = "abc  " + i;
            i++;
        }
        beersList = new ArrayList<>();
        mAdapter = new BeerAdapter(beersList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        configureBeerAPI();
    }

    private void configureBeerAPI(){
        retrofitClient = RetrofitClient.newBuilder().build();

        Call<BeersList> call = retrofitClient.listBeers();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<BeersList> call, Response<BeersList> response) {
        response.body().loadBreweries(retrofitClient);
        beersList = response.body().getBeersList();
        //mRecyclerView.invalidate();
        mAdapter = new BeerAdapter(beersList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Call<BeersList> call, Throwable t) {

    }
}
