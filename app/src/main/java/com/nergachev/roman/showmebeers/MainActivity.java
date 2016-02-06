package com.nergachev.roman.showmebeers;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;

import com.squareup.moshi.Moshi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.MoshiConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends Activity implements Callback<BeersList> {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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
        mAdapter = new BeerAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        configureBeerAPI();
    }

    private void configureBeerAPI(){
        String apiKey = "920c6d2232172e28c4854416d53fc530";
        String abv = "-10";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.brewerydb.com")
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        BreweryDbAPI breweryDbAPI = retrofit.create(BreweryDbAPI.class);

        Call<BeersList> call = breweryDbAPI.listBeers(apiKey, abv);
        //asynchronous call
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<BeersList> call, Response<BeersList> response) {
    int i=0;
        i++;
    }

    @Override
    public void onFailure(Call<BeersList> call, Throwable t) {

    }
}
