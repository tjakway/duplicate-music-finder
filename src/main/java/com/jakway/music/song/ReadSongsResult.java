package com.jakway.music.song;

import com.google.common.collect.Multimap;
import java.io.File;
import java.util.ArrayList;

/**
 * Immutable class used to hold return values of SongReader.readSongs
 * This class guarantees that it will always return null instead of an empty collection
 */
public class ReadSongsResult
{
    private Multimap<RejectionReason, File> rejectedFiles;
    private ArrayList<Song> songs;

    /**
     * @param rejectedFiles
     * @param songs
     */
    public ReadSongsResult(Multimap<RejectionReason, File> rejectedFiles, ArrayList<Song> songs) {
        this.rejectedFiles = rejectedFiles;
        this.songs = songs;

        //never store empty collections!  setting them to null makes it easier to catch some very annoying bugs
        if(this.rejectedFiles.isEmpty())
            this.rejectedFiles = null;
        if(this.songs.isEmpty())
            this.songs = null;
    }

    /**
     * @return the rejectedFiles
     */
    public Multimap<RejectionReason, File> getRejectedFiles() {
        return rejectedFiles;
    }

    /**
     * @return the songs
     */
    public ArrayList<Song> getsongs() {
        return songs;
    }
}
