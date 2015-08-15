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

    /**
     * Returns true ONLY if title and artist match but albums do not match
     * this method will return true ONLY for a subset of the SongKeys for which equals returns true
     * that is, not every SongKey that is equal is a possible match
     * this method exists to sort the uncertain matches from the certain ones
     */
    public boolean possibleMatch(SongKey otherKey)
    {
        boolean[] matches = getMatchingResults(otherKey);
        assert matches.length == 3;
        
        /**
         * checks (in order):
         * artist
         * title
         * !album
         */
        if(matches[0] && matches[1] && (!matches[2]))
            return true;
        else
            return false;

    }

    /**
     * returns an array of size 3: {artistsMatch, titlesMatch, albumsMatch}
     * if artist or title are null in either song, the artist or title (respectively) are considered not to match
     * if album is null in either key the albums are considered to match
     * This is because there are more duplicate songs with missing album fields than there are duplicate songs missing artist or title info
     */
    private boolean[] getMatchingResults(SongKey otherKey)
    {
        boolean artistsMatch;

        //if the artist or title fields are null, the songs cannot match
        //otherwise we'd get way too many false positives from songs that don't have artist or title data
        if(this.getArtist() == null || otherKey.getArtist() == null)
            artistsMatch = false;
        else
            artistsMatch = DuplicateHandler.stringsFuzzyMatch(this.getArtist(), otherKey.getArtist());

        boolean titlesMatch;
        if(this.getTitle() == null || otherKey.getTitle() == null)
            titlesMatch = false;
        else
            titlesMatch = DuplicateHandler.stringsFuzzyMatch(this.getTitle(), otherKey.getTitle());

        //BUT--album information is more often mangled in the tag.  2 songs can be duplicates even if the albums don't match
        //so if the album field is null in either SongKey, consider the albums matching
        
        boolean albumsMatch;
        if(this.getAlbum() == null || otherKey.getAlbum() == null)
            albumsMatch = true;
        else
            albumsMatch = DuplicateHandler.stringsFuzzyMatch(this.getAlbum(), otherKey.getAlbum());

        return new boolean[] { artistsMatch, titlesMatch, albumsMatch };
    }
}
