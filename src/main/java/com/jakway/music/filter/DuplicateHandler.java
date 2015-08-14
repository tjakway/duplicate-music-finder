package com.jakway.music.filter;

import com.jakway.music.Settings;
import org.apache.commons.lang3.StringUtils;

public class DuplicateHandler
{

    /**
     * TODO: implement
     * This function tests if the strings are similar ("fuzzy matching")--use the Levenshtein distance functions from Apache Common's String Utils
     */
    private static final boolean stringsFuzzyMatch(String first, String second)
    {
        //this function returns -1 if the levenshtein distance > threshold
        //otherwise it returns the distance
        //we don't care about the distance so long as its smaller than the maximum amount to consider the strings similar
        int threshold = Settings.getMaxLevenshteinDistance();
        int distance = StringUtils.getLevenshteinDistance(first, second, threshold);
        if(distance == -1)
            return false;
        else
            return true;

    }
}
