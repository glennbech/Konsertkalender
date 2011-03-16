package com.glennbech.events.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;


/**
 *
 * Service that schedules the Database Reload task.
 */
public class EventReloadService extends Service {

    private static final int SECOND = 1000 ;
    private static final int MINUTE = 60 * SECOND;
    private final IBinder binder = new LocalBinder();
    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(EventReloadService.class.getName(), "onCreate");
        timer = new Timer(true);
        timer.schedule(new ReloadDatabaseTask(this), MINUTE, MINUTE);
        Log.d(EventReloadService.class.getName(), "Timer task scheduled");
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
        public EventReloadService getService() {
            return (EventReloadService.this);
        }
    }

}
