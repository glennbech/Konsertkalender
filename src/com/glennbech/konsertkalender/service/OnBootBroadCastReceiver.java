package com.glennbech.konsertkalender.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Re enables the service.
 *
 * @author Glenn Bech
 */
public class OnBootBroadCastReceiver extends BroadcastReceiver {

    private static String TAG = OnBootBroadCastReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {

        ComponentName comp = new ComponentName(context.getPackageName(), EventReloadService.class.getName());
        ComponentName service = context.startService(new Intent().setComponent(comp));
        if (null == service) {
            // something really wrong here
            Log.e(TAG, "Could not start service " + comp.toString());
        }
    }


}
