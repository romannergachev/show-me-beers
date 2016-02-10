package com.nergachev.roman.showmebeers;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.nergachev.roman.showmebeers.adapter.BeerAdapter;
import com.nergachev.roman.showmebeers.adapter.decoration.DividerItemDecoration;
import com.nergachev.roman.showmebeers.json.BreweriesList;
import com.nergachev.roman.showmebeers.service.BreweryService;
import com.nergachev.roman.showmebeers.service.ServiceFactory;
import com.nergachev.roman.showmebeers.model.Beer;
import com.nergachev.roman.showmebeers.json.BeersList;
import com.nergachev.roman.showmebeers.model.parser.BeerParser;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.List;

import io.realm.Realm;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity{
    private static final String apiKey = "1455fc3ce46786a1a2e1c7996e8985c3";
    private static final String abv = "-10";


    private RecyclerView mRecyclerView;
    private BeerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private Realm realm;
    private BreweryService service;
    private ConnectivityManager connectivityManager;
    private SwipyRefreshLayout mSwipyRefreshLayout;
    private List<Beer> downloadedBeersList;
    private volatile int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.beer_recycler_view);
        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL_LIST));

        realm = Realm.getInstance(getApplicationContext());
        connectivityManager = (ConnectivityManager)getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        mAdapter = new BeerAdapter(realm);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        service = ServiceFactory.createRetrofitService(BreweryService.class, BreweryService.SERVICE_ENDPOINT);
        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                makeBeerCall();
            }
        });
        makeBeerCall();
    }

    private void makeBeerCall(){
        if(checkNetworkState()){
            service.listBeers(apiKey, abv, mAdapter.getPageCount())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getSubscriber());
        } else {
            mSwipyRefreshLayout.setRefreshing(false);
            if(realm.where(Beer.class).equalTo("page", mAdapter.getPageCount()).findAll().size() > 0) {
                mAdapter.increasePageCount();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private boolean checkNetworkState(){
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if(activeNetwork == null){
            return false;
        }
        boolean isConnected = activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private Subscriber<BeersList> getSubscriber() {
        Subscriber<BeersList> subscriber = new Subscriber<BeersList>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mSwipyRefreshLayout.setRefreshing(false);
                mAdapter.increasePageCount();
                mAdapter.notifyDataSetChanged();
                Log.d("NETWORKING", e.getMessage());
            }

            @Override
            public void onNext(BeersList beers) {
                mSwipyRefreshLayout.setRefreshing(false);
                //beers.loadBreweries(service, apiKey);
                downloadedBeersList = BeerParser.parseBeerJsonToBeer(beers);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(downloadedBeersList);
                realm.commitTransaction();
                mAdapter.increasePageCount();
                mAdapter.notifyDataSetChanged();

                counter = beers.getIds().size();
                for (int i = 0; i < beers.getIds().size(); i++) {
                    String id = beers.getIds().get(i);
                    service.listBreweries(id, apiKey)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(getBreweriesSubscriber(id));
                }
            }
        };
        return subscriber;
    }

    private Subscriber<BreweriesList> getBreweriesSubscriber(final String id) {
        Subscriber<BreweriesList> subscriber = new Subscriber<BreweriesList>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mSwipyRefreshLayout.setRefreshing(false);
                mAdapter.notifyDataSetChanged();
                Log.d("NETWORKING", e.getMessage());
            }

            @Override
            public void onNext(final BreweriesList breweriesList) {
                counter--;
                if(breweriesList != null && breweriesList.getData() != null
                        && breweriesList.getData().size() > 0){
                    for (Beer beer:downloadedBeersList){
                        if(beer.getId().equals(id)){
                            beer.setBrewery(breweriesList.getData().get(0).getName());
                            //mAdapter.notifyDataSetChanged();
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(beer);
                            realm.commitTransaction();
                            break;
                        }
                    }
//                    if(counter == 0){
//                        realm.beginTransaction();
//                        realm.copyToRealmOrUpdate(downloadedBeersList);
//                        realm.commitTransaction();
//                    }

//
//
//
//
//                    Beer beer = realm.where(Beer.class).equalTo("id", id).findFirst();
//                    if(beer != null){
//                        beer.setBrewery(breweriesList.getData().get(0).getName());
//                        realm.beginTransaction();
//                        realm.copyToRealmOrUpdate(beer);
//                        realm.commitTransaction();
//                    }
                }
            }
        };
        return subscriber;
    }
}
