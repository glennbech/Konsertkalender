package com.glennbech.events;

/**
 * @author Glenn Bech
 */
public class InStringBuilder {

    public static String inString(int arguments) {
        if (arguments < 1) {
            throw new IllegalArgumentException("don't give below 1");
        }
        StringBuffer query = new StringBuffer();
        query.append("(");
        for (int i = 0; i < arguments; i++) {
            query.append("?");
            if (i != arguments - 1) {
                query.append(",");
            }
        }
        query.append(")");
        return query.toString();
    }

    public static void main(String[] args) {
        System.out.println(inString(0));
    }

}

