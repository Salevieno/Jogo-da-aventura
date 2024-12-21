package testing;

import java.awt.Point;

import graphics.Animation;
import graphics.AnimationTypes;
import items.Item;
import liveBeings.Pterodactile;
import main.AtkResults;
import main.AtkTypes;
import main.Game;
import utilities.AtkEffects;
import utilities.Directions;

public class TestingAnimations
{
	public static void run()
	{    	
		Animation.start(AnimationTypes.damage, new Object[] {new Point(200, 200), 0, new AtkResults(AtkTypes.physical, AtkEffects.hit, 20, null)});
		Animation.start(AnimationTypes.damage, new Object[] {new Point(220, 200), 1, new AtkResults(AtkTypes.physical, AtkEffects.crit, 20, null)});
		Animation.start(AnimationTypes.damage, new Object[] {new Point(250, 200), 1, new AtkResults(AtkTypes.magical, AtkEffects.hit, 20, null)});
		
		Animation.start(AnimationTypes.win, new Object[] {new Item[] {Item.allItems.get(0), Item.allItems.get(1)}} ) ;
		
		Animation.start(AnimationTypes.fishing, new Object[] {new Point(400, 300), Directions.up} ) ;
		
		Animation.start(AnimationTypes.message, new Object[] {new Point(400, 300), "Oi", Game.palette[5]} ) ;
		
		Animation.start(AnimationTypes.obtainedItem, new Object[] {new Point(100, 300), "Oi", Game.palette[5]} ) ;

		Pterodactile.speak() ;
		
	}
}
