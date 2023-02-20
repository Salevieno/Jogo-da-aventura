package testing;

import java.awt.Dimension;
import java.awt.Point;

import graphics.Animations;
import liveBeings.Pterodactile;
import main.AtkResults;
import main.AtkTypes;
import utilities.AttackEffects;

public class TestingAnimations
{
	public static void runTests(Animations[] ani)
	{    	
		
		Object[] object1 = new Object[] {100, new Point(200, 300), new Dimension(100, 100), new AtkResults(AtkTypes.magical, AttackEffects.crit, 200), 1} ;
    	ani[1].start(object1) ;
    	
    	Object[] object2 = new Object[] {100, new Point(300, 200), new Dimension(100, 100), new AtkResults(AtkTypes.magical, AttackEffects.crit, 300), 1} ;
    	ani[2].start(object2) ;
    	   	

		Object[] object3 = new Object[] { 200, new String[] {"a", "b", "c"} } ;
    	ani[3].start(object3) ;

		Object[] object4 = new Object[] { 200, new double[] {2, 5, 2, 1, 3, 1}, 1 } ;
		ani[4].start(object4) ;

		Pterodactile.speak(ani[5]) ;
		
	}
}
