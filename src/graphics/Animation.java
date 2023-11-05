package graphics ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;

import components.AnimationDisplayFunction;
import items.Item;
import liveBeings.Pet;
import liveBeings.Player;
import main.AtkResults;
import main.Game;
import utilities.Align;
import utilities.Directions;
import utilities.TimeCounter;
import utilities.UtilG;

public class Animation 
{
	private TimeCounter counter ;
	private boolean isActive ;
	private Object[] vars ;
	private AnimationDisplayFunction displayFunction ;
	
	public Animation(int type)
	{
		counter = new TimeCounter(0, 0) ;
		isActive = false ;
		vars = null ;
		displayFunction = displayFunctionFromType(type) ;
	}
	
	public TimeCounter getCounter() { return counter ;}
	public void activate() {isActive = true ;}
	public void start(int duration, Object[] vars)
	{
		isActive = true ;
		this.vars = vars ;
		counter.setDuration(duration) ;
	}
	
	private AnimationDisplayFunction displayFunctionFromType(int type)
	{
		switch(type)
		{
			case 0 :
				return (vars, DP) -> {
					Point targetPos = (Point) vars[0] ;
					Dimension targetSize = (Dimension) vars[1] ;
					AtkResults atkResults = (AtkResults) vars[2] ;
					int style = (int) vars[3] ;
					Color textColor = (Color) vars[4] ;
					Point pos = new Point(targetPos.x, targetPos.y - targetSize.height - 25) ;
					DP.DrawDamageAnimation(pos, atkResults, counter, style, textColor) ;
				} ;
				
			case 1 :
				return (vars, DP) -> {
					Point targetPos = (Point) vars[0] ;
					Dimension targetSize = (Dimension) vars[1] ;
					AtkResults atkResults = (AtkResults) vars[2] ;
					int style = (int) vars[3] ;
					Point pos = new Point(targetPos.x, targetPos.y - targetSize.height - 25) ;
					DP.DrawDamageAnimation(pos, atkResults, counter, style, Game.colorPalette[6]) ;
				} ;
				
			case 2 :
				return (vars, DP) -> {
					Point targetPos = (Point) vars[0] ;
					Dimension targetSize = (Dimension) vars[1] ;
					AtkResults atkResults = (AtkResults) vars[2] ;
					int style = (int) vars[3] ;
					Point pos = new Point(targetPos.x, targetPos.y - targetSize.height - 25) ;
					DP.DrawDamageAnimation(pos, atkResults, counter, style, Game.colorPalette[6]) ;
				} ;				
				
			case 3 :
				return (vars, DP) -> {
					Item[] itemsObtained = (Item[]) vars[0] ;
					DP.winAnimation(counter, itemsObtained) ;
				} ;				
				
			case 4 :
				return (vars, DP) -> {
					Point playerPos = (Point) vars[0] ;
					Player.levelUpGif.play(playerPos, Align.bottomCenter, DP) ;
					if (Player.levelUpGif.isDonePlaying())
					{
						Player.levelUpGif.resetTimeCounter() ;
					}
				} ;				
				
			case 5 :
				return (vars, DP) -> {
					Point petPos = (Point) vars[0] ;
					Pet.levelUpGif.play(petPos, Align.bottomCenter, DP) ;
					if (Pet.levelUpGif.isDonePlaying())
					{
						Pet.levelUpGif.resetTimeCounter() ;
					}
				} ;				
				
			case 6 :
				return (vars, DP) -> {
					double[] attributesInc = (double[]) vars[0] ;
					int playerLevel = (int) vars[1] ;
					Color textColor = Game.colorPalette[6] ;

					DP.levelUpAnimation(counter, attributesInc, playerLevel, textColor) ;
				} ;				
				
			case 7 :
				return (vars, DP) -> {
					double[] attributesInc = (double[]) vars[0] ;
					int playerLevel = (int) vars[1] ;
					Color textColor = Game.colorPalette[6] ;

					DP.levelUpAnimation(counter, attributesInc, playerLevel, textColor) ;
				} ;				
				
			case 8 :
				return (vars, DP) -> {
					Image PterodactileImage = (Image) vars[0] ;
					Image SpeakingBubbleImage = (Image) vars[1] ;
					String[] message = (String[]) vars[2] ;
					DP.PterodactileAnimation(counter, PterodactileImage, SpeakingBubbleImage, message) ;
				} ;				
				
			case 9 :
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

					Player.FishingGif.play(fishingPos, Align.bottomCenter, DP) ;
					if (Player.FishingGif.isDonePlaying())
					{
						Player.FishingGif.resetTimeCounter() ;
					}

				} ;				
				
			case 10 :
				return (vars, DP) -> {
					int goldObtained = (int) vars[0] ;
					DP.gainGoldAnimation(counter, goldObtained) ;
				} ;				
				
			case 11 :
				return (vars, DP) -> {
					DP.notEnoughGold(counter) ;
				} ;				
				
			case 12 :
				return (vars, DP) -> {
					Point pos = (Point) vars[0] ;
					String message = (String) vars[1] ;
					Color color = (Color) vars[2] ;
					DP.quickTextAnimation(pos, counter, message, color) ;
				} ;				
				
			default: return null ;
		}
	}
	
	public void setDisplayFunction(AnimationDisplayFunction displayFunction) { this.displayFunction = displayFunction ;}

	public boolean isActive() { return isActive ;}
	 	
	public void run(DrawingOnPanel DP)
	{	
		
		if (!isActive) { return ;}
		
		if (counter.finished())
		{
			end() ;
			return ;
		}

		displayFunction.act(vars, DP) ;
		counter.inc() ;
		
	}
	
	private void end()
	{
		counter.reset() ;
		isActive = false ;
		Game.getAnimations().remove(this) ;
	}

	@Override
	public String toString()
	{
		return "Animations [counter=" + counter + ", isActive=" + isActive + ", vars=" + Arrays.toString(vars) + "]";
	}

}
