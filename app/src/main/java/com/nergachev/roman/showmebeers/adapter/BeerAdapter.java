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
import io.realm.RealmResults;

/**
 * Created by rone on 05/02/16.
 */
public class BeerAdapter extends RecyclerView.Adapter<BeerAdapter.ViewHolder> {
    private List<Beer> mBeersList;
    private Realm realm;
    private Integer pageCount;

    public List<Beer> getData() {
        return mBeersList;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
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

    // Provide a suitable constructor (depends on the kind of dataset)
    public BeerAdapter(Realm realm) {
        this.mBeersList = realm.where(Beer.class).findAll();
        this.realm = realm;
        this.pageCount = 1;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BeerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.beer_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Beer beer = mBeersList.get(position);
        holder.mBeerName.setText(beer.getName());
        holder.mBeerStyle.setText(beer.getStyle());
        holder.mBeerBrewery.setText(beer.getBrewery());
        holder.mBeerCreationDate.setText(beer.getCreateDate());
        if(beer.getLabel() != null){
            Picasso.with(holder.mBeerView.getContext()).load(beer.getLabel()).into(holder.mBeerImage);
        }


        //holder.mBeerImage.set


    }

    public void increasePageCount() {
        this.pageCount++;
    }

    public void updateDataSet(RealmResults<Beer> beers){
        this.mBeersList.clear();
        this.mBeersList.addAll(beers);
        notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(mBeersList == null){
            return 0;
        } else {
            return realm.where(Beer.class).lessThan("page", pageCount).findAll().size();
        }

    }

    public Integer getPageCount() {
        return pageCount;
    }
}
