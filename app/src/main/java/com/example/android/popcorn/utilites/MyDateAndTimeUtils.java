package com.example.android.popcorn.utilites;

/**
 * Created by Andrew Osborne on 5/1/17.
 *
 */

public class MyDateAndTimeUtils {

    private static final int MINUTES_IN_ONE_HOUR = 60;

    /**
     * Formats a String representing the running time of a movie formatted as:
     * "# hrs ## mins"
     *
     * @param aRuntime int representing the runtime of a particular movie
     * @return
     */
    public static String GetFormattedRuntime(int aRuntime) {

        int hours = aRuntime / MINUTES_IN_ONE_HOUR;
        int minutes = aRuntime % MINUTES_IN_ONE_HOUR;

        String formattedRuntime =
                hours + " hrs " + minutes + " mins";

        return formattedRuntime;
    }
}
