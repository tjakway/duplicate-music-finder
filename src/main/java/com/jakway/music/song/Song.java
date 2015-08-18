package com.jakway.music.song;

import com.jakway.music.song.SongKey;

import java.io.File;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.Tag;

public class Song extends AudioFile
{
    SongKey getSongKey()
    {
        return null;
    }

    //subclasses don't inherit constructors
    public Song()
    {
        super();
    }
    public Song(File f, AudioHeader audioHeader, Tag tag)
    {
        super(f, audioHeader, tag);
    }

    public Song(String s, AudioHeader audioHeader, Tag tag)
    {
        super(s, audioHeader, tag);
    }


    

    /**
     * WARNING: equals() MUST BE A DEEP COMPARISON FOR OBJECT EQUALITY, *NOT* A TEST IF THE SONGS MATCH!
     */
}
