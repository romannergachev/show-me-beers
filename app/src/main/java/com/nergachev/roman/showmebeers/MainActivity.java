package com.nergachev.roman.showmebeers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.nergachev.roman.showmebeers.adapter.BeerAdapter;
import com.nergachev.roman.showmebeers.adapter.decoration.DividerItemDecoration;
import com.nergachev.roman.showmebeers.model.json.BreweriesList;
import com.nergachev.roman.showmebeers.service.BreweryService;
import com.nergachev.roman.showmebeers.service.ServiceFactory;
import com.nergachev.roman.showmebeers.model.Beer;
import com.nergachev.roman.showmebeers.model.json.BeersList;
import com.nergachev.roman.showmebeers.model.parser.BeerParser;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.List;

import io.realm.Realm;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity {
    private static final String apiKey = "a0985db0b48c730a55d174a10697c816";
    private static final String abv = "-10";

    private RecyclerView mRecyclerView;
    private BeerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private Realm realm;
    private BreweryService service;
    private ConnectivityManager connectivityManager;
    private SwipyRefreshLayout mSwipyRefreshLayout;
    private List<Beer> downloadedBeersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.beer_recycler_view);
        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL_LIST));

        realm = Realm.getInstance(getApplicationContext());
        connectivityManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        mAdapter = new BeerAdapter(realm);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkPermissions();
        service = ServiceFactory.createRetrofitService(BreweryService.class, BreweryService.SERVICE_ENDPOINT);
        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                makeBeerCall();
            }
        });
        makeBeerCall();
    }

    private void makeBeerCall() {
        if (checkNetworkState()) {
            service.listBeers(apiKey, abv, mAdapter.getPageCount())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getBeerSubscriber());
        } else {
            mSwipyRefreshLayout.setRefreshing(false);
            if (realm.where(Beer.class).equalTo("page", mAdapter.getPageCount()).findAll().size() > 0) {
                mAdapter.increasePageCount();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private void errorOccurred(Throwable e) {
        mSwipyRefreshLayout.setRefreshing(false);
        mAdapter.notifyDataSetChanged();
        Log.d("NETWORKING", e.getMessage());
    }

    private boolean checkNetworkState() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork == null) {
            return false;
        }
        boolean isConnected = activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                    1);
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.INTERNET},
                    1);
        }
    }

    private Subscriber<BeersList> getBeerSubscriber() {
        Subscriber<BeersList> subscriber = new Subscriber<BeersList>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mAdapter.increasePageCount();
                errorOccurred(e);
            }

            @Override
            public void onNext(BeersList beers) {
                mSwipyRefreshLayout.setRefreshing(false);
                downloadedBeersList = BeerParser.parseBeerJsonToBeer(beers);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(downloadedBeersList);
                realm.commitTransaction();
                mAdapter.increasePageCount();
                mAdapter.notifyDataSetChanged();

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
                errorOccurred(e);
            }

            @Override
            public void onNext(final BreweriesList breweriesList) {
                if (breweriesList != null && breweriesList.getData() != null
                        && breweriesList.getData().size() > 0) {
                    for (Beer beer : downloadedBeersList) {
                        if (beer.getId().equals(id)) {
                            beer.setBrewery(breweriesList.getData().get(0).getName());
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(beer);
                            realm.commitTransaction();
                            break;
                        }
                    }
                }
            }
        };
        return subscriber;
    }
}
