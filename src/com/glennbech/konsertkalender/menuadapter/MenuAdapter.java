package com.glennbech.konsertkalender.menuadapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.glennbech.konsertkalender.R;
import com.glennbech.konsertkalender.menu.OptionGroup;
import com.glennbech.konsertkalender.menu.Options;


/**
 * @author Glenn Bech
 */
public class MenuAdapter extends SectionedAdapter {

    private Activity context;

    public MenuAdapter(Activity context, Options options) {
        this.context = context;
        for (OptionGroup g : options.getGroups()) {
            addSection(g.getCaption(), new OptionAdapter(context, -1, g.getItems()));
        }
    }

    @Override
    protected View getHeaderView(String caption, int index, View convertView, ViewGroup parent) {
        View view = (View) convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.divider, null);
        }

        TextView tv = (TextView) view.findViewById(R.id.dividertext);
        tv.setText(caption);
        return view;
    }

}
