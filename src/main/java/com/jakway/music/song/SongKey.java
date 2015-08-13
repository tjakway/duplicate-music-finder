package com.jakway.music.song;

import com.jakway.music.Settings;
import org.jaudiotagger.audio.generic.GenericAudioHeader;

/**
 * This class compares songs to see if they're "duplicates"
 * This is considerably more complex than just testing each field because of typos, varying song quality, etc.
 */
public class SongKey 
{
    private Song song;

    public SongKey(Song song)
    {
        this.song = song;
    }

    public Song getSong()
    {
        return song;
    }

    

    @Override
    public boolean equals(Object other)
    {
        if(!(other instanceof SongKey))
            return false;
        SongKey otherKey = (SongKey) other;

        //compare track lengths
        //if they differ by more than the maximum specified in Settings the files are not considered equal
        int trackLengthDifference = Math.abs(this.song.getAudioHeader().getTrackLength() - otherKey.getSong().getAudioHeader().getTrackLength());
        if(trackLengthDifference > Settings.getTrackComparisonLength())
            return false;

        return true;
    }

}
