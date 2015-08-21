package com.jakway.music.test;

import java.util.Random;



public class RandomUtils 
{
	private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static Random random = new Random(System.currentTimeMillis());
	
	/**
	 * see http://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
	 * @param length
	 * @return
	 */
	public static final String getRandomString(int length)
	{
		   StringBuilder sb = new StringBuilder( length );
		   for( int i = 0; i < length; i++ ) 
		      sb.append( AB.charAt( random.nextInt(AB.length()) ) );
		   return sb.toString();
	}
	
	/**
	 * returns a random string with a length between 1 and 30 characters
	 * internally calls to getRandomString(length)
	 * @param length
	 * @return
	 */
	public static final String getRandomString()
	{
		return getRandomString(getRandomInt(30, 1));
	}
	
	/**
	 * This function uses the formula from 
	 * http://stackoverflow.com/questions/363681/generating-random-number-in-a-range-with-java
	 * Min + (int)(Math.random() * ((Max - Min) + 1))
	 * @param min
	 * @param max
	 * @return
	 */
	public static final int getRandomInt(int min, int max)
	{
		return min + (int)(Math.random()*((max-min) + 1));
	}
	
	/**
	 * see http://stackoverflow.com/questions/6078157/random-nextfloat-is-not-applicable-for-floats
	 * @param min
	 * @param max
	 * @return
	 */
	public static final float getRandomFloat(float min, float max)
	{
		//Random.nextFloat() returns a float between 0.0 and 1.0
		//scale the float over the range
		return random.nextFloat() * (max - min) + min;
	}
	
	/**
	 * splits original on random indices and returns 2 fragments
	 * returns an array of size 2 with copies of the original string
	 * if(!(original.length > 1))
	 * @param original
	 * @return
	 */
	public static final String[] splitInTwo(String original)
	{
		if(!(original.length() > 1))
			return new String[] { original, original };
		
		final int numFragments=2;
		
		String[] strFragments = new String[numFragments];
		if(original.length() > 2)
		{
			//split at a random point in the string
			strFragments[0] = original.substring(0, RandomUtils.getRandomInt(1, original.length() - 1));
				
			//unless the split occurred on the very last character,
			//make the 2nd fragment the remainder of the title
			if(!strFragments[0].equals(original))
			{
				strFragments[1] = original.substring(strFragments.length - 1, original.length() - 1);
			}
			else
				strFragments[1] = original;
		}
		//if the title is only a couple characters don't split it
		//make each fragment the title
		else
		{
			for(int i = 0; i < strFragments.length; i++)
				strFragments[i] = original;
		}
		
		return strFragments;
	}
}
