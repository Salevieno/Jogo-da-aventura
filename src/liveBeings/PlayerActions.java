package liveBeings;

import java.util.ArrayList;
import java.util.List;

public enum PlayerActions
{
	moveUp("W"),
	moveLeft("A"),
	moveDown("S"),
	moveRight("D"),
	bag("B"),
	attWindow("C"),
	interact("E"),
	map("M"),
	pet("P"),
	quest("Q"),
	hints("H"),
	ride("R"),
	tent("T"),
	dig("X"),
	bestiary("Z");
	
	String key ;
	
	private PlayerActions(String key)
	{
		this.key = key ;
	}
	
	public String getKey()
	{
		return key ;
	}
	
	public void setKey(String key)
	{
		this.key = key ;
	}
	
	public static PlayerActions actionOfKey(String key)
	{
		for (PlayerActions action : PlayerActions.values())
		{
			if (action.key.equals(key)) { return action ;}
		}
		
		return null ;
	}
	
	public static List<String> keys()
	{
		List<String> keys = new ArrayList<>() ;
		
		for (PlayerActions action : PlayerActions.values())
		{
			keys.add(action.key) ;
		}
		
		return keys ;
	}
}
