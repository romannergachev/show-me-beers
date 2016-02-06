package com.nergachev.roman.showmebeers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by rone on 05/02/16.
 */
public class BeerAdapter extends RecyclerView.Adapter<BeerAdapter.ViewHolder> {
    private String[] mDataset;

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
    public BeerAdapter(String[] myDataset) {
        mDataset = myDataset;
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
        holder.mBeerName.setText(mDataset[position]);
        //holder.mBeerBrewery.setText(mDataset[position]);
        //holder.mBeerStyle.setText(mDataset[position]);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
