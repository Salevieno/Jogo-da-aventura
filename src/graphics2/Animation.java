package graphics2 ;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import battle.AtkResults;
import items.Item;
import main.GameTimer;

public class Animation 
{
	private GameTimer timer ;
	private Object[] vars ;
	private AnimationDisplayFunction displayFunction ;
	
	private static final List<Animation> all = new ArrayList<>() ;

	private Animation(AnimationTypes type)
	{
		this.timer = new GameTimer(type.getDuration()) ;
		this.vars = null ;
		this.displayFunction = displayFunctionFromType(type.ordinal()) ;
	}
	
	public static List<Animation> getAll() { return all ;}
	public GameTimer getTimer() { return timer ;}
	
	public static void start(AnimationTypes type, Object[] vars)
	{
		start(type, type.getDuration(), vars) ;		
	}
	
	public static void start(AnimationTypes type, double duration, Object[] vars)
	{
		Animation ani = new Animation(AnimationTypes.values()[type.ordinal()]) ;
		all.add(ani) ;
		ani.getTimer().setDuration(duration) ;
		ani.vars = vars ;
		ani.getTimer().start() ;
	}
	
	@SuppressWarnings("unchecked")
	private AnimationDisplayFunction displayFunctionFromType(int type)
	{
		switch(type)
		{
			case 0 :
				return (vars) -> {
					Point pos = (Point) vars[0] ;
					int style = (int) vars[1] ;
					AtkResults atkResults = (AtkResults) vars[2] ;
					Color color = (Color) vars[3] ;
					Draw.damageAnimation(pos, atkResults, timer, style, color) ;
				} ;
				
			case 1 :
				return (vars) -> {
					List<Item> itemsObtained = (List<Item>) vars[0] ;
					Draw.winAnimation(timer, itemsObtained) ;
				} ;
				
			case 2 :
				return (vars) -> {
					Image PterodactileImage = (Image) vars[0] ;
					Image SpeakingBubbleImage = (Image) vars[1] ;
					String[] message = (String[]) vars[2] ;
					Draw.pterodactileAnimation(timer, PterodactileImage, SpeakingBubbleImage, message) ;
				} ;
				
			case 4 :
				return (vars) -> {
					double[] attributesInc = (double[]) vars[0] ;
					int playerLevel = (int) vars[1] ;

					Draw.levelUpAnimation(timer, attributesInc, playerLevel) ;
				} ;
				
			case 5 :
				return (vars) -> {
					Point pos = (Point) vars[0] ;
					String message = (String) vars[1] ;
					Color color = (Color) vars[2] ;
					Draw.quickTextAnimation(pos, timer, message, color) ;
				} ;
				
			case 6 :
				return (vars) -> {
					Point pos = (Point) vars[0] ;
					String message = (String) vars[1] ;
					Color color = (Color) vars[2] ;
					Draw.obtainedItemAnimation(pos, timer, message, color) ;
				} ;
				
			case 7 :
				return (vars) -> {
					Point pos = (Point) vars[0] ;
					String message = (String) vars[1] ;
					Color color = (Color) vars[2] ;
					Draw.bufferedTextAnimation(pos, timer, message, color) ;
				} ;
				
			default: return null ;
		}
	}

	public boolean isActive() { return timer.isActive() ;}
	
	public static void playAll()
	{
		for (int i = 0 ; i <= all.size() - 1; i += 1)
		{
			all.get(i).play() ;
		}
	}
	
	private void play()
	{

		if (timer.hasFinished())
		{
			end() ;
			return ;
		}

		displayFunction.act(vars) ;
		
	}
	
	private void end()
	{
		timer.reset() ;
		all.remove(this) ;
	}


}
