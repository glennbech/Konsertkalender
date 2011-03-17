package com.glennbech.konsertkalender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Glenn Bech
 */
public class EventSectionedAdapter extends SectionedAdapter {

    private final Context context;

    public EventSectionedAdapter(Context context) {
        this.context = context;
    }

    protected View getHeaderView(String caption, int index, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.divider, null);
        }
        TextView tv = (TextView) view.findViewById(R.id.dividertext);
        tv.setText(caption);
        return view;
    }
}
