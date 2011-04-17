package com.glennbech.konsertkalender.quickbar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import com.glennbech.konsertkalender.R;
import com.glennbech.konsertkalender.parser.VEvent;

/**
 * @author Glenn Bech
 */
public class GotoExternalAction extends ActionItem implements View.OnClickListener {

    private Context context;
    private QuickAction quickAction;

    public GotoExternalAction(Context context, QuickAction quickAction) {
        this.context = context;
        this.quickAction = quickAction;
        setIcon(context.getResources().getDrawable(R.drawable.info));
        setOnClickListener(this);
        setTitle("Mer...");
    }

    public void onClick(View v) {

        quickAction.dismiss();
        VEvent clickedEvent = (VEvent) getArgument();
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(clickedEvent.getUrl())));

    }
}