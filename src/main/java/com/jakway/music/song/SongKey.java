package com.jakway.music.song;

import com.jakway.music.Settings;
import com.jakway.music.filter.DuplicateHandler;

import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.tag.FieldKey;

/**
 * This class compares songs to see if they're "duplicates"
 * This is considerably more complex than just testing each field because of typos, varying song quality, etc.
 * It's a song "key" this class can be used as a key in a map to sort songs (duplicate songs will have the same key even if the files are not the same)
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

    /**
     * @return the artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * @return the album
     */
    public String getAlbum() {
        return album;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * This test is meant to see if two music files match closely enough to be considered duplicates
     * This is NOT meant to be a strict test for equality
     * songs can be duplicates but the SongKey 
     */
    @Override
    public boolean equals(Object other)
    {
        if(!(other instanceof SongKey))
            return false;
        SongKey otherKey = (SongKey) other;

        /*
         * the maximum levenshtein distance 2 strings can differ by
         * and still be considered equal
         * not used for track length comparison because thats not a string
         */
        final int maxStringDifference = Settings.getMaxLevenshteinDistance();

        final boolean artistsMatch =  DuplicateHandler.stringsFuzzyMatch(this.getArtist(), otherKey.getArtist());
        final boolean albumsMatch = DuplicateHandler.stringsFuzzyMatch(this.getAlbum(), otherKey.getAlbum());
        final boolean titlesMatch = DuplicateHandler.stringsFuzzyMatch(this.getTitle(), otherKey.getTitle());

        if(!artistsMatch || !albumsMatch || !titlesMatch)
        {
            return false;
        }

        //compare track lengths
        //if they differ by more than the maximum specified in Settings the files are not considered equal
        int trackLengthDifference = Math.abs(this.song.getAudioHeader().getTrackLength() - otherKey.getSong().getAudioHeader().getTrackLength());
        if(trackLengthDifference > Settings.getTrackComparisonLength())
            return false;

        return true;
    }
}
