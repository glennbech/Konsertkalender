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

    protected static final int MINUTE = 60 * SECOND;
    protected static final int HOUR = MINUTE * 60;
    private IBinder binder = new LocalBinder();
    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(EventService.class.getName(), "onCreate");
        timer = new Timer(true);
        timer.schedule(new ReloadDatabaseTask(this), 60 * MINUTE, 60 * MINUTE);
        Log.d(EventService.class.getName(), "Timer task scheduled");
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
        EventService getService() {
            return (EventService.this);
        }
    }

}
