package com.example.a.przepisowo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a.przepisowo.R;

import java.util.List;

public class ListViewAdapterPrzepisyPrezentacja extends BaseAdapter {

    private Context mContext;
    private final List<String> gridViewString;
    private final int[] gridViewImageId;

    public ListViewAdapterPrzepisyPrezentacja(Context mContext, List<String> gridViewString, int[] gridViewImageId) {
        this.mContext = mContext;
        this.gridViewString = gridViewString;
        this.gridViewImageId = gridViewImageId;
    }

    @Override
    public int getCount() {
        return gridViewString.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.table_row_przepisy_prezentacja_layout, null);
            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.tableRowTv1);
            TextView textViewAndroid2 = (TextView) gridViewAndroid.findViewById(R.id.tableRowTv2);
            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.tableRowIv);

            imageViewAndroid.setScaleType(ImageView.ScaleType.CENTER_CROP);

            textViewAndroid.setText(gridViewString.get(i));
            imageViewAndroid.setImageResource(gridViewImageId[i]);
        } else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }
}
