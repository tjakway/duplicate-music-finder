package com.jakway.music.song;

/**
 * Represents the reason a song could not be read
 */
public enum RejectionReason
{
    PERMISSIONS, HIDDEN_FILE,
    /** generic value indicating a problem with the file, e.g. does not exist */
    FILE_ERROR,
    INVALID_AUDIO_FILE,

    TAG_ERROR,
    INVALID_AUDIO_FRAME,
    /** catch-all for errors while reading the file */
    READ_ERROR
}
