package com.jakway.music.utils;

public class Utils
{
    public static final boolean allTrue(boolean[] array)
    {
        for(boolean item : array)
        {
            if(item == false)
                return false;
        }
        return true;
    }
}
