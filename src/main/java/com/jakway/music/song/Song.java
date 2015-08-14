package com.jakway.music.song;

import com.jakway.music.song.SongKey;
import org.jaudiotagger.audio.AudioFile;

public class Song extends AudioFile
{
    SongKey getSongKey()
    {
        return null;
    }

    /**
     * WARNING: equals() MUST BE A DEEP COMPARISON FOR OBJECT EQUALITY, *NOT* A TEST IF THE SONGS MATCH!
     */
}
