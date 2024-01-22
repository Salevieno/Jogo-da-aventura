package graphics ;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import components.AnimationDisplayFunction;
import items.Item;
import liveBeings.Player;
import main.AtkResults;
import utilities.Align;
import utilities.Directions;
import utilities.Log;
import utilities.TimeCounter;
import utilities.UtilG;
import utilities.UtilS;

public class Animation 
{
	private TimeCounter counter ;
	private Object[] vars ;
	private AnimationDisplayFunction displayFunction ;
	
	private static final List<Animation> all = new ArrayList<>() ;
	public static final Image obtainedItem = UtilS.loadImage("ObtainedItem.png") ;

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
		Log.animationStart(ani) ;
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
					Item[] itemsObtained = (Item[]) vars[0] ;
					Draw.winAnimation(counter, itemsObtained) ;
				} ;				
//				
//			case 4 :
//				return (vars, DP) -> {
//					Point playerPos = (Point) vars[0] ;
//					Player.levelUpGif.play(playerPos, Align.bottomCenter, DP) ;
////					if (Player.levelUpGif.isDonePlaying())
////					{
////						Player.levelUpGif.resetTimeCounter() ;
////					}
//				} ;				
//				
//			case 5 :
//				return (vars, DP) -> {
//					Point petPos = (Point) vars[0] ;
//					Pet.levelUpGif.play(petPos, Align.bottomCenter, DP) ;
////					if (Pet.levelUpGif.isDonePlaying())
////					{
////						Pet.levelUpGif.resetTimeCounter() ;
////					}
//				} ;				
//				
//			case 6 :
//				return (vars, DP) -> {
//					double[] attributesInc = (double[]) vars[0] ;
//					int playerLevel = (int) vars[1] ;
//					Color textColor = Game.colorPalette[6] ;
//
//					Draw.levelUpAnimation(counter, attributesInc, playerLevel, textColor) ;
//				} ;				
//				
//			case 7 :
//				return (vars, DP) -> {
//					double[] attributesInc = (double[]) vars[0] ;
//					int playerLevel = (int) vars[1] ;
//					Color textColor = Game.colorPalette[6] ;
//
//					Draw.levelUpAnimation(counter, attributesInc, playerLevel, textColor) ;
//				} ;				
//				
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
					Point fishingPos = UtilG.Translate(playerPos, 0, 0) ;

					switch (playerDir)
					{
						case left:
							fishingPos = UtilG.Translate(playerPos, -Player.FishingGif.size().width, 0) ;
							break ;
							
						case right:
							fishingPos = UtilG.Translate(playerPos, Player.FishingGif.size().width, 0) ;
							break ;
							
						case up:
							fishingPos = UtilG.Translate(playerPos, 0, -Player.FishingGif.size().height) ;
							break ;
							
						case down:
							fishingPos = UtilG.Translate(playerPos, 0, Player.FishingGif.size().height) ;
							break ;
					}

//					Player.FishingGif.start(fishingPos, Align.bottomCenter) ;
//					if (Player.FishingGif.isDonePlaying())
//					{
//						Player.FishingGif.resetTimeCounter() ;
//					}

				} ;				
				
//			case 10 :
//				return (vars, DP) -> {
//					int goldObtained = (int) vars[0] ;
//					Draw.gainGoldAnimation(counter, goldObtained) ;
//				} ;				
//				
//			case 11 :
//				return (vars, DP) -> {
//					Draw.notEnoughGold(counter) ;
//				} ;				
				
			case 4 :
				return (vars, DP) -> {
					Point pos = (Point) vars[0] ;
					String message = (String) vars[1] ;
					Color color = (Color) vars[2] ;
					Draw.quickTextAnimation(pos, counter, message, color) ;
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
