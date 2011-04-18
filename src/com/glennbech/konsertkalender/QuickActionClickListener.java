package com.glennbech.konsertkalender;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import com.glennbech.konsertkalender.parser.VEvent;
import com.glennbech.konsertkalender.quickbar.DeferActionItem;
import com.glennbech.konsertkalender.quickbar.FavoriteAction;
import com.glennbech.konsertkalender.quickbar.GotoExternalAction;
import com.glennbech.konsertkalender.quickbar.QuickAction;

/**
 * @author Glenn Bech
 */
public class QuickActionClickListener implements AdapterView.OnItemClickListener {

    private Context context;
    private BaseAdapter adapter;
    private static String TAG = QuickActionClickListener.class.getName();

    public QuickActionClickListener(Context context, BaseAdapter adapter) {
        if (adapter == null) {
            throw new RuntimeException();
        }
        this.context = context;
        this.adapter = adapter;
    }

    public void onItemClick(AdapterView<?> adapterView, View
            view, int position, long l) {
        Log.d(TAG, "On Item Click");

        VEvent e = (VEvent) adapterView.getItemAtPosition(position);
        QuickAction quickAction = new QuickAction(view);

        final DeferActionItem deferActionItem = new DeferActionItem(context, quickAction);
        final GotoExternalAction gotoExternalAction = new GotoExternalAction(context, quickAction);
        final FavoriteAction favoriteAction = new FavoriteAction(context, quickAction, e, adapter);

        deferActionItem.setArgument(e);
        gotoExternalAction.setArgument(e);
        favoriteAction.setArgument(e);

        quickAction.addActionItem(deferActionItem);
        quickAction.addActionItem(gotoExternalAction);
        quickAction.addActionItem(favoriteAction);

        quickAction.setAnimStyle(QuickAction.ANIM_AUTO);
        quickAction.show();
    }
}
