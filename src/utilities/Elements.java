package utilities;

import java.util.Arrays;

public enum Elements
{
	neutral,
	water,
	fire,
	plant,
	earth,
	air,
	thunder,
	light,
	dark,
	snow;
	
	public static int getID(Elements elem)
	{
		return Arrays.asList(Elements.values()).indexOf(elem) ;
	}
}
