package com.glennbech.konsertkalender.quickbar;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import com.glennbech.konsertkalender.R;
import com.glennbech.konsertkalender.parser.VEvent;
import com.glennbech.konsertkalender.persistence.SQLiteEventStore;


/**
 * @author Glenn Bech
 */
public class FavoriteAction extends ActionItem implements View.OnClickListener {

    private Context context;
    private QuickAction quickAction;
    private BaseAdapter adapter;
    private VEvent event;

    public FavoriteAction(Context context, QuickAction quickAction, VEvent event, BaseAdapter adapter) {
        if (adapter == null) {
            throw new RuntimeException();
        }
        this.context = context;
        this.quickAction = quickAction;
        this.adapter = adapter;
        this.event = event;


        if (event.isFavorite()) {
            setIcon(context.getResources().getDrawable(R.drawable.starunchecked));
            setTitle("Favoritt av");
        } else {
            setIcon(context.getResources().getDrawable(R.drawable.starchecked));
            setTitle("Favoritt");
        }


        setOnClickListener(this);
    }

    public void onClick(View v) {
        SQLiteEventStore store = new SQLiteEventStore(context);
        if (event.isFavorite()) {
            store.setFavorite(event.getUid(), event.getStartDate(), false);
            event.setFavorite(false);
        } else {
            store.setFavorite(event.getUid(), event.getStartDate(), true);
            event.setFavorite(true);
        }
        adapter.notifyDataSetChanged();
        quickAction.dismiss();
    }

}