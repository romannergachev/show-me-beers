package com.nergachev.roman.showmebeers;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nergachev.roman.showmebeers.adapter.BeerAdapter;
import com.nergachev.roman.showmebeers.adapter.decoration.DividerItemDecoration;
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
    private static final String apiKey = "e336e24da1f407346460595d9a7144d0";
    private static final String abv = "-10";


    private RecyclerView mRecyclerView;
    private BeerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private Realm realm;
    private BreweryService service;
    private ConnectivityManager connectivityManager;
    private SwipyRefreshLayout mSwipyRefreshLayout;

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
            if(realm.where(Beer.class).equalTo("page", mAdapter.getPageCount() + 1).findAll().size() > 0) {
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
            }

            @Override
            public void onNext(BeersList beers) {
                mSwipyRefreshLayout.setRefreshing(false);
                beers.loadBreweries(service, apiKey);
                List<Beer> beerList = BeerParser.parseBeerJsonToBeer(beers);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(beerList);
                realm.commitTransaction();
                mAdapter.increasePageCount();
                mAdapter.notifyDataSetChanged();
            }
        };
        return subscriber;
    }
}
