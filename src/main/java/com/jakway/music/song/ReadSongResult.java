package com.jakway.music.song;

import com.google.common.collect.Multimap;
import java.io.File;
import java.util.ArrayList;

public class ReadSongResult
{
    private Multimap<RejectionReason, File> rejectedFiles;
    private ArrayList<File> validFiles;

    /**
     * @param rejectedFiles
     * @param validFiles
     */
    public ReadSongResult(Multimap<RejectionReason, File> rejectedFiles, ArrayList<File> validFiles) {
        this.rejectedFiles = rejectedFiles;
        this.validFiles = validFiles;
    }

    /**
     * @return the rejectedFiles
     */
    public Multimap<RejectionReason, File> getRejectedFiles() {
        return rejectedFiles;
    }

    /**
     * @return the validFiles
     */
    public ArrayList<File> getValidFiles() {
        return validFiles;
    }

}
