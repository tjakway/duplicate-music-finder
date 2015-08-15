package com.jakway.music.song;

import com.jakway.music.Settings;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.tag.FieldKey;

/**
 * This class compares songs to see if they're "duplicates"
 * This is considerably more complex than just testing each field because of typos, varying song quality, etc.
 */
public class SongKey 
{
    private Song song;
    private String artist, album, title;
    private int year = -1;

    public SongKey(Song song)
    {
        this.song = song;
        Tag tag = song.getTag();
        //some formats don't require tags
        //for the formats that require tags they may still be empty tags
        if(tag != null && !tag.isEmpty())
        {
            artist = tag.getFirst(FieldKey.ARTIST);
            album = tag.getFirst(FieldKey.ALBUM);
            title = tag.getFirst(FieldKey.TITLE);
            try {
            year = Integer.parseInt(tag.getFirst(FieldKey.YEAR));
            } catch(NumberFormatException e)
            {
                //ignore the year field if it isn't well formed
                year = -1;
            }
        }
        //make any empty strings null to make comparisons and debugging easier
        if(artist != null && artist.isEmpty())
            artist = null;
        if(album != null && album.isEmpty())
            album = null;
        if(title != null && title.isEmpty())
            title = null;
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
