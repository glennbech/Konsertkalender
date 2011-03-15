package com.glennbech.events.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.glennbech.events.parser.VEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Glenn Bech
 */
public class SQLiteEventStore extends SQLiteOpenHelper {

    private static String ddlCreateMessage = "CREATE TABLE IF NOT EXISTS EVENT (id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "uid TEXT," +
            "location TEXT," +
            "summary TEXT," +
            "url TEXT," +
            "isnew INTEGER," +
            "isfavorite INTEGER," +
            "startdate INTEGER," +
            "enddate INTEGER);";


    private static String ddlCreateMessageFavorites = "CREATE TABLE IF NOT EXISTS EVENT_FAVORITE (UID PRIMARY KEY, eventdate INTEGER);";

    private static String ddlCreateMessageReminders = "CREATE TABLE IF NOT EXISTS EVENT_REMINDER (id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "date INTEGER," +
            "uid TEXT," +
            "hourtodeliver integer," +
            "daysinadvance integer);";

    private static final String DATABASE_NAME = "eventually";
    private static final String COLUMN_UID = "uid";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_TITLE = "summary";
    private static final String COLUMN_STARTDATE = "startdate";
    private static final String COLUMN_ENDDATE = "enddate";
    private static final String COLUMN_ISNEW = "isnew";
    private static final String COLUMN_URL = "url";

    private static String TAG = SQLiteEventStore.class.getName();

