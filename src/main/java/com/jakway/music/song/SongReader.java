package com.jakway.music.song;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jaudiotagger.audio.AudioFileFilter;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.tag.TagException;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.io.Files;

public class SongReader
{
    private ListMultimap<RejectionReason, File> rejectedFiles = ArrayListMultimap.create();

    /** valid files as determined by assembleSongFiles
     *  *NOT* the same as valid songs! */
    private ArrayList<File> validFiles = new ArrayList<File>();

    public ReadSongsResult readSongs(File dir) throws Exception
    {
        //sort into valid and invalid files
        assembleSongFiles(dir);

        ArrayList<Song> songs = new ArrayList<Song>(validFiles.size());

        for(File thisFile : validFiles)
        {
            try {
            //use AudioFileIO to select the appropriate reader for each file type
            songs.add(new Song(AudioFileIO.read(thisFile)));
            }
            catch(TagException e)
            {
                rejectedFiles.put(RejectionReason.TAG_ERROR, thisFile);
            }
            catch(InvalidAudioFrameException e)
            {
                rejectedFiles.put(RejectionReason.INVALID_AUDIO_FRAME, thisFile);
            }
            //don't catch Exception because we want the program to crash if it's
            //some other strange error we didn't anticipate
            catch(IOException e)
            {
                rejectedFiles.put(RejectionReason.READ_ERROR, thisFile);
            }
        }
        
        return new ReadSongsResult(rejectedFiles, songs);
    }

    /**
     * populates rejectedFiles and validFiles
     */
    private void assembleSongFiles(File dir)
    {
        //iterate through every file in this directory
        //see http://stackoverflow.com/questions/14882820/google-guava-iterate-all-files-starting-from-a-begin-directory
        for(File thisFile : Files.fileTreeTraverser().preOrderTraversal(dir))
        {
            //ignore directories
            //this isn't a rejected file because it isn't a "file"
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

            //check that this is a valid audio file
            AudioFileFilter fileChecker = new AudioFileFilter(false); //don't allow directories
            //checks if the file matches certain parameters (e.g. not hidden, permissions OK) and if the extension is one of the supported formats
            //AudioFileFilter.accept repeats a lot of the work done by this method but doesn't give very much information about a problem if one occurs.  It doesn't throw exceptions, it just returns false.
            //we've already checked nearly everything that could go wrong so it's likely a format problem if accept() returns false
            if(!fileChecker.accept(thisFile))
            {
                rejectedFiles.put(RejectionReason.INVALID_AUDIO_FILE, thisFile);
                continue;
            }


            //if we've gotten here the file is OK
            //add it to the list of acceptable files
            validFiles.add(thisFile);
        }
    }
}
