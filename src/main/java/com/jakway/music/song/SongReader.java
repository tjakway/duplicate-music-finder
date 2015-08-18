package com.jakway.music.song;

import java.io.File;

import com.google.common.io.Files;

/**
 * TODO exclude:
 * -hidden files
 * -files we don't have permission to read (warn user)
 */
public class SongReader
{
    private Multimap<RejectionReason, File> rejectedFiles;
    private ArrayList<File> validFiles;

    public final ReadSongsResult readSongDir(File dir)
    {
        //iterate through every file in this directory
        //see http://stackoverflow.com/questions/14882820/google-guava-iterate-all-files-starting-from-a-begin-directory
        for(File thisFile : Files.fileTreeTraverser().preOrderTraversal(dir))
        {
            //ignore directories
            if(thisFile.isDirectory())
                continue;



        }
    }

}
