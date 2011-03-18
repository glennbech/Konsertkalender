package com.glennbech.konsertkalender.service;

import android.content.*;
import android.os.IBinder;
import android.util.Log;
import com.glennbech.konsertkalender.MainActivity;

/**
 * Re enables the service.
 *
 * @author Glenn Bech
 */
public class OnBootBroadCastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        context.bindService(new Intent(context, EventReloadService.class), onServiceConntection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Callback for the service.
     */
    public final ServiceConnection onServiceConntection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            EventReloadService reloadService = ((EventReloadService.LocalBinder) iBinder).getService();

            Log.d(MainActivity.class.getName(), "Service connected.");
        }

        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(MainActivity.class.getName(), "Service disconnected.");
        }
    };


}
