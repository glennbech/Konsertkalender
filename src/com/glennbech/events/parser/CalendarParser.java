package com.glennbech.events.parser;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for parsing a ical celandar into calendar Items.
 */
public class CalendarParser {

    private static final String VEVENT_FIELD_URL = "URL;VALUE=URI:";
    private static final String VEVENT_FIELD_SUMMARY = "SUMMARY:";
    private static final String VEVENT_FIELD_LOCATION = "LOCATION:";
    private static final String VEVENT_FIELD_DATE = "DTSTART;VALUE=DATE:";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private static final String VEVENT_FIELD_UID = "UID:";

    public List<VEvent> parse(InputStream is) throws IOException {

        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        String line;
        VEvent currentEvent = null;
        List<VEvent> events = new ArrayList<VEvent>();

        while ((line = r.readLine()) != null) {
            if (line.indexOf("BEGIN:VEVENT") != -1) {
                currentEvent = new VEvent();
            }
            if (line.indexOf("END:VEVENT") != -1) {
                events.add(currentEvent);
            }

            if (line.indexOf(VEVENT_FIELD_SUMMARY) != -1) {
                int beginIndex = line.indexOf(VEVENT_FIELD_SUMMARY) + +VEVENT_FIELD_SUMMARY.length();
                currentEvent.setSummary(line.substring(beginIndex, line.length()));
            }

            if (line.indexOf(VEVENT_FIELD_LOCATION) != -1) {
                int beginIndex = line.indexOf(VEVENT_FIELD_LOCATION) + +VEVENT_FIELD_LOCATION.length();
                currentEvent.setLocation(line.substring(beginIndex, line.length()));
            }

            if (line.indexOf(VEVENT_FIELD_URL) != -1) {
                int beginIndex = line.indexOf(VEVENT_FIELD_URL) + +VEVENT_FIELD_URL.length();
                currentEvent.setUrl(line.substring(beginIndex, line.length()));
            }

            if (line.indexOf(VEVENT_FIELD_UID) != -1) {
                int beginIndex = line.indexOf(VEVENT_FIELD_UID) + +VEVENT_FIELD_UID.length();
                currentEvent.setUid(line.substring(beginIndex, line.length()));
            }

            if (line.indexOf(VEVENT_FIELD_DATE) != -1) {
                int beginIndex = line.indexOf(VEVENT_FIELD_DATE) + VEVENT_FIELD_DATE.length();
                String substring = line.substring(beginIndex, line.length());
                try {
                    currentEvent.setStartDate(dateFormat.parse(substring));
                } catch (ParseException e) {
                    currentEvent.setStartDate(null);
                }
            }
        }
        return events;
    }
}