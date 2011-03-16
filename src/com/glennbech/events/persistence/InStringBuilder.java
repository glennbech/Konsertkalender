package com.glennbech.events.persistence;

/**
 *
 * This class builds a string in the format (?,?,?, n) for use in SQL queries like "select * from event where
 * location in ('John dee','Rockefeller'). I hate this!: -)
 *
 * @author Glenn Bech
 */
 class InStringBuilder {

    public static String inString(int arguments) {
        if (arguments < 1) {
            throw new IllegalArgumentException("don't give below 1");
        }
        StringBuffer query = new StringBuffer();
        query.append("(");
        for (int i = 0; i < arguments; i++) {
            query.append("?");
            boolean moreElements = i != arguments - 1;
            if (moreElements) {
                query.append(",");
            }
        }
        query.append(")");
        return query.toString();
    }



}

