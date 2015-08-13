package com.jakway.music;

public class Settings
{
    /**
     * length in seconds that 2 music files can differ while still being considered duplicates
     * so if there's a difference of less than e.g. 30 seconds between 2 songs they can still be considered equal
     */
    private static final int DEFAULT_TRACK_COMPARE_LENGTH=30;    
    private static int TRACK_COMPARE_LENGTH=DEFAULT_TRACK_COMPARE_LENGTH;

    public static final int getTrackComparisonLength()
    {
        return TRACK_COMPARE_LENGTH;
    }

}
