package com.nergachev.roman.showmebeers.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nergachev.roman.showmebeers.R;
import com.nergachev.roman.showmebeers.model.Beer;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;

/**
 * Created by rone on 05/02/16.
 */
public class BeerAdapter extends RecyclerView.Adapter<BeerAdapter.ViewHolder> {
    private List<Beer> mBeersList;
    private Realm realm;
    private Integer pageCount;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mBeerView;
        public ImageView mBeerImage;
        public TextView mBeerName;
        public TextView mBeerBrewery;
        public TextView mBeerStyle;
        public TextView mBeerCreationDate;

        public ViewHolder(View v) {
            super(v);
            mBeerView = v;
            mBeerImage = (ImageView) mBeerView.findViewById(R.id.beer_image);
            mBeerName = (TextView) mBeerView.findViewById(R.id.beer_name);
            mBeerBrewery = (TextView) mBeerView.findViewById(R.id.beer_brewery);
            mBeerStyle = (TextView) mBeerView.findViewById(R.id.beer_style);
            mBeerCreationDate = (TextView) mBeerView.findViewById(R.id.beer_date);
        }
    }

    public BeerAdapter(Realm realm) {
        this.mBeersList = realm.where(Beer.class).findAll();
        this.realm = realm;
        this.pageCount = 1;
    }

    @Override
    public BeerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.beer_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Beer beer = mBeersList.get(position);
        holder.mBeerName.setText(beer.getName());
        holder.mBeerStyle.setText(beer.getStyle());
        if (beer.getBrewery() != null) {
            holder.mBeerBrewery.setText(beer.getBrewery());
        }
        holder.mBeerCreationDate.setText(beer.getCreateDate());
        if (beer.getLabel() != null) {
            Picasso.with(holder.mBeerView.getContext()).load(beer.getLabel()).into(holder.mBeerImage);
        }
    }

    public void increasePageCount() {
        this.pageCount++;
    }

    @Override
    public int getItemCount() {
        if (mBeersList == null) {
            return 0;
        } else {
            return realm.where(Beer.class).lessThan("page", pageCount).findAll().size();
        }

    }

    public Integer getPageCount() {
        return pageCount;
    }
}
