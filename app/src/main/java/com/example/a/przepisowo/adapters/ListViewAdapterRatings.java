package com.example.a.przepisowo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.a.przepisowo.R;
import com.example.a.przepisowo.model.Rating;

import java.util.List;

public class ListViewAdapterRatings extends BaseAdapter {
    private Context mContext;
    private final List<Rating> ratings;

    public ListViewAdapterRatings(Context mContext, List<Rating> ratings) {
        this.mContext = mContext;
        this.ratings = ratings;
    }

    @Override
    public int getCount() {
        return ratings.size();
    }

    @Override
    public Object getItem(int position) {
        return ratings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.rating_row, null);
            TextView nick = (TextView) gridViewAndroid.findViewById(R.id.ratingRowNick);
            RatingBar ratingBar = (RatingBar) gridViewAndroid.findViewById(R.id.ratingRowRatingBar);
            nick.setText(ratings.get(position).getNick());
            ratingBar.setRating(ratings.get(position).getRating());
        } else {
            gridViewAndroid = (View) convertView;
        }
        return gridViewAndroid;
    }
}
