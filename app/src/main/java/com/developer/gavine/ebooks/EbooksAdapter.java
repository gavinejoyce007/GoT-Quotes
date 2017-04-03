package com.developer.gavine.ebooks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class EbooksAdapter extends ArrayAdapter<Ebooks> {

    public EbooksAdapter(Context context, List<Ebooks> ebooks) {
        super(context, 0 , ebooks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.ebooks_list, parent, false);
        }

        Ebooks currentEarthquake = getItem(position);
        TextView bib_key = (TextView) listItemView.findViewById(R.id.quote);
        bib_key.setText(currentEarthquake.getBib_key());

        TextView preview = (TextView) listItemView.findViewById(R.id.character);
        String sym = "-"+currentEarthquake.getPreview();
        preview.setText(sym);


        return listItemView;
    }
}

