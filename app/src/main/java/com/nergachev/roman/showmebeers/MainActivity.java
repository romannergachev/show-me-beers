package com.nergachev.roman.showmebeers;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nergachev.roman.showmebeers.service.BreweryService;
import com.nergachev.roman.showmebeers.service.ServiceFactory;
import com.nergachev.roman.showmebeers.model.Beer;
import com.nergachev.roman.showmebeers.json.BeersList;
import com.nergachev.roman.showmebeers.model.parser.BeerParser;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity{
    private static final String apiKey = "b472a4a7277a9944ae2af0732ebaf395";
    private static final String abv = "-10";


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private RealmResults<Beer> beersList;
    private Realm realm;
    private BreweryService service;
    private int currentPage;
    private Subscriber<BeersList> subscriber;

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
        realm = Realm.getInstance(getApplicationContext());

        beersList = realm.where(Beer.class).findAll();
        currentPage = 1;
        mAdapter = new BeerAdapter(beersList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        configBeerAPI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                service.listBeers(apiKey, abv, currentPage)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<BeersList>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(BeersList beersList) {
                                List<Beer> beerList = BeerParser.parseBeerJsonToBeer(beersList.getBeersList());
                                realm.beginTransaction();
                                realm.copyToRealmOrUpdate(beerList);
                                realm.commitTransaction();
                                currentPage++;
                                mAdapter.notifyDataSetChanged();
                            }
                        });

            }
        });
    }

    private void configBeerAPI(){
        subscriber = new Subscriber<BeersList>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BeersList beersList) {
                List<Beer> beerList = BeerParser.parseBeerJsonToBeer(beersList.getBeersList());
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(beerList);
                realm.commitTransaction();
                currentPage++;
                mAdapter.notifyDataSetChanged();
            }
        };



        service = ServiceFactory.createRetrofitService(BreweryService.class, BreweryService.SERVICE_ENDPOINT);
        makeBeerCall();
    }

    private void makeBeerCall(){
        service.listBeers(apiKey, abv, currentPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
