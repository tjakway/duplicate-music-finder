package com.jakway.music.song;

import java.io.File;
import java.util.ArrayList;

import com.google.common.collect.ListMultimap;
import com.google.common.io.Files;

/**
 * TODO exclude:
 * -hidden files
 * -files we don't have permission to read (warn user)
 */
public class SongReader
{
    private ListMultimap<RejectionReason, File> rejectedFiles;
    private ArrayList<File> validFiles;

    public ReadSongsResult readSongs(File dir)
    {
        //TODO: IMPLEMENT
        return null;
    }

    private void assembleSongFiles(File dir)
    {
        //iterate through every file in this directory
        //see http://stackoverflow.com/questions/14882820/google-guava-iterate-all-files-starting-from-a-begin-directory
        for(File thisFile : Files.fileTreeTraverser().preOrderTraversal(dir))
        {
            //ignore directories
            if(thisFile.isDirectory())
                continue;

            //rejected files should be added to the rejectedFiles multimap with the correct RejectionReason.  DO NOT add them to validFiles
            //don't forget to continue() to the 

            //ignore hidden files
            if(thisFile.isHidden())
            {

                rejectedFiles.put(RejectionReason.HIDDEN_FILE, thisFile);
                continue;
            }

            //check if the file doesn't exist or is empty
            if(!thisFile.exists() || thisFile.length() < 1)
            {
                rejectedFiles.put(RejectionReason.FILE_ERROR, thisFile);
                continue;
            }

            //we need to be able to read and write the file (so we can delete it if it's a duplicate)
            if(!thisFile.canRead() || !thisFile.canWrite())
            {
                rejectedFiles.put(RejectionReason.PERMISSIONS, thisFile);
                continue;
            }
        }
    }

}
