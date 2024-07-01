package graphics ;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import components.AnimationDisplayFunction;
import items.Item;
import libUtil.Util;
import liveBeings.Player;
import main.AtkResults;
import utilities.Directions;
import utilities.TimeCounter;
import utilities.UtilS;

public class Animation 
{
	private TimeCounter counter ;
	private Object[] vars ;
	private AnimationDisplayFunction displayFunction ;
	
	private static final List<Animation> all = new ArrayList<>() ;
	public static final Image obtainedItem = UtilS.loadImage("ObtainedItem.png") ;
	public static final Image messageBox = UtilS.loadImage("messageBox.png") ;
	public static final Image levelUp = UtilS.loadImage("LevelUp.png") ;
	public static final Image win = UtilS.loadImage("Win.png") ;

	private Animation(AnimationTypes type)
	{
		this.counter = new TimeCounter(type.getDuration()) ;
		this.vars = null ;
		this.displayFunction = displayFunctionFromType(type.ordinal()) ;
	}
	
	public List<Animation> getAll() { return all ;}
	public TimeCounter getCounter() { return counter ;}
	
	public static void start(AnimationTypes type, Object[] vars)
	{
		start(type, type.getDuration(), vars) ;		
	}
	
	public static void start(AnimationTypes type, double duration, Object[] vars)
	{
		Animation ani = new Animation(AnimationTypes.values()[type.ordinal()]) ;
		all.add(ani) ;
		ani.getCounter().setDuration(duration) ;
		ani.vars = vars ;
		ani.getCounter().start() ;
//		Log.animationStart(ani) ;
	}
	
	private AnimationDisplayFunction displayFunctionFromType(int type)
	{
		switch(type)
		{
			case 0 :
				return (vars, DP) -> {
					Point pos = (Point) vars[0] ;
					int style = (int) vars[1] ;
					AtkResults atkResults = (AtkResults) vars[2] ;
					Draw.damageAnimation(pos, atkResults, counter, style) ;
				} ;
				
			case 1 :
				return (vars, DP) -> {
					List<Item> itemsObtained = (List<Item>) vars[0] ;
					Draw.winAnimation(counter, itemsObtained) ;
				} ;
				
			case 2 :
				return (vars, DP) -> {
					Image PterodactileImage = (Image) vars[0] ;
					Image SpeakingBubbleImage = (Image) vars[1] ;
					String[] message = (String[]) vars[2] ;
					Draw.pterodactileAnimation(counter, PterodactileImage, SpeakingBubbleImage, message) ;
				} ;				
				
			case 3 :
				return (vars, DP) -> {
					Point playerPos = (Point) vars[0] ;
					Directions playerDir = (Directions) vars[1] ;
					Point fishingPos = Util.Translate(playerPos, 0, 0) ;

					switch (playerDir)
					{
						case left:
							fishingPos = Util.Translate(playerPos, -Player.FishingGif.size().width, 0) ;
							break ;
							
						case right:
							fishingPos = Util.Translate(playerPos, Player.FishingGif.size().width, 0) ;
							break ;
							
						case up:
							fishingPos = Util.Translate(playerPos, 0, -Player.FishingGif.size().height) ;
							break ;
							
						case down:
							fishingPos = Util.Translate(playerPos, 0, Player.FishingGif.size().height) ;
							break ;
					}

				} ;
				
			case 4 :
				return (vars, DP) -> {
					double[] attributesInc = (double[]) vars[0] ;
					int playerLevel = (int) vars[1] ;

					Draw.levelUpAnimation(counter, attributesInc, playerLevel) ;
				} ;	
				
			case 5 :
				return (vars, DP) -> {
					Point pos = (Point) vars[0] ;
					String message = (String) vars[1] ;
					Color color = (Color) vars[2] ;
					Draw.quickTextAnimation(pos, counter, message, color) ;
				} ;	
				
			case 6 :
				return (vars, DP) -> {
					Point pos = (Point) vars[0] ;
					String message = (String) vars[1] ;
					Color color = (Color) vars[2] ;
					Draw.obtainedItemAnimation(pos, counter, message, color) ;
				} ;			
				
			default: return null ;
		}
	}

	public boolean isActive() { return counter.isActive() ;}
	
	public static void playAll(DrawPrimitives DP)
	{
		for (int i = 0 ; i <= all.size() - 1; i += 1)
		{
			all.get(i).play(DP) ;
		}
	}
	
	private void play(DrawPrimitives DP)
	{

		if (counter.finished())
		{
			end() ;
			return ;
		}

		displayFunction.act(vars, DP) ;
		
	}
	
	private void end()
	{
		counter.reset() ;
		all.remove(this) ;
	}


}
