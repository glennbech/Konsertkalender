package com.glennbech.events;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;

import static com.glennbech.events.TimerConsts.SECOND;

/**
 * @author Glenn Bech
 */

public class MonitorEventListService extends Service {

    private IBinder binder = new LocalBinder();
    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(MonitorEventListService.class.getName(), "onCreate");
        timer = new Timer(true);
        timer.schedule(new ReloadDatabaseTask(this), 60 * SECOND , 12 * 60 * SECOND * 60);
        Log.d(MonitorEventListService.class.getName(), "Timer task scheduled");
    }

    public void reloadDatabase() {
        ReloadDatabaseTask t = new ReloadDatabaseTask(this);
        t.run();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        timer.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        MonitorEventListService getService() {
            return (MonitorEventListService.this);
        }
    }

}
