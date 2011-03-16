package com.glennbech.events;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;

import static com.glennbech.events.TimerConsts.SECOND;

/**
 * Service that schedules the Database Reload task.
 */

public class EventService extends Service {

    private static final int MINUTE = 60 * SECOND;
    private final IBinder binder = new LocalBinder();
    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(EventService.class.getName(), "onCreate");
        timer = new Timer(true);
        timer.schedule(new ReloadDatabaseTask(this), 60 * MINUTE,  MINUTE);
        Log.d(EventService.class.getName(), "Timer task scheduled");
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
        EventService getService() {
            return (EventService.this);
        }
    }

}