    public SQLiteEventStore(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ddlCreateMessage);
        sqLiteDatabase.execSQL(ddlCreateMessageFavorites);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void deleteMessage(long id) {
        ContentValues values = new ContentValues();
        values.put("id", id);

        SQLiteDatabase db = getWritableDatabase();
        db.delete("EVENT", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void clear() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("EVENT", null, null);
        db.close();
    }

    public void createEvent(VEvent event) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_UID, event.getUid());
        values.put(COLUMN_LOCATION, event.getLocation());
        values.put(COLUMN_TITLE, event.getSummary());
        long startDate = event.getStartDate() != null ? event.getStartDate().getTime() : -1;
        values.put(COLUMN_STARTDATE, startDate);
        values.put(COLUMN_URL, event.getUrl());
        long endDate = event.getEndDate() != null ? event.getEndDate().getTime() : -1;
        values.put(COLUMN_ENDDATE, endDate);
        values.put(COLUMN_ISNEW, event.isBrandSpankingNew() ? 1 : 0);
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert("EVENT", null, values);
        event.setId(id);
        db.close();
    }

    public List<VEvent> getEvents() {

        String query = "SELECT id, EVENT.uid, location, summary, url, startdate , enddate, isnew, EVENT_FAVORITE.uid as isFavorite " +
                "from EVENT LEFT OUTER JOIN EVENT_FAVORITE " +
                "ON EVENT.uid = EVENT_FAVORITE.uid " +
                "order by startdate asc ";

        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor result = writableDatabase.rawQuery(query, null);
        List<VEvent> messageList = getMessagesFromCursor(result);
        writableDatabase.close();
        Log.d(TAG, messageList.toString());
        return messageList;
    }

    public List<VEvent> search(String searchString) {


        String query = "SELECT id, EVENT.uid, location, summary, url, startdate , enddate, isnew, EVENT_FAVORITE.uid as isFavorite " +
                "from EVENT LEFT OUTER JOIN EVENT_FAVORITE " +
                "ON EVENT.uid = EVENT_FAVORITE.uid " +
                "WHERE EVENT.summary like :0 " +
                "order by startdate asc ";

        searchString = "%" + searchString + "%";
        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor result = writableDatabase.rawQuery(query, new String[]{searchString});

        List<VEvent> messageList = getMessagesFromCursor(result);
        writableDatabase.close();
        return messageList;
    }

    private List<String> getFavoritesFromCursor(Cursor result) {
        List<String> favorites = new ArrayList<String>();
        result.moveToFirst();
        while (!result.isAfterLast()) {
            String uid = result.getString(0);
            favorites.add(uid);
            result.moveToNext();
        }
        return favorites;
    }

    public void setFavorite(String uid, Date startdate, boolean isFavorite) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        if (isFavorite) {
            values.put("uid", uid);
            values.put("eventdate", startdate.getTime());
            db.insert("EVENT_FAVORITE", null, values);

        } else {
            db.delete("EVENT_FAVORITE", "uid = :0", new String[]{uid});
        }
        db.close();
    }

    /**
     * private static String ddlCreateMessageReminders = "CREATE TABLE IF NOT EXISTS EVENT_REMINDER (id INTEGER PRIMARY KEY AUTOINCREMENT," +
     * "date INTEGER," +
     * "uid TEXT," +
     * "hourtodeliver integer," +
     * "daysinadvance integer);";
     */
    public List<Reminder> getReminders() {
        String query = "SELECT id, date, uid, hourtodeliver, daysinadvance FROM EVENT_REMINDER order by startdate asc ";
        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor result = writableDatabase.rawQuery(query, null);
        List<Reminder> messageList = getRemindersFromCursor(result);
        writableDatabase.close();
        return messageList;
    }

    private List<Reminder> getRemindersFromCursor(Cursor result) {

        List<Reminder> reminders = new ArrayList<Reminder>();
        result.moveToFirst();
        while (!result.isAfterLast()) {
            int id = result.getInt(0);
            long date = result.getInt(1);
            String uid = result.getString(2);
            int hourtodeliver = result.getInt(3);
            int daysinadvance = result.getInt(4);
            Reminder r = new Reminder(uid, new Date(date), hourtodeliver, daysinadvance);
            reminders.add(r);
            result.moveToNext();
        }
        return reminders;
    }

    public void createReminder(Reminder r) {
        ContentValues values = new ContentValues();
        values.put("uid", r.getUid());
        values.put("daysinadvance", r.getDaysInAdvance());
        values.put("hourtodeliver", r.getHourToDeliver());
        values.put("date", r.getEventDate().getTime());
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert("EVENT_REMINDER", null, values);
        db.close();
    }

    private List<VEvent> getMessagesFromCursor(Cursor result) {
        List<VEvent> messageList = new ArrayList<VEvent>();
        result.moveToFirst();
        while (!result.isAfterLast()) {
            int id = result.getInt(0);
            String uid = result.getString(1);
            String location = result.getString(2);
            String title = result.getString(3);
            String url = result.getString(4);
            long startdate = result.getLong(5);
            long enddate = result.getLong(6);
            int brandSpankingNew = result.getInt(7);
            String isfavorite = result.getString(8);

            VEvent event = new VEvent(id, new Date(startdate), new Date(enddate), title, location, url, uid);

            event.setBrandSpankingNew(brandSpankingNew == 1);
            event.setFavorite(isfavorite != null);

            messageList.add(event);
            result.moveToNext();
        }
        result.close();
        return messageList;
    }

    public List<VEvent> getFavorites() {
        String query = "SELECT id, EVENT.uid, location, summary, url, startdate , enddate, isnew, EVENT_FAVORITE.uid as isFavorite " +
                "from EVENT LEFT OUTER JOIN EVENT_FAVORITE " +
                "ON EVENT.uid = EVENT_FAVORITE.uid " +
                "WHERE EVENT_FAVORITE.uid is not null " +
                "order by startdate asc ";

        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor result = writableDatabase.rawQuery(query, null);
        List<VEvent> messageList = getMessagesFromCursor(result);
        writableDatabase.close();
        return messageList;
    }

    public List<VEvent> getNewItems() {
        String query = "SELECT id, uid, location, summary, url, startdate , enddate, isnew, isfavorite from EVENT where isnew = 1 order by startdate asc ";
        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor result = writableDatabase.rawQuery(query, null);
        List<VEvent> messageList = getMessagesFromCursor(result);
        writableDatabase.close();
        return messageList;
    }

    public void createEvents(List<VEvent> allEvents) {

        for (VEvent e : allEvents) {
            if (e.getStartDate().getTime() > System.currentTimeMillis()) {
                continue;
            }
            createEvent(e);
        }


    }
}

